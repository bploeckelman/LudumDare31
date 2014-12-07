package levels.planetary;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld31.Assets;
import levels.PlanetaryLevel;

public class Moon {

    private static float hW;
    private static float hH;

    static {
        hW = PlanetaryLevel.MOON_WIDTH / 2;
        hH = PlanetaryLevel.MOON_HEIGHT / 2;
    }

    // -----------------------------------------------


    /** The center position of the object */
    private Vector2 pos = new Vector2();
    /** Rotation of earth in degrees */
    private float r = 0f;

    private Sprite baseMoon;

    // -----------------------------------------------


    public Moon() {

        baseMoon = new Sprite(Assets.planetaryTempEarth, Assets.planetaryTempEarth.getWidth(), Assets.planetaryTempEarth.getHeight());
        baseMoon.setSize(PlanetaryLevel.MOON_WIDTH, PlanetaryLevel.MOON_HEIGHT);
        baseMoon.setOriginCenter();

    }

    // -----------------------------------------------


    public void draw(SpriteBatch batch) {
        baseMoon.draw(batch);
    }

    public Vector2 getPosition() {
        return pos;
    }
    public float getRotation() {
        return r;
    }
    public void setPosition(Vector2 position) {
        pos.set(position);
        baseMoon.setX(pos.x - hW);
        baseMoon.setY(pos.y - hH);
    }
    public void setRotation(float rotation) {
        r = rotation;
        baseMoon.setRotation(r);
    }

}
