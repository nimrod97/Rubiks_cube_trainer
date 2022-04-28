package com.example.rubikscubetrainer.scanning;

import android.os.Bundle;
import android.text.Html;
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

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PlayingWithScannedCube extends FragmentActivity {
    private GLView glview;
    public static ImageView undoBtn;
    public static Button solveBtn;
    private TextView solvingNotations;
    public static TextView solveAloneGreeting;
    private OkHttpClient okHttpClient;
    private Button continueBtn;
    public static Button replayBtn;
    private TextView instructionText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_with_scanned_cube);
        glview = findViewById(R.id.glview);
        undoBtn = findViewById(R.id.undo_btn);
        solveBtn = findViewById(R.id.solve);
        solvingNotations = findViewById(R.id.solve_notations);
        solveAloneGreeting = findViewById(R.id.solve_alone_greeting);
        okHttpClient = new OkHttpClient();
        continueBtn = findViewById(R.id.Continue);
        replayBtn = findViewById(R.id.replay);
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
                        response.close();
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
                String cubeString = glview.getGlrenderer().getCube().getStringRepresentation();
                String[] result = new String[1];
                RequestBody formbody = new FormBody.Builder()
                        .add("cubeString", cubeString)
                        .add("username", LoginActivity.username.getText().toString())
                        .build();
                Request request = new Request.Builder().url("https://rubiks-cube-server-oh2xye4svq-oa.a.run.app/solve")
//        Request request = new Request.Builder().url("http://10.100.102.26:5000/solve")
                        .post(formbody)
                        .build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "server down", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        result[0] = response.body().string();
                        response.close();
                        if (!result[0].equals("error")) {
                            String[] steps = result[0].split(" ");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    solvingNotations.setText(Arrays.toString(steps));
                                    solvingNotations.setVisibility(View.VISIBLE);
                                }
                            });
                            int i = 0;
                            for (String s : steps) {
                                int temp = i;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        StringBuilder s1 = new StringBuilder();
                                        int j;
                                        for (j = 0; j < temp; j++)
                                            s1.append(steps[j]).append(", ");
                                        if (j == steps.length - 1)
                                            s1.append("<b>").append(steps[j]).append("</b>");
                                        else
                                            s1.append("<b>").append(steps[j]).append("</b>").append(", ");

                                        for (int k = j + 1; k < steps.length - 1; k++)
                                            s1.append(steps[k]).append(", ");
                                        if (j != steps.length - 1)
                                            s1.append(steps[steps.length - 1]);
                                        solvingNotations.setText(Html.fromHtml(s1.toString()));
                                    }
                                });
                                glview.getGlrenderer().getCube().beginRotate(s);
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                i++;
                            }
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                solvingNotations.setVisibility(View.INVISIBLE);
                                replayBtn.setVisibility(View.VISIBLE);
                                solveBtn.setVisibility(View.INVISIBLE);
                            }
                        });

                    }
                });


            }
        });

    }
}