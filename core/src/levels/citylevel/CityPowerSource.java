package levels.citylevel;

import java.util.Stack;

/**
 * Brian Ploeckelman created on 12/6/2014.
 */
public class CityPowerSource {

    public int x, y;

    private Stack<PowerTile> powerTileStack;

    public CityPowerSource(int x, int y) {
        this.x = x;
        this.y = y;
        this.powerTileStack = new Stack<PowerTile>();
    }

    public void energizeConnections(PowerTile[][] powerTiles) {
        powerTileStack.clear();
        powerTileStack.push(powerTiles[y][x]);
        while (!powerTileStack.isEmpty()) {
            PowerTile top = powerTileStack.pop();
            if (!top.visited) {
                // Have we reached the bar node?
                if (top.isBar) break;

                top.visited = true;
                top.energized = true;

                // Add connected neighbors to stack
                if (top.up    != null) powerTileStack.push(powerTiles[top.up.y   ][top.up.x   ]);
                if (top.down  != null) powerTileStack.push(powerTiles[top.down.y ][top.down.x ]);
                if (top.left  != null) powerTileStack.push(powerTiles[top.left.y ][top.left.x ]);
                if (top.right != null) powerTileStack.push(powerTiles[top.right.y][top.right.x]);
            }
        }
    }


}
