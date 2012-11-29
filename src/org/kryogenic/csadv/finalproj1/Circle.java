package org.kryogenic.csadv.finalproj1;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Set;

/**
 * @author: Kale
 * @date: 27/11/12
 */
public class Circle {
	private final float HUE;
	private final float SAT;
    private final float LIFE_PER_TICK;

    private Set<Force> forces = new HashSet<Force>();
    private float life;
	private Point2D.Float p;

    public Force.Vector2 last = Force.Vector2.ZERO;
	
	public Circle(float hue, float sat, Point p) {
		this(hue, sat, 1/8f, p);
	}
	private Circle(float hue, float sat, float life, Point p) {
		this.HUE = hue;
		this.SAT = sat;
        this.LIFE_PER_TICK = (1 - life) / 5;
		this.life = life > 1 ? 1 : life;
		this.p = new Point2D.Float(p.x, p.y);
        addForce(new Force(0, 100, 0, 0, new Falloff.Gravity()));
	}
    
    public void addForce(Force f) {
        forces.add(f);
    }
	
	public void draw(Graphics2D g) {
		g.setColor(Color.getHSBColor(HUE, SAT, life));
        g.fill(getShape());
        g.setColor(Color.getHSBColor(0, 0, 1/8));
        g.drawString("Forces: " + String.valueOf(forces.size()), p.x - 25, p.y - 5);
        g.drawString("(" + p.x + ", " + p.y + ")", p.x - 35, p.y + 5);
	}
    
    public boolean equals(Object o) {
        if(o instanceof Circle) {
            Circle c = (Circle) o;
            if(this.p.equals(c.p)) {
                if(this.life == c.life) {
                    if(this.getShape().equals(c.getShape())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Ellipse2D getShape() {
        float d = life * 75;
        float r = d / 2;
        return new Ellipse2D.Double(p.x - r, p.y - r, d, d);
    }

    public void update() {
        if(life + LIFE_PER_TICK <= 1)
            life += LIFE_PER_TICK;
        Set<Force> toRemove = new HashSet<Force>();
        for(Force f : forces) {
            f.tick();
            if(f.xMag() == 0 && f.yMag() == 0) {
                toRemove.add(f);
            }
        }
        for(Force f : toRemove) {
            forces.remove(f);
        }
        last = Force.add(forces);
        p.x += last.getX() / 10;
        p.y += last.getY() / 10;
    }
}
