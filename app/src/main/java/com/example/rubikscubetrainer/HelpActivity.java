package com.example.rubikscubetrainer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HelpActivity extends AppCompatActivity {
    private Button solvingMethods;
    private Button notations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        solvingMethods = findViewById(R.id.solvingMethods);
        notations = findViewById(R.id.notations);
        solvingMethods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HelpActivity.this, SolvingMethodsExplanation.class);
                startActivity(intent);
            }
        });
        notations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HelpActivity.this, NotationsActivity.class);
                startActivity(intent);
            }
        });
    }
}
