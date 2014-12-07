package levels.planetary;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld31.Assets;
import levels.PlanetaryLevel;

public class Earth {


    /** The center position of the object */
    private Vector2 pos = new Vector2();
    /** Rotation of earth in degrees */
    private float r = 0f;

    private Sprite baseEarth;

    // -----------------------------------------------


    public Earth() {

        baseEarth = new Sprite(Assets.planetaryTempEarth, Assets.planetaryTempEarth.getWidth(), Assets.planetaryTempEarth.getHeight());
        baseEarth.setSize(PlanetaryLevel.EARTH_RADIUS * 2, PlanetaryLevel.EARTH_RADIUS * 2);
        baseEarth.setOriginCenter();

    }

    // -----------------------------------------------


    public void draw(SpriteBatch batch) {
        baseEarth.draw(batch);
    }

    public Vector2 getPosition() {
        return pos;
    }
    public float getRotation() {
        return r;
    }
    public void setPosition(Vector2 position) {
        pos.set(position);
        baseEarth.setPosition(
                pos.x - PlanetaryLevel.EARTH_RADIUS,
                pos.y - PlanetaryLevel.EARTH_RADIUS
        );
//        baseEarth.setX(pos.x - PlanetaryLevel.EARTH_RADIUS);
//        baseEarth.setY(pos.y - PlanetaryLevel.EARTH_RADIUS);
    }
    public void setRotation(float rotation) {
        r = rotation;
        baseEarth.setRotation(r);
    }

}
