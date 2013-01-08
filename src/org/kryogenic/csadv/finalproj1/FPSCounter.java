package org.kryogenic.csadv.finalproj1;

/**
 * @author: Kale
 * @date: 07/01/13
 */
public class FPSCounter {
    private final long interval;
    
    private double lastFPS = 0;
    private int ticks;
    private long time;
    
    public FPSCounter(long interval) {
        this.interval = interval;
        this.time = System.currentTimeMillis();
    }

    public double getFPS() {
        reset();
        return lastFPS;
    }
    
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
