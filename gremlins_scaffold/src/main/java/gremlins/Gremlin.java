package gremlins;

import java.util.*;

public class Gremlin extends MovingObject {

    private static final String[] directions = {"left", "up", "right", "down"};
    protected Wizard wizard;
    protected ArrayList<StationaryObject> emptyspaces;
    protected static int numGremlins;
    protected static boolean frozen;
    protected static int frozenTimer;
    protected int directionIndex;
    protected String direction;

    public Gremlin(int x, int y, ArrayList<StationaryObject> stonewalls,
                    ArrayList<Brickwall> brickwalls, Wizard wizard,
                    ArrayList<StationaryObject> emptyspaces) {
        super(x, y, stonewalls, brickwalls, 1);
        this.wizard = wizard;
        this.emptyspaces = emptyspaces;
        this.directionIndex = App.randomGenerator.nextInt(directions.length);
        frozen = false;
        frozenTimer = 0;
    }

    /**
     * Checks whether Gremlins are frozen.
    */
    public static boolean getFrozen() {
        return frozen;
    }

    /**
     * Gives the frozen timer for Gremlins.
    */
    public static int getFrozenTimer() {
        return frozenTimer;
    }

    /**
     * Sets the Gremlins to be frozen.
    */
    public static void freeze() {
        frozen = true;
    }

    /**
     * Sets the Gremlins to be unfrozen.
    */
    public static void resetFreeze() {
        frozen = false;
        frozenTimer = 0;
    }

    /**
     * Passes in the number of gremlins.
     * @param   gremlins    Number of gremlins for the level.
    */
    public static void giveNumGremlins(int gremlins) {
        numGremlins = gremlins;
    }

    /**
     * Gives the evaluated direction index for a gremlin between 0 and 3, looping back around if the index passed is greater than 3.
     * @param   index   The gremlin's yet-to-be evaluated direction index from tick().
    */
    protected int getDirectionIndex(int index) {
        if (index > 3) {
            return index - 4;
        } else {
            return index;
        }
    }

    /**
     * Gives the gremlin's current direction.
    */
    public String getDirection() {
        return this.direction;
    }

    /**
     * Passes in the wizard.
     * @param   wiz The wizard used in the game(player) which is used to know where the gremlins should respawn when killed.
    */
    public void giveWizard(Wizard wiz) {
        this.wizard = wiz;
    }

    /**
     * Runs when a fireball touches a gremlin. Makes the gremlin respawn on an empty tile that is at least 10 tiles away from the wizard.
    */
    public void kill() {
        // Find all empty spaces that are at least 10 spaces away from the player and store in validSpaces
        ArrayList<StationaryObject> validSpaces = new ArrayList<StationaryObject>();
        for (StationaryObject emptyspace : this.emptyspaces) {
            if ((emptyspace.getX() < this.wizard.getX() - 10*App.SPRITESIZE || 
                emptyspace.getX() > this.wizard.getX() + 10*App.SPRITESIZE) && 
                (emptyspace.getY() < this.wizard.getY() - 10*App.SPRITESIZE || 
                emptyspace.getY() > this.wizard.getY() +10*App.SPRITESIZE)) {
                validSpaces.add(emptyspace);
            }
        }

        int index = App.randomGenerator.nextInt(validSpaces.size());
        StationaryObject newSpace = validSpaces.get(index);
        this.x = newSpace.getX();
        this.y = newSpace.getY();
    }

    /**
     * Moves the gremlin, checks for wall collisions and decides which new direction to go.
     * Checks for frozen.
    */
    public void tick() {
        this.direction = directions[this.directionIndex];

        if (!this.wallCollision(this.direction) && !frozen) {
            this.move(this.direction);
        } else {
            int adjacentDirectionIndex1 = getDirectionIndex(this.directionIndex+1);
            String adjacentDirection1 = directions[adjacentDirectionIndex1];
            int adjacentDirectionIndex2 = getDirectionIndex(this.directionIndex+3);
            String adjacentDirection2 = directions[adjacentDirectionIndex2];

             // If gremlin meets a dead end, new direction is back the way it came
            if (this.wallCollision(adjacentDirection1) && this.wallCollision(adjacentDirection2)) {
                this.directionIndex = getDirectionIndex(this.directionIndex+2);
            }
            // If gremlin meets a wall with only one direction to go, go that way
            else if (!this.wallCollision(adjacentDirection1) && 
                    this.wallCollision(adjacentDirection2)) {
                this.directionIndex = adjacentDirectionIndex1;
            }
            // If gremlin meets a wall with only one direction to go, go that way
            else if (this.wallCollision(adjacentDirection1) && 
                    !this.wallCollision(adjacentDirection2)) {
                this.directionIndex = adjacentDirectionIndex2;
            }
            // If gremlin meets a T-intersection, choose a random direction
            else if (!this.wallCollision(adjacentDirection1) && 
                    !this.wallCollision(adjacentDirection2)) {
                int[] tempIndexes = {adjacentDirectionIndex1, adjacentDirectionIndex2};
                this.directionIndex = tempIndexes[App.randomGenerator.nextInt(tempIndexes.length)];
            }
        }

        if (frozen) {
            if (frozenTimer >= 5 * numGremlins * App.FPS) {
                resetFreeze();
            } else {
                frozenTimer++;
            }
        }
    }
}