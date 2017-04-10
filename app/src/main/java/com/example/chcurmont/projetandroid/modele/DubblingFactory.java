package com.example.chcurmont.projetandroid.modele;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.chcurmont.projetandroid.MainActivity;
import com.example.chcurmont.projetandroid.R;
import com.example.chcurmont.projetandroid.controller.UserController;
import com.example.chcurmont.projetandroid.metier.Doublage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

/**
 * Created by Charly on 02/04/2017.
 */

public class DubblingFactory {
    private static final String TAG = DubblingFactory.class.getSimpleName();
    public DubblingFactory(){

    }

    public Doublage creerDoublage(String nom) throws IOException {
        File vid = loadDubbling(nom);
        File pic = loadDubblingPic(nom);
        if(vid==null || pic == null)
            return null;
        else
            return new Doublage(nom, vid, pic);
    }
    private File loadDubbling(String nom) throws IOException {
        File localFile = File.createTempFile("dubbling","mp4");
        StorageReference riversRef = UserController.getUserController().getmStorageRef().child("Dubbling/"+nom+".mp4");

        riversRef.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG,"téléchargement réussi.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG,"Téléchargement échoué.");
                    }
                });
        return localFile;
    }

    private File loadDubblingPic(String nom) throws IOException {
        File localFile = File.createTempFile("dubblingPic","jpg");
        StorageReference riversRef = UserController.getUserController().getmStorageRef().child("DubblingPic/"+nom+".jpg");

        riversRef.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG,"téléchargement réussi.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG,"Téléchargement échoué.");
                    }
                });
        return localFile;
    }
}
