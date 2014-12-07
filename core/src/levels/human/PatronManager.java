package levels.human;

import java.util.ArrayList;

import lando.systems.ld31.Assets;
import levels.human.Patron.PatronType;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PatronManager {
	
	private Texture[] _patronGfx = new Texture[] {
			new Texture(HumanAssets.Patron1),
			new Texture(HumanAssets.Patron2),
			new Texture(HumanAssets.Patron3)			
	};
	
	ArrayList<Patron> _patrons = new ArrayList<Patron>(15);
	
	int[] _barlocations;
	float _patronAddTime = 1f;
	float _addReset = 3f;
	
	public PatronManager(int[] barlocations) {
		_barlocations = barlocations;
		reset();
	}
	
	public void reset() {
		_patronAddTime = 1f;
		_addReset = 3f;
		_patrons.clear();
	}
	
	public void update(ArrayList<Glass> glasses, ArrayList<TimedImage> items, float dt) {
		_patronAddTime -= dt;
			
		for (int i = 0; i < _patrons.size(); i++) {
			_patrons.get(i).check(glasses);
		}
	
		// update
		for (int i = _patrons.size() - 1; i >= 0; i--) {
			Patron patron = _patrons.get(i);
			patron.update(dt);
			if (patron.remove) {
				Texture drop = patron.getDrop();
				
				if (drop != null && items.size() < 4) {
					TimedImage item = new TimedImage(drop, 40);
					item.tag = patron.patronType;
					item.level = patron.level;
					item.x = patron.x;
					item.y = patron.y - 4;
					
					items.add(item);
				}
				
				_patrons.remove(patron);
			}
		}
		
		// add
		if (_patronAddTime < 0) {
			if (_addReset > 0.5f) { 
				_addReset -= 0.2f;
			}
			
			_patronAddTime = _addReset;
			
			int barLevel = Assets.rand.nextInt(_barlocations.length);
			int y = _barlocations[barLevel] + 60;		
			Patron patron = createPatron(y);			
			patron.level = barLevel;
			_patrons.add(patron);			
		}		
	}
	
	private Patron createPatron(int y) {
		Texture image = _patronGfx[Assets.rand.nextInt(_patronGfx.length)];	
		Patron patron = new Patron(image, 90, -image.getWidth(), y);
		patron.speed = 200 + Assets.rand.nextInt(200);
		
		if (Assets.rand.nextDouble() < 0.15) {
			patron.setPatronType(Assets.rand.nextBoolean() ? Patron.PatronType.nurse : PatronType.exterminator);
		}

		return patron;
	}

	public void draw(SpriteBatch batch) {
		for (int i = 0; i < _patrons.size(); i++) {
			_patrons.get(i).draw(batch);
		}
	}
}