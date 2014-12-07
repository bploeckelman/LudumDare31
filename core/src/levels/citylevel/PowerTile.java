package levels.citylevel;

/**
 * Brian Ploeckelman created on 12/6/2014.
 */
public class PowerTile {

    public boolean energized;
    public PowerTile up;
    public PowerTile down;
    public PowerTile left;
    public PowerTile right;

    public CityTileTypes powerGridType;

    public PowerTile() {
        energized = false;
        up    = null;
        down  = null;
        left  = null;
        right = null;
        powerGridType = CityTileTypes.empty;
    }

}
