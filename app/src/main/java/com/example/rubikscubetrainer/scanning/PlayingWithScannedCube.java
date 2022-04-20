package com.example.rubikscubetrainer.scanning;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.rubikscubetrainer.GLView;
import com.example.rubikscubetrainer.LoginActivity;
import com.example.rubikscubetrainer.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
                glview.setMode(0);
                // saving the colors of the faces in the order: front,left,back,right,top,bottom faces in the db
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody formbody = new FormBody.Builder()
                        .add("colorsVector", String.join(",", ScannedCube.allColors))
                        .add("username", LoginActivity.username.getText().toString())
                        .build();
//                Request request=new Request.Builder().url("http://10.100.102.19:5000/save_cube_state")
                Request request = new Request.Builder().url("https://rubiks-cube-server-oh2xye4svq-oa.a.run.app/save_cube_state")
                        .post(formbody)
                        .build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "server down", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    }
                });

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
                try {
                    glview.solve();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
    }
}