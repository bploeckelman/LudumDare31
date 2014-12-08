package levels.planetary;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld31.Assets;
import lando.systems.ld31.GameConstants;

/**
 * Author: Ian McNamara <ian.mcnamara@wisc.edu>
 * Teaching and Research Application Development
 * Copyright 2014 Board of Regents of the University of Wisconsin System
 */
public class Asteroid extends ThreatObject {

    private static final String TAG = "Asteroid";

    private static final float STAGING_TIME = 0;


    private float rotationalVelocity;

    private float timer;

    // -----------------------------------------------------------------------------------------------------------------

    public Asteroid(Vector2 origin, Vector2 trajectory, float radius, float rotationalVelocity) {

        this.timer = 0;

        this.position = origin.cpy();
        this.trajectory = trajectory.cpy();
        this.scaledTrajectory = new Vector2();
        this.rotationalVelocity = rotationalVelocity;
        this.radius = radius;

        sprite = new Sprite(Assets.plAsteroids.get(Assets.rand.nextInt(Assets.plAsteroids.size())));
        sprite.setSize(this.radius * 2, this.radius * 2);
        sprite.setOriginCenter();
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public void draw(SpriteBatch batch) {

        if (destroyable || timer <= STAGING_TIME) {
            return;
        }

        sprite.draw(batch);

    }

    public void update(float dt) {

        if (destroyable) {
            return;
        }

        timer += dt;

        if (timer > STAGING_TIME) {
            // Update the position
            scaledTrajectory.set(trajectory).scl(dt);
            position.add(scaledTrajectory);
            // Check for offscreen.
            if (position.x < -radius || position.x > GameConstants.GameWidth + radius ||
                    position.y < -radius || position.y > GameConstants.GameHeight + radius) {
                // It's gone, destroy it.
                Gdx.app.log(TAG, "we've destroyed an off-screen sprite");
                setDestroyable(true);
            }
            // Position it.
            sprite.setCenter(position.x, position.y);

            // Update the rotation
            sprite.setRotation((sprite.getRotation() + (rotationalVelocity * dt)) % 360);

        }

    }

    // -----------------------------------------------------------------------------------------------------------------



}
