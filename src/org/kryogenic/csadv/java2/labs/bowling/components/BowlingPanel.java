package org.kryogenic.csadv.java2.labs.bowling.components;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.kryogenic.csadv.java2.labs.bowling.Bowling;

public class BowlingPanel extends AbstractPanel {
	private static final long serialVersionUID = 0;
	private int currentPlayer = 0;
	private final ScoreTable scoreTable;
	
	private int bowlingTicks = 0;
	private final int maxBowlingTicks = 4500;
	
	private int[] pinsHit = new int[2];
	private int ballNumber = -1;
	
	private final Random r = new Random();
	
	private final ViewComp viewComp = new ViewComp();
	
	public BowlingPanel(Bowling parent) {
		super(parent, new BorderLayout());
		this.add(new ControlPanel(), BorderLayout.NORTH);
		scoreTable = new ScoreTable(parent.players);
		this.add(viewComp, BorderLayout.CENTER);
		this.add(scoreTable, BorderLayout.AFTER_LAST_LINE);
	}
	public void setUp() {
		super.setUp();
		parent.setSize(1000, 240);
	}
	
	private class ControlPanel extends JPanel {
		private static final long serialVersionUID = 0;
		public ControlPanel() {
			super(new FlowLayout());
			final JLabel playerLabel = new JLabel(parent.players.get().toString());
			this.add(playerLabel);
			JButton bowlButton = new JButton("Bowl!");
			bowlButton.addActionListener(new ActionListener() {
				private boolean bowling = false;
				public void actionPerformed(ActionEvent e) {
					if(bowling == true) {
						return;
					}
					bowling = true;
					bowlingTicks = maxBowlingTicks;
				    ballNumber++;
				    if(ballNumber == 2) {
				    	ballNumber = 0;
				    	pinsHit[1] = 0;
				    	currentPlayer += currentPlayer == 3 ? -3 : 1;
				    }
					pinsHit[ballNumber] = Math.min(Math.max(r.nextInt(1424) - 200, 0), 1023);
					if(ballNumber == 1) { // if we're on the second ball
						pinsHit[ballNumber] &= ~pinsHit[0]; // don't hit any pins that we hit last ball
					}
					final int pinCount = Integer.bitCount(pinsHit[ballNumber]);
					try {
				          Clip clip = AudioSystem.getClip();
				          AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(parent.workingDirectory, "bowl3s.wav"));
				          clip.open(inputStream);
				          clip.start();
				          clip.addLineListener(new LineListener() {
				        	  public void update(LineEvent e) {
				        		  if(e.getType() == LineEvent.Type.STOP) {
				        			  String message = parent.players.get().toString() + " hit " + pinCount + " pin" + (pinCount == 1 ? "" : "s") + (pinCount == 0 ? "..." : "!");
				        			  int totalPinsHit = Integer.bitCount(pinsHit[0] | pinsHit[1]);
				        			  int score = totalPinsHit;
				                      if(totalPinsHit == 10) {
				                    	  if(ballNumber == 0) {
				                    		  message += "\nStrike! 20 points awarded.";
				                    		  score += 10;
				                    		  ballNumber = 1;
				                    	  } else if(ballNumber == 1) {
				                    		  message += "\nSpare! 15 points awarded.";
				                    		  score += 5;
				                    	  }
				        			  } else if(ballNumber == 1) {
				        				  message += "\n" + totalPinsHit + " points awarded.";
				        			  }
				                      parent.players.get().addScore(score);
				                      if(ballNumber == 1) {
				                    	  parent.players.step();
				                    	  playerLabel.setText(parent.players.get().toString());
				                    	  playerLabel.repaint();
				                      }
				                      bowling = false;
				                      scoreTable.repaint();
				        			  JOptionPane.showMessageDialog(null, message);
				        			  e.getLine().close();
				        		  } else if(e.getType() == LineEvent.Type.START && pinsHit[ballNumber] == 0) {
				        			  try {
				        			  	Thread.sleep(1650);
				        			  } catch (Exception ex) {
				        				  ex.printStackTrace();
				        			  }
				        			  e.getLine().close();
				        		  }
				        	  }
				          });
				    } catch (Exception ex) {
				          ex.printStackTrace();
				    }
				}
			});
			this.add(bowlButton);
		}
	}
	private class ViewComp extends JComponent {
		private static final long serialVersionUID = 0;
		BufferedImage ball, pin;
		
		public ViewComp() {
			try {
				ball = ImageIO.read(new File(parent.workingDirectory, "bowling_ball.png"));
				pin = ImageIO.read(new File(parent.workingDirectory, "bowling_pin.png"));
			} catch (Exception e) {
				System.out.println(new File(parent.workingDirectory, "bowling_pin.png").getAbsolutePath());
				e.printStackTrace();
			}
			new Thread(new Runnable() {
				public void run() {
					while(true) {
						while(bowlingTicks > 0) {
							repaint();
						}
					}
				}
			}).start();
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			double angle = Math.toRadians((maxBowlingTicks - bowlingTicks) / (maxBowlingTicks / 1080d));
			BufferedImage rotated = rotate(ball, angle);
			
		    int x = (int) ((this.getWidth() + rotated.getWidth()) / (double) maxBowlingTicks * (maxBowlingTicks - bowlingTicks) - rotated.getWidth());
		    int y = this.getHeight() - rotated.getHeight() / 2 - ball.getHeight() / 2 - Math.max((int)(50*Math.pow((maxBowlingTicks - bowlingTicks)/200d + 0.5, -1) - 10), 0);
		    
			g.drawImage(rotated, x, y, null);
			
			int pinsY = this.getHeight() - pin.getHeight();
			int pinsX = this.getWidth() - pin.getWidth();
			int row = 0;
			int pinNumber = 0;
			for(int i = 4; i >= 0; i--, row++) {
				for(int j = 0; j < row; j++, pinNumber++) {
					int pinX = pinsX - i * 12;
					if(ballNumber <= 0 || ((pinsHit[0] & (1 << pinNumber)) == 0)) { // if we didn't hit this pin with the last ball
						if(ballNumber == -1) { // if we haven't bowled yet
							g.drawImage(pin, pinX, pinsY, null);
						} else if(x > pinX && (pinsHit[ballNumber] & (1 << pinNumber)) != 0) { // if the pin we are checking has been hit, and the ball is hitting the pins
							if(x < this.getWidth()) { // if the ball is not off screen
								g.drawImage(rotate(pin, r.nextDouble() * 2 * Math.PI), (int) (pinX + r.nextDouble() * 2 * (x - pinX)), (int) (pinsY - r.nextDouble() * 2 * (x - pinX)), null);
							}
						} else {
							g.drawImage(pin, pinX, pinsY, null);
						}
					}
				}
			}
			bowlingTicks--;
		}
		
		private BufferedImage rotate(BufferedImage i, double angle) {
			double sin = Math.abs(Math.sin(angle)), cos = Math.abs(Math.cos(angle));
		    int w = i.getWidth(), h = i.getHeight();
		    int neww = (int)Math.floor(w*cos+h*sin), newh = (int)Math.floor(h*cos+w*sin);
		    GraphicsConfiguration gc = this.getGraphicsConfiguration();
		    BufferedImage result = gc.createCompatibleImage(neww, newh, Transparency.TRANSLUCENT);
		    
		    Graphics2D g2 = result.createGraphics();
		    g2.translate((neww-w)/2, (newh-h)/2);
		    g2.rotate(angle, w/2d, h/2d);
		    g2.drawRenderedImage(i, null);
		    g2.dispose();
		    
		    return result;
		}
	}
}


