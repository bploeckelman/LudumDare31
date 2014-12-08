package levels;

import lando.systems.ld31.Assets;
import lando.systems.ld31.GameConstants;
import levels.human.ScaleImage;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Snowman extends GameLevel {

	private static Texture _snowman = new Texture("snowman.png");
	private static Texture _occulous = new Texture("occulous.png");
	
	private ScaleImage _mainImage;
	private ScaleImage _occulousImage;
	
	public Snowman() {
		tutorialText = "Looks like our home was destroyed by another galaxy.  Good thing this was just a game.";
		_mainImage = new ScaleImage(_snowman, GameConstants.GameHeight);
		_mainImage.y = -600;
		
		_occulousImage = new ScaleImage(_occulous, 400);
		_occulousImage.x = 100;
		_occulousImage.y = -225;
	}
		
	@Override
	public int hasThreat() {
		return 0;
	}

	@Override
	public void handleInput(float dt) {
		// TODO Auto-generated method stub
		
	}
	
	float _distance = 400;
	float _rotation = 1f;
	@Override
	public void update(float dt) {
		if (!top) return;
		
		float dy = dt * 100;
		
		_distance -= dy;
		if (_distance > 0) {		
			_mainImage.y += dy;
			_occulousImage.y += dy;
		} else if (_distance < -200 && _distance > -800) {
			_occulousImage.y += dy* 2f;
			_occulousImage.x += dy;
			_occulousImage.rotation -= _rotation;
			_rotation += 0.08f;
		}
	}

	@Override
	public void draw(SpriteBatch batch) {
		batch.draw(Assets.squareTex, 0, 0, GameConstants.GameWidth, GameConstants.GameHeight);
		_mainImage.draw(batch);
		_occulousImage.draw(batch);
	}
}
