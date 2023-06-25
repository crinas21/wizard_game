package gremlins;

import java.util.ArrayList;

public class Wizard extends MovingObject {

    protected ArrayList<Gremlin> gremlins;
    protected ArrayList<Slime> slimes;
    protected ArrayList<StationaryObject> doors;
    protected ArrayList<StationaryObject> portals;
    protected Snowflake snowflake;

    protected boolean isAlive;
    protected boolean goNextLevel;
    protected String facingDirection;
    protected boolean fired;
    protected boolean readyForPortal;

    protected boolean moveLeft;
    protected boolean moveUp;
    protected boolean moveRight;
    protected boolean moveDown;

    // These indicate the direction needed to reach the next whole tile
    protected boolean horizontalLeft;
    protected boolean horizontalRight;
    protected boolean verticalUp;
    protected boolean verticalDown;

    public Wizard(int x, int y, ArrayList<StationaryObject> stonewalls, 
                ArrayList<Brickwall> brickwalls, ArrayList<Gremlin> gremlins, 
                ArrayList<Slime> slimes, ArrayList<StationaryObject> doors, 
                ArrayList<StationaryObject> portals, Snowflake snowflake) {
        super(x, y, stonewalls, brickwalls, 2);
        this.gremlins = gremlins;
        this.slimes = slimes;
        this.doors = doors;
        this.portals = portals;
        this.snowflake = snowflake;

        this.isAlive = true;
        this.goNextLevel = false;
        this.facingDirection = "right";
        this.fired = false;
        this.readyForPortal = true;
    }

    /**
     * Checks whether the wizard has lost a life by touching a slime or gremlin.
    */
    public boolean getIsAlive() {
        return this.isAlive;
    }

    /**
     * Checks whether the wizard has reached a door.
    */
    public boolean getGoNextLevel() {
        return this.goNextLevel;
    }

    /**
     * Gives the direction the wizard is facing.
    */
    public String getDirection() {
        return this.facingDirection;
    }

    /**
     * Checks whether the wizard has just fired a fireball and has to wait to cooldown.
    */
    public boolean getFired() {
        return this.fired;
    }

    /**
     * Sets the wizard as having fired a fireball and in need to cooldown.
    */
    public void setFired(boolean isFired) {
        this.fired = isFired;
    }

    /**
     * Makes the wizard move left
    */
    public void pressLeft() {
        if (moveUp) {
            this.releaseUp();
        } else if (moveDown) {
            this.releaseDown();
        }
        this.moveLeft = true;
        this.moveUp = false;
        this.moveRight = false;
        this.moveDown = false;
        this.horizontalRight = false;
        this.facingDirection = "left";
    }

    /**
     * Makes the wizard stop moving left
    */
    public void releaseLeft() {
        this.moveLeft = false;
        this.horizontalLeft = true;
    }

    /**
     * Makes the wizard move up
    */
    public void pressUp() {
        if (moveLeft) {
            this.releaseLeft();
        } else if (moveRight) {
            this.releaseRight();
        }
        this.moveLeft = false;
        this.moveUp = true;
        this.moveRight = false;
        this.moveDown = false;
        this.verticalDown = false;
        this.facingDirection = "up";
    }

    /**
     * Makes the wizard stop moving up
    */
    public void releaseUp() {
        this.moveUp = false;
        this.verticalUp = true;
    }

    /**
     * Makes the wizard move right
    */
    public void pressRight() {
        if (moveUp) {
            this.releaseUp();
        } else if (moveDown) {
            this.releaseDown();
        }
        this.moveLeft = false;
        this.moveUp = false;
        this.moveRight = true;
        this.moveDown = false;
        this.horizontalLeft = false;
        this.facingDirection = "right";
    }

    /**
     * Makes the wizard stop moving right
    */
    public void releaseRight() {
        this.moveRight = false;
        this.horizontalRight = true;
    }

