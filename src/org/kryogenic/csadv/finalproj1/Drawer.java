package org.kryogenic.csadv.finalproj1;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

import javax.swing.JComponent;

public class Drawer extends JComponent implements MouseListener {
	Set<Circle> circles = new LinkedHashSet<Circle>();
	Random r = new Random();
	float minBrightness = 1/8f;
	
	public Drawer() {
		this.addMouseMotionListener(new MouseMotionListener() {
			public void mouseMoved(MouseEvent e) {
				circles.add(new Circle(r.nextFloat(), r.nextFloat(), e.getPoint()));
			}
			public void mouseDragged(MouseEvent e) {
				circles.add(new Circle(r.nextFloat(), r.nextFloat(), e.getPoint()));
			}
		});
	}
	
	protected void paintComponent(Graphics g) {
		g.setColor(Color.getHSBColor(0, 0, minBrightness));
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		for(Circle c : circles)
			c.draw(g);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {
		circles.add(new Circle(r.nextFloat(), r.nextFloat(), e.getPoint()));
	}
}
