package levels.galactic;

import java.nio.file.Paths;
import java.util.ArrayList;
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
	public float width;
	public ArrayList<Vector2> path = new ArrayList<Vector2>();
	public boolean alive;
	public boolean isMilkyWay;
	
	public float mass;
	
	public Galaxy(Vector2 pos){
		this.pos = pos;
		isMilkyWay = false;
		alive = true;
		this.vel = new Vector2(0,0); // head towards the center
		sprite = new Sprite(Assets.squareTex);
		sprite.setOriginCenter();
		
		sprite.setSize(40, 40);
		sprite.setCenter(pos.x, pos.y);
		mass = 5;
		width = 40;

	}
	
	public Vector2 getPos(){
		return pos;
	}
	
	public void update(float dt, List<Galaxy> gals){
		for (int i = 0; i < gals.size(); i++){
			Galaxy gal = gals.get(i);
			if (gal == this) continue;
			if (!gal.alive) continue;
			if (pos.dst(gal.pos) < width/2.0 + gal.width/2.0){
				if (gal.isMilkyWay){
					
				}
				this.alive = false;
				float newMass = gal.mass + this.mass;
				gal.vel = gal.vel.scl(gal.mass / newMass).add(this.vel.scl(this.mass/newMass));
				
				gal.mass = newMass;
				gal.width += this.width;
				break;
			}
		}
		if (path.isEmpty()){
			Vector2 accel = new Vector2(0,0);
			for (int i = 0; i < gals.size(); i++){
				Galaxy gal = gals.get(i);
				if (gal == this) continue;
				if (!gal.alive) continue;
				Vector2 galPos = gal.getPos();
				Vector2 angle = galPos.cpy().sub(pos);
				float dist2 = MathUtils.clamp(angle.len2(), 1, 10000);
				float g = 200 * this.mass * gal.mass / dist2;
				angle.nor().scl(g);
				accel.add(angle);
				
			}
			vel.add(accel.scl(dt));

		} else {
			Vector2 direction = path.get(0).cpy().sub(pos);
			float dist = direction.len();
			float speed = vel.len() * dt;
			if (speed + (width/2) >= dist){
				path.remove(0);
			} else {
				vel.setAngle(direction.angle());
			}
		}
		
		pos.x += vel.x * dt;
		pos.y += vel.y * dt;
	}
	
	public void draw(SpriteBatch batch) {
		sprite.setSize(width,  width);
		sprite.setCenter(pos.x, pos.y);
		sprite.draw(batch);
		for (int i = 0; i < path.size(); i++){
			Vector2 point = path.get(i);
			batch.draw(Assets.squareTex, point.x, point.y, 1, 1); 
		}
	}
}
