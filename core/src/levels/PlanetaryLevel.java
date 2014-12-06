package levels;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import levels.planetary.Earth;


public class PlanetaryLevel extends GameLevel {

    private Earth earth;


    public PlanetaryLevel() {
        earth = new Earth(50, 30, 0);
    }


    @Override
    public boolean hasThreat() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void handleInput(float dt) {

    }

    @Override
    public void update(float dt) {
        // TODO Auto-generated method stub

    }

    @Override
    public void draw(SpriteBatch batch) {
        earth.draw(batch);
    }

}
