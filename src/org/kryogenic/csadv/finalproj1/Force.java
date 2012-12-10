package org.kryogenic.csadv.finalproj1;

import java.awt.geom.Point2D;
import java.util.Collection;

/**
 * @author: Kale
 * @date: 28/11/12
 */
public class Force {
    private Direction d;
    private final Falloff falloff;
    private int ticks = 0;


    public Force(float x1, float y1, float x2, float y2, Falloff falloff) {
        this(new Direction(new Point2D.Float(x1, y1), new Point2D.Float(x2, y2)), falloff);
    }
    public Force(Direction d, Falloff falloff) {
        this.d = d;
        this.falloff = falloff;
    }

    
    public Direction direction() {
    	return d;
    }
    public void collide(float angle) {
    	// todo should flip this force in relation to the angle given
    }
    public void flip(TriPlane p) {
        d.flip(p);
    }
    public void flip(TriPlane p, Sign s) {
    	d.flip(p, s);
    }
    public void tick() {
        ticks++;
    }
    public float xMag() {
        return d.diff(BiPlane.X) * falloff.get(ticks);
    }
    public float yMag() {
        return d.diff(BiPlane.Y) * falloff.get(ticks);
    }


    public static Vector2 add(Collection<Force> forces) {
        double x = 0, y = 0;
        for(Force f : forces) {
            x += f.xMag();
            y += f.yMag();
        }
        final int num = forces.size();
        return new Vector2((float)(x / num), (float)(y / num));
    }
    public static class Vector2 {
        public static final Vector2 ZERO = new Vector2(0, 0);
        private final float x, y;
        private Vector2(float x, float y) {
            this.x = x;
            this.y = y;
        }
        @Override
        public boolean equals(Object o) {
        	if(o instanceof Vector2) {
        		Vector2 v = (Vector2) o;
        		return v.getX() == getX() && v.getY() == getY();
        	}
        	return false;
        }
        public float getX() {
            return x;
        }
        public float getY() {
            return y;
        }
    }
}
