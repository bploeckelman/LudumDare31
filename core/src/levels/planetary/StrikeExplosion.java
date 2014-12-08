package levels.planetary;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class StrikeExplosion extends DestroyableObject {

    protected Vector2 position = new Vector2();
    protected float rotationRelativeToBody;
    protected float rotation;
    protected float bodyRadius;
    protected Sprite sprite;
    protected Animation animation;
    protected float timer = 0;
    protected Vector2 _tempV2 = new Vector2();

    // -----------------------------------------------------------------------------------------------------------------

    public float getRotation() {
        return rotation;
    }
    @Override
    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position.set(position);
    }

    public abstract void update(float dt);
    public void update(float dt, float duration) {
        timer += dt;
        if (timer > duration) {
            destroyable = true;
            return;
        }
        sprite.setRegion(animation.getKeyFrame(timer));
        // Other stuff.
    }


    public abstract void updateBodyPosition(Vector2 bodyPosition);
    public void updateBodyPosition(Vector2 bodyPosition, Vector2 spriteCenterOffset) {
        position.set(bodyPosition).add(0, this.bodyRadius).sub(spriteCenterOffset);
        this.sprite.setPosition(position.x, position.y);
        // We'll want all rotates to go around the body.
        _tempV2.set(bodyPosition).sub(position);
        this.sprite.setOrigin(_tempV2.x, _tempV2.y);
    }

    public void updateBodyRotation(float bodyRotation) {
        rotation = bodyRotation - rotationRelativeToBody;
        sprite.setRotation(rotation);
    }

}
