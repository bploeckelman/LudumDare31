package levels.citylevel;

/**
 * Brian Ploeckelman created on 12/6/2014.
 */
public enum CityTileTypes {

    bar(0),

    house1(1),
    house2(2),
    house3(3),

    road_h(4),
    road_v(5),
    road_x(6),
    road_corner_lt(7),
    road_corner_rt(8),
    road_corner_lb(9),
    road_corner_rb(10),

    power_station(11),
    power_pole(12),
    power_line_h(13),
    power_line_v(14),
    power_line_x(15),
    power_line_corner_lt(16),
    power_line_corner_rt(17),
    power_line_corner_lb(18),
    power_line_corner_rb(19),

    power_line_up_t(22),
    power_line_down_t(23),
    power_line_left_t(24),
    power_line_right_t(25),

    empty(20),
    unknown(21);

    private int value;

    private CityTileTypes(int value) { this.value = value; }

    public int i() { return value; }

    public static CityTileTypes getTile(int i) {
        for (CityTileTypes tile : CityTileTypes.values()) {
            if (tile.i() == i) return tile;
        }
        return unknown;
    }

}
