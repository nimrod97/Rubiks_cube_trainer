package com.example.rubikscubetrainer.db;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rubikscubetrainer.R;

public class SolvingMethodsList extends AppCompatActivity {
    private Button beginner;
    private Button cfop;
    private Button kociemba;
    private TextView text;
    public static String chosenMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solving_methods_list);
        beginner = findViewById(R.id.beginner);
        cfop = findViewById(R.id.cfop);
        kociemba = findViewById(R.id.kociemba);
        text = findViewById(R.id.message);
        beginner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenMethod = "Beginner";
                Intent intent = new Intent(SolvingMethodsList.this, ToughestCubes.class);
                startActivity(intent);
            }
        });
        cfop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenMethod = "CFOP";
                Intent intent = new Intent(SolvingMethodsList.this, ToughestCubes.class);
                startActivity(intent);
            }
        });
        kociemba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenMethod = "Kociemba";
                Intent intent = new Intent(SolvingMethodsList.this, ToughestCubes.class);
                startActivity(intent);
            }
        });
    }

}