package levels.intercellular;

import java.util.ArrayList;

import lando.systems.ld31.Assets;
import lando.systems.ld31.GameConstants;
import levels.IntercellularLevel;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by vandillen on 12/6/14.
 */
public class BloodCell {

    // Constants
    final int OUT_OF_SHOOTER_VELOCITY = 10;

    // Ivars
    private TileType type;
    private Vector2 vel;
    public Vector2 pos;
    public boolean alive;
    public boolean settled;
    public Vector2 gridPos;
    public Color color;

    private IntercellularLevel level;
    Animation anim;
    private float animationTimer;

    // Constructor
    public BloodCell(float x, float y, IntercellularLevel level, boolean enemy) {
    	int typeRand = 6;
    	if (!enemy) typeRand = 7;
    	typeRand = Assets.rand.nextInt(typeRand);
    	if (typeRand < 2)this.type = TileType.badBlue;
    	else if (typeRand < 4) this.type = TileType.badGreen;
    	else if (typeRand < 6) this.type = TileType.badYellow;
    	else this.type = TileType.goodWhite;
    	
    	animationTimer = Assets.rand.nextFloat();
    	switch (type){
    	case goodWhite:
    		color = Color.WHITE.cpy();
    		Array<TextureRegion> keyframes = new Array<TextureRegion>();
    		keyframes.add(Assets.bloodRegions[2][4]);
    		anim = new Animation(1/30f, keyframes, PlayMode.LOOP);
    		break;
    	case badBlue:
    		color = Color.BLUE.cpy();
    		keyframes = new Array<TextureRegion>();
    		for (int i = 0; i < 20; i++){
    			keyframes.add(Assets.bloodRegions[i/8][i%8]);
    		}
    		
    		anim = new Animation(1/30f, keyframes, PlayMode.LOOP);
    		break;
    	case badGreen:
    		color = Color.GREEN.cpy();
    		keyframes = new Array<TextureRegion>();
    		for (int i = 0; i < 20; i++){
    			keyframes.add(Assets.bloodRegions[i/8+3][i%8]);
    		}
    		
    		anim = new Animation(1/30f, keyframes, PlayMode.LOOP);
    		break;
    	case badYellow:
    		color = Color.YELLOW.cpy();
    		keyframes = new Array<TextureRegion>();
    		for (int i = 0; i < 20; i++){
    			keyframes.add(Assets.bloodRegions[i/8+6][i%8]);
    		}
    		
    		anim = new Animation(1/30f, keyframes, PlayMode.LOOP);
    		break;
    	default:
    		color = Color.WHITE.cpy();	
    		keyframes = new Array<TextureRegion>();
    		for (int i = 0; i < 20; i++){
    			keyframes.add(Assets.bloodRegions[i/8+3][i%8]);
    		}
    		
    		anim = new Animation(1/30f, keyframes, PlayMode.LOOP);
    	}
    	

    	this.pos = new Vector2(x, y);
    	vel = new Vector2();
    	gridPos = new Vector2(-1,-1);
    	alive = false;
    	this.level = level; 
    	settled = false;
    }

    public Vector2 getPosOnGrid(){
		settled = true;
		vel.set(0,0);
    	int y = (int)((GameConstants.ScreenHeight - pos.y + 16) /level.tile_size);
    	float oddAdjustment = y % 2 == 1 ? 0 : 16;
    	float x = (int)((pos.x  + 16  + oddAdjustment - level.gameBounds.x) / 32) - (oddAdjustment / 32.0f);
    	gridPos.set((int)x, y);
    	
    	// We should clear here
    	if (type == TileType.goodWhite){
    		ArrayList<BloodCell> n = level.getNeighbors(this);
    		for (int i = 0; i < n.size(); i++){
    			n.get(i).alive = false;
    		}
    		this.alive = false;
    	} else {
    		ArrayList<BloodCell> chain = new ArrayList<BloodCell>();
    		ArrayList<BloodCell> toCheck = new ArrayList<BloodCell>();
    		toCheck.add(this);
    		while (!toCheck.isEmpty()){
    			BloodCell cell = toCheck.remove(0);
    			if (cell.type == this.type && !chain.contains(cell)) {
    				chain.add(cell);
    				ArrayList<BloodCell> n = level.getNeighbors(cell);
    				toCheck.addAll(n);
    			}
    		}
    		if (chain.size() > 2){
    			for(int i = 0; i < chain.size(); i++){
    				chain.get(i).alive = false;
    			}
    		}
    	}
    	
    	
    	return new Vector2((x * level.tile_size) + level.gameBounds.x, GameConstants.ScreenHeight - (y * level.tile_size));
    }
    
    
    public static Vector2 gridPosToGame(Vector2 pos, IntercellularLevel level){
    	int y = (int) pos.y;
    	float oddAdjustment = y % 2 == 1 ? 0 : 16;
    	float x = (int)pos.x + (oddAdjustment / 32.0f);
    	return new Vector2((x * level.tile_size) + level.gameBounds.x, GameConstants.ScreenHeight - (y * level.tile_size));

    }

    
    public void fire(float rotation)
    {
        vel = new Vector2(1, 0).setAngle(rotation).scl(400);
        alive = true;
        settled = false;        
    }
    
    public void update(float dt){
    	if (pos.y < -100) alive = false;
    	animationTimer += dt;
    	if (settled) return;
    	
    	float tempY = pos.y + vel.y * dt;
    	if (tempY > level.gameBounds.height - 32){
    		pos = getPosOnGrid();

    	}
    	for (int i = 0; i < level.cells.size(); i++){
    		BloodCell cell = level.cells.get(i);
    		if (cell == this) continue;
    		if (pos.dst2(cell.pos) < 32 * 32){
    			pos = getPosOnGrid();
    			break;
    		}
    	}
    	float tempX = pos.x + vel.x * dt;
    	if (tempX < level.gameBounds.x || tempX > level.gameBounds.x + level.gameBounds.width - 32){
    		vel.x *= -1;
    	}
    	pos.x += vel.x * dt;
		pos.y += vel.y * dt;
    }
    
    public void draw(SpriteBatch batch){

    	//batch.setColor(color);
    	batch.draw(anim.getKeyFrame(animationTimer), pos.x, pos.y, 32, 32);
    	//batch.setColor(Color.WHITE);
    }

    // return true if collision with other cells or bulge
    private boolean collide () {
        return true;
    }
}
