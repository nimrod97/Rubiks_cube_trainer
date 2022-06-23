package com.example.rubikscubetrainer.db;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.example.rubikscubetrainer.GLView;
import com.example.rubikscubetrainer.R;

// This activity is responsible for displaying the cube in the order that it is written in the db.
// the cube representation is saved as string of colors in the db.
//The suitable layout for it is the 'activity_cube_from_db'

public class CubeFromDB extends FragmentActivity {
    private GLView glview;
    public static String[] colors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cube_from_db);
        glview = findViewById(R.id.glview);
        colors = getIntent().getStringArrayExtra("colors");
    }
}