package com.example.rubikscubetrainer.db;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rubikscubetrainer.R;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
        Request request = new Request.Builder().url("https://rubiks-cube-server-oh2xye4svq-oa.a.run.app/toughest_cubes").build();
//        Request request = new Request.Builder().url("http://10.100.102.24:5000/toughest_cubes").build();
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
                                        colorsFirst = ordered[i].split(";")[0].split(",");
                                        String numOfSteps = ordered[i].split(";")[1];
                                        first.setText(numOfSteps);
                                    } else if (i == 1) {
                                        second_btn.setVisibility(View.VISIBLE);
                                        colorsSecond = ordered[i].split(";")[0].split(",");
                                        String numOfSteps = ordered[i].split(";")[1];
                                        second.setText(numOfSteps);
                                    } else if (i == 2) {
                                        third_btn.setVisibility(View.VISIBLE);
                                        colorsThird = ordered[i].split(";")[0].split(",");
                                        String numOfSteps = ordered[i].split(";")[1];
                                        third.setText(numOfSteps);
                                    } else if (i == 3) {
                                        fourth_btn.setVisibility(View.VISIBLE);
                                        colorsFourth = ordered[i].split(";")[0].split(",");
                                        String numOfSteps = ordered[i].split(";")[1];
                                        fourth.setText(numOfSteps);
                                    } else {
                                        fifth_btn.setVisibility(View.VISIBLE);
                                        colorsFifth = ordered[i].split(";")[0].split(",");
                                        String numOfSteps = ordered[i].split(";")[1];
                                        fifth.setText(numOfSteps);
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