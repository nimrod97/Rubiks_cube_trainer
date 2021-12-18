package com.example.rubikscubetrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class PlayingOptionsActivity extends AppCompatActivity {
    private Button scan;
    private Button play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_options);
        scan = findViewById(R.id.scan_button);
        play = findViewById(R.id.play_in_the_app_button);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlayingOptionsActivity.this, ScanningActivity.class);
                startActivity(intent);
//                finish();
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlayingOptionsActivity.this, PlayInTheAppActivity.class);
                startActivity(intent);
//                finish();
            }
        });


    }
}
