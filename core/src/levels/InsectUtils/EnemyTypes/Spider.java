package levels.InsectUtils.EnemyTypes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld31.Assets;
import levels.InsectUtils.Enemies;

/**
 * Created by jhoopes on 12/6/14.
 */
public class Spider extends Enemies {

    public Spider(int pathYStart){
        super(pathYStart);

        this.name = "Spider";
        Texture spiderTexture = Assets.insectsAssets.Spider;
        this.enemySprite = new Sprite(spiderTexture);
        this.enemySprite.setSize(16, 16);


        this.currentPosition = new Vector2(16, (pathYStart*32) + 16);
        this.enemySprite.setCenter(this.currentPosition.x, this.currentPosition.y);

        this.speed = 20;

    }

    public void updateSprite(float dt){

        Vector2 target = this.checkPoints.get(0).cpy();
        Vector2 direction = target.sub(currentPosition);

        float distance = direction.len();

        if(distance < (dt * this.speed)){
            this.currentPosition = this.checkPoints.get(0);
            this.checkPoints.remove(0);
        }else{
            direction.nor().scl(dt*this.speed);
            this.currentPosition.add(direction);
        }

        this.enemySprite.setCenter(this.currentPosition.x, this.currentPosition.y);
    }
}
