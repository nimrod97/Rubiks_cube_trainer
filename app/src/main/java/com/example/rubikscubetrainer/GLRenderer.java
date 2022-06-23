package com.example.rubikscubetrainer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.rubikscubetrainer.activities.CubeGLActivity;
import com.example.rubikscubetrainer.activities.PlayingOptionsActivity;
import com.example.rubikscubetrainer.db.SavedCube;
import com.example.rubikscubetrainer.matrix.MatrixGrabber;
import com.example.rubikscubetrainer.scanning.PlayingWithScannedCube;
import com.example.rubikscubetrainer.scanning.ScannedCube;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GLRenderer extends Activity implements GLSurfaceView.Renderer {
    private Cube cube;
    public static GLRenderer instance;
    private float cubeX = 0.0f;
    private float cubeY = 0.0f;
    private float cubeZ = 0.0f;
    private OkHttpClient okHttpClient;
    private boolean saveCubeFlag = false;
    private boolean solveFlag = false;
    private boolean solvedByMyselfFlag = false;


    //-------------------ARCBALL--------------------------
    private Matrix4f LastRot = new Matrix4f();
    private Matrix4f ThisRot = new Matrix4f();
    private final Object matrixLock = new Object();
    private float[] matrix = new float[16];
    private CArcBall arcBall = new CArcBall(640.0f, 480.0f);  // NEW: ArcBall Instance
    private int[] mViewport = new int[4];
    private GLTextures textures;
    private Context context;
    private int mode;  //  0 - playing with virtual cube generated by the app
    // 1- verify scanned colors
    // 2- showing one of the scanned cubes from the db
    // 3- playing with scanned cube after verifying the colors

    public static final String BLUE = "blueside.png";
    public static final String GREEN = "greenside.png";
    public static final String ORANGE = "orangeside.png";
    public static final String RED = "redside.png";
    public static final String WHITE = "whiteside.png";
    public static final String YELLOW = "yellowside.png";

    public GLRenderer(float width, float height, Context context, int mode) {
        super();
        // Start Of User Initialization
        LastRot.setIdentity();                                // Reset Rotation
        ThisRot.setIdentity();                                // Reset Rotation
        ThisRot.get(matrix);
        arcBall.setBounds(width, height);
        this.context = context;
        instance = this;
        this.mode = mode;
        okHttpClient = new OkHttpClient();

    }


    void startDrag(Point2f MousePt) {
        synchronized (matrixLock) {
            LastRot.set(ThisRot);  // Set Last Static Rotation To Last Dynamic One
        }
        arcBall.click(MousePt);    // Update Start Vector And Prepare For Dragging
    }

    void drag(Point2f MousePt) {       // Perform Motion Updates Here
        Quat4f ThisQuat = new Quat4f();

        // Update End Vector And Get Rotation As Quaternion
        arcBall.drag(MousePt, ThisQuat);
        synchronized (matrixLock) {
            ThisRot.setRotation(ThisQuat);  // Convert Quaternion Into Matrix3fT
            ThisRot.mul(ThisRot, LastRot); // Accumulate Last Rotation Into This One
        }
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glClearColor(0.8f, 0.8f, 0.8f, 1.0f);  // Set the background's color
        gl.glEnable(GL10.GL_DEPTH_TEST);   // Enables depth-buffer for hidden surface removal
        textures = new GLTextures(gl, context);
        textures.add(WHITE);
        textures.add(YELLOW);
        textures.add(BLUE);
        textures.add(GREEN);
        textures.add(RED);
        textures.add(ORANGE);
        textures.loadTextures();
        try {
            createCube();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (this.mode == 0) {
            cube.startRandomRotating(20);
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        aspect = (float) width / height;
        nScreenWidth = width;
        nScreenHeight = height;
        // Set the viewport (display area) to cover the entire window
        gl.glViewport(0, 0, width, height);
        if (gl instanceof GL11) {
            gl.glGetIntegerv(GL11.GL_VIEWPORT, mViewport, 0);
        }

        arcBall.setBounds((float) width, (float) height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        GLU.gluPerspective(gl, fFOV, aspect, fNear, fFar);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        GLU.gluLookAt(gl, posCamera[0], posCamera[1], posCamera[2], posLook[0], posLook[1], posLook[2], 0, 0, 1);
    }

    public void createCube() throws JSONException {
        if (this.mode == 1)
            cube = new ScannedCube(textures);
        else if (this.mode == 2)
            cube = new SavedCube(textures);
        else if (mode == 0)
            cube = new Cube(textures);
    }

    public void mix() {
        cube.startRandomRotating(20);
    }

    public void cancelMove() {
        cube.cancelMoves(1);
    }


    public int nScreenWidth, nScreenHeight;
    public float fFOV = 45f;
    public float aspect;
    public float fNear = 0.1f;
    public float fFar = 100.0f;
    public float[] posCamera = new float[4];
    public float[] posLook = new float[4];

    private void drawScene(GL10 gl, int mode) {
        synchronized (matrixLock) {
            ThisRot.get(matrix);
        }
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        gl.glLoadIdentity();                  // Reset The Current Modelview Matrix
        mg.getCurrentState(gl);
        gl.glTranslatef(0.0f, 0.0f, -3.5f * cube.getCubeSize());

        gl.glMultMatrixf(matrix, 0);        // NEW: Apply Dynamic Transform
        gl.glTranslatef(cubeX, cubeY, cubeZ);

        gl.glRotatef(30.0f, 1.0f, 1.0f, 0.0f);
        cube.drawCube(gl, 9999);
    }

    private boolean isDragged = false;


    public void setMode(int mode) {
        this.mode = mode;
    }

    public boolean getSolveFlag() {
        return solveFlag;
    }

    public void setSolveFlag(boolean flag) {
        this.solveFlag = flag;
    }

    public boolean isSaveCubeFlag() {
        return saveCubeFlag;
    }

    public void setSaveCubeFlag(boolean flag) {
        this.saveCubeFlag = flag;
    }

    public void handleTouch(MotionEvent event) {
        if (cube.getCubeRepresentationByFaces().equals("UUUUUUUUURRRRRRRRRFFFFFFFFFDDDDDDDDDLLLLLLLLLBBBBBBBBB") && !solveFlag && mode != 1 && !solvedByMyselfFlag)
            solvedByMyself();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                selectParts(event.getX(), event.getY());
                if (selectedParts.size() == 0) {
                    startDrag(new Point2f(event.getX(), event.getY()));
                    isDragged = true;
                } else if (mode == 1) {
                    changeColor(selectedParts.get(0));
                } else if (mode == 0 && !saveCubeFlag) {
                    saveCubeState();
                    saveCubeFlag = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isDragged)
                    selectParts(event.getX(), event.getY());
                else
                    drag(new Point2f(event.getX(), event.getY()));
                break;
            case MotionEvent.ACTION_UP:
                if (cube.getCubeRepresentationByFaces().equals("UUUUUUUUURRRRRRRRRFFFFFFFFFDDDDDDDDDLLLLLLLLLBBBBBBBBB") && !solveFlag && mode != 1 && !solvedByMyselfFlag)
                    solvedByMyself();
                isDragged = false;
                deselectParts();
                break;
        }

    }

    public void solvedByMyself() {
        solvedByMyselfFlag = true;
        if (mode == 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    CubeGLActivity.solveAloneGreeting.setVisibility(View.VISIBLE);
                    CubeGLActivity.solveBtn.setVisibility(View.INVISIBLE);
                    CubeGLActivity.replayBtn.setVisibility(View.VISIBLE);
                    CubeGLActivity.backToHomeBtn.setVisibility(View.VISIBLE);
                    CubeGLActivity.shuffleBtn.setVisibility(View.INVISIBLE);
                    CubeGLActivity.undoBtn.setVisibility(View.INVISIBLE);
                    CubeGLActivity.playBtn.setVisibility(View.INVISIBLE);
                    CubeGLActivity.pauseBtn.setVisibility(View.INVISIBLE);
                    CubeGLActivity.slider.setVisibility(View.INVISIBLE);
                    CubeGLActivity.sliderText.setVisibility(View.INVISIBLE);
                }
            });

        } else if (mode == 3) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    PlayingWithScannedCube.solveAloneGreeting.setVisibility(View.VISIBLE);
                    PlayingWithScannedCube.solveBtn.setVisibility(View.INVISIBLE);
                    PlayingWithScannedCube.replayBtn.setVisibility(View.VISIBLE);
                    PlayingWithScannedCube.backToHomeBtn.setVisibility(View.VISIBLE);
                    PlayingWithScannedCube.undoBtn.setVisibility(View.INVISIBLE);
                    PlayingWithScannedCube.playBtn.setVisibility(View.INVISIBLE);
                    PlayingWithScannedCube.pauseBtn.setVisibility(View.INVISIBLE);
                    PlayingWithScannedCube.slider.setVisibility(View.INVISIBLE);
                    PlayingWithScannedCube.sliderText.setVisibility(View.INVISIBLE);
                }
            });
        }
        RequestBody formbody = new FormBody.Builder()
                .add("username", PlayingOptionsActivity.username)
                .build();
        Request request = new Request.Builder().url("https://rubiks-cube-server-oh2xye4svq-oa.a.run.app/solved_by_myself")
                .post(formbody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "server down", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                response.close();
            }
        });

    }

    public void saveCubeState() {
        List<String> colors = cube.getColors();
        RequestBody formbody = new FormBody.Builder()
                .add("username", PlayingOptionsActivity.username)
                .add("generatedColors", String.join(",", colors))
                .add("override", "false")
                .build();
        Request request = new Request.Builder().url("https://rubiks-cube-server-oh2xye4svq-oa.a.run.app/save_cube_state")
                .post(formbody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "server down", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                response.close();
            }
        });
    }

    public void changeColor(int partId) {
        String[] colors = new String[]{"white", "blue", "red", "green", "yellow", "orange"};
        new AlertDialog.Builder(context).setSingleChoiceItems(colors, 0, null)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        long chosenColor = ((AlertDialog) dialog).getListView().getCheckedItemIds()[0];
                        switch ((int) (chosenColor)) {
                            case 0:
                                cube.getParts().get(partId).setRectangle(new Rectangle(cube.getQuad(), textures.getTextureIdforResource(GLRenderer.WHITE)));
                                ScannedCube.allColors.set(partId, 'W');
                                break;
                            case 1:
                                cube.getParts().get(partId).setRectangle(new Rectangle(cube.getQuad(), textures.getTextureIdforResource(GLRenderer.BLUE)));
                                ScannedCube.allColors.set(partId, 'B');
                                break;
                            case 2:
                                cube.getParts().get(partId).setRectangle(new Rectangle(cube.getQuad(), textures.getTextureIdforResource(GLRenderer.RED)));
                                ScannedCube.allColors.set(partId, 'R');
                                break;
                            case 3:
                                cube.getParts().get(partId).setRectangle(new Rectangle(cube.getQuad(), textures.getTextureIdforResource(GLRenderer.GREEN)));
                                ScannedCube.allColors.set(partId, 'G');
                                break;
                            case 4:
                                cube.getParts().get(partId).setRectangle(new Rectangle(cube.getQuad(), textures.getTextureIdforResource(GLRenderer.YELLOW)));
                                ScannedCube.allColors.set(partId, 'Y');
                                break;
                            default:
                                cube.getParts().get(partId).setRectangle(new Rectangle(cube.getQuad(), textures.getTextureIdforResource(GLRenderer.ORANGE)));
                                ScannedCube.allColors.set(partId, 'O');
                                break;
                        }

                    }
                })
                .show();
    }

    public void deselectParts() {
        for (Part p : cube.getParts()) {
            p.setSelected(false);
        }
        selectedParts.clear();
    }

    Vector<Integer> selectedParts = new Vector<Integer>();

    public boolean selectParts(float x, float y) {
        float[] ray = getViewRay(x, y);
        if (cube.getStatus() != 'N') return false;
        Vector<Part> parts = cube.getParts();
        Part selectedPart = null;
        int selId = -1;
        for (int i = 0; i < parts.size(); i++) {
            Part p = parts.get(i);
            if (collision(p, ray)) {
                if (selectedPart == null) {
                    selectedPart = p;
                    selId = i;
                    break;
                } else {
                    if (Math.round(p.z * 100000) > Math.round(selectedPart.z * 100000)) {
                        selectedPart = p;
                        selId = i;
                    }
                }
            }
        }
        if (selectedPart != null) {
            if (!selectedPart.isSelected() && !selectedParts.contains(selId)) {
                selectedParts.add(selId);
                if (selectedParts.size() >= 2) {
                    if (cube.checkSidesForRotating(selectedParts)) {
                        deselectParts();
                    }
                }
            }
        }

        return selectedPart != null;
    }

    float[] collisionPointS = {0f, 0f, 0f};
    float[] collisionPointE = {0.0f, 0.0f, 0.0f};
    private static int RAY_ITERATIONS = 1000;

    public boolean collision(Part p, float[] rayVector) {
        float length = rayVector[3];

        collisionPointS[0] = rayVector[0] * length / RAY_ITERATIONS * 0;
        collisionPointS[1] = rayVector[1] * length / RAY_ITERATIONS * 0;
        collisionPointS[2] = rayVector[2] * length / RAY_ITERATIONS * 0;

        collisionPointE[0] = rayVector[0] * length / RAY_ITERATIONS * RAY_ITERATIONS;
        collisionPointE[1] = rayVector[1] * length / RAY_ITERATIONS * RAY_ITERATIONS;
        collisionPointE[2] = rayVector[2] * length / RAY_ITERATIONS * RAY_ITERATIONS;

        return Helper.hitWithTriangle(p.polygon[0], p.polygon[1], p.polygon[2], collisionPointS, collisionPointE)
                || Helper.hitWithTriangle(p.polygon[1], p.polygon[2], p.polygon[3], collisionPointS, collisionPointE)
                || Helper.hitWithTriangle(p.polygon[2], p.polygon[3], p.polygon[0], collisionPointS, collisionPointE)
                || Helper.hitWithTriangle(p.polygon[3], p.polygon[0], p.polygon[1], collisionPointS, collisionPointE);
    }

    MatrixGrabber mg = new MatrixGrabber();

    public float[] getViewRay(float x, float y) {
        // view port
        int[] viewport = {0, 0, GLRenderer.instance.nScreenWidth, GLRenderer.instance.nScreenHeight};

        // far eye point
        float[] nearPoint = {0f, 0f, 0f, 0f};
        float[] farPoint = {0f, 0f, 0f, 0f};
        float[] rayVector = {0f, 0f, 0f, 0f};
        y = GLRenderer.instance.nScreenHeight - y;

        //Retreiving position projected on near plane
        GLU.gluUnProject(x, y, -1f, mg.mModelView, 0, mg.mProjection, 0, viewport, 0, nearPoint, 0);

        //Retreiving position projected on far plane
        GLU.gluUnProject(x, y, 1f, mg.mModelView, 0, mg.mProjection, 0, viewport, 0, farPoint, 0);

        // extract 3d Coordinates put of 4d Coordinates
        nearPoint = Helper.fixW(nearPoint);
        farPoint = Helper.fixW(farPoint);
        //Processing ray vector
        rayVector[0] = farPoint[0] - nearPoint[0];
        rayVector[1] = farPoint[1] - nearPoint[1];
        rayVector[2] = farPoint[2] - nearPoint[2];

        // calculate ray vector length
        float rayLength = (float) Math.sqrt((rayVector[0] * rayVector[0]) + (rayVector[1] * rayVector[1]) + (rayVector[2] * rayVector[2]));

        //normalizing ray vector
        rayVector[0] /= rayLength;
        rayVector[1] /= rayLength;
        rayVector[2] /= rayLength;
        rayVector[3] = rayLength;

        return rayVector;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        drawScene(gl, GL10.GL_MODELVIEW);
    }

    public Cube getCube() {
        return cube;
    }

}
