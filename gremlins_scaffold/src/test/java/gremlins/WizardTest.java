package gremlins;

import processing.core.PApplet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class WizardTest {

    public Wizard getExampleWizard() {
        ArrayList<StationaryObject> stonewalls = new ArrayList<StationaryObject>();
        ArrayList<Brickwall> brickwalls = new ArrayList<Brickwall>();
        ArrayList<Gremlin> gremlins = new ArrayList<Gremlin>();
        ArrayList<Slime> slimes = new ArrayList<Slime>();
        ArrayList<StationaryObject> doors = new ArrayList<StationaryObject>();
        ArrayList<StationaryObject> portals = new ArrayList<StationaryObject>();
        Snowflake snowflake = new Snowflake(60, 60, null);
        Wizard wizard = new Wizard(20, 20, stonewalls, brickwalls, gremlins, slimes, doors, portals, snowflake);
        return wizard;
    }

    @Test
    public void constructor() {
        Wizard wizard = new Wizard(20, 20, null, null, null, null, null, null, null);
        assertNotNull(wizard);
        assertNull(wizard.gremlins);
        assertNull(wizard.slimes);
        assertNull(wizard.doors);
        assertNull(wizard.portals);
        assertNull(wizard.snowflake);
        assertTrue(wizard.isAlive);
        assertFalse(wizard.goNextLevel);
        assertTrue(wizard.facingDirection == "right");
        assertFalse(wizard.fired);
        assertTrue(wizard.readyForPortal);
    }

    @Test
    public void getIsAliveTest() {
        Wizard wizard = new Wizard(20, 20, null, null, null, null, null, null, null);
        assertTrue(wizard.getIsAlive());
    }

    @Test
    public void getGoNextLevelTest() {
        Wizard wizard = new Wizard(20, 20, null, null, null, null, null, null, null);
        assertFalse(wizard.getGoNextLevel());
    }

    @Test
    public void getDirectionTest() {
        Wizard wizard = new Wizard(20, 20, null, null, null, null, null, null, null);
        assertTrue(wizard.getDirection() == "right");
    }

    @Test
    public void setFiredTest() {
        Wizard wizard = new Wizard(20, 20, null, null, null, null, null, null, null);
        wizard.setFired(true);
        assertTrue(wizard.getFired());
    }

    @Test
    public void pressLeftTest() {
        App app = new App();
        app.noLoop();
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();
        app.keyCode = 37;
        app.keyPressed();

        assertTrue(app.wizard.moveLeft);
        assertFalse(app.wizard.moveUp);
        assertFalse(app.wizard.moveRight);
        assertFalse(app.wizard.moveDown);
        assertTrue(app.wizard.facingDirection == "left");

        app.wizard.moveUp = true;
        app.keyPressed();
        assertFalse(app.wizard.moveUp);
        assertTrue(app.wizard.verticalUp);

        app.wizard.moveUp = false;
        app.wizard.moveDown = true;
        app.keyPressed();
        assertFalse(app.wizard.moveDown);
        assertTrue(app.wizard.verticalDown);
    }

    @Test
    public void pressUpTest() {
        App app = new App();
        app.noLoop();
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();
        app.keyCode = 38;
        app.keyPressed();

        assertFalse(app.wizard.moveLeft);
        assertTrue(app.wizard.moveUp);
        assertFalse(app.wizard.moveRight);
        assertFalse(app.wizard.moveDown);
        assertTrue(app.wizard.facingDirection == "up");

        app.wizard.moveLeft = true;
        app.keyPressed();
        assertFalse(app.wizard.moveLeft);
        assertTrue(app.wizard.horizontalLeft);

        app.wizard.moveLeft = false;
        app.wizard.moveRight = true;
        app.keyPressed();
        assertFalse(app.wizard.moveRight);
        assertTrue(app.wizard.horizontalRight);
    }

    @Test
    public void pressRightTest() {
        App app = new App();
        app.noLoop();
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();
        app.keyCode = 39;
        app.keyPressed();

        assertFalse(app.wizard.moveLeft);
        assertFalse(app.wizard.moveUp);
        assertTrue(app.wizard.moveRight);
        assertFalse(app.wizard.moveDown);
        assertTrue(app.wizard.facingDirection == "right");

        app.wizard.moveUp = true;
        app.keyPressed();
        assertFalse(app.wizard.moveUp);
        assertTrue(app.wizard.verticalUp);

        app.wizard.moveUp = false;
        app.wizard.moveDown = true;
        app.keyPressed();
        assertFalse(app.wizard.moveDown);
        assertTrue(app.wizard.verticalDown);
    }

    @Test
    public void pressDownTest() {
        App app = new App();
        app.noLoop();
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();
        app.keyCode = 40;
        app.keyPressed();

        assertFalse(app.wizard.moveLeft);
        assertFalse(app.wizard.moveUp);
        assertFalse(app.wizard.moveRight);
        assertTrue(app.wizard.moveDown);
        assertTrue(app.wizard.facingDirection == "down");

        app.wizard.moveLeft = true;
        app.keyPressed();
        assertFalse(app.wizard.moveLeft);
        assertTrue(app.wizard.horizontalLeft);

        app.wizard.moveLeft = false;
        app.wizard.moveRight = true;
        app.keyPressed();
        assertFalse(app.wizard.moveRight);
        assertTrue(app.wizard.horizontalRight);
    }

    @Test
    public void LeftMovementTest() {
        Wizard wizard = getExampleWizard();
        wizard.pressLeft();
        wizard.tick();
        assertEquals(18, wizard.x);

        // Run tick an arbitrary amount of times to test that after button is released, the wizard moves to the next whole tile
        wizard.releaseLeft();
        for (int i = 0; i < 30; i++) {
            wizard.tick();
        }
        assertEquals(0, wizard.x);
    }

    @Test
    public void UpMovementTest() {
        Wizard wizard = getExampleWizard();
        wizard.pressUp();
        wizard.tick();
        assertEquals(18, wizard.y);

        wizard.releaseUp();
        for (int i = 0; i < 30; i++) {
            wizard.tick();
        }
        assertEquals(0, wizard.y);
    }

    @Test
    public void RightMovementTest() {
        Wizard wizard = getExampleWizard();
        wizard.pressRight();
        wizard.tick();
        assertEquals(22, wizard.x);

        wizard.releaseRight();
        for (int i = 0; i < 30; i++) {
            wizard.tick();
        }
        assertEquals(40, wizard.x);
    }

    @Test
    public void DownMovementTest() {
        Wizard wizard = getExampleWizard();
        wizard.pressDown();
        wizard.tick();
        assertEquals(22, wizard.y);

        wizard.releaseDown();
        for (int i = 0; i < 30; i++) {
            wizard.tick();
        }
        assertEquals(40, wizard.y);
    }

    @Test
    public void TouchGremlinTest() {
        Wizard wizard = getExampleWizard();
        wizard.gremlins.add(new Gremlin(39, 21, null, null, null, null));
        wizard.tick();
        assertFalse(wizard.isAlive);
    }

    @Test
    public void TouchSlimeTest() {
        Wizard wizard = getExampleWizard();
        wizard.slimes.add(new Slime(39, 21, null, null, null));
        wizard.tick();
        assertFalse(wizard.isAlive);
    }

    @Test
    public void TouchDoorTest() {
        Wizard wizard = getExampleWizard();
        wizard.x = 22;
        wizard.doors.add(new StationaryObject(40, 20));
        wizard.tick();
        assertTrue(wizard.goNextLevel);
    }

    @Test
    public void TouchPortalTest() {
        // Tests wizard's x and y coordinates change to another portal when touches a porttal
        Wizard wizard = getExampleWizard();
        wizard.x = 22;
        wizard.portals.add(new StationaryObject(40, 20));
        wizard.portals.add(new StationaryObject(80, 60));
        wizard.tick();
        assertFalse(wizard.readyForPortal);
        assertEquals(80, wizard.x);
        assertEquals(60, wizard.y);
    }

    @Test
    public void TouchSnowflakeTest() {
        Wizard wizard = getExampleWizard();
        wizard.x = 22;
        Snowflake snowflake = new Snowflake(80, 20, null);
        wizard.snowflake = snowflake;

        assertFalse(wizard.snowflakeCollision());
        wizard.snowflake.x = 40;
        wizard.snowflake.y = 20;
        assertTrue(wizard.snowflakeCollision());

        wizard.tick();
        assertEquals(-20, snowflake.x);
        assertEquals(-20, snowflake.y);
        assertTrue(snowflake.collected);
        assertTrue(snowflake.wizard == wizard);
    }
}