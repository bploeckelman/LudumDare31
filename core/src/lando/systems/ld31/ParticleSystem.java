package lando.systems.ld31;

import java.util.ArrayList;

import aurelienribon.tweenengine.TweenEquation;
import aurelienribon.tweenengine.TweenEquations;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class ParticleSystem {
	private final ArrayList<Particle> activeParticles = new ArrayList<Particle>();
	
	 private final Pool<Particle> particlePool = new Pool<Particle>() {
		    @Override
		    protected Particle newObject() {
		        return new Particle();
		    }
	 };
	 
	 public ParticleSystem(){
		 
	 }
	 
	 public void addParticle(Vector2 startPos, Vector2 endPos, Color startColor, Color endColor, float duration, TweenEquation easing){
		 Particle item = particlePool.obtain();
	     item.init(startPos, endPos, startColor, endColor, duration, easing);
	     activeParticles.add(item);
	 }

	public void update(float dt) {
		Particle item;
		int len = activeParticles.size();
		for (int i = len; --i >= 0;) {
			item = activeParticles.get(i);
			if (item.alive == false) {
				activeParticles.remove(i);
				particlePool.free(item);
			}
		}
	}
	 
	 public void draw(SpriteBatch batch){
		 for (int i = 0; i < activeParticles.size(); i++){
			 activeParticles.get(i).draw(batch);
		 }
	 }
		    
}
