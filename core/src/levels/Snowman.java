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
	
	private String nextText = "Wait, it wasn't real.\n\nLike a fantastic dream... but\nmaybe one day you can own a\nbar in Wisconsin\n\nEat your vegetables and don't do drugs, kids";
	public Snowman() {
		tutorialText = "You allowed the galaxies to collide and your galaxy has been destroyed!\n\nAre you sure you are from Wisconsin?\n\nWhat about the patrons?";
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
	boolean altMessage = false;
	
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
		} else if (!altMessage) {
			tutorialText = nextText;
			altMessage = true;
			_tutorialTime = 10;
		}
	}

	@Override
	public void draw(SpriteBatch batch) {
		batch.draw(Assets.squareTex, 0, 0, GameConstants.GameWidth, GameConstants.GameHeight);
		_mainImage.draw(batch);
		_occulousImage.draw(batch);
	}
}
