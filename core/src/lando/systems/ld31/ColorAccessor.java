package lando.systems.ld31;

import com.badlogic.gdx.graphics.Color;

import aurelienribon.tweenengine.TweenAccessor;

public class ColorAccessor implements TweenAccessor<Color> {

    public static final int COLOR = 1;
	public static final int COLOR_A = 2;
    
	
	@Override
	public int getValues(Color target, int tweenType, float[] returnValues) {
		switch(tweenType){
		case COLOR:
			returnValues[0] = target.r;
			returnValues[1] = target.g;
			returnValues[2] = target.b;
			returnValues[3] = target.a;
			return 4;
		case COLOR_A:
			returnValues[0] = target.a; return 1;
		default: assert false; return -1;
		}
	}

	@Override
	public void setValues(Color target, int tweenType, float[] newValues) {
        switch (tweenType) {
        case COLOR:
        	target.set(newValues[0], newValues[1], newValues[2], newValues[3]);
//        	System.out.println("ALPHA: " + newValues[3]);
        	break;
        case COLOR_A:
        	target.a = newValues[0];
        	break;
        	
        default: assert false; break;
        }
		
	}

}
