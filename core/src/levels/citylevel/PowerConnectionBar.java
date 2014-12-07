package levels.citylevel;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import levels.CityLevel;

/**
 * Brian Ploeckelman created on 12/7/2014.
 */
public class PowerConnectionBar {


    final int queue_tiles_wide = 4;
    final int tile_size = CityLevel.tile_size * 2;

    final float grid_width = CityLevel.tiles_wide * CityLevel.tile_size;
    final float queue_width = tile_size * queue_tiles_wide;
    final float margin_left = grid_width / 2f - queue_width / 2f;

    CityTileTypes[] powerLineQueue;

    public CityTileTypes currentPowerLineType;

    public PowerConnectionBar() {
        initializeQueue();
        currentPowerLineType = CityTileTypes.getRandomPowerLineType();
    }

    public void update(float dt) {
        // ...
    }

    public void draw(SpriteBatch batch) {
        batch.setColor(0.4f, 0.4f, 0.4f, 1.0f);

        // Draw the power line grid type in each cell
        batch.draw(CityLevel.textures.get(powerLineQueue[0]), margin_left + tile_size * 0, 4, tile_size - 8, tile_size - 8);
        batch.draw(CityLevel.textures.get(powerLineQueue[1]), margin_left + tile_size * 1, 4, tile_size - 8, tile_size - 8);
        batch.draw(CityLevel.textures.get(powerLineQueue[2]), margin_left + tile_size * 2, 4, tile_size - 8, tile_size - 8);
        batch.draw(CityLevel.textures.get(powerLineQueue[3]), margin_left + tile_size * 3, 4, tile_size - 8, tile_size - 8);

        // Draw cell borders
        batch.draw(CityAssets.empty, margin_left + tile_size * 0, 4, tile_size - 8, tile_size - 8);
        batch.draw(CityAssets.empty, margin_left + tile_size * 1, 4, tile_size - 8, tile_size - 8);
        batch.draw(CityAssets.empty, margin_left + tile_size * 2, 4, tile_size - 8, tile_size - 8);
        batch.draw(CityAssets.empty, margin_left + tile_size * 3, 4, tile_size - 8, tile_size - 8);

        batch.setColor(Color.WHITE);

        // Draw the current power line grid type and cell border
        batch.draw(CityLevel.textures.get(currentPowerLineType), margin_left + tile_size * -1, 0, tile_size, tile_size);
        batch.draw(CityAssets.empty, margin_left + tile_size * -1, 0, tile_size, tile_size);
    }

    // -------------------------------------------------------------------------

    private void initializeQueue() {
        powerLineQueue = new CityTileTypes[queue_tiles_wide];
        powerLineQueue[0] = CityTileTypes.getRandomPowerLineType();
        powerLineQueue[1] = CityTileTypes.getRandomPowerLineType();
        powerLineQueue[2] = CityTileTypes.getRandomPowerLineType();
        powerLineQueue[3] = CityTileTypes.getRandomPowerLineType();
    }

    public void dequeueCurrentType() {
        // TODO (brian): tween all the things?
        currentPowerLineType = powerLineQueue[0];
        powerLineQueue[0] = powerLineQueue[1];
        powerLineQueue[1] = powerLineQueue[2];
        powerLineQueue[2] = powerLineQueue[3];
        powerLineQueue[3] = CityTileTypes.getRandomPowerLineType();
    }
}
