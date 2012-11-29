package org.kryogenic.csadv.finalproj1;

import java.awt.Container;

import javax.swing.JFrame;

/**
 * @author: Kale
 * @date: 27/11/12
 */
public class Circles {
	public static void main(final String... args) {
		final JFrame f = new JFrame();
		Container c = f.getContentPane();
        final Drawer d = new Drawer();
		c.add(d);
		f.setSize(400, 400);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		new Thread(new Runnable() {
			public void run() {
				while(true) {
					d.repaint();
                    d.update();
					try {
						Thread.sleep(30);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
}
