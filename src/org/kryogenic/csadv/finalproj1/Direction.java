package org.kryogenic.csadv.finalproj1;

import java.awt.geom.Point2D;

/**
 * Two points, the first is treated as the origin
 * @author: Kale
 * @date: 03/12/12
 */
public class Direction {
    private final Point2D.Float[] data = new Point2D.Float[2];


    public Direction(Point2D.Float p1, Point2D.Float p2) {
        this.data[0] = p1;
        this.data[1] = p2;
    }


    public Point2D.Float diff() {
        return new Point2D.Float(diff(BiPlane.HORIZONTAL), diff(BiPlane.VERTICAL));
    }
    public float diff(BiPlane p) {
        switch(p) {
            case HORIZONTAL:
                return (float)(data[1].getX() - data[0].getX());
            case VERTICAL:
                return (float)(data[1].getY() - data[0].getY());
        }
        return 0;
    }

    public void flip(TriPlane d) {
        switch (d) {
            case HORIZONTAL:
                float p1x = data[0].x;
                data[0] = new Point2D.Float(data[1].x, data[0].y);
                data[1] = new Point2D.Float(p1x, data[1].y);
                break;
            case VERTICAL:
                float p1y = data[0].y;
                data[0] = new Point2D.Float(data[0].x, data[1].y);
                data[1] = new Point2D.Float(data[1].x, p1y);
                break;
            case BOTH:
                Point2D.Float temp = data[0];
                data[0] = data[1];
                data[1] = temp;
        }
    }

    public Point2D.Float sum() {
        return new Point2D.Float(sum(BiPlane.HORIZONTAL), sum(BiPlane.VERTICAL));
    }
    public float sum(BiPlane p) {
        switch(p) {
            case HORIZONTAL:
                return (float)(data[0].getX() + data[1].getX());
            case VERTICAL:
                return (float)(data[0].getY() + data[1].getY());
        }
        return 0;
    }
}
