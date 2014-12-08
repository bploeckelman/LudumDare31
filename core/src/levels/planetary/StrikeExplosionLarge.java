package levels.planetary;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld31.Assets;

public class StrikeExplosionLarge extends StrikeExplosion {

    private static final float DURATION = 4;
    private static final float SIZE = 78;
    private static final float SPRITE_CENTER_PERCENT_X = 0.5f;
    private static final float SPRITE_CENTER_PERCENT_Y = 0.5f;

    private static Vector2 spriteCenterOffset = new Vector2(SIZE * SPRITE_CENTER_PERCENT_X, SIZE * SPRITE_CENTER_PERCENT_Y);

    // -----------------------------------------------------------------

    public StrikeExplosionLarge(Vector2 bodyPosition, float bodyRotation, float rotationRelativeToBody, float bodyRadius) {

        // Record
        this.rotationRelativeToBody = rotationRelativeToBody;
        this.bodyRadius = bodyRadius;

        // Create the animation
        this.animation = new Animation(
                DURATION / Assets.plLargeStrikeExplosion.getRegions().size,
                Assets.plLargeStrikeExplosion.getRegions());
        this.animation.setPlayMode(Animation.PlayMode.NORMAL);

        // Create our sprite
        this.sprite = new Sprite(this.animation.getKeyFrame(0));
        this.sprite.setSize(SIZE, SIZE);

        // Where should our sprite be?
        updateBodyPosition(bodyPosition);
        updateBodyRotation(bodyRotation);

    }

    // ------------------------------------------------------------------------------

    @Override
    public void update(float dt) { update(dt, DURATION); }

    @Override
    public void updateBodyPosition(Vector2 bodyPosition) { updateBodyPosition(bodyPosition, spriteCenterOffset); }

}
