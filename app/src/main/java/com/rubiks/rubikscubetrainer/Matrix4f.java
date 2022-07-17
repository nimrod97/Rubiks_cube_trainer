package com.rubiks.rubikscubetrainer;

// This class is responsible to represent a matrix that we use for the rotation of the cube.
class Matrix4f {
    private float[][] matrix;

    public float[][] getMatrix() {
        return matrix;
    }

    public Matrix4f() {
        matrix = new float[4][4];
        setIdentity();
    }

    void get(float[] dest) {
        int k = -1;
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                dest[++k] = matrix[j][i];
            }
        }
    }

    void setZero() {
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                matrix[i][j] = 0.0f;
            }
        }
    }

    void setIdentity() {
        setZero();
        matrix[0][0] = matrix[1][1] = matrix[2][2] = matrix[3][3] = 1.0f;
    }

    void setRotation(Quat4f q1) {
        float n, s;
        float xs, ys, zs;
        float wx, wy, wz;
        float xx, xy, xz;
        float yy, yz, zz;

        n = (q1.x * q1.x) + (q1.y * q1.y) + (q1.z * q1.z) + (q1.w * q1.w);
        s = (n > 0.0f) ? (2.0f / n) : 0.0f;

        xs = q1.x * s;
        ys = q1.y * s;
        zs = q1.z * s;
        wx = q1.w * xs;
        wy = q1.w * ys;
        wz = q1.w * zs;
        xx = q1.x * xs;
        xy = q1.x * ys;
        xz = q1.x * zs;
        yy = q1.y * ys;
        yz = q1.y * zs;
        zz = q1.z * zs;

        matrix[0][0] = 1.0f - (yy + zz);
        matrix[0][1] = xy - wz;
        matrix[0][2] = xz + wy;
        matrix[0][3] = 0f;
        matrix[1][0] = xy + wz;
        matrix[1][1] = 1.0f - (xx + zz);
        matrix[1][2] = yz - wx;
        matrix[1][3] = 0f;
        matrix[2][0] = xz - wy;
        matrix[2][1] = yz + wx;
        matrix[2][2] = 1.0f - (xx + yy);
        matrix[2][3] = 0f;
        matrix[3][0] = 0f;
        matrix[3][1] = 0f;
        matrix[3][2] = 0f;
        matrix[3][3] = 1f;
    }

    public final void set(Matrix4f newMat) {
        float[][] m = newMat.getMatrix();
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                matrix[i][j] = m[i][j];
            }
        }
    }

    /**
     * Sets the value of this matrix to the result of multiplying
     * the two argument matrices together.
     *
     * @param mat1 the first matrix
     * @param mat2 the second matrix
     */
    public final void mul(Matrix4f mat1, Matrix4f mat2) {
        float[][] m1 = mat1.getMatrix();
        float[][] m2 = mat2.getMatrix();
        float[][] c = new float[4][4];
        //multiplying 2 matrices
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                for(int k = 0; k < 4; k++) {
                    c[i][j] += m1[i][k] * m2[k][j];
                }
            }
        }
        set(c);
    }

    private void set(float[][] c) {
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                matrix[i][j] = c[i][j];
            }
        }
    }
}
