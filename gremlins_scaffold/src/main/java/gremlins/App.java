package gremlins;

import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONObject;
import processing.data.JSONArray;

import java.util.*;
import java.io.*;


public class App extends PApplet {

    public static final int WIDTH = 720;
    public static final int HEIGHT = 720;
    public static final int SPRITESIZE = 20;
    public static final int BOTTOMBAR = 60;
    public static final int FPS = 60;

    public static final Random randomGenerator = new Random();

    protected String configPath;
    protected JSONObject conf;
    protected JSONArray levels;
    protected int numLevels;
    protected int lives;
    protected int levelIndex;
    protected double wizardCooldown;
    protected double enemyCooldown;
    protected int wizardTimer;
    protected int gremlinTimer;
    
    protected ArrayList<StationaryObject> emptyspaces;
    protected ArrayList<StationaryObject> stonewalls;
    protected ArrayList<Brickwall> brickwalls;
    protected ArrayList<Brickwall> destroyedBrickwalls;
    protected ArrayList<Fireball> fireballs;
    protected ArrayList<Gremlin> gremlins;
    protected ArrayList<Slime> slimes;
    protected ArrayList<StationaryObject> doors;
    protected ArrayList<StationaryObject> portals;
    protected Wizard wizard;
    protected Snowflake snowflake;
    private int wizardX;
    private int wizardY;

    public App() {
        this.configPath = "config.json";
        this.conf = loadJSONObject(new File(this.configPath));
        this.levels = this.conf.getJSONArray("levels");

        this.numLevels = this.levels.size();
        this.lives = this.conf.getInt("lives");
        this.levelIndex = 0;

        this.emptyspaces = new ArrayList<StationaryObject>();
        this.stonewalls = new ArrayList<StationaryObject>();
        this.brickwalls = new ArrayList<Brickwall>();
        this.destroyedBrickwalls = new ArrayList<Brickwall>();
        this.fireballs = new ArrayList<Fireball>();
        this.gremlins = new ArrayList<Gremlin>();
        this.slimes = new ArrayList<Slime>();
        this.doors = new ArrayList<StationaryObject>();
        this.portals = new ArrayList<StationaryObject>();
    }

    /**
     * Initialise the setting of the window size.
    */
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Load all resources such as images. Initialise the elements such as the player, enemies and map elements.
    */
    public void setup() {
        frameRate(FPS);

        JSONObject currentLevel = this.levels.getJSONObject(levelIndex);
        String layout = currentLevel.getString("layout");
        this.wizardCooldown = currentLevel.getDouble("wizard_cooldown");
        this.enemyCooldown = currentLevel.getDouble("enemy_cooldown");
        this.wizardTimer = (int) Math.round(this.wizardCooldown * FPS) + 1;
        this.gremlinTimer = 0;

        this.emptyspaces.clear();
        this.stonewalls.clear();
        this.brickwalls.clear();
        this.destroyedBrickwalls.clear();
        this.fireballs.clear();
        this.gremlins.clear();;
        this.slimes.clear();
        this.doors.clear();
        this.portals.clear();

        try {
            Scanner sc = new Scanner(new File(layout));
            int y = 0;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();

                for (int x = 0; x < line.length(); x++) {
                    if (line.charAt(x) == " ".charAt(0)) {
                        StationaryObject emptyspace = new StationaryObject(x*SPRITESIZE, y*SPRITESIZE);
                        this.emptyspaces.add(emptyspace);
                    }
                    else if (line.charAt(x) == "X".charAt(0)) {
                        StationaryObject stonewall = new StationaryObject(x*SPRITESIZE, y*SPRITESIZE);
                        stonewall.setSprite(this.loadImage("src/main/resources/gremlins/stonewall.png"));
                        this.stonewalls.add(stonewall);
                    }
                    else if (line.charAt(x) == "B".charAt(0)) {
                        Brickwall brickwall = new Brickwall(x*SPRITESIZE, y*SPRITESIZE);
                        brickwall.setSprite(this.loadImage("src/main/resources/gremlins/brickwall.png"));
                        this.brickwalls.add(brickwall);
                    }
                    else if (line.charAt(x) == "G".charAt(0)) {
                        Gremlin g = new Gremlin(x*SPRITESIZE, y*SPRITESIZE, this.stonewalls, 
                            this.brickwalls, this.wizard, this.emptyspaces);
                        g.setSprite(this.loadImage("src/main/resources/gremlins/gremlin.png"));
                        this.gremlins.add(g);
                        StationaryObject emptyspace = new StationaryObject(x*SPRITESIZE, y*SPRITESIZE);
                        this.emptyspaces.add(emptyspace);
                    }
                    else if (line.charAt(x) == "W".charAt(0)) {
                        this.wizardX = x*SPRITESIZE;
                        this.wizardY = y*SPRITESIZE;
                        StationaryObject emptyspace = new StationaryObject(x*SPRITESIZE, y*SPRITESIZE);
                        this.emptyspaces.add(emptyspace);
                    }
                    else if (line.charAt(x) == "E".charAt(0)) {
                        StationaryObject door = new StationaryObject(x*SPRITESIZE, y*SPRITESIZE);
                        door.setSprite(this.loadImage("src/main/resources/gremlins/door.png"));
                        this.doors.add(door);
                    }
                    else if (line.charAt(x) == "P".charAt(0)) {
                        StationaryObject portal = new StationaryObject(x*SPRITESIZE, y*SPRITESIZE);
                        portal.setSprite(this.loadImage("src/main/resources/gremlins/portal.png"));
                        this.portals.add(portal);
                    }
                    else if (line.charAt(x) == "S".charAt(0)) {
                        this.snowflake = new Snowflake(x*SPRITESIZE, y*SPRITESIZE, this.emptyspaces);
                        this.snowflake.setSprite(this.loadImage("src/main/resources/gremlins/snowflake.png"));
                        StationaryObject emptyspace = new StationaryObject(x*SPRITESIZE, y*SPRITESIZE);
                        this.emptyspaces.add(emptyspace);
                    }
                }
                y++;
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("Could not find layout file.");
            System.exit(0);
        }

        this.wizard = new Wizard(wizardX, wizardY, this.stonewalls, this.brickwalls,
            this.gremlins, this.slimes, this.doors, this.portals, this.snowflake);
        this.wizard.setSprite(this.loadImage("src/main/resources/gremlins/wizard1.png"));

        Gremlin.giveNumGremlins(this.gremlins.size());
    }

