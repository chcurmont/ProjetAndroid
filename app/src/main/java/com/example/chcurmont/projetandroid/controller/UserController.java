package com.example.chcurmont.projetandroid.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.example.chcurmont.projetandroid.ErrorActivity;
import com.example.chcurmont.projetandroid.MainActivity;
import com.example.chcurmont.projetandroid.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.SocketException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chcurmont on 14/02/17.
 */
public class UserController {
    public final static String EXTRA_MESSAGE = "com.example.chcurmont.projetandroid.MESSAGE";
    private static UserController instance;
    private static final String TAG = UserController.class.getSimpleName();
    private FirebaseDatabase database;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private String name;

    private UserController(){
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        mStorageRef = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance();
    }

    public String getUser(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            name = user.getDisplayName();
            if(name==null || name == ""){
                name = user.getEmail();
            }
            return name;
        }
        return null;
    }

    public static UserController getUserController(){
        if(instance==null){
            instance = new UserController();
        }
        return instance;
    }

    public StorageReference getmStorageRef(){
        return mStorageRef;
    }

    public FirebaseAuth getmAuth(){
        return mAuth;
    }

    public Task<AuthResult> createAccount(String email, String password) throws Exception {

        Pattern p = Patterns.EMAIL_ADDRESS;
        Matcher m = p.matcher(email);
        if (!m.matches()) {
            throw new Exception("Invalid Email");
        }

        p = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$");
        m = p.matcher(password);
        if (!m.matches()) {
            throw new Exception("Invalid Password");
        }

        return mAuth.createUserWithEmailAndPassword(email, password);
    }

    public Task<AuthResult> signIn(String email, String password) throws Exception {
        Pattern p = Patterns.EMAIL_ADDRESS;
        Matcher m = p.matcher(email);
        if (!m.matches()) {
            throw new Exception("Invalid Email");
        }

        p = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$");
        m = p.matcher(password);
        if (!m.matches()) {
            throw new Exception("Invalid Password");
        }
        getUser();

        return mAuth.signInWithEmailAndPassword(email, password);
    }

    public Boolean isSignedIn(){
        getUser();
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    public void write(String key,String value){
        DatabaseReference myRef = database.getReference(key);
        myRef.setValue(value);
    }

    public void write(String key, int value){
        DatabaseReference myRef = database.getReference(key);
        myRef.setValue(value);
    }

    public DatabaseReference read(String key){
        DatabaseReference myRef = database.getReference(key);
        return myRef;
    }

    public void onStop(){
        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void onStart(){
        mAuth.addAuthStateListener(mAuthListener);
    }
}
