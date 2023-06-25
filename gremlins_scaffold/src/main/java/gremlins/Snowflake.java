package gremlins;

import java.util.ArrayList;

public class Snowflake extends GameObject {
    protected int startingX;
    protected int startingY;
    protected Wizard wizard;
    protected ArrayList<StationaryObject> emptyspaces;
    protected boolean collected;
    protected int startingTimer;
    protected int timer;
    protected int secondsBeforeSpawn;

    public Snowflake(int x, int y, ArrayList<StationaryObject> emptyspaces) {
        super(-App.SPRITESIZE, -App.SPRITESIZE);
        this.startingX = x;
        this.startingY = y;
        this.emptyspaces = emptyspaces;
        this.collected = false;
        this.startingTimer = 0;
        this.timer = 0;
    }

    /**
     * Sets the snowflake as collected and sets its position to -20, -20.
     * @param   wizard  The wizard (player) to  know how far away to respawn.
    */
    public void setCollected(Wizard wizard) {
        this.x = -App.SPRITESIZE;
        this.y = -App.SPRITESIZE;
        this.collected = true;
        this.wizard = wizard;
    }

    /**
     * Initially spawns snowflake after 8 seconds, checks for collected, will make the snowflake later respawn after collected.
    */
    public void tick() {
        if (this.startingTimer == 8 * App.FPS) { // Snowflake initially spawns after 8 seconds
            this.x = this.startingX;
            this.y = this.startingY;
        }

        if (this.collected) {
            if (this.timer == 0) {
                this.secondsBeforeSpawn = App.randomGenerator.nextInt(6) + 5; // Random number of seconds between 5 and 10
            }
            this.timer++;
        }

        if (this.timer >= this.secondsBeforeSpawn * App.FPS && this.collected) {
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
            this.timer = 0;
            this.collected = false;
        }

        this.startingTimer++;
    }
}