package levels;

import java.util.ArrayList;

import lando.systems.ld31.Assets;
import lando.systems.ld31.ColorAccessor;
import lando.systems.ld31.GameConstants;
import lando.systems.ld31.LevelManager;
import lando.systems.ld31.LudumDare31;
import lando.systems.ld31.Score;
import lando.systems.ld31.SoundManager;
import lando.systems.ld31.ThreatLevel;
import levels.human.*;
import levels.human.Patron.PatronType;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class HumanLevel extends GameLevel {
	
	public int GlassHeight = 36;
	
	// time a glass is invisible from being served
	public float ServeTime = 0.2f;
	
	final int BarCount = 4;
	
	Texture _barTexture;
	
	Tapper[] _tappers;
	int _levelHeight;
	int [] _barlocation;
	
	ArrayList<Glass> _glasses = new ArrayList<Glass>(15);
	ArrayList<TimedImage> _items = new ArrayList<TimedImage>(5);
	
	Bartender _bartender;
	PatronManager _patronManager;
	
	int _glassCount = 20;
	GlassWidget _glassWidget = new GlassWidget();
	ScoreWidget _cashWidget = new ScoreWidget();
	
	public HumanLevel()
	{
		tutorialText = "Living the dream. Keep it clean and keep them coming.\n\n\nMove: Up, Down, Left\nServe: Right";
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
		return ThreatLevel.getThreatLevel(LevelManager.Levels.Human);
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
		
		if (Gdx.input.isKeyJustPressed(Keys.F)){
			powerFailure();
		}
	}
	
	private Color _powerColor = new Color(0, 0, 0, 0);
		
	public void powerFailure() {
		_powerColor = new Color(0, 0, 0, 1);
		Tween.to(_powerColor, ColorAccessor.COLOR_A, 2f)      
	        .target(0, 0, 0, 0)
	        .ease(TweenEquations.easeOutBounce)
	        .setCallbackTriggers(TweenCallback.END)
	        .start(LudumDare31.tweens);
	}
	
	private float _serveTime = 0;
	private void serveBeer(boolean serve, float dt) {
		_serveTime -= dt;
		
		if (serve && _serveTime < 0 && _glassCount > 0) {
			_bartender.serve();
			_glassCount--;
			
			_tappers[_bartender.level].serve();
			
			Glass glass = new Glass(GlassHeight, _barTexture.getWidth(), 
					_barlocation[_bartender.level] + _barTexture.getHeight(), ServeTime);
			glass.level = _bartender.level;
			_glasses.add(glass);
			
			_serveTime  = ServeTime;
		}
	}
	
	float _glassRegen = 4f;

	@Override
	public void update(float dt) {
		
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
				_glassCount++;
			}
			
			if (glass.remove) {
				_glasses.remove(glass);
			}
		}
		
		for (int i = _items.size() - 1; i >= 0; i--) {
			TimedImage item = _items.get(i);
			item.update(dt);
			if (_bartender.intersects(item)) {
				_items.remove(item);
				Score.Total += 100;				
				Score.ItemsCollected++;
				
				if (item.tag == PatronType.exterminator) {
					LevelManager.killBugs();
				} else {
					LevelManager.killMicrobes();
				}
				
				SoundManager.play(LevelManager.Levels.Human, HumanAssets.Sounds.Ding);
				
			} else if (item.remove){
				_items.remove(item);
			}
		}
	
		_patronManager.update(_glasses, _items, dt);
		_glassWidget.update(_glassCount, dt);
		_cashWidget.update(dt);
		
		_glassRegen -= dt;
		if (_glassRegen < 0) {
			_glassCount += 2;
			if (_glassCount > 99) {
				_glassCount = 99;
			}
			_glassRegen = 2f;
		}
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
		
		for (int i = 0; i < _items.size(); i++) {
			_items.get(i).draw(batch);
		}
		
		_bartender.draw(batch);
		
		if (tutorialText == null) {
			_glassWidget.draw(batch);
			_cashWidget.draw(batch);
		}

	
		batch.setColor(_powerColor);
		batch.draw(Assets.squareTex, 0,  0,  GameConstants.GameWidth,  GameConstants.GameHeight);
		batch.setColor(Color.WHITE);
	}
		
	
	@Override
	public void reset() {
		_patronManager.reset();
	}
}