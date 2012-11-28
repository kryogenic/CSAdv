package org.kryogenic.csadv.java2.labs.gameof21;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

public class CardTile extends JComponent {
	private static final Random r = new Random();
	private static final File workingDirectory = new File("H:/CS_Adv/2_Java2_KSchoorlemmer/src/org/kryogenic/csadv/java2/labs/gameof21/cards/");
	private static final BufferedImage[] blkCards = new BufferedImage[13];
	private static final BufferedImage[] redCards = new BufferedImage[13];
	private static final BufferedImage[] suits = new BufferedImage[4];
	private static BufferedImage pback = null;
	private static BufferedImage dback = null;
	static {
		try {
			for(int i = 0; i < 13; i++) {
				blkCards[i] = ImageIO.read(new File(workingDirectory, (i + 1) + ".png"));
				redCards[i] = ImageIO.read(new File(workingDirectory, (i + 1) + "r.png"));
			}
			String[] suitNames = { "spades", "hearts", "clubs", "diamonds" };
			for(int i = 0; i < 4; i++) {
				suits[i] = ImageIO.read(new File(workingDirectory, suitNames[i] + ".png"));
			}
			pback = ImageIO.read(new File(workingDirectory, "playerback.png"));
			dback = ImageIO.read(new File(workingDirectory, "dealerback.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private int suit; // 0: spades 1: hearts 2: clubs 3: diamonds
	private int card; // 0: ace, 1-9: 2-10, 10: J, 11: Q, 12: K
	private boolean faceup = false;
	private final boolean player;
	
	public CardTile(boolean faceup, boolean player) {
		this.faceup = faceup;
		this.player = player;
		this.suit = r.nextInt(4);
		this.card = r.nextInt(13);
	}
	
	public static int getScore(CardTile... tiles) {
		int score = 0;
		int aces = 0;
		for(CardTile c : tiles) {
			if(c != null) {
				if(c.card > 0) {
					score += Math.min(c.card + 1, 10);
				} else {
					aces++;
				}
			}
		}
		for(int i = 0; i < aces; i++) {
			if(score < 11) {
				score += 11;
			} else {
				score += 1;
			}
		}
		return score;
	}
	
	public void flip() {
		this.faceup = true;
		this.repaint();
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(faceup) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 90, 90);
			g.setColor(Color.WHITE);
			g.fillRect(1, 1, 88, 88);
			if(suit % 2 == 0) {
				g.drawImage(blkCards[card], 4, 8, null);
			} else {
				g.drawImage(redCards[card], 4, 8, null);
			}
			g.drawImage(suits[suit], 47, 47, null);
		} else {
			if(player) {
				g.drawImage(pback, 0, 0, null);
			} else {
				g.drawImage(dback, 0, 0, null);
			}
		}
	}
	
	public void regen() {
		this.suit = r.nextInt(4);
		this.card = r.nextInt(13);
		this.faceup = false;
		this.repaint();
	}
}
