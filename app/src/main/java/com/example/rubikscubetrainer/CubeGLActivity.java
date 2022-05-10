package com.example.rubikscubetrainer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
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
    public static ImageView playBtn;
    public static ImageView pauseBtn;
    public static SeekBar slider;
    private TextView solvingNotations;
    public static TextView solveAloneGreeting;
    private OkHttpClient okHttpClient;
    private boolean isPaused;
    public static int rotationSpeed;
    private String solvingMethod;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_with_virtual_cube);
        isPaused = false;
        rotationSpeed = 1000;
        glview = findViewById(R.id.glview);
        undoBtn = findViewById(R.id.undo_btn);
        shuffleBtn = findViewById(R.id.shuffle);
        solveBtn = findViewById(R.id.solve);
        replayBtn = findViewById(R.id.replay);
        playBtn = findViewById(R.id.play_btn);
        pauseBtn = findViewById(R.id.pause_btn);
        slider = findViewById(R.id.slider);
        solvingNotations = findViewById(R.id.solve_notations);
        solveAloneGreeting = findViewById(R.id.solve_alone_greeting);
        okHttpClient = new OkHttpClient();
        undoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                glview.cancelMove();
            }
        });
        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPaused = true;
                v.setVisibility(View.INVISIBLE);
                playBtn.setVisibility(View.VISIBLE);
            }
        });
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPaused = false;
                pauseBtn.setVisibility(View.VISIBLE);
                v.setVisibility(View.INVISIBLE);
            }
        });
        shuffleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                glview.mix();
            }
        });
        slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                switch (progress) {
                    case 1:
                        rotationSpeed = 4000;
                        break;
                    case 2:
                        rotationSpeed = 2000;
                        break;
                    case 3:
                        rotationSpeed = 1500;
                        break;
                    case 4:
                        rotationSpeed = 1000;
                        break;
                    case 5:
                        rotationSpeed = 666;
                        break;
                    case 6:
                        rotationSpeed = 500;
                        break;
                    default: //7
                        rotationSpeed = 350;
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                switch (seekBar.getProgress()) {
                    case 1:
                        rotationSpeed = 4000;
                        break;
                    case 2:
                        rotationSpeed = 2000;
                        break;
                    case 3:
                        rotationSpeed = 1500;
                        break;
                    case 4:
                        rotationSpeed = 1000;
                        break;
                    case 5:
                        rotationSpeed = 666;
                        break;
                    case 6:
                        rotationSpeed = 500;
                        break;
                    default: //7
                        rotationSpeed = 350;
                        break;
                }
            }
        });
        solveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cubeString = glview.getGlrenderer().getCube().getCubeRepresentationByFaces();
                List<String> colors = glview.getGlrenderer().getCube().getColors();
                String[] result = new String[1];
                RequestBody formbody = new FormBody.Builder()
                        .add("cubeString", cubeString)
                        .add("generatedColors", String.join(",", colors))
                        .build();
                Request request = new Request.Builder().url("https://rubiks-cube-server-oh2xye4svq-oa.a.run.app/get_solving_steps")
//                Request request = new Request.Builder().url("http://10.100.102.24:5000/get_solving_steps")
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    result[0] = response.body().string();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                response.close();
                                if (!result[0].equals("error")) {
                                    String[] numOfSteps = result[0].split("\n");
                                    String[] algorithms = new String[]{"Beginner, steps: " + numOfSteps[0], "CFOP, steps: " + numOfSteps[1], "Kociemba, steps: " + numOfSteps[2]};
                                    new AlertDialog.Builder(CubeGLActivity.this)
                                            .setTitle("choose a solving method!")
//                                                                        .setSingleChoiceItems(algorithms, 0, null)
                                            .setItems(algorithms, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    switch (which) {
                                                        case 0:
                                                            solvingMethod = "Beginner";
                                                            break;
                                                        case 1:
                                                            solvingMethod = "CFOP";
                                                            break;
                                                        default:
                                                            solvingMethod = "Kociemba";
                                                            break;
                                                    }
                                                    glview.getGlrenderer().setSolveFlag(true);
                                                    saveCubeState(colors);
                                                    startSolving(cubeString, colors);
                                                }
                                            })
                                            .show();
                                } else
                                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
            }
        });

        replayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CubeGLActivity.this, PlayingOptionsActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void saveCubeState(List<String> colors) {
        String override = "true";
        if (!glview.getGlrenderer().isSaveCubeFlag())//the user didn't touch the cube and pressed solve
            override = "false";
        RequestBody formbody = new FormBody.Builder()
                .add("username", LoginActivity.username.getText().toString())
                .add("generatedColors", String.join(",", colors))
                .add("override", override)
                .build();
//        Request request = new Request.Builder().url("http://10.100.102.24:5000/save_cube_state")
        Request request = new Request.Builder().url("https://rubiks-cube-server-oh2xye4svq-oa.a.run.app/save_cube_state")
                .post(formbody)
                .build();
        glview.getGlrenderer().setSaveCubeFlag(true);
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
    }

    public void startSolving(String cubeString, List<String> colors) {
        String[] result = new String[1];
        RequestBody formbody = new FormBody.Builder()
                .add("cubeString", cubeString)
                .add("username", LoginActivity.username.getText().toString())
                .add("generatedColors", String.join(",", colors))
                .add("method", solvingMethod)
                .build();
        Request request = new Request.Builder().url("https://rubiks-cube-server-oh2xye4svq-oa.a.run.app/solve")
//        Request request = new Request.Builder().url("http://10.100.102.24:5000/solve")
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
                String[] steps;
                if (!result[0].equals("error")) {
                    if (solvingMethod.equals("Kociemba"))
                        steps = result[0].split(" ");
                    else
                        steps = result[0].split(",");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pauseBtn.setVisibility(View.VISIBLE);
                            slider.setVisibility(View.VISIBLE);
                            solvingNotations.setText(Arrays.toString(steps));
                            solvingNotations.setVisibility(View.VISIBLE);
                        }
                    });
                    int i = 0;
                    for (String s : steps) {
                        s = s.replace(" ", "");
                        if (isPaused)
                            while (isPaused) {
                            }
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
                            Thread.sleep(rotationSpeed);
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
                        playBtn.setVisibility(View.INVISIBLE);
                        pauseBtn.setVisibility(View.INVISIBLE);
                        slider.setVisibility(View.INVISIBLE);
                    }
                });

            }
        });
    }
}