package levels.citylevel;

/**
 * Brian Ploeckelman created on 12/6/2014.
 */
public class PowerTile {

    public boolean visited;
    public boolean energized;
    public boolean isBar;

    public PowerTile up;
    public PowerTile down;
    public PowerTile left;
    public PowerTile right;

    public int x, y;

    public CityTileTypes powerGridType;

    public PowerTile(int x, int y) {
        this.x = x;
        this.y = y;
        visited = false;
        energized = false;
        up    = null;
        down  = null;
        left  = null;
        right = null;
        powerGridType = CityTileTypes.empty;
    }

}
