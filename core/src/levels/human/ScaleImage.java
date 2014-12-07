package levels.human;

import lando.systems.ld31.GameObject;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ScaleImage extends GameObject {

	public int x;
	public int y;
	
	public int width;
	public int height;
	
	protected boolean flipImage = false;
	
	Texture _image;
	
	public ScaleImage(Texture image, int height) {
		_image = image;		
		setHeight(height);	}
	
	public void setHeight(int height) {
		this.height = height;
		width = (int)((float)height/_image.getHeight()*_image.getWidth());
	}
	
	protected Texture getImage() {
		return _image;
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		int drawX = x;
		int w = width;
		if (flipImage) {
			drawX += width;
			w = -width;
		}
		batch.draw(getImage(),  drawX,  y,  w,  height);
	}
}
