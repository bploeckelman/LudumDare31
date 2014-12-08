package lando.systems.ld31;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class LudumDare31 extends ApplicationAdapter {
	
    public static TweenManager tweens;
    public static LevelManager levelMgr;
    
    SpriteBatch batch;
    
    @Override
    public void create () {
    	
    	Assets.load();

    	tweens = new TweenManager();
        Tween.setCombinedAttributesLimit(4);
		Tween.registerAccessor(Vector2.class, new Vector2Accessor());
		Tween.registerAccessor(Color.class, new ColorAccessor());

		levelMgr = new LevelManager();
        batch = new SpriteBatch();
    }

    @Override
    public void render () {
    	
        float delta = Gdx.graphics.getDeltaTime();

        tweens.update(delta);
        levelMgr.update(delta);

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        
        levelMgr.render(batch);
    }
}
