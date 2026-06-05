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
    
    private Character Player; 
    private boolean up, down, left, right; // movement booleans
    private int IFramesCooldown = 0;
    private int I_FRAMES_LIMIT = 30; // 0.5 seconds of invincibility

    private ArrayList<Character> enemiesList = new ArrayList<>(); // stores all enemies
    private int enemySpawnCooldown = 180;
    private int enemySpawnTick = 0;
    
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
        
        if (stage == 0) {
            fill(255);
            text("My Cultural Story", 20, 50);
            text("Press enter to continue", 20, 100);
        } else if (stage == 1) {
            combatHandler();
        }
    }
    
    public void combatHandler() {
        movePlayer();
        Player.draw();
        collisionHandler();
        moveEnemy();
        spawnEnemy();
    }
    
    public void collisionHandler() {
        // check if enemies are colliding with player
        for (Character enemy: enemiesList) {
            if (Player.isCollidingWith(enemy)) {
                if (IFramesCooldown == 0) { // damage player if not invincibile
                    IFramesCooldown = 30; // activate invicibility frames
                    Player.damage(1);
                    enemy.damage(999); // destroy enemy after hitting player
                }
                break;
            }
        }
        
        if (IFramesCooldown > 0) {
            IFramesCooldown -= 1;
        }
    }
    
    public void moveEnemy() {
        for (Character enemy: enemiesList) { // move every enemy forwards
            enemy.move(-1, 0);
            enemy.draw();
        }
        
        enemiesList.removeIf(e -> e.x < 0); // remove enemy
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
            stage = 1;
        } else if (stage == 1) {
            setMovement(keyCode, true); // add direction to movement
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
