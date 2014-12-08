package levels.planetary;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld31.Assets;

public class Rocket extends DestroyableObject {

    private static final String TAG = "Rocket";
    public static final float LENGTH = 28f;
    public static final float ACCELERATION = 512f; // per second
    public static final float TOP_SPEED = 1024f;


    private Vector2 pos;
    private Vector2 targetPos;
    private float distanceTraveled;
    private float totalDistanceToTarget;
    private float speed;
    private Vector2 dir;

    private static float hW = 0;
//    private float rotation

    private Sprite rocket;

    public Rocket(Vector2 origin, Vector2 target) {

        // Positions
        pos = origin.cpy();
        targetPos = target.cpy();
        distanceTraveled = 0f;
        totalDistanceToTarget = origin.dst(target) - LENGTH;
        speed = 0;
        dir = target.cpy().sub(origin).nor();

        // Build, size the rocket sprite
        rocket = new Sprite(Assets.planetaryTempRocket);
        float per = LENGTH / rocket.getHeight();
        rocket.setSize(rocket.getWidth() * per, rocket.getHeight() * per);
        if (hW == 0) {
            hW = rocket.getWidth() / 2;
        }
        rocket.setOrigin(hW, 0);

        // What's the angle?
        // This angle is clockwise from the +X.
        float angle = dir.angle() - 90;
//        Gdx.app.log(TAG, "s='"+startingPosition+"',t='"+targetPosition+"', dir='"+dir+"', a='"+angle+"'");
        rocket.setRotation(angle);
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (!destroyable) {
            rocket.draw(batch);
        }
    }

    public Vector2 getTargetPos() {
        return targetPos;
    }

    public void update(float dt) {

        speed = Math.min(speed + (ACCELERATION * dt), TOP_SPEED);
        float dist = speed * dt;
        distanceTraveled += dist;
        if (distanceTraveled >= totalDistanceToTarget) {
            setDestroyable(true);
            return;
        }
        pos.add(dir.cpy().scl(dist));
        rocket.setPosition(pos.x - hW, pos.y);
    }
}
