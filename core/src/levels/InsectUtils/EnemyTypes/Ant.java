package levels.InsectUtils.EnemyTypes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld31.Assets;
import levels.InsectUtils.Enemies;

/**
 * Created by jhoopes on 12/7/14.
 */
public class Ant extends Enemies {

    protected float animTime;
    protected Animation anim;

    public Ant(int pathYStart){
        super(pathYStart);

        this.name = "Spider";
        this.health = 10f;
        this.value = 2;

        this.enemySprite = new Sprite(Assets.insectsAssets.Spider);
        this.enemySprite.setSize(16, 16);
        this.currentPosition = new Vector2(16, (pathYStart*32) + 16);
        this.enemySprite.setCenter(this.currentPosition.x, this.currentPosition.y);

        this.currentSpeed = 30;
        this.originalSpeed = 30;
        this.alive = true;


        Animation newAnim = new Animation(0.1f, Assets.insectsAssets.Ant.getRegions());
        newAnim.setPlayMode(Animation.PlayMode.LOOP);
        this.anim = newAnim;
        this.animTime = 0f;
    }

    @Override
    public void drawSprite(SpriteBatch batch){

        this.enemySprite.setRegion(this.anim.getKeyFrame(this.animTime));

        float currRotation = this.enemySprite.getRotation();
        currRotation -= 90;

        this.enemySprite.setRotation(currRotation);
        this.enemySprite.draw(batch);
    }

    public void updateSprite(float dt){

        if(this.checkPoints.size() == 0){
            this.alive = false;
            return;
        }

        this.animTime += dt;

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
