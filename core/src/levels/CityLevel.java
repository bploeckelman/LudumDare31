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

    final int tile_size  = 32;
    final int tiles_wide = GameConstants.ScreenWidth  / tile_size - 2;
    final int tiles_high = GameConstants.ScreenHeight / tile_size - 2;
    final float margin_bottom = tile_size * 2;

    // ------- OLD SHIT vvvvvv --------------------------

    Map<CityTileTypes, TextureRegion> textures;
    CityTileTypes tiles[][];
    CityTileTypes powerlines[][];

    CityPowerSource[] powerSources;
    int barx, bary;

    // ------- OLD SHIT ^^^^^^ --------------------------

    PowerTile powerGrid[][];


    public CityLevel() {
        CityAssets.load();

        textures = new HashMap<CityTileTypes, TextureRegion>();
        populateTextures();

        powerSources = new CityPowerSource[4];
        powerlines = new CityTileTypes[tiles_high][tiles_wide];
        tiles = new CityTileTypes[tiles_high][tiles_wide];

        generatePowerGrid();
        populateTiles();
        enableGenerators();
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

    Vector3 screenPos = new Vector3();
    Vector3 worldPos = new Vector3();
    @Override
    public void handleInput ( float dt){
        if (Gdx.input.justTouched()) {
            screenPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            worldPos = camera.unproject(screenPos);

            int x = (int) (worldPos.x / tile_size);
            int y = (int) ((worldPos.y - margin_bottom) / tile_size);
            activatePowerGridTile(x, y);
        }
    }

    private void activatePowerGridTile(int x, int y) {
        if (x < 0 || y < 0 || x >= tiles_wide || y >= tiles_high) return;

        PowerTile other;
        PowerTile tile = powerGrid[y][x];
        if (y + 1 <  tiles_high) {
            other = powerGrid[y + 1][x];
            if (other.down == tile || other.energized) {
                tile.energized = true;
                tile.up = other;
            }
        }
        if (y - 1 >= 0) {
            other = powerGrid[y - 1][x];
            if (other.up == tile || other.energized) {
                tile.energized = true;
                tile.down = other;
            }
        }
        if (x - 1 >= 0) {
            other = powerGrid[y][x - 1];
            if (other.right == tile || other.energized) {
                tile.energized = true;
                tile.left = other;
            }
        }
        if (x + 1 <  tiles_wide) {
            other = powerGrid[y][x + 1];
            if (other.left == tile || other.energized) {
                tile.energized = true;
                tile.right = other;
            }
        }
    }

    @Override
    public void update(float dt) {
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
        CityTileTypes powerGridType;
        for (int y = 0; y < tiles_high; ++y) {
            for (int x = 0; x < tiles_wide; ++x) {
                powerGridType = getPowerGridType(powerGrid[y][x]);
                batch.draw(textures.get(powerGridType),
                        x * tile_size, y * tile_size + margin_bottom, tile_size, tile_size);
            }
        }
    }

    private CityTileTypes getPowerGridType(PowerTile powerTile) {
        boolean hasUp    = powerTile.up    != null;
        boolean hasDown  = powerTile.down  != null;
        boolean hasLeft  = powerTile.left  != null;
        boolean hasRight = powerTile.right != null;

        if      (!hasUp && !hasDown && !hasLeft && !hasRight) return CityTileTypes.empty;
        else if ( hasUp &&  hasDown &&  hasLeft &&  hasRight) return CityTileTypes.power_line_x;
        else if (!hasUp &&  hasDown &&  hasLeft &&  hasRight) return CityTileTypes.power_line_down_t;
        else if ( hasUp && !hasDown &&  hasLeft &&  hasRight) return CityTileTypes.power_line_up_t;
        else if ( hasUp &&  hasDown && !hasLeft &&  hasRight) return CityTileTypes.power_line_right_t;
        else if ( hasUp &&  hasDown &&  hasLeft && !hasRight) return CityTileTypes.power_line_left_t;
        else if ( hasUp && !hasDown &&  hasLeft && !hasRight) return CityTileTypes.power_line_corner_lt;
        else if ( hasUp && !hasDown && !hasLeft &&  hasRight) return CityTileTypes.power_line_corner_rt;
        else if (!hasUp &&  hasDown &&  hasLeft && !hasRight) return CityTileTypes.power_line_corner_lb;
        else if (!hasUp &&  hasDown && !hasLeft &&  hasRight) return CityTileTypes.power_line_corner_rb;
        else if ( hasUp   || hasDown  )                       return CityTileTypes.power_line_v;
        else if ( hasLeft || hasRight )                       return CityTileTypes.power_line_h;

        return CityTileTypes.empty;
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
