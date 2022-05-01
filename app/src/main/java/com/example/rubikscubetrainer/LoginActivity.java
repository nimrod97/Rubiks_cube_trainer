package com.example.rubikscubetrainer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    public static EditText username;
    private EditText password;
    private Button loginBtn;
    private Button signInBtn;
    private Button registerBtn;
    private Button createNewUsernameBtn;
    private TextView registerTxt;
    private TextView signInTxt;
    private TextView title;
    private OkHttpClient okHttpClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.login_send);
        signInBtn = findViewById(R.id.sign_in_btn);
        signInTxt = findViewById(R.id.sign_in);
        registerBtn = findViewById(R.id.register_btn);
        registerTxt = findViewById(R.id.register);
        createNewUsernameBtn = findViewById(R.id.createUsername);
        title = findViewById(R.id.title);
        okHttpClient = new OkHttpClient();
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.INVISIBLE);
                signInTxt.setVisibility(View.INVISIBLE);
                registerBtn.setVisibility(View.INVISIBLE);
                registerTxt.setVisibility(View.INVISIBLE);
                loginBtn.setVisibility(View.VISIBLE);
                username.setVisibility(View.VISIBLE);
                password.setVisibility(View.VISIBLE);
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                v.setVisibility(View.INVISIBLE);
                registerTxt.setVisibility(View.INVISIBLE);
                signInBtn.setVisibility(View.INVISIBLE);
                signInTxt.setVisibility(View.INVISIBLE);
                title.setText("Registration page");
                username.setVisibility(View.VISIBLE);
                password.setVisibility(View.VISIBLE);
                createNewUsernameBtn.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "choose your username and password!", Toast.LENGTH_SHORT).show();

            }
        });
        createNewUsernameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String u = username.getText().toString();
                String p = password.getText().toString();
                RequestBody formbody
                        = new FormBody.Builder()
                        .add("username", u)
                        .add("password", p)
                        .build();
//                Request request= new Request.Builder().url("http://10.100.102.19:5000/register")
                Request request = new Request.Builder().url("https://rubiks-cube-server-oh2xye4svq-oa.a.run.app/register")
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String r = response.body().string();
                                    response.close();
                                    if (r.equals("ok")) {
                                        v.setVisibility(View.INVISIBLE);
                                        Intent intent = new Intent(LoginActivity.this, PlayingOptionsActivity.class);
                                        intent.putExtra("username", username.getText().toString());
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(getApplicationContext(), r, Toast.LENGTH_SHORT).show();
                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    }
                });
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String u = username.getText().toString();
                String p = password.getText().toString();
                RequestBody formbody
                        = new FormBody.Builder()
                        .add("username", u)
                        .add("password", p)
                        .build();
//                Request request= new Request.Builder().url("http://10.100.102.24:5000/login")
                Request request = new Request.Builder().url("https://rubiks-cube-server-oh2xye4svq-oa.a.run.app/login")
                        .post(formbody)
                        .build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(
                            @NotNull Call call,
                            @NotNull IOException e) {
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
                                    String r = response.body().string();
                                    if (r.equals("correct") ||
                                            r.startsWith("created")) {
                                        Intent intent = new Intent(LoginActivity.this, PlayingOptionsActivity.class);
                                        startActivity(intent);
                                    } else
                                        Toast.makeText(getApplicationContext(), r, Toast.LENGTH_SHORT).show();
//                                    finish();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                response.close();
                            }
                        });
                    }
                });

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(getApplicationContext(), "can't go back from here!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}