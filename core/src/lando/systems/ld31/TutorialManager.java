package lando.systems.ld31;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;

public class TutorialManager {

	float _hPad = 144;
	float _vPad = 108;
	float _thickness = 4;
	
	public void draw(SpriteBatch batch, String tutorialText) {
		if (tutorialText != null) {
			drawBox(batch, _hPad, _vPad, _thickness, tutorialText);
		}
	}

	protected void drawBox(SpriteBatch batch, float hPad, float vPad, float thickness, String text) {
		
		float width = GameConstants.ScreenWidth- (hPad*2);
		float height = GameConstants.GameHeight - (vPad*2);
		
		batch.draw(Assets.squareTex, hPad, vPad, width, height);
		batch.setColor(0, 0, 0, 1);
		
		hPad += thickness;
		vPad += thickness;
		
		width -= (thickness*2);
		height -= (thickness*2);
		
		batch.draw(Assets.squareTex, hPad, vPad, width, height);
		batch.setColor(1, 1, 1, 1);
		
		hPad += thickness;
		vPad += (thickness * 4);
		width -= (thickness*2);
		Assets.gameFont.drawWrapped(batch, text, hPad, GameConstants.GameHeight - vPad, width, HAlignment.CENTER);	
	}

	public boolean touchUp(int screenX, int screenY, int button) {
		return (screenX >= _hPad) && (screenX <= (GameConstants.ScreenWidth - _hPad)) 
				&& (screenY >= _vPad) && (screenY <= (GameConstants.ScreenHeight - _vPad));
		
	}
}
