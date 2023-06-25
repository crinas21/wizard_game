package gremlins;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class SnowflakeTest {

    public Snowflake getExampleSnowflake() {
        ArrayList<StationaryObject> emptyspaces = new ArrayList<StationaryObject>();
        Snowflake snowflake = new Snowflake(0, 0, emptyspaces);
        Wizard wizard = new Wizard(20, 20, null, null, null, null, null, null, null);
        snowflake.wizard = wizard;
        return snowflake;
    }

    @Test
    public void constructor() {
        Snowflake snowflake = new Snowflake(20, 20, null);
        assertNotNull(snowflake);
        assertEquals(-20, snowflake.x);
        assertEquals(-20, snowflake.y);
        assertEquals(20, snowflake.startingX);
        assertEquals(20, snowflake.startingY);
        assertNull(snowflake.emptyspaces);
        assertFalse(snowflake.collected);
        assertEquals(0, snowflake.startingTimer);
        assertEquals(0, snowflake.timer);
    }

    @Test
    public void setCollectedTest() {
        Snowflake snowflake = new Snowflake(20, 20, null);
        Wizard wizard = new Wizard(20, 20, null, null, null, null, null, null, null);
        snowflake.setCollected(wizard);

        assertEquals(-20, snowflake.x);
        assertEquals(-20, snowflake.y);
        assertTrue(snowflake.collected);
        assertTrue(snowflake.wizard == wizard);
    }

    @Test
    public void initialSpawnTest() {
        // Tests that the snowflake will initially spawn at the location specified in the level text file
        Snowflake snowflake = getExampleSnowflake();
        snowflake.startingTimer = 480;
        snowflake.tick();
        assertEquals(0, snowflake.x);
        assertEquals(0, snowflake.y);
    }

    @Test
    public void collectedTest() {
        Snowflake snowflake = getExampleSnowflake();
        snowflake.collected = true;
        snowflake.tick();
        assertTrue(snowflake.secondsBeforeSpawn==5 ^ snowflake.secondsBeforeSpawn==6 ^ snowflake.secondsBeforeSpawn==7 ^ snowflake.secondsBeforeSpawn==8 ^ snowflake.secondsBeforeSpawn==9 ^ snowflake.secondsBeforeSpawn==10);
        assertEquals(1, snowflake.timer);
    }

    @Test
    public void newSpawnTest() {
        // Tests the snowflake will randomly respawn on a tile at least 10 away from the player
        Snowflake snowflake = getExampleSnowflake();
        snowflake.emptyspaces.add(new StationaryObject(40, 40));
        snowflake.emptyspaces.add(new StationaryObject(500, 500));
        snowflake.timer = 900;
        snowflake.collected = true;
        
        snowflake.tick();
        assertEquals(500, snowflake.x);
        assertEquals(500, snowflake.y);
        assertEquals(0, snowflake.timer);
        assertFalse(snowflake.collected);
        assertEquals(1, snowflake.startingTimer);
    }
}