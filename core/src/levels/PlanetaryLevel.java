package levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import levels.planetary.Earth;
import levels.planetary.Moon;


public class PlanetaryLevel extends GameLevel {

    public static final float DAY_LENGTH = 2f;
    public static final int DIST_TO_MOON = 220;
    public static final int EARTH_WIDTH = 64;
    public static final int EARTH_HEIGHT = 64;
    public static final int MOON_WIDTH = 34;
    public static final int MOON_HEIGHT = 34;
    public static final int MOON_INITIAL_ORBIT_ANGLE = 60;
    /** Rotation to apply to the moon's texture point the near side of the moon's surface downwards towards earth. */
    public static final int MOON_TEXTURE_ROTATION = 180;
    public static final float MOON_ORBIT_PERIOD = 27.322f; // Days

    // -----------------------------------------------


    /** Moon's rotation around the Earth starting from the top */
    private static float moonOrbitAngle;
    private static Vector2 moonInitialPos;

    static {
        moonOrbitAngle = MOON_INITIAL_ORBIT_ANGLE;
        moonInitialPos = new Vector2(0, DIST_TO_MOON);
    }

    // -----------------------------------------------


    private Earth earth;
    private Moon moon;

    // -----------------------------------------------

    private Vector2 earthPos;
    private float earthR;
    private Vector2 moonPos = new Vector2();
    private float moonR = 0f;


    private float dayTimer = 0;


    public PlanetaryLevel() {

        // Get the centers!
        earthPos = new Vector2(
                Math.round(Gdx.graphics.getWidth() / 2),
                Math.round(Gdx.graphics.getHeight() / 2)
        );

        // Make the things!
        // Earth
        earth = new Earth();
        // Set the position now.
        earth.setPosition(earthPos);


        // Moon
        // Where's the moon?
        moonPos.set(moonInitialPos.cpy().rotate(MOON_INITIAL_ORBIT_ANGLE).add(earthPos));
        moon = new Moon();

    }


    @Override
    public int hasThreat() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void handleInput(float dt) {

    }

    @Override
    public void update(float dt) {

        // Update the dayTimer
        dayTimer += dt / DAY_LENGTH;

        // Update the Earth
        earthR = (dayTimer % 1f) * 360;
        earth.setRotation(earthR);

        // Moon Orbit Angle
        moonOrbitAngle = (((dayTimer / MOON_ORBIT_PERIOD) % 1) * 360 + MOON_INITIAL_ORBIT_ANGLE);
        moonPos.set(moonInitialPos.cpy().rotate(moonOrbitAngle).add(earthPos));
        moon.setPosition(moonPos);
        // Moon rotation
        moonR = (moonOrbitAngle + MOON_TEXTURE_ROTATION) % 360;
        moon.setRotation(moonR);


    }

    @Override
    public void draw(SpriteBatch batch) {
        earth.draw(batch);
        moon.draw(batch);
    }



}
