/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package canvas;
import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;
import java.util.Random;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.File;
import java.util.Scanner;

public class MySketch extends PApplet {
    private PImage background;
    private Button resetDataButton;
    private int stage = 0;
    private int score = 0;
    private int highscore = 0;
    private boolean combatActive = false;
    
    private Character Player; 
    private boolean up, down, left, right; // movement booleans
    private boolean lmbHeld = false; // attacking boolean
    private int IFramesCooldown = 0;
    private final int I_FRAMES_LIMIT = 30; // 0.5 seconds of invincibility
    private int upgradeChoice = 0;

    private ArrayList<Character> enemiesList = new ArrayList<>(); // stores all enemies
    private double enemySpawnCooldown = 180;
    private int enemySpawnTick = 0;
    private final int DIFFICULTY_SCALING = 300; // default is 300
    
    private ArrayList<Projectile> projectilesList = new ArrayList<>(); // store all projectiles
    
    
    @Override
    public void settings() {
        size(700, 400);
    }

    @Override
    public void setup() {
        background = loadImage("images/ScorchedBackground.png");
        resetDataButton = new Button(this, 580, 270, "images/change.png");
        Player = new PlayerCharacter(this, 50, 120, "Hou Yi", new StatBlock(3, 1), "images/houyi.png"); 
    }
    
    /*
     * Writes text information on main menu, calls combat and score handlers during combat, and shows text information on defeat
     */
    @Override
    public void draw() {
        image(background, 0, 0, width, height);
        
        switch (stage) {
            case 0 -> {
                // Main Menu
                fill(255);
                text("Hou Yi and Chang'e", 20, 50);
                text("Press enter to begin", 20, 100);
                text("Score: " + score, 20, 150);
                text("Highscore: " + highscore, 20, 200);
                resetDataButton.draw();
                text("Reset Data", 605, 260);
            }
            case 1 -> {
                // Combat
                scoreHandler();
                combatHandler();
                
                if (score % 600 == 0) {
                    upgradePlayer();
                }
                if (!(Player.alive)) { // end if player died
                    combatActive = false;
                    stage = 2;
                }
            }
            case 2 -> {
                // Defeat Screen
                fill(255);
                text("DEFEAT", 20, 50);
                text("Press enter to return to menu", 20, 100);
            }
            default -> {
                System.err.println("Can't load stage: " + stage);
            }
        }
    }
    
    /*
     * Handles incrementing the score, updating difficulty, displaying score, writing and reading to ScoreList text file
     */
    public void scoreHandler() {
        if (stage == 1) { // combat
            if (combatActive) { // check if in combat
                score++;

                if (score % DIFFICULTY_SCALING == 0) { // increase enemy spawnrate at higher score
                    enemySpawnCooldown = enemySpawnCooldown * 0.9;
                }
            }
            
            fill(255); // display current score in top left of screen
            text("Score: " + score, 20, 30);
        } 
        else if (stage == 2) { // after defeat
            
            try { // store score to text file
                PrintWriter output = new PrintWriter( new FileWriter( "src/ScoreList.txt", true) );
                output.println(score + "");
                output.close();
            } catch ( IOException ioException ) {
                System.err.println( "Java Exception: " + ioException );
            }
            
            try { // read to find highscore
                Scanner fileInput = new Scanner( new File("src/ScoreList.txt") );
                while (fileInput.hasNext()){
                    int sc = fileInput.nextInt();
                    if (sc > highscore) {
                        highscore = sc;
                    }
                }
                fileInput.close();
            } catch ( IOException ioException ) {
                System.err.println( "Java Exception: " + ioException);
            }
        }
    }
    
    /*
     * Handles and calls the methods for player movement, player interaction, collisions, enemy handling, and projectile handling, and dispalying player health
     */
    public void combatHandler() {
        Player.draw();
        
        if (combatActive) { // only handle while in combat
            movePlayer();
            ((PlayerCharacter) Player).decrementAttackCooldown(); // cooldown player attack
            if (lmbHeld) {
                ((PlayerCharacter)Player).attack();
            }
            
            collisionHandler();
            spawnEnemy();
        }
        moveEnemy();
        moveProjectiles();
        
        fill(255); // display health in bottom left of screen
        text("Health: " + Player.getStats().getHealth(), 20, 380);
    }
    
