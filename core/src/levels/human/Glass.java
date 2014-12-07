package levels.human;

import lando.systems.ld31.GameConstants;
import lando.systems.ld31.Score;
import lando.systems.ld31.ThreatLevel;
import levels.HumanLevel;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class Glass extends MovementImage {
	
	private final int _initialSpeed = -300;
	
	private static Texture _fullGfx = new Texture(HumanAssets.FullGfx);
	private static Texture _emptyGfx = new Texture(HumanAssets.EmptyGfx);
	private static Sound _crashSound = getSound(HumanAssets.CrashSound);
	
	public boolean isFull = true;
	
	public Glass(int height, int barEnd, int barTop, float time) {
		super(_fullGfx, height, time);
		
		x = barEnd - width;
		y = barTop;
		
		speed = _initialSpeed;
	}
	
	@Override
	protected Texture getImage() {
		return (isFull) ? super.getImage() : _emptyGfx;
	}

	@Override
	protected boolean checkX() {		
		boolean crash = (isFull) ? x < -width : x > GameConstants.GameWidth - width;
		
		if (crash) {
			if (HumanLevel.hasFocus) { 
				_crashSound.play();
			}
			
			Score.BrokenGlasses++;
			if (!isFull) {
				Score.MissedGlasses++;
			}
			
			ThreatLevel.addThreat(HumanLevel.Title, 5);
		}
		
		return crash;
	}

	public void drink() {
		speed = (int)(_initialSpeed* -1.3);
		isFull = false;
		shouldUpdate = true;		
	}
}
