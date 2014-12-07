package levels.human;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class PatronInfo {
	public Texture image;
	public Vector2 mouthOffset;
	public Vector2 hatOffset;
	public float hatRotation;
	
	public PatronInfo(String image, Vector2 mouth, Vector2 hat, float hatRot) {
		this.image = new Texture(image);
		mouthOffset = mouth;
		hatOffset = hat;
		hatRotation = hatRot;
	}		
}
