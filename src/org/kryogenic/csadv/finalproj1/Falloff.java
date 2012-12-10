package org.kryogenic.csadv.finalproj1;

/**
 * @author: Kale
 * @date: 28/11/12
 */
public abstract class Falloff {
    public float get(float x) {
        float y = getRaw(x);
        return y < 0 ? 0 : y;
    }
    protected abstract float getRaw(float x);
    
    static class Collision extends Falloff {
        public float getRaw(float x) {
            return (float)(-Math.pow(1/20d * x, 3) + 1) / (1/15 * x + 1);
        }
    }
    static class Gravity extends Falloff {
        public float getRaw(float x) {
            return 1;
        }
    }
    static class Initial extends Falloff {
    	public float getRaw(float x) {
    		return -1/300f * x + 1;
    	}
    }
}