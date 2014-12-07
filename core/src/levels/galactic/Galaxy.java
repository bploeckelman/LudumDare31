package levels.galactic;

import java.util.List;

import lando.systems.ld31.Assets;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Galaxy {
	
	Sprite sprite;
	public Vector2 vel;
	public Vector2 pos;
	
	public float mass;
	
	public Galaxy(Vector2 pos){
		this.pos = pos;
		this.vel = new Vector2(0,0); // head towards the center
		sprite = new Sprite(Assets.squareTex);
		sprite.setOriginCenter();
		
		sprite.setSize(40, 40);
		sprite.setCenter(pos.x, pos.y);
		mass = 5;

	}
	
	public Vector2 getPos(){
		return pos;
	}
	
	public void update(float dt, List<Galaxy> gals){
		Vector2 accel = new Vector2(0,0);
		for (int i = 0; i < gals.size(); i++){
			Galaxy gal = gals.get(i);
			if (gal == this) continue;
			
			Vector2 galPos = gal.getPos();
			Vector2 angle = galPos.cpy().sub(pos);
			float dist2 = MathUtils.clamp(angle.len2(), 1, 10000);
			float g = 200 * this.mass * gal.mass / dist2;
			angle.nor().scl(g);
			accel.add(angle);
			
		}
		vel.add(accel.scl(dt));
		pos.x += vel.x * dt;
		pos.y += vel.y * dt;
	}
	
	public void draw(SpriteBatch batch) {
		sprite.setCenter(pos.x, pos.y);
		sprite.draw(batch);
	}
}
