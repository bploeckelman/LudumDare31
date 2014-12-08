package levels.human;

import java.util.ArrayList;

import lando.systems.ld31.Assets;
import lando.systems.ld31.LevelManager;
import lando.systems.ld31.Score;
import lando.systems.ld31.SoundManager;
import lando.systems.ld31.ThreatLevel;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class Patron extends MovementImage {
	
	public enum PatronType { none, nurse, exterminator };
	
	private static Texture _vomit = new Texture(HumanAssets.Puke);
	private static Texture _puddle = new Texture(HumanAssets.PukePuddle);
	private static Texture _nurseHat = new Texture(HumanAssets.NurseHat);
	private static Texture _bugHat = new Texture(HumanAssets.Orkin);
	
	private static final Texture _pills = new Texture(HumanAssets.Pills);
	private static final Texture _bugspray = new Texture(HumanAssets.Off);
	
	public static int maxX;
	
	public PatronType patronType = PatronType.none;
	public boolean hasDrop;
	private ScaleImage _hat;
	
	private Glass _glass;	
	

	float _glassY;
	private float _glassTime = 1.5f;
	private float _pukeTime = 0;
	
	
	public Patron(Texture image, int height, int x, int y) {
		super(image, height, 0f);
		
		this.x = x;
		this.y = y;
	}
	
	public void setPatronType(PatronType type) {
		patronType = type;
		switch (type) {
			case nurse:
				_hat = new ScaleImage(_nurseHat, 20);
				break;
			case exterminator:
				_hat = new ScaleImage(_bugHat, 20);
				break;
			default:
				_hat = null;
				break;
		}
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
			ThreatLevel.addThreat(LevelManager.Levels.Human,  10);
			flipImage = hasPuked = true;
			_pukeTime = 1f;
			Score.PukingPatrons++;
			shouldUpdate = false;
			puked = true;
			
			SoundManager.play(LevelManager.Levels.Human,  HumanAssets.Sounds.Puke);
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
				
				hasDrop = (patronType != PatronType.none) && Assets.rand.nextInt(10) < 7;
				
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
		
		if (_hat != null) {
			_hat.x = x + ((flipImage) ? 15 : 25);
			_hat.y = y + 70;
			_hat.flipImage = flipImage;
			_hat.draw(batch);
		}
		
		if (hasPuked) {
			if ((_pukeTime - 0.7f) > 0) {		
				batch.draw(_vomit, x + width/2, y - 70);
			} else {
				batch.draw(_puddle,  x,  y - 63);
			}
		}
	}

	public Texture getDrop() {
		Texture dropImage = null;
		
		if (hasDrop) {
			switch (patronType) {
				case exterminator:
					dropImage = _bugspray;
					break;
				case nurse:
					dropImage = _pills;
					break;
				default:
					dropImage = null;
					break;
			}
		}
		
		return dropImage;
	}
}