    /**
     * Makes the wizard move down
    */
    public void pressDown() {
        if (moveLeft) {
            this.releaseLeft();
        } else if (moveRight) {
            this.releaseRight();
        }
        this.moveLeft = false;
        this.moveUp = false;
        this.moveRight = false;
        this.moveDown = true;
        this.verticalUp = false;
        this.facingDirection = "down";
    }

    /**
     * Makes the wizard stop moving down
    */
    public void releaseDown() {
        this.moveDown = false;
        this.verticalDown = true;
    }

    /**
     * Checks for a snowflake collision.
    */
    public boolean snowflakeCollision() {
        int[] snowflakeSides = {this.snowflake.getX(), this.snowflake.getY(), 
                                this.snowflake.getX()+App.SPRITESIZE, 
                                this.snowflake.getY()+App.SPRITESIZE};
        if (obstacleCollisionIndividual(snowflakeSides, "touching")) {
            return true;
        }
        return false;
    }

    /**
     * Moves the wizard, ensures that when buttons are released, it is moved to a whole tile, and checks for gremlin, slime, door, portal, and snowflake collisions.
    */
    public void tick() {
        if (!this.wallCollision(this.facingDirection)) {
            if (moveLeft) {
                this.x -= this.speed;
            }
            if (moveRight) {
                this.x += this.speed;
            }
            if (moveUp) {
                this.y -= this.speed;
            }
            if (moveDown) {
                this.y += this.speed;
            }
        }

        // When horizontal buttons are released, ensure wizard is moved to the next whole tile
        if (!moveLeft && !moveRight) {
            if (horizontalLeft) {
                double desiredX = App.SPRITESIZE*Math.floor((double) this.x/App.SPRITESIZE);
                if (this.x != desiredX) {
                    this.x -= this.speed;
                } else {
                    horizontalLeft = false;
                }
            } else if (horizontalRight) {
                double desiredX = App.SPRITESIZE*Math.ceil((double) this.x/App.SPRITESIZE);
                if (this.x != desiredX) {
                    this.x += this.speed;
                } else {
                    horizontalRight = false;
                }
            }
        }

        // When vertical buttons are released, ensure wizard is moved to the next whole tile
        if (!moveUp && !moveDown) {
            if (verticalUp) {
                double desiredY = App.SPRITESIZE*Math.floor((double) this.y/App.SPRITESIZE);
                if (this.y != desiredY) {
                    this.y -= this.speed;
                } else {
                    verticalUp = false;
                }
            } else if (verticalDown) {
                double desiredY = App.SPRITESIZE*Math.ceil((double) this.y/App.SPRITESIZE);
                if (this.y != desiredY) {
                    this.y += this.speed;
                } else {
                    verticalDown = false;
                }
            }
        }

        if (this.obstacleCollision("touching", this.gremlins) || 
            this.obstacleCollision("touching", this.slimes)) {
            this.isAlive = false;
            Gremlin.resetFreeze();
            this.snowflake.setCollected(this);
        }

        if (this.obstacleCollision("touching", this.doors)) {
            this.goNextLevel = true;
            Gremlin.resetFreeze();
            this.snowflake.setCollected(this);
        }

        if (this.portals.size() >= 2 && this.obstacleCollision("touching", this.portals) 
            && this.readyForPortal) {
            StationaryObject collidedPortal = this.portals.get(this.collidedObstacleIndex);
            this.portals.remove(collidedPortal);
            int newPortalIndex = App.randomGenerator.nextInt(this.portals.size());
            StationaryObject newPortal = this.portals.get(newPortalIndex);
            this.portals.add(collidedPortal);

            this.readyForPortal = false;
            this.x = newPortal.getX();
            this.y = newPortal.getY();
        }

        if (!this.obstacleCollision("touching", this.portals)) {
            this.readyForPortal = true;
        }

        if (this.snowflakeCollision()) {
            this.snowflake.setCollected(this);
            Gremlin.freeze();
        }
    }
}