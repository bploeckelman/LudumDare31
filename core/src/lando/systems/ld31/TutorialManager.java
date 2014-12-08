package lando.systems.ld31;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;

public class TutorialManager {

	float _hPad = 144;
	float _vPad = 108;
	float _thickness = 4;
	
	public void draw(SpriteBatch batch, String tutorialText) {
		if (tutorialText != null) {
			float width = GameConstants.ScreenWidth- (_hPad*2);
			float height = GameConstants.GameHeight - (_vPad*2);
						
			Box.draw(batch,  _hPad,  _vPad,  width,  height,  Color.BLACK);
			Assets.gameFont.drawWrapped(batch, tutorialText, 
					_hPad, GameConstants.GameHeight - (_vPad + _thickness + 18),
					width, HAlignment.CENTER);	
		}
	}

	public boolean touchUp(int screenX, int screenY, int button) {
		return (screenX >= _hPad) && (screenX <= (GameConstants.ScreenWidth - _hPad)) 
				&& (screenY >= _vPad) && (screenY <= (GameConstants.ScreenHeight - _vPad));
		
	}
}
