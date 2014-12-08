package levels.InsectUtils.BulletTypes;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld31.Assets;
import levels.InsectUtils.Bullet;
import levels.InsectUtils.Enemies;

/**
 * Created by jhoopes on 12/7/14.
 */
public class Dart extends Bullet {

    public Dart(Vector2 origin, Enemies target, int towerDamage){

        this.name = "Dart";
        this.shotSpeed = 40;
        this.damage = 1;
        Sprite  newBullet = new Sprite(Assets.insectsAssets.DartBullet);
        newBullet.setSize(4, 4);
        Vector2 newBulletLocation = new Vector2(origin.x, origin.y);
        newBullet.setCenter(newBulletLocation.x, newBulletLocation.y);
        this.bulletSprite = newBullet;
        this.currentPosition = newBulletLocation;
        this.target = target;
        this.onScreen = true;
    }

    public void updateBullet(float dt){

        if(!this.onScreen){
            return;
        }

        Vector2 target = this.target.getCurrentPosition().cpy();
        Vector2 direction = target.sub(this.currentPosition);

        float rotation = direction.angle();
        float distance = direction.len();

        if(distance < 10){
            this.target.damageEnemy(this.damage);
            this.onScreen = false;
        }else{
            direction.nor().scl(dt*this.shotSpeed);
            this.currentPosition.add(direction);
        }

        this.bulletSprite.setCenter(this.currentPosition.x, this.currentPosition.y);
    }
}
