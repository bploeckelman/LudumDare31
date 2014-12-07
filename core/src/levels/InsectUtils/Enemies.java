package levels.InsectUtils;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by jhoopes on 12/6/14.
 */
public abstract class Enemies {

    protected String name;
    protected Sprite enemySprite;

    public String getName(){ return this.name; }

    public Sprite getEnemySprite(){ return this.enemySprite; }

    public void drawSprite(SpriteBatch batch){
        this.enemySprite.draw(batch);
    }

    public abstract void updateSprite(float dt);
}
