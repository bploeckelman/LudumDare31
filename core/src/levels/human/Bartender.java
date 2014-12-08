package levels.human;

import lando.systems.ld31.LevelManager;
import lando.systems.ld31.SoundManager;

import com.badlogic.gdx.graphics.Texture;

public class Bartender extends MovementImage {
	
	private static final Texture _bartender = new Texture(HumanAssets.Bartender);
	
	float _defaultX;
	
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
		SoundManager.play(LevelManager.Levels.Human, HumanAssets.Sounds.BartenderMove);
	}

	public void setRight(float xPosition) {
		x = _defaultX = xPosition - width;
	}

	public void walk(float dt) {
		x += (speed*dt);	
		if (x < 20) {
			x = 20;
		}
	}

	public boolean intersects(MovementImage image) {

		float bartenderRight = x + width/2;
		float imageRight = image.x + image.width;
				
		return (image.level == level && imageRight >= x && image.x <= bartenderRight);
	}
}
