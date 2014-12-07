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
	ArrayList<Drug> _drugs = new ArrayList<Drug>(5);
	
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
		_bartender.setRight(GameConstants.GameWidth - Tapper.width);
		
		int y = 25;
		
		for (int i = 0; i < BarCount; i++) {
			_barlocation[i] = y;
			
			y += _levelHeight;
			
			_tappers[i] = new Tapper();
			_tappers[i].position = new Vector2(tapperX, y - Tapper.height);
		}
		
		_patronManager = new PatronManager(_barlocation);
				
		_bartender.y = _barlocation[_bartender.level];
	}
	
	@Override
	public int hasThreat() {
		return ThreatLevel.getThreatLevel(HumanLevel.Title);
	}

	@Override
	public void handleInput(float dt) {
		int level = _bartender.level;
		
		if (isUpJustPressed()) {
			level++;
		}
		else if (isDownJustPressed()) {
			level--;
		}
		
		level = (int)MathUtils.clamp(level, 0, BarCount-1);
	
		if (level != _bartender.level) {
			_bartender.level = level;
			_bartender.move(_barlocation[level]);
		}	
		
		serveBeer(isRightJustPressed(), dt);
		
		if (isLeftPressed()) {
			_bartender.walk(dt);
		}
	}
	
	private float _serveTime = 0;
	private void serveBeer(boolean serve, float dt) {
		_serveTime -= dt;
		
		if (serve && _serveTime < 0) {
			_bartender.serve();
			_tappers[_bartender.level].serve();
			
			Glass glass = new Glass(GlassHeight, _barTexture.getWidth(), 
					_barlocation[_bartender.level] + _barTexture.getHeight(), ServeTime);
			glass.level = _bartender.level;
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
		
		for (int i = _drugs.size() - 1; i >= 0; i--) {
			Drug drug = _drugs.get(i);
			drug.update(dt);
			if (_bartender.intersects(drug)) {
				_drugs.remove(drug);
				Score.Total += 100;
				Score.DrugsCollected++;
				
				ThreatLevel.reset(HumanLevel.Title);
			}
		}
		
		_patronManager.update(_glasses, _drugs, dt);
	}
	
	private boolean catchGlass(Glass glass) {
		return !glass.isFull && _bartender.intersects(glass);
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
		
		for (int i = 0; i < _drugs.size(); i++) {
			_drugs.get(i).draw(batch);
		}
		
		_bartender.draw(batch);
	}
}