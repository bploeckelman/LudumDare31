package lando.systems.ld31;

import com.badlogic.gdx.graphics.Texture;
import levels.InsectUtils.InsectsAssets;

import java.util.Random;

/**
 * Brian Ploeckelman created on 12/6/2014.
 */
public class Assets {

    // ----- Textures ---------------------------
    public static Texture ludumdare;
    public static Texture squareTex;
    public static Random rand;

    // Planetary
    public static Texture planetaryTempEarth;
    public static Texture planetaryTempExplosion;
    public static Texture planetaryTempRocket;

    // Insects
    public static InsectsAssets insectsAssets;




    // ----- Audio ------------------------------
//    public static Music music;

//    public static Sound sfx1;


    public static void load() {
        ludumdare = new Texture("ludumdare.png");
        squareTex = new Texture("square.png");
        planetaryTempEarth = new Texture("planetary/temp-earth.png");
        planetaryTempExplosion = new Texture("planetary/temp-missle-explosion.png");
        planetaryTempRocket = new Texture("planetary/temp_rocket.png");
        rand = new Random();
        insectsAssets = new InsectsAssets();
    }

    public static void dispose() {
        ludumdare.dispose();
    }

}
