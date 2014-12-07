package levels.human;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Glass {
	
	private static Texture _fullGfx = new Texture(HumanAssets.FullGfx);
	private static Texture _emptyGfx = new Texture(HumanAssets.EmptyGfx);
	
	public boolean isFull = true;
	public boolean remove;
	
	public int speed = 100;
	
	public int x;
	public int y;
	
	public int width;
	public int height;
	
	float _invisibleTime;
	
	public Glass(int height, int barEnd, int barTop, float time) {
		setHeight(height);
		
		x = barEnd - width;
		y = barTop;
		
		_invisibleTime = time;
	}
	
	public void setHeight(int height) {
		this.height = height;
		width = (int)((float)height/_fullGfx.getHeight()*_fullGfx.getWidth());
	}
	
	public void update(float dt) {
		_invisibleTime -= dt;
		if (_invisibleTime > 0) return;		
		
		x -= speed*dt;
		if (x < -width) {
			remove = true;
		}
	}
	
	public void draw(SpriteBatch batch) {
		if (remove || _invisibleTime > 0) return;
		
		batch.draw((isFull) ? _fullGfx : _emptyGfx,  x,  y,  width,  height);
	}
}
