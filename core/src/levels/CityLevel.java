package levels;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lando.systems.ld31.Assets;
import lando.systems.ld31.GameConstants;
import levels.citylevel.CityAssets;
import levels.citylevel.CityTiles;

import java.util.HashMap;
import java.util.Map;

/**
 * Brian Ploeckelman created on 12/6/2014.
 */
public class CityLevel extends GameLevel {

    final int tile_size  = 32;
    final int tiles_wide = GameConstants.ScreenWidth / tile_size - 2;
    final int tiles_high = GameConstants.ScreenHeight / tile_size;

    Map<CityTiles, TextureRegion> textures;
    CityTiles tiles[][];

    public CityLevel() {
        CityAssets.load();

        textures = new HashMap<CityTiles, TextureRegion>();
        populateTextures();

        tiles = new CityTiles[tiles_high][tiles_wide];
        populateTiles();
    }

    @Override
    public void handleInput(float dt) {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.setProjectionMatrix(camera.combined);
        batch.draw(Assets.squareTex, 0, 0, camera.viewportWidth, camera.viewportHeight);
        for (int y = 0; y < tiles_high; ++y) {
            for (int x = 0; x < tiles_wide; ++x) {
                batch.draw(textures.get(tiles[y][x]), x * tile_size, y * tile_size, tile_size, tile_size);
            }
        }
    }

    @Override
    public int hasThreat() {
        return 0;
    }

    // ------------------------------------------------------------------------
    private void populateTextures() {
        textures.put(CityTiles.bar,            CityAssets.bar);

        textures.put(CityTiles.house1,         CityAssets.house1);
        textures.put(CityTiles.house2,         CityAssets.house2);
        textures.put(CityTiles.house3,         CityAssets.house3);

        textures.put(CityTiles.road_h,         CityAssets.road_h);
        textures.put(CityTiles.road_v,         CityAssets.road_v);
        textures.put(CityTiles.road_x,         CityAssets.road_x);
        textures.put(CityTiles.road_corner_lt, CityAssets.road_corner_lt);
        textures.put(CityTiles.road_corner_rt, CityAssets.road_corner_rt);
        textures.put(CityTiles.road_corner_lb, CityAssets.road_corner_lb);
        textures.put(CityTiles.road_corner_rb, CityAssets.road_corner_rb);

        textures.put(CityTiles.power_station,        CityAssets.power_station);
        textures.put(CityTiles.power_pole,           CityAssets.power_pole);
        textures.put(CityTiles.power_line_h,         CityAssets.power_line_h);
        textures.put(CityTiles.power_line_v,         CityAssets.power_line_v);
        textures.put(CityTiles.power_line_x,         CityAssets.power_line_x);
        textures.put(CityTiles.power_line_corner_lt, CityAssets.power_line_corner_lt);
        textures.put(CityTiles.power_line_corner_rt, CityAssets.power_line_corner_rt);
        textures.put(CityTiles.power_line_corner_lb, CityAssets.power_line_corner_lb);
        textures.put(CityTiles.power_line_corner_rb, CityAssets.power_line_corner_rb);
    }

    private void populateTiles() {
        for (int y = 0; y < tiles_high; ++y) {
            for (int x = 0; x < tiles_wide; ++x) {
                tiles[y][x] = CityTiles.values()[Assets.rand.nextInt(CityTiles.values().length)];
            }
        }
    }


}
