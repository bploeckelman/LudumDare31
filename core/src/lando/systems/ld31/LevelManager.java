package lando.systems.ld31;

import levels.*;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.primitives.MutableFloat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.MathUtils;

public class LevelManager implements InputProcessor{

    public GameLevel[] levels = new GameLevel[7];
    public int currentLevel;
    public int lastLevel;
    public MutableFloat transition = new MutableFloat(0);
    private final FrameBuffer currentFBO;
    private final FrameBuffer lastFBO;

    public LevelManager(){
        levels[0] = new IntraCellularLevel();
        levels[1] = new DemoLevel();
        levels[2] = new Insects();
        levels[3] = new HumanLevel();
        levels[4] = new CityLevel();
        levels[5] = new PlanetaryLevel();
        levels[6] = new GalacticLevel();
        currentLevel = 3;
        lastLevel = -1;
        Gdx.input.setInputProcessor(this);
        currentFBO = new FrameBuffer(Pixmap.Format.RGB888, GameConstants.ScreenWidth, GameConstants.ScreenHeight, false);
        lastFBO = new FrameBuffer(Pixmap.Format.RGB888, GameConstants.ScreenWidth, GameConstants.ScreenHeight, false);

    }
    

    private final float transitionLength = 1f;
    public void setLevel(int index){
    	if (lastLevel != -1 || index == currentLevel) return;
    	
    	lastLevel = currentLevel;
        currentLevel = index;
        
        transition.setValue(1f);
        
        Tween.to(transition, 0, transitionLength)
        .target(0f)
        .setCallback(callbackAtEnd)
        .setCallbackTriggers(TweenCallback.END)
        .start(LudumDare31.tweens);
        //TODO make a way to transition these
    }

    public void update(float dt){
        for (int i = 0; i < levels.length; i++){
            if (levels[i] == null) continue;
            levels[i].update(i == currentLevel ? dt : dt/2.0f);
            if (i == currentLevel){
                levels[i].handleInput(dt);
            }
        }

        if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {
            if (Gdx.input.isKeyJustPressed(Keys.NUM_0)) {
            	setLevel(0);
            } else if (Gdx.input.isKeyJustPressed(Keys.NUM_1)) {
            	setLevel(1);
            } else if (Gdx.input.isKeyJustPressed(Keys.NUM_2)) {
            	setLevel(2);
            } else if (Gdx.input.isKeyJustPressed(Keys.NUM_3)) {
            	setLevel(3);
            } else if (Gdx.input.isKeyJustPressed(Keys.NUM_4)) {
            	setLevel(4);
            } else if (Gdx.input.isKeyJustPressed(Keys.NUM_5)) {
                setLevel(5);
            } else if (Gdx.input.isKeyJustPressed(Keys.NUM_6)) {
                setLevel(6);
            }
        }
    }

    public void render(SpriteBatch batch){
    	if (lastLevel != -1){
    		lastFBO.begin();
        	batch.begin();
            levels[lastLevel].render(batch);
            batch.end();
    		lastFBO.end();
    	}
    	currentFBO.begin();
    	batch.begin();
        levels[currentLevel].render(batch);
        batch.end();
        currentFBO.end();
        
        Texture lastTexture = lastFBO.getColorBufferTexture();
        Texture currentTexture = currentFBO.getColorBufferTexture();
        
        batch.begin();
        if (lastLevel == -1){
	        
	        batch.draw(currentTexture, 0, currentFBO.getHeight(), currentFBO.getWidth(), -currentFBO.getHeight());
	        
        } else {
        	if (lastLevel < currentLevel){ // Zooming out
        		float alpha = transition.floatValue();
        		float currentAlpha = 1 + alpha;
        		float lastAlpha = alpha;
    	        batch.draw(currentTexture, currentFBO.getWidth()/2 * (1 - currentAlpha), currentFBO.getHeight() - (currentFBO.getHeight()/2 * ( 1- currentAlpha)), currentFBO.getWidth() * currentAlpha, -currentFBO.getHeight() * currentAlpha);
    	        batch.setColor(1, 1, 1, transition.floatValue());
    	        batch.draw(lastTexture, currentFBO.getWidth() /2 * (1 -lastAlpha), currentFBO.getHeight() - (currentFBO.getHeight()/2 * ( 1- lastAlpha)), currentFBO.getWidth() * ( lastAlpha), -currentFBO.getHeight()* (alpha));
    	        batch.setColor(Color.WHITE);
        	} else { // zooming in
        		float alpha =  1 - transition.floatValue();
        		float currentAlpha = alpha;
        		float lastAlpha = 1 + alpha;
    	        batch.draw(lastTexture, currentFBO.getWidth()/2 * (1 - lastAlpha), currentFBO.getHeight() - (currentFBO.getHeight()/2 * ( 1- lastAlpha)), currentFBO.getWidth()* lastAlpha, -currentFBO.getHeight()* lastAlpha);
    	        batch.setColor(1, 1, 1, alpha);
    	        batch.draw(currentTexture, currentFBO.getWidth()/2 * (1 - currentAlpha), currentFBO.getHeight() - (currentFBO.getHeight()/2 * ( 1- currentAlpha)), currentFBO.getWidth()* ( currentAlpha), -currentFBO.getHeight()* ( currentAlpha));
    	        batch.setColor(Color.WHITE);
        	}
        }
        batch.end();
    }


	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	boolean touchedDown = false;
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		if (pointer != 0) return false;
		touchedDown = true;
		if (levels[currentLevel] != null && screenX < GameConstants.GameWidth){
			levels[currentLevel].touchDown(screenX, screenY, button);
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		if (pointer != 0) return false;
		touchedDown = false;
		if (levels[currentLevel] != null && screenX < GameConstants.GameWidth){
			levels[currentLevel].touchUp(screenX, screenY, button);
		}

		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		if (pointer != 0) return false;
		if (levels[currentLevel] != null && screenX < GameConstants.GameWidth){
			levels[currentLevel].touchDragged(screenX, screenY);
		}
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		if (levels[currentLevel] != null){
			levels[currentLevel].mouseMoved(screenX, screenY);
		}
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		if (!touchedDown){
			setLevel(MathUtils.clamp(currentLevel + Math.abs(amount) / amount, 0, 6));

		}
		return false;
	}
	
	private TweenCallback callbackAtEnd = new TweenCallback()
	{
		
		@Override
		public void onEvent(int type, BaseTween<?> source)
		{
			if(type == TweenCallback.END){
				lastLevel = -1;
			}
			
		}
	};

}
