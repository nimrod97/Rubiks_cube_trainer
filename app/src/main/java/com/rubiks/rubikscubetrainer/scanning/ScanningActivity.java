package com.rubiks.rubikscubetrainer.scanning;

import static org.opencv.core.Core.mean;
import static org.opencv.imgproc.Imgproc.putText;
import static org.opencv.imgproc.Imgproc.rectangle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.rubiks.rubikscubetrainer.R;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


// This activity is responsible for scanning the cube face after face
// it draws 9 cubbies templates that make it easier for the user to see if he
// scans correctly or needs to be in more light room, etc.
// The suitable layout for it is the 'activity_scanning'

public class ScanningActivity extends Activity implements CvCameraViewListener2 {
    private JavaCameraView camera;
    private BaseLoaderCallback baseLoaderCallback;
    private Mat mRgba;
    private Point textDrawPoint, arrowDrawPoint;
    private Scalar colorText = new Scalar(0, 0, 0, 255);
    private ImageButton saveFaceButton;
    private TextView faceText;

    private int thicknessRect = 13, sizeRect = 125;
    private ArrayList<Square> squares;
    private int squareLayoutDistance = 200;
    private Point[] squareLocations = {
            new Point(-1, -1), new Point(0, -1), new Point(1, -1),
            new Point(-1, 0), new Point(0, 0), new Point(1, 0),
            new Point(-1, 1), new Point(0, 1), new Point(1, 1)};
    public static Map<String, List<Character>> faces = new HashMap<>();
    private int index = 0;
    private long timeOffset; // every 200ms we look in new frame for processing colors

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_scanning);
        saveFaceButton = findViewById(R.id.saveFaceButton);
        faceText = findViewById(R.id.face_text);
        camera = findViewById(R.id.javaCameraView);
        camera.setCameraPermissionGranted();
        camera.setCvCameraViewListener(this);
        saveFaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFaceColors();
            }
        });
    }


    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        if (squares == null) {
            Point tempCenter = new Point(mRgba.width() / 2, mRgba.height() / 2);
            squares = new ArrayList<>();
            for (Point squareLocation : squareLocations) {
                squares.add(new Square(new Point(squareLocation.x * squareLayoutDistance + tempCenter.x, squareLocation.y * squareLayoutDistance + tempCenter.y), sizeRect));
            }
        }
        if (System.currentTimeMillis() - timeOffset >= 200)
            processColor();
        drawSquares();
        return mRgba;
    }

    private void processColor() {
        Scalar tmpColor;
        for (Square s : squares) {
            Mat rectRgba = mRgba.submat(s.getRect());
            tmpColor = mean(rectRgba);
            s.setColorRgb(tmpColor);
        }
        timeOffset = System.currentTimeMillis();

    }

    private void drawSquares() {
        // draw 9 cubbies templates on the camera screen
        for (int i = 0; i < squares.size(); i++) {
            Square s = squares.get(i);
            Scalar showColor = charToRGB(s.getColor());
            rectangle(mRgba, s.getTopLeftPoint(), s.getBottomRightPoint(), colorText, thicknessRect + 7);
            rectangle(mRgba, s.getTopLeftPoint(), s.getBottomRightPoint(), showColor, thicknessRect);
            textDrawPoint = new Point(s.getCenter().x - 100, s.getCenter().y);
            putText(mRgba, s.getColor(), textDrawPoint, 1, 6, colorText, 8);
        }

        // guiding the user which face he has to scan right now
        if (index == 0) {
            faceText.setText("front");
        } else if (index == 1) {
            faceText.setText("left");
        } else if (index == 2) {
            faceText.setText("back");
        } else if (index == 3) {
            faceText.setText("right");
        } else if (index == 4) {
            faceText.setText("top");
        } else {
            faceText.setText("bottom");
        }
    }

    // save each scanned face in the dictionary
    void saveFaceColors() {
        String key;
        String tempString = "";
        for (int i = 0; i < squares.size(); i++) {
            Square s = squares.get(i);
            tempString += s.getColor();
        }
        if (tempString.contains("n")) {
            return;
        }

        if (index == 0)
            key = "front";
        else if (index == 1)
            key = "left";
        else if (index == 2)
            key = "back";
        else if (index == 3)
            key = "right";
        else if (index == 4)
            key = "top";
        else
            key = "bottom";
        List<Character> temp = new ArrayList<>();
        for (char c : tempString.toCharArray())
            temp.add(c);
        faces.put(key, temp);
        index++;
        if (index == 6) {
            Intent intent = new Intent(ScanningActivity.this, PlayingWithScannedCube.class);
            startActivity(intent);
            finish();
        }

    }


    Scalar charToRGB(String color) {
        Scalar showColor;
        switch (color) {
            case "R":
                showColor = new Scalar(255, 0, 0);
                break;
            case "G":
                showColor = new Scalar(0, 255, 0);
                break;
            case "B":
                showColor = new Scalar(0, 0, 255);
                break;
            case "Y":
                showColor = new Scalar(255, 255, 0);
                break;
            case "O":
                showColor = new Scalar(255, 165, 0);
                break;
            case "W":
                showColor = new Scalar(255, 255, 255);
                break;
            default: //case "n" - none
                showColor = new Scalar(0, 0, 0);
                break;
        }
        return showColor;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (camera != null)
            camera.disableView();
    }

    public void onDestroy() {
        super.onDestroy();
        if (camera != null)
            camera.disableView();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
    }

    public void onCameraViewStopped() {
    }

    @Override
    public void onResume() {
        super.onResume();
        if (OpenCVLoader.initDebug()) {
            Log.d("OPENCV", "OpenCV loaded successfully");
            baseLoaderCallback = new BaseLoaderCallback(this) {
                @Override
                public void onManagerConnected(int status) {
                    switch (status) {
                        case LoaderCallbackInterface.SUCCESS: {
                            Log.d("OPENCV", "OPENCV loaded successfully");
                            camera.enableView();
                        }
                        break;
                        default: {
                            super.onManagerConnected(status);
                            Log.d("OPENCV", "OPENCV not loaded");
                        }
                        break;
                    }
                }
            };
            baseLoaderCallback.onManagerConnected(BaseLoaderCallback.SUCCESS);
        } else {
            Log.d("OPENCV", "Error in loading OpenCV");
        }

    }


}