/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package canvas;
import processing.core.PApplet;
import static processing.core.PApplet.constrain;
import processing.core.PImage;
/**
 *
 * @author 344813928
 */
public class Character {
    public int x, y;
    private String name; // name of the person
    private StatBlock stats; // age of the person
    private PImage image;
    private PApplet app;
    
    public Character(PApplet p, int x, int y, String name, StatBlock stats, String imagePath) {
        this.app = p;
        this.x = x;
        this.y = y;
        this.name = name;
        this.stats = stats;
        this.image = app.loadImage(imagePath);
        
        if (name.equals("Hou Yi")) {
            image.resize(100, 100);
        } else if (name.equals("Sun")) {
            image.resize(70, 70);
        }
    }
    
    public void move(int dx, int dy) {
        x += dx * stats.getSpeed();
        y += dy * stats.getSpeed();
        
        if (name.equals("Hou Yi")) { // keep player character inside of screen
            x = constrain(x, 10, 590);
            y = constrain(y, 10, 290);
        }
    }
    
    public void damage(int amount) {
        stats.setHealth(stats.getHealth() - amount);
        System.out.println("Health: " + stats.getHealth());
    }
    
    public void draw() {
        app.image(image, x, y);
    }
    
    public boolean isCollidingWith(Character other) {
        // check if bounding boxes intersect
        boolean isLeftOtherRight = x < other.x + other.image.width;
        boolean isRightOtherLeft = x + image.width > other.x;
        boolean isAboveOtherBottom = y < other.y + other.image.height;
        boolean isBelowOtherTop = y + image.height > other.y;
        
        // returns true if two characters are touching
        return isLeftOtherRight && isRightOtherLeft && isAboveOtherBottom && isBelowOtherTop;
    } // end isCollidingWith
}