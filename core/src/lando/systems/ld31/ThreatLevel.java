package lando.systems.ld31;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.math.MathUtils;

public class ThreatLevel {
	
	static Map<String, ThreatLevel> _threats = new HashMap<String, ThreatLevel>();
	
	public static int getThreatLevel(String screen) {
		return getThreat(screen).getThreatLevel();
	}
	
	private static ThreatLevel getThreat(String key) {
		if (!_threats.containsKey(key)) {
			_threats.put(key,  new ThreatLevel());
		}
		
		return _threats.get(key);
	}
	
	public static void addThreat(String screen, int value) {
		getThreat(screen).add(value);
	}
	
	public static void reset(String screen)
	{
		getThreat(screen).reset();
	}	

	public static void set(String screen, int i) {
		getThreat(screen).set(i);
	}

	private int _level;
	
	private void reset() {
		_level = 0;
	}
	
	private void add(int value) {
		_level += value;
	}
	
	private int getThreatLevel() {
		return MathUtils.clamp(_level / 25, 0, 3);
	}

	private void set(int i) {
		_level = i;
	}
}
