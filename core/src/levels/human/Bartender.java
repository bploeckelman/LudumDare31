package levels.human;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class Bartender extends MovementImage {
	
	private static final Texture _bartender = new Texture(HumanAssets.Bartender);
	private static final Sound _moveSound = getSound(HumanAssets.BartenderMove);
	
	public Bartender(int height) {
		super(_bartender, height, 0);
	}
	
	float _serveTime = 0;
	
	public void update(float dt) {
		flipImage = _serveTime > 0;	
		_serveTime -= dt;
	}
	
	public void serve() {
		_serveTime = 0.10f;		
	}

	public void move(int yPosition) {
		if (y == yPosition) return;
		y = yPosition;	
		_moveSound.play();
	}

	@Override
	protected boolean checkX() {
		// TODO Auto-generated method stub
		return false;
	}
}
