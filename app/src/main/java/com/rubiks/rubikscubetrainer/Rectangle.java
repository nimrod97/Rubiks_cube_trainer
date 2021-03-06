package com.rubiks.rubikscubetrainer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

// The class is responsible to represent a rectangle for the cubies in the cube.
// Each rectangle has a vertices and texture the we use for drawing the cubies on the screen.

public class Rectangle {
    private FloatBuffer vertexBuffer;  // Buffer for vertex-array
    private int textureId;
    private FloatBuffer mCoordsBuffer;

    public Rectangle(float[] vertices, int textureId) {
        this(vertices);
        this.textureId = textureId;
        float textureCoords[] = {
                0.0f, 1.0f,
                1.0f, 1.0f,
                1.0f, 0.0f,
                0.0f, 0.0f,
        };
        mCoordsBuffer = Helper.createFloatBuffer(textureCoords);
    }

    public Rectangle(float[] vertices) {
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder()); // Use native byte order
        vertexBuffer = vbb.asFloatBuffer(); // Convert from byte to float
        vertexBuffer.put(vertices);         // Copy data into buffer
        vertexBuffer.position(0);           // Rewind
    }

    public int getTextureId() { return this.textureId; }


    public void draw(GL10 gl) {
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mCoordsBuffer);
        // Set the color for each of the faces

        // Draw the primitive from the vertex-array directly
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
        gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    }
}
