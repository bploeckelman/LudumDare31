package levels.galactic;

import lando.systems.ld31.Assets;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Galaxy {
	
	Sprite sprite;
	public Vector2 vel;
	
	public Galaxy(Vector2 pos, Vector2 vel){
		this.vel = vel; // head towards the center
		sprite = new Sprite(Assets.squareTex);
		sprite.setOriginCenter();
		sprite.setPosition(pos.x, pos.y);
		sprite.setSize(40, 40);

	}
	
	public void update(float dt){
		sprite.setPosition(sprite.getX() + vel.x * dt, sprite.getY() + vel.y * dt);
	}
	
	public void draw(SpriteBatch batch) {
		sprite.draw(batch);
	}
}
