package com.example.rubikscubetrainer.activities;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rubikscubetrainer.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

// This activity is responsible for the opening screen of the app and the users
// log in to the app via in this activity.
// The suitable layout for it is the 'activity_main'

public class MainActivity extends AppCompatActivity {

    private TextView welcomeMessage;
    private TextView signInMessage;
    private SignInButton signInButton;
    private GoogleSignInClient mGoogleSignInClient;
    private ImageView img;
    private static final int RC_SIGN_IN = 0;
    private String username;
    private OkHttpClient okHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        welcomeMessage = findViewById(R.id.welcome_message);
        signInMessage = findViewById(R.id.signin_text);
        signInButton = findViewById(R.id.signin);
        img = findViewById(R.id.cube_pic);
        okHttpClient = new OkHttpClient();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.signin) {
                    signIn();
                }
            }
        });
        Request request = new Request.Builder().url("https://rubiks-cube-server-oh2xye4svq-oa.a.run.app").build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "server down", Toast.LENGTH_SHORT).show();
                        welcomeMessage.setText("error connecting to the server");
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            welcomeMessage.setText(response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        response.close();
                        img.setVisibility(View.VISIBLE);
                        signInMessage.setVisibility(View.VISIBLE);
                        signInButton.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Toast.makeText(this, "Sign-in Successfully", Toast.LENGTH_SHORT).show();
            if (account != null)
                username = account.getDisplayName();
            RequestBody formbody = new FormBody.Builder()
                    .add("username", username)
                    .build();
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
                public void onResponse(@NonNull Call call, @NonNull Response response) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            response.body().close();
                        }
                    });
                }
            });
            Intent intent = new Intent(this, PlayingOptionsActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        } catch (ApiException e) {
            Log.w("Error", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            username = account.getDisplayName();
            Toast.makeText(this, "User already Signed-in", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), PlayingOptionsActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        }
    }

}