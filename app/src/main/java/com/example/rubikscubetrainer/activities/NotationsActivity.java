package com.example.rubikscubetrainer.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rubikscubetrainer.R;

// This activity is responsible for displaying to the users the notations of the cube
// in case of solving. The suitable layout for it is the 'activity_notations'

public class NotationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notations);
    }
}