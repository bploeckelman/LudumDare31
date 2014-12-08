package levels.planetary;

import lando.systems.ld31.GameObject;

public abstract class DestroyableObject extends GameObject {

    protected boolean destroyable = false;

    public boolean isDestroyable() {
        return destroyable;
    }

    protected void setDestroyable(boolean isDestroyable) {
        destroyable = isDestroyable;
    }

}
