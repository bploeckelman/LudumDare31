package levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import lando.systems.ld31.Assets;
import lando.systems.ld31.GameConstants;
import levels.citylevel.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Brian Ploeckelman created on 12/6/2014.
 */
public class CityLevel extends GameLevel {

    public static final int tile_size  = 32;
    public static final int tiles_wide = GameConstants.ScreenWidth  / tile_size - 3;
    public static final int tiles_high = GameConstants.ScreenHeight / tile_size - 2;
    public static final float margin_bottom = tile_size * 2;

    // ------- OLD SHIT vvvvvv --------------------------

    CityTileTypes tiles[][];
    CityTileTypes powerlines[][];

    CityPowerSource[] powerSources;
    int barx, bary;

    // ------- OLD SHIT ^^^^^^ --------------------------

    public static Map<CityTileTypes, TextureRegion> textures;

    PowerTile powerGrid[][];
    PowerConnectionBar powerBar;


    public CityLevel() {
        CityAssets.load();

        textures = new HashMap<CityTileTypes, TextureRegion>();
        populateTextures();

        powerSources = new CityPowerSource[4];
        powerlines = new CityTileTypes[tiles_high][tiles_wide];
        tiles = new CityTileTypes[tiles_high][tiles_wide];

        powerBar = new PowerConnectionBar();

        generatePowerGrid();
        populateTiles();
        enableGenerators();
    }

    Vector3 screenPos = new Vector3();
    Vector3 worldPos = new Vector3();
    Vector2 tilePos = new Vector2();
    @Override
    public void handleInput ( float dt){
        if (Gdx.input.justTouched()) {
            placePowerGridTile((int) tilePos.x, (int) tilePos.y);
        }
    }

    private void placePowerGridTile(int x, int y) {
        if (x < 0 || y < 0 || x >= tiles_wide || y >= tiles_high) {
            // TODO (brian): removeme
            Gdx.app.log("GAH!", "attempted to place power grid tile out of bounds: " + x + ", " + y);
            return;
        }

        // Place the current power tile grid at the tile under the mouse
        PowerTile thisTile = powerGrid[y][x];
        thisTile.powerGridType = powerBar.currentPowerLineType;

        // Update the power bar
        powerBar.dequeueCurrentType();

        // Connect up neighbors tiles that have a power connection
        PowerTile thatTile;
        if (y + 1 <  tiles_high) {
            thatTile = powerGrid[y + 1][x];
            if (CityTileTypes.connectsUp(thisTile.powerGridType) && CityTileTypes.connectsDown(thatTile.powerGridType)) {
                thisTile.up = thatTile;
                thatTile.down = thisTile;
            }
        }
        if (y - 1 >= 0) {
            thatTile = powerGrid[y + 1][x];
            if (CityTileTypes.connectsDown(thisTile.powerGridType) && CityTileTypes.connectsUp(thatTile.powerGridType)) {
                thisTile.down = thatTile;
                thatTile.up = thisTile;
            }
        }
        if (x - 1 >= 0) {
            thatTile = powerGrid[y + 1][x];
            if (CityTileTypes.connectsLeft(thisTile.powerGridType) && CityTileTypes.connectsRight(thatTile.powerGridType)) {
                thisTile.left = thatTile;
                thatTile.right = thisTile;
            }
        }
        if (x + 1 <  tiles_wide) {
            thatTile = powerGrid[y + 1][x];
            if (CityTileTypes.connectsRight(thisTile.powerGridType) && CityTileTypes.connectsLeft(thatTile.powerGridType)) {
                thisTile.right = thatTile;
                thatTile.left = thisTile;
            }
        }
    }

    @Override
    public void update(float dt) {
        // Update mouse positions
        screenPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        worldPos = camera.unproject(screenPos);
        tilePos.set((int)  (worldPos.x / tile_size),
                    (int) ((worldPos.y - margin_bottom) / tile_size));

        // TODO (brian): is it time for a power line to break somewhere?
        // TODO (brian): is there a threat?  how severe?
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.setProjectionMatrix(camera.combined);
        batch.setColor(0.4f, 0.4f, 0.4f, 1.0f);
        batch.draw(Assets.squareTex, 0, 0, camera.viewportWidth, camera.viewportHeight);
        for (int y = 0; y < tiles_high; ++y) {
            for (int x = 0; x < tiles_wide; ++x) {
                if (tiles[y][x] == CityTileTypes.power_station) {
                    batch.setColor(Color.YELLOW);
                    batch.draw(Assets.squareTex,
                            x * tile_size, y * tile_size + margin_bottom, tile_size, tile_size);
                    batch.setColor(0.4f, 0.4f, 0.4f, 1.0f);
                } else if (tiles[y][x] == CityTileTypes.bar) {
                    batch.setColor(Color.MAGENTA);
                    batch.draw(Assets.squareTex,
                            x * tile_size, y * tile_size + margin_bottom, tile_size, tile_size);
                    batch.setColor(0.4f, 0.4f, 0.4f, 1.0f);
                }
                batch.draw(textures.get(tiles[y][x]),
                        x * tile_size, y * tile_size + margin_bottom, tile_size, tile_size);
            }
        }

        batch.setColor(Color.WHITE);
        for (int y = 0; y < tiles_high; ++y) {
            for (int x = 0; x < tiles_wide; ++x) {
                batch.draw(textures.get(powerGrid[y][x].powerGridType),
                        x * tile_size, y * tile_size + margin_bottom, tile_size, tile_size);
            }
        }

        // HUD type stuff ---------------------
        powerBar.draw(batch);
        // Draw the currently active power grid type
        batch.draw(textures.get(powerBar.currentPowerLineType),
                tilePos.x * tile_size, tilePos.y * tile_size + margin_bottom, tile_size, tile_size);
    }

