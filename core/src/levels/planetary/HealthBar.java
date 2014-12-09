package levels.planetary;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld31.Assets;

/**
 * Author: Ian McNamara <ian.mcnamara@wisc.edu>
 * Teaching and Research Application Development
 * Copyright 2014 Board of Regents of the University of Wisconsin System
 */
public class HealthBar {

    private final static String TAG = "HealthBar";

    private final static float BORDER_WIDTH = 2f;

    private Sprite background = new Sprite(Assets.squareTex);
    private Sprite indicator = new Sprite(Assets.squareTex);
    private Sprite indicatorBg = new Sprite(Assets.squareTex);
    private float indicatorMaxWidth;
    private float indicatorHeight;

    public HealthBar(Vector2 position, float width, float height, float healthPercent) {
        indicatorHeight = height - (BORDER_WIDTH * 2);
        indicatorMaxWidth = width - (BORDER_WIDTH * 2);

        // Set the size of the background, as that will never change.
        background.setSize(width, height);
        indicatorBg.setSize(indicatorMaxWidth, indicatorHeight);

        // Set the rest
        setPosition(position);
        setHealthPercent(healthPercent);

        background.setColor(Color.BLACK);
        indicator.setColor(Color.GREEN);
        indicatorBg.setColor(Color.RED);
    }

    public void draw(Batch batch) {
        background.draw(batch);
        indicatorBg.draw(batch);
        indicator.draw(batch);
    }

    public void setHealthPercent(float healthPercent) {
        // resize the indicator
        indicator.setSize(indicatorMaxWidth * healthPercent, indicatorHeight);
    }

    public void setPosition(Vector2 position) {
        background.setCenter(position.x, position.y);
        indicatorBg.setCenter(position.x, position.y);
        indicator.setPosition(background.getX() + BORDER_WIDTH, background.getY() + BORDER_WIDTH);
    }

}
