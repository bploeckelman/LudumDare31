package levels.galactic;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import lando.systems.ld31.Assets;

public class MilkyWay extends Galaxy{


	
	public MilkyWay(Vector2 pos){
		super(pos);
		sprite = new Sprite(Assets.squareTex);
		sprite.setOriginCenter();
		sprite.setSize(40, 40);
		sprite.setCenter(pos.x, pos.y);
		mass = 20;

	}
	
	@Override
	public void update(float dt, List<Galaxy> gals){
		//TODO check for destruction
	}


}
