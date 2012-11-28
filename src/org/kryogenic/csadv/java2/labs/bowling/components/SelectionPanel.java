package org.kryogenic.csadv.java2.labs.bowling.components;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import org.kryogenic.csadv.java2.labs.bowling.Bowling;

public class SelectionPanel extends AbstractPanel {
	public static final long serialVersionUID = 0;
	public SelectionPanel(Bowling parent) {
		super(parent, new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 5, 5);
		this.add(new JLabel("Players: "), c);
		c.gridx = 1;
		c.gridy = 0;
		c.ipadx = 50;
		final JComboBox players = new JComboBox(new String[]{"One", "Two", "Three", "Four"});
		this.add(players, c);
		JButton play = new JButton("Play");
		final Bowling p = super.parent;
		play.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				p.play(players.getSelectedIndex() + 1);
			}
		});
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		c.insets = new Insets(2, 5, 5, 5);
		c.ipadx = 125;
		c.ipady = 10;
		this.add(play, c);
	}
	
	public void setUp() {
		super.setUp();
		parent.pack();
	}
}