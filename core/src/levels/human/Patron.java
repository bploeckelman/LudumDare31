package levels.human;

import com.badlogic.gdx.graphics.Texture;

public class Patron extends MovementImage {
	
	public static int maxX;
	
	public Patron(Texture image, int height, int x, int y) {
		super(image, height, 0f);
		
		this.x = x;
		this.y = y;
	}
	
	@Override
	protected boolean checkX() {
		return x > (maxX - width);
	}
}
