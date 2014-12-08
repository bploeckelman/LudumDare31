package levels.InsectUtils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import javax.xml.soap.Text;

/**
 * Created by jhoopes on 12/6/14.
 */
public class InsectsAssets {


    public static Texture HUDTile;
    public static Texture Bar;
    public static Texture Path;
    public static Texture Beer;
    public static Texture Blank;
    public static Texture Background;

    public static Texture Spider;
    public static TextureAtlas Ant;

    public static Texture DartTower;

    public static Texture DartBullet;

    public static Texture TowerSelectBG;
    public static Texture TowerSelectBGSelected;

    public InsectsAssets(){

        HUDTile = new Texture("insects/HUDtile.png");
        Bar = new Texture("insects/Bar.png");
        Beer = new Texture("insects/Beer.png");
        Path = new Texture("insects/Path.png");
        Blank = new Texture("insects/Blank.png");
        Background = new Texture("insects/wood_grain.jpg");

        Spider = new Texture("insects/Spider.png");

        DartTower = new Texture("insects/DartTower.png");

        DartBullet = new Texture("insects/DartBullet.png");

        TowerSelectBG = new Texture("insects/TowerSelectBG.png");
        TowerSelectBGSelected = new Texture("insects/TowerSelectBG-selected.png");


         Ant = new TextureAtlas("insects/ant.atlas");

    }

    public void dispose(){
        Bar.dispose();;
        Path.dispose();
        Beer.dispose();
    }

}
