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
    TweenManager tweens;
    SpriteBatch batch;
    LevelManager levelMgr;
    
    Texture img;
    float x, y;
    MutableFloat r, g, b;
    float accum;
    float threshold;
    int xdir, ydir;

    final float speed = 50f;

    @Override
    public void create () {
    	
    	Assets.load();
    	
    	levelMgr = new LevelManager();
        tweens = new TweenManager();
        batch = new SpriteBatch();

        
        img = Assets.ludumdare;

        x = Gdx.graphics.getWidth()  / 2f - img.getWidth()  / 2f;
        y = Gdx.graphics.getHeight() / 2f - img.getHeight() / 2f;
        r = new MutableFloat(1);
        g = new MutableFloat(1);
        b = new MutableFloat(1);
        accum = 0;
        threshold = 1;
        xdir = 1;
        ydir = 1;
    }

    @Override
    public void render () {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        float delta = Gdx.graphics.getDeltaTime();

        tweens.update(delta);
        levelMgr.update(delta);

        accum += delta;
        if (accum > threshold) {
            accum -= threshold;
            Timeline.createParallel()
                    .push(Tween.to(r, 0, threshold).target((float) Math.random()).ease(Cubic.INOUT))
                    .push(Tween.to(g, 0, threshold).target((float) Math.random()).ease(Cubic.INOUT))
                    .push(Tween.to(b, 0, threshold).target((float) Math.random()).ease(Cubic.INOUT))
                    .start(tweens);
        }

        x += xdir * delta * speed;
        y += ydir * delta * speed;
        if (x < 0 || x + img.getWidth()  > Gdx.graphics.getWidth())  xdir *= -1;
        if (y < 0 || y + img.getHeight() > Gdx.graphics.getHeight()) ydir *= -1;

        Gdx.gl.glClearColor(r.floatValue(),g.floatValue(),b.floatValue(),1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        //batch.draw(img, x, y);
        levelMgr.render(batch);
        batch.end();
    }
}
