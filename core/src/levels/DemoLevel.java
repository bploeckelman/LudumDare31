package levels;

import lando.systems.ld31.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class DemoLevel extends GameLevel {

    Texture img;
    float x, y;
    int xdir, ydir;

    final float speed = 50f;
    
    public DemoLevel(){
    	super();
    	
        img = Assets.ludumdare;

        x = Gdx.graphics.getWidth()  / 2f - img.getWidth()  / 2f;
        y = Gdx.graphics.getHeight() / 2f - img.getHeight() / 2f;

        xdir = 1;
        ydir = 1;
    }
    
	@Override
	public void handleInput(float dt) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(float dt) {
		
		float tempX = x + (xdir * dt * speed);
		if (tempX < 0 || tempX > camera.viewportWidth - img.getWidth()){
			xdir *= -1;
			tempX = x + (xdir * dt * speed); 
		}
		float tempY = y + (ydir * dt * speed);
		if (tempY < 0 || tempY > camera.viewportHeight - img.getHeight()){
			ydir *= -1;
			tempY = y + (ydir * dt * speed);
		}
		
		x = tempX;
		y = tempY;
		
	}

	@Override
	public void draw(SpriteBatch batch) {
		batch.draw(img, x, y);
		Assets.gameFont.draw(batch, "Large text?", 0, 200);
		Assets.smallFont.draw(batch, "Small font", 0, 300);
		
	}

	@Override
	public int hasThreat() {
		// TODO Auto-generated method stub
		return 0;
	}

}
