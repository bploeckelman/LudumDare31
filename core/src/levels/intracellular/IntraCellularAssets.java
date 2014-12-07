package levels.intracellular;

import com.badlogic.gdx.graphics.Texture;

public class IntraCellularAssets {
    public static Texture background;
    public static Texture ship;
    public static Texture asteroid;
    public static Texture bullet;

    public static void init() {
        background = new Texture("intracellular/background_924x768.png");
        ship = new Texture("intracellular/ship.png");
        asteroid = new Texture("intracellular/virus_128x128.png");
        bullet = new Texture("intracellular/bullet.png");
    }

    public static void dispose() {
        background.dispose();
        ship.dispose();
        asteroid.dispose();
        bullet.dispose();
    }
}
