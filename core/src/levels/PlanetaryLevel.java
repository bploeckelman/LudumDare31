package levels;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld31.Assets;
import lando.systems.ld31.GameConstants;
import levels.planetary.Earth;
import levels.planetary.Moon;
import levels.planetary.Rocket;
import levels.planetary.RocketExplosion;

import java.util.ArrayList;


// TODO: cursor indicating if firing is possible (too close to earth, etc)

public class PlanetaryLevel extends GameLevel {

    public static final String TAG = "PlanetaryLevel";

    public static final float DAY_LENGTH = 2f;
    public static final int DIST_TO_MOON = 220;
    public static final int EARTH_RADIUS = 32;
    public static final int MOON_RADIUS = 17;
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
    private ArrayList<Rocket> rockets;
    private ArrayList<RocketExplosion> rocketExplosions;

    // -----------------------------------------------

    private Vector2 earthPos;
    private Vector2 moonPos = new Vector2();

    private float dayTimer = 0;


    // -----------------------------------------------------------------------------------------------------------------


    public PlanetaryLevel() {

        // Get the centers!
        earthPos = new Vector2(
                GameConstants.GameWidth / 2,
                GameConstants.GameHeight / 2
        );
        // This is also the zoom out point
        zoomOutPoint = earthPos.cpy();

        // Make the things!
        // Earth
        earth = new Earth(earthPos);

        // Moon
        // Where's the moon?
        moonPos.set(moonInitialPos.cpy().rotate(MOON_INITIAL_ORBIT_ANGLE).add(earthPos));
        moon = new Moon();

        // Explosions
        rockets = new ArrayList<Rocket>();
        rocketExplosions = new ArrayList<RocketExplosion>();

    }

    // -----------------------------------------------------------------------------------------------------------------


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
        float earthRotation = (dayTimer % 1f) * 360;
        earth.setRotation(earthRotation);

        // Moon Orbit Angle
        moonOrbitAngle = (((dayTimer / MOON_ORBIT_PERIOD) % 1) * 360 + MOON_INITIAL_ORBIT_ANGLE);
        moonPos.set(moonInitialPos.cpy().rotate(moonOrbitAngle).add(earthPos));
        moon.setPosition(moonPos);
        // Moon rotation
        float moonRotation = (moonOrbitAngle + MOON_TEXTURE_ROTATION) % 360;
        moon.setRotation(moonRotation);

        // Rockets -----------------
        for (int i = rockets.size() - 1; i >= 0; i--) {
            rockets.get(i).update(dt);
            if (rockets.get(i).isDestroyable()) {
                detonateRocket(rockets.get(i));
                rockets.remove(i);
            }
        }

        // Explosions! ----------------
        for (int i = rocketExplosions.size() - 1; i >= 0; i--) {
            rocketExplosions.get(i).update(dt);
            if (rocketExplosions.get(i).isDestroyable()) {
                rocketExplosions.remove(i);
            }
        }

    }

    @Override
    public void draw(SpriteBatch batch) {
        earth.draw(batch);
        moon.draw(batch);
        for (RocketExplosion rocketExplosion : rocketExplosions) {
            rocketExplosion.draw(batch);
        }
        for (Rocket rocket : rockets) {
            rocket.draw(batch);
        }

        batch.setColor(Color.RED);
        batch.draw(Assets.squareTex, earthPos.x, earthPos.y, 3, 3);
        batch.draw(Assets.squareTex, moonPos.x, moonPos.y, 3, 3);
        batch.setColor(Color.WHITE);

    }

    @Override
    public boolean touchDown(int screenX, int screenY, int button) {

        Vector2 gameTouchPos = getGamePos(new Vector2(screenX, screenY));

        // New explosions
//        Vector2 explosionPosition = getGamePos(new Vector2(screenX, screenY));
//        rocketExplosions.add(new RocketExplosion(explosionPosition));

        launchRocketFromEarth(gameTouchPos);
        launchRocketFromMoon(gameTouchPos);


//        rockets.add(new Rocket(earthPos.cpy(), getGamePos(new Vector2(screenX, screenY))));
//        rockets.add(new Rocket(moonPos.cpy(), getGamePos(new Vector2(screenX, screenY))));

        return true;
    }

    // -----------------------------------------------------------------------------------------------------------------

    private void detonateRocket(Rocket rocket) {
        // Explosion!
        rocketExplosions.add(new RocketExplosion(rocket.getTargetPos()));
    }
    private void launchRocket(Vector2 origin, Vector2 target) {
        // Add a launch explosion
        // Add the rocket
        rockets.add(new Rocket(origin, target));
    }
    private void launchRocketFromEarth(Vector2 target) {
        launchRocketFromHeavenlyBody(earthPos, EARTH_RADIUS, target);
    }
    private void launchRocketFromHeavenlyBody(Vector2 bodyPos, float bodyRadius, Vector2 target) {
        // Where's the surface of the planet?
        // Get the normalized vector to target.
        Vector2 dir = target.cpy().sub(bodyPos).nor();
        Vector2 origin = bodyPos.cpy().add(dir.scl(bodyRadius));
        // Fire le missile!
        launchRocket(origin, target);
    }
    private void launchRocketFromMoon(Vector2 target) {
        launchRocketFromHeavenlyBody(moonPos, MOON_RADIUS, target);
    }


}
