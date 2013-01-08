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
 */
public class Canvas extends JComponent {
    
    private final FPSCounter fps = new FPSCounter(1000);
	private final Set<Circle> circles = new LinkedHashSet<Circle>();
    private final Set<Circle> toAdd = new LinkedHashSet<Circle>();
    
    private int circlesGenerated = 0;
	
	public Canvas() {
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

    public void tick() {
        fps.tick();
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
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 120, 28);
        g.setColor(Color.WHITE);
        g.drawString("Generated: " + circlesGenerated, 2, 12);
        g.drawString(String.format("FPS: %.2f", fps.getFPS()), 2, 24);
	}
    
    public void update() {
        for(Circle c : circles) {
            c.update();
            Ellipse2D e1 = c.shape();
            if(e1.getMaxY() >= this.getHeight()) {
                c.signVelocity(Vector2D.Plane.VERTICAL, Vector2D.Sign.NEGATIVE);
            } else if (e1.getMinY() <= 0) {
                c.signVelocity(Vector2D.Plane.VERTICAL, Vector2D.Sign.POSITIVE);
            } else if (e1.getMaxX() >= this.getWidth()) {
                c.signVelocity(Vector2D.Plane.HORIZONTAL, Vector2D.Sign.NEGATIVE);
            } else if (e1.getMinX() <= 0) {
                c.signVelocity(Vector2D.Plane.HORIZONTAL, Vector2D.Sign.POSITIVE);
            }
            for(Circle c2 : circles) {
                if(c.equals(c2))
                    continue;
                // check for collision
                Vector2D normal = new Vector2D(c.center().x - c2.center().x, c.center().y - c2.center().y); // the position of c in relation to c2
                double distanceApart = normal.length() - (c.radius() + c2.radius());
                if(distanceApart < 0) { // if c is inside c2 (colliding)
                    // handle collision
                    c.applyVector(normal.normalize().multiplyLength(distanceApart / 2)); // move c halfway out of c2
                    c2.applyVector(normal.normalize().multiplyLength(distanceApart / 2).flip(Vector2D.Plane.BOTH)); // move c2 the other halfway out of c
                    Vector2D updated_normal = new Vector2D(c.center().x - c2.center().x, c.center().y - c2.center().y); // the position of c in relation to c2
                    c.reflectVelocity(updated_normal);
                    c2.reflectVelocity(updated_normal);
                    //c.affect(c2);
                    //c2.affect(c);
                }
            }
    	}
        synchronized(this) {
            circles.addAll(toAdd);
        }
    }
}
