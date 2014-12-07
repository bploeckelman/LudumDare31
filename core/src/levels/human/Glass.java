package levels.human;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Glass extends MovementImage {
	
	private static Texture _fullGfx = new Texture(HumanAssets.FullGfx);
	private static Texture _emptyGfx = new Texture(HumanAssets.EmptyGfx);
	
	public boolean isFull = true;
	
	public Glass(int height, int barEnd, int barTop, float time) {
		super(_fullGfx, height, time);
		
		x = barEnd - width;
		y = barTop;
		
		speed = -300;
	}
	
	@Override
	protected Texture getImage() {
		return (isFull) ? super.getImage() : _emptyGfx;
	}

	@Override
	protected boolean checkX() {
		return x < -width;
	}
}
