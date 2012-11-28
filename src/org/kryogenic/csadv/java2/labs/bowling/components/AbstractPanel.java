package org.kryogenic.csadv.java2.labs.bowling.components;

import java.awt.LayoutManager;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.kryogenic.csadv.java2.labs.bowling.Bowling;

public abstract class AbstractPanel extends JPanel {
	public static final long serialVersionUID = 0;
	final Bowling parent;
	
	protected AbstractPanel(Bowling parent, LayoutManager l) {
		super(l);
		this.parent = parent;
	}
	
	public void setUp() {
		parent.add(this);
		parent.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		parent.setLocationRelativeTo(null);
		parent.setVisible(true);
	}
}
