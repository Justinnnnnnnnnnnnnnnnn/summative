/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package canvas;
import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;
import java.util.Random;

public class MySketch extends PApplet {
    private PImage background;
    private int stage = 0;
    private int score = 0;
    private int highscore = 0;
    
    private Character Player; 
    private boolean up, down, left, right; // movement booleans
    private int IFramesCooldown = 0;
    private final int I_FRAMES_LIMIT = 30; // 0.5 seconds of invincibility

    private ArrayList<Character> enemiesList = new ArrayList<>(); // stores all enemies
    private double enemySpawnCooldown = 180;
    private int enemySpawnTick = 0;
    
    private ArrayList<Projectile> projectilesList = new ArrayList<>(); // store all projectiles
    
    
    @Override
    public void settings() {
        size(700, 400);
    }

    @Override
    public void setup() {
        background = loadImage("images/ScorchedBackground.png");
        Player = new PlayerCharacter(this, 50, 30, "Hou Yi", new StatBlock(5, 1), "images/houyi.png"); 
    }

    @Override
    public void draw() {
        image(background, 0, 0, width, height);
        
        switch (stage) {
            case 0 -> {
                // Main Menu
                fill(255);
                text("My Cultural Story", 20, 50);
                text("Press enter to begin", 20, 100);
            }
            case 1 -> {
                // Combat
                scoreHandler();
                combatHandler();
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
    
    public void scoreHandler() {
        score++;
        if (score > highscore) { // track highscore
            highscore = score;
        }

        if (score % 300 == 0) { // increase enemy spawnrate at higher score
            enemySpawnCooldown = enemySpawnCooldown * 0.9;
        }   if (!(Player.alive)) { // end if player died
            stage = 2;
        }
        
        fill(255); // display current score in top left of screen
        text("Score: " + score, 20, 30);
    }
    
    public void combatHandler() {
        movePlayer();
        Player.draw();
        collisionHandler();
        moveEnemy();
        spawnEnemy();
        
        fill(255); // display health in bottom left of screen
        text("Health: " + Player.getStats().getHealth(), 20, 380);
    }
    
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
                    if (proj.getTeam()) { // check if ally projectile
                        if (proj.isCollidingWith(enemy)) { // check if projectile hit enemy
                            enemy.kill();
                            proj.decreasePierce();
                        }
                    }
                }
            }
        }
        
        if (IFramesCooldown > 0) { // decrement invincibility frames
            IFramesCooldown -= 1;
        }
    }
    
    // PROJECTILE HANDLING
    
    public void moveProjectiles() {
        for (Projectile proj: projectilesList) { // move every projectile in launched direction
            if (proj.getTeam()) { // true is friendly projectile
                proj.move(1, 0);
            } else if (!(proj.getTeam())) { // false is enemy projectile
                proj.move(-1, 0);
            }
        }
        
        projectilesList.removeIf(p -> p.getPierce() < 0); // destroy projectile if out of pierce
    }
    
    public void createProjectile(boolean team, int damage, int velocity, int pierce, String name, String imagePath) {
        if (team) { // player projectile
            projectilesList.add(new Projectile(this, Player.x + 60, Player.y + 30, team, damage, velocity, pierce, name, imagePath));
        } else if (!(team)) { // enemy projectile
            projectilesList.add(new Projectile(this, 700, 200, team, damage, velocity, pierce, name, imagePath));
        }
    }
    
    // ENEMY HANDLING
    
    public void moveEnemy() {
        for (Character enemy: enemiesList) { // move every enemy forwards
            enemy.move(-1, 0);
            enemy.draw();
        }
        
        enemiesList.removeIf(e -> e.x < -10 || !(e.alive)); // remove enemy if off the map or is dead
    }
    
    public void spawnEnemy() {
        enemySpawnTick++; // progress time until next enemy spawn
        if (enemySpawnTick >= enemySpawnCooldown) {
            enemySpawnTick = 0; // reset counter back to 0
            
            int spawnY = new Random().nextInt(300) + 10; // spawn enemy at random position
            enemiesList.add(new Character(this, 700, spawnY, "Sun", new StatBlock(1, 1), "images/Enemy_Sun.png"));
        }
    }

    // PLAYER CONTROL HANDLING
    
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
    
    @Override
    public void keyPressed() {
        if (stage == 0 && keyCode == ENTER) {
            stage = 1; // start combat
            
        } else if (stage == 1) {
            setMovement(keyCode, true); // add direction to movement
            
        } else if (stage == 2 && keyCode == ENTER) {
            stage = 0; // return to menu on defeat
        }
    }
    
    @Override
    public void keyReleased() {
        if (stage == 1) {
            setMovement(keyCode, false); // remove direction from movement
        }
    }
    
    @Override
    public void mouseClicked() {
        if (mouseButton == LEFT)
            ((PlayerCharacter) Player).attack();
    }
}//end class
