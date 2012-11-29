package org.kryogenic.csadv.finalproj1;

import java.awt.geom.Point2D;
import java.util.Set;

/**
 * @author: Kale
 * @date: 28/11/12
 */
public class Force {
    private final Point2D.Float p1, p2;
    private final Falloff falloff;
    private int ticks = 0;
    public Force(float x1, float y1, float x2, float y2, Falloff falloff) {
        this(new Point2D.Float(x1, y1), new Point2D.Float(x2, y2), falloff);
    }
    public Force(Point2D.Float p1, Point2D.Float p2, Falloff falloff) {
        this.p1 = p1;
        this.p2 = p2;
        this.falloff = falloff;
    }
    
    public void tick() {
        ticks++;
    }
    
    public float xMag() {
        return (p1.x - p2.x) * falloff.get(ticks);
    }
    
    public float yMag() {
        return (p1.y - p2.y) * falloff.get(ticks);
    }
    
    public static Vector2 add(Set<Force> forces) {
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

        public float getX() {
            return x;
        }
        public float getY() {
            return y;
        }
    }
}
