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
    private final int DIFFICULTY_SCALING = 150; // default is 300
    
    private ArrayList<Projectile> projectilesList = new ArrayList<>(); // store all projectiles
    
    
    @Override
    public void settings() {
        size(700, 400);
    }

    @Override
    public void setup() {
        background = loadImage("images/ScorchedBackground.png");
        Player = new PlayerCharacter(this, 50, 120, "Hou Yi", new StatBlock(5, 1), "images/houyi.png"); 
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
                text("Score: " + score, 20, 150);
                text("Highscore: " + highscore, 20, 200);
            }
            case 1 -> {
                // Combat
                scoreHandler();
                combatHandler();
                if (!(Player.alive)) { // end if player died
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
    
    public void scoreHandler() {
        if (stage == 1) { // active combat
            score++;

            if (score % DIFFICULTY_SCALING == 0) { // increase enemy spawnrate at higher score
                enemySpawnCooldown = enemySpawnCooldown * 0.9;
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
    
    public void combatHandler() {
        movePlayer();
        Player.draw();
        ((PlayerCharacter) Player).decrementAttackCooldown();
        
        collisionHandler();
        moveEnemy();
        spawnEnemy();
        moveProjectiles();
        
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
                    if (proj.getTeam() && proj.isCollidingWith(enemy)) { // check if friendly projectile and hits enemy
                        enemy.kill();
                        proj.decreasePierce();
                    }
                }
            }
        }
        
        if (IFramesCooldown > 0) { // decrement invincibility frames
            IFramesCooldown -= 1;
        }
    }
    
    public void resetCombat() {
        Player.getStats().setHealth(3); // reset health
        Player.revive();
        Player.x = 50; // reset position
        Player.y = 120;
        score = 0; // reset score
        enemySpawnTick = 0; // reset difficulty
        enemySpawnCooldown = DIFFICULTY_SCALING;
        enemiesList.clear(); // clean lists
        projectilesList.clear();
    }
    
    // PROJECTILE HANDLING
    
    public void moveProjectiles() {
        for (Projectile proj: projectilesList) { // move every projectile in launched direction
            if (proj.getTeam()) { // true is friendly projectile
                proj.move(1, 0);
            } else { // false is enemy projectile
                proj.move(-1, 0);
            }
            proj.draw(); // draw projectile
        }
        
        projectilesList.removeIf(p -> p.x > 710 || p.x < -100 || p.getPierce() < 0); // destroy projectile if off the map or out of pierce
    }
    
    public void createProjectile(boolean team, int x, int y, int damage, int velocity, int pierce, String name, String imagePath) {
        projectilesList.add(new Projectile(this, x, y, team, damage, velocity, pierce, name, imagePath));
    }
    
    // ENEMY HANDLING
    
    public void moveEnemy() {
        for (Character enemy: enemiesList) { // move every enemy forwards
            enemy.move(-1, 0);
            enemy.draw(); // draw enemy
        }
        
        enemiesList.removeIf(e -> e.x < -100 || !(e.alive)); // remove enemy if off the map or is dead
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
            resetCombat();
            stage = 1; // start combat
            
        } else if (stage == 1) {
            setMovement(keyCode, true); // add direction to movement
            
        } else if (stage == 2 && keyCode == ENTER) {
            scoreHandler(); // update scorelist
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
        if (mouseButton == LEFT && stage == 1)
            ((PlayerCharacter) Player).attack();
    }
}//end class
