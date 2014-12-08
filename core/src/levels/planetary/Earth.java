package levels.planetary;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld31.Assets;

public class Earth {


    /** The center position of the object */
    private Vector2 pos = new Vector2();
    /** Rotation of earth in degrees */
    private float r = 0f;

    private Sprite baseEarth;

    // -----------------------------------------------


    public Earth(Vector2 position) {

        baseEarth = new Sprite(Assets.planetaryTempEarth);
//        baseEarth.setSize(PlanetaryLevel.EARTH_RADIUS * 2, PlanetaryLevel.EARTH_RADIUS * 2);
        setPosition(position);
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
        baseEarth.setCenter(pos.x, pos.y);
    }
    public void setRotation(float rotation) {
        r = rotation;
        baseEarth.setRotation(r);
    }

}
