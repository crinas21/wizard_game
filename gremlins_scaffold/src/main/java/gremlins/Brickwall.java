package gremlins;

import processing.core.PApplet;

public class Brickwall extends GameObject {

    protected boolean isDestroyed;
    protected boolean isDestroyedFully;
    protected int timer;
    protected String spriteString;

    public Brickwall(int x, int y) {
        super(x, y);
        this.isDestroyed = false;
        this.isDestroyedFully = false;
        this.timer = 0;
    }

    /**
     * Checks if the brickwall has been destroyed, but nor fully.
    */
    public boolean checkDestroyed() {
        return this.isDestroyed;
    }

    /**
     * Checks if the brickwall has been destroyed fully.
    */
    public boolean checkDestroyedFully() {
        return this.isDestroyedFully;
    }

    /**
     * Sets the brickwall to be destroyed.
    */
    public void destroyBrickwall() {
        this.isDestroyed = true;
    }

    /**
     * Sets the brickwall's destroyed sprites based on the time it has been since destroyed.
    */
    public void setDestroyedSprite(PApplet app) {
        this.spriteString = "src/main/resources/gremlins/brickwall_destroyed0.png";

        if (this.timer >= 4 && this.timer < 8) {
            this.spriteString = "src/main/resources/gremlins/brickwall_destroyed1.png";
        } else if (this.timer >= 8 && this.timer < 12) {
            this.spriteString = "src/main/resources/gremlins/brickwall_destroyed2.png";
        } else if (this.timer >= 12 && this.timer < 16) {
            this.spriteString = "src/main/resources/gremlins/brickwall_destroyed3.png";
        } else if (this.timer >= 16) {
            this.isDestroyedFully = true;
        }
        this.setSprite(app.loadImage(spriteString));
        this.timer++;
    }
}