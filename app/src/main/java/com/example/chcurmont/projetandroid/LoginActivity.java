package com.example.chcurmont.projetandroid;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chcurmont.projetandroid.controller.UserController;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    public final static String EXTRA_MESSAGE = "com.example.chcurmont.projetandroid.MESSAGE";
    EditText email;
    EditText password;
    private Boolean firstClick = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = (EditText) findViewById(R.id.loginEditText);
        password = (EditText) findViewById(R.id.passwordText);
        email.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                Boolean retour = false;
                if(i == EditorInfo.IME_ACTION_NEXT){

                    password.requestFocus();
                    retour = true;
                }
                return retour;
            }
        });
        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                Boolean retour = false;
                if(i == EditorInfo.IME_ACTION_DONE){
                    connection(null);
                    retour = true;
                }
                return retour;
            }
        });

    }

    public void connection(View view) {
        UserController c = UserController.getUserController();
        try{
            c.signIn(email.getText().toString(), password.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.d(TAG,"signInWithEmail:onComplete:" + task.isSuccessful());

                    if(!task.isSuccessful()){
                        Log.w(TAG,"signInWithEmail:failed", task.getException());
                        Toast.makeText(LoginActivity.this,R.string.auth_failed,Toast.LENGTH_SHORT).show();
                    }
                }
            });
            this.finish();
        }
        catch(Exception e){
            Intent errorIntent = new Intent(this,ErrorActivity.class);
            errorIntent.putExtra(EXTRA_MESSAGE, e.getMessage());
            startActivity(errorIntent);
        }
    }
}
