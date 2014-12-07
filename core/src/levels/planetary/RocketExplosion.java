package levels.planetary;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld31.Assets;
import lando.systems.ld31.GameObject;

public class RocketExplosion extends GameObject {

    public static final String TAG = "RocketExplosion";

    private final static float EXPLOSION_DURATION = 0.2f;
    private final static double EXPLOSION_EASING_POWER = 1;
    private final static float IMPLOSION_DURATION = 0.8f;
    private final static double IMPLOSION_EASING_POWER = 6;
    private final static float EXPLOSION_RADIUS = 28f;

    private Vector2 pos;
    private float timer;
    private Sprite explosion;
    private float explosionRadius;

    public boolean isComplete = false;

    public RocketExplosion(Vector2 position) {

        this.pos = position;
        this.timer = 0f;
        this.explosion = new Sprite(Assets.planetaryTempExplosion);
        this.explosionRadius = 0;

        this.explosion.setPosition(this.pos.x, this.pos.y);
    }

    @Override
    public void draw(SpriteBatch batch) {
        explosion.draw(batch);
    }

    public void update(float dt) {
        timer += dt;

        float percent = 0f;

        if (timer == 0) {
            // Do nothing
            return;

        } else if (timer <= EXPLOSION_DURATION) {
            // Scale percent
            percent = timer / EXPLOSION_DURATION;
            // Easing
            percent = (float) Math.pow(percent, EXPLOSION_EASING_POWER);

        } else if (timer <= EXPLOSION_DURATION + IMPLOSION_DURATION) {
            // Scale percent
            percent = (timer - EXPLOSION_DURATION) / IMPLOSION_DURATION;
            // Easing
            percent = (float) Math.pow((1 - percent), IMPLOSION_EASING_POWER);
            // What's the radius?
            explosionRadius = EXPLOSION_RADIUS * percent;

        } else {
            // We're done!
            isComplete = true;
        }

        explosionRadius = EXPLOSION_RADIUS * percent;
        explosion.setSize(explosionRadius * 2, explosionRadius * 2);
        explosion.setPosition(pos.x - explosionRadius, pos.y - explosionRadius);

    }

}
