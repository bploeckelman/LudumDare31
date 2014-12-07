package levels;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lando.systems.ld31.Assets;
import lando.systems.ld31.GameConstants;

/**
 * Created by vandillen on 12/6/14.
 */
public class IntercellularLevel extends GameLevel {

    // Constants
    private final int slotCellsForShooter = 2;
    private final int tile_size  = 32;
    private final int tiles_wide = GameConstants.ScreenWidth / tile_size - 2;
    private final int tiles_high = GameConstants.ScreenHeight / tile_size;

    // Ivars
    private int windowGameSize;
    private int[] intercellTiles;
    private int[] cellTileColorAttribute;
    private String shooter;
    private int[] incomingCellsForShooter;
    private String healthMeterName;
    private int currentHealthNumber;
    private int windowMargin;

    // Class constructor
    public IntercellularLevel() {
        windowGameSize = 150; // ** Fix this later **
        shooter = "shooter";
        currentHealthNumber = 0;
        healthMeterName = "healthMeter";
        caluculateIntercellTiles();
    }

    // 2d array to a 1d:
    // index = x + (y * width);
    // x = index%width;
    // y = index/width;

    // random bulge method initializer

    // bulge method if falling cell makes contact with bulge

    // random method for incoming cells to shoot

    // Using gameWindowSize Calculate the vein space for actual play
    public void caluculateIntercellTiles() {
        //windowMargin = windowGameSize/4; // ** Fix this later **
        //int startingTile = clearTile;
        intercellTiles = new int[5]; // ** Fix this later **

    }

    @Override
    public int hasThreat() {
        return 0;
    }

    @Override
    public void handleInput(float dt) {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void draw(SpriteBatch batch) {

    }
}
