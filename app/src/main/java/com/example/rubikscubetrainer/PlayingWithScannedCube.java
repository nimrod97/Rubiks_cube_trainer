package com.example.rubikscubetrainer;

import static java.lang.Boolean.FALSE;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

public class PlayingWithScannedCube extends FragmentActivity {
    private GLView glview;
    private ImageView undoBtn;
    private Button solveBtn;
    private Button continueBtn;
    private TextView instructionText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_with_scanned_cube);
        glview = findViewById(R.id.glview);
        undoBtn = findViewById(R.id.undo_btn);
        solveBtn = findViewById(R.id.solve);
        continueBtn = findViewById(R.id.Continue);
        instructionText = findViewById(R.id.instructions_text);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instructionText.setText("");
                v.setVisibility(View.GONE);
                solveBtn.setVisibility(View.VISIBLE);
                undoBtn.setVisibility(View.VISIBLE);
                glview.setScanMode(FALSE);

            }
        });
        undoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                glview.cancelMove();
            }
        });
        solveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //solve
            }
        });
    }
}