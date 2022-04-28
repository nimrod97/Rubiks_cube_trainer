package com.example.rubikscubetrainer.db;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rubikscubetrainer.R;

public class StatsActivity extends AppCompatActivity {
    private Button myGames;
    private Button otherGames;
    private Button mostPlay;
    private Button solvedByMyself;
    private Button toughestCubes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        myGames = findViewById(R.id.myLastGame);
        otherGames = findViewById(R.id.gamesOfOthers);
        mostPlay = findViewById(R.id.mostPlaying);
        solvedByMyself = findViewById(R.id.solved_myself);
        toughestCubes = findViewById(R.id.toughest_cubes);
        myGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatsActivity.this, MyLastGames.class);
                startActivity(intent);
            }
        });
        otherGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatsActivity.this, LastGamesOfOthers.class);
                startActivity(intent);
            }
        });
        mostPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatsActivity.this, MostPlay.class);
                startActivity(intent);
            }
        });
        solvedByMyself.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatsActivity.this, SolvedByMyself.class);
                startActivity(intent);
            }
        });
        toughestCubes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatsActivity.this, ToughestCubes.class);
                startActivity(intent);
            }
        });
    }
}