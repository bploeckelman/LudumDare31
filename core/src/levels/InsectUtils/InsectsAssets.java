package levels.InsectUtils;

import com.badlogic.gdx.graphics.Texture;

import javax.xml.soap.Text;

/**
 * Created by jhoopes on 12/6/14.
 */
public class InsectsAssets {

    public static Texture Bar;
    public static Texture Path;
    public static Texture Beer;

    public static Texture Spider;

    public InsectsAssets(){

        Bar = new Texture("insects/Bar.png");
        Beer = new Texture("insects/Beer.png");
        Path = new Texture("insects/Path.png");

        Spider = new Texture("insects/Spider.png");
    }

    public void dispose(){
        Bar.dispose();;
        Path.dispose();
        Beer.dispose();
    }

}
