package levels.InsectUtils;

import lando.systems.ld31.Assets;
import levels.InsectUtils.EnemyTypes.Spider;

/**
 * Created by jhoopes on 12/7/14.
 */
public class wave {

    protected int enemyNum;
    protected int waveNum;


    public wave(int waveNum){

        this.enemyNum = Assets.rand.nextInt((15 - 7) + 1) + 7;

    }

    public Enemies generateNewEnemy(int pathYStart){
        this.enemyNum = this.enemyNum - 1;
        return new Spider(pathYStart);
    }

    public boolean isComplete(){

        if(this.enemyNum < 1){
            return true;
        }else{
            return false;
        }
    }

}
