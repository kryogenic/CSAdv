package org.kryogenic.csadv.finalproj1;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author: Kale
 * @date: 27/11/12
 */
public class Circle {
	private final float HUE;
	private final float SAT;
    private final float LIFE_PER_TICK;
	private final float MAGNITUDE;

    private Vector2D velocity = Vector2D.ZERO;
    private float life;
	private Point2D.Float p;

	
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
    
    public void addVector(Vector2D v) {
        velocity = velocity.add(v);
    }
    
    public void affect(Circle c) {
        Color col = color();
        if(col.getRed() >= 235) {
            double take = c.life / 8;
            if(c.life > take)
                c.life -= take;
            if(life <= 1 - take)
                life += take;
        }
        if(col.getGreen() >= 235) {
            double give = life / 8;
            if(c.life <= 1 - give)
                c.life += give;
        }
        if(col.getBlue() >= 235) {
            c.life = life;
        }
    }
    
    public void applyVector(Vector2D v) {
    	p.x += v.getX();
    	p.y += v.getY();
    }
    
    public Point2D.Float center() {
    	return p;
    }
    
    public Color color() {
        return Color.getHSBColor(HUE, SAT, life);
    }
	
	public void draw(Graphics2D g, boolean net, Collection<Circle> circles) {
		g.setColor(color());
	    g.fill(shape());
		if (net) {
			for(Iterator<Circle> i = circles.iterator(); i.hasNext();) {
				Circle c = i.next();
				g.drawLine((int)p.x, (int)p.y, (int)c.p.x, (int)c.p.y);
			}
		}
	}
    
    public boolean equals(Object o) {
        if(o instanceof Circle) {
            Circle c = (Circle) o;
            if(this.p.equals(c.p)) {
                if(this.life == c.life) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public float life() {
    	return life;
    }
    
    public float radius() {
    	return life() * MAGNITUDE;
    }

    public void reflectVelocity(Vector2D normal) {
    	velocity = velocity.reflect(normal);
    }

    public void signVelocity(Vector2D.Plane p, Vector2D.Sign s) {
    	velocity = velocity.sign(p, s);
    }
    
    public Ellipse2D shape() {
        return new Ellipse2D.Double(p.x - radius(), p.y - radius(), radius() * 2, radius() * 2);
    }

    public void update() {
        if(life + LIFE_PER_TICK <= 1)
            life += LIFE_PER_TICK;
        p.x += velocity.getX() / MAGNITUDE;
        p.y += velocity.getY() / MAGNITUDE;
    }
    
    public Vector2D velocity() {
    	return velocity;
    }
}
