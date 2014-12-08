package lando.systems.ld31;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {
	
	private static Map<String, Sound> _sounds = new HashMap<String, Sound>();
	
	public static Sound getSound(String path) {		
		if (!_sounds.containsKey(path)) {
			_sounds.put(path, Gdx.audio.newSound(Gdx.files.internal(path)));
		}
		
		return _sounds.get(path);
    }
	
	public static void clear() {
		for (Sound sound : _sounds.values()) {
			sound.dispose();
		}
		_sounds.clear();
	}
	
	// returns sound id or -1 if failure
	public static long play(String screenTitle, String soundPath) {
		return play(screenTitle, getSound(soundPath), 1f);
	}
		
	// returns sound id or -1 if failure
	public static long play(String screenTitle, String soundPath, float volume) {
		return play(screenTitle, getSound(soundPath), volume);
	}
	
	// returns sound id or -1 if failure
	public static long play(String screenTitle, Sound sound, float volume) {
		long value = -1;
		if (LevelManager.isLevelActive(screenTitle)) {
			value = sound.play(volume);
		}
		return value;
	}
}
