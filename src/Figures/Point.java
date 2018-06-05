package Figures;

import java.io.Serializable;

/**
 * Point. Coordinates of point.
 *
 * @author Yan Khonskiy.
 */
public class Point implements Serializable {

    private int x;
    private int y;

    public Point() {
        x = y = 0;
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point P) {
        x = P.x;
        y = P.y;
    }

    public static double distance(Point A, Point B) {
        return Math.sqrt(Math.pow(A.x - B.x, 2) + Math.pow(A.y - B.y, 2));
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "(" + x + " " + y + ")";
    }


    @Override
    public Object clone() {
        return new Point(x, y);
    }

    public boolean equal(Point B) {
        return ((y == B.y) && (x == B.x));
    }
}