    /*
     * Handles enemy collisions with the player and the player's projectiles, player invincibility frames
     */
    public void collisionHandler() {
        // check if enemies are colliding with player
        for (Character enemy: enemiesList) {
            if (enemy.isCollidingWith(Player)) {
                if (IFramesCooldown == 0) { // damage player if not invincibile
                    IFramesCooldown = I_FRAMES_LIMIT; // activate invicibility frames
                    Player.damage(1);
                }
                enemy.kill(); // destroy enemy after colliding with player, regardless of damage dealt
                break;
            } else {
                for (Projectile proj: projectilesList) {
                    if (proj.getTeam() && proj.isCollidingWith(enemy)) { // check if friendly projectile and hits enemy
                        if (!(proj.getPiercedTargets().contains(enemy))) {
                            enemy.damage(proj.getDamage());
                            proj.decreasePierce();
                            proj.pierceTarget(enemy); // target wont get hit again by same projectile
                        }
                    }
                }
            }
        }
        
        if (IFramesCooldown > 0) { // decrement invincibility frames
            IFramesCooldown -= 1;
        }
    }
    
    /*
     * Clears the ScoreList text file and sets scores back to 0
     */
    public void resetScore() {
        try (PrintWriter w = new PrintWriter("src/ScoreList.txt")) {
            // clear score text file
        } catch ( IOException ioException ) {
                System.err.println( "Java Exception: " + ioException);
        }
        score = 0; // reset scores and highscore
        highscore = 0;
    }
    
    /*
     * Resets player, enemy, projectile, and score to starting values
     */
    public void resetCombat() {
        Player.getStats().setHealth(3); // reset health
        Player.revive();
        Player.x = 50; // reset position
        Player.y = 120;
        setMovement(LEFT, false); // reset movement to idle
        setMovement(RIGHT, false);
        setMovement(UP, false);
        setMovement(DOWN, false);
        score = 0; // reset score
        enemySpawnTick = 0; // reset difficulty
        enemySpawnCooldown = DIFFICULTY_SCALING;
        enemiesList.clear(); // clean lists
        projectilesList.clear();
    }
    
    public void upgradePlayer() {
        combatActive = false;
        
        fill(255); // display upgrade options
        text("Press number key to select upgrade", 200, 100);
        text("(1) Terminal Velocity: +100% movement speed", 200, 120);
        text("(2) Quickdraw: -10% attack cooldown", 200, 140);
        text("(3) Sharp Arrows: +1 pierce", 200, 160);
        
        switch (upgradeChoice) {
            case 1 -> { // +100% movement speed
                Player.getStats().setSpeed(Player.getStats().getSpeed() + 1);
                combatActive = true;
                upgradeChoice = 0; // reset upgrade choice
            }
            case 2 -> { // -10% frames attack cooldown
                int attackSpeed = ((PlayerCharacter)Player).getAttackSpeed();
                ((PlayerCharacter)Player).setAttackSpeed(round((float) (attackSpeed * 0.9)));
                combatActive = true;
                upgradeChoice = 0; // reset upgrade choice
            }
            case 3 -> { // +1 pierce
                ((PlayerCharacter)Player).incrementAttackPierce();
                combatActive = true;
                upgradeChoice = 0; // reset upgrade choice
            }
        }
    }
    
    // PROJECTILE HANDLING
    
    /*
     * Moves all player projectiles to the right and enemy projectiles to the left
     */
    public void moveProjectiles() {
        for (Projectile proj: projectilesList) { // move every projectile in launched direction
            if (combatActive) { // check if combat is active
                if (proj.getTeam()) { // true is friendly projectile
                    proj.move(1);
                } else { // false is enemy projectile
                    proj.move(-1);
                }
            }
            proj.draw(); // draw projectile
        }
        
        projectilesList.removeIf(p -> p.x > 710 || p.x < -100 || p.getPierce() < 0); // destroy projectile if off the map or out of pierce
    }
    
    /*
     * Creates a new projectile when a player or enemy fires one
     * @param team whether it is the player or enemy's projectile
     * @param x the x position of the projectile
     * @param y the y position of the projectile
     * @param damage the damage dealt by the projectile
     * @param velocity the velocity the projectile travels
     * @param pierce the number of targets the projectile can damage
     * @param name the name of the projectile
     * @param imagePath the path to find the image
     */
    public void createProjectile(boolean team, int x, int y, int damage, int velocity, int pierce, String name, String imagePath) {
        projectilesList.add(new Projectile(this, x, y, team, damage, velocity, pierce, name, imagePath));
    }
    
    // ENEMY HANDLING
    
    /*
     * Moves all enemies to the left and removs enemies outside the arena or that are dead
     */
    public void moveEnemy() {
        for (Character enemy: enemiesList) { // move every enemy forwards
            if (combatActive) { // check if in combat
                enemy.move(-1, 0);
            }
            enemy.draw(); // draw enemy
        }
        
        enemiesList.removeIf(e -> e.x < -100 || !(e.alive)); // remove enemy if off the map or is dead
    }
    
