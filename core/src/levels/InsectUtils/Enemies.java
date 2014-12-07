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
    protected boolean alive;
    protected int health;
    protected int value;

    protected Vector2 currentPosition;

    public Enemies(int pathYStart){

        this.checkPoints.add(new Vector2((float) 4.5 * 32, (pathYStart * 32) + 16));
        this.checkPoints.add(new Vector2((float) 4.5 * 32, (pathYStart * 32) + (5*32) + 16));
        this.checkPoints.add(new Vector2((float) 7.5 * 32, (pathYStart * 32) + (5*32) + 16));
        this.checkPoints.add(new Vector2((float) 7.5 * 32, (pathYStart * 32) - (4*32) + 16));
        this.checkPoints.add(new Vector2((float) 14.5 * 32, (pathYStart * 32) - (4*32) + 16));
        this.checkPoints.add(new Vector2((float) 14.5 * 32, (pathYStart * 32) - (7*32) + 16));
        this.checkPoints.add(new Vector2((float) 18.5 * 32, (pathYStart * 32) - (7*32) + 16));
        this.checkPoints.add(new Vector2((float) 18.5 * 32, (pathYStart * 32) + 16));
        this.checkPoints.add(new Vector2((float) 26.5 * 32, (pathYStart * 32) + 16));
    }

    public String getName(){ return this.name; }

    public float getSpeed(){ return this.speed; }

    public Sprite getEnemySprite(){ return this.enemySprite; }

    public int getValue(){ return this.value; }

    public Vector2 getCurrentPosition(){ return this.currentPosition; }

    public void drawSprite(SpriteBatch batch){
        this.enemySprite.draw(batch);
    }

    public boolean alive(){ return this.alive; }

    public void damageEnemy(int damage){
        this.health = this.health - damage;

        if(this.health < 1){
            this.alive = false;
        }
    }

    public int pathsLeft(){ return this.checkPoints.size(); }

    public abstract void updateSprite(float dt);
}
