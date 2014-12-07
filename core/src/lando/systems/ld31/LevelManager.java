package lando.systems.ld31;

import levels.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class LevelManager implements InputProcessor{

    public GameLevel[] levels = new GameLevel[7];
    public int currentLevel;

    public LevelManager(){
        levels[0] = new IntraCellularLevel();
        levels[1] = new DemoLevel();
        levels[2] = new Insects();
        levels[3] = new HumanLevel();
        levels[4] = new CityLevel();
        levels[5] = new PlanetaryLevel();
        levels[6] = new GalacticLevel();
        currentLevel = 0;
        Gdx.input.setInputProcessor(this);
    }

    public void setLevel(int index){
        currentLevel = index;
        //TODO make a way to transition these
    }

    public void update(float dt){
        for (int i = 0; i < levels.length; i++){
            if (levels[i] == null) continue;
            levels[i].update(i == currentLevel ? dt : dt/100.0f);
            if (i == currentLevel){
                levels[i].handleInput(dt);
            }
        }

        if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {
            if (Gdx.input.isKeyJustPressed(Keys.NUM_0)) {
                currentLevel = 0;
            } else if (Gdx.input.isKeyJustPressed(Keys.NUM_1)) {
                currentLevel = 1;
            } else if (Gdx.input.isKeyJustPressed(Keys.NUM_2)) {
                currentLevel = 2;
            } else if (Gdx.input.isKeyJustPressed(Keys.NUM_3)) {
                currentLevel = 3;
            } else if (Gdx.input.isKeyJustPressed(Keys.NUM_4)) {
                currentLevel = 4;
            } else if (Gdx.input.isKeyJustPressed(Keys.NUM_5)) {
                currentLevel = 5;
            } else if (Gdx.input.isKeyJustPressed(Keys.NUM_6)) {
                currentLevel = 6;
            }
        }
    }

    public void render(SpriteBatch batch){
        levels[currentLevel].render(batch);
    }


	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	boolean touchedDown = false;

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		if (pointer != 0) return false;
		touchedDown = true;
		if (levels[currentLevel] != null && screenX < GameConstants.GameWidth){
			levels[currentLevel].touchDown(screenX, screenY, button);
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		if (pointer != 0) return false;
		touchedDown = false;
		if (levels[currentLevel] != null && screenX < GameConstants.GameWidth){
			levels[currentLevel].touchUp(screenX, screenY, button);
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		if (pointer != 0) return false;
		if (levels[currentLevel] != null && screenX < GameConstants.GameWidth){
			levels[currentLevel].touchDragged(screenX, screenY);
		}
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		if (levels[currentLevel] != null){
			levels[currentLevel].mouseMoved(screenX, screenY);
		}
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		if (!touchedDown){
			currentLevel = MathUtils.clamp(currentLevel + Math.abs(amount) / amount, 0, 6);

		}
		return false;
	}
}
