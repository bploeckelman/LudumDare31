package lando.systems.ld31;

import com.badlogic.gdx.math.Vector2;

import levels.GameLevel;

public class TransitionManager {
	
	public static final TransitionManager Instance = new TransitionManager();
	
	public boolean vomitHandled;
	public boolean antsHandled;
	public boolean heartAttackStarted;
	public boolean defendPlanet;
	
	public void handleVomit(float x, float y, float width, float height)
	{
		if (vomitHandled) return;
		vomitHandled = true;
		
		LevelManager lm = LudumDare31.levelMgr;
		
		GameLevel level = lm.levels[lm.currentLevel];
		level.zoomOutPoint = new Vector2(x + width/2, y + height/2);

		lm.addLevel(2);
	}
	
	public void finishedAnts(){
		if (antsHandled) return;
		antsHandled = true;
		LevelManager lm = LudumDare31.levelMgr;
		lm.addLevel(4); // lets do power
	}
	
	public void startHeartAttack(){
		if (heartAttackStarted)return;
		heartAttackStarted = true;
		LevelManager lm = LudumDare31.levelMgr;
		lm.addLevel(1); // lets bust a move
	}
	
	public void defendPlanet(){
		if (defendPlanet) return;
		defendPlanet = true;
		LevelManager lm = LudumDare31.levelMgr;
		lm.addLevel(5); // lets missle command
	}
	
	public boolean defendVirii;
	public void defendVirii(){
		if (defendVirii) return;
		defendVirii = true;
		LevelManager lm = LudumDare31.levelMgr;
		lm.addLevel(0); // lets asteroids
	}
}