    /**
     * Receive key pressed signal from the keyboard.
    */
    public void keyPressed(){
        if (this.lives > 0 && this.levelIndex <= this.numLevels-1) {
            if (this.keyCode == 37) {
                this.wizard.pressLeft();
                this.wizard.setSprite(this.loadImage("src/main/resources/gremlins/wizard0.png"));
            } else if (this.keyCode == 38) {
                this.wizard.pressUp();
                this.wizard.setSprite(this.loadImage("src/main/resources/gremlins/wizard2.png"));
            } else if (this.keyCode == 39) {
                this.wizard.pressRight();
                this.wizard.setSprite(this.loadImage("src/main/resources/gremlins/wizard1.png"));
            } else if (this.keyCode == 40) {
                this.wizard.pressDown();
                this.wizard.setSprite(this.loadImage("src/main/resources/gremlins/wizard3.png"));
            } else if (this.keyCode == 32 && this.wizardTimer >= this.wizardCooldown * FPS) {
                Fireball fireball = new Fireball(this.wizard.getX(), this.wizard.getY(),
                    this.stonewalls, this.brickwalls, this.gremlins, this.slimes,
                    this.wizard.getDirection());
                fireball.setSprite(this.loadImage("src/main/resources/gremlins/fireball.png"));
                this.fireballs.add(fireball);
                this.wizard.setFired(true);
                this.wizardTimer = 0;
            }
        }
        else {
            this.lives = this.conf.getInt("lives");
            this.levelIndex = 0;
            this.setup();
        }
    }
    
    /**
     * Receive key released signal from the keyboard.
    */
    public void keyReleased(){
        if (this.keyCode == 37) {
            this.wizard.releaseLeft();
        } else if (this.keyCode == 38) {
            this.wizard.releaseUp();
        } else if (this.keyCode == 39) {
            this.wizard.releaseRight();
        } else if (this.keyCode == 40) {
            this.wizard.releaseDown();
        }
    }

    /**
     * Forms all necessary text and visual indicators/timers such as mana recharge and lives left.
    */
    public void drawLayout() {
        textSize(SPRITESIZE);
        this.background(191, 153, 114);
        this.text("Lives: ", 10, 695);
        for (int i = 0; i < this.lives; i++) {
            PImage lifeSprite = this.loadImage("src/main/resources/gremlins/wizard1.png");
            this.image(lifeSprite, 70+i*SPRITESIZE, 680);
        }
        this.text("Level "+(levelIndex+1)+"/"+this.numLevels, 190, 695);

        if (this.wizard.getFired()) {
            rect(620, 680, 80, 5);
            if (this.wizardTimer < this.wizardCooldown * FPS  && this.wizardTimer > 0) {
                float rectWidth = Math.round(80*this.wizardTimer/(this.wizardCooldown * FPS));
                fill(0);
                rect(620, 680, rectWidth, 5);
                fill(255);
            } else if (this.wizardTimer != 0) {
                this.wizard.setFired(false);
            }
        }

        if (Gremlin.getFrozen()) {
            textSize(SPRITESIZE/2);
            this.text("Freeze", 400, 695);
            textSize(SPRITESIZE);
            rect(400, 680, 80, 5);
            if (Gremlin.getFrozen()) {
                float rectWidth = 80*Gremlin.getFrozenTimer()/(5 * FPS * this.gremlins.size());
                fill(138, 207, 246);
                rect(400, 680, Math.round(rectWidth), 5);
                fill(255);
            }
        }
    }

