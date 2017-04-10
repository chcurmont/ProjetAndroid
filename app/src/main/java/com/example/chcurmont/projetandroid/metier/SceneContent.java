package com.example.chcurmont.projetandroid.metier;

import com.example.chcurmont.projetandroid.modele.DubblingFactory;
import com.example.chcurmont.projetandroid.modele.SceneFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Charly on 02/04/2017.
 */

public class SceneContent {
    public static final List<Scene> ITEMS = new ArrayList<Scene>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            try {
                addItem(createSceneItem(i));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static Scene createSceneItem(int i) throws IOException {
        SceneFactory sf = new SceneFactory();
        return sf.creerScene(((Integer) i).toString());
    }

    private static void addItem(Scene s){
        ITEMS.add(s);
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }
}
