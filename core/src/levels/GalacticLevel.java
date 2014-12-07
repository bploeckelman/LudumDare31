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
	float spawnTimer = 0;
	
	public GalacticLevel(){
		home = new MilkyWay(new Vector2(camera.viewportWidth/2.0f, camera.viewportHeight/2.0f));
		galaxies.add(home);
		galaxies.add(new Galaxy(new Vector2(camera.viewportWidth/2.0f, 0)));
		galaxies.add(new Galaxy(new Vector2(0, camera.viewportHeight/2.0f)));
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
	
	Galaxy touchedGalaxy;
	
	@Override
	public boolean touchDown(int screenX, int screenY, int button) {
		for (int i = 0; i < galaxies.size(); i++){
			System.out.println("Down");
		}
		return false;
	};
	
	@Override
	public boolean touchUp(int screenX, int screenY, int button) {
		touchedGalaxy = null;
		return false;
	};
	
	@Override
	public boolean touchDragged(int screenX, int screenY) {
		return false;
	};

	@Override
	public void update(float dt) {
		for (int i = 0; i < galaxies.size(); i++){
			galaxies.get(i).update(dt, galaxies);
		}

	}

	@Override
	public void draw(SpriteBatch batch) {
		// TODO Auto-generated method stub
		
		for (int i = 0; i < galaxies.size(); i++){
			galaxies.get(i).draw(batch);
		}
	}

}
