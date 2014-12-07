package levels.human;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class Bartender extends MovementImage {
	
	private static final Texture _bartender = new Texture(HumanAssets.Bartender);
	private static final Sound _moveSound = getSound(HumanAssets.BartenderMove);
	
	int _defaultX;
	
	public Bartender(int height) {
		super(_bartender, height, 0);
		speed = -300;
	}
	
	float _serveTime = 0;
	
	public void update(float dt) {
		flipImage = _serveTime > 0;	
		_serveTime -= dt;
	}
	
	public void serve() {
		_serveTime = 0.10f;	
		x = _defaultX;
	}

	public void move(int yPosition) {
		final int offset = 3;
		
		if (y == yPosition - offset) return;
		x = _defaultX;
		y = yPosition - offset;	
		_moveSound.play();
	}

	@Override
	protected boolean checkX() {
		return false;
	}

	public void setRight(int xPosition) {
		x = _defaultX = xPosition - width;
	}

	public void walk(float dt) {
		x += (speed*dt);	
		if (x < 20) {
			x = 20;
		}
	}
}
