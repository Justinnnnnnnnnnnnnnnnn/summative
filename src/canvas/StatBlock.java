/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package canvas;

/**
 *
 * @author 344813928
 */
public class StatBlock {
    private int health;
    private int speed;
    
    public StatBlock(int health, int speed) {
        this.health = health;
        this.speed = speed;
    }
    
    public int getHealth() {
        return health;
    }
    
    public int getSpeed() {
        return speed;
    }
    
    public void setHealth(int health) {
        if (health >= 0) {
            this.health = health;
        }
    }
    
    public void setSpeed(int speed) {
        if (speed >= 0) {
            this.speed = speed;
        }
    }
}
