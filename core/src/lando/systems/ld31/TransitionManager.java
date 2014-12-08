package lando.systems.ld31;

import com.badlogic.gdx.math.Vector2;

import levels.GameLevel;

public class TransitionManager {
	
	public static final TransitionManager Instance = new TransitionManager();
	
	public boolean vomitHandled;
	
	public void handleVomit(float x, float y, float width, float height)
	{
		if (vomitHandled) return;
		vomitHandled = true;
		
		LevelManager lm = LudumDare31.levelMgr;
		
		GameLevel level = lm.levels[lm.currentLevel];
		level.zoomOutPoint = new Vector2(x + width/2, y + height/2);

		lm.addLevel(2);
	}
}