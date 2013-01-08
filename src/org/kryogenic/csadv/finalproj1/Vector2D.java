/**
 * 
 */
package org.kryogenic.csadv.finalproj1;

public class Vector2D {
    public static final Vector2D ZERO = new Vector2D(0, 0);
    private final float x, y;
    public Vector2D(float x, float y) {
        this.x = x;
        this.y = y;
    }
    public Vector2D add(Vector2D v) {
    	return new Vector2D(x + v.x, y + v.y);
    }
    public float dotProduct(Vector2D v) {
    	return x * v.x + y * v.y;
    }
    public Vector2D diff(Vector2D v) {
    	return new Vector2D(x - v.x, y - v.y);
    }
    @Override
    public boolean equals(Object o) {
    	if(o instanceof Vector2D) {
    		Vector2D v = (Vector2D) o;
    		return v.getX() == x && v.getY() == y;
    	}
    	return false;
    }
    public Vector2D flip(Plane p) {
        switch (p) {
            case HORIZONTAL:
                return new Vector2D(-x, y);
            case VERTICAL:
            	return new Vector2D(x, -y);
            case BOTH:
            	return new Vector2D(-x, -y);
        }
        return null;
    }
    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }
    public float length() {
    	return (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }
    public Vector2D multiply(double d) {
    	return new Vector2D((float)(x * d), (float)(y * d));
    }
    public Vector2D multiply(Vector2D v) {
    	return new Vector2D(x * v.x, y * v.y);
    }
    public Vector2D multiplyLength(double d) {
        double b = Math.sqrt(
            (Math.pow(length(), 2) * Math.pow(d, 2)) /
            (Math.pow(x, 2) + Math.pow(y, 2))
        );
        return new Vector2D((float)(b * x), (float)(b * y));
    }
    public Vector2D normalize() { 
    	float length = length();
    	return new Vector2D(x / length, y / length);
    }
    public Vector2D reflect(Vector2D normal) {
    	normal = normal.normalize();
    	return diff(normal.multiply(2 * normal.dotProduct(this)));
    }
    public Vector2D sign(Plane p, Sign s) {
    	switch (p) {
            case HORIZONTAL:
            	return new Vector2D(x < 0 ? (s == Sign.NEGATIVE ? x : -x) : (s == Sign.NEGATIVE ? -x : x), y);
            case VERTICAL:
            	return new Vector2D(x, y < 0 ? (s == Sign.NEGATIVE ? y : -y) : (s == Sign.NEGATIVE ? -y : y));
            case BOTH:
            	return new Vector2D(
            			x < 0 ? (s == Sign.NEGATIVE ? x : -x) : (s == Sign.NEGATIVE ? -x : x),
            			y < 0 ? (s == Sign.NEGATIVE ? y : -y) : (s == Sign.NEGATIVE ? -y : y));
        }
    	return null;
    }
    public String toString() {
        return "[Vector2D (" + String.valueOf(x) + "," + String.valueOf(y) + ")]";
    }

    public static enum Plane {
        HORIZONTAL, VERTICAL, BOTH
    }

    public static enum Sign {
        POSITIVE, NEGATIVE
    }
}