package com.example.chcurmont.projetandroid;

import android.app.Activity;
import android.app.ProgressDialog;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.chcurmont.projetandroid.modele.CameraMethod;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;


import static com.example.chcurmont.projetandroid.R.id.button_video;
import static com.example.chcurmont.projetandroid.R.id.camera_preview;
import static com.example.chcurmont.projetandroid.modele.CameraMethod.getCameraInstance;
import static com.example.chcurmont.projetandroid.modele.CameraMethod.getOutputMediaFile;

//import android.hardware.Camera;

/**
 * Created by yadenguir on 17/03/17.
 */
public class CameraActivity extends Activity {

    private Camera mCamera;
    private CameraPreview mPreview;
    private String TAG;
    private EOFException e;
    private MediaRecorder mMediaRecorder;
    private boolean isRecording = false;
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    public static int compteurCover = 1;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cameraview);

        // Create an instance of Camera
        mCamera = getCameraInstance();

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        mPreview.setCameraDisplayOrientation();
        FrameLayout preview = (FrameLayout) findViewById(camera_preview);

        preview.addView(mPreview);



        final Button buttonVideo = (Button) findViewById(button_video);
        buttonVideo.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // isRecording= prepareVideoRecorder();
                        if (isRecording) {
                            // stop recording and release camera
                            mMediaRecorder.stop();  // stop the recording
                            //releaseMediaRecorder(); // release the MediaRecorder object
                            mCamera.lock();         // take camera access back from MediaRecorder

                            // inform the user that recording has stopped
                            buttonVideo.setText("Video :stop");
                            isRecording = false;
                            uploadfile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Cover"));
                        } else {
                            // initialize video camera
                            if (prepareVideoRecorder()) {
                                // Camera is available and unlocked, MediaRecorder is prepared,
                                // now you can start recording
                                mMediaRecorder.start();

                                // inform the user that recording has started
                                buttonVideo.setText("Video :start");
                                isRecording = true;
                            } else {

                                releaseMediaRecorder();
                            }
                        }
                    }
                }
        );
    }

    private void uploadfile(File file){
        Date date = new Date();
        StorageReference riversRef = mStorageRef.child("Dumbling/cover"+date.getTime()+".mp4");
        final ProgressDialog progressdialog= new ProgressDialog(this);
        progressdialog.setTitle("Uploading...");
        progressdialog.show();

        File fichier = new File( file.getPath() + File.separator +
                "COVER_"+ compteurCover+ ".mp4");
        riversRef.putFile(Uri.fromFile(fichier))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressdialog.hide();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        progressdialog.setTitle("Erreur...");


                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progess = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                progressdialog.setMessage(((int) progess)+"% Uploaded...");
            }
        })
        ;
        compteurCover++;

    }

    private boolean prepareVideoRecorder() {


        mMediaRecorder = new MediaRecorder();


        // Step 1: Unlock and set camera to MediaRecorder
        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);

        // Step 2: Set sources
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));

        // Step 4: Set output file

        mMediaRecorder.setOutputFile(getOutputMediaFile(CameraMethod.MEDIA_TYPE_VIDEO).toString());


        // Step 5: Set the preview output
        mMediaRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());

        // Step 6: Prepare configured MediaRecorder
        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            Log.d(TAG, "IllegalStateException preparing MediaRecorder: ");
            return false;
        } catch (IOException e) {
            Log.d(TAG, "IOException preparing MediaRecorder: ");
            return false;
        }
        return true;
    }

    private void releaseMediaRecorder() {
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();   // clear recorder configuration
            mMediaRecorder.release(); // release the recorder object
            mMediaRecorder = null;
            mCamera.lock();           // lock camera for later use
        }
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}