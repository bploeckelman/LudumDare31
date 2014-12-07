package lando.systems.ld31;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.*;
import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LudumDare31 extends ApplicationAdapter {
    public static TweenManager tweens;
    SpriteBatch batch;
    LevelManager levelMgr;
    

    @Override
    public void create () {
    	
    	Assets.load();
    	
    	levelMgr = new LevelManager();
        tweens = new TweenManager();
        batch = new SpriteBatch();

        
    }

    @Override
    public void render () {
        //if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
        //    Gdx.app.exit();
        //}

        float delta = Gdx.graphics.getDeltaTime();

        tweens.update(delta);
        levelMgr.update(delta);

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        
        levelMgr.render(batch);
    }
}
