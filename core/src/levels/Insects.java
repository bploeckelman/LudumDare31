package levels;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import levels.InsectUtils.MapTileTypes.Bar;
import levels.InsectUtils.MapTileTypes.Path;
import levels.InsectUtils.MapTileTypes.Beer;
import levels.InsectUtils.MapTiles;

import levels.InsectUtils.Enemies;

/**
 * Created by jhoopes on 12/6/14.
 */
public class Insects extends GameLevel {

    protected MapTiles[][] baseMap;
    protected Enemies[] enemies;
    protected int tileWidth;
    protected int tileHeight;


    public Insects(){
        super();

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

        for(int x = 0; x < enemies.length; x++){
            enemies[x].updateSprite(dt);
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
                batch.draw(this.baseMap[x][y].getTileTexture(), (x+1)*32, (y+1)*32);
            }
        }

        for(int x = 0; x < enemies.length; x++){
            enemies[x].drawSprite(batch);
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
