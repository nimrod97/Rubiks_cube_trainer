package com.example.rubikscubetrainer;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class CubeGLActivity extends FragmentActivity {
    private GLView glview;
    private ImageView undoBtn;
    private Button shuffleBtn;
    private Button solveBtn;
    private int size;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        size = 3;
        glview = findViewById(R.id.glview);
        undoBtn = findViewById(R.id.undo_btn);
        shuffleBtn = findViewById(R.id.shuffle);
        solveBtn = findViewById(R.id.solve);
        undoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                glview.cancelMove();
            }
        });
        shuffleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                glview.mix();
            }
        });
        solveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //solve
            }
        });
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody formbody = new FormBody.Builder()
                .add("username", LoginActivity.username.getText().toString())
                .build();
//                Request request=new Request.Builder().url("http://10.100.102.19:5000/save_cube_state")
        Request request = new Request.Builder().url("https://rubiks-cube-server-oh2xye4svq-oa.a.run.app/save_cube_state")
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
            }
        });

    }

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//	    MenuInflater inflater = getMenuInflater();
//	    inflater.inflate(R.menu.game_menu, menu);
//	    return true;
//	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case R.id.mix:
//			glview.mix();
//			return true;
//		case R.id.exit:
//			this.finish();
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}

}