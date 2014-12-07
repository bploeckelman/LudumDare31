package levels.intracellular;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld31.Assets;

public class Ship {
    public static float invulnerabilityTime = 2;

    public Sprite sprite;
    public Vector2 velocity;
    public Vector2 acceleration = new Vector2(0,1);
    public Vector2 position;
    public boolean isThrusting;
    public float invulnerabilityTimeLeft;


    public Ship(float x, float y) {
        sprite = new Sprite(IntraCellularAssets.ship);
        sprite.setPosition(x, y);
        sprite.setOriginCenter();

        velocity = new Vector2(0, 0);
        position = new Vector2(x, y);

        invulnerabilityTimeLeft = invulnerabilityTime;
    }

    public void setRotation(float d) {
        if(d < 0) {
            d = 360 + d;
        } else if(d > 359) {
            d = 360 - d;
        }
        sprite.setRotation(d);
    }

    public float getRotation() {
        return sprite.getRotation();
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

    public void reset(float x, float y) {
        setPosition(x, y);
        velocity.set(0, 0);
        invulnerabilityTimeLeft = invulnerabilityTime;
    }

    public void accelerate(float dt) {
        acceleration.nor();
        acceleration.setAngle(sprite.getRotation() + 90);
        acceleration.scl(dt * 500);
        velocity.add(acceleration);
        velocity.clamp(0, 500);
    }

    public void slowDown(float dt) {
        velocity.x = velocity.x * .99f;
        velocity.y = velocity.y * .99f;
    }

    public Bullet shoot() {
        return new Bullet(position.x, position.y, sprite.getRotation() + 90, velocity);
    }

    public void thrust(boolean t) {
        isThrusting = t;
        if(isThrusting) {
            sprite.setTexture(IntraCellularAssets.shipThrusting);
        } else {
            sprite.setTexture(IntraCellularAssets.ship);
        }
    }
}
