package levels.human;

import lando.systems.ld31.GameObject;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ScaleImage extends GameObject {

	public float x;
	public float y;
	
	public float rotation;
	
	public float width;
	public float height;
	
	protected boolean flipImage = false;
	
	Texture _image;
	
	public ScaleImage(Texture image, float height) {
		_image = image;		
		setHeight(height);	}
	
	public void setHeight(float height) {
		this.height = height;
		width = (int)((float)height/_image.getHeight()*_image.getWidth());
	}
	
	protected Texture getImage() {
		return _image;
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		Texture image = getImage();
		batch.draw(image, x, y, width/2, height/2, width, height, 1f, 1f,
				rotation, 0, 0, image.getWidth(), image.getHeight(), flipImage, false);	
	}
}
