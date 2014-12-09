package levels;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld31.Assets;
import lando.systems.ld31.GameConstants;
import lando.systems.ld31.Score;
import lando.systems.ld31.TransitionManager;
import levels.planetary.*;

import java.util.ArrayList;


// TODO: cursor indicating if firing is possible (too close to earth, etc)

public class PlanetaryLevel extends GameLevel {

    public static final String TAG = "PlanetaryLevel";

    public static final float ASTEROID_RADIUS_MIN = 12f;
    public static final float ASTEROID_RADIUS_MAX = 32f;
    public static final float ASTEROID_RADIUS_POW = 2f; // Higher values make large less common
    public static final float ASTEROID_ROTATIONAL_VELOCITY_MAX = 256f;
    public static final float ASTEROID_SPEED_MIN = 32f;
    public static final float ASTEROID_SPEED_MAX = 82f;
    public static final float ASTEROID_SPEED_POW = 2.5f; // Higher makes fast less likely
    public static final float ASTEROID_TRAJECTORY_SPREAD = 36f; // max angle off-target to start (either dir)
    public static final float ASTEROID_TRAJECTORY_SPREAD_POW = 1.8f; // Higher values make aiming more accurate
    public static final int ASTEROID_MAX_COUNT = 14;
    public static final float DAY_LENGTH = 4f;
    public static final int DIST_TO_MOON = 220;
    public static final int EARTH_RADIUS = 32;
    public static final float EARTH_HEALTH_REGEN = 0.01f;
    public static final float MOON_HEALTH_REGEN = 0.02f;
    public static final int MOON_RADIUS = 17;
    public static final int MOON_INITIAL_ORBIT_ANGLE = 60;
    public static final float HEALTH_REQUIRED_TO_LAUNCH = 0.2f;
    /** Rotation to apply to the moon's texture point the near side of the moon's surface downwards towards earth. */
    public static final int MOON_TEXTURE_ROTATION = 0;
    public static final float MOON_ORBIT_PERIOD = 27.322f; // Days
    public static final float BACKGROUND_SHIFT_DIST = 64f; // Max number of pixels the bg can shift from center
    public static final float BACKGROUND_SHIFT_RANGE = 300f; // dist from center to get max shift effect
    public static final float BACKGROUND_SHIFT_POWER = 300f; // dist from center to get max shift effect

    public static final float EARTH_LAUNCH_COOLDOWN = 0.8f;
    public static final float MOON_LAUNCH_COOLDOWN = 0.4f;

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

    // RocketExplosion collections, sorted by phase.  Updated with each call to updateRocketExplosions
    private ArrayList<RocketExplosion> rocketExplosionsExploding = new ArrayList<RocketExplosion>();
    private ArrayList<RocketExplosion> rocketExplosionsImploding = new ArrayList<RocketExplosion>();

    // -----------------------------------------------

    private Vector2 tempV2; // To be used and forgotten.
    private Vector2 backgroundPos;
    private Vector2 centerPos;
    private Vector2 earthPos;
    private Vector2 moonPos;
    private float dayTimer = 0;
    private float levelTimer = 60f;

    private float earthLaunchCountdown = 0;
    private float moonLaunchCountdown = 0;


    private Sprite background;

    private int threatLevel = 0;

    // -----------------------------------------------------------------------------------------------------------------


    public PlanetaryLevel() {
    	tutorialText = "Holy asteroids, Batman!\nSo that's why the @!#?@! power lines keep breaking.\n\nWhat's a bar owner to do?\nDestroy the asteroids.\n\nAnd don't forget about your patrons.";

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
        moon = new Moon(moonPos);

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

        // Set the initial cooldown countdown to create a shower
        showerCooldownCountdown = 1; //SHOWER_COOLDOWN + SHOWER_COOLDOWN_DEVIATION - (SHOWER_DURATION_DEVIATION * 2 * Assets.rand.nextFloat());

    }

    // -----------------------------------------------------------------------------------------------------------------


