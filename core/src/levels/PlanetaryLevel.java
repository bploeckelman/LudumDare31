package levels;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld31.Assets;
import lando.systems.ld31.GameConstants;
import levels.planetary.*;

import java.util.ArrayList;


// TODO: cursor indicating if firing is possible (too close to earth, etc)

public class PlanetaryLevel extends GameLevel {

    public static final String TAG = "PlanetaryLevel";

    public static final float ASTEROID_RADIUS_MIN = 8f;
    public static final float ASTEROID_RADIUS_MAX = 24f;
    public static final float ASTEROID_RADIUS_POW = 1.6f; // Higher values make large less common
    public static final float ASTEROID_ROTATIONAL_VELOCITY_MAX = 256f;
    public static final float ASTEROID_SPEED_MIN = 16f;
    public static final float ASTEROID_SPEED_MAX = 128f;
    public static final float ASTEROID_SPEED_POW = 2; // Higher makes fast less likely
    public static final float ASTEROID_TRAJECTORY_SPREAD = 4f; // max angle off-target to start (either dir)
    public static final float ASTEROID_TRAJECTORY_SPREAD_POW = 2.2f; // Higher values make aiming more accurate
    public static final float DAY_LENGTH = 4f;
    public static final int DIST_TO_MOON = 220;
    public static final int EARTH_RADIUS = 32;
    public static final int MOON_RADIUS = 17;
    public static final int MOON_INITIAL_ORBIT_ANGLE = 60;
    /** Rotation to apply to the moon's texture point the near side of the moon's surface downwards towards earth. */
    public static final int MOON_TEXTURE_ROTATION = 0;
    public static final float MOON_ORBIT_PERIOD = 27.322f; // Days
    public static final float BACKGROUND_SHIFT_DIST = 64f; // Max number of pixels the bg can shift from center
    public static final float BACKGROUND_SHIFT_RANGE = 300f; // dist from center to get max shift effect
    public static final float BACKGROUND_SHIFT_POWER = 300f; // dist from center to get max shift effect
    public static enum EDGE {
        NORTH, SOUTH, EAST, WEST
    }


    // -----------------------------------------------


    /** Moon's rotation around the Earth starting from the top */
    private static float moonOrbitAngle;
    private static Vector2 moonInitialPos;

    static {
        moonOrbitAngle = MOON_INITIAL_ORBIT_ANGLE;
        moonInitialPos = new Vector2(0, DIST_TO_MOON);
    }

    // -----------------------------------------------


    private ArrayList<Asteroid> asteroids;
    private Earth earth;
    private Moon moon;
    private ArrayList<Rocket> rockets;
    private ArrayList<RocketExplosion> rocketExplosions;

    // -----------------------------------------------

    private Vector2 tempV2; // To be used and forgotten.
    private Vector2 backgroundPos;
    private Vector2 centerPos;
    private Vector2 earthPos;
    private Vector2 moonPos;
    private float dayTimer = 0;

    private Sprite background;


    // -----------------------------------------------------------------------------------------------------------------


    public PlanetaryLevel() {

        tempV2 = new Vector2();

        // Get the centers!
        centerPos = new Vector2(
                GameConstants.GameWidth / 2,
                GameConstants.GameHeight / 2
        );
        earthPos = centerPos.cpy();
        // This is also the zoom out point
        zoomOutPoint = earthPos.cpy();

        // Make the things!
        // Earth
        earth = new Earth(earthPos);

        // Moon
        // Where's the moon?
        moonPos = new Vector2();
        moonPos.set(moonInitialPos.cpy().rotate(MOON_INITIAL_ORBIT_ANGLE).add(earthPos));
        moon = new Moon();

        // Lists
        asteroids = new ArrayList<Asteroid>();
        rockets = new ArrayList<Rocket>();
        rocketExplosions = new ArrayList<RocketExplosion>();

        // Background
        backgroundPos = new Vector2();
        background = new Sprite(Assets.plStarBackdrop);
        float bgMinWidth = GameConstants.GameWidth + (BACKGROUND_SHIFT_DIST * 2);
        float bgMinHeight = GameConstants.GameHeight + (BACKGROUND_SHIFT_DIST * 2);
        float bgScale = Math.max(bgMinWidth / background.getWidth(), bgMinHeight / background.getHeight());
        background.setSize(background.getWidth() * bgScale, background.getHeight() * bgScale);
        background.setCenter(centerPos.x, centerPos.y);

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

        // Update the background
//        tempV2.set(
//                MathUtils.clamp(Gdx.input.getX(), 0, GameConstants.ScreenWidth),
//                MathUtils.clamp(Gdx.input.getY(), 0, GameConstants.ScreenHeight)
//        ); // Where's the mouse?
//        Gdx.app.log(TAG, "mousePos='" + tempV2 + "'");
//        tempV2.set(getGamePos(tempV2)); // Where's that in the game world?
//        Gdx.app.log(TAG, "gamePos='" + tempV2 + "'");
//        tempV2.sub(centerPos); // Distance vector from the center
//        Gdx.app.log(TAG, "toCenter='" + tempV2 + "'");
//        float bgDist = tempV2.len();
//        float bgShiftPer = Math.max(1, Math.abs(bgDist / BACKGROUND_SHIFT_RANGE));
//        bgShiftPer = 1f - (float)Math.pow(bgShiftPer - 1, BACKGROUND_SHIFT_POWER);
//        float bgShiftDist = BACKGROUND_SHIFT_DIST * bgShiftPer;
//        tempV2.nor();
//        tempV2.scl(-bgShiftDist);
//        backgroundPos.set(centerPos.cpy().add(tempV2));
//        background.setCenter(backgroundPos.x, backgroundPos.y);


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

        // Asteroids -----------------
        for (int i = asteroids.size() - 1; i >= 0; i--) {
            asteroids.get(i).update(dt);
            if (asteroids.get(i).isDestroyable()) {
                asteroids.remove(i);
            }
        }
        if (asteroids.size() < 11) {
            generateAsteroid();
        }

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
        background.draw(batch);
        earth.draw(batch);
        moon.draw(batch);
        for (Asteroid asteroid : asteroids) {
            asteroid.draw(batch);
        }
        for (RocketExplosion rocketExplosion : rocketExplosions) {
            rocketExplosion.draw(batch);
        }
        for (Rocket rocket : rockets) {
            rocket.draw(batch);
        }
//        batch.setColor(Color.RED);
//        batch.draw(Assets.squareTex, earthPos.x, earthPos.y, 3, 3);
//        batch.draw(Assets.squareTex, moonPos.x, moonPos.y, 3, 3);
//        batch.setColor(Color.WHITE);

    }

