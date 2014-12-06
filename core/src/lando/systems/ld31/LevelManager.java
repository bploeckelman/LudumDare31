package lando.systems.ld31;

import levels.*;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LevelManager {

	public GameLevel[] levels = new GameLevel[7];
	public int currentLevel;
	
	public LevelManager(){
		levels[4] = new DemoLevel();
		currentLevel = 4;
	}
	
	public void setLevel(int index){
		currentLevel = index;
		//TODO make a way to transition these
	}
	
	public void update(float dt){
		for (int i = 0; i < levels.length; i++){
			if (levels[i] == null) continue;
			levels[i].update(dt);
			if (i == currentLevel){
				levels[i].handleInput(dt);
			}
		}
	}
	
	public void render(SpriteBatch batch){
		levels[currentLevel].render(batch);
	}
}
