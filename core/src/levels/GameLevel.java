package levels;

import lando.systems.ld31.GameConstants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class GameLevel {
	
	protected Camera camera;
	
	/**
	 * Start threat on creation, this won't get created until the 
	 * level is ready to be handled.
	 */
	public GameLevel(){
		camera = new OrthographicCamera(GameConstants.ScreenWidth, GameConstants.ScreenHeight);
		
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
	 * Poll Gdx.input here to control the game
	 */
	public void handleInput(){
		//float mouseX = Gdx.input.getX();
		
	}
	
	/**
	 * Override me!!
	 * @param dt the update fraction in Seconds
	 */
	public void update(float dt){
		
	}
	
	/**
	 * Override me!!
	 * @param batch
	 */
	public void render(SpriteBatch batch){
		
	}
	
	/**
	 * 
	 * @return if this has a threat that needs attention
	 */
	public boolean hasThreat(){
		return false;
	}
}
