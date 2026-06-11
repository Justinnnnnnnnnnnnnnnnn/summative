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
    
    /*
     * Constructor for the StatBlock
     * @param health number of health the character has
     * @param speed movement speed of the character
     */
    public StatBlock(int health, int speed) {
        this.health = health;
        this.speed = speed;
    }
    
    /*
     * Getter method for the health
    */
    public int getHealth() {
        return health;
    }
    
    /*
     * Getter method for the speed
    */
    public int getSpeed() {
        return speed;
    }
    
    /*
     * Setter method for updating health directly
     * @param health the new health value
    */
    public void setHealth(int health) {
        if (health >= 0) {
            this.health = health;
        }
    }
    
    /*
     * Setter method for updating speed directly
     * @param speed the new speed value
    */
    public void setSpeed(int speed) {
        if (speed >= 0) {
            this.speed = speed;
        }
    }
}
