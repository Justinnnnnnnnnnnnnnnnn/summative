/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package canvas;
import processing.core.PApplet;
import processing.core.PImage;

public class MySketch extends PApplet {
    private Character Player; 
    private PImage background;
    private boolean up, down, left, right; // movement booleans
    private int stage = 0;

    @Override
    public void settings() {
        size(700, 400);
    }

    @Override
    public void setup() {
        background = loadImage("images/ScorchedBackground.png");
        Player = new PlayerCharacter(this, 50, 30, "Hou Yi", new StatBlock(100, 1), "images/houyi.png"); 
    }

    @Override
    public void draw() {
        image(background, 0, 0, width, height);
        
        if (stage == 0) {
            fill(255);
            text("My Cultural Story", 20, 50);
            text("Press enter to continue", 20, 100);
        } else if (stage == 1) {
            movePlayer();
            Player.draw();
        }
    }

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
