package org.kryogenic.csadv.finalproj1;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author: Kale
 * @date: 27/11/12
 */
public class Circle {
	private final float HUE;
	private final float SAT;
    private final float LIFE_PER_TICK;
	private final float MAGNITUDE;

    private Set<Force> forces = new HashSet<Force>();
    private float life;
	private Point2D.Float p;

	
	public Circle(float hue, float sat, float magnitude, Point p, Vector2D v) {
		this(hue, sat, 1/8f, magnitude, p, v);
	}
	private Circle(float hue, float sat, float life, float magnitude, Point p, Vector2D d) {
		this.HUE = hue;
		this.SAT = sat;
        this.LIFE_PER_TICK = (1 - life) / 5;
        this.MAGNITUDE = magnitude;
		this.life = life > 1 ? 1 : life;
		this.p = new Point2D.Float(p.x, p.y);
        addForce(new Force(v, new Falloff.Gravity()));
	}
    
    public void addForce(Force f) {
        forces.add(f);
    }
    
    public Point2D.Float center() {
    	return p;
    }
	
	public void draw(Graphics2D g, boolean net, Collection<Circle> circles) {
		g.setColor(Color.getHSBColor(HUE, SAT, life));
	    g.fill(shape());
		if (net) {
			for(Iterator<Circle> i = circles.iterator(); i.hasNext();) {
				Circle c = i.next();
				g.drawLine((int)p.x, (int)p.y, (int)c.p.x, (int)c.p.y);
			}
		}
        //g.setColor(Color.CYAN);
        //g.drawString(String.valueOf(forces.size()), p.x - 5, p.y + 5);
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

    public void flipForces(TriPlane p, Sign s) {
        for(Force f : forces) {
            f.flip(p, s);
        }
    }
    
    public float life() {
    	return life;
    }
    
    public float radius() {
    	return life() * MAGNITUDE;
    }
    
    public void reflectForces(Vector2D normal) {
    	for(Force f : forces) {
    		f.reflect(normal);
    	}
    }
    
    public Ellipse2D shape() {
        return new Ellipse2D.Double(p.x - radius(), p.y - radius(), radius() * 2, radius() * 2);
    }

    public boolean update() {
        if(life + LIFE_PER_TICK <= 1)
            life += LIFE_PER_TICK;
        for(Iterator<Force> i = forces.iterator(); i.hasNext();) {
        	Force f = i.next();
            f.tick();
            if(f.xMag() == 0 && f.yMag() == 0) {
                i.remove();
            }
        }
        if(forces.isEmpty())
        	return false;
        Vector2D sum = Force.add(forces);
        p.x += sum.getX() / 5;
        p.y += sum.getY() / 5;
        return true;
    }
}
