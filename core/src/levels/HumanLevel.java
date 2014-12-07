package levels;

import java.util.ArrayList;

import lando.systems.ld31.GameConstants;
import levels.human.*;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class HumanLevel extends GameLevel {

	public int GlassHeight = 36;
	
	// time a glass is invisible from being serverd
	public float ServeTime = 0.2f;
	
	final int BarCount = 4;
	
	Texture _barTexture;
	
	Tapper[] _tappers;
	int _levelHeight;
	int [] _barlocation;
	
	ArrayList<Glass> _glasses = new ArrayList<Glass>(15);
	
	int _bartenderLevel = 0;

	Bartender _bartender;
	
	public HumanLevel()
	{
		_barTexture = new Texture(HumanAssets.Bar);
	
		_barlocation = new int[BarCount];
		_tappers = new Tapper[BarCount];
				
		_levelHeight = (int)((GameConstants.ScreenHeight*.85) / BarCount);
		int tapperX = GameConstants.ScreenWidth - Tapper.width;
		
		_bartender = new Bartender();
		_bartender.setHeight(_levelHeight - 20);
		_bartender.x = GameConstants.ScreenWidth - 70;
		
		int y = 25;
		
		for (int i = 0; i < BarCount; i++) {
			_barlocation[i] = y;
			
			y += _levelHeight;
			
			_tappers[i] = new Tapper();
			_tappers[i].position = new Vector2(tapperX, y - Tapper.height);
		}
				
		_bartender.y = _barlocation[_bartenderLevel];
	}
	
	@Override
	public int hasThreat() {
		return 0;
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
			_glasses.add(new Glass(GlassHeight, _barTexture.getWidth(), 
					_barlocation[_bartenderLevel] + _barTexture.getHeight(), ServeTime));
			
			_serveTime  = ServeTime;
		}
	}

	@Override
	public void update(float dt) {
		_bartender.update(dt);
		for (int i = 0; i < BarCount; i++) {
			_tappers[i].update(dt);
		}
		
		for (int i = _glasses.size() - 1; i >= 0; i--) {
			Glass glass = _glasses.get(i);
			glass.update(dt);
			if (glass.remove) {
				_glasses.remove(glass);
			}
		}
	}

	@Override
	public void draw(SpriteBatch batch) {
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