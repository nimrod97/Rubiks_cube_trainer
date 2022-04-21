package com.example.rubikscubetrainer;

import org.json.JSONException;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

public class Cube {

	private int mode; // GL_SELECT - selection mode
	private float speed; // speed of rotating
	private float rotateAngle; // current angle of rotation
	private float sizeOfQuad;
	private float sizeGl; // the size of +side projection on any axis
	private char status; //  N - normal status, R - rotating, A - autorotating,  C - canceling moves
	private boolean isClockWise;
	private int rotSide;
	private int dimOfCube; // the dimension of the cube
	private Vector<Integer> sides;
	protected Vector<Part> parts;
	private Vector<Vector<Float>> dxyz; // translating of parts. has size (HZ,3)
	private Vector<Vector<Integer>> faceFirst; // second - number of parts which make the whole cube
	private Vector<Vector<Integer>> faceSecond; // ---- //
	private Vector<Vector<Integer>> faceThird;  // ---- //
	private Vector<Vector<Pair<Integer, Integer>>> swaps; // swaping parts during i rotating
	private Vector<Pair<Integer, Integer>> oSides; // rotating sides that contain i-part
	private Vector<Pair<Integer, Boolean>> movingSides; // the moves that we want to do
	private Stack<Pair<Integer, Boolean>> madeMoves; // the moves that have been made
	protected GLTextures textures;

	public Vector<Part> getParts() {
		return parts;
	}

