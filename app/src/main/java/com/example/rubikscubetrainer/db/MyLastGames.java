package com.example.rubikscubetrainer.db;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rubikscubetrainer.LoginActivity;
import com.example.rubikscubetrainer.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyLastGames extends AppCompatActivity {
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
        setContentView(R.layout.activity_my_last_games);
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
        String username = LoginActivity.username.getText().toString();
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url("https://rubiks-cube-server-oh2xye4svq-oa.a.run.app/myLastGames?username=" + username).build();
//        Request request = new Request.Builder().url("http://10.100.102.19:5000/myLastGames?username=" + username).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyLastGames.this, "server down", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(
                    @NotNull Call call,
                    @NotNull Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String res = response.body().string();
                            response.close();
                            JSONObject jsonObject = new JSONObject(res);
                            String one = jsonObject.getString("first");
                            String two = jsonObject.getString("second");
                            String three = jsonObject.getString("third");
                            String four = jsonObject.getString("fourth");
                            String five = jsonObject.getString("fifth");
                            if (one.contains("scanned cube colors")) {
                                first_btn.setVisibility(View.VISIBLE);
                                colorsFirst = one.split("scanned cube colors")[1].split(",");
                            }
                            if (two.contains("scanned cube colors")) {
                                second_btn.setVisibility(View.VISIBLE);
                                colorsSecond = two.split("scanned cube colors")[1].split(",");
                            }
                            if (three.contains("scanned cube colors")) {
                                third_btn.setVisibility(View.VISIBLE);
                                colorsThird = three.split("scanned cube colors")[1].split(",");
                            }
                            if (four.contains("scanned cube colors")) {
                                fourth_btn.setVisibility(View.VISIBLE);
                                colorsFourth = four.split("scanned cube colors")[1].split(",");
                            }
                            if (five.contains("scanned cube colors")) {
                                fifth_btn.setVisibility(View.VISIBLE);
                                colorsFifth = five.split("scanned cube colors")[1].split(",");
                            }
                            one = one.replace("\"", "");
                            one = one.replace("{", "");
                            one = one.replace("}", "");
                            two = two.replace("\"", "");
                            two = two.replace("{", "");
                            two = two.replace("}", "");
                            three = three.replace("\"", "");
                            three = three.replace("{", "");
                            three = three.replace("}", "");
                            four = four.replace("\"", "");
                            four = four.replace("{", "");
                            four = four.replace("}", "");
                            five = five.replace("\"", "");
                            five = five.replace("{", "");
                            five = five.replace("}", "");
                            String[] temp = one.split(",");
                            first.setText(temp[0] + '\n' + temp[1] + '\n' + temp[2]);
                            temp = two.split(",");
                            second.setText(temp[0] + '\n' + temp[1] + '\n' + temp[2]);
                            temp = three.split(",");
                            third.setText(temp[0] + '\n' + temp[1] + '\n' + temp[2]);
                            temp = four.split(",");
                            fourth.setText(temp[0] + '\n' + temp[1] + '\n' + temp[2]);
                            temp = five.split(",");
                            fifth.setText(temp[0] + '\n' + temp[1] + '\n' + temp[2]);

                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }

                    }
                });


            }
        });
        first_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyLastGames.this, ScannedCubeFromDB.class);
                intent.putExtra("colors", colorsFirst);
                startActivity(intent);
            }
        });
        second_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyLastGames.this, ScannedCubeFromDB.class);
                intent.putExtra("colors", colorsSecond);
                startActivity(intent);
            }
        });
        third_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyLastGames.this, ScannedCubeFromDB.class);
                intent.putExtra("colors", colorsThird);
                startActivity(intent);

            }
        });
        fourth_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyLastGames.this, ScannedCubeFromDB.class);
                intent.putExtra("colors", colorsFourth);
                startActivity(intent);
            }
        });
        fifth_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyLastGames.this, ScannedCubeFromDB.class);
                intent.putExtra("colors", colorsFifth);
                startActivity(intent);
            }
        });

    }
}