package levels.planetary;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld31.Assets;

public class RocketExplosion extends DestroyableObject {

    public static final String TAG = "RocketExplosion";

    public static enum PHASE {
        EXPLODE, IMPLODE
    }

    private final static float EXPLOSION_DURATION = 0.2f;
    private final static double EXPLOSION_EASING_POWER = 1;
    private final static float IMPLOSION_DURATION = 0.8f;
    private final static double IMPLOSION_EASING_POWER = 6;
    private final static float EXPLOSION_RADIUS = 28f;

    private Vector2 pos;
    private float timer;
    private Sprite explosion;
    private float radius;
    private PHASE phase;

    public RocketExplosion(Vector2 position) {

        this.pos = position;
        this.timer = 0f;
        this.explosion = new Sprite(Assets.planetaryTempExplosion);
        this.radius = 0;
        this.phase = PHASE.EXPLODE;

        this.explosion.setPosition(this.pos.x, this.pos.y);

    }

    @Override
    public void draw(SpriteBatch batch) {
        explosion.draw(batch);
    }

    public PHASE getPhase() {
        return phase;
    }
    public Vector2 getPos() {
        return pos;
    }
    public float getRadius() {
        return radius;
    }
    public void update(float dt) {
        timer += dt;

        float percent = 0f;

        if (timer == 0) {
            // Do nothing
            return;

        } else if (timer <= EXPLOSION_DURATION) {
            // Set Phase
            phase = PHASE.EXPLODE;
            // Scale percent
            percent = timer / EXPLOSION_DURATION;
            // Easing
            percent = (float) Math.pow(percent, EXPLOSION_EASING_POWER);

        } else if (timer <= EXPLOSION_DURATION + IMPLOSION_DURATION) {
            // Set Phase
            phase = PHASE.IMPLODE;
            // Scale percent
            percent = (timer - EXPLOSION_DURATION) / IMPLOSION_DURATION;
            // Easing
            percent = (float) Math.pow((1 - percent), IMPLOSION_EASING_POWER);
            // What's the radius?
            radius = EXPLOSION_RADIUS * percent;

        } else {
            // We're done!
            setDestroyable(true);
        }

        radius = EXPLOSION_RADIUS * percent;
        explosion.setSize(radius * 2, radius * 2);
        explosion.setPosition(pos.x - radius, pos.y - radius);

    }

}
