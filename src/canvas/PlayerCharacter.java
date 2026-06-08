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
    private int attackSpeed = 60;
    private int attackDamage = 1;
    private int attackPierce = 0;
    private MySketch sketch;
    
    public PlayerCharacter(PApplet p, int x, int y, String name, StatBlock stats, String imagePath) {
        super(p, x, y, name, stats, imagePath);
        this.sketch = (MySketch) p;
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
    
    public void attack() {
        // sketch.createProjectile(true, attackDamage, 90, attackPierce, "Arrow", "images/playerArrow.png");
    }
}
