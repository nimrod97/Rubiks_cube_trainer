package com.rubiks.rubikscubetrainer.db;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.rubiks.rubikscubetrainer.R;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

// In this activity the users can see the 5 toughest cubes ever that solved by this specific
// solving method.
// Each bullet shows the username, game time, number of steps,
// the cube itself and if the cube was scanned or not.
//The suitable layout for it is the 'activity_toughest_cubes'

public class ToughestCubes extends AppCompatActivity {
    private TextView first;
    private TextView second;
    private TextView third;
    private TextView fourth;
    private TextView fifth;
    private Button first_btn;
    private Button second_btn;
    private Button third_btn;
    private Button fourth_btn;
    private Button fifth_btn;
    public static String[] colorsFirst;
    public static String[] colorsSecond;
    public static String[] colorsThird;
    public static String[] colorsFourth;
    public static String[] colorsFifth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toughest_cubes);
        first = findViewById(R.id.first);
        second = findViewById(R.id.second);
        third = findViewById(R.id.third);
        fourth = findViewById(R.id.fourth);
        fifth = findViewById(R.id.fifth);
        first_btn = findViewById(R.id.button_first);
        second_btn = findViewById(R.id.button_second);
        third_btn = findViewById(R.id.button_third);
        fourth_btn = findViewById(R.id.button_fourth);
        fifth_btn = findViewById(R.id.button_fifth);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(getString(R.string.SERVER_URL) + "/toughest_cubes?method=" + SolvingMethodsList.chosenMethod).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ToughestCubes.this, "server down", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String res = response.body().string();
                            response.close();
                            if (!res.equals("")) {
                                String[] ordered = res.split("\n");
                                for (int i = 0; i < ordered.length; i++) {
                                    if (i == 0) {
                                        first_btn.setVisibility(View.VISIBLE);
                                        String[] temp = ordered[i].split(";");
                                        colorsFirst = temp[0].split(",");
                                        String numOfSteps = temp[1];
                                        String user = temp[2];
                                        String gameTime = temp[3];
                                        String isScanned = temp[4];
                                        first.setText(numOfSteps + '\n' + user + '\n' + gameTime + '\n' + isScanned);
                                    } else if (i == 1) {
                                        second_btn.setVisibility(View.VISIBLE);
                                        String[] temp = ordered[i].split(";");
                                        colorsSecond = temp[0].split(",");
                                        String numOfSteps = temp[1];
                                        String user = temp[2];
                                        String gameTime = temp[3];
                                        String isScanned = temp[4];
                                        second.setText(numOfSteps + '\n' + user + '\n' + gameTime + '\n' + isScanned);
                                    } else if (i == 2) {
                                        third_btn.setVisibility(View.VISIBLE);
                                        String[] temp = ordered[i].split(";");
                                        colorsThird = temp[0].split(",");
                                        String numOfSteps = temp[1];
                                        String user = temp[2];
                                        String gameTime = temp[3];
                                        String isScanned = temp[4];
                                        third.setText(numOfSteps + '\n' + user + '\n' + gameTime + '\n' + isScanned);
                                    } else if (i == 3) {
                                        fourth_btn.setVisibility(View.VISIBLE);
                                        String[] temp = ordered[i].split(";");
                                        colorsFourth = temp[0].split(",");
                                        String numOfSteps = temp[1];
                                        String user = temp[2];
                                        String gameTime = temp[3];
                                        String isScanned = temp[4];
                                        fourth.setText(numOfSteps + '\n' + user + '\n' + gameTime + '\n' + isScanned);
                                    } else {
                                        fifth_btn.setVisibility(View.VISIBLE);
                                        String[] temp = ordered[i].split(";");
                                        colorsFifth = temp[0].split(",");
                                        String numOfSteps = temp[1];
                                        String user = temp[2];
                                        String gameTime = temp[3];
                                        String isScanned = temp[4];
                                        fifth.setText(numOfSteps + '\n' + user + '\n' + gameTime + '\n' + isScanned);
                                    }
                                }

                            } else
                                Toast.makeText(ToughestCubes.this, "empty list", Toast.LENGTH_SHORT).show();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });


            }
        });
        first_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ToughestCubes.this, CubeFromDB.class);
                intent.putExtra("colors", colorsFirst);
                startActivity(intent);
            }
        });
        second_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ToughestCubes.this, CubeFromDB.class);
                intent.putExtra("colors", colorsSecond);
                startActivity(intent);
            }
        });
        third_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ToughestCubes.this, CubeFromDB.class);
                intent.putExtra("colors", colorsThird);
                startActivity(intent);

            }
        });
        fourth_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ToughestCubes.this, CubeFromDB.class);
                intent.putExtra("colors", colorsFourth);
                startActivity(intent);
            }
        });
        fifth_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ToughestCubes.this, CubeFromDB.class);
                intent.putExtra("colors", colorsFifth);
                startActivity(intent);
            }
        });


    }
}