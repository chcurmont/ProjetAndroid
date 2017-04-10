package com.example.chcurmont.projetandroid.metier;

import android.provider.ContactsContract;
import android.provider.MediaStore;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.File;

/**
 * Created by chcurmont on 28/02/17.
 */
public class Scene {
    private File video;
    private File videoPic;
    private String id;


    public Scene(File video, File videoPic, String id){
        this.video = video;
        this.videoPic = videoPic;
        this.id = id;
    }

    public String getId() { return id; }

    public File getVideo() {
        return video;
    }

    public File getVideoPic(){return videoPic;}

    @Override
    public String toString(){
        return "Scene "+id;
    }
}