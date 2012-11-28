package org.kryogenic.csadv.finalproj1;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.swing.JComponent;

public class Drawer extends JComponent implements MouseListener {
	Set<Circle> circles = new HashSet<Circle>();
	Random r = new Random();
	protected void paintComponent(Graphics g) {
		g.setColor(new Color(45, 45, 45));
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		for(Circle c : circles)
			c.draw(g);
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		circles.add(new Circle(Color.getHSBColor(r.nextFloat(), r.nextFloat(), 255), e.getPoint()));
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
