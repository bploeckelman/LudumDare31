package levels.intercell;

/**
 * Created by vandillen on 12/6/14.
 */
public class BloodCell {

    // Constants
    final int OUT_OF_SHOOTER_VELOCITY = 10;

    // Ivars
    private boolean alive;
    private int row;
    private int column;
    private int timer;
    private int color;
    private int velocity;
    private boolean checked;

    // Constructor
    public BloodCell(int x, int y, int color) {

    }

    public void fire(int x,int y)
    {
        //velocity = VELOCITY ;
    }

    // return true if collision with other cells or bulge
    private boolean collide () {
        return true;
    }
}
