package levels.InsectUtils;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by jhoopes on 12/6/14.
 */
public abstract class Tower {

    protected String name;
    protected Sprite towerSprite;
    protected float shootSpeed;
    protected float range;
    protected int towerDamage;
    protected int cost;
    protected Vector2 position;
    protected Enemies target;
    protected ArrayList<Bullet> towerBullets;
    protected float lastShotTime;

    public Tower(){
        this.towerBullets = new ArrayList<Bullet>();
    }

    public String getName(){ return this.name; }

    public float getShootSpeed(){ return this.shootSpeed; }

    public Sprite getTowerSprite(){ return this.towerSprite; }

    public int getCost(){ return this.cost; }

    public void drawSprite(SpriteBatch batch){
        this.towerSprite.draw(batch);
    }

    public void renderBullets(SpriteBatch batch){
        for(int x = 0; x < this.towerBullets.size(); x++){
            this.towerBullets.get(x).drawSprite(batch);
        }
    }

    public abstract void updateTower(ArrayList<Enemies> enemies, float dt);

}
