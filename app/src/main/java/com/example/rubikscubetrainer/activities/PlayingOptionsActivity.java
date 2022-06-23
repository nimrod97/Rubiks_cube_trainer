package com.example.rubikscubetrainer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.rubikscubetrainer.R;
import com.example.rubikscubetrainer.fragments.HomeFragment;
import com.example.rubikscubetrainer.fragments.LearnFragment;
import com.example.rubikscubetrainer.fragments.StatsFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

// This activity is the main menu of the app when the user can decide which type
// of game he wants to play - playing by scanning his own cube or play in the app
// with virtual cube.
//The suitable layout for it is the 'activity_playing_options'

public class PlayingOptionsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    GoogleSignInClient mGoogleSignInClient;

    private androidx.appcompat.widget.Toolbar mToolBar;
    private DrawerLayout mDrawerLayout;

    public static String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_options);

        setUpToolBarMenu();
        setUpNavigationDrawerMenu();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        username = getIntent().getExtras().getString("username");

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();
    }

    private void setUpToolBarMenu() {
        mToolBar = findViewById(R.id.toolbar);
        mToolBar.setTitle("Home");
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
        switch (item.getItemId()) {
            case R.id.home_id:
                if (mToolBar.getTitle().charAt(0) != 'H') {
                    mToolBar.setTitle("Home");
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new HomeFragment()).commit();
                }
                break;
            case R.id.learn_id:
                if (mToolBar.getTitle().charAt(0) != 'L') {
                    mToolBar.setTitle("Learn More");
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new LearnFragment()).commit();
                }
                break;
            case R.id.stats_id:
                if (mToolBar.getTitle().charAt(0) != 'S') {
                    mToolBar.setTitle("Statistics");
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new StatsFragment()).commit();
                }
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
            super.onBackPressed();
        }
    }

}
