package com.hle.notepad;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class AboutThisApp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_this_app);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }
}