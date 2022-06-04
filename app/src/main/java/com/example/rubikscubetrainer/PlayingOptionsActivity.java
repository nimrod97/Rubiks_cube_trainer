package com.example.rubikscubetrainer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rubikscubetrainer.db.StatsActivity;
import com.example.rubikscubetrainer.scanning.Scanner;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
//import com.example.rubikscubetrainer.scanning.ScanningActivity;


public class PlayingOptionsActivity extends AppCompatActivity {
    private Button scan;
    private Button play;
    private Button stats;
    private Button logout;
    GoogleSignInClient mGoogleSignInClient;
    //    private String username;
    public static String username;
    private TextView text;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_options);
        scan = findViewById(R.id.scan_button);
        play = findViewById(R.id.play_in_the_app_button);
        stats = findViewById(R.id.stats_button);
        logout = findViewById(R.id.logout);
        text = findViewById(R.id.username);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null)
            username = account.getDisplayName();

//        username = LoginActivity.username.getText().toString();
//        text = findViewById(R.id.username);
        text.setText("Welcome, " + username + "!");
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlayingOptionsActivity.this, Scanner.class);
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
