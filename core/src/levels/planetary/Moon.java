package levels.planetary;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld31.Assets;
import levels.PlanetaryLevel;

import java.util.ArrayList;

public class Moon {

    /** The center position of the object */
    private Vector2 pos = new Vector2();
    /** Rotation of earth in degrees */
    private float r = 0f;

    private Sprite baseMoon;
    private ArrayList<StrikeExplosion> strikeExplosions;
    private Vector2 _tempV2 = new Vector2();

    private HealthBar healthBar;
    private float healthPercent = 1;

    // -----------------------------------------------


    public Moon(Vector2 position) {

        strikeExplosions = new ArrayList<StrikeExplosion>();

        healthBar = new HealthBar(position, PlanetaryLevel.MOON_RADIUS * 2 - 18, 8, healthPercent);

        baseMoon = new Sprite(Assets.plMoon);
        baseMoon.setSize(PlanetaryLevel.MOON_RADIUS * 2, PlanetaryLevel.MOON_RADIUS * 2);
        baseMoon.setOriginCenter();

    }

    // -----------------------------------------------


    public void draw(SpriteBatch batch) {

        baseMoon.draw(batch);

        // Draw the health bar
        healthBar.draw(batch);
        // Draw the explosions
        for (StrikeExplosion sE : strikeExplosions) {
            sE.draw(batch);
        }
    }

//    public Vector2 getPosition() {
//        return pos;
//    }
//    public float getRotation() {
//        return r;
//    }

    public float getHealthPercent() { return healthPercent; }

    public void setPosition(Vector2 position) {
        pos.set(position);
        updatePositions();
    }
    public void setRotation(float rotation) {
        r = rotation;
        updateRotations();
    }

    public void update(float dt) {
        // Heal
        healthPercent = Math.min(1, healthPercent+(PlanetaryLevel.MOON_HEALTH_REGEN * dt));
        healthBar.setHealthPercent(healthPercent);
        updateStrikeExplosions(dt);
    }

    // -------------------------------------------------------------------------

    public void processThreatObjectCollision(ThreatObject threatObject) {
        // Strike position, relative to earth's center
        Vector2 earthStrikePosition = _tempV2.set(threatObject.getPosition()).sub(pos).nor().scl(PlanetaryLevel.EARTH_RADIUS);
        // What's the angle of that position *relative to earth's current rotation*?
        float earthRelativeRotation = r - earthStrikePosition.angle() + 90;
        // Create the new StrikeExplosion
        // Todo: small or large?
        strikeExplosions.add(new StrikeExplosionSmall(pos, r, earthRelativeRotation, PlanetaryLevel.EARTH_RADIUS));

        // Damage.
        healthPercent = Math.max(0, healthPercent - 1f);
        healthBar.setHealthPercent(healthPercent);
    }

    private void updatePositions() {
        baseMoon.setCenter(pos.x, pos.y);
        for (StrikeExplosion sE : strikeExplosions) {
            sE.updateBodyPosition(pos);
        }
        healthBar.setPosition(pos);
    }

    private void updateRotations() {
        baseMoon.setRotation(r);
        // Rotating the Moon also updates the StrikeExplosions
        for (StrikeExplosion sE : strikeExplosions) {
            sE.updateBodyRotation(r);
        }
    }

    private void updateStrikeExplosions(float dt) {
        StrikeExplosion thisStrikeExplosion;
        for (int i = strikeExplosions.size() - 1; i >= 0; i--) {
            thisStrikeExplosion = strikeExplosions.get(i);
            thisStrikeExplosion.update(dt);
            if (thisStrikeExplosion.isDestroyable()) {
                // Remove it.
                strikeExplosions.remove(i);
            }
        }
    }

}
