package levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.math.Vector2;
import lando.systems.ld31.Assets;
import lando.systems.ld31.GameConstants;
import levels.InsectUtils.EnemyTypes.Spider;
import levels.InsectUtils.MapTileTypes.Bar;
import levels.InsectUtils.MapTileTypes.Path;
import levels.InsectUtils.MapTileTypes.Beer;
import levels.InsectUtils.MapTiles;

import levels.InsectUtils.Enemies;
import levels.InsectUtils.Tower;
import levels.InsectUtils.TowerTypes.Dart;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by jhoopes on 12/6/14.
 */
public class Insects extends GameLevel {

    protected int currentThreat;

    protected MapTiles[][] baseMap;
    protected ArrayList<Enemies> enemies;
    protected ArrayList<Tower> towers;
    protected int tileWidth;
    protected int tileHeight;

    protected float enemyDeltaTime;
    protected float nextEnemyDeltaTime;
    protected int pathYStart;


    public Insects(){
        super();

        this.currentThreat = 0;
        this.enemyDeltaTime = 0;
        this.enemies = new ArrayList<Enemies>();
        this.towers = new ArrayList<Tower>();
        this.getWidthAndHeight();
        this.baseMap = new MapTiles[this.tileWidth][];
        generateMap();
    }


    public int hasThreat() {
        return this.currentThreat;
    }

    public void updateThreat(int pathsLeft){

        int threat = 0;
        if(pathsLeft < 6 && pathsLeft > 4){
            threat = 1;
        }else if(pathsLeft <= 4 && pathsLeft >= 3){
            threat = 2;
        }else if (pathsLeft < 3){
            threat = 3;
        }

        if(threat > this.currentThreat){
            this.currentThreat = threat;
        }

    }

    public void handleInput(float dt){

    }


    public boolean touchUp(int screenX, int screenY, int button) {
        return this.addTower(getGamePos(new Vector2(screenX, screenY)));
    }

    protected boolean addTower(Vector2 clickPos){

        int cellXNum = (int) Math.floor(clickPos.x / 32);
        int cellYNum = (int) Math.floor(clickPos.y / 32);

        if(cellXNum >= this.baseMap.length){ // if they're clicking off the map return false
            return false;
        }

        // don't add towers to path
        if(this.baseMap[cellXNum][cellYNum] instanceof Path || this.baseMap[cellXNum][cellYNum] instanceof Beer ||
                                                                            this.baseMap[cellXNum][cellYNum].hasTower()){
            return false;
        }

        Tower newTower = new Dart(new Vector2(cellXNum * 32, cellYNum * 32));
        this.towers.add(newTower);
        this.baseMap[cellXNum][cellYNum].setHasTower(true);

        return true;
    }

    @Override
    public void update(float dt) {

        if(this.enemyDeltaTime > this.nextEnemyDeltaTime){
            Spider spider = new Spider(this.pathYStart);
            this.enemies.add(spider);
            this.nextEnemyDeltaTime = (Assets.rand.nextFloat() * 4) + 1;
            this.enemyDeltaTime = 0;
        }else{
            this.enemyDeltaTime = this.enemyDeltaTime + dt;
        }


        ArrayList<Enemies> tempEnemies = new ArrayList<Enemies>();
        if(this.enemies != null){
            for(int x = 0; x < this.enemies.size(); x++){
                this.enemies.get(x).updateSprite(dt);
                if(this.enemies.get(x).alive()) {
                    tempEnemies.add(this.enemies.get(x));
                    this.updateThreat(this.enemies.get(x).pathsLeft());
                }
            }
            this.enemies = tempEnemies;
        }

        if(this.towers != null){
            for(int x = 0; x < this.towers.size(); x++){
                this.towers.get(x).updateTower(this.enemies, dt);
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


        if(this.towers != null){
            for(int x =0; x < this.towers.size(); x++){
                this.towers.get(x).drawSprite(batch);
                this.towers.get(x).renderBullets(batch);
            }
        }

        if(this.enemies != null){
            for(int x = 0; x < this.enemies.size(); x++){
                this.enemies.get(x).drawSprite(batch);
            }
        }


    }

    public void getWidthAndHeight(){

        float width = GameConstants.GameWidth + 32;
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


            if((this.tileWidth - x) < 4){

                this.baseMap[x][currenty] = new Beer();
                this.baseMap[x][currenty+1] = new Beer();
                this.baseMap[x][currenty-1] = new Beer();
                this.baseMap[x+1][currenty] = new Beer();
                this.baseMap[x+1][currenty+1] = new Beer();
                this.baseMap[x+1][currenty-1] = new Beer();

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
