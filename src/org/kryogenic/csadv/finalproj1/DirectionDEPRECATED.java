package org.kryogenic.csadv.finalproj1;

import java.awt.geom.Point2D;

/**
 * Two points, the first is treated as the origin
 * @author: Kale
 * @date: 03/12/12
 */
public class DirectionDEPRECATED {
    private final Point2D.Float[] data = new Point2D.Float[2];


    public DirectionDEPRECATED(Point2D.Float p1, Point2D.Float p2) {
        this.data[0] = p1;
        this.data[1] = p2;
    }

    /**
     * Modifies this Direction such that it is relative to (0, 0)
     * Does not modify the magnitude!
     * @return the modified Direction
     */
    public DirectionDEPRECATED ground() {
    	float minx = Math.min(data[0].x, data[1].x), miny = Math.min(data[0].y, data[1].y);
    	data[0] = new Point2D.Float(data[0].x - minx, data[0].y - miny);
    	data[1] = new Point2D.Float(data[1].x - minx, data[1].y - miny);
    	return this;
    }
    public Point2D.Float destination() {
    	return data[1];
    }
    public Vector2D diff() {
        return new Vector2D(diff(BiPlane.X), diff(BiPlane.Y));
    }
    public float diff(BiPlane p) {
        switch(p) {
            case X:
                return (float)(data[1].getX() - data[0].getX());
            case Y:
                return (float)(data[1].getY() - data[0].getY());
        }
        return 0;
    }
    /**
     * Flips the data in the specified plane
     * Modifies, then returns the object
     * @param p
     * @return the modified, flipped Direction
     */
    public DirectionDEPRECATED flip(TriPlane p) {
        switch (p) {
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
        return this;
    }
    /**
     * Flips the data in the specified plane, ensuring the difference will have the sign specified
     * Modifies, then returns the object
     * @param p the plane to flip in
     * @param s the sign desired
     * @return the flipped Direction
     */
    public void flip(TriPlane p, Sign s) {
    	switch (p) {
            case HORIZONTAL:
            	if(s == Sign.POSITIVE && data[0].x > data[1].x || s == Sign.NEGATIVE && data[0].x < data[1].x) {
	                float p1x = data[0].x;
	                data[0] = new Point2D.Float(data[1].x, data[0].y);
	                data[1] = new Point2D.Float(p1x, data[1].y);
            	}
                break;
            case VERTICAL:
            	if(s == Sign.POSITIVE && data[0].y > data[1].y || s == Sign.NEGATIVE && data[0].y < data[1].y) {
	                float p1y = data[0].y;
	                data[0] = new Point2D.Float(data[0].x, data[1].y);
	                data[1] = new Point2D.Float(data[1].x, p1y);
            	}
                break;
            case BOTH:
            	flip(TriPlane.HORIZONTAL, s);
            	flip(TriPlane.VERTICAL, s);
            	break;
        }
    }
    public Point2D.Float origin() {
    	return data[0];
    }
    public DirectionDEPRECATED reflect(Vector2D normal) {
    	normal = normal.normalize();
    	Vector2D v = this.diff();
    	float length = v.length();
    	Vector2D reflection = normal.diff(v).multiply(v.dotProduct(normal)).multiply(2);
    	reflection = reflection.normalize().multiply(length);
    	Point2D.Float destination = new Point2D.Float(
    			this.data[1].x + reflection.getX(),
    			this.data[1].y + reflection.getY());
    	return new DirectionDEPRECATED(this.data[1], destination);
    }
    public Point2D.Float sum() {
        return new Point2D.Float(sum(BiPlane.X), sum(BiPlane.Y));
    }
    public float sum(BiPlane p) {
        switch(p) {
            case X:
                return data[0].x + data[1].x;
            case Y:
                return data[0].y + data[1].y;
        }
        return 0;
    }
}
