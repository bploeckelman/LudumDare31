package lando.systems.ld31;

import levels.*;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.primitives.MutableFloat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class LevelManager implements InputProcessor{
	
	public class Levels { 
		public static final String IntraCellular = "Asteroids";
		public static final String InterCellular = "Bubble Pop";
		public static final String Insects = "Tower Defense";
		public static final String Human = "Tapper";
		public static final String City = "Pipe Dream";
		public static final String Planetary = "Missle Defense";
		public static final String Galaxy = "ATC";
	}
	
	public static final int levelCount = 7;
	public static int powerLevel = 4;
	
    public GameLevel[] levels = new GameLevel[levelCount + 1];
    public int currentLevel;
    public int lastLevel;
    public int targetLevel;
    public MutableFloat transition = new MutableFloat(0);
    private final float transitionLength = 1f;
    
    private final FrameBuffer currentFBO;
    private final FrameBuffer lastFBO;
    private OrthographicCamera camera;
    
    private boolean gameStarted;

    public LevelManager(){
    	camera = new OrthographicCamera(GameConstants.ScreenWidth, GameConstants.ScreenHeight);
    	camera.translate(camera.viewportWidth/2, camera.viewportHeight/2);
    	camera.update();
        //levels[0] = new IntraCellularLevel();
        //levels[1] = new IntercellularLevel();
        //levels[2] = new Insects();
        //levels[3] = new HumanLevel();
        //levels[4] = new CityLevel();
        //levels[5] = new PlanetaryLevel();
        //levels[6] = new GalacticLevel();
        //levels[7] = new Snowman();
        currentLevel = 3;
        lastLevel = -1;
        Gdx.input.setInputProcessor(this);
        currentFBO = new FrameBuffer(Pixmap.Format.RGB888, GameConstants.ScreenWidth, GameConstants.ScreenHeight, false);
        lastFBO = new FrameBuffer(Pixmap.Format.RGB888, GameConstants.ScreenWidth, GameConstants.ScreenHeight, false);
    }
    
    public void addLevel(int index) {
    	
    	if (index < 0 || index >= levels.length || levels[index] != null) return;
    	
    	switch (index) {
    		case 0:
    			levels[0] = new IntraCellularLevel();
    			break;
    		case 1:
    			levels[1] = new IntercellularLevel();
    			break;
    		case 2:
    			levels[2] = new Insects();
    			break;
    			// starts on 3
    		case 3:
    			levels[3] = new HumanLevel();
    			break;
    		case 4:
    			levels[4] = new CityLevel();
    			break;
    		case 5:
	    		levels[5] = new PlanetaryLevel();
	    		break;
    		case 6:
    			levels[6] = new GalacticLevel();
	        	break;
    		case 7:
    			levels[7] = new Snowman();
    			break;
    	}

    	setLevel(index);
    }
   

    public void setLevel(int index){
    	if (currentLevel == 7) return; // Don't leave the snowman
    	if (index < 0 || index >= levels.length) return;
    	if (lastLevel != -1 || index == currentLevel) return;
    	if (levels[index] == null) return; // Not ready for this one yet
    	targetLevel = index;
    	
    	lastLevel = currentLevel;
    	do {
	    	if (currentLevel < targetLevel)
	    	{
	    		currentLevel ++;
	    	} else {
	    		currentLevel --;
	    	}
    	} while (levels[currentLevel] == null);
    	
        
        
        transition.setValue(1f);
        
        Tween.to(transition, 0, transitionLength /( 1 + Math.abs(currentLevel - targetLevel)))
        .target(0f)
        .setCallback(callbackAtEnd)
        .setCallbackTriggers(TweenCallback.END)
        .start(LudumDare31.tweens);
        //TODO make a way to transition these
    }

    public void update(float dt){
    	if (!gameStarted) return;
        for (int i = 0; i < levels.length; i++){
            if (levels[i] == null) continue;
            levels[i].update(dt, (i == currentLevel));
        }        
    }

    public void render(SpriteBatch batch){
    	if (!gameStarted){
    		batch.begin();
    		//batch.draw(Assets.titleScreen, 0, 0, GameConstants.ScreenWidth, GameConstants.ScreenHeight);
    		drawTitle(batch);
    		batch.end();
    		return;
    	}
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
        lastTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        Texture currentTexture = currentFBO.getColorBufferTexture();
        currentTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        
        batch.begin();
        if (lastLevel == -1){	        
	        batch.draw(currentTexture, 0, currentFBO.getHeight(), currentFBO.getWidth(), -currentFBO.getHeight());        
        } else {
        	if (lastLevel < currentLevel){ // Zooming out
        		float alpha = transition.floatValue();
        		float currentAlpha = 1 + 20f* alpha;
        		float lastAlpha = alpha;
        		Vector2 zoomPoint = levels[currentLevel].zoomOutPoint;
    	        batch.draw(currentTexture, zoomPoint.x * (1 - currentAlpha), currentFBO.getHeight() - (zoomPoint.y * ( 1- currentAlpha)), currentFBO.getWidth() * currentAlpha, -currentFBO.getHeight() * currentAlpha);
    	        batch.setColor(1, 1, 1, transition.floatValue());
    	        batch.draw(lastTexture, zoomPoint.x * (1 -lastAlpha), currentFBO.getHeight() - (zoomPoint.y * ( 1- lastAlpha)), currentFBO.getWidth() * ( lastAlpha), -currentFBO.getHeight()* (alpha));
    	        batch.setColor(Color.WHITE);
        	} else { // zooming in
        		float alpha =  1 - transition.floatValue();
        		float currentAlpha = alpha;
        		float lastAlpha = 1 + 20f * alpha;
        		Vector2 zoomPoint = levels[lastLevel].zoomOutPoint;
    	        batch.draw(lastTexture, zoomPoint.x  * (1 - lastAlpha), currentFBO.getHeight() - (zoomPoint.y * ( 1- lastAlpha)), currentFBO.getWidth()* lastAlpha, -currentFBO.getHeight()* lastAlpha);
    	        batch.setColor(1, 1, 1, alpha);
    	        batch.draw(currentTexture, zoomPoint.x  * (1 - currentAlpha), currentFBO.getHeight() - (zoomPoint.y * ( 1- currentAlpha)), currentFBO.getWidth()* ( currentAlpha), -currentFBO.getHeight()* ( currentAlpha));
    	        batch.setColor(Color.WHITE);
        	}
        }
        
        // Hud time
        batch.setProjectionMatrix(camera.combined);
        batch.draw(Assets.sidebarBackground, GameConstants.GameWidth, 0, 100, GameConstants.ScreenHeight);
        for (int i = 0; i < levelCount; i++){
        	Texture tex = Assets.sidebarBlack;
        	if (levels[i] != null){
        		tex = Assets.sidebarStatus[levels[i].hasThreat()];
        	}
        	batch.draw(tex, GameConstants.GameWidth + 48, GameConstants.ScreenHeight - 196 - (i * 75), 32, 75);
        	
        	batch.draw(Assets.sidebarLabels[i], GameConstants.GameWidth + 2, GameConstants.ScreenHeight - 196 - (i * 75), 78, 75);
        }
        
        if (currentLevel < 7){
        	float trans = transition.floatValue() * Math.signum(lastLevel - currentLevel);
	        batch.draw(Assets.sidebarSelection, GameConstants.GameWidth + 46, GameConstants.ScreenHeight - 199 - ((currentLevel + trans) * 75.0f));
        }
        batch.end();
    }


	private void drawTitle(SpriteBatch batch) {
		float width = GameConstants.ScreenWidth;
		float height = GameConstants.ScreenHeight;
		
		BitmapFont font = Assets.gameFont;
		
		float y = height - 180;
		font.setScale(4f);
		font.drawWrapped(batch, "BAR", 0, y, width, HAlignment.CENTER);
		
		y -= 200;
		font.setScale(2f);		
		font.drawWrapped(batch, "Order of", 0, y, width, HAlignment.CENTER);
		
		y -= 150;
		
		font.setScale(1f);		
		font.drawWrapped(batch, "Magnitude", 0, y, width, HAlignment.CENTER);
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
		if (!gameStarted) return false;
		// TODO Auto-generated method stub
		if (pointer != 0) return false;
		Vector3 gamePos = camera.unproject(new Vector3(screenX, screenY, 0));
		if (gamePos.x > GameConstants.GameWidth && gamePos.x < GameConstants.GameWidth + 78){
			int level = (int)Math.floor(((GameConstants.ScreenHeight - gamePos.y) - 125) / 75 );
			setLevel(level);
			return true;
		}
		touchedDown = true;
		if (levels[currentLevel] != null && screenX < GameConstants.GameWidth){
			levels[currentLevel].touchDown(screenX, screenY, button);
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		if (!gameStarted) {
			gameStarted = true;
			addLevel(3);
			return true;
		}
		if (pointer != 0) return false;
		touchedDown = false;
		if (levels[currentLevel] != null && screenX < GameConstants.GameWidth){
			levels[currentLevel].touchUp(screenX, screenY, button);
		}

		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (!gameStarted) return false;
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
		if (!gameStarted) return false;
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
				if (currentLevel == targetLevel){
					lastLevel = -1;
				} else {
					lastLevel = -1;
					setLevel(targetLevel);
				}
			}
		}
	};
	
	public static boolean isLevelActive(String level) {
		boolean active = false;
		
		switch (LudumDare31.levelMgr.currentLevel){
			case 0:
				active = (level == LevelManager.Levels.IntraCellular);
				break;
			case 1: 
				active = (level == LevelManager.Levels.InterCellular);
				break;
			case 2:
				active = (level == LevelManager.Levels.Insects);
				break;
			case 3:
				active = (level == LevelManager.Levels.Human);
				break;
			case 4:
				active = (level == LevelManager.Levels.City);
				break;
			case 5:
				active = (level == LevelManager.Levels.Planetary);
				break;
			case 6:
				active = (level == LevelManager.Levels.Galaxy);
				break;
			default:
				active = false;
				break;
		}
		
		return active;
	}
	
	public static void killBugs() {
		resetLevel(2);
	}
	
	public static void killMicrobes() {
		resetLevel(0);
	}
	
	public static void resetPatrons() {
		resetLevel(3);
	}
	
	private static void resetLevel(int levelIndex)
	{
		if (levelIndex < 0 || levelIndex >= LevelManager.levelCount) return;
		
		GameLevel level = LudumDare31.levelMgr.levels[levelIndex];
		if (level != null) {
			level.reset();
		}		
	}
}
