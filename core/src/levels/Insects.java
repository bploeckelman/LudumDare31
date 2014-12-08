package levels;

import aurelienribon.tweenengine.equations.Quad;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import lando.systems.ld31.Assets;
import lando.systems.ld31.GameConstants;
import lando.systems.ld31.TransitionManager;
import levels.InsectUtils.*;
import levels.InsectUtils.EnemyTypes.Spider;
import levels.InsectUtils.MapTileTypes.Bar;
import levels.InsectUtils.MapTileTypes.Path;
import levels.InsectUtils.MapTileTypes.Beer;
import levels.InsectUtils.TowerTypes.Dart;
import levels.InsectUtils.TowerTypes.Poison;
import levels.InsectUtils.TowerTypes.Slow;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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


    protected wave currentWave;
    protected int currentWaveNum;
    protected float nextWaveTime;
    protected float waveDeltaTime;
    protected float enemyDeltaTime;
    protected float nextEnemyDeltaTime;
    protected int pathYStart;

    protected int money; // amount the player has to spend

    protected ArrayList<HashMap<Object, Object>>  towerSelects;
    protected String currentTowerType;


    public Insects(){
        super();
        tutorialText = "Bugs are in your dirty bar!\nSelect and place bug killing tech with your mouse.\n\nDon't forget about your partons\n\nZoom back by clicking on the green bar on the left";
        this.currentThreat = 0;
        this.enemyDeltaTime = 0;
        this.money = 15;
        this.currentWaveNum = 0;
        this.enemies = new ArrayList<Enemies>();
        this.towers = new ArrayList<Tower>();
        this.currentTowerType = "Dart";
        this.getWidthAndHeight();
        this.baseMap = new MapTiles[this.tileWidth][];
        generateMap();
        generateTowerSelects();
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
    	boolean handled = false;
    	if (tutorialText == null) {
    		Vector2 pos = getGamePos(new Vector2(screenX, screenY));
    		selectTower(pos);
    		handled = addTower(pos);
    	}
    	super.touchUp(screenX, screenY, button);
    	return handled;
    }

    protected void selectTower(Vector2 pointClicked){

        int cellXNum = (int) Math.floor(pointClicked.x / 32);
        int cellYNum = (int) Math.floor(pointClicked.y / 32);

        if(cellXNum == 10 && cellYNum == this.tileHeight + 1){
            this.currentTowerType = "Dart";
        }else if(cellXNum == 16 && cellYNum == this.tileHeight + 1){
            this.currentTowerType = "Slow";
        }else if(cellXNum == 22 && cellYNum == this.tileHeight + 1){
            this.currentTowerType = "Poison";
        }

    }

    protected boolean addTower(Vector2 clickPos){

        int cellXNum = (int) Math.floor(clickPos.x / 32);
        int cellYNum = (int) Math.floor(clickPos.y / 32);

        if(cellXNum >= this.baseMap.length){ // if they're clicking off the map return false
            return false;
        }

        if(cellYNum >= this.baseMap[0].length){
            return false;
        }

        // don't add towers to path
        if(this.baseMap[cellXNum][cellYNum] instanceof Beer || this.baseMap[cellXNum][cellYNum].hasTower()){
            return false;
        }

        // get newTower based on current tower type
        Tower newTower;

        // first check if the cell num is an instance of path, if so and our current tower type is slow, then add it

        if(this.baseMap[cellXNum][cellYNum] instanceof Path && this.currentTowerType == "Slow"){

            newTower = new Slow(new Vector2(cellXNum * 32, cellYNum * 32));

        }else if(this.baseMap[cellXNum][cellYNum] instanceof Path) {
            // don't allow other towers to be created

            return false;

        }else{
            if(this.currentTowerType == "Dart"){
                newTower = new Dart(new Vector2(cellXNum * 32, cellYNum * 32));
            }else if(this.currentTowerType == "Poison"){
                newTower = new Poison(new Vector2(cellXNum * 32, cellYNum * 32));
            }else{
                return false; // if we haven't found a tower yet return false
            }
        }


        if(this.money >= newTower.getCost()){
            this.money = this.money - newTower.getCost();
            this.towers.add(newTower);
            this.baseMap[cellXNum][cellYNum].setHasTower(true);
        }

        return true;
    }

    @Override
    public void update(float dt) {



        if(this.currentWave != null){
            if(!this.currentWave.isComplete()){

                if(this.enemyDeltaTime > this.nextEnemyDeltaTime){

                    this.enemies.add(this.currentWave.generateNewEnemy(this.pathYStart));
                    this.nextEnemyDeltaTime = (Assets.rand.nextFloat() * 4) + 1;
                    this.enemyDeltaTime = 0;
                }else{
                    this.enemyDeltaTime = this.enemyDeltaTime + dt;
                }

            }else{
                this.currentWave = null;
                this.nextWaveTime = (Assets.rand.nextFloat() * 10) + 10;
            }
        }else{
            // wait for next wave
            if(this.waveDeltaTime > this.nextWaveTime){
                // generate wave
                this.currentWave = new wave(this.currentWaveNum);
                this.waveDeltaTime = 0;
                this.nextWaveTime = 0;
                this.currentWaveNum = this.currentWaveNum + 1;
                if (currentWaveNum > 2) TransitionManager.Instance.finishedAnts();
            }else{
                this.waveDeltaTime = this.waveDeltaTime + dt;
            }

        }



        // reset current threat to nothing before updating all enemies
        this.currentThreat = 0;

        if(this.enemies != null){
            for(int x = 0; x < this.enemies.size(); x++){
                this.enemies.get(x).updateSprite(dt);
                if(this.enemies.get(x).alive()) {
                    this.updateThreat(this.enemies.get(x).pathsLeft());
                }
            }
        }

        if(this.towers != null){
            for(int x = 0; x < this.towers.size(); x++){
                this.towers.get(x).updateTower(this.enemies, dt);
            }
        }
        startNext = currentWaveNum > 3;
    }

    @Override
    public void draw(SpriteBatch batch) {

        batch.draw(Assets.insectsAssets.Background, 0, 0, camera.viewportWidth, camera.viewportHeight - 32);

        for(int x = 0; x < this.tileWidth; x++){
            for(int y = 0; y < this.tileHeight; y++){
                batch.draw(this.baseMap[x][y].getTileTexture(), (x)*32, (y)*32);
            }
        }
        // draw HUD tiles
        for(int x=0; x < this.tileWidth; x++){
            batch.draw(Assets.insectsAssets.HUDTile, x * 32, this.tileHeight * 32);
            batch.draw(Assets.insectsAssets.HUDTile, x * 32, (this.tileHeight + 1) * 32);
        }

        // draw tower boxes
        for(int x=0; x < this.towerSelects.size(); x++){
            HashMap<Object, Object> towerSelect = this.towerSelects.get(x);

            int CellXNum = (Integer) towerSelect.get("CellXNum");
            int CellYNum = (Integer) towerSelect.get("CellYNum");

            if(this.currentTowerType == towerSelect.get("Name")){
                batch.draw(Assets.insectsAssets.TowerSelectBGSelected, CellXNum * 32, CellYNum * 32);
            }else{
                batch.draw(Assets.insectsAssets.TowerSelectBG, CellXNum * 32, CellYNum * 32);
            }
            Assets.smallFont.draw(batch, towerSelect.get("Name") + " - Cost: " + towerSelect.get("Cost"), (CellXNum * 32) - 50, (CellYNum * 32) - 8);
        }



        Assets.smallFont.draw(batch, "Coins: " + Integer.toString(this.money), 0, this.camera.viewportHeight - 6);
        Assets.smallFont.draw(batch, "Wave: " + Integer.toString(this.currentWaveNum), 0, this.camera.viewportHeight - 32);


        if(this.towers != null){
            for(int x =0; x < this.towers.size(); x++){
                this.towers.get(x).drawSprite(batch);
                this.towers.get(x).renderBullets(batch);
            }
        }

        if(this.enemies != null){
            for(int x = 0; x < this.enemies.size(); x++){
                if(this.enemies.get(x).alive()){
                    this.enemies.get(x).drawSprite(batch);
                }else{
                    for(int j = 0; j < 40; j++){
                        Vector2 cellCenter = this.enemies.get(x).getCurrentPosition();
                        Vector2 dest = new Vector2(1, 0).rotate(Assets.rand.nextInt(360)).scl(Assets.rand.nextFloat() * 15)
                                .add(cellCenter);
                        particles.addParticle(cellCenter, dest, Color.WHITE, Color.YELLOW, 1f, Quad.OUT);
                    }
                    this.money = this.money + this.enemies.get(x).getValue();
                    this.enemies.remove(x);
                }
            }
        }


    }

    public void getWidthAndHeight(){

        float width = GameConstants.GameWidth + 32;
        float height = InsectConstants.towerGameHeight;

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

    protected void generateTowerSelects(){

        this.towerSelects = new ArrayList<HashMap<Object, Object>>();

        //Dart Tower
        HashMap<Object, Object> dartTower = new HashMap<Object, Object>();
        dartTower.put("Name", "Dart");
        dartTower.put("CellXNum", 10);
        dartTower.put("CellYNum", this.tileHeight + 1);
        dartTower.put("Cost", "10");

        this.towerSelects.add(dartTower);

        HashMap<Object, Object> slowTower = new HashMap<Object, Object>();
        slowTower.put("Name", "Slow");
        slowTower.put("CellXNum", 16);
        slowTower.put("CellYNum", this.tileHeight + 1);
        slowTower.put("Cost", "20");

        this.towerSelects.add(slowTower);

        HashMap<Object, Object> poisonTower = new HashMap<Object, Object>();
        poisonTower.put("Name", "Poison");
        poisonTower.put("CellXNum", 22);
        poisonTower.put("CellYNum", this.tileHeight + 1);
        poisonTower.put("Cost", "30");

        this.towerSelects.add(poisonTower);


    }

}
