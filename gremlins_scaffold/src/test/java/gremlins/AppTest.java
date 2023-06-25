package gremlins;

import processing.core.PApplet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AppTest {
    @Test
    public void constructor() {
        App app = new App();
        assertNotNull(app);
        assertTrue(app.configPath == "config.json");
        assertEquals(2, app.numLevels);
        assertEquals(3, app.lives);
        assertEquals(0, app.levelIndex);
    }

    @Test
    public void setupTest() {
        App app = new App();
        app.noLoop();
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();

        assertEquals(0.3333, app.wizardCooldown);
        assertEquals(3.0, app.enemyCooldown);
        assertEquals(21, app.wizardTimer);
        assertEquals(0, app.gremlinTimer);
    }

    @Test
    public void spacePressTest() {
        App app = new App();
        app.noLoop();
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();

        app.keyCode = 32;
        app.keyPressed();
        assertEquals(1, app.fireballs.size());
        assertTrue(app.wizard.fired);
        assertEquals(0, app.wizardTimer);
    }

    @Test
    public void anyKeyPressedGameOverTest() {
        App app = new App();
        app.noLoop();
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();

        app.lives = 0;
        app.keyPressed();
        assertEquals(3, app.lives);
        assertEquals(0, app.levelIndex);
    }

    @Test
    public void anyKeyPressedGameWonTest() {
        App app = new App();
        app.noLoop();
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();

        app.levelIndex = 2;
        app.keyPressed();
        assertEquals(3, app.lives);
        assertEquals(0, app.levelIndex);
    }

    @Test
    public void keyReleasedTest() {
        App app = new App();
        app.noLoop();
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();

        app.keyCode = 37;
        app.keyReleased();
        assertFalse(app.wizard.moveLeft);
        assertTrue(app.wizard.horizontalLeft);

        app.keyCode = 38;
        app.keyReleased();
        assertFalse(app.wizard.moveUp);
        assertTrue(app.wizard.verticalUp);

        app.keyCode = 39;
        app.keyReleased();
        assertFalse(app.wizard.moveRight);
        assertTrue(app.wizard.horizontalRight);

        app.keyCode = 40;
        app.keyReleased();
        assertFalse(app.wizard.moveDown);
        assertTrue(app.wizard.verticalDown);
    }

    @Test
    public void drawLayoutTest() {
        App app = new App();
        app.noLoop();
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();

        // Test if wizard's mana is recharged that the fired variable is set to false
        app.wizard.fired = true;
        app.wizardTimer = 60;
        app.drawLayout();
        assertFalse(app.wizard.fired);
    }

    @Test
    public void evaluateBrickwallsTest() {
        App app = new App();
        app.noLoop();
        PApplet.runSketch(new String[] { "App" }, app);
        Brickwall brickwall = new Brickwall(20, 20);
        brickwall.sprite = app.loadImage("src/main/resources/gremlins/brickwall.png");
        app.brickwalls.add(brickwall);

        // Test when a brickwall is not destroyed, it remains in the brickwalls list
        app.evaluateBrickwalls();
        assertEquals(1, app.brickwalls.size());
        assertEquals(0, app.destroyedBrickwalls.size());

        // Test when a brickwall is destroyed, it is removed from brickwalls and added to destroyedBrickwalls
        app.brickwalls.get(0).isDestroyed = true;
        app.evaluateBrickwalls();
        assertEquals(0, app.brickwalls.size());
        assertEquals(1, app.destroyedBrickwalls.size());
        assertNotNull(app.destroyedBrickwalls.get(0));
    }

    @Test
    public void evaluateGremlinsTest() {
        App app = new App();
        app.noLoop();
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();
        app.delay(1000);

        // Test initally no slimes spawn from the gremlins
        app.evaluateGremlins();
        assertEquals(0, app.slimes.size());
    }

    @Test
    public void evaluateSlimesTest() {
        App app = new App();
        app.noLoop();
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();

        Slime slime = new Slime(20, 20, null, null, null);
        slime.collided = true;
        app.slimes.add(slime);

        // Tests when a slime has collided it is removed from the list
        assertEquals(1, app.slimes.size());
        app.evaluateSlimes();
        assertEquals(0, app.slimes.size());
    }

    @Test
    public void evaluateFireballsTest() {
        App app = new App();
        app.noLoop();
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();

        Fireball fireball = new Fireball(20, 20, null, null, null, null, null);
        fireball.collided = true;
        app.fireballs.add(fireball);

        // Tests when a fireball has collided it is removed from the list
        assertEquals(1, app.fireballs.size());
        app.evaluateFireballs();
        assertEquals(0, app.fireballs.size());
    }

    @Test
    public void nextLevelTest() {
        App app = new App();
        app.noLoop();
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();

        // Branch where nothing should happen since goNextLevel is false
        app.nextLevel();
        assertEquals(0, app.levelIndex);

        // Player goes to the next level but does not win
        app.wizard.goNextLevel = true;
        app.nextLevel();
        assertEquals(1, app.levelIndex);

        // Player wins
        app.wizard.goNextLevel = true;
        assertTrue(app.levelIndex >= app.numLevels-1);
        app.nextLevel();
        assertEquals(2, app.levelIndex);
    }

    @Test
    public void lostLifeTest() {
        App app = new App();
        app.noLoop();
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();
        app.delay(1000);

        // Branch where nothing happens
        app.lostLife();
        assertEquals(3, app.lives);

        // Player loses a life
        app.wizard.isAlive = false;
        app.lostLife();
        assertEquals(2, app.lives);
    }
}
