package com.example.rubikscubetrainer;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class CubeGLActivity extends FragmentActivity {
    private GLView glview;
    public static ImageView undoBtn;
    public static Button shuffleBtn;
    public static Button solveBtn;
    public static Button replayBtn;
    private TextView solvingNotations;
    public static TextView solveAloneGreeting;
    private OkHttpClient okHttpClient;
    private int size;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        size = 3;
        glview = findViewById(R.id.glview);
        undoBtn = findViewById(R.id.undo_btn);
        shuffleBtn = findViewById(R.id.shuffle);
        solveBtn = findViewById(R.id.solve);
        replayBtn = findViewById(R.id.replay);
        solvingNotations = findViewById(R.id.solve_notations);
        solveAloneGreeting = findViewById(R.id.solve_alone_greeting);
        okHttpClient = new OkHttpClient();
        undoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                glview.cancelMove();
            }
        });
        shuffleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                glview.mix();
            }
        });
        solveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                glview.getGlrenderer().setSolveFlag(true);
                String override = "true";
                if (!glview.getGlrenderer().isSaveCubeFlag())//the user didn't touch the cube and pressed solve
                    override = "false";
                List<String> colors = new ArrayList<>();
                List<Part> p = glview.getGlrenderer().getCube().getParts();
                for (int i = 0; i < p.size(); i++) {
                    switch (p.get(i).getRectangle().getTextureId()) {
                        case 1:
                            colors.add("white");
                            break;
                        case 2:
                            colors.add("yellow");
                            break;
                        case 3:
                            colors.add("blue");
                            break;
                        case 4:
                            colors.add("green");
                            break;
                        case 5:
                            colors.add("red");
                            break;
                        default:
                            colors.add("orange");
                            break;
                    }
                }
                RequestBody formbody = new FormBody.Builder()
                        .add("username", LoginActivity.username.getText().toString())
                        .add("generatedColors", String.join(",", colors))
                        .add("override", override)
                        .build();
//                Request request = new Request.Builder().url("http://10.100.102.24:5000/save_cube_state")
                Request request = new Request.Builder().url("https://rubiks-cube-server-oh2xye4svq-oa.a.run.app/save_cube_state")
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
                        response.close();
                    }
                });
                String cubeString = glview.getGlrenderer().getCube().getStringRepresentation();
                String[] result = new String[1];
                formbody = new FormBody.Builder()
                        .add("cubeString", cubeString)
                        .add("username", LoginActivity.username.getText().toString())
                        .build();
                request = new Request.Builder().url("https://rubiks-cube-server-oh2xye4svq-oa.a.run.app/solve")
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
                                shuffleBtn.setVisibility(View.INVISIBLE);
                            }
                        });

                    }
                });
            }
        });
        replayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CubeGLActivity.this, CubeGLActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}