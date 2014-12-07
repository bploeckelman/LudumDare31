package lando.systems.ld31;

import levels.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LevelManager {

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
    }

    public void setLevel(int index){
        currentLevel = index;
        //TODO make a way to transition these
    }

    public void update(float dt){
        for (int i = 0; i < levels.length; i++){
            if (levels[i] == null) continue;
            levels[i].update(dt);
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
}
