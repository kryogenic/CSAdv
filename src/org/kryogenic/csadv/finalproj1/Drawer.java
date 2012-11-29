package org.kryogenic.csadv.finalproj1;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

import javax.swing.JComponent;

/**
 * @author: Kale
 * @date: 27/11/12
 */
public class Drawer extends JComponent implements MouseListener {
	Set<Circle> circles = new LinkedHashSet<Circle>();
    Set<Circle> toAdd = new HashSet<Circle>();
	Random r = new Random();
	
	public Drawer() {
		/*this.addMouseMotionListener(new MouseMotionListener() {
			public void mouseMoved(MouseEvent e) {
				circles.add(new Circle(r.nextFloat(), r.nextFloat(), e.getPoint()));
			}
			public void mouseDragged(MouseEvent e) {
				circles.add(new Circle(r.nextFloat(), r.nextFloat(), e.getPoint()));
			}
		});*/
        this.addMouseListener(this);
	}
	
	protected void paintComponent(Graphics g) {
		g.setColor(Color.getHSBColor(0, 0, 1/8f));
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		for(Circle c : circles)
			c.draw(g2);
	}
    
    public void update() {
        for(Circle c : toAdd) {
            circles.add(c);
        }
        for(Circle c : circles) {
            c.update();
            Ellipse2D e1 = c.getShape();
            if(e1.getMaxY() >= this.getHeight()) {
                c.addForce(new Force(0, 0, 0, Math.abs(c.last.getY()) * 2, new Falloff.Collision()));
            } else if (e1.getMinY() <= 0) {
                c.addForce(new Force(0, c.last.getY() * 2, 0, 0, new Falloff.Collision()));
            } else if (e1.getMaxX() >= this.getWidth()) {
                c.addForce(new Force(0, 0, c.last.getX() * 2, 0, new Falloff.Collision()));
            } else if (e1.getMinX() <= 0) {
                c.addForce(new Force(c.last.getX() * 2, 0, 0, 0, new Falloff.Collision()));
            }
            for(Circle c2 : circles) {
                if(c.equals(c2))
                    continue;
                Ellipse2D e2 = c2.getShape();
                if(Math.sqrt(Math.pow(e1.getCenterX() - e2.getCenterX(), 2) + Math.pow(e1.getCenterY() - e2.getCenterY(), 2)) <= 75) {
                    //c.addForce(new Force((float) e1.getCenterX(), (float) e1.getCenterY(), (float) e2.getCenterX(), (float) e2.getCenterY(), new Falloff.Collision()));
                    // todo: fix the force cancelling issue. the collision of another circle ends up hitting too hard and making it sink...
                    c.addForce(new Force((float) e1.getCenterX(), (float) e1.getCenterY(), (float) e2.getCenterX(), (float) e2.getCenterY(), new Falloff.Collision()));
                }
            }
        }
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
		toAdd.add(new Circle(r.nextFloat(), r.nextFloat(), e.getPoint()));
	}
}