    @Override
    public boolean touchDown(int screenX, int screenY, int button) {

        Vector2 gameTouchPos = getGamePos(new Vector2(screenX, screenY));

        launchRocketFromEarth(gameTouchPos);
        launchRocketFromMoon(gameTouchPos);

        return true;
    }

    // -----------------------------------------------------------------------------------------------------------------

    private void detonateRocket(Rocket rocket) {
        // Explosion!
        rocketExplosions.add(new RocketExplosion(rocket.getTargetPos()));
    }

    private void generateAsteroid() {

        // Select a radius
        float radius = (ASTEROID_RADIUS_MAX - ASTEROID_RADIUS_MIN) *
                (float) Math.pow(Assets.rand.nextFloat(), ASTEROID_RADIUS_POW) +
                ASTEROID_RADIUS_MIN;

        // Pick a threat direction
        Vector2 threatV2 = new Vector2(0f, 1f);
        float threatDirection = Assets.rand.nextFloat() * 360;
        threatV2.rotate(threatDirection);
        EDGE threatEdge;
        // Extend it to the edge; let's work with the largest axis
        if (Math.abs(threatV2.x) > Math.abs(threatV2.y)) {
            // X is bigger.  Extend to width.
            threatV2.scl(centerPos.x / Math.abs(threatV2.x));
            // Check for over-expansion and correct
            if (Math.abs(threatV2.y) > centerPos.y) {
                threatV2.scl(centerPos.y / Math.abs(threatV2.y));
                threatEdge = centerPos.y > 0 ? EDGE.EAST : EDGE.WEST;
            } else {
                threatEdge = centerPos.x > 0 ? EDGE.NORTH : EDGE.SOUTH;
            }
        } else {
            // Y is bigger.  Extend to height.
            threatV2.scl(centerPos.y / Math.abs(threatV2.y));
            // Check for over-expansion and correct
            if (Math.abs(threatV2.x) > centerPos.x) {
                threatV2.scl(centerPos.x / Math.abs(threatV2.x));
                threatEdge = centerPos.x > 0 ? EDGE.NORTH : EDGE.SOUTH;
            } else {
                threatEdge = centerPos.y > 0 ? EDGE.EAST : EDGE.WEST;
            }
        }
        // Add the center position to get the edge coordinate
        threatV2.add(centerPos);
        // Move the coordinate away from the edge by the radius
        switch(threatEdge) {
            case NORTH:
                threatV2.add(0, radius);
                break;
            case EAST:
                threatV2.add(radius, 0);
                break;
            case SOUTH:
                threatV2.add(0, -radius);
                break;
            case WEST:
            default:
                threatV2.add(-radius, 0);
                break;
        }

        // Cool.  Now, which way is it traveling?
        // Start by getting a norm pointing at earth.
        Vector2 trajectory = centerPos.cpy().sub(threatV2).nor();
        // Adjust the aim.
        float spreadPercent = Assets.rand.nextFloat();
        spreadPercent = (float) Math.pow(spreadPercent, ASTEROID_TRAJECTORY_SPREAD_POW);
        spreadPercent *= Assets.rand.nextFloat() < 0.5 ? -1 : 1;
        trajectory.rotate(ASTEROID_TRAJECTORY_SPREAD * spreadPercent);
        // How fast?
        float speed =  (ASTEROID_SPEED_MAX - ASTEROID_SPEED_MIN) *
                (float) Math.pow(Assets.rand.nextFloat(), ASTEROID_SPEED_POW) +
                ASTEROID_SPEED_MIN;
        // Apply the speed to the trajectory
        trajectory.scl(speed);

        // Rotational velocity
        float rotationalVelocity = (float) Math.pow(Assets.rand.nextFloat(), 2) * ASTEROID_ROTATIONAL_VELOCITY_MAX;
        rotationalVelocity *= Assets.rand.nextFloat() < 0.5 ? -1 : 1;

        // Make the thing!
        asteroids.add(new Asteroid(threatV2, trajectory, radius, rotationalVelocity));

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
