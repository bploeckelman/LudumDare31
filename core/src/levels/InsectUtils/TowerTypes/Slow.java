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
public class Slow extends Tower {

    public Slow(Vector2 towerPosition){
        super();
        this.name = "Dart";
        this.range = 5;
        this.shootSpeed = 1;
        this.towerDamage = 0;
        this.slowFactor = 2;
        this.slowTime = 3;
        this.cost = 20;
        this.towerSprite = new Sprite(Assets.insectsAssets.DartTower);
        this.towerSprite.setSize(16, 16);

        Vector2 position = new Vector2(towerPosition.x + 16, towerPosition.y + 16);
        this.position = position;

        this.towerSprite.setCenter(position.x, position.y);
    }

    public void updateTower(ArrayList<Enemies> enemies, float dt){

        // first update towerBullets
        // slow tower doesn't have bullets


        for(int x = 0; x < enemies.size(); x++){

            // calculate the distance from the tower and enemy
            Vector2 enemyPosition = enemies.get(x).getCurrentPosition().cpy();
            Vector2 direction = enemyPosition.sub(this.position);

            float rotation = direction.angle();
            float distance = direction.len();

            if (distance < this.range) {
                enemies.get(x).slowEnemy(this.slowTime, this.slowFactor);
                break;
            }
        }
    }

}
