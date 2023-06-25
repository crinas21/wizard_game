package gremlins;

import java.util.ArrayList;

public class Slime extends MovingObject implements Projectile {

    protected String direction;
    protected boolean collided;

    public Slime(int x, int y, ArrayList<StationaryObject> stonewalls, 
                ArrayList<Brickwall> brickwalls, String direction) {
        super(x, y, stonewalls, brickwalls, 4);
        this.direction = direction;
        this.collided = false;
    }

    /**
     * Gives the current direction of the slime.
    */
    public String getDirection() {
        return this.direction;
    }

    /**
     * Checks whether the slime has collided.
    */
    public boolean getCollided() {
        return this.collided;
    }

    /**
     * Sets the slime to have collided.
    */
    public void breakSlime() {
        this.collided = true;
    }

    /**
     * Moves the slime and checks for wall collisions.
    */
    public void tick() {
        this.move(this.direction);

        if(this.wallCollision(direction)) {
            this.collided = true;
        }
    }
}