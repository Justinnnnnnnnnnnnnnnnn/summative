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
    
    /*
     * Constructor for the PlayerCharacter, child of Character
     * @param p PApplet
     * @param x starting x position of character
     * @param y starting y position of character
     * @param name name of the character
     * @param stats StatBlock of the character
     * @param imagePath path to the image
     */
    public PlayerCharacter(PApplet p, int x, int y, String name, StatBlock stats, String imagePath) {
        super(p, x, y, name, stats, imagePath);
        sketch = (MySketch) p;
    }
    
    /*
     * Setter method for the attack speed of the player
     * @param attackSpeed new attack speed in frame cooldown
     */
    public void setAttackSpeed(int attackSpeed) {
        if (attackSpeed > this.attackSpeed - 6) {
            this.attackSpeed = attackSpeed;
        } else {
            this.attackSpeed = 6;
        }
    }
    
    /*
     * Getter method for the attack speed of the player
     */
    public int getAttackSpeed() {
        return attackSpeed;
    }
    
    /*
     * Setter method for the damage of the player attacks
     * @param attackDamage new damage of the attack
     */
    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }
    
    /*
     * Increase the attack pierce by 1
     */
    public void incrementAttackPierce() {
        attackPierce += 1;
    }
    
    /*
     * Count down the remaining attack cooldown after an attack is made
     */
    public void decrementAttackCooldown() {
        if (attackCooldown > 0) {
            attackCooldown -= 1;
        }
    }
    
    /*
     * Make an attack if not on cooldown and set cooldown to attack speed
     */
    public void attack() {
        if (attackCooldown == 0) {
            attackCooldown = attackSpeed; // activate attack cooldown
            sketch.createProjectile(true, x + 50, y + 30, attackDamage, 3, attackPierce, "Arrow", "images/playerArrow.png");
        }
    }
}
