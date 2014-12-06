package levels;

import lando.systems.ld31.Assets;
import lando.systems.ld31.GameConstants;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class GameLevel {
	
    Texture img;
    float x, y;
    int xdir, ydir;

    final float speed = 50f;
	
	protected Camera camera;
	
	/**
	 * Start threat on creation, this won't get created until the 
	 * level is ready to be handled.
	 */
	public GameLevel(){
		camera = new OrthographicCamera(GameConstants.ScreenWidth, GameConstants.ScreenHeight);
		camera.translate(camera.viewportWidth/2, camera.viewportHeight/2, 0);
		camera.update();
        img = Assets.ludumdare;

        x = Gdx.graphics.getWidth()  / 2f - img.getWidth()  / 2f;
        y = Gdx.graphics.getHeight() / 2f - img.getHeight() / 2f;

        xdir = 1;
        ydir = 1;
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
        
        x += xdir * dt * speed;
        y += ydir * dt * speed;
        if (x < 0 || x + img.getWidth()  > camera.viewportWidth)  xdir *= -1;
        if (y < 0 || y + img.getHeight() > camera.viewportHeight) ydir *= -1;
	}
	
	/**
	 * Override me!!
	 * @param batch
	 */
	public void render(SpriteBatch batch){
		batch.setProjectionMatrix(camera.combined);
		batch.draw(img, x, y);
	}
	
	/**
	 * 
	 * @return if this has a threat that needs attention
	 */
	public boolean hasThreat(){
		return false;
	}
}
