package gremlins;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class GremlinTest {

    public Gremlin getExampleGremlin() {
        ArrayList<StationaryObject> stonewalls = new ArrayList<StationaryObject>();
        ArrayList<Brickwall> brickwalls = new ArrayList<Brickwall>();
        Wizard wizard = new Wizard(20, 20, null, null, null, null, null, null, null);
        ArrayList<StationaryObject> emptyspaces = new ArrayList<StationaryObject>();
        Gremlin gremlin = new Gremlin(20,20, stonewalls, brickwalls, wizard, emptyspaces);
        return gremlin;
    }

    @Test
    public void constructor() {
        Gremlin gremlin = new Gremlin(20, 20, null, null, null, null);
        assertNotNull(gremlin);
        assertNull(gremlin.wizard);
        assertNull(gremlin.emptyspaces);
        assertFalse(Gremlin.frozen);
        assertEquals(0, Gremlin.frozenTimer);
    }

    @Test
    public void getFrozenTest() {
        assertFalse(Gremlin.getFrozen());
    }

    @Test
    public void getFrozenTimerTest() {
        assertEquals(0, Gremlin.getFrozenTimer());
    }

    @Test
    public void freezeTest() {
        Gremlin.freeze();
        assertTrue(Gremlin.frozen);
    }

    @Test
    public void resetFreezeTest() {
        Gremlin.resetFreeze();
        assertFalse(Gremlin.frozen);
        assertEquals(0, Gremlin.frozenTimer);
    }

    @Test
    public void getDirectionIndexTest() {
        Gremlin gremlin = new Gremlin(20, 20, null, null, null, null);
        assertEquals(2, gremlin.getDirectionIndex(2));
        assertEquals(0, gremlin.getDirectionIndex(4));
        assertEquals(2, gremlin.getDirectionIndex(6));
    }

    @Test
    public void getDirectionTest() {
        Gremlin gremlin = getExampleGremlin();
        gremlin.tick();
        assertTrue(gremlin.getDirection() == "left" || gremlin.getDirection() == "up" || gremlin.getDirection() == "right" || gremlin.getDirection() == "down");
    }

    @Test
    public void giveWizardTest() {
        Gremlin gremlin = new Gremlin(20, 20, null, null, null, null);
        Wizard wizard = new Wizard(40, 40, null, null, null, null, null, null, null);
        gremlin.giveWizard(wizard);
        assertTrue(gremlin.wizard == wizard);
    }

    @Test
    public void killTest() {
        Wizard wizard = new Wizard(40, 40, null, null, null, null, null, null, null);
        ArrayList<StationaryObject> emptyspaces = new ArrayList<StationaryObject>();
        emptyspaces.add(new StationaryObject(60, 60)); // This is less than 10 tiles away from wizard
        emptyspaces.add(new StationaryObject(500, 500)); // This is more than 10 tiles away from wizard, so this space should always be chosen
        Gremlin gremlin = new Gremlin(58, 40, null, null, wizard, emptyspaces);
        
        gremlin.kill();
        assertEquals(500, gremlin.x);
        assertEquals(500, gremlin.y);
    }

    @Test
    public void movementTest() {
        // Check gremlin moves in the right direction with correct speed
        Gremlin gremlin = getExampleGremlin();
        gremlin.tick();
        if (gremlin.direction == "left") {
            assertEquals(19, gremlin.x);
        } else if (gremlin.direction == "up") {
            assertEquals(19, gremlin.y);
        } else if (gremlin.direction == "right") {
            assertEquals(21, gremlin.x);
        } else if (gremlin.direction == "down") {
            assertEquals(21, gremlin.y);
        }
    }

    @Test
    public void collisionWithNoDirectionsTest() {
        // Tests if the gremlin meets a dead end, it will go back the way it came
        Gremlin gremlin = getExampleGremlin();
        gremlin.stonewalls.add(new StationaryObject(20, 0)); // Above the gremlin
        gremlin.stonewalls.add(new StationaryObject(0, 20)); // To the left of the gremlin
        gremlin.stonewalls.add(new StationaryObject(20, 40)); // Below the gremlin
        gremlin.directionIndex = 0; // Direction set as left

        gremlin.tick();
        assertEquals(2, gremlin.directionIndex); // This directionIndex is for right
    }

    @Test
    public void collisionWithOneDirectionTest1() {
        Gremlin gremlin = getExampleGremlin();
        gremlin.stonewalls.add(new StationaryObject(20, 0)); // Above the gremlin
        gremlin.stonewalls.add(new StationaryObject(0, 20)); // To the left of the gremlin
        gremlin.directionIndex = 0;

        gremlin.tick();
        assertEquals(3, gremlin.directionIndex); // This directionIndex is for down
    }
    
    @Test
    public void collisionWithOneDirectionTest2() {
        Gremlin gremlin = getExampleGremlin();
        gremlin.stonewalls.add(new StationaryObject(20, 40)); // Below the gremlin
        gremlin.stonewalls.add(new StationaryObject(0, 20)); // To the left of the gremlin
        gremlin.directionIndex = 0;

        gremlin.tick();
        assertEquals(1, gremlin.directionIndex); // This directionIndex is for down
    }

    @Test
    public void collisionWithTwoDirectionsTest() {
        Gremlin gremlin = getExampleGremlin();
        gremlin.stonewalls.add(new StationaryObject(0, 20)); // To the left of the gremlin
        gremlin.directionIndex = 0;

        gremlin.tick();
        assertTrue(gremlin.directionIndex == 1 ^ gremlin.directionIndex == 3); // This is exclusive or for up and down
    }

    @Test
    public void frozenTest(){
        Gremlin gremlin = getExampleGremlin();
        Gremlin.numGremlins = 4;
        Gremlin.frozen = true;
        gremlin.tick();
        assertEquals(1, Gremlin.frozenTimer);

        Gremlin.frozenTimer = 2400;
        gremlin.tick();
        assertEquals(0, Gremlin.frozenTimer);
        assertFalse(Gremlin.frozen);
    }
}