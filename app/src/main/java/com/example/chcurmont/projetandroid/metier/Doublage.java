package com.example.chcurmont.projetandroid.metier;

import android.provider.MediaStore;

import java.io.File;

/**
 * Created by Charly on 03/03/2017.
 */

public class Doublage {
    private String id;
    private File video;
    private File pic;

    public Doublage(String id, File video, File pic){
        this.id = id;
        this.video = video;
        this.pic = pic;
    }

    public String getId() {
        return id;
    }
    public File getVideo(){
        return video;
    }
    public File getPic(){return pic;}

    public String toString(){return "Dubbling "+id;}

}
