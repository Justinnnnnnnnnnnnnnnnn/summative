/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package canvas;
import processing.core.PApplet;
import processing.core.PImage;

public class Button {
    private PApplet p;
    private float x, y;
    private PImage img;

    /*
     * Constructor for the button
     * @param p PApplet
     * @param x starting x position of button
     * @param y starting y position of button
     * @param imagePath path to the image
     */
    public Button(PApplet p, float x, float y, String imagePath) {
        this.p = p;
        this.x = x;
        this.y = y;
        this.img = p.loadImage(imagePath); // Load the PNG

        img.resize(100,100); // resize the button
    }
    
    /*
     * Draw the button
     */
    public void draw() {
        p.image(img, x, y);
    }

    
    /*
     * Collision detection based of the dimensions of the target image
     * @param mx the x position of the cursor
     * @param my the y position of the cursor
     */
    public boolean isClicked(float mx, float my) {
        return (mx >= x && mx <= x + img.width && my >= y && my <= y + img.height);
    }
}
