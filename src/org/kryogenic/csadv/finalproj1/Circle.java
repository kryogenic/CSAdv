package org.kryogenic.csadv.finalproj1;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Circle {
	private final Color startColor;
	private final short life;
	private Point p;
	private final int r;
	
	public Circle(Color startColor, Point p) {
		this(startColor, 255, p, 50);
	}
	public Circle(Color startColor, Point p, int r) {
		this(startColor, 255, p, r);
	}
	public Circle(Color startColor, int life, Point p, int r) {
		this.startColor = startColor;
		if(life > 255) {
			throw new IllegalArgumentException("Life cannot be greater than 255");
		}
		this.life = (short)life;
		this.p = p;
		this.r = r;
	}
	
	public void draw(Graphics g) {
		g.setColor(limit(startColor, life));
		g.fillOval(p.x, p.y, r * 2, r * 2);
	}
	
	private Color limit(Color c, short limit) {
		int r = c.getRed(), g = c.getGreen(), b = c.getBlue();
		if(r > limit)
			r = limit;
		if(g > limit)
			g = limit;
		if(b > limit)
			b = limit;
		return new Color(r, g, b);
	}
}
