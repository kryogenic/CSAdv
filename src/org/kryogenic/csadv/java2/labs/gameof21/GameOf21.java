package org.kryogenic.csadv.java2.labs.gameof21;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class GameOf21 extends JFrame {
	private static final long serialVersionUID = 1L;

	private int cardIdx = 0;
	
	public static void main(final String... args) {
		new GameOf21();
	}
	
	public GameOf21() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setSize(280, 265);
		
		Container c = this.getContentPane();
		c.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		final CardTile[] dcards = { new CardTile(true, false), new CardTile(false, false), new CardTile(false, false) };
		int gridx = 0;
		for(CardTile card : dcards) {
			gbc.anchor = GridBagConstraints.FIRST_LINE_START;
			gbc.gridx = gridx;
			gridx++;
			gbc.gridy = 0;
			gbc.ipadx = 90;
			gbc.ipady = 90;
			gbc.weightx = 1/3;
			gbc.weighty = 1/2;
			c.add(card, gbc);
		}
		
		final CardTile[] pcards = { new CardTile(false, true), new CardTile(false, true), new CardTile(false, true) };
		gridx = 0;
		for(CardTile card : pcards) {
			gbc.anchor = GridBagConstraints.FIRST_LINE_START;
			gbc.gridx = gridx;
			gridx++;
			gbc.gridy = 1;
			gbc.ipadx = 90;
			gbc.ipady = 90;
			gbc.weightx = 1/3;
			gbc.weighty = 1/2;
			c.add(card, gbc);
		}
		
		final JLabel pscore = new JLabel("Your score: 0");
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.VERTICAL;
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.ipadx = 0;
		gbc.ipady = 10;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		c.add(pscore, gbc);
		
		final JLabel dscore = new JLabel("Dealer score: " + CardTile.getScore(dcards[0]));
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.VERTICAL;
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.ipadx = 0;
		gbc.ipady = 10;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		c.add(dscore, gbc);

		final ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String c = e.getActionCommand();
				action(dcards, pcards, dscore, pscore, Action.valueOf(c));
			}
		};
		
		final JButton stand = new JButton("End");
		stand.setFocusPainted(false);
		stand.addActionListener(actionListener);
		stand.setActionCommand("STAND");
		gbc.anchor = GridBagConstraints.LAST_LINE_END;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.ipadx = 0;
		gbc.ipady = 0;
		gbc.gridwidth = 1;
		gbc.weightx = 1/3;
		gbc.weighty = 1/3;
		c.add(stand, gbc);
		
		final JButton hitMe = new JButton("Play");
		hitMe.setFocusPainted(false);
		hitMe.addActionListener(actionListener);
		hitMe.setActionCommand("HIT");
		gbc.anchor = GridBagConstraints.LAST_LINE_START;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.ipadx = 0;
		gbc.ipady = 0;
		gbc.gridwidth = 1;
		gbc.weightx = 1/3;
		gbc.weighty = 1/3;
		c.add(hitMe, gbc);
		
		final JButton newGame = new JButton("New Game");
		newGame.setFocusPainted(false);
		newGame.addActionListener(actionListener);
		newGame.setActionCommand("NEW_GAME");
		gbc.anchor = GridBagConstraints.LAST_LINE_START;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 2;
		gbc.gridy = 3;
		gbc.ipadx = 0;
		gbc.ipady = 0;
		gbc.gridwidth = 1;
		gbc.weightx = 1/3;
		gbc.weighty = 1/3;
		c.add(newGame, gbc);
		
		this.setVisible(true);
	}
	
	private enum Action {
		HIT, STAND, NEW_GAME
	}

	private void action(CardTile[] dcards, CardTile[] pcards, JLabel dscore, JLabel pscore, Action a) {
		switch(a) {
		case HIT:
			if(cardIdx < 3) {
				pcards[cardIdx].flip();
				cardIdx++;
				if(cardIdx < 2) {
					pcards[cardIdx].flip();
					cardIdx++;
				}
				CardTile[] visibleCards = new CardTile[cardIdx];
				System.arraycopy(pcards, 0, visibleCards, 0, cardIdx);
				pscore.setText("Your Score: " + CardTile.getScore(visibleCards));
				pscore.repaint();
				if(cardIdx == 3) {
					action(dcards, pcards, dscore, pscore, Action.STAND);
					cardIdx = 0;
				}
			}
			break;
		case STAND:
			dcards[1].flip();
			CardTile[] visibleCards = new CardTile[2];
			System.arraycopy(dcards, 0, visibleCards, 0, 2);
			if(CardTile.getScore(visibleCards) < 17) {
				dcards[2].flip();
				visibleCards = new CardTile[3];
				System.arraycopy(dcards, 0, visibleCards, 0, 3);
			}
			int dealer = CardTile.getScore(visibleCards);
			dscore.setText("Dealer score: " + dealer);
			dscore.repaint();
			int player = Integer.parseInt(pscore.getText().substring(12));
			String message = "You ";
			if(player > 21) {
				message += "bust...";
			} else if(dealer > 21 || player > dealer) {
				message += "win!";
			} else if (dealer == player) {
				message += "tie.";
			} else {
				message += "lose...";
			}
			message += "\nDealer's score: " + dealer + ". Your score: " + player + ".";
			message += "\nPlay Again?";
			int response = JOptionPane.showConfirmDialog(GameOf21.this, message, "Game Over", JOptionPane.YES_NO_OPTION);
			if(response == JOptionPane.YES_OPTION) {
				action(dcards, pcards, dscore, pscore, Action.NEW_GAME);
			} else {
				System.exit(0);
			}
			break;
		case NEW_GAME:
			for(int i = 0; i < 6; i++) {
				if(i < 3) {
					dcards[i].regen();
					if(i == 0) {
						dcards[i].flip();
					}
				} else {
					pcards[i - 3].regen();
				}
			}
			cardIdx = 0;
			//action(dcards, pcards, dscore, pscore, Action.HIT);
			dscore.setText("Dealer score: " + CardTile.getScore(dcards[0]));
			pscore.setText("Your score: 0");
			break;
		}
	}
}
