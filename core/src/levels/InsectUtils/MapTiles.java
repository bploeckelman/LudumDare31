package levels.InsectUtils;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by jhoopes on 12/6/14.
 */
public abstract class MapTiles {

    protected String name = "MapTile";
    protected Texture tileTexture;

    public String getName(){
        return this.name;
    }

    public Texture getTileTexture(){
        return tileTexture;
    }
}
