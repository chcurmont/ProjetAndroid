package com.example.chcurmont.projetandroid.modele;

import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.chcurmont.projetandroid.MainActivity;
import com.example.chcurmont.projetandroid.R;
import com.example.chcurmont.projetandroid.controller.UserController;
import com.example.chcurmont.projetandroid.metier.Scene;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

/**
 * Created by Charly on 07/03/2017.
 */

public class SceneFactory {
    private static final String TAG = SceneFactory.class.getSimpleName();
    public SceneFactory(){

    }
    public Scene creerScene(String nom) throws IOException {
        File vid = loadScene(nom);
        File pic = loadScenePic(nom);
        if(vid==null || pic == null)
            return null;
        else
            return new Scene(vid,pic,nom);
    }

    public File loadScene(String nom) throws IOException {
        File localFile = File.createTempFile("scene","mp4");
        StorageReference riversRef = UserController.getUserController().getmStorageRef().child("Scenes/"+nom+".mp4");

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
                        Log.e(TAG,"téléchargement échoué.");
                    }
                });
        return localFile;
    }

    public File loadScenePic(String nom) throws IOException {
        File localFile = File.createTempFile("scenePic","jpg");
        StorageReference riversRef = UserController.getUserController().getmStorageRef().child("ScenesPic/"+nom+".jpg");

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
                        Log.e(TAG,"Télechargement échoué.");
                    }
                });
        return localFile;
    }
}