package gremlins;


import processing.core.PImage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameObjectTest {
    @Test
    public void setSpriteTest() {
        Wizard wizard = new Wizard(20, 20, null, null, null, null, null, null, null);
        PImage sprite = null;
        wizard.setSprite(sprite);
        assertNull(wizard.sprite);
    }

    @Test
    public void getXTest() {
        Wizard wizard = new Wizard(20, 40, null, null, null, null, null, null, null);
        assertEquals(20, wizard.getX());
    }

    @Test
    public void getYTest() {
        Wizard wizard = new Wizard(20, 40, null, null, null, null, null, null, null);
        assertEquals(40, wizard.getY());
    }
}
