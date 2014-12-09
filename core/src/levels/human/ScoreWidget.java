package levels.human;

import lando.systems.ld31.Assets;
import lando.systems.ld31.Box;
import lando.systems.ld31.GameConstants;
import lando.systems.ld31.GameObject;
import lando.systems.ld31.Score;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ScoreWidget extends GameObject {

	private Integer _score;
	private String _text;
	
	public float x = 20;
	public float y = GameConstants.GameHeight - 70;
	
	public void update(float dt) {
		if (_text == null || _score != Score.CashMoneyYo) {
			_score = Score.CashMoneyYo;
			_text = "$" + _score;
		}
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		if (_text == null) return;
		
		Box.draw(batch,  x,  y,  300,  60, Color.BLACK);
		Assets.gameFont.drawWrapped(batch, _text, x + 10, y + 45, 280, HAlignment.RIGHT);
	}
}
