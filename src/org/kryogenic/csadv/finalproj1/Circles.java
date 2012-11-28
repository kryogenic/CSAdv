package org.kryogenic.csadv.finalproj1;

import java.awt.Container;

import javax.swing.JFrame;

public class Circles {
	public static void main(final String... args) {
		JFrame f = new JFrame();
		Container c = f.getContentPane();
		c.add(new Drawer());
		f.setSize(400, 400);
		f.setVisible(true);
		while(true)
			f.repaint();
	}
}
