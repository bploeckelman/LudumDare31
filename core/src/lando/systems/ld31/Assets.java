package lando.systems.ld31;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
<<<<<<< Updated upstream
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

=======
>>>>>>> Stashed changes
import levels.InsectUtils.InsectsAssets;

import java.util.ArrayList;
import java.util.Random;

/**
 * Brian Ploeckelman created on 12/6/2014.
 */
public class Assets {
	
    // ----- Textures ---------------------------
    public static Texture ludumdare;
    public static Texture squareTex;
    public static Random rand;
    public static Texture milkyWay;
    public static Texture bloodVessel;

    // Planetary
    public static Texture planetaryTempExplosion;
    public static Texture planetaryTempRocket;
    public static ArrayList<Texture> plAsteroids;
    public static ArrayList<Texture> plComets;
    public static Texture plEarth;
    public static Texture plEarthNightLights;
    public static Texture plEarthShadowMask;
    public static Texture plMoon;
    public static Texture plMoonShadow;
    public static Texture plStarBackdrop;

    // Insects
    public static InsectsAssets insectsAssets;

    public static Texture sidebarBackground;
    public static Texture sidebarBlack;
    public static Texture[] sidebarStatus = new Texture[4];
    public static Texture[] sidebarLabels = new Texture[7];
    public static Texture sidebarSelection;
    public static BitmapFont gameFont;
    public static BitmapFont smallFont;
    
    public static TextureRegion bloodRegions[][];



    // ----- Audio ------------------------------
//    public static Music music;

//    public static Sound sfx1;


    public static void load() {
    	gameFont = new BitmapFont(Gdx.files.internal("font/ariel.fnt"), false);
    	smallFont = new BitmapFont(Gdx.files.internal("font/book30b.fnt"), false);
    	
        ludumdare = new Texture("ludumdare.png");
        squareTex = new Texture("square.png");
        milkyWay = new Texture("galactic/milkyway.png");

        // Planetary
        plAsteroids = new ArrayList<Texture>();
        plAsteroids.add(new Texture("planetary/asteroid_1.png"));
        plAsteroids.add(new Texture("planetary/asteroid_2.png"));
        plAsteroids.add(new Texture("planetary/asteroid_3.png"));
        plComets = new ArrayList<Texture>();
        plComets.add(new Texture("planetary/comet_1.png"));
        plComets.add(new Texture("planetary/comet_2.png"));
        plComets.add(new Texture("planetary/comet_3.png"));
        plEarth = new Texture("planetary/earth_texture.png");
        plEarthNightLights = new Texture("planetary/earth_night_lights.png");
        plEarthShadowMask = new Texture("planetary/earth_shadow_mask.png");
        plMoon = new Texture("planetary/moon_texture.png");
        plMoonShadow = new Texture("planetary/moon_shadow.png");
        plStarBackdrop = new Texture("planetary/star_backdrop.png");

        planetaryTempExplosion = new Texture("planetary/temp-missle-explosion.png");
        planetaryTempRocket = new Texture("planetary/temp_rocket.png");


        sidebarBackground = new Texture("sidebar/side_meter_backdrop.png");
        sidebarBlack= new Texture("sidebar/side_meter_fill_black.png");
        sidebarStatus[0] = new Texture("sidebar/side_meter_fill_green.png");
        sidebarStatus[1] = new Texture("sidebar/side_meter_fill_yellow.png");
        sidebarStatus[2] = new Texture("sidebar/side_meter_fill_orange.png");
        sidebarStatus[3] = new Texture("sidebar/side_meter_fill_red.png");
        sidebarLabels[0] = new Texture("sidebar/side_meter_0001x.png");
        sidebarLabels[1] = new Texture("sidebar/side_meter_001x.png");
        sidebarLabels[2] = new Texture("sidebar/side_meter_01x.png");
        sidebarLabels[3] = new Texture("sidebar/side_meter_1x.png");
        sidebarLabels[4] = new Texture("sidebar/side_meter_10x.png");
        sidebarLabels[5] = new Texture("sidebar/side_meter_100x.png");
        sidebarLabels[6] = new Texture("sidebar/side_meter_1000x.png");
        sidebarSelection = new Texture("sidebar/selection.png");
        bloodVessel = new Texture("intercellular/blood_vessel_01.png");
        bloodRegions = TextureRegion.split(new Texture("intercellular/a_final_blood.png"), 32, 32);
        rand = new Random();
        insectsAssets = new InsectsAssets();
    }

    public static void dispose() {
        ludumdare.dispose();
    }

}
