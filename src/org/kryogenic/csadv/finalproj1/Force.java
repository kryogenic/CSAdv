package org.kryogenic.csadv.finalproj1;

import java.awt.geom.Point2D;
import java.util.Collection;

/**
 * @author: Kale
 * @date: 28/11/12
 */
public class Force {
    private Vector2D v;
    private final Falloff falloff;
    private int ticks = 0;


    public Force(float x, float y, Falloff falloff) {
        this(new Vector2D(x, y), falloff);
    }
    public Force(Vector2D v, Falloff falloff) {
        this.v = v;
        this.falloff = falloff;
    }

    public void flip(TriPlane p) {
        v = v.flip(p);
    }
    public void reflect(Vector2D normal) {
    	v = v.reflect(normal);
    }
    public void sign(TriPlane p, Sign s) {
    	v = v.sign(p, s);
    }
    public void tick() {
        ticks++;
    }
    public Vector2D vector() {
    	return v;
    }
    public float xMag() {
        return v.getX() * falloff.get(ticks);
    }
    public float yMag() {
        return v.getY() * falloff.get(ticks);
    }


    public static Vector2D add(Collection<Force> forces) {
        double x = 0, y = 0;
        for(Force f : forces) {
            x += f.xMag();
            y += f.yMag();
        }
        final int num = forces.size();
        return new Vector2D((float)(x / num), (float)(y / num));
    }
}
