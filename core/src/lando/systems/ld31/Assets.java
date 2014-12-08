package lando.systems.ld31;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

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
    public static Texture milkyWay;
    public static Texture bloodVessel;

    // Planetary
    public static Texture planetaryTempEarth;
    public static Texture planetaryTempExplosion;
    public static Texture planetaryTempRocket;

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
        planetaryTempEarth = new Texture("planetary/temp-earth.png");
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
