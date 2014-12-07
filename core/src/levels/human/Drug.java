package levels.human;

import com.badlogic.gdx.graphics.Texture;

public class Drug extends MovementImage {

	private static Texture _pills = new Texture(HumanAssets.Pills);
	
	private float _duration = 5f;
	
	public Drug(int height) {
		super(_pills, height, 0);
		
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
