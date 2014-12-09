package levels;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.*;
import aurelienribon.tweenengine.primitives.MutableFloat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import lando.systems.ld31.*;
import levels.citylevel.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Brian Ploeckelman created on 12/6/2014.
 */
public class CityLevel extends GameLevel {

    public static final int tile_size  = 32;
    public static final int tiles_wide = GameConstants.ScreenWidth  / tile_size - 3;
    public static final int tiles_high = GameConstants.ScreenHeight / tile_size - 2;
    public static final float margin_bottom = tile_size * 2;
    public static final float lightning_bolt_duration = 1.7f; // seconds

    public static Map<CityTileTypes, TextureRegion> textures;

    final float min_glow = 0.05f;
    final float max_glow = 0.2f;

    CityTileTypes tiles[][];
    PowerTile[][] powerGrid;
    CityPowerSource[] powerSources;
    PowerConnectionBar powerBar;

    boolean[] stationConnections = new boolean[] { true, true, true, true };
    boolean[] prevConnectedState = new boolean[] { true, true, true, true };
    int numBarConnections = 0;
    int barx, bary;

    float disasterTimer = 0f;
    float disasterThreshold = 3f;
    float levelTimer = 30f;
    float scrollAccum = 0;

    float cloudTimer = 0f;
    float cloudThreshold = 5f;
    MutableFloat cloudX = new MutableFloat(-260);
    float cloudY = 0;
    boolean isClouding = false;

    MutableFloat cursorPulse = new MutableFloat(1);
    MutableFloat glowAlpha = new MutableFloat(0);
    MutableFloat cloudAlpha = new MutableFloat(0);
    MutableFloat lightningBoltTimer = new MutableFloat(0);
    boolean lightningBoltDone = true;
    int lastLightningTileX = -1;
    int lastLightningTileY = -1;
    Sound lightningSound;
    Sound powerOnSound;
    Sound powerOffSound;

    Vector3 screenPos = new Vector3();
    Vector3 worldPos = new Vector3();
    Vector2 tilePos = new Vector2();

    public CityLevel() {
    	tutorialText = "There's a power problem a brewin'.\n\nNo power means no patrons\n\nRepair the power lines to your bar with your mouse.\n\nDon't forget about your patrons.";
        CityAssets.load();

        textures = new HashMap<CityTileTypes, TextureRegion>();
        populateTextures();

        powerSources = new CityPowerSource[4];
        tiles = new CityTileTypes[tiles_high][tiles_wide];

        powerBar = new PowerConnectionBar();

        generatePowerGrid();
        generateTiles();
        enableGenerators();

        cursorPulse.setValue(0.9f);
        Tween.to(cursorPulse, 0, 1)
                .target(1.1f)
                .ease(Circ.INOUT)
                .repeatYoyo(Tween.INFINITY, 0)
                .start(LudumDare31.tweens);

        glowAlpha = new MutableFloat(min_glow);
        Tween.to(glowAlpha, 0, 1.33f)
                .target(max_glow)
                .ease(Quad.INOUT)
                .repeatYoyo(Tween.INFINITY, 0)
                .start(LudumDare31.tweens);

        cloudAlpha = new MutableFloat(0.3f);
        Tween.to(cloudAlpha, 0, 5)
                .target(0.2f)
                .ease(Quad.INOUT)
                .repeatYoyo(Tween.INFINITY, 0)
                .start(LudumDare31.tweens);

        lightningSound = SoundManager.getSound("city/lightning_strike.mp3");
        powerOnSound   = SoundManager.getSound("city/power_on.mp3");
        powerOffSound  = SoundManager.getSound("city/power_off.mp3");
    }

    @Override
    public void handleInput ( float dt){
        if (Gdx.input.justTouched()) {
            placePowerGridTile((int) tilePos.x, (int) tilePos.y);
        }
    }

