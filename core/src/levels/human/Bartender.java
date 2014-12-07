package levels.human;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Bartender {
	
	private static final Texture _bartender = new Texture(HumanAssets.Bartender);
	private static final Sound _moveSound = Gdx.audio.newSound(Gdx.files.internal(HumanAssets.BartenderMove));
	
	public int x;
	public int y;
	
	public int width;
	public int height;
	
	public void setHeight(int height) {
		this.height = height;
		width = (int)((float)height/_bartender.getHeight()*_bartender.getWidth());
	}
	
	public void draw(SpriteBatch batch) {
		int w = (_serveTime > 0) ? -width : width;
		int x = (_serveTime > 0) ? this.x : this.x - width;		
		
		batch.draw(_bartender, x, y, w, height);
	}

	float _serveTime = 0;
	
	public void update(float dt) {
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
}
