package gremlins;

import java.util.ArrayList;

public class Fireball extends MovingObject implements Projectile {

    protected ArrayList<Gremlin> gremlins;
    protected ArrayList<Slime> slimes;
    protected String direction;
    protected boolean collided;

    public Fireball(int x, int y, ArrayList<StationaryObject> stonewalls, 
                    ArrayList<Brickwall> brickwalls, ArrayList<Gremlin> gremlins, 
                    ArrayList<Slime> slimes, String direction) {
        super(x, y, stonewalls, brickwalls, 4);
        this.gremlins = gremlins;
        this.slimes = slimes;
        this.direction = direction;
        this.collided = false;
    }

    /**
     * Gives the fireball's current direction.
    */
    public String getDirection() {
        return this.direction;
    }

    /**
     * Tells whether or not the fireball has collided with something.
    */
    public boolean getCollided() {
        return this.collided;
    }

    /**
     * Moves the fireball and checks for wall, gremlin, and slime collisions.
    */
    public void tick() {
        this.move(this.direction);

        if (this.obstacleCollision(direction, this.brickwalls)) {
            this.brickwalls.get(this.collidedObstacleIndex).destroyBrickwall();
        }

        if (this.wallCollision(direction)) {
            this.collided = true;
        }

        if (this.obstacleCollision("touching", this.gremlins)) {
            this.gremlins.get(this.collidedObstacleIndex).kill();
            this.collided = true;
        }

        if (this.obstacleCollision("touching", this.slimes)) {
            this.slimes.get(this.collidedObstacleIndex).breakSlime();
            this.collided = true;
        }
    }
}