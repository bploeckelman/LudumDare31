package levels.human;

import lando.systems.ld31.Assets;

import com.badlogic.gdx.graphics.Texture;

public class PatronFactory {
	
	private Texture[] _patrons = new Texture[] {
			new Texture(HumanAssets.Patron1),
			new Texture(HumanAssets.Patron2),
			new Texture(HumanAssets.Patron3)			
	};
		
	public Patron getPatron(int y) {
		Texture image = _patrons[Assets.rand.nextInt(_patrons.length)];		
		Patron patron = new Patron(image, 90, -image.getWidth(), y);
		patron.speed = 100 + Assets.rand.nextInt(100);
		return patron;
	}
}
