package levels;

import lando.systems.ld31.Assets;
import lando.systems.ld31.GameConstants;


import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public abstract class GameLevel {
	
	protected Camera camera;
	
	/**
	 * Start threat on creation, this won't get created until the 
	 * level is ready to be handled.
	 */
	public GameLevel(){
		camera = new OrthographicCamera(GameConstants.ScreenWidth, GameConstants.ScreenHeight);
		camera.translate(camera.viewportWidth/2, camera.viewportHeight/2, 0);
		camera.update();

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
		//batch.draw(img, x, y, width, height);
	}
	
	/**
	 * 
	 * @return if this has a threat that needs attention
	 */
	public abstract boolean hasThreat();
	
	/**
	 * Poll Gdx.input here to control the game
	 */
	public abstract void handleInput(float dt);
	
	/**
	 * 
	 * @param dt the update fraction in Seconds
	 */
	public abstract void update(float dt);
	

	
	public abstract void draw(SpriteBatch batch);
	

}
