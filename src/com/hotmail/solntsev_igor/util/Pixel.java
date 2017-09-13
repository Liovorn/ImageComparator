package com.hotmail.solntsev_igor.util;

/**
 * Created by solncevigor on 9/12/17.
 */
public class Pixel {
    private int coordinate_X;
    private int coordinate_Y;
    private int field;

    Pixel(int coordinate_X, int coordinate_Y, int field) {
        this.coordinate_X = coordinate_X;
        this.coordinate_Y = coordinate_Y;
        this.field = field;
    }

    int getCoordinate_X() {
        return coordinate_X;
    }

    int getCoordinate_Y() {
        return coordinate_Y;
    }

    @Override
    public String toString() {
        return "com.hotmail.solntsev_igor.util.Pixel{" +
                "coordinate_X=" + coordinate_X +
                ", coordinate_Y=" + coordinate_Y +
                ", field=" + field +
                '}';
    }
}