    @Override
    public int hasThreat() {
        return 0;
    }

    // ------------------------------------------------------------------------
    private void generatePowerGrid() {
        powerGrid = new PowerTile[tiles_high][tiles_wide];
        // Create all the power tiles
        for (int y = 0; y < tiles_high; ++y) {
            for (int x = 0; x < tiles_wide; ++x) {
                powerGrid[y][x] = new PowerTile();
            }
        }
        // Add graph connections
//        PowerTile tile;
//        for (int y = 0; y < tiles_high; ++y) {
//            for (int x = 0; x < tiles_wide; ++x) {
//                tile = powerGrid[y][x];
//                if (y + 1 <  tiles_high) tile.up    = powerGrid[y + 1][x];
//                if (y - 1 >= 0)          tile.down  = powerGrid[y - 1][x];
//                if (x - 1 >= 0)          tile.left  = powerGrid[y][x - 1];
//                if (x + 1 <  tiles_wide) tile.right = powerGrid[y][x + 1];
//            }
//        }
    }

    private void enableGenerators() {
        for (int i = 0; i < powerSources.length; ++i) {
            CityPowerSource powerSource = powerSources[i];
            int x = powerSource.x;
            int y = powerSource.y;

            PowerTile generator = powerGrid[y][x];
            generator.energized = true;
            if (y + 1 <  tiles_high) generator.up    = powerGrid[y + 1][x];
            if (y - 1 >= 0)          generator.down  = powerGrid[y - 1][x];
            if (x - 1 >= 0)          generator.left  = powerGrid[y][x - 1];
            if (x + 1 <  tiles_wide) generator.right = powerGrid[y][x + 1];
        }
    }

    private void populateTextures() {
        textures.put(CityTileTypes.bar,            CityAssets.bar);

        textures.put(CityTileTypes.house1,         CityAssets.house1);
        textures.put(CityTileTypes.house2,         CityAssets.house2);
        textures.put(CityTileTypes.house3,         CityAssets.house3);

        textures.put(CityTileTypes.road_h,         CityAssets.road_h);
        textures.put(CityTileTypes.road_v,         CityAssets.road_v);
        textures.put(CityTileTypes.road_x,         CityAssets.road_x);
        textures.put(CityTileTypes.road_corner_lt, CityAssets.road_corner_lt);
        textures.put(CityTileTypes.road_corner_rt, CityAssets.road_corner_rt);
        textures.put(CityTileTypes.road_corner_lb, CityAssets.road_corner_lb);
        textures.put(CityTileTypes.road_corner_rb, CityAssets.road_corner_rb);

        textures.put(CityTileTypes.power_station,        CityAssets.power_station);
        textures.put(CityTileTypes.power_pole,           CityAssets.power_pole);

        textures.put(CityTileTypes.power_line_h,         CityAssets.power_line_h);
        textures.put(CityTileTypes.power_line_v,         CityAssets.power_line_v);
        textures.put(CityTileTypes.power_line_x,         CityAssets.power_line_x);

        textures.put(CityTileTypes.power_line_corner_lt, CityAssets.power_line_corner_lt);
        textures.put(CityTileTypes.power_line_corner_rt, CityAssets.power_line_corner_rt);
        textures.put(CityTileTypes.power_line_corner_lb, CityAssets.power_line_corner_lb);
        textures.put(CityTileTypes.power_line_corner_rb, CityAssets.power_line_corner_rb);

        textures.put(CityTileTypes.power_line_up_t,      CityAssets.power_line_up_t);
        textures.put(CityTileTypes.power_line_down_t,    CityAssets.power_line_down_t);
        textures.put(CityTileTypes.power_line_left_t,    CityAssets.power_line_left_t);
        textures.put(CityTileTypes.power_line_right_t,   CityAssets.power_line_right_t);

        textures.put(CityTileTypes.empty,   CityAssets.empty);
    }

