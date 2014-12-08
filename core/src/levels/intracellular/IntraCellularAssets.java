package levels.intracellular;

import com.badlogic.gdx.graphics.Texture;

public class IntraCellularAssets {
    public static Texture background;
    public static Texture ship;
    public static Texture shipThrusting;
    public static Texture asteroid;
    public static Texture bullet;

    public static final String asteroidExplode = "intracellular/virusBreak.mp3";
    public static final String shipExplode = "intracellular/shipExplode.mp3";
    public static final String shipThrust = "intracellular/shipThrust.mp3";
    public static final String shipShoot = "intracellular/shipShoot.mp3";

    public static void init() {
        background = new Texture("intracellular/background_924x768.png");
        ship = new Texture("intracellular/ship.png");
        shipThrusting = new Texture("intracellular/shipThrusting.png");
        asteroid = new Texture("intracellular/virus_128x128.png");
        bullet = new Texture("intracellular/bullet.png");
    }

    public static void dispose() {
        background.dispose();
        ship.dispose();
        shipThrusting.dispose();
        asteroid.dispose();
        bullet.dispose();
    }
}
