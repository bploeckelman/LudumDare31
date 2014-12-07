package levels.human;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;


public class Tapper {

	private static final Sound _pourSound = Gdx.audio.newSound(Gdx.files.internal(HumanAssets.PourSound));

	private static final Texture _tapUp = new Texture(HumanAssets.TapperUp);
	private static final Texture _tapDown = new Texture(HumanAssets.TapperDown);
	
	public Vector2 position;
	
	public static final int width = _tapUp.getWidth();
	public static final int height = _tapUp.getHeight();
	
	public void draw(SpriteBatch batch) {
		batch.draw((_serveTime < 0) ? _tapUp : _tapDown ,  position.x, position.y);
	}

	float _serveTime = 0;
	
	public void update(float dt) {
		_serveTime -= dt;	
	}
	
	public void serve() {
		_serveTime = 1f;
		_pourSound.play();
	}
}
