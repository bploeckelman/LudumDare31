package levels.planetary;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lando.systems.ld31.Assets;
import lando.systems.ld31.GameObject;

public class Earth extends GameObject {

    private Texture baseEarth;
    private int x;
    private int y;
    private float r;

    private static final int W = 64;
    private static final int H = 64;
    private static double hW;
    private static double hH;

    public Earth(int x, int y, float r) {
        baseEarth = Assets.planetaryTempEarth;
        this.x = x;
        this.y = y;
        this.r = r;
        Earth.hW = Earth.W / 2;
        Earth.hH = Earth.H / 2;
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(baseEarth, Math.round(x-hW), Math.round(y-hH));
    }
}
