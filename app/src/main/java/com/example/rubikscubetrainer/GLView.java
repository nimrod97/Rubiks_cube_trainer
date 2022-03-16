package com.example.rubikscubetrainer;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.example.rubikscubetrainer.matrix.MatrixTrackingGL;

import javax.microedition.khronos.opengles.GL;

public class GLView extends GLSurfaceView {
	private static final String LOG_TAG = GLView.class.getSimpleName();
	private GLRenderer glrenderer;
	private float x0 = 0.0f;
	private float y0 = 0.0f;
	private float x1 = 0.0f;
	private float y1 = 0.0f;
	private boolean scanMode;

	public GLView(Context context, AttributeSet attr) {
		super(context, attr);
		if (context.getClass().toString().endsWith("PlayingWithScannedCube"))
			scanMode = TRUE;
		else
			scanMode = FALSE;
		if (glrenderer != null) return;
		glrenderer = new GLRenderer(getResources().getDisplayMetrics().widthPixels,
				getResources().getDisplayMetrics().heightPixels, context, scanMode);
		setRenderer(glrenderer);
		setGLWrapper(new GLWrapper() {

			@Override
			public GL wrap(GL gl) {
				return new MatrixTrackingGL(gl);
			}
		});
	}

	public GLView(Context context) {
		super(context);
		if (context.getClass().toString().endsWith("PlayingWithScannedCube"))
			scanMode = TRUE;
		else
			scanMode = FALSE;
		if (glrenderer != null) return;
		glrenderer = new GLRenderer(getResources().getDisplayMetrics().widthPixels,
				getResources().getDisplayMetrics().heightPixels, context, scanMode);
		setRenderer(glrenderer);
		setGLWrapper(new GLWrapper() {

			@Override
			public GL wrap(GL gl) {
				return new MatrixTrackingGL(gl);
			}
		});
	}

	public void mix() {
		glrenderer.mix();
	}

	public void cancelMove() {
		glrenderer.cancelMove();
	}

	public boolean onTouchEvent(final MotionEvent event) {
		glrenderer.handleTouch(event);
		return true;
	}

	public void setScanMode(boolean scanMode) {
		this.scanMode = scanMode;
		glrenderer.setScanMode(scanMode);
	}
}
