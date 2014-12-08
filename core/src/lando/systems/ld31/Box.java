package lando.systems.ld31;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Box {
	
	public static void draw(SpriteBatch batch, float x, float y, float width, float height, Color color) {
		draw(batch, x, y, width, height, color, 2f, Color.WHITE);
	}
	
	public static void draw(SpriteBatch batch, float x, float y, float width, float height, Color color, float thickness, Color borderColor) {
		batch.setColor(borderColor);
		
		batch.draw(Assets.squareTex, x, y, width, height);
		batch.setColor(color);
		
		x += thickness;
		y += thickness;
		
		width -= (thickness*2);
		height -= (thickness*2);
		

		batch.draw(Assets.squareTex, x, y, width, height);
		batch.setColor(Color.WHITE);
	}
}
