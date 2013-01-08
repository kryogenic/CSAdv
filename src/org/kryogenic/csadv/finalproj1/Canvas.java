package org.kryogenic.csadv.finalproj1;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

import javax.swing.JComponent;

/**
 * @author: Kale
 * @date: 27/11/12
 *
 * This class keeps track of the circles, adds more, and draws them
 */
public class Canvas extends JComponent {
    
    // set this to true to see FPS and number of circles generated
    private final boolean debug = false;
    
    private final FPSCounter fps = new FPSCounter(1000);
	private final Set<Circle> circles = new LinkedHashSet<Circle>();
    private final Set<Circle> toAdd = new LinkedHashSet<Circle>();
    
    private int circlesGenerated = 0;
	
	public Canvas() {
        // all this does is add a listener so the user can generate circles
        final Random r = new Random();
		this.addMouseMotionListener(new MouseMotionListener() {
			public void mouseMoved(MouseEvent e) {}
			public void mouseDragged(MouseEvent e) {
				synchronized(Canvas.this) {
					toAdd.add(
                        new Circle(
                                r.nextFloat(),
                                r.nextFloat(),
                                (float)Math.abs(r.nextGaussian()) + 7,
                                e.getPoint(),
                                new Vector2D(
                                    (float)r.nextGaussian() * 15,
                                    (float)r.nextGaussian() * 15)
                                )
                        );
				}
                circlesGenerated++;
			}
		});
    }
    /**
     * Used by the Main class to keep track of FPS
     */
    public void tick() {
        fps.tick();
    }
	@Override
	protected void paintComponent(Graphics g) {
        // paint background
		g.setColor(Color.getHSBColor(0, 0, 1/8f));
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // paint circles
        synchronized(this) {
			for(Circle c : circles)
				c.draw(g2);
        }
        // paint debug
        if(debug) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, 80, 28);
            g.setColor(Color.WHITE);
            g.drawString("Generated: " + circlesGenerated, 2, 12);
            g.drawString(String.format("FPS: %.2f", fps.getFPS()), 2, 24);
        }
	}
    /**
     * This is the updating method
     * It calls the update methods of all the circles; checks for, and handles collisions
     */
    public void update() {
        for(Circle c : circles) {
            c.update();
            Ellipse2D e1 = c.shape();
            // if the Circle has hit the wall, reverse its direction
            if(e1.getMaxY() >= this.getHeight()) {
                c.signVelocity(Vector2D.Plane.VERTICAL, Vector2D.Sign.NEGATIVE);
            } else if (e1.getMinY() <= 0) {
                c.signVelocity(Vector2D.Plane.VERTICAL, Vector2D.Sign.POSITIVE);
            } else if (e1.getMaxX() >= this.getWidth()) {
                c.signVelocity(Vector2D.Plane.HORIZONTAL, Vector2D.Sign.NEGATIVE);
            } else if (e1.getMinX() <= 0) {
                c.signVelocity(Vector2D.Plane.HORIZONTAL, Vector2D.Sign.POSITIVE);
            }
            // check all Circles to see if they are colliding with this Circle
            boolean foundSelf = false;
            for(Circle c2 : circles) {
                if(!foundSelf && c.equals(c2)) { // unless they're the same :P
                    foundSelf = true;
                    continue;
                }
                // check for collision
                Vector2D normal = new Vector2D(c.center().x - c2.center().x, c.center().y - c2.center().y); // the position of c in relation to c2
                double distanceApart = normal.length() - (c.radius() + c2.radius());
                if(distanceApart <= 0) { // they are less than or equal to 0 pixels apart... colliding!
                    // move the two circles so they're no longer inside each other
                    c.applyVector(normal.normalize().multiplyLength(distanceApart / 2)); // move c halfway out of c2
                    c2.applyVector(normal.normalize().multiplyLength(distanceApart / 2).flip(Vector2D.Plane.BOTH)); // move c2 the other halfway out of c
                    // reflect their velocities
                    c.reflectVelocity(normal);
                    c2.reflectVelocity(normal);
                }
            }
    	}
        // add the circles we've been waiting to add while we were iterating
        synchronized(this) {
            circles.addAll(toAdd);
        }
    }
}
