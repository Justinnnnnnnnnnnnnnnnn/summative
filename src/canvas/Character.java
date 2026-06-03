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
    }
    
    public void move(int dx, int dy) {
        x += dx;
        y += dy;
    }
    
    public void damage(int amount) {
        stats.setHealth(stats.getHealth() - amount);
    }
    
    public void draw() {
        app.image(image, x, y);
    }
}