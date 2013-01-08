/**
 * @author: Kale
 * @date: 15/12/12
 *
 * Represents a 2D vector with float precision
 * Contains methods for adding, subtracting, multiplying and finding the dot product of vectors
 *
 * This class is immutable
 */
package org.kryogenic.csadv.finalproj1;

public class Vector2D {

    public static final Vector2D ZERO = new Vector2D(0, 0);

    private final float x, y;

    public Vector2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * The dot product of two vectors
     * @param v the second Vector2D
     * @return a new Vector2D representing the dot product of this Vector2D and v
     */
    public float dotProduct(Vector2D v) {
    	return x * v.x + y * v.y;
    }
    /**
     * The difference of two vectors
     * @param v the second Vector2D
     * @return a new Vector2D representing this Vector2D minus v
     */
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
    /**
     * Flips the sign of the x, or y (or both) coordinates of this Vector2D
     * ie.
     * <pre>new Vector2D(1, -2).flip(Vector2D.Plane.BOTH)</pre>
     * would return [Vector2D (-1, 2)]
     * @param p the Plane in which to flip
     * @return a new Vector2D with the desired coordinates flipped
     */
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
    /**
     * The length of this vector, calculated using the pythagorean theorem
     * @return length
     */
    public float length() {
    	return (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }
    /**
     * Scalar multiplication
     * Multiplies both coordinates of this Vector2D by the specified <pre>double</pre> value
     * @param d the value to multiply
     * @return a new Vector2D representing this Vector2D * d
     */
    public Vector2D multiply(double d) {
    	return new Vector2D((float)(x * d), (float)(y * d));
    }
    /**
     * Multiplies the length of this Vector2D by the specified value
     * Does not change the direction
     * @param d the value to multiply
     * @return a new Vector2D with coordinates such that the length will be equal to the length of this Vector2D * d
     */
    public Vector2D multiplyLength(double d) {
        double b = Math.sqrt(
            (Math.pow(length(), 2) * Math.pow(d, 2)) /
            (Math.pow(x, 2) + Math.pow(y, 2))
        );
        return new Vector2D((float)(b * x), (float)(b * y));
    }
    /**
     * Normalizes this Vector2D
     * (changes the coordinates such that the length = 1)
     * @return a new Vector2D with the same proportions, and length = 1
     */
    public Vector2D normalize() { 
    	float length = length();
    	return new Vector2D(x / length, y / length);
    }
    /**
     * Reflects this Vector2D across a specified normal
     * <pre>normal</pre> is normalized automatically
     * @param normal the normal Vector2D
     * @return a new Vector2D that represents this Vector2D reflected across the normal
     */
    public Vector2D reflect(Vector2D normal) {
    	normal = normal.normalize();
    	return diff(normal.multiply(2 * normal.dotProduct(this)));
    }
    /**
     * Sets the sign of the x, or y (or both) coordinates of this Vector2D
     * This method is similar to <pre>Vector2D#flip(Plane p)</pre>, but it allows the end result to be specified
     * @param p the Plane in which to change the sign
     * @param s the Sign desired
     * @return a new Vector2D that is ensured to have the specified sign in the specified plane(s)
     */
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
    /**
     * The sum of two vectors
     * @param v the second Vector2D
     * @return this Vector2D + v
     */
    public Vector2D sum(Vector2D v) {
        return new Vector2D(x + v.x, y + v.y);
    }
    @Override
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