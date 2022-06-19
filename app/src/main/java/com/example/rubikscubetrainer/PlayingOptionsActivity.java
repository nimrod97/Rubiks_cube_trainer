package com.example.rubikscubetrainer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rubikscubetrainer.db.StatsActivity;
import com.example.rubikscubetrainer.scanning.ScanningActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
//import com.example.rubikscubetrainer.scanning.ScanningActivity;


public class PlayingOptionsActivity extends AppCompatActivity {
    private Button scan;
    private Button play;
    private Button stats;
    private Button logout;
    //    private Button help;
    private ImageView help;

    GoogleSignInClient mGoogleSignInClient;
    public static String username;
    private TextView text;
    private OkHttpClient okHttpClient;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_options);
        scan = findViewById(R.id.scan_button);
        play = findViewById(R.id.play_in_the_app_button);
        stats = findViewById(R.id.stats_button);
        logout = findViewById(R.id.logout);
        help = findViewById(R.id.help_button);
        text = findViewById(R.id.username);
        okHttpClient = new OkHttpClient();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null)
            username = account.getDisplayName();
        text.setText("Welcome, " + username + "!");
        RequestBody formbody = new FormBody.Builder()
                .add("username", username)
                .build();
//                Request request= new Request.Builder().url("http://10.100.102.24:5000/register_to_DB")
        Request request = new Request.Builder().url("https://rubiks-cube-server-oh2xye4svq-oa.a.run.app/register_to_DB")
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
                        response.close();
                    }
                });
            }
        });

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlayingOptionsActivity.this, ScanningActivity.class);
//                Intent intent = new Intent(PlayingOptionsActivity.this, ScanningActivity.class);
                startActivity(intent);
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlayingOptionsActivity.this, CubeGLActivity.class);
                startActivity(intent);
            }
        });
        stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayingOptionsActivity.this, StatsActivity.class);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.logout) {
                    logOut();
                }
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayingOptionsActivity.this, HelpActivity.class);
                startActivity(intent);
            }
        });


    }

    private void logOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(PlayingOptionsActivity.this, "User logged out successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PlayingOptionsActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
    }
}
