package gremlins;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class SlimeTest {

    public Slime getExampleSlime() {
        ArrayList<StationaryObject> stonewalls = new ArrayList<StationaryObject>();
        ArrayList<Brickwall> brickwalls = new ArrayList<Brickwall>();
        Slime slime = new Slime(20, 20, stonewalls, brickwalls, "left");
        return slime;
    }

    @Test
    public void constructor() {
        Slime slime = new Slime(20, 20, null, null, null);
        assertNull(slime.direction);
        assertFalse(slime.collided);
    }

    @Test
    public void getDirectionTest() {
        Slime slime = new Slime(20, 20, null, null, "left");
        assertTrue(slime.getDirection() == "left");
    }

    @Test
    public void getCollidedTest() {
        Slime slime = new Slime(20, 20, null, null, "left");
        assertFalse(slime.getCollided());
    }

    @Test
    public void breakSlimeTest() {
        Slime slime = new Slime(20, 20, null, null, "left");
        slime.breakSlime();
        assertTrue(slime.collided);
    }

    @Test
    public void movementTest() {
        // Check slime moves in the right direction with correct speed
        Slime slime = getExampleSlime();
        slime.tick();
        assertEquals(16, slime.x);
    }

    @Test
    public void wallCollisionTest() {
        Slime slime = getExampleSlime();
        slime.brickwalls.add(new Brickwall(0, 20));
        slime.tick();
        assertTrue(slime.collided);
    }
}
