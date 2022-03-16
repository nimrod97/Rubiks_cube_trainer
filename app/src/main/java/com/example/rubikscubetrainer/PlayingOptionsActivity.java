package com.example.rubikscubetrainer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class PlayingOptionsActivity extends AppCompatActivity {
    private Button scan;
    private Button play;
    private String username;
    private TextView text;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_options);
        scan = findViewById(R.id.scan_button);
        play = findViewById(R.id.play_in_the_app_button);
        username = getIntent().getStringExtra("username");
        text = findViewById(R.id.username);
        text.setText("Welcome, " + username + "!");
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlayingOptionsActivity.this, ScanningActivity.class);
                startActivity(intent);
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlayingOptionsActivity.this, CubeGLActivity.class);
                startActivity(intent);
            }
        });


    }
}