    /*
     * Counts enemy spawning and spawns a random enemy type at a random Y position on the right of the screen
     */
    public void spawnEnemy() {
        enemySpawnTick++; // progress time until next enemy spawn
        if (enemySpawnTick >= enemySpawnCooldown) {
            enemySpawnTick = 0; // reset counter back to 0
            
            int spawnY = new Random().nextInt(300) + 10; // spawn enemy at random position
            int type = new Random().nextInt(4); // select one of 3 enemy types
            switch (type) {
                case 0 -> { // basic enemy
                    enemiesList.add(new Character(this, 700, spawnY, "Sun", new StatBlock(1, 1), "images/Enemy_Sun.png"));
                }
                case 1 -> { // fast enemy
                    enemiesList.add(new Character(this, 700, spawnY, "MiniSun", new StatBlock(1, 2), "images/Enemy_Sun.png"));
                }
                case 2 -> { // tanky enemy
                    enemiesList.add(new Character(this, 700, spawnY, "MegaSun", new StatBlock(2, 1), "images/Enemy_Sun.png"));
                }
                case 3 -> { // super tanky enemy
                    if (score > 1500) {
                        enemiesList.add(new Character(this, 700, spawnY, "SuperMegaSun", new StatBlock(5, 1), "images/Enemy_Sun.png"));
                    } else { // otherwise spawn a basic enemy if score is too low
                        enemiesList.add(new Character(this, 700, spawnY, "Sun", new StatBlock(1, 1), "images/Enemy_Sun.png"));
                    }
                }
                case 4 -> { // super ultra mega enemy
                    if (score > 3000) {
                        enemiesList.add(new Character(this, 700, spawnY, "VolatileSun", new StatBlock(8, 3), "images/Enemy_Sun.png"));
                    } else { // otherwise spawn a basic enemy if score is too low
                        enemiesList.add(new Character(this, 700, spawnY, "Sun", new StatBlock(1, 1), "images/Enemy_Sun.png"));
                    }
                }
            }
        }
    }

    // PLAYER CONTROL HANDLING
    
    /*
     * Moves the player based off the direction keys being held down
     */
    public void movePlayer() {
        int dx = 0, dy = 0;
        // check all directions player can move in
        if (up && left) {
            dx = -3;
            dy = -3;
        } else if (up && right) {
            dx = 3;
            dy = -3;
        } else if (down && left) {
            dx = -3;
            dy =  3;
        } else if (down && right) {
            dx = 3;
            dy = 3;
        } else if (up) {
            dy = -5;
        } else if (down) {
            dy = 5;
        } else if (left) {
            dx = -5;
        } else if (right) {
            dx = 5;
        }
        
        Player.move(dx, dy);
    }
    
    /*
     * Sets the movement of the player based off direction keys held
     * @param key the key being held
     * @param state whether it is held down or not
     */
    public void setMovement(int key, boolean state) {
        if (key == LEFT)
            left = state;
        if (key == RIGHT)
            right = state;
        if (key == UP)
            up = state;
        if (key == DOWN)
            down = state;
    }
    
    /*
     * Starts combat, sets player movement, and returns to menu when specific keys are pressed in each stage
     */
    @Override
    public void keyPressed() {
        if (stage == 0 && keyCode == ENTER) {
            resetCombat();
            combatActive = true;
            stage = 1; // start combat
            
        } else if (stage == 1) {
            setMovement(keyCode, true); // add direction to movement
            
            if (!(combatActive)) {
                if (keyCode == '1') {
                    upgradeChoice = 1;
                } else if (keyCode == '2') {
                    upgradeChoice = 2;
                } else if (keyCode == '3') {
                    upgradeChoice = 3;
                }
            }
            
        } else if (stage == 2 && keyCode == ENTER) {
            scoreHandler(); // update scorelist
            stage = 0; // return to menu on defeat
        }
    }
    
    /*
     * Removes movement direction from movement when key is unpressed
     */
    @Override
    public void keyReleased() {
        if (stage == 1) {
            setMovement(keyCode, false); // remove direction from movement
        }
    }
    
    /*
     * Enables automatic firing when lmb is held down
     */
    @Override
    public void mousePressed() {
        if (mouseButton == LEFT && stage == 1) {
            lmbHeld = true;
        }
    }
    
    /*
     * Disables automatic firing when lmb is released
     */
    @Override
    public void mouseReleased() {
        if (mouseButton == LEFT && stage == 1) {
            lmbHeld = false;
        }
    }
    
    /*
     * Clears ScoreList text file if the user clicks on the reset data button
     */
    @Override
    public void mouseClicked() {
        if (stage == 0 && resetDataButton.isClicked(mouseX, mouseY))
            resetScore();
    }
}//end class
