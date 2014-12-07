package levels;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import levels.InsectUtils.EnemyTypes.Spider;
import levels.InsectUtils.MapTileTypes.Bar;
import levels.InsectUtils.MapTileTypes.Path;
import levels.InsectUtils.MapTileTypes.Beer;
import levels.InsectUtils.MapTiles;

import levels.InsectUtils.Enemies;

import java.util.ArrayList;

/**
 * Created by jhoopes on 12/6/14.
 */
public class Insects extends GameLevel {

    protected MapTiles[][] baseMap;
    protected ArrayList<Enemies> enemies;
    protected int tileWidth;
    protected int tileHeight;

    protected float enemyDeltaTime;
    protected int pathYStart;


    public Insects(){
        super();
        this.enemyDeltaTime = 0;
        this.enemies = new ArrayList<Enemies>();
        this.getWidthAndHeight();
        this.baseMap = new MapTiles[this.tileWidth][];
        generateMap();

    }


    public int hasThreat() {
        return 0;
    }

    public void handleInput(float dt){

    }

    @Override
    public void update(float dt) {

        if(this.enemyDeltaTime > 5){
            Spider spider = new Spider(this.pathYStart);
            this.enemies.add(spider);
            this.enemyDeltaTime = 0;
        }else{
            this.enemyDeltaTime = this.enemyDeltaTime + dt;
        }

        if(this.enemies != null){
            for(int x = 0; x < this.enemies.size(); x++){
                this.enemies.get(x).updateSprite(dt);
            }
        }


    }

    @Override
    public void draw(SpriteBatch batch) {

        // init vars
        float width = this.camera.viewportWidth;
        float height = this.camera.viewportHeight;

        int px = 0;
        int py = 0;

        for(int x = 0; x < this.tileWidth; x++){
            for(int y = 0; y < this.tileHeight; y++){
                batch.draw(this.baseMap[x][y].getTileTexture(), (x)*32, (y)*32);
            }
        }

        if(this.enemies != null){
            for(int x = 0; x < enemies.size(); x++){
                this.enemies.get(x).drawSprite(batch);
            }
        }


    }

    public void getWidthAndHeight(){

        float width = this.camera.viewportWidth;
        float height = this.camera.viewportHeight;

        this.tileWidth = (int) Math.floor(width / 32);
        this.tileHeight = (int) Math.floor(height / 32);

    }

    private void generateMap(){

        for(int x = 0; x < this.tileWidth; x++){

            this.baseMap[x] = new MapTiles[this.tileHeight];

            for(int y = 0; y < this.tileHeight; y++){
                this.baseMap[x][y] = new Bar();
            }
        }

        int half = (int) Math.floor(this.tileHeight / 2);
        int currenty = half;
        this.pathYStart = half;
        int x = 0;
        while(x < this.tileWidth){


            if((this.tileWidth - x) < 3){

                this.baseMap[x][currenty] = new Beer();
                x = x + 500; // break x out of the tile width

            }else if(x < 4){
                this.baseMap[x][currenty] = new Path();
            }else if(x == 4){
                this.baseMap[x][currenty] = new Path();
                this.baseMap[x][currenty + 1] = new Path();
                this.baseMap[x][currenty + 2] = new Path();
                this.baseMap[x][currenty + 3] = new Path();
                this.baseMap[x][currenty + 4] = new Path();
                this.baseMap[x][currenty + 5] = new Path();
                currenty = currenty + 5;
            }else if(x < 7){
                this.baseMap[x][currenty] = new Path();
            }else if (x == 7){
                this.baseMap[x][currenty] = new Path();
                this.baseMap[x][currenty - 1] = new Path();
                this.baseMap[x][currenty - 2] = new Path();
                this.baseMap[x][currenty - 3] = new Path();
                this.baseMap[x][currenty - 4] = new Path();
                this.baseMap[x][currenty - 5] = new Path();
                this.baseMap[x][currenty - 6] = new Path();
                this.baseMap[x][currenty - 7] = new Path();
                this.baseMap[x][currenty - 8] = new Path();
                this.baseMap[x][currenty - 9] = new Path();
                currenty = currenty - 9;
            }else if (x < 14) {
                this.baseMap[x][currenty] = new Path();
            }else if(x == 14){
                this.baseMap[x][currenty] = new Path();
                this.baseMap[x][currenty - 1] = new Path();
                this.baseMap[x][currenty - 2] = new Path();
                this.baseMap[x][currenty - 3] = new Path();
                currenty = currenty - 3;
            }else if(x < 18){
                this.baseMap[x][currenty] = new Path();
            }else if(x == 18){
                this.baseMap[x][currenty] = new Path();
                this.baseMap[x][currenty + 1] = new Path();
                this.baseMap[x][currenty + 2] = new Path();
                this.baseMap[x][currenty + 3] = new Path();
                this.baseMap[x][currenty + 4] = new Path();
                this.baseMap[x][currenty + 5] = new Path();
                this.baseMap[x][currenty + 6] = new Path();
                this.baseMap[x][currenty + 7] = new Path();
                currenty = currenty + 7;
            }else{
                this.baseMap[x][currenty] = new Path();
            }

            x++;
        }

    }



}