    @Override
    public int hasThreat() {
        // TODO Auto-generated method stub
        return threatLevel;
    }

    @Override
    public void handleInput(float dt) {

    }

    @Override
    public void update(float dt) {

        // Timers ---------------------------
    	levelTimer -= dt;
    	if (levelTimer < 0){
    		TransitionManager.Instance.defendVirii();
    		levelTimer = 10000;
    	}
        // Update the dayTimer
        dayTimer += dt / DAY_LENGTH;
        // Cooldown timers
        earthLaunchCountdown -= dt;
        moonLaunchCountdown -= dt;


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


        updateHeavenlyBodies(dt);
        updateRockets(dt);
        updateRocketExplosions(dt);
        updateAsteroids(dt);
        updateShower(dt);

        // Set the threat level
        double threatPercent = (double) asteroids.size() / ASTEROID_MAX_COUNT;
        threatLevel = (int) Math.min(Math.ceil(4 * threatPercent), 3);

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

        if (tutorialText != null) {
            return false;
        }

        Vector2 gameTouchPos = getGamePos(new Vector2(screenX, screenY));

        if (earth.getHealthPercent() >= HEALTH_REQUIRED_TO_LAUNCH) {
            launchRocketFromEarth(gameTouchPos);
        }
        if (moon.getHealthPercent() >= HEALTH_REQUIRED_TO_LAUNCH) {
            launchRocketFromMoon(gameTouchPos);
        }

        return true;
    }

    // -----------------------------------------------------------------------------------------------------------------

