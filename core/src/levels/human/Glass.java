package levels.human;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Glass extends MovementImage {
	
	private final int _initialSpeed = -300;
	
	private static Texture _fullGfx = new Texture(HumanAssets.FullGfx);
	private static Texture _emptyGfx = new Texture(HumanAssets.EmptyGfx);
	
	public boolean isFull = true;
	
	public Glass(int height, int barEnd, int barTop, float time) {
		super(_fullGfx, height, time);
		
		x = barEnd - width;
		y = barTop;
		
		speed = _initialSpeed;
	}
	
	@Override
	protected Texture getImage() {
		return (isFull) ? super.getImage() : _emptyGfx;
	}

	@Override
	protected boolean checkX() {
		return x < -width;
	}

	public void drink() {
		speed = (int)(_initialSpeed* -1.3);
		isFull = false;
		shouldUpdate = true;		
	}
}
