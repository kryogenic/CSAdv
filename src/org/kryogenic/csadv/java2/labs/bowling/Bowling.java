package org.kryogenic.csadv.java2.labs.bowling;

import javax.swing.JFrame;

import org.kryogenic.csadv.java2.labs.bowling.components.BowlingPanel;
import org.kryogenic.csadv.java2.labs.bowling.components.SelectionPanel;

public class Bowling extends JFrame {
	private static final long serialVersionUID = 1L;
	private BowlingPanel bowlPan;
	private final SelectionPanel selPan = new SelectionPanel(this);
	public final String workingDirectory = "H:/CS_Adv/2_Java2_KSchoorlemmer/src/org/kryogenic/csadv/java2/labs/bowling/";
	
	public Players players;
	
	public Bowling() {
		selPan.setUp();
		this.setResizable(false);
	}
	
	public void play(int numPlayers) {
		players = new Players(numPlayers);
		this.remove(selPan);
		bowlPan = new BowlingPanel(this);
		bowlPan.setUp();
		this.validate();
	}
	
	public static void main(final String... args) {
		new Bowling();
	}
}
