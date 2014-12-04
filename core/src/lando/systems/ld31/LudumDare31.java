package lando.systems.ld31;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LudumDare31 extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	float x, y;
	float r, g, b;
	float accum;
	float threshold;
	int xdir, ydir;

	final float speed = 50f;

	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("ludumdare.png");
		x = Gdx.graphics.getWidth()  / 2f - img.getWidth()  / 2f;
		y = Gdx.graphics.getHeight() / 2f - img.getHeight() / 2f;
		r = g = b = 1;
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

		accum += delta;
		if (accum > threshold) {
			accum -= threshold;
			r = (float) Math.random();
			g = (float) Math.random();
			b = (float) Math.random();
		}

		x += xdir * delta * speed;
		y += ydir * delta * speed;
		if (x < 0 || x + img.getWidth()  > Gdx.graphics.getWidth())  xdir *= -1;
		if (y < 0 || y + img.getHeight() > Gdx.graphics.getHeight()) ydir *= -1;

		Gdx.gl.glClearColor(r,g,b,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		batch.draw(img, x, y);
		batch.end();
	}
}
