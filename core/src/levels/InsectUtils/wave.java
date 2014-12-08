package levels.InsectUtils;

import lando.systems.ld31.Assets;
import levels.InsectUtils.EnemyTypes.Ant;
import levels.InsectUtils.EnemyTypes.Spider;

/**
 * Created by jhoopes on 12/7/14.
 */
public class wave {

    protected int enemyNum;
    protected int waveNum;


    public wave(int waveNum){

        this.enemyNum = Assets.rand.nextInt((15 - 7) + 1) + 7;
        this.waveNum = waveNum;
    }

    public Enemies generateNewEnemy(int pathYStart){

        this.enemyNum = this.enemyNum - 1;

        if(this.waveNum < 3){
            return new Spider(pathYStart);
        }else{
            Enemies newEnemy;
            int choice = Assets.rand.nextInt((2 - 1) + 1) + 1;
            switch(choice){
                case 1:
                    newEnemy = new Spider(pathYStart);
                    break;
                case 2:
                    newEnemy = new Ant(pathYStart);
                    break;
                default:
                    newEnemy = new Spider(pathYStart);
            }

            return newEnemy;
        }
    }

    public boolean isComplete(){

        if(this.enemyNum < 1){
            return true;
        }else{
            return false;
        }
    }

}
