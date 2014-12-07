package levels.InsectUtils;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by jhoopes on 12/7/14.
 */
public abstract class Bullet {

    protected String name;
    protected Sprite bulletSprite;
    protected float  shotSpeed;
    protected Vector2 currentPosition;
    protected int damage;
    protected Enemies target;
    protected boolean onScreen;

    public void drawSprite(SpriteBatch batch){
        if(this.onScreen){
            this.bulletSprite.draw(batch);
        }
    }

    public boolean getOnScreen(){ return  this.onScreen; }

    public abstract void updateBullet(float dt);


}
