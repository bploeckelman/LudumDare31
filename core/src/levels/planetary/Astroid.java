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
public class Astroid extends DestroyableObject {

    private static final String TAG = "Astroid";

    private static final float STAGING_TIME = 4;

    private Sprite astroid;

    private Vector2 pos;
    private Vector2 trajectory;
    private Vector2 scaledTrajectory;
    private float radius;
    private float rotationalVelocity;

    private float timer;

    // -----------------------------------------------------------------------------------------------------------------

    public Astroid(Vector2 origin, Vector2 trajectory, float radius, float rotationalVelocity) {

        this.timer = 0;

        this.pos = origin.cpy();
        this.trajectory = trajectory.cpy();
        this.scaledTrajectory = new Vector2();
        this.rotationalVelocity = rotationalVelocity;
        this.radius = radius;

        astroid = new Sprite(Assets.plAsteroids.get(Assets.rand.nextInt(Assets.plAsteroids.size())));
        astroid.setSize(this.radius * 2, this.radius * 2);
        astroid.setOriginCenter();
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public void draw(SpriteBatch batch) {

        if (destroyable) {
            return;
        }

        astroid.draw(batch);

    }

    public void update(float dt) {

        if (destroyable) {
            return;
        }

        timer += dt;

        if (timer > STAGING_TIME) {
            // Update the position
            scaledTrajectory.set(trajectory).scl(dt);
            pos.add(scaledTrajectory);
            // Check for offscreen.
            if (pos.x < -radius || pos.x > GameConstants.GameWidth + radius ||
                    pos.y < -radius || pos.y > GameConstants.GameHeight + radius) {
                // It's gone, destroy it.
                Gdx.app.log(TAG, "we've destroyed an off-screen astroid");
                setDestroyable(true);
            }
            // Position it.
            astroid.setCenter(pos.x, pos.y);

            // Update the rotation
            astroid.setRotation((astroid.getRotation() + (rotationalVelocity * dt)) % 360);

        }

    }

    // -----------------------------------------------------------------------------------------------------------------



}
