/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package canvas;
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
    private PImage image;
    private PApplet app;
    
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
        
        if (name.equals("Fire Arrow")) {
            image.resize(100, 100);
        }
    }
    
    public void move(int dx, int dy) {
        x += dx * velocity;
        y += dy * velocity;
    }
    
    public boolean getTeam() {
        return team;
    }
    
    public int getDamage() {
        return damage;
    }
    
    public int getPierce() {
        return pierce;
    }
    
    public void decreasePierce() {
        pierce -= 1;
    }
    
    public boolean isCollidingWith(Character other) {
        // check if bounding boxes intersect
        boolean isLeftOtherRight = x < other.x + other.getImage().width;
        boolean isRightOtherLeft = x + image.width > other.x;
        boolean isAboveOtherBottom = y < other.y + other.getImage().height;
        boolean isBelowOtherTop = y + image.height > other.y;
        
        // returns true if two characters are touching
        return isLeftOtherRight && isRightOtherLeft && isAboveOtherBottom && isBelowOtherTop;
    } // end isCollidingWith
}
