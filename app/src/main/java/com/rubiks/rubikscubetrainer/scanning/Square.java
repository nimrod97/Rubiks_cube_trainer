package com.rubiks.rubikscubetrainer.scanning;

import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;

// In this class we create the squares that will be shown as templates
// for cubbies in the camera when scanning

public class Square {
    private Scalar colorRgb;
    private Rect rect;
    private Point center;
    private int size;
    private String prec = "n";

    public Square(Point center, int size) {
        colorRgb = new Scalar(255, 0, 0);
        this.size = size;
        this.center = center;

        rect = new Rect();
        rect.x = (int) (center.x - size / 2);
        rect.y = (int) (center.y - size / 2);
        rect.width = size;
        rect.height = size;
    }

    public void setColorRgb(Scalar colorHsv) {
        this.colorRgb = colorHsv;
    }

    public Rect getRect() {
        return rect;
    }

    public Point getTopLeftPoint() {
        return new Point(rect.x, rect.y);
    }

    public Point getBottomRightPoint() {
        return new Point(rect.x + rect.width, rect.y + rect.height);
    }

    public Point getCenter() {
        return center;
    }

    public String getColor() {
        String tempString = "";
        if (colorRgb.val[0] >= 150 && colorRgb.val[1] < 100 && colorRgb.val[2] < 100) {
            tempString = "R";
        } else if (colorRgb.val[0] < 100 && colorRgb.val[1] >= 150 && colorRgb.val[2] < 100) {
            tempString = "G";
        } else if (colorRgb.val[0] < 100 && colorRgb.val[1] < 100 && colorRgb.val[2] >= 150) {
            tempString = "B";
        } else if (colorRgb.val[0] >= 150 && colorRgb.val[1] >= 170 && colorRgb.val[2] < 100) {
            tempString = "Y";
        } else if (colorRgb.val[0] >= 150 && colorRgb.val[1] >= 80 && colorRgb.val[1] <= 200 && colorRgb.val[2] < 100) {
            tempString = "O";
        } else if (colorRgb.val[0] > 150 && colorRgb.val[1] > 150 && colorRgb.val[2] > 150) {
            tempString = "W";
        } else {
            tempString = prec;
        }

        prec = tempString;

        return tempString;
    }

}
