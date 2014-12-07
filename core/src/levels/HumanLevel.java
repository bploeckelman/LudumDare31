package levels;

import java.util.ArrayList;

import lando.systems.ld31.GameConstants;
import lando.systems.ld31.Score;
import lando.systems.ld31.ThreatLevel;
import levels.human.*;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class HumanLevel extends GameLevel {

	public static final String Title = "HumanLevel";
	public static boolean hasFocus;
	
	public int GlassHeight = 36;
	
	// time a glass is invisible from being served
	public float ServeTime = 0.2f;
	
	final int BarCount = 4;
	
	Texture _barTexture;
	
	Tapper[] _tappers;
	int _levelHeight;
	int [] _barlocation;
	
	ArrayList<Glass> _glasses = new ArrayList<Glass>(15);
	
	int _bartenderLevel = 0;

	Bartender _bartender;
	PatronManager _patronManager;
	
	public HumanLevel()
	{
		_barTexture = new Texture(HumanAssets.Bar);
		
		Patron.maxX = _barTexture.getWidth();
	
		_barlocation = new int[BarCount];
		_tappers = new Tapper[BarCount];
				
		_levelHeight = (int)((GameConstants.ScreenHeight*.85) / BarCount);
		int tapperX = GameConstants.GameWidth - Tapper.width;
		
		_bartender = new Bartender(_levelHeight - 20);
		_bartender.x = GameConstants.GameWidth - Tapper.width - _bartender.width;;
		
		int y = 25;
		
		for (int i = 0; i < BarCount; i++) {
			_barlocation[i] = y;
			
			y += _levelHeight;
			
			_tappers[i] = new Tapper();
			_tappers[i].position = new Vector2(tapperX, y - Tapper.height);
		}
		
		_patronManager = new PatronManager(_barlocation);
				
		_bartender.y = _barlocation[_bartenderLevel];
	}
	
	@Override
	public int hasThreat() {
		return ThreatLevel.getThreatLevel(HumanLevel.Title);
	}

	@Override
	public void handleInput(float dt) {
		if (isUpJustPressed()) {
			_bartenderLevel++;
		}
		else if (isDownJustPressed()) {
			_bartenderLevel--;
		}
		
		_bartenderLevel = (int)MathUtils.clamp(_bartenderLevel, 0, BarCount-1);
		_bartender.move(_barlocation[_bartenderLevel]);
		
		serveBeer(isRightJustPressed(), dt);
	}
	
	private float _serveTime = 0;
	private void serveBeer(boolean serve, float dt) {
		_serveTime -= dt;
		
		if (serve && _serveTime < 0) {
			_bartender.serve();
			_tappers[_bartenderLevel].serve();
			
			Glass glass = new Glass(GlassHeight, _barTexture.getWidth(), 
					_barlocation[_bartenderLevel] + _barTexture.getHeight(), ServeTime);
			glass.level = _bartenderLevel;
			_glasses.add(glass);
			
			_serveTime  = ServeTime;
		}
	}

	@Override
	public void update(float dt) {
		hasFocus = top;
		
		_bartender.update(dt);
		for (int i = 0; i < BarCount; i++) {
			_tappers[i].update(dt);
		}
		
		for (int i = _glasses.size() - 1; i >= 0; i--) {
			Glass glass = _glasses.get(i);
			glass.update(dt);
			if (catchGlass(glass)) {
				Score.Total += 100;
				glass.remove = true;
			}
			
			if (glass.remove) {
				_glasses.remove(glass);
			}
		}
		
		_patronManager.update(_glasses, dt);
	}
	
	private boolean catchGlass(Glass glass) {
		if (glass.isFull || glass.level != _bartenderLevel) return false;
		
		int bartenderRight = _bartender.x + _bartender.width / 2;
		int glassRight = glass.x + glass.width;
				
		return (glassRight >= _bartender.x && glass.x <= bartenderRight);
	}
		
	@Override
	public void draw(SpriteBatch batch) {
		_patronManager.draw(batch);
		
		for (int i = 0; i < BarCount; i++) {
			batch.draw(_barTexture, 0, _barlocation[i]);
			_tappers[i].draw(batch);
		}
		
		for (int i = 0; i < _glasses.size(); i++) {
			_glasses.get(i).draw(batch);
		}		
		
		_bartender.draw(batch);
	}
}