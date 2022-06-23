package com.example.rubikscubetrainer.db;

import android.graphics.Typeface;
import android.os.Bundle;
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

// This activity is responsible for showing the users who are the top-3 users that play
// the most games in the app.
//The suitable layout for it is the 'activity_most_play'


public class MostPlay extends AppCompatActivity {
    private TextView first;
    private TextView second;
    private TextView third;
    private TextView num1;
    private TextView num2;
    private TextView num3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_most_play);
        first = findViewById(R.id.first);
        second = findViewById(R.id.second);
        third = findViewById(R.id.third);
        num1 = findViewById(R.id.num1);
        num2 = findViewById(R.id.num2);
        num3 = findViewById(R.id.num3);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url("https://rubiks-cube-server-oh2xye4svq-oa.a.run.app/mostPlay").build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MostPlay.this, "server down", Toast.LENGTH_SHORT).show();
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
                            if (!res.equals("error")) {
                                String[] ordered = res.split("\n");
                                for (int i = 0; i < ordered.length; i++) {
                                    if (i == 0) {
                                        String[] temp = ordered[i].split(",");
                                        first.setText("1. " + temp[0]);
                                        num1.setText(temp[1]);
                                        first.setTextSize(30);
                                        first.setTypeface(Typeface.DEFAULT_BOLD);
                                        num1.setTextSize(30);

                                    } else if (i == 1) {
                                        String[] temp = ordered[i].split(",");
                                        second.setText("2. " + temp[0]);
                                        num2.setText(temp[1]);
                                        second.setTextSize(30);
                                        second.setTypeface(Typeface.DEFAULT_BOLD);
                                        num2.setTextSize(30);
                                    } else {
                                        String[] temp = ordered[i].split(",");
                                        third.setText("3. " + temp[0]);
                                        num3.setText(temp[1]);
                                        third.setTextSize(30);
                                        third.setTypeface(Typeface.DEFAULT_BOLD);
                                        num3.setTextSize(30);
                                    }

                                }

                            } else
                                Toast.makeText(MostPlay.this, "empty list", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });


            }
        });
    }
}