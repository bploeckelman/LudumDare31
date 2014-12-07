package levels.InsectUtils;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by jhoopes on 12/6/14.
 */
public abstract class MapTiles {

    protected String name = "MapTile";
    protected Texture tileTexture;
    protected boolean hasTower;

    public String getName(){
        return this.name;
    }

    public Texture getTileTexture(){
        return tileTexture;
    }

    public void setHasTower(boolean hasTower){
        this.hasTower = hasTower;
    }

    public boolean hasTower(){
        return this.hasTower;
    }
}
