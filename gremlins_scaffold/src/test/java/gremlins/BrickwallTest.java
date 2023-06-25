package gremlins;

import processing.core.PApplet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BrickwallTest {
    @Test
    public void constructor() {
        Brickwall brickwall = new Brickwall(20, 20);
        assertNotNull(brickwall);
        assertFalse(brickwall.isDestroyed);
        assertFalse(brickwall.isDestroyedFully);
        assertEquals(0, brickwall.timer);
    }

    @Test
    public void checkDestroyedTest() {
        Brickwall brickwall = new Brickwall(20, 20);
        assertFalse(brickwall.checkDestroyed());
    }

    @Test
    public void checkDestroyedFullyTest() {
        Brickwall brickwall = new Brickwall(20, 20);
        assertFalse(brickwall.checkDestroyedFully());
    }

    @Test
    public void destroyBrickwallTest() {
        Brickwall brickwall = new Brickwall(20, 20);
        brickwall.destroyBrickwall();
        assertTrue(brickwall.isDestroyed);
    }

    @Test
    public void setDestroyedSpriteTest() {
        App app = new App();
        app.noLoop();
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();
        Brickwall brickwall = new Brickwall(20, 20);

        brickwall.setDestroyedSprite(app);
        assertTrue(brickwall.spriteString == "src/main/resources/gremlins/brickwall_destroyed0.png");
        assertEquals(1, brickwall.timer);

        brickwall.timer = 4;
        brickwall.setDestroyedSprite(app);
        assertTrue(brickwall.spriteString == "src/main/resources/gremlins/brickwall_destroyed1.png");

        brickwall.timer = 8;
        brickwall.setDestroyedSprite(app);
        assertTrue(brickwall.spriteString == "src/main/resources/gremlins/brickwall_destroyed2.png");

        brickwall.timer = 12;
        brickwall.setDestroyedSprite(app);
        assertTrue(brickwall.spriteString == "src/main/resources/gremlins/brickwall_destroyed3.png");

        brickwall.timer = 16;
        brickwall.setDestroyedSprite(app);
        assertTrue(brickwall.isDestroyedFully);
    }
}
