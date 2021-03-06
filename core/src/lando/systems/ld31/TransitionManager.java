package lando.systems.ld31;

import com.badlogic.gdx.math.Vector2;

import levels.GameLevel;

public class TransitionManager {
	
	public static final TransitionManager Instance = new TransitionManager();
	
	public boolean vomitHandled;
	public boolean antsHandled;
	public boolean heartAttackStarted;
	public boolean defendPlanet;
	
	public void handleVomit(Vector2 pukeSpot)
	{
		if (vomitHandled) return;
		vomitHandled = true;
		
		LevelManager lm = LudumDare31.levelMgr;
		
		GameLevel level = lm.levels[lm.currentLevel];
		level.zoomOutPoint = pukeSpot;
		level.zoomOutPoint.y = 768 - level.zoomOutPoint.y;
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
	
	public boolean galaxies;
	public void defendGalaxy(){
		if (galaxies) return;
		galaxies = true;
		LevelManager lm = LudumDare31.levelMgr;
		lm.addLevel(6); // lets ATC
	}
	
	public boolean snowmanTime;
	public void snowman(){
		if (snowmanTime) return;
		snowmanTime = true;
		LevelManager lm = LudumDare31.levelMgr;
		lm.addLevel(7); // lets ATC
	}
}