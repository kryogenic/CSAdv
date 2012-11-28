package org.kryogenic.csadv.finalproj1;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Circle {
	private final float HUE;
	private final float SAT;
	
	private boolean growing = true;
	private float life;
	private Point p;
	
	public Circle(float hue, float sat, Point p) {
		this(hue, sat, 1/8f, p);
	}
	private Circle(float hue, float sat, float life, Point p) {
		this.HUE = hue;
		this.SAT = sat;
		this.life = life > 1 ? 1 : life;
		this.p = p;
	}
	
	public void draw(Graphics g) {
		if(growing)
			growing = (life += 0.05) <= 1;
		g.setColor(Color.getHSBColor(HUE, SAT, life));
		float d = life * 100;
		float r = d / 2;
		g.fillOval(p.x - (int)r, p.y - (int)r,  (int)d, (int)d);
		g.setColor(new Color(HUE, SAT, 1));
		//g.drawString("Hue: " + String.valueOf(HUE) + " Sat: " + String.valueOf(SAT), p.x, p.y);
		//g.drawString("Bright: " + String.valueOf(life), p.x, p.y + 20);
	}
}
