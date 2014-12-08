package levels.InsectUtils.TowerTypes;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld31.Assets;
import levels.InsectUtils.Bullet;
import levels.InsectUtils.Enemies;
import levels.InsectUtils.Tower;

import java.util.ArrayList;

/**
 * Created by jhoopes on 12/7/14.
 */
public class Poison extends Tower {


    public Poison(Vector2 towerPosition){
        super();
        this.name = "Dart";
        this.range = 40;
        this.shootSpeed = 0.1f;
        this.towerDamage = 0.1f;
        this.cost = 30;
        this.towerSprite = new Sprite(Assets.insectsAssets.DartTower);
        this.towerSprite.setSize(16, 16);

        Vector2 position = new Vector2(towerPosition.x + 16, towerPosition.y + 16);
        this.position = position;

        this.towerSprite.setCenter(position.x, position.y);
    }

    public void updateTower(ArrayList<Enemies> enemies, float dt){

        // first update towerBullets
        for(int x = 0; x < this.towerBullets.size(); x++){
            this.towerBullets.get(x).updateBullet(dt);
            if(!this.towerBullets.get(x).getOnScreen()){ // remove off screen bullets
                this.towerBullets.remove(x);
            }
        }

        if(this.lastShotTime > this.shootSpeed) {

            for(int x = 0; x < enemies.size(); x++){

                // calculate the distance from the tower and enemy
                Vector2 enemyPosition = enemies.get(x).getCurrentPosition().cpy();
                Vector2 direction = enemyPosition.sub(this.position);

                float rotation = direction.angle();
                float distance = direction.len();

                if (distance < this.range) {
                    this.towerSprite.setRotation(rotation);
                    this.target = enemies.get(x);
                    Bullet newBullet = new levels.InsectUtils.BulletTypes.Dart(this.position, enemies.get(x), this.towerDamage);
                    this.towerBullets.add(newBullet);
                    this.lastShotTime = 0;

                    break;
                }
            }
        }else{
            this.lastShotTime = this.lastShotTime + dt;
        }

    }
}
