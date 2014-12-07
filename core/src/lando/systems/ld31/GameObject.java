package lando.systems.ld31;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class GameObject {

    public abstract void draw(SpriteBatch batch);
    
    public static Sound getSound(String path) {
    	return Gdx.audio.newSound(Gdx.files.internal(path));
    }
}
