package org.kryogenic.csadv.finalproj1;

/**
 * @author: Kale
 * @date: 07/01/13
 *
 * Keeps track of FPS
 */
public class FPSCounter {
    private final long interval;
    
    private double lastFPS = 0;
    private int ticks;
    private long time;

    /**
     * Constructs a new FPS counter that updates every <pre>interval</pre> milliseconds
     * @param interval the time in milliseconds between FPS updates
     */
    public FPSCounter(long interval) {
        this.interval = interval;
        this.time = System.currentTimeMillis();
    }

    /**
     * Gets the latest FPS value
     * @return the latest FPS value
     */
    public double getFPS() {
        reset();
        return lastFPS;
    }
    /**
     * Call this after every complete frame cycle
     */
    public void tick() {
        reset();
        ticks++;
    }

    private void reset() {
        long currentTime = System.currentTimeMillis();
        if(currentTime - time > interval) {
            lastFPS = (double) ticks / ((currentTime - time) / 1000d);
            ticks = 0;
            time += (int)((currentTime - time) / interval) * interval;
        }
    }
}