	protected void fillOSides(int size) {
		oSides = new Vector<Pair<Integer, Integer>>();
		//----------------White [0] - side--------------//
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++) {
				oSides.add(new Pair(j, size + i));
			}
		//----------------Red [1] - side--------------//
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++) {
				oSides.add(new Pair(size + i, size * 2 + size - j - 1));
			}
		//--------------Yellow [2] - side-------------//
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++) {
				oSides.add(new Pair(size - j - 1, size + i));
			}
		//--------------Orange [3] - side-------------//
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++) {
				oSides.add(new Pair(size + i, size * 2 + j));
			}
		//--------------Green [4] - side-------------//
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++) {
				oSides.add(new Pair(j, size * 2 + size - i - 1));
			}
		//--------------Blue [5] - side-------------//
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++) {
				oSides.add(new Pair(j, size * 2 + i));
			}
	}

	protected void fillDXYZ(int size) {
		sizeOfQuad = 1.0f;
//		sizeGl=1.0f;
		sizeGl = (sizeOfQuad * size) / 2.0f;
		// Fill White side[0]
		float dx, dy, dz;
		Vector<Float> txyz = new Vector<Float>(3); // temporary
		dxyz = new Vector<Vector<Float>>();
		// now we fill the distance of translation for every quad;
		// Front Side
		dz  = sizeGl;
		for (dy = sizeGl - sizeOfQuad; dy >= -sizeGl; dy -= sizeOfQuad)
			for (dx = -sizeGl; dx <= sizeGl - sizeOfQuad; dx += sizeOfQuad) {
				txyz.add(0, dx);
				txyz.add(1, dy);
				txyz.add(2, dz);
				dxyz.add(new Vector<Float>(txyz));
				txyz.clear();
			}
		// Left Side
		dx = -sizeGl;
		for (dy = sizeGl - sizeOfQuad; dy >= -sizeGl; dy -= sizeOfQuad)
			for (dz = -sizeGl; dz <= sizeGl - sizeOfQuad; dz += sizeOfQuad) {
				txyz.add(0, dx);
				txyz.add(1, dy);
				txyz.add(2, dz);
				dxyz.add(new Vector<Float>(txyz));
				txyz.clear();
			}
		// Back Side
		dz = -sizeGl;
		for (dy = sizeGl - sizeOfQuad; dy >= -sizeGl; dy -= sizeOfQuad)
			for (dx = sizeGl; dx >= -sizeGl + sizeOfQuad; dx -= sizeOfQuad) {
				txyz.add(0, dx);
				txyz.add(1, dy);
				txyz.add(2, dz);
				dxyz.add(new Vector<Float>(txyz));
				txyz.clear();
			}
		// Right Side
		dx = sizeGl;
		for (dy = sizeGl - sizeOfQuad; dy >= -sizeGl; dy -= sizeOfQuad)
			for (dz = sizeGl; dz >= -sizeGl + sizeOfQuad; dz -= sizeOfQuad) {
				txyz.add(0, dx);
				txyz.add(1, dy);
				txyz.add(2, dz);
				dxyz.add(new Vector<Float>(txyz));
				txyz.clear();
			}
		// Bottom Side
		dy = sizeGl;
		for (dz = -sizeGl + sizeOfQuad; dz <= sizeGl; dz += sizeOfQuad)
			for (dx = -sizeGl; dx <= sizeGl - sizeOfQuad; dx += sizeOfQuad) {
				txyz.add(0, dx);
				txyz.add(1, dy);
				txyz.add(2, dz);
				dxyz.add(new Vector<Float>(txyz));
				txyz.clear();
			}
		// Top side
		dy = -sizeGl;
		for (dz = sizeGl - sizeOfQuad; dz >= -sizeGl; dz -= sizeOfQuad)
			for (dx = -sizeGl; dx <= sizeGl - sizeOfQuad; dx += sizeOfQuad) {
				txyz.add(0, dx);
				txyz.add(1, dy);
				txyz.add(2, dz);
				dxyz.add(new Vector<Float>(txyz));
				txyz.clear();
			}
	}

	protected void fillParts(int size) throws JSONException {
		parts = new Vector<Part>();
		Rectangle rectWhite =
				new Rectangle(quad, textures.getTextureIdforResource(GLRenderer.WHITE));
		int id = 1;
		for (int i = 0; i < size * size; i++)
			parts.add(new Part(MyOpenGL.white, id++, rectWhite));
		Rectangle rectBlue =
				new Rectangle(quad, textures.getTextureIdforResource(GLRenderer.BLUE));
		for (int i = 0; i < size * size; i++)
			parts.add(new Part(MyOpenGL.red, id++, rectBlue));
		Rectangle rectYellow =
				new Rectangle(quad, textures.getTextureIdforResource(GLRenderer.YELLOW));
		for (int i = 0; i < size * size; i++)
			parts.add(new Part(MyOpenGL.yellow, id++, rectYellow));
		Rectangle rectGreen =
				new Rectangle(quad, textures.getTextureIdforResource(GLRenderer.GREEN));
		for (int i = 0; i < size * size; i++)
			parts.add(new Part(MyOpenGL.orange, id++, rectGreen));
		Rectangle rectRed =
				new Rectangle(quad, textures.getTextureIdforResource(GLRenderer.RED));
		for (int i = 0; i < size * size; i++)
			parts.add(new Part(MyOpenGL.green, id++, rectRed));
		Rectangle rectOrange =
				new Rectangle(quad, textures.getTextureIdforResource(GLRenderer.ORANGE));
		for (int i = 0; i < size * size; i++)
			parts.add(new Part(MyOpenGL.blue, id++, rectOrange));
	}

	private void createFaces(int size) {
		createFirstFace(size);
		createSecondFace(size);
		createThirdFace(size);
	}

	protected void createFirstFace(int size) {
		faceFirst = new Vector<Vector<Integer>>();
		Vector<Integer> tmp = new Vector<Integer>();
		int doubleSize = size * size;
		for (int i = 0; i < doubleSize; i++) {
			tmp.add(doubleSize + i);
		}
		for (int j = 0; j < size; j++) {
			for (int i = 0; i < size; i++) {
				tmp.add(size * i + j);
				tmp.add(j + 4 * doubleSize + size * i);
				tmp.add(j + 5 * doubleSize + size * i);
				tmp.add(2 * doubleSize + size * i + size - 1 - j);
			}
			if (j != size - 1) {
				faceFirst.add(new Vector<Integer>(tmp));
				tmp.clear();
			}
		}
		for (int i = 0; i < doubleSize; i++) {
			tmp.add(3 * doubleSize + i);
		}
		faceFirst.add(new Vector<Integer>(tmp));
		tmp.clear();
	}

	protected void createSecondFace(int size) {
		Vector<Integer> tmp = new Vector<Integer>();
		faceSecond = new Vector<Vector<Integer>>();
		int doubleSize = size * size;
		for (int i = 0; i < doubleSize; i++) {
			tmp.add(4 * doubleSize + i);
		}
		for (int j = 0; j < size; j++) {
			for (int i = 0; i < size; i++) {
				tmp.add(size * j + i);
				tmp.add(doubleSize + size * j + i);
				tmp.add(2 * doubleSize + size * j + i);
				tmp.add(3 * doubleSize + size * j + i);
			}
			if (j != size - 1) {
				faceSecond.add(new Vector<Integer>(tmp));
				tmp.clear();
			}
		}
		for (int i = 0; i < doubleSize; i++) {
			tmp.add(5 * doubleSize + i);
		}
		faceSecond.add(new Vector<Integer>(tmp));
		tmp.clear();
	}

	protected void createThirdFace(int size) {
		Vector<Integer> tmp = new Vector<Integer>();
		faceThird = new Vector<Vector<Integer>>();
		int doubleSize = size * size;
		for (int i = 0; i < doubleSize; i++) {
			tmp.add(i);
		}
		for (int j = 0; j < size; j++) {
			for (int i = 0; i < size; i++) {
				tmp.add(4 * doubleSize + size * (size - j - 1) + i);
				tmp.add(3 * doubleSize + size * i + j);
				tmp.add(doubleSize + size - 1 - j + size * i);
				tmp.add(5 * doubleSize + size * j + i);
			}
			if (j != size - 1) {
				faceThird.add(new Vector<Integer>(tmp));
				tmp.clear();
			}
		}
		for (int i = 0; i < doubleSize; i++) {
			tmp.add(2 * doubleSize + i);
		}
		faceThird.add(new Vector<Integer>(tmp));
		tmp.clear();
	}

	protected void createSwaps(int size) {
		swaps = new Vector<Vector<Pair<Integer, Integer>>>();
		createFirstTypeSwap(size);
		createSecondTypeSwap(size);
		createThirdTypeSwap(size);
	}

	protected void createFirstTypeSwap(int size) {
		Vector<Pair<Integer, Integer>> tmp = new Vector<Pair<Integer, Integer>>();
		//-----------------First type of rotating swaps-----------------//
		//first we should rotate matrix of the main side on 90 degrees
		int doubleSize = size * size;
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++) {
				tmp.add(
						new Pair(1 * doubleSize + size * j + i,
								1 * doubleSize + size * i + size - j - 1)
				);
			}
		// then swap middle parts
		for (int j = 0; j < size; j++) {
			for (int i = 0; i < size; i++) {
				tmp.add(
						new Pair(4 * doubleSize + i * size + j,
								0 * doubleSize + i * size + j)
				);
				tmp.add(new Pair(0 * doubleSize + i * size + j,
						5 * doubleSize + i * size + j)
				);
				tmp.add(new Pair(5 * doubleSize + i * size + j,
						2 * doubleSize + doubleSize - 1 - i * size - j)
				);
				tmp.add(new Pair(2 * doubleSize + doubleSize - 1 - i * size - j,
						4 * doubleSize + i * size + j)
				);

			}
			if (j != size - 1) {
				swaps.add(new Vector<Pair<Integer, Integer>>(tmp));
				tmp.clear();
			}
		}
		// then swap last one
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++) {
				tmp.add(
						new Pair(3 * doubleSize + size * i + size - j - 1,
								3 * doubleSize + size * j + i)
				);
			}
		swaps.add(new Vector<Pair<Integer, Integer>>(tmp));
		tmp.clear();
	}

	protected void createSecondTypeSwap(int size) {
		Vector<Pair<Integer, Integer>> tmp = new Vector<Pair<Integer, Integer>>();
		//-----------------Second type of rotating swaps-----------------//
		int doubleSize = size * size;
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++) {
				tmp.add(
						new Pair(4 * doubleSize + size * i + size - j - 1,
								4 * doubleSize + size * j + i)
				);
			}
		for (int j = 0; j < size; j++) {
			for (int i = 0; i < size; i++) {
				tmp.add(new Pair(0 * doubleSize + i + j * size,
						3 * doubleSize + i + j * size)
				);
				tmp.add(new Pair(3 * doubleSize + i + j * size,
						2 * doubleSize + i + j * size)
				);
				tmp.add(new Pair(2 * doubleSize + i + j * size,
						1 * doubleSize + i + j * size)
				);
				tmp.add(new Pair(1 * doubleSize + i + j * size,
						0 * doubleSize + i + j * size)
				);
			}
			if (j != size - 1) {
				swaps.add(new Vector<Pair<Integer, Integer>>(tmp));
				tmp.clear();
			}
		}
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++) {
				tmp.add(
						new Pair(5 * doubleSize + size * j + i,
								5 * doubleSize + size * i + size - j - 1)
				);
			}
		swaps.add(new Vector<Pair<Integer, Integer>>(tmp));
		tmp.clear();
	}

	protected void createThirdTypeSwap(int size) {
		Vector<Pair<Integer, Integer>> tmp = new Vector<Pair<Integer, Integer>>();
		//-----------------Third type of rotating swaps-----------------//
		int doubleSize = size * size;
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++) {
				tmp.add(
						new Pair(0 * doubleSize + size * i + size - j - 1,
								0 * doubleSize + size * j + i)
				);
			}
		for (int j = 0; j < size; j++) {
			for (int i = 0; i < size; i++) {
				tmp.add(new Pair(3 * doubleSize + i * size + j,
						4 * doubleSize + (size - j - 1) * size + i)
				);
				/*
				 I didn`t really understand why i need swap(),
				 but without it it works incorrectly
				*/
				tmp.add(new Pair(5 * doubleSize + j * size + size - i - 1,
						3 * doubleSize + i * size + j)
				);
				tmp.add(new Pair(1 * doubleSize + (size - i - 1) * size + size - j - 1,
						5 * doubleSize + j * size + size - i - 1)
				);
				tmp.add(new Pair(4 * doubleSize + (size - j - 1) * size + i,
						1 * doubleSize + (size - i - 1) * size + size - j - 1)
				);
			}
			if (j != size - 1) {
				swaps.add(new Vector<Pair<Integer, Integer>>(tmp));
				tmp.clear();
			}
		}
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++) {
				tmp.add(
						new Pair(2 * doubleSize + size * j + i,
								2 * doubleSize + size * i + size - j - 1)
				);
			}
		swaps.add(new Vector<Pair<Integer, Integer>>(tmp));
		tmp.clear();
	}

	protected void drawType(GL10 gl, Vector<Vector<Integer>> face, int hzside) {
		int curSide = rotSide % dimOfCube;
		for (int i = 0; i < face.size(); i++) {
			if (i == curSide) {
				gl.glPushMatrix();
				switch (hzside) {
					case 0:
						gl.glRotatef(rotateAngle, 1.0f, 0.0f, 0.0f);
						break;
					case 1:
						gl.glRotatef(rotateAngle, 0.0f, 1.0f, 0.0f);
						break;
					case 2:
						gl.glRotatef(rotateAngle, 0.0f, 0.0f, 1.0f);
						break;
				}
			}
			for (int j = 0; j < face.get(i).size(); j++) {
				int current = face.get(i).get(j);
				int nOfSide = current / dimOfCube / dimOfCube;
				rotateGL(gl, nOfSide, current);
			}
			if (i == curSide) {
				if (isClockWise) rotateAngle += speed;
				else rotateAngle -= speed;
				if (Math.abs(rotateAngle) >= 90) {
					endRotate();
					if (status == 'A' || status == 'C') {
						int s = movingSides.size();
						if (s != 0) {
							Pair<Integer, Boolean> p = movingSides.get(s - 1);
							beginRotate(p.getFirst(), p.getSecond());
						}
						else
							status = 'N';
					}
				}
				gl.glPopMatrix();
			}
		}
	}

	protected void endRotate() {
		if (status != 'A' && status != 'C') {
			status = 'N';
		}
		if (status != 'C' && status != 'A') {
			madeMoves.add(new Pair(rotSide, isClockWise));
		}
		rotateAngle = 0;
		rotate(rotSide, isClockWise);
		rotSide = -1;
	}

	protected void rotate(int rotSide, boolean isClockWise) {
		Vector<Part> partsTmp = new Vector<Part>(parts);
		for (int i = 0; i < swaps.get(rotSide).size(); i++) {
			if (isClockWise) {
				parts.removeElementAt(swaps.get(rotSide).get(i).getSecond());
				parts.add(swaps.get(rotSide).get(i).getSecond(),
						partsTmp.get(swaps.get(rotSide).get(i).getFirst()));
			} else {
				parts.removeElementAt(swaps.get(rotSide).get(i).getFirst());
				parts.add(swaps.get(rotSide).get(i).getFirst(),
						partsTmp.get(swaps.get(rotSide).get(i).getSecond()));
				//parts[swaps[rotSide][i].first] = partsTmp[swaps[rotSide][i].second];
			}
		}
	}

	protected boolean isClockWiseDirection(Vector<Integer> setOfSides, int choosenSide) {
		if (choosenSide >= 0 && choosenSide <= dimOfCube - 1) {
			if (sides.get(setOfSides.get(0)) == sides.get(setOfSides.get(1))) {
				if (sides.get(setOfSides.get(0)) != 2)
					return (setOfSides.get(0) < setOfSides.get(1));
				else return (setOfSides.get(0) > setOfSides.get(1));
			} else {
				if ((sides.get(setOfSides.get(0)) == 0 && sides.get(setOfSides.get(1)) == 5)
						|| (sides.get(setOfSides.get(0)) == 5 && sides.get(setOfSides.get(1)) == 2)
						|| (sides.get(setOfSides.get(0)) == 2 && sides.get(setOfSides.get(1)) == 4)
						|| (sides.get(setOfSides.get(0)) == 4 && sides.get(setOfSides.get(1)) == 0)
				) return true;
				else return false;
			}
		}
		if (choosenSide >= dimOfCube && choosenSide <= 2 * dimOfCube - 1) {
			if (sides.get(setOfSides.get(0)) == sides.get(setOfSides.get(1))) {
				if (sides.get(setOfSides.get(0)) != 2)
					return (setOfSides.get(0) < setOfSides.get(1));
				else return (setOfSides.get(0) < setOfSides.get(1));
			} else {
				if ((sides.get(setOfSides.get(0)) == 0 && sides.get(setOfSides.get(1)) == 3)
						|| (sides.get(setOfSides.get(0)) == 3 && sides.get(setOfSides.get(1)) == 2)
						|| (sides.get(setOfSides.get(0)) == 2 && sides.get(setOfSides.get(1)) == 1)
						|| (sides.get(setOfSides.get(0)) == 1 && sides.get(setOfSides.get(1)) == 0)
				) return true;
				else return false;
			}
		}
		if (choosenSide >= 2 * dimOfCube && choosenSide <= 3 * dimOfCube - 1) {
			if (sides.get(setOfSides.get(0)) == sides.get(setOfSides.get(1))) {
				if (sides.get(setOfSides.get(0)) != 1 && sides.get(setOfSides.get(0)) != 5)
					return (setOfSides.get(0) > setOfSides.get(1));
				else
					return (setOfSides.get(0) < setOfSides.get(1));
			} else {
				if ((sides.get(setOfSides.get(0)) == 4 && sides.get(setOfSides.get(1)) == 3)
						|| (sides.get(setOfSides.get(0)) == 3 && sides.get(setOfSides.get(1)) == 5)
						|| (sides.get(setOfSides.get(0)) == 5 && sides.get(setOfSides.get(1)) == 1)
						|| (sides.get(setOfSides.get(0)) == 1 && sides.get(setOfSides.get(1)) == 4)
				) return false;
				else return true;
			}
		}
		return true;
	}

	/* =================public declarations====================== */
	public int getCubeSize() {
		return dimOfCube;
	}

	protected float[] quad =
			{
					0.0f, 0.0f, 0.0f,
					1.0f, 0.0f, 0.0f,
					1.0f, 1.0f, 0.0f,
					0.0f, 1.0f, 0.0f

			};

	public Cube(GLTextures textures) throws JSONException {
		this.textures = textures;
		dimOfCube = 3;
		status = 'N';
		speed = 10.0f;
		mode = GL10.GL_MODELVIEW;
		sides = new Vector<Integer>();
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < dimOfCube * dimOfCube; j++) {
				//tmp.add(current++);
				sides.add(i);
			}
			//	sidesIDColors.push_back(tmp);
			//tmp.clear();
		}
		madeMoves = new Stack<Pair<Integer, Boolean>>();
		movingSides = new Vector<Pair<Integer, Boolean>>();
		// fill parts with parameters
		createFaces(dimOfCube);
		createSwaps(dimOfCube);
		fillParts(dimOfCube);
		fillDXYZ(dimOfCube);
		fillOSides(dimOfCube);
	}

	private Dictionary<Character, Float> calcRotation(int side) {
		Dictionary<Character, Float> angles = new Hashtable<>();
		angles.put('x', 0.0f);
		angles.put('y', 0.0f);
		angles.put('z', 0.0f);
		switch (side) {
			case 0: // white
				break;
			case 1: // red
				angles.put('y', -90.0f);
				break;
			case 2: // yellow
				angles.put('y', 180.0f);
				break;
			case 3: // orange
				angles.put('y', 90.0f);
				break;
			case 4: // green
				angles.put('x', -90.0f);
				break;
			case 5: // blue
				angles.put('x', 90.0f);
				break;
		}
		return angles;
	}

	private void rotateGL(GL10 gl, int side, int current) {
		Vector<Float> coordinates = dxyz.get(current);
		gl.glPushMatrix();
		gl.glTranslatef(coordinates.get(0), coordinates.get(1), coordinates.get(2));
		Dictionary<Character, Float> angles = calcRotation(side);
		gl.glRotatef(angles.get('x'), 1.0f, 0.0f, 0.0f);
		gl.glRotatef(angles.get('y'), 0.0f, 1.0f, 0.0f);
		gl.glRotatef(angles.get('z'), 0.0f, 0.0f, 1.0f);
		parts.get(current).draw(gl, mode);
		gl.glPopMatrix();
	}

	public void drawCube(GL10 gl, int mode) {
		setMode(mode);
		if (status == 'N') {
			for (int i = 0; i < dxyz.size(); i++) {
				int nOfSide = i / dimOfCube / dimOfCube;
				rotateGL(gl, nOfSide, i);
			}
		}
		if (status == 'R' || status == 'A' || status == 'C') {
			int hzside = rotSide / dimOfCube;
			switch (hzside) {
				case 0:
					drawType(gl, faceFirst, 0);
					break;
				case 1:
					drawType(gl, faceSecond, 1);
					break;
				case 2:
					drawType(gl, faceThird, 2);
					break;
			}
		}
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public float[] getQuad() {
		return quad;
	}

	public boolean isSelected(int id) {
		return parts.get(id).isSelected();
	}

	public void selectPart(int id) {
		parts.get(id).setSelected(true);
	}

	public void deselectPart(int id) {
		parts.get(id).setSelected(false);
	}

	public void beginRotate(String step) {
		int side = -1;
		boolean isClockWise = true;
		switch(step) {
			case "L":
				side = 0;
				isClockWise = true;
				break;
			case "L'":
				side = 0;
				isClockWise = false;
				break;
			case "L2":
				side = 0;
				isClockWise = true;
				break;
			case "R":
				side = 2;
				isClockWise = false;
				break;
			case "R'":
				side = 2;
				isClockWise = true;
				break;
			case "R2":
				side = 2;
				isClockWise = false;
				break;
			case "U":
				side = 3;
				isClockWise = false;
				break;
			case "U'":
				side = 3;
				isClockWise = true;
				break;
			case "U2":
				side = 3;
				isClockWise = false;
				break;
			case "D":
				side = 5;
				isClockWise = true;
				break;
			case "D'":
				side = 5;
				isClockWise = false;
				break;
			case "D2":
				side = 5;
				isClockWise = true;
				break;
			case "F":
				side = 6;
				isClockWise = false;
				break;
			case "F'":
				side = 6;
				isClockWise = true;
				break;
			case "F2":
				side = 6;
				isClockWise = false;
				break;
			case "B":
				side = 8;
				isClockWise = true;
				break;
			case "B'":
				side = 8;
				isClockWise = false;
				break;
			case "B2":
				side = 8;
				isClockWise = true;
		}
		beginRotate(side, isClockWise);
		// if the step contains the char 2 we need to perform the step twice
		if (step.contains("2")) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			beginRotate(side, isClockWise);
		}

	}


	public void beginRotate(int side, boolean isClockWise) {
		if (status != 'R' && side > -1 && side < 9) {
			if (status == 'A' || status == 'C') {
//				status = 'A';
//				movingSides.remove(movingSides.size() - 1);
//			} else if (status == 'C') {
//				status = 'C';
				movingSides.remove(movingSides.size() - 1);
			} else {
				status = 'R';
			}
			rotateAngle = 0;
			rotSide = side;
			this.isClockWise = isClockWise;
		}
	}

	public boolean checkCollision(float tx, float ty) {
		return true;
	}

	public boolean checkSidesForRotating(Vector<Integer> setOf) {
		Vector<Integer> setOfSides = new Vector<Integer>(setOf);
		////////
		// first int is element, another pair is i of element and its number

		if (setOfSides.size() < 2 || status != 'N') return false;

		int f = -1, s = -1;
		float minD = 999.0f;
		for (int i = 0; i < setOfSides.size() - 1; i++) {
			for (int j = i + 1; j < setOfSides.size(); j++) {
				Part p1 = parts.get(i);
				Part p2 = parts.get(j);
				float d = Math.abs(p1.z - p2.z);
				if (d < minD) {
					minD = d;
					f = setOfSides.get(i);
					s = setOfSides.get(j);
				}
			}
		}
		if (f == -1) return false;
		setOfSides.clear();
		setOfSides.add(f);
		setOfSides.add(s);
		Map<Integer, Integer> mp = new HashMap<Integer, Integer>();
		int maxSide = 0, iSide = -1;
		for (int i = 0; i < setOfSides.size(); i++) {
			if (mp.get(oSides.get(setOfSides.get(i)).getFirst()) != null) {
				mp.put(oSides.get(setOfSides.get(i)).getFirst(), mp.get(oSides.get(setOfSides.get(i)).getFirst()) + 1);
			} else {
				mp.put(oSides.get(setOfSides.get(i)).getFirst(), 1);
			}
			if (mp.get(oSides.get(setOfSides.get(i)).getSecond()) != null) {
				mp.put(oSides.get(setOfSides.get(i)).getSecond(), mp.get(oSides.get(setOfSides.get(i)).getSecond()) + 1);
			} else {
				mp.put(oSides.get(setOfSides.get(i)).getSecond(), 1);
			}
			/*
			 * mp[oSides[ setOfSides[i] ].first]++;
				mp[oSides[ setOfSides[i] ].second]++;*/
		}
		Iterator<Entry<Integer, Integer>> iter = mp.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<Integer, Integer> mEntry = (Entry<Integer, Integer>) iter.next();
			if (mEntry.getValue() > maxSide) {
				maxSide = mEntry.getValue();
				iSide = mEntry.getKey();
			}
		}
		/*for (map<int, int>::iterator it = mp.begin(); it != mp.end(); it++)
		{
			if (it->second > maxSide)
			{
				maxSide = it->second;
				iSide = it->first;
			}
		}*/
		for (int i = setOfSides.size() - 1; i >= 0; i--) {
			if (oSides.get(setOfSides.get(i)).getFirst() != iSide
					&& oSides.get(setOfSides.get(i)).getSecond() != iSide)
				setOfSides.remove(i);
		}
		if (setOfSides.size() >= 2) {
			beginRotate(iSide, isClockWiseDirection(setOfSides, iSide));
		}
		return true;
	}

	public char getStatus() {
		return status;
	}

	public void startRandomRotating(int numberOfMoves) {
		status = 'A';
		movingSides = new Vector<Pair<Integer, Boolean>>();
		movingSides.clear();
		for (int i = 0; i < numberOfMoves; i++) {
			int curS = (int) (Math.random() * (dimOfCube * 3 - 1));
			int hz = (int) (Math.random() * 2);
			boolean res = (hz == 1);
			movingSides.add(new Pair(curS, res));
		}
		beginRotate(movingSides.lastElement().getFirst(), movingSides.lastElement().getSecond());
	}

	public void cancelMoves(int number) {
		int k = madeMoves.size();
		if (k != 0 && status == 'N') {
			status = 'C';
			for (int i = k - number; i < k; i++) {
				Pair p = madeMoves.pop();
				Pair newP = new Pair(p.getFirst(), !(Boolean)p.getSecond());
				movingSides.add(newP);
			}
			beginRotate(movingSides.lastElement().getFirst(), movingSides.lastElement().getSecond());
		}
	}

	private StringBuilder buildFaceString(int index, int frontColor, int leftColor, int backColor,
										  int rightColor, int upColor, int downColor) {
		StringBuilder str = new StringBuilder();
		for (int i = index; i < index + dimOfCube * dimOfCube; i++) {
			Part p = parts.get(i);
			int textureID = p.getRectangle().getTextureId();
			if (textureID == frontColor) {
				str.append('F');
			} else if (textureID == rightColor) {
				str.append('R');
			} else if (textureID == upColor) {
				str.append('U');
			} else if (textureID == leftColor) {
				str.append('L');
			} else if (textureID == downColor) {
				str.append('D');
			} else if (textureID == backColor) {
				str.append('B');
			}
		}
		return str;
	}

	public String getStringRepresentation() {
		// find the color of each face according to the center part of the pace
		int frontColor = parts.get(4).getRectangle().getTextureId();
		int leftColor = parts.get(13).getRectangle().getTextureId();
		int backColor = parts.get(22).getRectangle().getTextureId();
		int rightColor = parts.get(31).getRectangle().getTextureId();
		int upColor = parts.get(40).getRectangle().getTextureId();
		int downColor = parts.get(49).getRectangle().getTextureId();

		StringBuilder frontString = buildFaceString(0, frontColor, leftColor, backColor, rightColor, upColor, downColor);
		StringBuilder leftString = buildFaceString(9, frontColor, leftColor, backColor, rightColor, upColor, downColor);
		StringBuilder backString = buildFaceString(18, frontColor, leftColor, backColor, rightColor, upColor, downColor);
		StringBuilder rightString = buildFaceString(27, frontColor, leftColor, backColor, rightColor, upColor, downColor);
		StringBuilder upString = buildFaceString(36, frontColor, leftColor, backColor, rightColor, upColor, downColor);
		StringBuilder downString = buildFaceString(45, frontColor, leftColor, backColor, rightColor, upColor, downColor);

		StringBuilder cubeString = new StringBuilder();
		cubeString.append(upString);
		cubeString.append(rightString);
		cubeString.append(frontString);
		cubeString.append(downString);
		cubeString.append(leftString);
		cubeString.append(backString);
		return cubeString.toString();
	}
}

