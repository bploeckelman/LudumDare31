package levels;

import lando.systems.ld31.Assets;
import lando.systems.ld31.GameConstants;
import lando.systems.ld31.LevelManager;
import lando.systems.ld31.LudumDare31;
import lando.systems.ld31.ParticleSystem;
import lando.systems.ld31.TutorialManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public abstract class GameLevel {
	
	protected Camera camera;
	public Vector2 zoomOutPoint;
	public String tutorialText;
	protected boolean startNext;
	protected boolean top;
	protected ParticleSystem particles;
	
	private static TutorialManager _tutorialManager = new TutorialManager();
	
	/**
	 * Start threat on creation, this won't get created until the 
	 * level is ready to be handled.
	 */
	public GameLevel(){
		particles = new ParticleSystem();
		camera = new OrthographicCamera(GameConstants.ScreenWidth, GameConstants.ScreenHeight);
		camera.translate(camera.viewportWidth/2, camera.viewportHeight/2, 0);
		camera.update();
		zoomOutPoint = new Vector2(camera.viewportWidth/2, camera.viewportHeight/2);
		startNext = false;
		top = false;
	}
	
	/**
	 * Use this to convert mouse position to game Coordinates
	 * @param screenPos
	 * @return the game Position
	 */
	protected Vector2 getGamePos(Vector2 screenPos){
		Vector3 screen3 = new Vector3(screenPos.x, screenPos.y, 0);
		camera.unproject(screen3);
		return new Vector2(screen3.x, screen3.y);
	}
	
	/**
	 * @param batch
	 */
	public void render(SpriteBatch batch){
		batch.setProjectionMatrix(camera.combined);
		batch.setColor(0,0,0,1);
		batch.draw(Assets.squareTex, 0, 0, camera.viewportWidth, camera.viewportHeight);
		batch.setColor(1,1,1,1);
			
		
		draw(batch);
		
		particles.draw(batch);
		
		_tutorialManager.draw(batch, tutorialText);
	}
	
	public boolean startNext(){
		return startNext;
	}
	
	public void reset() { }
	
	/**
	 * Find out if the Up Keys is held Down
	 * @return 
	 */
	protected boolean isUpPressed(){
		return Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.W);
	}
	
	/**
	 * Find out if the Up Keys were just pressed
	 * @return
	 */
	protected boolean isUpJustPressed(){
		return Gdx.input.isKeyJustPressed(Keys.UP) || Gdx.input.isKeyJustPressed(Keys.W);
	}
	
	/**
	 * Find out if the Up Keys is held Down
	 * @return 
	 */
	protected boolean isDownPressed(){
		return Gdx.input.isKeyPressed(Keys.DOWN) || Gdx.input.isKeyPressed(Keys.S);
	}
	
	/**
	 * Find out if the Up Keys were just pressed
	 * @return
	 */
	protected boolean isDownJustPressed(){
		return Gdx.input.isKeyJustPressed(Keys.DOWN) || Gdx.input.isKeyJustPressed(Keys.S);
	}
	
	/**
	 * Find out if the Up Keys is held Down
	 * @return 
	 */
	protected boolean isLeftPressed(){
		return Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A);
	}
	
	/**
	 * Find out if the Up Keys were just pressed
	 * @return
	 */
	protected boolean isLeftJustPressed(){
		return Gdx.input.isKeyJustPressed(Keys.LEFT) || Gdx.input.isKeyJustPressed(Keys.A);
	}
	
	/**
	 * Find out if the Up Keys is held Down
	 * @return 
	 */
	protected boolean isRightPressed(){
		return Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D);
	}
	
	/**
	 * Find out if the Up Keys were just pressed
	 * @return
	 */
	protected boolean isRightJustPressed(){
		return Gdx.input.isKeyJustPressed(Keys.RIGHT) || Gdx.input.isKeyJustPressed(Keys.D);
	}
	
	
	/**
	 * This method is called whenever the mouse is clicked. Make sure you call getGamePos
	 * so it is in game coords
	 * @param screenX
	 * @param screenY
	 * @param button
	 * @return
	 */
	public boolean touchDown(int screenX, int screenY, int button) {
		return false;
	}
	
	/**
	 * This is a method that is called whenever the mouse is let go. Make sure you
	 * call getGamePos so it is in game coords
	 * @param screenX
	 * @param screenY
	 * @param button
	 * @return
	 */
	public boolean touchUp(int screenX, int screenY, int button) {
		if (_tutorialManager.touchUp(screenX, screenY, button)) {
			tutorialText = null;
		}
		
		return false;
	}
	
	/**
	 * This will only be called when the mouse moves and a button is down.
	 * Make sure you call getGamePos so it is in game coords.
	 * @param screenX
	 * @param screenY
	 * @return
	 */
	public boolean touchDragged(int screenX, int screenY) {
		return false;
	}
	
	/**
	 * This is called whenever the mouse is moved, even if it isn't clicked. Make
	 * @param screenX
	 * @param screenY
	 * @return
	 */
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}
	
	/**
	 * 0 - no Threat
	 * 1 - Mild
	 * 2 - Medium
	 * 3 - Imminent
	 * @return if this has a threat that needs attention
	 */
	public abstract int hasThreat();
	
	/**
	 * Poll Gdx.input here to control the game
	 */
	public abstract void handleInput(float dt);
	
	float _tutorialTime = 5f;
	
	public void update(float dt, boolean isTop)
	{
		if (top != isTop) {
			gotFocus(isTop);
			top = isTop;
		}
		
		particles.update(dt);
		if (!isTop) {
			dt *= .1f;
		}
		
		if (tutorialText == null) {
			update(dt);
			
			if (isTop) {
				handleInput(dt);
			}
		} else {
			_tutorialTime -= dt;
			
			if (_tutorialTime < 0 || Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
				tutorialText = null;
			}
		}
		
		//temp
		debugLevelJump();
	}
	
	private void debugLevelJump() {
		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
		{		
			LevelManager lm = LudumDare31.levelMgr;
			
			if (Gdx.input.isKeyJustPressed(Keys.NUM_0)) {
				lm.addLevel(0);
			} else if (Gdx.input.isKeyJustPressed(Keys.NUM_1)) {
				lm.addLevel(1);
			} else if (Gdx.input.isKeyJustPressed(Keys.NUM_2)){
				lm.addLevel(2);
			} else if (Gdx.input.isKeyJustPressed(Keys.NUM_3)){
				lm.addLevel(3);
			} else if (Gdx.input.isKeyJustPressed(Keys.NUM_4)){
				lm.addLevel(4);
			} else if (Gdx.input.isKeyJustPressed(Keys.NUM_5)){
				lm.addLevel(5);
			} else if (Gdx.input.isKeyJustPressed(Keys.NUM_6)){
				lm.addLevel(6);;
			} else if (Gdx.input.isKeyJustPressed(Keys.NUM_7)){
				lm.addLevel(7);
			} 
		}
		
		
		
	}
		
	protected void gotFocus(boolean hasFocus) { }

	/**
	 * 
	 * @param dt the update fraction in Seconds
	 */
	public abstract void update(float dt);
	
	public abstract void draw(SpriteBatch batch);
}
