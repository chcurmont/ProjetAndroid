package com.example.chcurmont.projetandroid.metier;

import com.example.chcurmont.projetandroid.modele.DubblingFactory;
import com.example.chcurmont.projetandroid.modele.SceneFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class DoublageContent {
    public static final List<Doublage> ITEMS = new ArrayList<Doublage>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            try {
                addItem(createDoublageItem(i));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static Doublage createDoublageItem(int i) throws IOException {
        DubblingFactory df = new DubblingFactory();
        return df.creerDoublage(((Integer) i).toString());
    }

    private static void addItem(Doublage d){
        ITEMS.add(d);
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
