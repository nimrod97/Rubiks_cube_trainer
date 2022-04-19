package com.example.rubikscubetrainer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.view.MotionEvent;

import com.example.rubikscubetrainer.db.SavedCube;
import com.example.rubikscubetrainer.matrix.MatrixGrabber;
import com.example.rubikscubetrainer.scanning.ScannedCube;
import com.example.rubikscubetrainer.twophasealgorithm.Search;

import org.json.JSONException;

import java.util.Vector;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

//import org.kociemba.twophase.*;

public class GLRenderer implements GLSurfaceView.Renderer {
    private Cube cube;
    public static GLRenderer instance;
    private float cubeX = 0.0f;
    private float cubeY = 0.0f;
    private float cubeZ = 0.0f;
//    public float tx, ty; // Touch coords
//    private float sHeight;

    //-------------------ARCBALL--------------------------
//    private float[] mModelview = new float[16];
//    private float[] mProjection = new float[16];
    private Matrix4f LastRot = new Matrix4f();
    private Matrix4f ThisRot = new Matrix4f();
    private final Object matrixLock = new Object();
    private float[] matrix = new float[16];
    private CArcBall arcBall = new CArcBall(640.0f, 480.0f);  // NEW: ArcBall Instance
    private int[] mViewport = new int[4];
    private GLTextures textures;
    private Context context;
    private int mode;  //  0 - playing with virtual cube generated by the app
    // 1- playing with scanned cube
    // 2- showing one of the scanned cubes from the db
    //-----------------------------------------------------

    public static final String BLUE = "blueside.png";
    public static final String GREEN = "greenside.png";
    public static final String ORANGE = "orangeside.png";
    public static final String RED = "redside.png";
    public static final String WHITE = "whiteside.png";
    public static final String YELLOW = "yellowside.png";

    // constructor
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
    }

    void reset() {
        synchronized (matrixLock) {
            LastRot.setIdentity();   // Reset Rotation
            ThisRot.setIdentity();   // Reset Rotation
        }
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
//        float[] lightAmbient = {1.0f, 1.0f, 1.0f, 1.0f};
//        float[] lightDiffuse = {1.0f, 1.0f, 1.0f, 1.0f};
//        float[] matSpecular = {1.0f, 1.0f, 1.0f, 1.0f};
//        float[] matShines = {5.0f};
//        float[] matAmbient = {1.0f, 1.0f, 1.0f, 1.0f};
//        float[] matDiffuse = {1.0f, 1.0f, 1.0f, 1.0f};
//        float[] lightPost = {0.0f, 5.0f, 10.0f, 1.0f};
        gl.glClearColor(0.8f, 0.8f, 0.8f, 1.0f);  // Set the background's color
//        gl.glClearDepthf(1.0f);            // Set depth's clear-value to farthest
        gl.glEnable(GL10.GL_DEPTH_TEST);   // Enables depth-buffer for hidden surface removal
//        gl.glDepthFunc(GL10.GL_LEQUAL);    // The type of depth testing to do
//        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);  // nice perspective view
//        gl.glShadeModel(GL10.GL_SMOOTH);   // Enable smooth shading of color
        // enable smooth shading

//        gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_SPECULAR, matSpecular, 0);
//        gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_SHININESS, matShines, 0);
//        gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_AMBIENT, matAmbient, 0);
        //   gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_DIFFUSE, matDiffuse, 0);
//        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, lightPost, 0);
//        gl.glEnable(GL10.GL_LIGHTING);
//        gl.glEnable(GL10.GL_LIGHT0);
//        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, lightAmbient, 0);
        //  gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, lightDiffuse, 0);
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
//        if (this.mode == 0)
//            cube.startRandomRotating(20);
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
        else
            cube = new Cube(textures);
    }

    public void mix() {
        cube.startRandomRotating(10 * cube.getCubeSize());
    }

    public void cancelMove() {
        cube.cancelMoves(1);
    }


    //    float[] cl = new float[4];
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

    public void handleTouch(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                selectParts(event.getX(), event.getY());
                if (selectedParts.size() == 0) {
                    startDrag(new Point2f(event.getX(), event.getY()));
                    isDragged = true;
                } else if (mode == 1) {
                    changeColor(selectedParts.get(0));
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isDragged)
                    selectParts(event.getX(), event.getY());
                else
                    drag(new Point2f(event.getX(), event.getY()));
                break;
            case MotionEvent.ACTION_UP:
                isDragged = false;
                deselectParts();
                break;
        }
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
                                ScannedCube.allColors.set(partId, "white");
                                break;
                            case 1:
                                cube.getParts().get(partId).setRectangle(new Rectangle(cube.getQuad(), textures.getTextureIdforResource(GLRenderer.BLUE)));
                                ScannedCube.allColors.set(partId, "blue");
                                break;
                            case 2:
                                cube.getParts().get(partId).setRectangle(new Rectangle(cube.getQuad(), textures.getTextureIdforResource(GLRenderer.RED)));
                                ScannedCube.allColors.set(partId, "red");
                                break;
                            case 3:
                                cube.getParts().get(partId).setRectangle(new Rectangle(cube.getQuad(), textures.getTextureIdforResource(GLRenderer.GREEN)));
                                ScannedCube.allColors.set(partId, "green");
                                break;
                            case 4:
                                cube.getParts().get(partId).setRectangle(new Rectangle(cube.getQuad(), textures.getTextureIdforResource(GLRenderer.YELLOW)));
                                ScannedCube.allColors.set(partId, "yellow");
                                break;
                            default:
                                cube.getParts().get(partId).setRectangle(new Rectangle(cube.getQuad(), textures.getTextureIdforResource(GLRenderer.ORANGE)));
                                ScannedCube.allColors.set(partId, "orange");
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
                //pps.add(p);
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
                //		Log.i("selected part is: ", String.valueOf(selId));
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

    float[] collisionPoint = {0f, 0f, 0f};
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

    public float distance(float[] a, float[] b) {
        float abx = b[0] - a[0];
        float aby = b[1] - a[1];
        float abz = b[2] - a[2];
        float distance = (float) Math.sqrt(abx * abx + aby * aby + abz * abz);

        return distance;
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

    public void solve() {
//        String cubeString = cube.getStringRepresentation();

        String result = Search.solution("UUUUUUUUUFFFRRRRRRLLLFFFFFFDDDDDDDDDBBBLLLLLLRRRBBBBBB", 24, 5000, false);
        System.out.println(result);
    }
}
