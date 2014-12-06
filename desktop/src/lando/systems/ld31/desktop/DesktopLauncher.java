package lando.systems.ld31.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import lando.systems.ld31.LudumDare31;

public class DesktopLauncher {
    public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Ludum Dare 31 entry by Lando Systems";
        config.resizable = false;
        new LwjglApplication(new LudumDare31(), config);
    }
}
