package com.example.rubikscubetrainer;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.rubikscubetrainer.db.StatsActivity;
import com.example.rubikscubetrainer.scanning.ScanningActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
//import com.example.rubikscubetrainer.scanning.ScanningActivity;


public class PlayingOptionsActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {
    private Button scan;
    private Button play;

    GoogleSignInClient mGoogleSignInClient;
    public static String username;
    private TextView text;
    private OkHttpClient okHttpClient;

    private androidx.appcompat.widget.Toolbar mToolBar;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_options);

        setUpToolBarMenu();
        setUpNavigationDrawerMenu();

        scan = findViewById(R.id.scan_button);
        play = findViewById(R.id.play_in_the_app_button);
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
    }

    private void setUpToolBarMenu() {
        mToolBar = findViewById(R.id.toolbar);
//        mToolBar.setTitle("Home");
    }

    private void setUpNavigationDrawerMenu() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navView);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle menuToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolBar, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(menuToggle);
        menuToggle.syncState();
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        closeDrawer();
        Intent intent;
        switch (item.getItemId()) {
            case R.id.home_id:
//                mToolBar.setTitle("Home");
                intent = new Intent(this, PlayingOptionsActivity.class);
                startActivity(intent);
                break;
            case R.id.learn_id:
//                mToolBar.setTitle("Learn More");
                intent = new Intent(this, HelpActivity.class);
                startActivity(intent);
                break;
            case R.id.stats_id:
//                mToolBar.setTitle("Statistics");
                intent = new Intent(this, StatsActivity.class);
                startActivity(intent);
                break;
            case R.id.logout_id:
                logOut();
                break;
        }
        return true;
    }

    private void closeDrawer() {
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
            closeDrawer();
        else {
//            mToolBar.setTitle("Home");
            super.onBackPressed();
        }
    }
}
