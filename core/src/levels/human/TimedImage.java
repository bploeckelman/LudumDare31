package levels.human;

import com.badlogic.gdx.graphics.Texture;

public class TimedImage extends MovementImage {

	public Object tag;
	
	private float _duration = 5f;
	
	public TimedImage(Texture image, int height) {
		super(image, height, 0);
		
		speed = 0;
	}
	
	@Override
	public void update(float dt) {
		_duration -= dt;
		if (_duration < 0) {
			remove = true;
		}
	}
}
