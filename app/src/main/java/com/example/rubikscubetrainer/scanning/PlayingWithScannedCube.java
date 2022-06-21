package com.example.rubikscubetrainer.scanning;

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

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.rubikscubetrainer.GLView;
import com.example.rubikscubetrainer.R;
import com.example.rubikscubetrainer.activities.PlayingOptionsActivity;

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

public class PlayingWithScannedCube extends FragmentActivity {
    private GLView glview;
    public static ImageView undoBtn;
    public static Button solveBtn;
    private TextView solvingNotations;
    public static TextView solveAloneGreeting;
    private OkHttpClient okHttpClient;
    private Button continueBtn;
    public static Button replayBtn;
    public static Button backToHomeBtn;
    public static ImageView playBtn;
    public static ImageView pauseBtn;
    public static SeekBar slider;
    public static TextView sliderText;
    private TextView instructionText;
    private boolean isPaused;
    private String solvingMethod;
    public static int rotationSpeed;
    private volatile int stepIndex;
    private String[] steps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_with_scanned_cube);
        isPaused = true;
        rotationSpeed = 1000;
        glview = findViewById(R.id.glview);
        undoBtn = findViewById(R.id.undo_btn);
        solveBtn = findViewById(R.id.solve);
        solvingNotations = findViewById(R.id.solve_notations);
        solveAloneGreeting = findViewById(R.id.solve_alone_greeting);
        okHttpClient = new OkHttpClient();
        continueBtn = findViewById(R.id.Continue);
        replayBtn = findViewById(R.id.replay);
        backToHomeBtn = findViewById(R.id.back_to_home_screen);
        playBtn = findViewById(R.id.play_btn);
        pauseBtn = findViewById(R.id.pause_btn);
        slider = findViewById(R.id.slider);
        sliderText = findViewById(R.id.slider_text);
        instructionText = findViewById(R.id.instructions_text);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instructionText.setVisibility(View.INVISIBLE);
                v.setVisibility(View.INVISIBLE);
                solveBtn.setVisibility(View.VISIBLE);
                undoBtn.setVisibility(View.VISIBLE);
                glview.setMode(3);
                // saving the colors of the faces in the order: front,left,back,right,top,bottom faces in the db
                RequestBody formbody = new FormBody.Builder()
                        .add("colorsVector", String.join(",", glview.getGlrenderer().getCube().getColors()))
                        .add("username", PlayingOptionsActivity.username)
                        .build();
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
                if (glview.getGlrenderer().getSolveFlag()) {
                    if (stepIndex >= 1) {
                        String s = steps[stepIndex - 1];
                        s = s.replace(" ", "");
                        StringBuilder lastStep = new StringBuilder(s);
                        if (!lastStep.toString().contains("2")) {
                            if (lastStep.substring(lastStep.length() - 1).equals("'"))
                                lastStep.deleteCharAt(lastStep.length() - 1);
                            else
                                lastStep.append("'");
                        }

                        stepIndex--;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                glview.getGlrenderer().getCube().beginRotate(lastStep.toString());
                                try {
                                    Thread.sleep(rotationSpeed);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                StringBuilder s1 = new StringBuilder();
                                int j;
                                for (j = 0; j < stepIndex; j++)
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


                    }
                } else
                    glview.cancelMove();
            }

        });
        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPaused = true;
                v.setVisibility(View.INVISIBLE);
                playBtn.setVisibility(View.VISIBLE);
                undoBtn.setVisibility(View.VISIBLE);
            }
        });
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPaused = false;
                pauseBtn.setVisibility(View.VISIBLE);
                undoBtn.setVisibility(View.INVISIBLE);
                v.setVisibility(View.INVISIBLE);
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
                                    new AlertDialog.Builder(PlayingWithScannedCube.this)
                                            .setTitle("choose a solving method!")
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
                                                    startSolving(cubeString, colors);
                                                }
                                            })
                                            .show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "error, not valid cube", Toast.LENGTH_SHORT).show();
                                    glview.setMode(1);
                                    continueBtn.setVisibility(View.VISIBLE);
                                    instructionText.setVisibility(View.VISIBLE);
                                    solveBtn.setVisibility(View.INVISIBLE);
                                    undoBtn.setVisibility(View.INVISIBLE);
                                }
                            }
                        });
                    }
                });
            }
        });
        replayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayingWithScannedCube.this, ScanningActivity.class);
                startActivity(intent);
                finish();
            }
        });

        backToHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlayingWithScannedCube.this, PlayingOptionsActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void startSolving(String cubeString, List<String> colors) {
        String[] result = new String[1];
        RequestBody formbody = new FormBody.Builder()
                .add("cubeString", cubeString)
                .add("username", PlayingOptionsActivity.username)
                .add("generatedColors", String.join(",", colors))
                .add("method", solvingMethod)
                .build();
        Request request = new Request.Builder().url("https://rubiks-cube-server-oh2xye4svq-oa.a.run.app/solve")
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
                result[0] = response.body().string();
                response.close();
                if (!result[0].equals("error")) {
                    if (solvingMethod.equals("Kociemba"))
                        steps = result[0].split(" ");
                    else
                        steps = result[0].split(",");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            playBtn.setVisibility(View.VISIBLE);
                            slider.setVisibility(View.VISIBLE);
                            sliderText.setVisibility(View.VISIBLE);
                            undoBtn.setVisibility(View.INVISIBLE);
                            solveBtn.setVisibility(View.INVISIBLE);
                            solvingNotations.setText(Arrays.toString(steps));
                            solvingNotations.setVisibility(View.VISIBLE);
                        }
                    });
                    stepIndex = 0;
                    while (stepIndex < steps.length) {
                        if (isPaused)
                            while (isPaused) {
                            }
                        String s = steps[stepIndex];
                        s = s.replace(" ", "");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder s1 = new StringBuilder();
                                int j;
                                for (j = 0; j < stepIndex; j++)
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
                        stepIndex++;
                    }

                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        solvingNotations.setVisibility(View.INVISIBLE);
                        replayBtn.setVisibility(View.VISIBLE);
                        backToHomeBtn.setVisibility(View.VISIBLE);
                        solveBtn.setVisibility(View.INVISIBLE);
                        playBtn.setVisibility(View.INVISIBLE);
                        pauseBtn.setVisibility(View.INVISIBLE);
                        slider.setVisibility(View.INVISIBLE);
                        sliderText.setVisibility(View.INVISIBLE);
                    }
                });

            }
        });

    }
}