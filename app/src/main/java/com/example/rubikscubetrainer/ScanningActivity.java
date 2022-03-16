package com.example.rubikscubetrainer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
    private Button continueButton;
    private OkHttpClient okHttpClient;
    public static Map<String, JSONArray> faces = new HashMap<>(); // map each type of face to his colors
    public String current_sending_face = null;
    public int numOfScan;


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
        continueButton=findViewById(R.id.Continue);
        this.numOfScan=0;
        ActivityResultLauncher<Intent> startActivityForResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
                                Intent data = result.getData();
                                assert data != null;
                                Bundle extras = data.getExtras();
                                Bitmap imageBitmap = (Bitmap) extras.get("data");
                                byte[] RGB_values = rgbValuesFromBitmap(imageBitmap);
                                String img_arr = Base64.encodeToString(RGB_values, Base64.DEFAULT);
                                RequestBody formbody
                                        = new FormBody.Builder()
                                        .add(current_sending_face, img_arr)
                                        .build();
                                Request request = new Request.Builder().url("https://rubiks-cube-server-oh2xye4svq-oa.a.run.app/process_face")
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
                                                    String res = response.body().string();
                                                    if (res.contains("scan"))
                                                        Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();
                                                    else {
                                                        // create the face according to the returned json
                                                        // in format {face : list of colors} in order:
                                                        // for example for face looks like: 1  2  3
                                                        //                                  4  5  6
                                                        //                                  7  8  9
                                                        // the colors order will be:
                                                        // 1,4,7,2,5,8,3,6,9
                                                        JSONObject jsonObject = new JSONObject(res);
                                                        String faceType = jsonObject.getString("face");
                                                        JSONArray colors = jsonObject.getJSONArray("colors");
                                                        callSuccessScan(faceType, colors);
                                                    }
                                                } catch (IOException | JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
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
                current_sending_face = "top";
                startActivityForResult.launch(intent);


            }
        });
        leftFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                current_sending_face = "left";
                startActivityForResult.launch(intent);
            }
        });
        frontFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                current_sending_face = "front";
                startActivityForResult.launch(intent);
            }
        });
        rightFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                current_sending_face = "right";
                startActivityForResult.launch(intent);
            }
        });
        backFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                current_sending_face = "back";
                startActivityForResult.launch(intent);
            }
        });
        bottomFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                current_sending_face = "bottom";
                startActivityForResult.launch(intent);
            }
        });
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numOfScan==6){
                    Intent intent = new Intent(ScanningActivity.this, PlayingWithScannedCube.class);
                    startActivity(intent);
                    finish();

                }
                else{
                    Toast.makeText(getApplicationContext(),"scan all the faces before continue",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private byte[] rgbValuesFromBitmap(Bitmap bitmap) {
        ColorMatrix colorMatrix = new ColorMatrix();
        ColorFilter colorFilter = new ColorMatrixColorFilter(
                colorMatrix);
        Bitmap argbBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(argbBitmap);

        Paint paint = new Paint();

        paint.setColorFilter(colorFilter);
        canvas.drawBitmap(bitmap, 0, 0, paint);

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int componentsPerPixel = 3;
        int totalPixels = width * height;
        int totalBytes = totalPixels * componentsPerPixel;

        byte[] rgbValues = new byte[totalBytes];
        @ColorInt int[] argbPixels = new int[totalPixels];
        argbBitmap.getPixels(argbPixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < totalPixels; i++) {
            @ColorInt int argbPixel = argbPixels[i];
            int red = Color.red(argbPixel);
            int green = Color.green(argbPixel);
            int blue = Color.blue(argbPixel);
            rgbValues[i * componentsPerPixel + 0] = (byte) red;
            rgbValues[i * componentsPerPixel + 1] = (byte) green;
            rgbValues[i * componentsPerPixel + 2] = (byte) blue;
        }

        return rgbValues;
    }

    private void callSuccessScan(String faceType, JSONArray colors) {
        numOfScan++;
        switch (faceType) {
            case "top":
                topFace.setText("top\ncompleted!");
                faces.put("top", colors);
                break;
            case "left":
                leftFace.setText("left\ncompleted!");
                faces.put("left", colors);
                break;
            case "front":
                frontFace.setText("front\ncompleted!");
                faces.put("front", colors);
                break;
            case "right":
                rightFace.setText("right\ncompleted!");
                faces.put("right", colors);
                break;
            case "back":
                backFace.setText("back\ncompleted!");
                faces.put("back", colors);
                break;
            case "bottom":
                bottomFace.setText("bottom\ncompleted!");
                faces.put("bottom", colors);
                break;
        }
    }


}