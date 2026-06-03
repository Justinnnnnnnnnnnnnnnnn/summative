/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package canvas;
import processing.core.PApplet;

public class MySketch extends PApplet {
    private Character Player; 
    int stage = 0;

    public void settings() {
        size(400, 400);
    }

    public void setup() {
        background(255);
        Player = new PlayerCharacter(this, 50, 30, "Hou Yi", new StatBlock(100, 1), "images/houyi.png"); 
    }

    public void draw() {
        background(255);
        if (stage == 0) {
            fill(0);
            text("My Cultural Story", 20, 50);
            text("Press enter to continue", 20, 100);
        } else if (stage == 1) {
            if (keyPressed) { // player movement
                if (keyCode == LEFT) {
                  Player.move(-5, 0);
                } else if (keyCode == RIGHT) {
                  Player.move(5, 0);
                } else if (keyCode == UP) {
                  Player.move(0, -5);
                } else if (keyCode == DOWN) {
                  Player.move(0, 5);
                }
            }
            Player.draw();
        }
    }

    public void keyPressed() {
        if (stage == 0 && keyCode == ENTER) {
            stage = 1;
        }
    }
}//end class
