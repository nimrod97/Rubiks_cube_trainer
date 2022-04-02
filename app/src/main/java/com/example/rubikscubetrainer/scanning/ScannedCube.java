package com.example.rubikscubetrainer.scanning;

import com.example.rubikscubetrainer.Cube;
import com.example.rubikscubetrainer.GLRenderer;
import com.example.rubikscubetrainer.GLTextures;
import com.example.rubikscubetrainer.MyOpenGL;
import com.example.rubikscubetrainer.Part;
import com.example.rubikscubetrainer.Rectangle;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class ScannedCube extends Cube {
    private Map<String, JSONArray> faces;
    public static List<String> allColors;
    public ScannedCube(GLTextures textures) throws JSONException {
        super(textures);
    }

    @Override
    public void fillParts(int size) throws JSONException {
        this.faces = new HashMap<>(ScanningActivity.faces);
        parts = new Vector<Part>();
        String[] colorsFront = new String[9];
        String[] colorsLeft = new String[9];
        String[] colorsBack = new String[9];
        String[] colorsRight = new String[9];
        String[] colorsTop = new String[9];
        String[] colorsBottom = new String[9];
        int j = 0;
        for (int i = 0; i < 9; i += 3) {
            colorsFront[j] = (String) faces.get("front").get(i);
            colorsLeft[j] = (String) faces.get("left").get(i);
            colorsBack[j] = (String) faces.get("back").get(i);
            colorsRight[j] = (String) faces.get("right").get(i);
            colorsTop[j] = (String) faces.get("top").get(i);
            colorsBottom[j] = (String) faces.get("bottom").get(i);
            j++;
        }
        for (int i = 1; i < 9; i += 3) {
            colorsFront[j] = (String) faces.get("front").get(i);
            colorsLeft[j] = (String) faces.get("left").get(i);
            colorsBack[j] = (String) faces.get("back").get(i);
            colorsRight[j] = (String) faces.get("right").get(i);
            colorsTop[j] = (String) faces.get("top").get(i);
            colorsBottom[j] = (String) faces.get("bottom").get(i);
            j++;
        }
        for (int i = 2; i < 9; i += 3) {
            colorsFront[j] = (String) faces.get("front").get(i);
            colorsLeft[j] = (String) faces.get("left").get(i);
            colorsBack[j] = (String) faces.get("back").get(i);
            colorsRight[j] = (String) faces.get("right").get(i);
            colorsTop[j] = (String) faces.get("top").get(i);
            colorsBottom[j] = (String) faces.get("bottom").get(i);
            j++;
        }
        allColors = new ArrayList<>(Arrays.asList(colorsFront));
        allColors.addAll(Arrays.asList(colorsLeft));
        allColors.addAll(Arrays.asList(colorsBack));
        allColors.addAll(Arrays.asList(colorsRight));
        allColors.addAll(Arrays.asList(colorsTop));
        allColors.addAll(Arrays.asList(colorsBottom));
        int[] texturesID = new int[54];
        for (int i = 0; i < allColors.size(); i++) {
            switch (allColors.get(i)) {
                case "white":
                    texturesID[i] = textures.getTextureIdforResource(GLRenderer.WHITE);
                    break;
                case "red":
                    texturesID[i] = textures.getTextureIdforResource(GLRenderer.RED);
                    break;
                case "green":
                    texturesID[i] = textures.getTextureIdforResource(GLRenderer.GREEN);
                    break;
                case "blue":
                    texturesID[i] = textures.getTextureIdforResource(GLRenderer.BLUE);
                    break;
                case "yellow":
                    texturesID[i] = textures.getTextureIdforResource(GLRenderer.YELLOW);
                    break;
                default:
                    texturesID[i] = textures.getTextureIdforResource(GLRenderer.ORANGE);
                    break;
            }
        }
        int id = 1;
        j = 0;
        for (int i = 0; i < size * size; i++) {
            parts.add(new Part(MyOpenGL.white, id++, new Rectangle(quad, texturesID[j])));
            j++;
        }
        for (int i = 0; i < size * size; i++) {
            parts.add(new Part(MyOpenGL.red, id++, new Rectangle(quad, texturesID[j])));
            j++;
        }
        for (int i = 0; i < size * size; i++) {
            parts.add(new Part(MyOpenGL.yellow, id++, new Rectangle(quad, texturesID[j])));
            j++;
        }
        for (int i = 0; i < size * size; i++) {
            parts.add(new Part(MyOpenGL.orange, id++, new Rectangle(quad, texturesID[j])));
            j++;
        }
        for (int i = 0; i < size * size; i++) {
            parts.add(new Part(MyOpenGL.green, id++, new Rectangle(quad, texturesID[j])));
            j++;
        }
        for (int i = 0; i < size * size; i++) {
            parts.add(new Part(MyOpenGL.blue, id++, new Rectangle(quad, texturesID[j])));
            j++;
        }

    }

}
