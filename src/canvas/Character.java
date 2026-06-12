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
    public boolean alive = true;
    private String name; // name of the character
    private StatBlock stats; // health and speed of character
    private PImage image;
    private PApplet app;
    
    /*
     * Constructor for the character
     * @param p PApplet
     * @param x starting x position of character
     * @param y starting y position of character
     * @param name name of the character
     * @param stats StatBlock of the character
     * @param imagePath path to the image
     */
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
        } else if (name.equals("MiniSun")) {
            image.resize(50, 50);
        } else if (name.equals("MegaSun")) {
            image.resize(100, 100);
        } else if (name.equals("SuperMegaSun")) {
            image.resize(160, 160);
        } else if (name.equals("VolatileSun")) {
            image.resize(200, 200);
        } 
    }
    
    /*
     * Move character given a direction and multiplied by speed, restrain main character inside arena
     * @param dx distance moved on x axis
     * @param dy distance moved on y axis
     */
    public void move(int dx, int dy) {
        x += dx * stats.getSpeed();
        y += dy * stats.getSpeed();
        
        if (name.equals("Hou Yi")) { // keep player character inside of screen
            x = constrain(x, 10, 590);
            y = constrain(y, 10, 290);
        }
    }
    
    /*
     * Deal damage to the enemy and kill if below critical health
     * @param amount amount of damage taken
     */
    public void damage(int amount) {
        stats.setHealth(stats.getHealth() - amount);
        if (stats.getHealth() < 1) // kill character when health goes below critical
            kill();
    }
    
    /*
     * Kill the character so other parts of code know to delete it
     */
    public void kill() {
        alive = false;
    }
    
    /*
     * Revive the character, usually used for the PlayerCharacter when starting a new round
     */
    public void revive() {
        alive = true;
    }
    
    /*
     * Draw the character
     */
    public void draw() {
        app.image(image, x, y);
    }
    
    /*
     * Check if this and another character are touching using rectangular hitboxes
     * @param other the other character
     */
    public boolean isCollidingWith(Character other) {
        // check if bounding boxes intersect
        boolean isLeftOtherRight = x < other.x + other.image.width;
        boolean isRightOtherLeft = x + image.width > other.x;
        boolean isAboveOtherBottom = y < other.y + other.image.height;
        boolean isBelowOtherTop = y + image.height > other.y;
        
        // returns true if two characters are touching
        return isLeftOtherRight && isRightOtherLeft && isAboveOtherBottom && isBelowOtherTop;
    } // end isCollidingWith
    
    /*
     * Getter method to return the StatBlock of the character
     */
    public StatBlock getStats() {
        return stats;
    }
    
    /*
     * Getter method to return the image of the character
     */
    public PImage getImage() {
        return image;
    }
}