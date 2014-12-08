package levels;

import java.util.ArrayList;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Linear;
import aurelienribon.tweenengine.equations.Quad;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import lando.systems.ld31.Assets;
import lando.systems.ld31.GameConstants;
import lando.systems.ld31.LudumDare31;
import lando.systems.ld31.Score;
import lando.systems.ld31.TransitionManager;
import lando.systems.ld31.Vector2Accessor;
import levels.intercellular.BloodCell;
import levels.intracellular.IntraCellularAssets;

import java.util.ArrayList;

/**
 * Created by vandillen on 12/6/14.
 */
public class IntercellularLevel extends GameLevel {

    // Constants
    public final int tile_size  = 32;
    private final int tiles_wide = 320 + 16;
    private final int tiles_high = GameConstants.ScreenHeight / tile_size;
    public Vector2 spawnPoint = new Vector2(GameConstants.GameWidth/2.0f, 30);
    public BloodCell spawnCell;

    public float nextSpawn = 0;

    public Rectangle gameBounds;
    public ArrayList<BloodCell> cells;
    public float levelTimer = 30f;
    float timeAccum = 0;

    
    // Class constructor
    public IntercellularLevel() {
    	tutorialText = "All the stress from running this\nbar (and fried food) is\nclogging your arteries.\n\nMatch 3 or more to clear a group.\n\n\nAnd don't forget about your patrons.";
    	cells = new ArrayList<BloodCell>();
    	gameBounds = new Rectangle((GameConstants.GameWidth - (tile_size * 10 + 16)) / 2.0f, 0, tile_size * 10 + 16, GameConstants.ScreenHeight);
    	spawnCell = new BloodCell(spawnPoint.x, spawnPoint.y, this, false);

    }
    


    // 2d array to a 1d:
    // index = x + (y * width);
    // x = index%width;
    // y = index/width;

    // random bulge method initializer

    // bulge method if falling cell makes contact with bulge

    // random method for incoming cells to shoot



    @Override
    public int hasThreat() {
        int highestRow = 0;
        for (int i = 0; i < cells.size(); i++){
        	BloodCell cell = cells.get(i);
        	if (highestRow < cell.gridPos.y){
        		highestRow = (int)cell.gridPos.y;
        	}
        }
        if (highestRow < 10){
        	return 0;
        } 
        if (highestRow < 14) return 1;
        if (highestRow < 18) return 2;
        return 3;
    }

    @Override
    public void handleInput(float dt) {

    }

    public void wipeBoard(){
    	cells.clear();
    }
    
    public void lose(){
    	wipeBoard();
    }
    
    public void addChains(){
    	for(int i = 0; i < cells.size(); i++){
    		BloodCell cell = cells.get(i);
    		cell.gridPos.y += 2;
            Tween.to(cell.pos, Vector2Accessor.POSITION_Y, .5f)
            .target(cell.pos.y - 64)
            .ease(Linear.INOUT)
            .start(LudumDare31.tweens);
    		
    		if (cell.gridPos.y >= 21){
    			lose();
    		}
    	}
    	for (int y = 1; y < 3; y++){
	    	for (int x = 0; x < 10; x++){
	    		Vector2 gamePos = BloodCell.gridPosToGame(new Vector2(x,y), this);
	    		BloodCell cell = new BloodCell(gamePos.x, gamePos.y + 64, this, true);
	    		cell.gridPos = new Vector2(x,y);
	    		cell.alive = true;
	    		cell.settled = true;
	    		cells.add(cell);
	            Tween.to(cell.pos, Vector2Accessor.POSITION_Y, .5f)
	            .target(cell.pos.y - 64)
	            .ease(Linear.INOUT)
	            .start(LudumDare31.tweens);

	    	}
    	}
    }
    
    @Override
    public boolean touchUp(int screenX, int screenY, int button) {
    	if (tutorialText != null) {
    		super.touchUp(screenX, screenY, button);
    		return true;
    	}
    	
    	if (cellsMoving() || nextSpawn <= 0) return false;
    	Vector2 gamePos = getGamePos(new Vector2(screenX, screenY));
    	float rot = gamePos.sub(16,16).sub(spawnPoint).angle();
    	spawnCell.fire(rot);
    	cells.add(spawnCell);
    	spawnCell = new BloodCell(spawnPoint.x, spawnPoint.y, this, false);
    	return true;
    };
    
    public boolean cellsMoving(){
    	boolean moving = false;
    	for (int i =0; i < cells.size(); i++){
    		if (!cells.get(i).settled) moving = true;
    	}
    	return moving;
    }
    
