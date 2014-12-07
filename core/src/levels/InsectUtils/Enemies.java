package levels.InsectUtils;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jhoopes on 12/6/14.
 */
public abstract class Enemies {

    protected String name;
    protected Sprite enemySprite;
    protected float speed;
    protected ArrayList<Vector2> checkPoints = new ArrayList<Vector2>();

    protected Vector2 currentPosition;

    public Enemies(int pathYStart){


        this.checkPoints.add(new Vector2((float) 4.5 * 32, (pathYStart * 32) + 16));
        this.checkPoints.add(new Vector2((float) 4.5 * 32, (pathYStart * 32) + (5*32) + 16));


    }



    public String getName(){ return this.name; }

    public float getSpeed(){ return this.speed; }

    public Sprite getEnemySprite(){ return this.enemySprite; }

    public void drawSprite(SpriteBatch batch){
        this.enemySprite.draw(batch);
    }

    public abstract void updateSprite(float dt);
}
