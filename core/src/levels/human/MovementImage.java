package levels.human;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class MovementImage extends ScaleImage {

	public boolean remove;
	public int level;
	
	public int speed = 100;
	
	public boolean shouldUpdate = true;
	
	float _invisibleTime;
	
	public MovementImage(Texture image, int height, float time) {
		super(image, height);
		
		_invisibleTime = time;
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
	
	@Override
	public void draw(SpriteBatch batch) {
		if (remove || _invisibleTime > 0) return;
		super.draw(batch);
	}
}
