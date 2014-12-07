package levels.intracellular;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld31.Assets;

public class Bullet {
    public static float fireRate = 1/10;
    public static float fireDuration = 1;

    public Sprite sprite;
    public Vector2 origin;
    public float alive;
    public Vector2 velocity;
    public Vector2 position;

    public Bullet(float x, float y, float d) {
        origin = new Vector2(x, y);
        position = new Vector2(x, y);
        alive = 0;

        sprite = new Sprite(IntraCellularAssets.bullet);
        sprite.setCenter(x, y);

        velocity = new Vector2(0, 1);
        velocity.setAngle(d);
        velocity.scl(500);
    }

    public void setPosition(float x, float y) {
        position.set(x, y);
        sprite.setCenter(x, y);
    }

    public float getX() {
        return position.x;
    }

    public void setX(float x) {
        position.x = x;
        origin.x = x;
        sprite.setCenter(x, position.y);
    }

    public float getY() {
        return position.y;
    }

    public void setY(float y) {
        position.y = y;
        origin.y = y;
        sprite.setCenter(position.x, y);
    }
}
