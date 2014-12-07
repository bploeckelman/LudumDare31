package levels.InsectUtils;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by jhoopes on 12/6/14.
 */
public class Tower {

    protected String name;
    protected Sprite towerSprite;
    protected float shootSpeed;
    protected float range;
    protected Vector2 target;


    public String getName(){ return this.name; }

    public float getShootSpeed(){ return this.shootSpeed; }

    public Sprite getTowerSprite(){ return this.towerSprite; }

    public void drawSprite(SpriteBatch batch){
        this.towerSprite.draw(batch);
    }

}
