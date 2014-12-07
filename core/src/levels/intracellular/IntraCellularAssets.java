package levels.intracellular;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import lando.systems.ld31.GameObject;

public class IntraCellularAssets {
    public static Texture background;
    public static Texture ship;
    public static Texture shipThrusting;
    public static Texture asteroid;
    public static Texture bullet;

    public static Sound asteroidExplode;
    public static Sound shipExplode;
    public static Sound shipThrust;
    public static Sound shipShoot;

    public static void init() {
        background = new Texture("intracellular/background_924x768.png");
        ship = new Texture("intracellular/ship.png");
        shipThrusting = new Texture("intracellular/shipThrusting.png");
        asteroid = new Texture("intracellular/virus_128x128.png");
        bullet = new Texture("intracellular/bullet.png");

        asteroidExplode = GameObject.getSound("intracellular/virusBreak.mp3");
        shipExplode = GameObject.getSound("intracellular/shipExplode.mp3");
        shipThrust = GameObject.getSound("intracellular/shipThrust.mp3");
        shipShoot = GameObject.getSound("intracellular/shipShoot.mp3");
    }

    public static void dispose() {
        background.dispose();
        ship.dispose();
        shipThrusting.dispose();
        asteroid.dispose();
        bullet.dispose();

        asteroidExplode.dispose();
        shipExplode.dispose();
        shipThrust.dispose();
        shipShoot.dispose();
    }
}
