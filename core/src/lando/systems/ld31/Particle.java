package lando.systems.ld31;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquation;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Particle {
	
	public boolean alive;
	public Color color;
	public Vector2 pos;
	
	public Particle(){
		
	}
	
	public void init(Vector2 startPos, Vector2 endPos, Color startColor, Color endColor, float duration, TweenEquation easing){
		alive = true;
		color = startColor.cpy();
        Tween.to(color, ColorAccessor.COLOR, duration)      
        .target(endColor.r, endColor.g, endColor.b, endColor.a)
        .ease(easing)
        .setCallback(callbackAtEnd)
        .setCallbackTriggers(TweenCallback.END)
        .start(LudumDare31.tweens);
        
        pos = startPos.cpy();
        Tween.to(pos, Vector2Accessor.POSITION_XY, duration)
        .target(endPos.x, endPos.y)
        .ease(easing)
        .start(LudumDare31.tweens);
	}
	
	public void update(float dt){
		
	}
	
	public void draw(SpriteBatch batch){
		batch.setColor(color);
		batch.draw(Assets.squareTex, pos.x, pos.y, 1, 1);
		batch.setColor(Color.WHITE);
	}
	
	private TweenCallback callbackAtEnd = new TweenCallback()
	{
		
		@Override
		public void onEvent(int type, BaseTween<?> source)
		{
			if(type == TweenCallback.END){
				alive = false;
			}
		}
	};
}
