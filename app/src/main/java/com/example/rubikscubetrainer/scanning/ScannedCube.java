package com.example.rubikscubetrainer.scanning;

import com.example.rubikscubetrainer.Cube;
import com.example.rubikscubetrainer.GLRenderer;
import com.example.rubikscubetrainer.GLTextures;
import com.example.rubikscubetrainer.Part;
import com.example.rubikscubetrainer.Rectangle;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

// This class is responsible for building 3D cube that already scanned by the user
// from the string of colors.
// it extents the cube class and override the 'fillParts' method

public class ScannedCube extends Cube {
    private Map<String, List<Character>> faces;
    public static List<Character> allColors;

    public ScannedCube(GLTextures textures) throws JSONException {
        super(textures);
    }

    @Override
    public void fillParts(int size) throws JSONException {

        this.faces = new HashMap<>(ScanningActivity.faces);
        parts = new Vector<Part>();
        allColors = new ArrayList<>(faces.get("front"));
        allColors.addAll(faces.get("left"));
        allColors.addAll(faces.get("back"));
        allColors.addAll(faces.get("right"));
        allColors.addAll(faces.get("top"));
        allColors.addAll(faces.get("bottom"));
        int[] texturesID = new int[54];
        for (int i = 0; i < allColors.size(); i++) {
            switch (allColors.get(i)) {
                case 'W':
                    texturesID[i] = textures.getTextureIdforResource(GLRenderer.WHITE);
                    break;
                case 'R':
                    texturesID[i] = textures.getTextureIdforResource(GLRenderer.RED);
                    break;
                case 'G':
                    texturesID[i] = textures.getTextureIdforResource(GLRenderer.GREEN);
                    break;
                case 'B':
                    texturesID[i] = textures.getTextureIdforResource(GLRenderer.BLUE);
                    break;
                case 'Y':
                    texturesID[i] = textures.getTextureIdforResource(GLRenderer.YELLOW);
                    break;
                default:
                    texturesID[i] = textures.getTextureIdforResource(GLRenderer.ORANGE);
                    break;
            }
        }
        int j = 0;
        for (int i = 0; i < size * size; i++) {
            parts.add(new Part(new Rectangle(quad, texturesID[j])));
            j++;
        }
        for (int i = 0; i < size * size; i++) {
            parts.add(new Part(new Rectangle(quad, texturesID[j])));
            j++;
        }
        for (int i = 0; i < size * size; i++) {
            parts.add(new Part(new Rectangle(quad, texturesID[j])));
            j++;
        }
        for (int i = 0; i < size * size; i++) {
            parts.add(new Part(new Rectangle(quad, texturesID[j])));
            j++;
        }
        for (int i = 0; i < size * size; i++) {
            parts.add(new Part(new Rectangle(quad, texturesID[j])));
            j++;
        }
        for (int i = 0; i < size * size; i++) {
            parts.add(new Part(new Rectangle(quad, texturesID[j])));
            j++;
        }

    }

}
