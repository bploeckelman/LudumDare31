package lando.systems.ld31;

import java.util.Random;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

/**
 * Brian Ploeckelman created on 12/6/2014.
 */
public class Assets {

    // ----- Textures ---------------------------
    public static Texture ludumdare;
    public static Random rand;


    // ----- Audio ------------------------------
//    public static Music music;

//    public static Sound sfx1;


    public static void load() {
        ludumdare = new Texture("ludumdare.png");
        rand = new Random();
    }

    public static void dispose() {
        ludumdare.dispose();
    }

}
