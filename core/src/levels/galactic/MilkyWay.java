package levels.galactic;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import lando.systems.ld31.Assets;
import lando.systems.ld31.GameObject;

public class MilkyWay extends GameObject{

	Sprite sprite;
	
	public MilkyWay(float x, float y){
		sprite = new Sprite(Assets.squareTex);
		sprite.setOriginCenter();
		sprite.setPosition(x, y);
		sprite.setSize(40, 40);

	}
	
	@Override
	public void draw(SpriteBatch batch) {
		sprite.draw(batch);
	}

}
