package levels.human;

import lando.systems.ld31.GameConstants;
import lando.systems.ld31.LevelManager;
import lando.systems.ld31.Score;
import lando.systems.ld31.SoundManager;
import lando.systems.ld31.ThreatLevel;
import com.badlogic.gdx.graphics.Texture;

public class Glass extends MovementImage {
	
	private final int _initialSpeed = -300;
	
	public static final Texture FulllGfx = new Texture(HumanAssets.FullGfx);
	public static final Texture EmptyGfx = new Texture(HumanAssets.EmptyGfx);
	
	public boolean isFull = true;
	
	public Glass(int height, int barEnd, int barTop, float time) {
		super(FulllGfx, height, time);
		
		x = barEnd - width;
		y = barTop;
		
		speed = _initialSpeed;
	}
	
	@Override
	protected Texture getImage() {
		return (isFull) ? super.getImage() : EmptyGfx;
	}

	@Override
	protected boolean checkX() {		
		boolean crash = (isFull) ? x < -width : x > GameConstants.GameWidth - width;
		
		if (crash) {
			SoundManager.play(LevelManager.Levels.Human, HumanAssets.Sounds.Crash);
			
			Score.BrokenGlasses++;
			if (!isFull) {
				Score.MissedGlasses++;
			}
		}
		
		return crash;
	}

	public void drink() {
		speed = (int)(_initialSpeed* -1.3);
		isFull = false;
		shouldUpdate = true;		
	}
}
