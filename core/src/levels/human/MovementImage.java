package levels.human;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class MovementImage {

	public boolean remove;
	
	private Texture _image;
	
	public int speed = 100;
	
	public int x;
	public int y;
	
	public int width;
	public int height;
	
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
		if (_invisibleTime > 0) return;		
		
		x += speed*dt;
		if (checkX()) {
			remove = true;
		}
	}
	
	protected abstract boolean checkX();
	
	public void draw(SpriteBatch batch) {
		if (remove || _invisibleTime > 0) return;
		
		batch.draw(getImage(),  x,  y,  width,  height);
	}
}
