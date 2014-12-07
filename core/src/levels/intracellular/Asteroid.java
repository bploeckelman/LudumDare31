package levels.intracellular;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld31.Assets;

import java.util.List;

public class Asteroid {
    public enum Size {
        SMALL (32, null),
        MEDIUM (64, SMALL),
        LARGE (128, MEDIUM);

        public final int value;
        public final Size splitInto;
        Size(int value, Size splitInto) {
            this.value = value;
            this.splitInto = splitInto;
        }
    }

    public Sprite sprite;
    public Vector2 velocity;
    public Vector2 position;
    public Size size;

    public Asteroid(float x, float y, Size size, Vector2 velocity) {
        this.size = size;

        position = new Vector2(x, y);

        sprite = new Sprite(IntraCellularAssets.asteroid);
        sprite.setSize(size.value, size.value);
        sprite.setCenter(x, y);

        this.velocity = velocity;
    }

    public Asteroid(float x, float y) {
        this(x, y, Size.values()[Assets.rand.nextInt(3)],
                (new Vector2(0, 1)).setAngle(Assets.rand.nextInt(360)).scl(Assets.rand.nextFloat() * 300));
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

    public void split(List asteroids, Vector2 newVelocity) {
        asteroids.remove(this);

        if(size.splitInto != null) {
            asteroids.add(new Asteroid(position.x, position.y, size.splitInto, newVelocity.cpy().nor().rotate(90).scl(velocity)));
            asteroids.add(new Asteroid(position.x, position.y, size.splitInto, newVelocity.cpy().nor().rotate(-90).scl(velocity)));
        }
    }
}