    @Override
    public void update(float dt) {
    	timeAccum+= dt;
    	for (int i = 0; i < cells.size(); i++){
    		cells.get(i).update(dt);
    	}
    	for (int i = cells.size() -1; i >= 0; i--){
    		if (!cells.get(i).alive) {
    			BloodCell cell = cells.remove(i);
    			for (int j = 0; j < 40; j++){
    				Vector2 cellCenter = new Vector2(16,16).add(cell.pos);
    				Vector2 dest = new Vector2(1,0).rotate(Assets.rand.nextInt(360)).scl(Assets.rand.nextFloat() * 40)
    						.add(cellCenter);
    				particles.addParticle(cellCenter, dest, cell.color, Color.RED, 1f, Quad.OUT);
    			}
    		}
    	}
    	if (nextSpawn <= 0 && !cellsMoving()){
    		addChains();
    		nextSpawn += 20 + Assets.rand.nextInt(20);
    	}
    	nextSpawn = Math.max(nextSpawn - dt, 0);
    	fixHangers();
    	levelTimer -= dt;
    	if (cells.isEmpty() || levelTimer < 0){
    		TransitionManager.Instance.defendPlanet();
    		levelTimer = 10000;
    	}
    }
    
    public BloodCell getCellAtPos(Vector2 pos){
    	for (int i = 0; i < cells.size(); i++){
    		if (pos.equals(cells.get(i).gridPos)) return cells.get(i);
    	}
    	return null;
    }
    
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
    	Vector2 dir = getGamePos(new Vector2(screenX, screenY)).sub(spawnPoint).nor();
    	Vector2 shootPos = new Vector2(0, 1).setAngle(dir.angle()).scl(40).add(spawnPoint);
    	spawnCell.pos = shootPos;
    	return true;
    };
    
    public void fixHangers(){
    	ArrayList<BloodCell> chain = new ArrayList<BloodCell>();
		ArrayList<BloodCell> toCheck = new ArrayList<BloodCell>();
    	for (int x =0; x < 10; x++){
    		BloodCell cell = getCellAtPos(new Vector2(x, 1));
    		if (cell != null) toCheck.add(cell);
    	}
    	while (!toCheck.isEmpty()){
    		BloodCell cell = toCheck.remove(0);
    		if (!chain.contains(cell)){
    			chain.add(cell);
    			toCheck.addAll(getNeighbors(cell));
    		}
    	}
    	
    	for (int i =0; i < cells.size(); i++){
    		BloodCell cell = cells.get(i);
    		if (!cell.settled) continue;
    		if (!chain.contains(cell)) {
    			Score.ArteryGermsKilled++;
    			cell.alive = false;
    		}
    	}
    }
    
    public ArrayList<BloodCell> getNeighbors(BloodCell cell){
    	ArrayList<BloodCell> neighbors= new ArrayList<BloodCell>();
    	Vector2 grid = cell.gridPos;
    	int xOffset = 0;
    	if ((int)grid.y % 2 == 0 ) xOffset = 1;
    	
    	BloodCell neighbor = getCellAtPos(new Vector2(grid.x-1 + xOffset, grid.y -1));
    	if (neighbor != null) neighbors.add(neighbor);
        neighbor = getCellAtPos(new Vector2(grid.x + xOffset, grid.y -1));
    	if (neighbor != null) neighbors.add(neighbor);
    	
    	neighbor = getCellAtPos(new Vector2(grid.x - 1, grid.y));
    	if (neighbor != null) neighbors.add(neighbor);
        neighbor = getCellAtPos(new Vector2(grid.x + 1, grid.y));
    	if (neighbor != null) neighbors.add(neighbor);
    	
    	neighbor = getCellAtPos(new Vector2(grid.x -1 + xOffset, grid.y +1));
    	if (neighbor != null) neighbors.add(neighbor);
        neighbor = getCellAtPos(new Vector2(grid.x + xOffset, grid.y +1));
    	if (neighbor != null) neighbors.add(neighbor);
    	
    	return neighbors;
    }

    @Override
    public void draw(SpriteBatch batch) {
    	
    	batch.setShader(Assets.waterProgram);
    	Assets.waterProgram.setUniformf("time", timeAccum);
        batch.draw(Assets.cellbackground, 0, 0, camera.viewportWidth - 100, camera.viewportHeight);
        batch.setShader(null);
        
    	batch.setColor(Color.WHITE);
    	batch.draw(Assets.bloodVessel, gameBounds.x, gameBounds.y, gameBounds.width, gameBounds.height);
    	for (int i = 0; i < cells.size(); i++){
    		cells.get(i).draw(batch);
    	}
    	if (!cellsMoving()){
    		spawnCell.draw(batch);
    	}
    }
}
