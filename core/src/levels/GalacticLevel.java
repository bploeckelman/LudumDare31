package levels;

import java.util.ArrayList;
import java.util.List;

import lando.systems.ld31.Assets;
import levels.galactic.Galaxy;
import levels.galactic.MilkyWay;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;


public class GalacticLevel extends GameLevel {
	MilkyWay home;
	List<Galaxy> galaxies = new ArrayList<Galaxy>();
	float spawnTimer = 0;
	
	public GalacticLevel(){
		tutorialText = "The beings of your galaxy have noticed your fine managing skills.\n\n" +
					"You've been tasked with keeping the galaxies from colliding.\n\n" +
				"Drag paths to keep the galaxies from colliding with yours  ..and\nDon't forget your patrons.";
		home = new MilkyWay(new Vector2(camera.viewportWidth/2.0f, camera.viewportHeight/2.0f));
		galaxies.add(home);
		galaxies.add(new Galaxy(new Vector2(camera.viewportWidth/2.0f, 0)));
		galaxies.add(new Galaxy(new Vector2(0, camera.viewportHeight/2.0f)));
	}
	
	@Override
	public int hasThreat() {
		float closest = Float.MAX_VALUE;
		for (int i = 0; i < galaxies.size(); i++){
			Galaxy gal = galaxies.get(i);
			if (gal == home) continue;
			if (gal.path.isEmpty()){
				float dist = home.pos.dst2(gal.pos) - home.width/2 - gal.width/2;
				if (dist < closest){
					closest = dist;
				}
			}
		}
		if (closest < 100 * 100){
			return 3;
		}
		if (closest < 200 * 200){
			return 2;
		}
		if (closest < 300 * 300){
			return 1;
		}
		return 0;
	}

	@Override
	public void handleInput(float dt) {
		// TODO Auto-generated method stub


	}
	
	public void addNewGalaxy(){
		Vector2 dir = new Vector2(1,0);
		dir.rotate(Assets.rand.nextFloat() * 360);
		dir.scl(1200);
		galaxies.add(new Galaxy(dir));
	}
	
	Galaxy touchedGalaxy;
	
	@Override
	public boolean touchDown(int screenX, int screenY, int button) {
		Vector2 gamePos = getGamePos(new Vector2(screenX, screenY));
		for (int i = 0; i < galaxies.size(); i++){
			Galaxy gal = galaxies.get(i);
			if (gal == home) continue;
			if (gamePos.cpy().dst(gal.getPos()) < gal.width){
				touchedGalaxy = gal;
				touchedGalaxy.path.clear();
				break;
			}
		}
		return false;
	};
	
	@Override
	public boolean touchUp(int screenX, int screenY, int button) {
		super.touchUp(screenX, screenY, button);
		touchedGalaxy = null;
		return false;
	};
	
	@Override
	public boolean touchDragged(int screenX, int screenY) {
		if (touchedGalaxy != null){
			Vector2 gamePos = getGamePos(new Vector2(screenX, screenY));
			touchedGalaxy.path.add(gamePos);
		}
		return false;
	};

	@Override
	public void update(float dt) {
		for (int i = galaxies.size() -1; i >= 0; i--){
			galaxies.get(i).update(dt, galaxies);
			if (!galaxies.get(i).alive) galaxies.remove(i);
		}
		
		spawnTimer -= dt;
		if (spawnTimer < 0){
			addNewGalaxy();
			spawnTimer += (20 + Assets.rand.nextFloat() * 15);

		}

	}

	@Override
	public void draw(SpriteBatch batch) {
		// TODO Auto-generated method stub
		batch.draw(Assets.galaxyBackground, 0, 0, camera.viewportWidth, camera.viewportHeight);
		
		for (int i = 0; i < galaxies.size(); i++){
			galaxies.get(i).draw(batch);
		}
	}

}
