package org.kryogenic.csadv.finalproj1;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JFrame;

/**
 * @author: Kale
 * @date: 27/11/12
 */
public class Main {
	public static void main(final String... args) {
        final JFrame f = new JFrame();
        final Canvas d = new Canvas();
        f.getContentPane().add(d);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(416, 439);
        f.setVisible(true);

        final AtomicBoolean needsRepaint = new AtomicBoolean(false);
        final Object lock = new Object();
        new Thread(new Runnable() {
            public void run() {
                final int tickMin = 12;
                long tickStart;
                while(true) {
                    tickStart = System.currentTimeMillis();
                    d.update();
                    needsRepaint.set(true);
                    synchronized (lock) {
                        lock.notify();
                    }
                    long deltaTick = System.currentTimeMillis() - tickStart;
                    if(deltaTick < tickMin)
                        try{
                            Thread.sleep(tickMin - deltaTick);
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                }
            }
        }).start();
        new Thread(new Runnable() {
            public void run() {
                while(true) {
                    d.repaint();
                    d.tick();
                    needsRepaint.set(false);
                    while(!needsRepaint.get()) {
                        synchronized (lock) {
                            try {
                                lock.wait();
                            } catch(InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }).start();
	}
}
