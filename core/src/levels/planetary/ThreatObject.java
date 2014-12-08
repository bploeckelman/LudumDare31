package levels.planetary;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public abstract class ThreatObject extends DestroyableObject {

    protected Vector2 position;
    protected float radius;
    protected float rotation;
    protected Sprite sprite;
    protected Vector2 trajectory;
    protected Vector2 scaledTrajectory;   // Prevents a new V2 from being created every update

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }
}
