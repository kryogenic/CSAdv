package org.kryogenic.csadv.finalproj1;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 * @author: Kale
 * @date: 27/11/12
 *
 * Contains all the data about a circle
 * Provides methods for updating the position of the circle, and drawing it
 */
public class Circle {
	private final float HUE;
	private final float SAT;
    private final float LIFE_PER_TICK;
	private final float MAGNITUDE;

    private Vector2D velocity = Vector2D.ZERO;
    private float life;
	private Point2D.Float p;

    /**
     * Constructs a Circle
     * @param hue the hue of the Circle's color, from 0-1
     * @param sat the saturation of the Circle's color, from 0-1
     * @param magnitude the magnitude of the Circle (the maximum radius)
     * @param p the starting point of the Circle
     * @param v the starting direction of the Circle
     */
	public Circle(float hue, float sat, float magnitude, Point2D p, Vector2D v) {
		this(hue, sat, 1/8f, magnitude, p, v);
	}
	private Circle(float hue, float sat, float life, float magnitude, Point2D p, Vector2D v) {
		this.HUE = hue;
		this.SAT = sat;
        this.LIFE_PER_TICK = (1 - life) / 50;
        this.MAGNITUDE = magnitude;
		this.life = life > 1 ? 1 : life;
		this.p = new Point2D.Float((float)p.getX(), (float)p.getY());
        addVector(v);
	}

    /**
     * Adds a Vector2D to this Circle's velocity Vector2D
     * @param v the Vector2D to add
     */
    public void addVector(Vector2D v) {
        velocity = velocity.sum(v);
    }
    /**
     * Applies a Vector2D to this Circle's coordinates
     * Not to be confused with <pre>Vector2D#addVector(Vector2D v)</pre>, this method changes the circle's x and y
     * coordinates directly, and discards the Vector2D
     * @param v the Vector2D to apply
     */
    public void applyVector(Vector2D v) {
    	p.x += v.getX();
    	p.y += v.getY();
    }
    /**
     * The center point of this Circle
     * @return the center point
     */
    public Point2D.Float center() {
    	return p;
    }
    /**
     * Constructs a Color object from this circle's hue, saturation, and current <pre>life</pre> value
     * @return this circle's Color
     */
    public Color color() {
        return Color.getHSBColor(HUE, SAT, life());
    }
    /**
     * Draws this circle on the specified Graphics2D object
     * @param g the Graphics2D on which to draw
     */
	public void draw(Graphics2D g) {
		g.setColor(color());
	    g.fill(shape());
	}
    @Override
    public boolean equals(Object o) {
        if(o instanceof Circle) {
            Circle c = (Circle) o;
            if(this.p.equals(c.p)) {
                if(this.life() == c.life()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @return this Circle's <pre>life</pre> value
     */
    public float life() {
    	return life;
    }

    /**
     * Calculates this Circle's radius using the <pre>life</pre> value and magnitude
     * @return this Circle's radius
     */
    public float radius() {
    	return life() * MAGNITUDE;
    }
    /**
     * Reflects this circle's velocity across the Vector2D <pre>normal</pre>
     * This method is necessary to change the private Vector2D <pre>velocity</pre>
     *
     * See <pre>Vector2D#reflect(Vector2D normal)</pre> for more info
     * @param normal the normal to reflect across
     */
    public void reflectVelocity(Vector2D normal) {
    	velocity = velocity.reflect(normal);
    }
    /**
     * Changes the sign of the coordinates of this Circle's velocity to the one specified
     * This method is necessary to change the private Vector2D <pre>velocity</pre>
     *
     * See <pre>Vector2D#sign(Plane p, Sign s)</pre> for more info
     * @param p the Vector2D.Plane to change the coordinates in
     * @param s the Vector2D.Sign to change the coordinates to
     */
    public void signVelocity(Vector2D.Plane p, Vector2D.Sign s) {
    	velocity = velocity.sign(p, s);
    }
    /**
     * Calculates the 2D shape of this Circle
     * @return an Ellipse2D representing the shape of this Circle
     */
    public Ellipse2D shape() {
        return new Ellipse2D.Double(p.x - radius(), p.y - radius(), radius() * 2, radius() * 2);
    }
    /**
     * Performs necessary updating... growing the circle; changing the location
     */
    public void update() {
        if(life + LIFE_PER_TICK <= 1)
            life += LIFE_PER_TICK;
        p.x += velocity.getX() / MAGNITUDE;
        p.y += velocity.getY() / MAGNITUDE;
    }
}
