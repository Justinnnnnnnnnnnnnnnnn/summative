/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package canvas;
import java.util.ArrayList;
import processing.core.PApplet;
import processing.core.PImage;
/**
 *
 * @author 344813928
 */
public class Projectile {
    public int x, y;
    private int damage, velocity, pierce;
    private String name;
    private boolean team; // true = ally, false = enemy
    private ArrayList<Character> piercedTargets = new ArrayList<>(); // stores all pierced targets
    private PImage image;
    private PApplet app;
    
    /*
     * Constructor for the PlayerCharacter, child of Character
     * @param p PApplet
     * @param x starting x position of character
     * @param y starting y position of character
     * @param team whether the projectile is player or enemy's
     * @param damage damage of the projectile
     * @param velocity velocity speed of the projectile
     * @param pierce number of targets the projectile can damage
     * @param name name of the character
     * @param imagePath path to the image
     */
    public Projectile(PApplet p, int x, int y, boolean team, int damage, int velocity, int pierce, String name, String imagePath) {
        this.app = p;
        this.x = x;
        this.y = y;
        this.team = team;
        this.damage = damage;
        this.velocity = velocity;
        this.pierce = pierce;
        this.name = name;
        this.image = app.loadImage(imagePath);
        
        if (name.equals("Arrow")) {
            image.resize(100, 30);
        }
    }
    
    /*
     * Move projectile given the distance multiplied by velocity
     * @param dx distance moved on x axis
     */
    public void move(int dx) {
        x += dx * velocity;
    }
    
    /*
     * Getter method to return the team of the projectile
     */
    public boolean getTeam() {
        return team;
    }
    
    /*
     * Getter method to return the damage of the projectile
     */
    public int getDamage() {
        return damage;
    }
    
    /*
     * Getter method to return the pierce of the projectile
     */
    public int getPierce() {
        return pierce;
    }
    
    /*
     * Decreases the remaining number of targets the projectile can damage
     */
    public void decreasePierce() {
        pierce -= 1;
    }
    
    /*
     * Getter method to return the array of all targets previously pierced by this projectile
     */
    public ArrayList getPiercedTargets() {
        return piercedTargets;
    }
    
    /*
     * Draw the projectile
     */
    public void draw() {
        app.image(image, x, y);
    }
    
    /*
     * Check if this projectile and a character are touching using rectangular hitboxes
     * @param other the target character
     */
    public boolean isCollidingWith(Character other) {
        // check if bounding boxes intersect
        boolean isLeftOtherRight = x < other.x + other.getImage().width;
        boolean isRightOtherLeft = x + image.width > other.x;
        boolean isAboveOtherBottom = y < other.y + other.getImage().height;
        boolean isBelowOtherTop = y + image.height > other.y;
        
        // returns true if two characters are touching
        return isLeftOtherRight && isRightOtherLeft && isAboveOtherBottom && isBelowOtherTop;
    } // end isCollidingWith
    
    /*
     * Add the pierced target to the array storing all pierced targets so it isnt hit again
     */
    public void pierceTarget(Character target) {
        piercedTargets.add(target);
    }
}
