package com.example.rubikscubetrainer.db;

import com.example.rubikscubetrainer.Cube;
import com.example.rubikscubetrainer.GLRenderer;
import com.example.rubikscubetrainer.GLTextures;
import com.example.rubikscubetrainer.MyOpenGL;
import com.example.rubikscubetrainer.Part;
import com.example.rubikscubetrainer.Rectangle;

import org.json.JSONException;

import java.util.Vector;

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
            if (colors[i].contains("white"))
                texturesID[i] = textures.getTextureIdforResource(GLRenderer.WHITE);
            else if (colors[i].contains("red"))
                texturesID[i] = textures.getTextureIdforResource(GLRenderer.RED);
            else if (colors[i].contains("green"))
                texturesID[i] = textures.getTextureIdforResource(GLRenderer.GREEN);
            else if (colors[i].contains("blue"))
                texturesID[i] = textures.getTextureIdforResource(GLRenderer.BLUE);
            else if (colors[i].contains("yellow"))
                texturesID[i] = textures.getTextureIdforResource(GLRenderer.YELLOW);
            else if (colors[i].contains("orange"))
                texturesID[i] = textures.getTextureIdforResource(GLRenderer.ORANGE);
        }
        int id = 1;
        int j = 0;
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