    @Override
    public void update(float dt) {
    	levelTimer -= dt;
    	if (levelTimer < 0){
    		TransitionManager.Instance.startHeartAttack();
    		levelTimer = 100000;
    	}
        // Update mouse positions
        screenPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        worldPos = camera.unproject(screenPos);
        tilePos.set((int) (worldPos.x / tile_size),
                (int) ((worldPos.y - margin_bottom) / tile_size));

        // Reset energized state of all powerlines
        for (int y = 0; y < tiles_high; ++y) {
            for (int x = 0; x < tiles_wide; ++x) {
                powerGrid[y][x].energized = false;
                powerGrid[y][x].visited = false;
            }
        }
        powerGrid[bary][barx].isBar = true;

        // Figure out which powerlines are energized
        for (CityPowerSource powerSource : powerSources) {
            powerSource.energizeConnections(powerGrid);
        }

        // Figure out how many powers are connected to the bar
        updateBarPowerConnections();

        // Is it time for a power line to break somewhere?
        disasterTimer += dt;
        if (disasterTimer >= disasterThreshold) {
            disasterTimer -= disasterThreshold;
            disasterThreshold = Assets.rand.nextFloat() * 10f + 2f;
//            Gdx.app.log("THRESHOLD", "disaster threshold achieved, new threshold: " + disasterThreshold);
            disasterStrike();
        }

        scrollAccum += dt;
        if (scrollAccum > 0.1f) {
            scrollAccum -= 0.1f;
            CityAssets.clouds.scroll(dt * -0.2f, dt * 0.2f);
        }

        cloudTimer += dt;
        if (cloudTimer > cloudThreshold) {
            cloudTimer -= cloudThreshold;
            cloudThreshold = Assets.rand.nextFloat() * 5 + 3;
            if (!isClouding) {
                isClouding = true;

                cloudX.setValue(-256f);
                cloudY = Assets.rand.nextFloat() * (camera.viewportHeight - 256 - margin_bottom) + margin_bottom;
                Tween.to(cloudX, 0, 15)//Assets.rand.nextFloat() * 8 + 10)
                        .target(tiles_wide * tile_size)
                        .ease(Linear.INOUT)
                        .setCallback(new TweenCallback() {
                            @Override
                            public void onEvent(int type, BaseTween<?> source) {
                                isClouding = false;
                            }
                        })
                        .start(LudumDare31.tweens);
            }
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.setProjectionMatrix(camera.combined);

        // Draw city background
        batch.setColor(0.7f, 0.7f, 0.7f, 1.0f);
        batch.draw(CityAssets.city_background, 0, margin_bottom);

        // Draw power lines
        batch.setColor(Color.WHITE);
        for (int y = 0; y < tiles_high; ++y) {
            for (int x = 0; x < tiles_wide; ++x) {
                // Which texture to use? on or off (or empty)
                CityTileTypes type = powerGrid[y][x].powerGridType;
                TextureRegion texture = textures.get(CityTileTypes.empty);
                if (type != CityTileTypes.empty) {
                    PowerTexture powerTexture = CityAssets.powerTextures.get(powerGrid[y][x].powerGridType);
                    texture = (powerGrid[y][x].energized ? powerTexture.on : powerTexture.off);
                }

                // Draw power line texture
                batch.draw(texture, x * tile_size, y * tile_size + margin_bottom, tile_size, tile_size);

                // Draw 'glow' overlay
                if (powerGrid[y][x].energized) {
                    batch.setColor(1, 1, 0, glowAlpha.floatValue());
                    batch.draw(Assets.squareTex,
                            x * tile_size, y * tile_size + margin_bottom, tile_size, tile_size);
                    batch.setColor(Color.WHITE);
                }

                // Draw power source overlay
                for (CityPowerSource powerSource : powerSources) {
                    if (x == powerSource.x && y == powerSource.y) {
                        batch.setColor(1, 1, 0, glowAlpha.floatValue() * 3f);
                        batch.draw(Assets.squareTex,
                                x * tile_size, y * tile_size + margin_bottom, tile_size, tile_size);
                        batch.setColor(Color.WHITE);
                        break;
                    }
                }

                // Draw bar sink overlay
                if (x == barx && y == bary) {
                    batch.setColor(1, 0, 1, glowAlpha.floatValue() * 3.5f);
                    batch.draw(Assets.squareTex,
                            x * tile_size, y * tile_size + margin_bottom, tile_size, tile_size);
                    batch.setColor(Color.WHITE);
                }
            }
        }

        // Draw lightning
        if (!lightningBoltDone) {
//            Gdx.app.log("BOOM", "lighting strike");
            // TODO (brian): make noise
            batch.draw(CityAssets.lightningBoltAnim.getKeyFrame(lightningBoltTimer.floatValue()),
                    lastLightningTileX * tile_size - 16, lastLightningTileY * tile_size + margin_bottom);
            batch.draw(CityAssets.lightningFlashAnim.getKeyFrame(lightningBoltTimer.floatValue()),
                    0, margin_bottom, camera.viewportWidth, camera.viewportHeight);
        }

        // Draw cloud layer
        batch.setColor(1.0f, 1.0f, 1.0f, 0.9f);//cloudAlpha.floatValue());
        batch.draw(CityAssets.cloud1, cloudX.floatValue(), cloudY);
//        batch.draw(CityAssets.clouds, 0, margin_bottom, camera.viewportWidth, camera.viewportHeight - margin_bottom);

        // HUD type stuff ---------------------
        batch.setColor(Color.WHITE);
        powerBar.draw(batch, cursorPulse.floatValue());

        // Draw an underneath layer for the currently active power grid type
        final float pulse_offset = 1.6f;
        batch.setColor(Color.DARK_GRAY);
        batch.draw(Assets.squareTex,
                tilePos.x * tile_size - pulse_offset, tilePos.y * tile_size + margin_bottom - pulse_offset,
                tile_size * cursorPulse.floatValue(), tile_size * cursorPulse.floatValue());

        // Draw the currently active power grid type
        batch.setColor(Color.WHITE);
        batch.draw(textures.get(powerBar.currentPowerLineType),
                tilePos.x * tile_size - pulse_offset, tilePos.y * tile_size + margin_bottom - pulse_offset,
                tile_size * cursorPulse.floatValue(), tile_size * cursorPulse.floatValue());

        // Draw the number of connections to the bar
        batch.draw((stationConnections[0] ? CityAssets.fuse_on : CityAssets.fuse_off), 32 * 0 + 2, 0);
        batch.draw((stationConnections[1] ? CityAssets.fuse_on : CityAssets.fuse_off), 32 * 1 + 4, 0);
        batch.draw((stationConnections[2] ? CityAssets.fuse_on : CityAssets.fuse_off), 32 * 2 + 6, 0);
        batch.draw((stationConnections[3] ? CityAssets.fuse_on : CityAssets.fuse_off), 32 * 3 + 8, 0);
    }

    @Override
    public int hasThreat() {
        if      (numBarConnections == 4)                           return 0;
        else if (numBarConnections == 3 || numBarConnections == 2) return 1;
        else if (numBarConnections == 1)                           return 2;
        else                                                       return 3;
    }

    // ------------------------------------------------------------------------
    private void placePowerGridTile(int x, int y) {
        placePowerGridTile(x, y, powerBar.currentPowerLineType);
    }

    private void placePowerGridTile(int x, int y, CityTileTypes powerLineType) {
        if (x < 0 || y < 0 || x >= tiles_wide || y >= tiles_high) {
//            Gdx.app.log("GAH!", "attempted to place power grid tile out of bounds: " + x + ", " + y);
            return;
        }

        // Update score
        if (powerLineType != CityTileTypes.empty) {
            Score.PowerTilesPlaced++;
        }

        // Place the current power tile grid at the tile under the mouse
        PowerTile thisTile = powerGrid[y][x];
        thisTile.powerGridType = powerLineType;

        // Update the power bar
        if (powerLineType != CityTileTypes.empty) {
            powerBar.dequeueCurrentType();
        }

        // Connect up neighbors tiles that have a power connection
        PowerTile thatTile;
        if (y + 1 <  tiles_high) {
            thatTile = powerGrid[y + 1][x];
            if (CityTileTypes.connectsUp(thisTile.powerGridType) && CityTileTypes.connectsDown(thatTile.powerGridType)) {
                thisTile.up = thatTile;
                thatTile.down = thisTile;
            } else {
                thisTile.up = null;
                thatTile.down = null;
            }
        }
        if (y - 1 >= 0) {
            thatTile = powerGrid[y - 1][x];
            if (CityTileTypes.connectsDown(thisTile.powerGridType) && CityTileTypes.connectsUp(thatTile.powerGridType)) {
                thisTile.down = thatTile;
                thatTile.up = thisTile;
            } else {
                thisTile.down = null;
                thatTile.up = null;
            }
        }
        if (x - 1 >= 0) {
            thatTile = powerGrid[y][x - 1];
            if (CityTileTypes.connectsLeft(thisTile.powerGridType) && CityTileTypes.connectsRight(thatTile.powerGridType)) {
                thisTile.left = thatTile;
                thatTile.right = thisTile;
            } else {
                thisTile.left = null;
                thatTile.right = null;
            }
        }
        if (x + 1 <  tiles_wide) {
            thatTile = powerGrid[y][x + 1];
            if (CityTileTypes.connectsRight(thisTile.powerGridType) && CityTileTypes.connectsLeft(thatTile.powerGridType)) {
                thisTile.right = thatTile;
                thatTile.left = thisTile;
            } else {
                thisTile.right = null;
                thatTile.left = null;
            }
        }
    }

    private void disasterStrike() {
        int iterations = 0;
        while (++iterations <= 20) {
            int x = Assets.rand.nextInt(tiles_wide);
            int y = Assets.rand.nextInt(tiles_high);

            // Make sure random x and y isn't a power source coordinate
            boolean isPowerSource = false;
            for (CityPowerSource powerSource : powerSources) {
                if (powerSource.x == x && powerSource.y == y) {
                    isPowerSource = true;
                }
            }
            if (isPowerSource) continue;

            PowerTile tile = powerGrid[y][x];
            if (!tile.isBar && tile.powerGridType != CityTileTypes.empty)  {
//                Gdx.app.log("DISASTER", "disaster struck at " + x + ", " + y);
                lastLightningTileX = x;
                lastLightningTileY = y;
                lightningBoltDone = false;
                lightningBoltTimer.setValue(0);
                Tween.to(lightningBoltTimer, 0, lightning_bolt_duration)
                        .target(lightning_bolt_duration)
                        .setCallback(new TweenCallback() {
                            @Override
                            public void onEvent(int type, BaseTween<?> source) {
                                lightningBoltDone = true;
                                placePowerGridTile(lastLightningTileX, lastLightningTileY, CityTileTypes.empty);
                            }
                        })
                        .start(LudumDare31.tweens);

                SoundManager.play(LevelManager.Levels.City, lightningSound, 1);

                LevelManager.powerLevel = numBarConnections;
                break;
            }
        }
//        Gdx.app.log("DISASTER", "got off safe this time...");
    }

    LinkedList<PowerTile> connectionQueue = new LinkedList<PowerTile>();
    private void updateBarPowerConnections() {
        numBarConnections = 0;

        // Reset energized state of all powerlines
        for (int y = 0; y < tiles_high; ++y) {
            for (int x = 0; x < tiles_wide; ++x) {
                powerGrid[y][x].visited = false;
            }
        }

        // Arbitrarily set the barTile to be one of the tiles from the new map
        // TODO (brian): handle the 4 tile version?
        barx = 15;
        bary = 10;
        PowerTile barTile = powerGrid[bary][barx];
        if (CityTileTypes.connectsDown (powerGrid[bary - 1][barx    ].powerGridType))
             barTile.up = powerGrid[bary - 1][barx    ];
        else barTile.up = null;
        if (CityTileTypes.connectsUp   (powerGrid[bary + 1][barx    ].powerGridType))
             barTile.down = powerGrid[bary + 1][barx    ];
        else barTile.down = null;
        if (CityTileTypes.connectsLeft (powerGrid[bary    ][barx - 1].powerGridType))
             barTile.right = powerGrid[bary    ][barx - 1];
        else barTile.right = null;
        if (CityTileTypes.connectsRight(powerGrid[bary    ][barx + 1].powerGridType))
            barTile.left  = powerGrid[bary    ][barx + 1];
        else barTile.left = null;


        System.arraycopy(stationConnections, 0, prevConnectedState, 0, stationConnections.length);
        stationConnections[0] = false;
        stationConnections[1] = false;
        stationConnections[2] = false;
        stationConnections[3] = false;

        connectionQueue.clear();
        connectionQueue.add(barTile);
        barTile.visited = true;
        while (!connectionQueue.isEmpty()) {
            // remove the front node
            PowerTile powerTile = connectionQueue.remove();

            // if we've hit a power station tile, set it as connected
            for (int i = 0; i < powerSources.length; ++i) {
                if (powerTile.x == powerSources[i].x && powerTile.y == powerSources[i].y) {
                    stationConnections[i] = true;
                }
            }

            // add neighbors if they exist, are energized, and haven't been visited already
            if (powerTile.up != null && powerTile.up.energized && !powerTile.up.visited) {
                powerTile.up.visited = true;
                connectionQueue.add(powerTile.up);
            }
            if (powerTile.down != null && powerTile.down.energized && !powerTile.down.visited) {
                powerTile.down.visited = true;
                connectionQueue.add(powerTile.down);
            }
            if (powerTile.left != null && powerTile.left.energized && !powerTile.left.visited) {
                powerTile.left.visited = true;
                connectionQueue.add(powerTile.left);
            }
            if (powerTile.right != null && powerTile.right.energized && !powerTile.right.visited) {
                powerTile.right.visited = true;
                connectionQueue.add(powerTile.right);
            }
        }

        // Play a power on/off sound if the connection state changed
        for (int i = 0; i < prevConnectedState.length; ++i) {
            if (prevConnectedState[i] != stationConnections[i]) {
                if (stationConnections[i]) {
                    SoundManager.play(LevelManager.Levels.City, powerOnSound, 0.5f);
                } else {
                    SoundManager.play(LevelManager.Levels.City, powerOffSound, 0.5f);
                }
            }
        }

        // how many connections did we find?
        for (boolean isConnected : stationConnections) {
            if (isConnected) ++numBarConnections;
        }
    }

    private void enableGenerators() {
        for (CityPowerSource powerSource : powerSources) {
            int x = powerSource.x;
            int y = powerSource.y;

            PowerTile generator = powerGrid[y][x];
            generator.energized = true;
            generator.powerGridType = CityTileTypes.power_line_x;
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

        textures.put(CityTileTypes.power_line_h,         CityAssets.power_line_h_off);
        textures.put(CityTileTypes.power_line_v,         CityAssets.power_line_v_off);
        textures.put(CityTileTypes.power_line_x,         CityAssets.power_line_x_off);

        textures.put(CityTileTypes.power_line_corner_lt, CityAssets.power_line_corner_lt_off);
        textures.put(CityTileTypes.power_line_corner_rt, CityAssets.power_line_corner_rt_off);
        textures.put(CityTileTypes.power_line_corner_lb, CityAssets.power_line_corner_lb_off);
        textures.put(CityTileTypes.power_line_corner_rb, CityAssets.power_line_corner_rb_off);

        textures.put(CityTileTypes.power_line_up_t,      CityAssets.power_line_up_t_off);
        textures.put(CityTileTypes.power_line_down_t,    CityAssets.power_line_down_t_off);
        textures.put(CityTileTypes.power_line_left_t,    CityAssets.power_line_left_t_off);
        textures.put(CityTileTypes.power_line_right_t,   CityAssets.power_line_right_t_off);

        textures.put(CityTileTypes.empty,   CityAssets.empty);
    }

    private void generatePowerGrid() {
        int[][] layout = new int[][] {
                { 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20 },
                { 20, 15, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 19, 15, 20 },
                { 20, 17, 13, 18, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 14, 20, 20 },
                { 20, 20, 20, 14, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 19, 13, 13, 13, 13, 13, 13, 13, 13, 13, 16, 20, 20 },
                { 20, 20, 20, 17, 13, 13, 13, 18, 20, 20, 20, 20, 20, 20, 20, 20, 20, 14, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20 },
                { 20, 20, 20, 20, 20, 20, 20, 14, 20, 20, 20, 20, 20, 20, 20, 20, 20, 14, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20 },
                { 20, 20, 20, 20, 20, 20, 20, 14, 20, 20, 20, 20, 20, 20, 19, 13, 13, 16, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20 },
                { 20, 20, 20, 20, 20, 20, 20, 14, 20, 20, 20, 20, 20, 20, 14, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20 },
                { 20, 20, 20, 20, 20, 20, 20, 17, 13, 13, 13, 13, 18, 20, 17, 18, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20 },
                { 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 14, 20, 20, 14, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20 },
                { 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 17, 18, 20, 14, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20 },
                { 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 17, 13, 20, 13, 18, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20 },
                { 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 14, 20, 14, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20 },
                { 20, 20, 20, 20, 20, 19, 13, 13, 13, 13, 13, 18, 20, 20, 20, 14, 20, 14, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20 },
                { 20, 20, 20, 20, 20, 14, 20, 20, 20, 20, 20, 17, 13, 13, 13, 16, 20, 14, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20 },
                { 20, 20, 20, 20, 20, 14, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 14, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20 },
                { 20, 20, 19, 13, 13, 16, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 17, 13, 13, 13, 13, 18, 20, 20, 20, 20, 20, 20, 20 },
                { 20, 20, 14, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 14, 20, 20, 20, 20, 20, 20, 20 },
                { 20, 20, 14, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 17, 13, 13, 13, 18, 20, 20, 20 },
                { 20, 19, 16, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 17, 18, 20, 20 },
                { 20, 15, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 17, 15, 20 },
                { 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20 },
        };

        // Initialize the power grid tiles
        powerGrid = new PowerTile[tiles_high][tiles_wide];
        for (int y = 0; y < tiles_high; ++y) {
            for (int x = 0; x < tiles_wide; ++x) {
                powerGrid[y][x] = new PowerTile(x, y);
            }
        }
        // Place the initial power line connections
        for (int y = 0; y < tiles_high; ++y) {
            for (int x = 0; x < tiles_wide; ++x) {
                placePowerGridTile(x, y, CityTileTypes.getTile(layout[tiles_high - y - 1][x]));
            }
        }
    }

    private void generateTiles() {
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
//                else if (tiles[y][x] == CityTileTypes.bar) {
//                    barx = x;
//                    bary = y;
//                    powerGrid[y][x].isBar = true;
//                }
            }
        }
    }


}
