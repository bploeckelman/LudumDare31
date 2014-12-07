package levels.intracellular;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld31.Assets;

public class Asteroid {
    public Sprite sprite;
    public Vector2 velocity;
    public Vector2 position;

    public Asteroid(float x, float y) {
        position = new Vector2(x, y);

        sprite = new Sprite(IntraCellularAssets.asteroid);
        sprite.setSize(sprite.getWidth() * 8, sprite.getHeight() * 8);
        sprite.setCenter(x, y);

        velocity = new Vector2(0, 1);
        velocity.setAngle(Assets.rand.nextInt(360));
        velocity.scl(Assets.rand.nextFloat() * 500);
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
        sprite.setCenter(x, position.y);
    }

    public float getY() {
        return position.y;
    }

    public void setY(float y) {
        position.y = y;
        sprite.setCenter(position.x, y);
    }
}
