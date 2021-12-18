package com.example.rubikscubetrainer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ScanningActivity extends AppCompatActivity {

    private Button topFace;
    private Button leftFace;
    private Button frontFace;
    private Button rightFace;
    private Button backFace;
    private Button bottomFace;
    private OkHttpClient okHttpClient;
    public String current_sending_face=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning);
        okHttpClient = new OkHttpClient();
        topFace = findViewById(R.id.top);
        leftFace = findViewById(R.id.left);
        frontFace = findViewById(R.id.front);
        rightFace = findViewById(R.id.right);
        backFace = findViewById(R.id.back);
        bottomFace = findViewById(R.id.bottom);
        ActivityResultLauncher <Intent> startActivityForResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
                                Intent data = result.getData();
                                assert data != null;
                                Bundle extras= data.getExtras();
                                Bitmap imageBitmap = (Bitmap)extras.get("data");
                                ByteArrayOutputStream baos=new ByteArrayOutputStream();
                                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                byte[] imageByte=baos.toByteArray();
                                String img_arr= Base64.encodeToString(imageByte,Base64.DEFAULT);
                                RequestBody formbody
                                        = new FormBody.Builder()
                                        .add(current_sending_face, img_arr)
                                        .build();
                                Request request = new Request.Builder().url("http://10.100.102.9:5000/process_face")
                                        .post(formbody)
                                        .build();
                                okHttpClient.newCall(request).enqueue(new Callback() {
                                    @Override
                                    public void onFailure(@NonNull Call call, @NonNull IOException e) {

                                    }

                                    @Override
                                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                                    }
                                });
                            }
                        }
                    }).start();

                }
        );
        topFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                current_sending_face="top";
                startActivityForResult.launch(intent);

            }
        });
        leftFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                current_sending_face="left";
                startActivityForResult.launch(intent);
            }
        });
        frontFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                current_sending_face="front";
                startActivityForResult.launch(intent);
            }
        });
        rightFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                current_sending_face="right";
                startActivityForResult.launch(intent);
            }
        });
        backFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                current_sending_face="back";
                startActivityForResult.launch(intent);
            }
        });
        bottomFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                current_sending_face="bottom";
                startActivityForResult.launch(intent);
            }
        });

    }




}