package org.kryogenic.csadv.finalproj1;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JFrame;

/**
 * @author: Kale
 * @date: 27/11/12
 */
public class Main {
	public static void main(final String... args) {
        // set up the frame and canvas
        final JFrame frame = new JFrame();
        final Canvas canvas = new Canvas();
        frame.getContentPane().add(canvas);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(416, 439);
        frame.setVisible(true);


        final AtomicBoolean needsRepaint = new AtomicBoolean(false);
        final Object lock = new Object(); // used so the repaint thread doesn't repaint until the Circles have moved

        // updater thread
        new Thread(new Runnable() {
            public void run() {
                final int tickMin = 12; // minimum time for each tick
                long tickStart;
                while(true) {
                    tickStart = System.currentTimeMillis();
                    // update the positions of all the Circles
                    canvas.update();
                    // now that we have updated data for the Circles, we need to tell the repaint thread to redraw
                    needsRepaint.set(true);
                    synchronized (lock) {
                        lock.notify();
                    }
                    // wait until next tick, if necessary
                    long deltaTick = System.currentTimeMillis() - tickStart;
                    if(deltaTick < tickMin)
                        try{
                            Thread.sleep(tickMin - deltaTick);
                        } catch(InterruptedException e) {
                            e.printStackTrace();
                        }
                }
            }
        }).start();

        // repaint thread
        new Thread(new Runnable() {
            public void run() {
                while(true) {
                    // redraw the whole canvas
                    canvas.repaint();
                    // tick the FPS counter
                    canvas.tick();
                    // wait until the next update
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
