package com.rubiks.rubikscubetrainer.db;

import com.rubiks.rubikscubetrainer.Cube;
import com.rubiks.rubikscubetrainer.GLRenderer;
import com.rubiks.rubikscubetrainer.GLTextures;
import com.rubiks.rubikscubetrainer.Part;
import com.rubiks.rubikscubetrainer.Rectangle;

import org.json.JSONException;

import java.util.Vector;

// This class is responsible for building 3D cube from the string of colors that saved in the db.
// it extents the cube class and override the 'fillParts' method

public class SavedCube extends Cube {
    private String[] colors;

    public SavedCube(GLTextures textures) throws JSONException {
        super(textures);
    }

    @Override
    public void fillParts(int size) {
        this.colors = CubeFromDB.colors;
        parts = new Vector<Part>();
        int[] texturesID = new int[54];
        for (int i = 0; i < colors.length; i++) {
            if (colors[i].contains("W"))
                texturesID[i] = textures.getTextureIdforResource(GLRenderer.WHITE);
            else if (colors[i].contains("R"))
                texturesID[i] = textures.getTextureIdforResource(GLRenderer.RED);
            else if (colors[i].contains("G"))
                texturesID[i] = textures.getTextureIdforResource(GLRenderer.GREEN);
            else if (colors[i].contains("B"))
                texturesID[i] = textures.getTextureIdforResource(GLRenderer.BLUE);
            else if (colors[i].contains("Y"))
                texturesID[i] = textures.getTextureIdforResource(GLRenderer.YELLOW);
            else if (colors[i].contains("O"))
                texturesID[i] = textures.getTextureIdforResource(GLRenderer.ORANGE);
        }
        int id = 1;
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
