package gremlins;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class MovingObjectTest {
    @Test
    public void moveTest() {
        Gremlin gremlin = new Gremlin(20, 40, null, null, null, null);
        gremlin.move("left");
        assertEquals(19, gremlin.getX());
        assertEquals(40, gremlin.getY());
        gremlin.move("right");
        assertEquals(20, gremlin.getX());
        assertEquals(40, gremlin.getY());
        gremlin.move("up");
        assertEquals(20, gremlin.getX());
        assertEquals(39, gremlin.getY());
        gremlin.move("down");
        assertEquals(20, gremlin.getX());
        assertEquals(40, gremlin.getY());
        gremlin.move("neither");
        assertEquals(20, gremlin.getX());
        assertEquals(40, gremlin.getY());
    }

    @Test
    public void findObjectRangeXTest() {
        Gremlin gremlin = new Gremlin(10, 20, null, null, null, null);
        int[] expectedRangeX = {10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30};
        int[] objectRangeX = gremlin.findObjectRangeX();
        assertArrayEquals(expectedRangeX, objectRangeX);
    }

    @Test
    public void findObjectRangeYTest() {
        Gremlin gremlin = new Gremlin(20, 10, null, null, null, null);
        int[] expectedRangeY = {10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30};
        int[] objectRangeY = gremlin.findObjectRangeY();
        assertArrayEquals(expectedRangeY, objectRangeY);
    }

    @Test
    public void inObstacleRangeTest() {
        Gremlin gremlin = new Gremlin(20, 40, null, null, null, null);
        int[] objectRangeX = gremlin.findObjectRangeX();
        assertTrue(gremlin.inObstacleRange(objectRangeX, 38, 58));
        assertFalse(gremlin.inObstacleRange(objectRangeX, 0, 20));
    }

    @Test
    public void obstacleCollisionIndividualTest() {
        Gremlin gremlin = new Gremlin(20, 40, null, null, null, null);
        Wizard wizard = new Wizard(39, 59, null, null, null, null, null, null, null);
        StationaryObject stonewall1 = new StationaryObject(0, 40);
        StationaryObject stonewall2 = new StationaryObject(20, 20);
        StationaryObject stonewall3 = new StationaryObject(40, 40);
        StationaryObject stonewall4 = new StationaryObject(20, 60);
        StationaryObject stonewall5 = new StationaryObject(0, 60);
        
        int[] stonewall1Sides = {stonewall1.getX(), stonewall1.getY(), stonewall1.getX()+20, stonewall1.getY()+20};
        int[] stonewall2Sides = {stonewall2.getX(), stonewall2.getY(), stonewall2.getX()+20, stonewall2.getY()+20};
        int[] stonewall3Sides = {stonewall3.getX(), stonewall3.getY(), stonewall3.getX()+20, stonewall3.getY()+20};
        int[] stonewall4Sides = {stonewall4.getX(), stonewall4.getY(), stonewall4.getX()+20, stonewall4.getY()+20};
        int[] stonewall5Sides = {stonewall5.getX(), stonewall5.getY(), stonewall5.getX()+20, stonewall5.getY()+20};
        int[] wizardSides = {wizard.getX(), wizard.getY(), wizard.getX()+20, wizard.getY()+20};

        assertTrue(gremlin.obstacleCollisionIndividual(stonewall1Sides, "left"));
        assertTrue(gremlin.obstacleCollisionIndividual(stonewall2Sides, "up"));
        assertTrue(gremlin.obstacleCollisionIndividual(stonewall3Sides, "right"));
        assertTrue(gremlin.obstacleCollisionIndividual(stonewall4Sides, "down"));
        assertTrue(gremlin.obstacleCollisionIndividual(wizardSides, "touching"));
        assertFalse(gremlin.obstacleCollisionIndividual(stonewall5Sides, "touching"));
    }

    @Test
    public void obstacleCollisionTest() {
        Wizard wizard = new Wizard(20, 40, null, null, null, null, null, null, null);

        ArrayList<Gremlin> gremlins = new ArrayList<Gremlin>();
        gremlins.add(new Gremlin(0, 20, null, null, null, null));
        gremlins.add(new Gremlin(100, 100, null, null, null, null));
        assertFalse(wizard.obstacleCollision("touching", gremlins));

        gremlins.add(new Gremlin(21, 41, null, null, null, null));
        assertTrue(wizard.obstacleCollision("touching", gremlins));
    }

    @Test
    public void wallCollisionTest() {
        ArrayList<Brickwall> brickwalls = new ArrayList<Brickwall>();
        ArrayList<StationaryObject> stonewalls = new ArrayList<StationaryObject>();
        Wizard wizard = new Wizard(20, 40, stonewalls, brickwalls, null, null, null, null, null);

        wizard.brickwalls.add(new Brickwall(100, 100));
        wizard.stonewalls.add(new StationaryObject(80, 80));
        assertFalse(wizard.wallCollision("left"));
        
        wizard.brickwalls.add(new Brickwall(0, 40));
        assertTrue(wizard.wallCollision("left"));
        assertFalse(wizard.wallCollision("right"));

        wizard.stonewalls.add(new StationaryObject(40, 40));
        assertTrue(wizard.wallCollision("right"));
    }
}
