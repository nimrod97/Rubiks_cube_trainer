package com.example.rubikscubetrainer.db;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.example.rubikscubetrainer.GLView;
import com.example.rubikscubetrainer.R;

public class ScannedCubeFromDB extends FragmentActivity {
    private GLView glview;
    public static String[] colors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_cube_from_db);
        glview = findViewById(R.id.glview);
        colors = getIntent().getStringArrayExtra("colors");
    }
}