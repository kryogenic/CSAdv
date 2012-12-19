package org.kryogenic.csadv.finalproj1;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

import javax.swing.JComponent;

/**
 * @author: Kale
 * @date: 27/11/12
 */
public class Drawer extends JComponent implements MouseListener {
	static final long serialVersionUID = 1;
	Set<Circle> circles = new LinkedHashSet<Circle>();
	Random r = new Random();
	
	public Drawer() {
		this.addMouseMotionListener(new MouseMotionListener() {
			public void mouseMoved(MouseEvent e) {
				synchronized(Drawer.this) {
					circles.add(
							new Circle(
									r.nextFloat(),
									r.nextFloat(),
									(float)Math.abs(r.nextGaussian()) + 7,
									e.getPoint(),
									new Vector2D(
										(float)r.nextGaussian() * 10,
										(float)r.nextGaussian() * 10
									)
							)
					);
				}
			}
			public void mouseDragged(MouseEvent e) {
				synchronized(Drawer.this) {
					circles.add(
							new Circle(
									r.nextFloat(),
									r.nextFloat(),
									(float)Math.abs(r.nextGaussian()) + 7,
									e.getPoint(),
									new Vector2D(
										(float)r.nextGaussian() * 10,
										(float)r.nextGaussian() * 10)
									)
							);
				}
			}
		});
        //this.addMouseListener(this);
	}
	
	protected void paintComponent(Graphics g) {
		g.setColor(Color.getHSBColor(0, 0, 1/8f));
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        synchronized(this) {
			for(Circle c : circles)
				c.draw(g2, false, circles);
        }
	}
    
    public void update() {
    	synchronized(this) {
	        for(Iterator<Circle> i = circles.iterator(); i.hasNext();) {
	        	Circle c = i.next();
	            if(!c.update()) {
	            	//i.remove();
	            }
	            Ellipse2D e1 = c.shape();
	            if(e1.getMaxY() >= this.getHeight()) {
	                c.signForces(TriPlane.VERTICAL, Sign.NEGATIVE);
	            } else if (e1.getMinY() <= 0) {
	                c.signForces(TriPlane.VERTICAL, Sign.POSITIVE);
	            } else if (e1.getMaxX() >= this.getWidth()) {
	                c.signForces(TriPlane.HORIZONTAL, Sign.NEGATIVE);
	            } else if (e1.getMinX() <= 0) {
	                c.signForces(TriPlane.HORIZONTAL, Sign.POSITIVE);
	            }
	            for(Circle c2 : circles) {
	                if(c.equals(c2))
	                    continue;
	                // check for collision
	                float minCollisionRange = c.radius() + c2.radius();
	                Vector2D normal = new Vector2D(c.center().x - c2.center().x, c.center().y - c2.center().y);
	                // handle collision
	                if(normal.length() < minCollisionRange) {
	                	Ellipse2D e2 = c.shape();
	                	Area a1 = new Area(e1);
	                	Area a2 = new Area(e2);
	                	a1.intersect(a2);
	                	Rectangle2D b = a1.getBounds2D();
	                	Vector2D v = new Vector2D((float)b.getWidth(), (float)b.getHeight());
	                	c.applyVector(v.normalize().multiply(normal.length() - minCollisionRange));
	                	System.out.println("Balls were " + (normal.length() - minCollisionRange) + "px away. Moved " + v.length() + "px");
	                	//c2.addVector(c.velocity());
	                	//collisionValue = collisionValue < 1 ? 1 : collisionValue;
	                    //c.addForce(new Force(c2.center().x * collisionValue, c2.center().y * collisionValue, c.center().x * collisionValue, c.center().y * collisionValue, new Falloff.Collision()));
	                	c.reflectForces(normal);
	                }
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
		synchronized(this) {
			circles.add(
				new Circle(
						r.nextFloat(),
						r.nextFloat(),
						(float)Math.abs(r.nextGaussian()) + 7,
						e.getPoint(),
						new Vector2D(
							(float)r.nextGaussian() * 10,
							(float)r.nextGaussian() * 10)
						)
				);
		}
	}
}
