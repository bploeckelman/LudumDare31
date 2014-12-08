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
        this.health = 3f;
        this.value = 1;
        Texture spiderTexture = Assets.insectsAssets.Spider;
        this.enemySprite = new Sprite(spiderTexture);
        this.enemySprite.setSize(16, 16);


        this.currentPosition = new Vector2(16, (pathYStart*32) + 16);
        this.enemySprite.setCenter(this.currentPosition.x, this.currentPosition.y);

        this.currentSpeed = 20;
        this.originalSpeed = 20;
        this.alive = true;

    }

    public void updateSprite(float dt){

        if(this.checkPoints.size() == 0){
            this.alive = false;
            return;
        }

        if(this.slowEnemyFor != 0){
            if(this.slowDelta > this.slowEnemyFor){
                // if our delta is bigger, meaning that we've gone the amount of time for slow, then reset
                this.currentSpeed = this.originalSpeed;
                this.slowEnemyFor = 0;
                this.slowDelta = 0;
            }else{
                this.slowDelta = this.slowDelta + dt;
            }
        }


        Vector2 target = this.checkPoints.get(0).cpy();
        Vector2 direction = target.sub(currentPosition);

        float rotation = direction.angle();
        this.enemySprite.setRotation(rotation);

        float distance = direction.len();

        if(distance < (dt * this.currentSpeed)){
            this.currentPosition = this.checkPoints.get(0);
            this.checkPoints.remove(0);
        }else{
            direction.nor().scl(dt*this.currentSpeed);
            this.currentPosition.add(direction);
        }

        this.enemySprite.setCenter(this.currentPosition.x, this.currentPosition.y);
    }
}
