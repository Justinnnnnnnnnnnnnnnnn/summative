/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package canvas;
import processing.core.PApplet;

/**
 *
 * @author 344813928
 */
public class PlayerCharacter extends Character {
    private int attackDamage = 1;
    private int attackPierce = 1;
    private int attackSpeed = 60;
    private int attackCooldown = 0;
    private static MySketch sketch;
    
    public PlayerCharacter(PApplet p, int x, int y, String name, StatBlock stats, String imagePath) {
        super(p, x, y, name, stats, imagePath);
        sketch = (MySketch) p;
    }
    
    public void setAttackSpeed(int attackSpeed) {
        this.attackSpeed = attackSpeed;
    }
    
    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }
    
    public void setAttackPierce(int attackPierce) {
        this.attackPierce = attackPierce;
    }
    
    public void decrementAttackCooldown() {
        if (attackCooldown > 0) {
            attackCooldown -= 1;
        }
    }
    
    public void attack() {
        if (attackCooldown == 0) {
            attackCooldown = attackSpeed; // activate attack cooldown
            sketch.createProjectile(true, x + 50, y + 30, attackDamage, 3, attackPierce, "Arrow", "images/playerArrow.png");
        }
    }
}
