package levels.InsectUtils.TowerTypes;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld31.Assets;
import levels.InsectUtils.Tower;


/**
 * Created by jhoopes on 12/6/14.
 */
public class Dart extends Tower {

    public Dart(Vector2 towerPosition){

        this.name = "Dart";
        this.towerSprite = new Sprite(Assets.insectsAssets.DartTower);
        this.towerSprite.setSize(16, 16);

        this.towerSprite.setX(towerPosition.x + 8);
        this.towerSprite.setY(towerPosition.y + 8);

        //this.towerSprite.setCenter(towerPosition.x, towerPosition.y);
    }

}
