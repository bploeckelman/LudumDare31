package levels.human;

import java.util.ArrayList;

import lando.systems.ld31.Score;
import lando.systems.ld31.ThreatLevel;
import levels.HumanLevel;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Patron extends MovementImage {
	
	private static Texture _vomit = new Texture(HumanAssets.Puke);
	private static Texture _puddle = new Texture(HumanAssets.PukePuddle);
	private static Texture _hat = new Texture(HumanAssets.NurseHat);
	private static Sound _vomitSound = getSound(HumanAssets.PukeSound);
	
	public static int maxX;
	
	public boolean isNurse;
	private ScaleImage _nurseHat;
	
	private Glass _glass;
	float _glassY;
	private float _glassTime = 1.5f;
	private float _pukeTime = 0;
	
	
	public Patron(Texture image, int height, int x, int y) {
		super(image, height, 0f);
		
		this.x = x;
		this.y = y;
		
		_nurseHat = new ScaleImage(_hat, 20);
	}
	
	@Override 
	public void update(float dt) {
		super.update(dt);
		
		if (_pukeTime > 0) {
			_pukeTime -= dt;
			shouldUpdate = _pukeTime < 0;
		}
		
		if (_glass != null) {
			_glassTime -= dt;
			_glass.x = x + width - (_glass.y - _glassY);
			_glass.y += 15 *dt;
			_glass.rotation += 90 * dt/1.5f;
		}
	}
	
	@Override
	protected boolean checkX() {
		
		boolean isMax = x > (maxX - width);
		
		if (isMax && puke()) {
			return false;			
		}
		
		return isMax || checkGlass();
	}
	
	public boolean hasPuked;
	
	private boolean puke() {
		boolean puked = false;
		
		if (!hasPuked) {
			ThreatLevel.addThreat(HumanLevel.Title,  10);
			flipImage = hasPuked = true;
			_pukeTime = 1f;
			Score.PukingPatrons++;
			shouldUpdate = false;
			puked = true;
			
			if (HumanLevel.hasFocus) {
				_vomitSound.play();
			}
		}
		
		return puked;
	}
	
	private boolean checkGlass() {
		if (_glass == null) return false;
		boolean remove = false;
		if (x > -width) {
			// throw glass back
			if (_glassTime < 0) {
				_glass.rotation = 0;
				_glass.y = _glassY;
				_glass.drink();
				remove = true;
			}
		} else {
			_glass.remove = true;
			Score.Total += 200;
			remove = true;
		}
		
		return remove;
	}

	public void check(ArrayList<Glass> _glasses) {
		if (_glass != null || hasPuked) return;
		
		for (int i = 0; i < _glasses.size(); i++) {
			Glass glass = _glasses.get(i);
			
			if (glass.isFull && level == glass.level && glass.shouldUpdate && glass.x <= x + width) {
				glass.shouldUpdate = false;
				_glass = glass;
				_glassY = glass.y;
				speed = _glass.speed / 2;
			}
		}
	}
	
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		
		if (isNurse) {
			_nurseHat.x = x + 5;
			_nurseHat.y = y + 70;
			_nurseHat.flipImage = flipImage;
			_nurseHat.draw(batch);
		}
		
		if (hasPuked) {
			if ((_pukeTime - 0.7f) > 0) {		
				batch.draw(_vomit, x + width/2, y - 60);
			} else {
				batch.draw(_puddle,  x,  y - 63);
			}
		}
	}
}
