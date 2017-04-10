package com.example.chcurmont.projetandroid;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.chcurmont.projetandroid.controller.UserController;
import com.example.chcurmont.projetandroid.metier.Doublage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StreamDownloadTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

    public class MainActivity extends AppCompatActivity implements DubblingFragment.OnFragmentInteractionListener, SceneFragment.OnFragmentInteractionListener, UserProfileFragment.OnFragmentInteractionListener, BottomBarFragment.OnFragmentInteractionListener, LoginOrSignInFragment.OnFragmentInteractionListener{

        private static final String TAG = MainActivity.class.getSimpleName();
        private Fragment bottomButtonBarFragment;
        private Fragment actualFragment;
        private int position;
        private Integer numberDubbling;
        private Integer numberScene;
        private Doublage lastDubbling;
        private BottomNavigationView mBottomButton;
        private UserController mController;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            bottomButtonBarFragment = getSupportFragmentManager().findFragmentById(R.id.bottomButtonBarFragment);
            position = 0;

            actualFragment = new SceneFragment();
            Bundle args = new Bundle();
            //remplir le bundle ici
            actualFragment.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container,actualFragment);
            transaction.addToBackStack(null);
            transaction.commit();

            mBottomButton = (BottomNavigationView) findViewById(R.id.navigation);
            mBottomButton.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch(item.getItemId()){
                        case R.id.action_scene:
                            scenes(null);
                            break;
                        case R.id.action_dubbling:
                            dubbling(null);
                            break;
                        case R.id.action_profile:
                            profile(null);
                            break;
                    }
                    return true;
                }
            });

            mController = UserController.getUserController();

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA)||
                        ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.RECORD_AUDIO) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ) {

                } else {

                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},3);


                }
            }
            int permissionCheck = ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA);
        }

        public void setNumberDubbling(int number){
            numberDubbling=number;
            mController.write("numberDubbling",numberDubbling);
        }

        public void saveDubbling(String path,Doublage dubbling){
            lastDubbling = dubbling;
            Uri file = Uri.fromFile(new File(path));
            StorageReference riversRef = mController.getmStorageRef().child("Dubbling/"+path.split("/")[path.split("/").length-1]);

            riversRef.putFile(file)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests") Uri downloadUri = taskSnapshot.getDownloadUrl();
                            setNumberDubbling(numberDubbling++);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, R.string.upload_fail, Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        @Override
        public void onStart(){
            super.onStart();
            mController.onStart();
        }

        @Override
        public void onStop(){
            super.onStop();
            mController.onStop();
        }

        public void profile(View view){
            if(mController.isSignedIn()) {
                UserProfileFragment newFragment = new UserProfileFragment();
                Bundle args = new Bundle();
                //remplir le bundle ici
                newFragment.setArguments(args);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(actualFragment.getId(),newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                actualFragment = newFragment;
            }
            else{
                LoginOrSignInFragment newFragment = new LoginOrSignInFragment();
                Bundle args = new Bundle();
                args.putInt(LoginOrSignInFragment.ARG_POSITION,position);
                args.putInt(LoginOrSignInFragment.ARG_PARAM1,R.id.bottomButtonBarFragment);
                newFragment.setArguments(args);

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                transaction.replace(actualFragment.getId(),newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                actualFragment = newFragment;
            }
        }

        public void dubbling(View view){
            if(actualFragment.getClass() == DubblingFragment.class) return;

            DubblingFragment newFragment = new DubblingFragment();
            Bundle args = new Bundle();
            //remplir le bundle ici
            newFragment.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(actualFragment.getId(),newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
            actualFragment = newFragment;
        }

        public void scenes(View view){
            if(actualFragment.getClass() == SceneFragment.class) return;

            SceneFragment newFragment = new SceneFragment();
            Bundle args = new Bundle();
            //remplir le bundle ici
            newFragment.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(actualFragment.getId(),newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
            actualFragment = newFragment;
        }
        public void login(View view){
            Intent loginIntent = new Intent(this,LoginActivity.class);
            startActivity(loginIntent);
        }
        public void signin(View view){
            Intent signinIntent = new Intent(this,RegisterActivity.class);
            startActivity(signinIntent);
        }

        @Override
        public void onFragmentInteraction(Uri uri) {

        }
    }