    /**
     * Draws all brickwalls if not destroyed, otherwise sets sprites of destroyed brickwalls.
    */
    public void evaluateBrickwalls() {
        Iterator<Brickwall> brickItr = this.brickwalls.iterator();
        while (brickItr.hasNext()) {
            Brickwall brickwall = brickItr.next();
            if (!brickwall.checkDestroyed()) {
                brickwall.draw(this);
            } else {
                this.destroyedBrickwalls.add(brickwall);
                this.emptyspaces.add(new StationaryObject(brickwall.getX(), brickwall.getY()));
                brickItr.remove();
            }
        }

        for (Brickwall brickwall : this.destroyedBrickwalls) {
            if (!brickwall.checkDestroyedFully()) {
                brickwall.setDestroyedSprite(this);
                brickwall.draw(this);
            } else {
                brickwall = null;
            }
        }
    }

    /**
     * Ticks and draws all gremlins and spawns slimes according to enemy cooldown.
     * Changes gremlin sprites to blue if frozen.
    */
    public void evaluateGremlins() {
        for (Gremlin gremlin : this.gremlins) {
            gremlin.giveWizard(this.wizard);
            gremlin.tick();
            gremlin.draw(this);
            if (this.gremlinTimer != 0 && (int)(this.gremlinTimer % (this.enemyCooldown*FPS)) == 0 
                && !Gremlin.getFrozen()) {
                Slime slime = new Slime(gremlin.getX(), gremlin.getY(), this.stonewalls, 
                    this.brickwalls, gremlin.getDirection());
                slime.setSprite(this.loadImage("src/main/resources/gremlins/slime.png"));
                this.slimes.add(slime);
            }

            if (Gremlin.getFrozen()) {
                gremlin.setSprite(this.loadImage("src/main/resources/gremlins/gremlinFrozen.png"));
            } else {
                gremlin.setSprite(this.loadImage("src/main/resources/gremlins/gremlin.png"));
            }
        }
    }

    /**
     * Ticks and draws all slimes.
     * If they have collided, remove them from the slimes ArrayList.
    */
    public void evaluateSlimes() {
        Iterator<Slime> slimeItr = this.slimes.iterator();
        while (slimeItr.hasNext()) {
            Slime slime = slimeItr.next();
            if (!slime.getCollided()) {
                slime.tick();
                slime.draw(this);
            } else {
                slimeItr.remove();
                slime = null;
            }
        }
    }

    /**
     * Ticks and draws all fireballs.
     * If they have collided, remove them from the fireballs ArrayList.
    */
    public void evaluateFireballs() {
        Iterator<Fireball> fireItr = this.fireballs.iterator();
        while (fireItr.hasNext()) {
            Fireball fireball = fireItr.next();
            if (!fireball.getCollided()) {
                fireball.tick();
                fireball.draw(this);
            } else {
                fireItr.remove();
                fireball = null;
            }
        }
    }

    /**
     * Evalues what happens next when the wizard reaches the door.
     * If there are still levels left, player goes to the next level.
     * If there are no leveles left, player wins.
    */
    public void nextLevel() {
        if (this.levelIndex < this.numLevels-1) {
            if (this.wizard.getGoNextLevel()) {
                this.levelIndex++;
                this.setup();
            }
        } else if (this.wizard.getGoNextLevel()) {
            this.levelIndex++;
            textSize(SPRITESIZE*3);
            background(191, 153, 114);
            this.text("YOU WIN!", 200, 330);
        }
    }

    /**
     * Evaluates what happens next when the wizard loses a life.
     * If it still has lives, simply run setup again and minus a life.
     * If no lives are left, game over.
    */
    public void lostLife() {
        if (!this.wizard.getIsAlive() && this.lives > 0) {
            this.lives--;
            this.setup();
        }
        if (this.lives == 0) {
            textSize(SPRITESIZE*3);
            background(191, 153, 114);
            this.text("GAME OVER", 180, 330);
        }
    }


    /**
     * Draw all elements in the game by current frame. 
	 */
    public void draw() {
        this.drawLayout();

        for (StationaryObject portal : this.portals) {
            portal.draw(this);
        }

        for (StationaryObject stonewall : this.stonewalls) {
            stonewall.draw(this);
        }

        for (StationaryObject door : this.doors) {
            door.draw(this);
        }

        this.wizard.tick();
        this.wizard.draw(this);
        this.snowflake.tick();
        this.snowflake.draw(this);

        this.evaluateGremlins();
        this.evaluateSlimes();
        this.evaluateFireballs();
        this.evaluateBrickwalls();

        this.nextLevel();
        this.lostLife();

        this.wizardTimer++;
        this.gremlinTimer++;
    }

    public static void main(String[] args) {
        PApplet.main("gremlins.App");
    }
}