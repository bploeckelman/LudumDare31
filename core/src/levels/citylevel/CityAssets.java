package levels.citylevel;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import javax.xml.soap.Text;

/**
 * Brian Ploeckelman created on 12/6/2014.
 */
public class CityAssets {

    public static Texture city_background;

    public static Texture spritesheet;

    public static TextureRegion bar;
    public static TextureRegion house1;
    public static TextureRegion house2;
    public static TextureRegion house3;
    public static TextureRegion road_h;
    public static TextureRegion road_v;
    public static TextureRegion road_x;
    public static TextureRegion road_corner_lt;
    public static TextureRegion road_corner_rt;
    public static TextureRegion road_corner_lb;
    public static TextureRegion road_corner_rb;

    public static TextureRegion power_station;
    public static TextureRegion power_pole;

    public static TextureRegion power_line_h;
    public static TextureRegion power_line_v;
    public static TextureRegion power_line_x;
    public static TextureRegion power_line_corner_lt;
    public static TextureRegion power_line_corner_rt;
    public static TextureRegion power_line_corner_lb;
    public static TextureRegion power_line_corner_rb;
    public static TextureRegion power_line_up_t;
    public static TextureRegion power_line_down_t;
    public static TextureRegion power_line_left_t;
    public static TextureRegion power_line_right_t;

    public static TextureRegion connections0;
    public static TextureRegion connections1;
    public static TextureRegion connections2;
    public static TextureRegion connections3;
    public static TextureRegion connections4;

    public static TextureRegion empty;

    public static Texture clouds_texture;
    public static TextureRegion clouds;

    public static TextureRegion fuse_on;
    public static TextureRegion fuse_off;

    public static void load() {
        city_background = new Texture("city/city_map.png");
        spritesheet = new Texture("city/city_tiles.png");

        TextureRegion[][] regions = TextureRegion.split(spritesheet, 32, 32);
        bar                  = regions[0][0];

        house1               = regions[0][1];
        house2               = regions[0][2];
        house3               = regions[0][3];

        road_h               = regions[0][4];
        road_v               = regions[0][5];
        road_x               = regions[0][6];
        road_corner_lt       = regions[0][7];
        road_corner_rt       = regions[1][0];
        road_corner_lb       = regions[1][1];
        road_corner_rb       = regions[1][2];

        power_station        = regions[1][3];
        power_pole           = regions[1][4];
        power_line_h         = regions[1][5];
        power_line_v         = regions[1][6];
        power_line_x         = regions[1][7];

        power_line_corner_lt = regions[2][0];
        power_line_corner_rt = regions[2][1];
        power_line_corner_lb = regions[2][2];
        power_line_corner_rb = regions[2][3];

        power_line_up_t      = regions[2][4];
        power_line_down_t    = regions[2][5];
        power_line_left_t    = regions[2][6];
        power_line_right_t   = regions[2][7];

        connections0 = regions[7][0];
        connections1 = regions[7][1];
        connections2 = regions[7][2];
        connections3 = regions[7][3];
        connections4 = regions[7][4];

        empty = regions[7][7];

        TextureRegion[][] fuseRegions = TextureRegion.split(spritesheet, 32, 64);
        fuse_off = fuseRegions[2][0];
        fuse_on = fuseRegions[2][1];

        clouds_texture = new Texture("city/clouds.png");
        clouds_texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        clouds = new TextureRegion(clouds_texture);
    }

}
