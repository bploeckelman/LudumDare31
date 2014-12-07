package levels.InsectUtils.EnemyTypes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import lando.systems.ld31.Assets;
import levels.InsectUtils.Enemies;

/**
 * Created by jhoopes on 12/6/14.
 */
public class Spider extends Enemies {

    public Spider(){

        this.name = "Spider";
        Texture spiderTexture = Assets.insectsAssets.Spider;
        this.enemySprite = new Sprite(spiderTexture);


    }

    public void updateSprite(float dt){

    }
}
