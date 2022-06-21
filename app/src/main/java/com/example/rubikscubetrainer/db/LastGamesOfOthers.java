package com.example.rubikscubetrainer.db;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rubikscubetrainer.R;
import com.example.rubikscubetrainer.activities.PlayingOptionsActivity;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LastGamesOfOthers extends AppCompatActivity {

    private TextView first;
    private TextView second;
    private TextView third;
    private TextView fourth;
    private TextView fifth;
    private TextView sixth;
    private TextView seventh;
    private Button first_btn;
    private Button second_btn;
    private Button third_btn;
    private Button fourth_btn;
    private Button fifth_btn;
    private Button sixth_btn;
    private Button seventh_btn;
    public static String[] colorsFirst;
    public static String[] colorsSecond;
    public static String[] colorsThird;
    public static String[] colorsFourth;
    public static String[] colorsFifth;
    public static String[] colorsSixth;
    public static String[] colorsSeventh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_games_of_others);
        first = findViewById(R.id.first);
        second = findViewById(R.id.second);
        third = findViewById(R.id.third);
        fourth = findViewById(R.id.fourth);
        fifth = findViewById(R.id.fifth);
        sixth = findViewById(R.id.sixth);
        seventh = findViewById(R.id.seventh);
        first_btn = findViewById(R.id.button_first);
        second_btn = findViewById(R.id.button_second);
        third_btn = findViewById(R.id.button_third);
        fourth_btn = findViewById(R.id.button_fourth);
        fifth_btn = findViewById(R.id.button_fifth);
        sixth_btn = findViewById(R.id.button_sixth);
        seventh_btn = findViewById(R.id.button_seventh);
        String username = PlayingOptionsActivity.username;
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url("https://rubiks-cube-server-oh2xye4svq-oa.a.run.app/lastGamesOfOthers?username=" + username).build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LastGamesOfOthers.this, "server down", Toast.LENGTH_SHORT).show();
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
                            if (!res.equals("error") && !res.equals("")) {
                                String[] ordered = res.split("\n");
                                for (int i = 0; i < ordered.length; i++) {
                                    if (i == 0) {
                                        first_btn.setVisibility(View.VISIBLE);
                                        colorsFirst = ordered[i].split("colors")[1].split(",");
                                        ordered[i] = ordered[i].replace("'", "");
                                        ordered[i] = ordered[i].replace("{", "");
                                        ordered[i] = ordered[i].replace("}", "");
                                        String[] temp = ordered[i].split(",");
                                        first.setText(temp[0] + '\n' + temp[1] + '\n' + temp[2] + '\n' + temp[3] + '\n' + temp[4]);

                                    } else if (i == 1) {
                                        second_btn.setVisibility(View.VISIBLE);
                                        colorsSecond = ordered[i].split("colors")[1].split(",");
                                        ordered[i] = ordered[i].replace("'", "");
                                        ordered[i] = ordered[i].replace("{", "");
                                        ordered[i] = ordered[i].replace("}", "");
                                        String[] temp = ordered[i].split(",");
                                        second.setText(temp[0] + '\n' + temp[1] + '\n' + temp[2] + '\n' + temp[3] + '\n' + temp[4]);
                                    } else if (i == 2) {
                                        third_btn.setVisibility(View.VISIBLE);
                                        colorsThird = ordered[i].split("colors")[1].split(",");
                                        ordered[i] = ordered[i].replace("'", "");
                                        ordered[i] = ordered[i].replace("{", "");
                                        ordered[i] = ordered[i].replace("}", "");
                                        String[] temp = ordered[i].split(",");
                                        third.setText(temp[0] + '\n' + temp[1] + '\n' + temp[2] + '\n' + temp[3] + '\n' + temp[4]);
                                    } else if (i == 3) {
                                        fourth_btn.setVisibility(View.VISIBLE);
                                        colorsFourth = ordered[i].split("colors")[1].split(",");
                                        ordered[i] = ordered[i].replace("'", "");
                                        ordered[i] = ordered[i].replace("{", "");
                                        ordered[i] = ordered[i].replace("}", "");
                                        String[] temp = ordered[i].split(",");
                                        fourth.setText(temp[0] + '\n' + temp[1] + '\n' + temp[2] + '\n' + temp[3] + '\n' + temp[4]);
                                    } else if (i == 4) {
                                        fifth_btn.setVisibility(View.VISIBLE);
                                        colorsFifth = ordered[i].split("colors")[1].split(",");
                                        ordered[i] = ordered[i].replace("'", "");
                                        ordered[i] = ordered[i].replace("{", "");
                                        ordered[i] = ordered[i].replace("}", "");
                                        String[] temp = ordered[i].split(",");
                                        fifth.setText(temp[0] + '\n' + temp[1] + '\n' + temp[2] + '\n' + temp[3] + '\n' + temp[4]);
                                    } else if (i == 5) {
                                        sixth_btn.setVisibility(View.VISIBLE);
                                        colorsSixth = ordered[i].split("colors")[1].split(",");
                                        ordered[i] = ordered[i].replace("'", "");
                                        ordered[i] = ordered[i].replace("{", "");
                                        ordered[i] = ordered[i].replace("}", "");
                                        String[] temp = ordered[i].split(",");
                                        sixth.setText(temp[0] + '\n' + temp[1] + '\n' + temp[2] + '\n' + temp[3] + '\n' + temp[4]);
                                    } else {
                                        seventh_btn.setVisibility(View.VISIBLE);
                                        colorsSeventh = ordered[i].split("colors")[1].split(",");
                                        ordered[i] = ordered[i].replace("'", "");
                                        ordered[i] = ordered[i].replace("{", "");
                                        ordered[i] = ordered[i].replace("}", "");
                                        String[] temp = ordered[i].split(",");
                                        seventh.setText(temp[0] + '\n' + temp[1] + '\n' + temp[2] + '\n' + temp[3] + '\n' + temp[4]);
                                    }
                                }

                            } else
                                Toast.makeText(LastGamesOfOthers.this, "empty list", Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(LastGamesOfOthers.this, CubeFromDB.class);
                intent.putExtra("colors", colorsFirst);
                startActivity(intent);
            }
        });
        second_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LastGamesOfOthers.this, CubeFromDB.class);
                intent.putExtra("colors", colorsSecond);
                startActivity(intent);
            }
        });
        third_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LastGamesOfOthers.this, CubeFromDB.class);
                intent.putExtra("colors", colorsThird);
                startActivity(intent);

            }
        });
        fourth_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LastGamesOfOthers.this, CubeFromDB.class);
                intent.putExtra("colors", colorsFourth);
                startActivity(intent);
            }
        });
        fifth_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LastGamesOfOthers.this, CubeFromDB.class);
                intent.putExtra("colors", colorsFifth);
                startActivity(intent);
            }
        });
        sixth_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LastGamesOfOthers.this, CubeFromDB.class);
                intent.putExtra("colors", colorsSixth);
                startActivity(intent);
            }
        });
        seventh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LastGamesOfOthers.this, CubeFromDB.class);
                intent.putExtra("colors", colorsSeventh);
                startActivity(intent);
            }
        });


    }
}