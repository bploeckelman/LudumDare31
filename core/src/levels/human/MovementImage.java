package levels.human;

import lando.systems.ld31.GameObject;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class MovementImage extends GameObject {

	public boolean remove;
	public int level;
	
	public int speed = 100;
	
	public int x;
	public int y;
	
	public int width;
	public int height;

	public boolean shouldUpdate = true;
	
	protected boolean flipImage = false;
	
	Texture _image;
	float _invisibleTime;
	
	public MovementImage(Texture image, int height, float time) {
		_image = image;		
		setHeight(height);
		
		_invisibleTime = time;
	}
	
	public void setHeight(int height) {
		this.height = height;
		width = (int)((float)height/_image.getHeight()*_image.getWidth());
	}
	
	protected Texture getImage() {
		return _image;
	}
	
	public void update(float dt) {
		_invisibleTime -= dt;
		if (_invisibleTime > 0 || !shouldUpdate) return;		
		
		x += speed*dt;
		if (checkX()) {
			remove = true;
		}
	}
	
	protected abstract boolean checkX();
	
	public void draw(SpriteBatch batch) {
		if (remove || _invisibleTime > 0) return;
		
		int drawX = x;
		int w = width;
		if (flipImage) {
			drawX += width;
			w = -width;
		}
		batch.draw(getImage(),  drawX,  y,  w,  height);
	}
}