    private void populateTiles() {
        int[][] layout = new int[][] {
                { 6,  4,  4,  4,  4,  6,  4,  4,  4,  4,  4,  4,  4,  4,  4,  6,  4,  4,  4,  4,  4,  4,  4,  4,  6,  4,  4,  4,  4,  6 },
                { 5, 11,  2,  2,  1,  5,  1,  2,  1,  2,  1,  2,  1,  2,  2,  5,  1,  2,  1,  2,  1,  2,  1,  1,  5,  2,  1,  2, 11,  5 },
                { 5,  2,  1,  1,  2,  5,  2,  1,  2,  1,  2,  1,  2,  1,  1,  5,  2,  1,  2,  1,  2,  1,  2,  2,  5,  1,  2,  1,  2,  5 },
                { 6,  4,  4,  4,  4,  6,  4,  4,  4,  4,  4,  4,  4,  4,  4,  6,  4,  4,  4,  4,  4,  4,  4,  4,  6,  4,  4,  4,  4,  6 },
                { 5,  1,  2,  2,  1,  5,  1,  2,  1,  2,  1,  2,  1,  2,  2,  5,  1,  2,  1,  2,  1,  2,  1,  1,  5,  2,  1,  2,  1,  5 },
                { 5,  2,  1,  1,  2,  5,  2,  1,  2,  1,  2,  1,  2,  1,  1,  5,  2,  1,  2,  1,  2,  1,  2,  2,  5,  1,  2,  1,  2,  5 },
                { 6,  4,  4,  4,  4,  6,  4,  4,  4,  4,  4,  4,  4,  4,  4,  6,  4,  4,  4,  4,  4,  4,  4,  4,  6,  4,  4,  4,  4,  6 },
                { 5,  1,  2,  2,  1,  5,  1,  2,  1,  2,  1,  2,  1,  2,  2,  5,  1,  2,  1,  2,  1,  2,  1,  1,  5,  2,  1,  2,  1,  5 },
                { 5,  2,  1,  1,  2,  5,  2,  1,  2,  1,  2,  1,  2,  1,  1,  5,  2,  1,  2,  1,  2,  1,  2,  2,  5,  1,  2,  1,  2,  5 },
                { 6,  4,  4,  4,  4,  6,  4,  4,  4,  4,  4,  4,  4,  4,  4,  6,  4,  4,  4,  4,  4,  4,  4,  4,  6,  4,  4,  4,  4,  6 },
                { 5,  1,  2,  2,  1,  5,  1,  2,  1,  2,  1,  2,  1,  2,  2,  5,  1,  2,  1,  2,  1,  2,  1,  1,  5,  2,  1,  2,  1,  5 },
                { 5,  2,  1,  1,  2,  5,  2,  1,  2,  1,  2,  1,  2,  1,  0,  5,  2,  1,  2,  1,  2,  1,  2,  2,  5,  1,  2,  1,  2,  5 },
                { 6,  4,  4,  4,  4,  6,  4,  4,  4,  4,  4,  4,  4,  4,  4,  6,  4,  4,  4,  4,  4,  4,  4,  4,  6,  4,  4,  4,  4,  6 },
                { 5,  1,  2,  2,  1,  5,  1,  2,  1,  2,  1,  2,  1,  2,  2,  5,  1,  2,  1,  2,  1,  2,  1,  1,  5,  2,  1,  2,  1,  5 },
                { 5,  2,  1,  1,  2,  5,  2,  1,  2,  1,  2,  1,  2,  1,  1,  5,  2,  1,  2,  1,  2,  1,  2,  2,  5,  1,  2,  1,  2,  5 },
                { 6,  4,  4,  4,  4,  6,  4,  4,  4,  4,  4,  4,  4,  4,  4,  6,  4,  4,  4,  4,  4,  4,  4,  4,  6,  4,  4,  4,  4,  6 },
                { 5,  1,  2,  2,  1,  5,  1,  2,  1,  2,  1,  2,  1,  2,  2,  5,  1,  2,  1,  2,  1,  2,  1,  1,  5,  2,  1,  2,  1,  5 },
                { 5,  2,  1,  1,  2,  5,  2,  1,  2,  1,  2,  1,  2,  1,  1,  5,  2,  1,  2,  1,  2,  1,  2,  2,  5,  1,  2,  1,  2,  5 },
                { 6,  4,  4,  4,  4,  6,  4,  4,  4,  4,  4,  4,  4,  4,  4,  6,  4,  4,  4,  4,  4,  4,  4,  4,  6,  4,  4,  4,  4,  6 },
                { 5,  1,  2,  2,  1,  5,  1,  2,  1,  2,  1,  2,  1,  2,  2,  5,  1,  2,  1,  2,  1,  2,  1,  1,  5,  2,  1,  2,  1,  5 },
                { 5, 11,  1,  1,  2,  5,  2,  1,  2,  1,  2,  1,  2,  1,  1,  5,  2,  1,  2,  1,  2,  1,  2,  2,  5,  1,  2,  1, 11,  5 },
                { 6,  4,  4,  4,  4,  6,  4,  4,  4,  4,  4,  4,  4,  4,  4,  6,  4,  4,  4,  4,  4,  4,  4,  4,  6,  4,  4,  4,  4,  6 },
        };

        int i = 0;
        for (int y = 0; y < tiles_high; ++y) {
            for (int x = 0; x < tiles_wide; ++x) {
                tiles[y][x] = CityTileTypes.getTile(layout[tiles_high - y - 1][x]);
                if (tiles[y][x] == CityTileTypes.power_station) {
                    powerSources[i++] = new CityPowerSource(x,y);
                }
                else if (tiles[y][x] == CityTileTypes.bar) {
                    barx = x;
                    bary = y;
                }
            }
        }
    }


}