    private boolean checkForCircleCollision(Vector2 v1, float r1, Vector2 v2, float r2) {
        // Quick check for miss
        if (Math.abs(v1.x-v2.x) > r1+r2 && Math.abs(v1.y-v2.y) > r1+r2) {
            return false;
        } else {
            // Let's actually compute the distance
            return v1.dst(v2) <= r1 + r2;
        }
    }

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
        if (earthLaunchCountdown <= 0) {
            launchRocketFromHeavenlyBody(earthPos, EARTH_RADIUS, target);
            earthLaunchCountdown = EARTH_LAUNCH_COOLDOWN;
        }
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
        if (moonLaunchCountdown <= 0) {
            launchRocketFromHeavenlyBody(moonPos, MOON_RADIUS, target);
            moonLaunchCountdown = MOON_LAUNCH_COOLDOWN;
        }
    }

    private void processEarthCollision(Asteroid asteroid) {
        Vector2 strikePos = earthPos.cpy().sub(asteroid.getPosition()).nor().scl(EARTH_RADIUS);

    }

    /**
     * Update the Asteroids
     * @param dt
     */
    private void updateAsteroids(float dt) {
        Asteroid thisAstroid;
        // Reverse for loop, as we might be removing some
        astroidLoop: for (int i = asteroids.size() - 1; i >= 0; i--) {
            thisAstroid = asteroids.get(i);
            // Update
            thisAstroid.update(dt);
            // Remove/process destroyed
            if (thisAstroid.isDestroyable()) {
                asteroids.remove(i);
                continue astroidLoop;
            }
            // Check for explosion collision
            for (RocketExplosion rocketExplosion : rocketExplosionsExploding) {
                if (checkForCircleCollision(
                        rocketExplosion.getPos(), rocketExplosion.getRadius(),
                        thisAstroid.getPosition(), thisAstroid.getRadius())) {
                    // Collision! Destroy it.
                    Score.AsteroidsDestroyed++;
                    asteroids.remove(i);
                    continue astroidLoop;
                }
            }
            // Check for Earth collision
            if (checkForCircleCollision(
                    thisAstroid.getPosition(), thisAstroid.getRadius(),
                    earthPos, EARTH_RADIUS)) {
                // There's been a collision
                earth.processThreatObjectCollision(thisAstroid);
                // Remove the astroid
                asteroids.remove(i);
                continue astroidLoop;
            }
            // Check for Moon collision
            if (checkForCircleCollision(
                    thisAstroid.getPosition(), thisAstroid.getRadius(),
                    moonPos, MOON_RADIUS)) {
                // There's been a collision
                moon.processThreatObjectCollision(thisAstroid);
                // Remove the astroid
                asteroids.remove(i);
                continue astroidLoop;
            }

        }
    }
    /**
     * Updates the Earth and the Moon
     * • Position/Rotation
     * @param dt
     */
    private void updateHeavenlyBodies(float dt) {

        // Update the Earth
        float earthRotation = (dayTimer % 1f) * 360;
        earth.setRotation(earthRotation);
        earth.update(dt);

        // Moon Orbit Angle
        moonOrbitAngle = (((dayTimer / MOON_ORBIT_PERIOD) % 1) * 360 + MOON_INITIAL_ORBIT_ANGLE);
        moonPos.set(moonInitialPos.cpy().rotate(moonOrbitAngle).add(earthPos));
        moon.setPosition(moonPos);
        // Moon rotation
        float moonRotation = (moonOrbitAngle + MOON_TEXTURE_ROTATION) % 360;
        moon.setRotation(moonRotation);
        // Moon update
        moon.update(dt);

    }

    /**
     *
     * @param dt
     */
    private void updateRocketExplosions(float dt) {
        // Clear the phase tracking arrays
        rocketExplosionsExploding.clear();
        rocketExplosionsImploding.clear();
        // Foreach, backwards for removal reasons
        RocketExplosion thisRocketExplosion;
        for (int i = rocketExplosions.size() - 1; i >= 0; i--) {
            thisRocketExplosion = rocketExplosions.get(i);
            // Update it
            thisRocketExplosion.update(dt);
            // Check for destruction
            if (thisRocketExplosion.isDestroyable()) {
                // Remove it
                rocketExplosions.remove(i);
                continue;
            }
            // Sort it into the phase tracking arrays
            if (thisRocketExplosion.getPhase() == RocketExplosion.PHASE.EXPLODE) {
                rocketExplosionsExploding.add(thisRocketExplosion);
            }
            if (thisRocketExplosion.getPhase() == RocketExplosion.PHASE.IMPLODE) {
                rocketExplosionsImploding.add(thisRocketExplosion);
            }
        }
    }

    /**
     *
     * @param dt
     */
    private void updateRockets(float dt) {
        for (int i = rockets.size() - 1; i >= 0; i--) {
            rockets.get(i).update(dt);
            if (rockets.get(i).isDestroyable()) {
                detonateRocket(rockets.get(i));
                rockets.remove(i);
            }
        }
    }

    //----------------------------------------------------------------------------------------------------------

    private static final float SHOWER_COOLDOWN = 28;
    private static final float SHOWER_COOLDOWN_DEVIATION = 2;
    private static final float SHOWER_DURATION = 15f;
    private static final float SHOWER_DURATION_DEVIATION = 1f;

    private float showerCooldownCountdown = 0;
    private float showerDurationCountdown = 0;

    private void updateShower(float dt) {
        // Update the countdowns
        showerCooldownCountdown -= dt;
        showerDurationCountdown -= dt;

        // Have we reached the end of a cooldown?
        if (showerCooldownCountdown <= 0) {
            startShower();
        }

        // Are we in a shower?
        if (showerDurationCountdown > 0) {
            // Yes.
            if (asteroids.size() < ASTEROID_MAX_COUNT) {
                if (Assets.rand.nextFloat() < (1/60f)) {
                    generateAsteroid();
                }
            }
        }
    }
    private void startShower() {
        // Reset the cooldown
        showerCooldownCountdown = SHOWER_COOLDOWN + SHOWER_COOLDOWN_DEVIATION - (SHOWER_DURATION_DEVIATION * 2 * Assets.rand.nextFloat());
        // Set a new duration
        showerDurationCountdown = SHOWER_DURATION + SHOWER_DURATION_DEVIATION - (SHOWER_DURATION_DEVIATION * 2 * Assets.rand.nextFloat());
    }


}
