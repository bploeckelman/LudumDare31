package levels.human;

import java.util.ArrayList;

import lando.systems.ld31.Assets;

import com.badlogic.gdx.graphics.Texture;

public class Patron extends MovementImage {
	
	public static int maxX;
	
	private Glass _glass;
	private float _glassTime = 1.5f;
	
	public Patron(Texture image, int height, int x, int y) {
		super(image, height, 0f);
		
		this.x = x;
		this.y = y;
	}
	
	@Override 
	public void update(float dt) {
		super.update(dt);
		
		if (_glass != null) {
			_glassTime -= dt;
			_glass.x = x + width - 3;
		}
	}
	
	@Override
	protected boolean checkX() {
		return x > (maxX - width) || checkGlass();
	}
	
	private boolean checkGlass() {
		if (_glass == null) return false;
		boolean remove = false;
		if (x > -width) {
			// throw glass back
			if (_glassTime < 0) {
				_glass.drink();
				remove = true;
			}
		} else {
			_glass.remove = true;
			Assets.score += 200;
			remove = true;
		}
		
		return remove;
	}

	public void check(ArrayList<Glass> _glasses) {
		if (_glass != null) return;
		
		for (int i = 0; i < _glasses.size(); i++) {
			Glass glass = _glasses.get(i);
			
			if (glass.isFull && level == glass.level && glass.shouldUpdate && glass.x <= x + width) {
				glass.shouldUpdate = false;
				_glass = glass;
				speed = _glass.speed / 2;
			}
		}
	}
}
