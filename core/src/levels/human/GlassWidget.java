package levels.human;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import lando.systems.ld31.Assets;
import lando.systems.ld31.Box;
import lando.systems.ld31.GameConstants;
import lando.systems.ld31.GameObject;

public class GlassWidget extends GameObject {

	private ScaleImage _image;
	private Integer _glassCount = -1;
	private String _text;
	
	public float x = GameConstants.GameWidth - 160;
	public float y = GameConstants.GameHeight - 70;
	
	public GlassWidget() {
		_image = new ScaleImage(Glass.EmptyGfx, 40);
	}
	
	public void update(int glassCount, float dt) {
		if (_text == null || glassCount != _glassCount) {
			_glassCount = glassCount;
			_text = _glassCount.toString();
		}
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		if (_text == null) return;
		
		Box.draw(batch,  x,  y,  140,  60, Color.BLACK);
		_image.x = x + 10;
		_image.y = y + 10;
		_image.draw(batch);
		Assets.gameFont.draw(batch, _text, x + _image.width + 30, y + 45);
	}
}
