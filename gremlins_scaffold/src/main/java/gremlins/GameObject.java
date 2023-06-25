package gremlins;

import processing.core.PApplet;
import processing.core.PImage;

public abstract class GameObject {

    protected int x;
    protected int y;
    protected PImage sprite;

    public GameObject(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Sets the object's sprite
     * @param   sprite  The sprite that the object should have.
    */
    public void setSprite(PImage sprite) {
        this.sprite = sprite;
    }

    /**
     * Draws the object onto the screen.
     * @param   app The PApplet object.
    */
    public void draw(PApplet app) {
        app.image(this.sprite, this.x, this.y);
    }

    /**
     * Gives the object's x coordinate.
    */
    public int getX() {
        return this.x;
    }

    /**
     * Gives the object's y coordinate.
    */
    public int getY() {
        return this.y;
    }
}