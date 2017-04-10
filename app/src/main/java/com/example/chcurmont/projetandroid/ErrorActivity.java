package com.example.chcurmont.projetandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.chcurmont.projetandroid.controller.UserController;

public class ErrorActivity extends AppCompatActivity {
    String error;
    TextView errorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);

        Intent intent = getIntent();
        String erreur = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE);
        errorView = (TextView) findViewById(R.id.ErrorTextView);
        errorView.setText(erreur);
    }

}
