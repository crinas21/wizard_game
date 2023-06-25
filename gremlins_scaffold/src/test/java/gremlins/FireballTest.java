package gremlins;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import  java.util.ArrayList;

public class FireballTest {

    public Fireball getExampleFireball() {
        ArrayList<StationaryObject> stonewalls = new ArrayList<StationaryObject>();
        ArrayList<Brickwall> brickwalls = new ArrayList<Brickwall>();
        ArrayList<Gremlin> gremlins = new ArrayList<Gremlin>();
        ArrayList<Slime> slimes = new ArrayList<Slime>();
        Fireball fireball = new Fireball(20, 20, stonewalls, brickwalls, gremlins, slimes, "left");
        return fireball;
    }

    @Test
    public void constructor() {
        Fireball fireball = new Fireball(20, 20, null, null, null, null, null);
        assertNull(fireball.gremlins);
        assertNull(fireball.slimes);
        assertNull(fireball.direction);
        assertFalse(fireball.collided);
    }

    @Test
    public void getDirectionTest() {
        Fireball fireball = new Fireball(20, 20, null, null, null, null, "left");
        assertTrue(fireball.getDirection() == "left");
    }

    @Test
    public void getCollidedTest() {
        Fireball fireball = new Fireball(20, 20, null, null, null, null, null);
        assertFalse(fireball.getCollided());

    }

    @Test
    public void movementTest() {
        // Check fireball moves in the right direction with the right speed
        Fireball fireball = getExampleFireball();
        fireball.tick();
        assertEquals(16, fireball.x);
    }

    @Test
    public void brickwallCollisionTest() {
        Fireball fireball = getExampleFireball();
        Brickwall brickwall = new Brickwall(0, 20);
        fireball.brickwalls.add(brickwall);
        fireball.tick();
        assertTrue(brickwall.isDestroyed);
    }

    @Test
    public void wallCollisionTest() {
        Fireball fireball = getExampleFireball();
        StationaryObject stonewall = new StationaryObject(0, 20);
        fireball.stonewalls.add(stonewall);
        fireball.tick();
        assertTrue(fireball.collided);
    }

    @Test
    public void gremlinCollisionTest() {
        ArrayList<StationaryObject> emptyspaces = new ArrayList<StationaryObject>();
        emptyspaces.add(new StationaryObject(500, 500));
        Wizard wizard = new Wizard(0, 0, null, null, null, null, null, null, null);
        Gremlin gremlin = new Gremlin(30, 20, null, null, wizard, emptyspaces);

        Fireball fireball = getExampleFireball();
        fireball.gremlins.add(gremlin);
        fireball.tick();
        assertTrue(fireball.collided);
    }

    @Test
    public void slimeCollisionTest() {
        Fireball fireball = getExampleFireball();
        Slime slime = new Slime(30, 20, null, null, null);
        fireball.slimes.add(slime);
        fireball.tick();
        assertTrue(fireball.collided);
    }
}