package levels;

import java.util.ArrayList;
import java.util.List;

import levels.galactic.Galaxy;
import levels.galactic.MilkyWay;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;


public class GalacticLevel extends GameLevel {
	MilkyWay home;
	List<Galaxy> galaxies = new ArrayList<Galaxy>();
	
	public GalacticLevel(){
		home = new MilkyWay(camera.viewportWidth/2.0f, camera.viewportHeight/2.0f);
		galaxies.add(new Galaxy(new Vector2(0, 0), new Vector2(0,0)));
	}
	
	@Override
	public int hasThreat() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void handleInput(float dt) {
		// TODO Auto-generated method stub


	}

	@Override
	public void update(float dt) {
		for (int i = 0; i < galaxies.size(); i++){
			galaxies.get(i).update(dt);
		}

	}

	@Override
	public void draw(SpriteBatch batch) {
		// TODO Auto-generated method stub
		home.draw(batch);
		for (int i = 0; i < galaxies.size(); i++){
			galaxies.get(i).draw(batch);
		}
	}

}
