import processing.core.PApplet;
import processing.core.PImage;

/**
 * ...
 * @author: L. Wong
 */
public class Sketch extends PApplet {
	
  PImage imgAlien; 
  PImage imgBackground; 
  PImage imgShip; 
 
  int intAlienRows = 5;
  int intAliensPerRow = 8;
  float fltAlienSpacing = 35;

  float[][] fltAlienX = new float[intAlienRows][intAliensPerRow];
  float[][] fltAlienY = new float[intAlienRows][intAliensPerRow];

  boolean[][] blnAlienAlive = new boolean[intAlienRows][intAliensPerRow];

  /**
   * Called once at the beginning of execution, size call in this method
   */
  public void settings() {
    size(400, 400);
  }

  /** 
   * Called once at the beginning of execution.  Add initial set up
   * values  i.e background, stroke, fill etc.
   */
  public void setup() {

    imgAlien = loadImage("Space Invaders Alien.png");
    imgAlien.resize(35, 35);

    imgShip = loadImage("Ship.png"); 
    imgShip.resize(50, 50);

    imgBackground = loadImage("background.png");
    imgBackground.resize(700, 400);

     // Initialize alien positions in rows
    for (int intRow = 0; intRow < intAlienRows; intRow++) {
      for (int intCol = 0; intCol < intAliensPerRow; intCol++) {
        fltAlienX[intRow][intCol] = intCol * fltAlienSpacing + 60;
        fltAlienY[intRow][intCol] = intRow * fltAlienSpacing + 50;
        blnAlienAlive[intRow][intCol] = true;
      }
    }
  }
    
  /**
   * Called repeatedly, anything drawn to the screen goes here
   */
  public void draw() {
	  
    image(imgBackground, 0, 0);

    drawShip(175, 300);

    for (int intRow = 0; intRow < intAlienRows; intRow++) {
      for (int intCol = 0; intCol < intAliensPerRow; intCol++) {
        if (blnAlienAlive[intRow][intCol]) {
          drawAlien(fltAlienX[intRow][intCol], fltAlienY[intRow][intCol]);
        }
      }
    }
  }

  /**
   * Draws a ship on the screen at the initialized coordinates.
   * @param fltShipX The X-coordinate of the ship.
   * @param fltShipY The Y-coordinate of the ship.
   */
  public void drawShip(float fltShipX, float fltShipY) {
    image(imgShip, fltShipX, fltShipY);
  }

  /**
   * Draws an alien on the screen at the initialized coordinates. 
   * @param fltAlienX The X-coordinate of the aliens. 
   * @param fltAlienY The Y-coordinate of the aliens. 
   */
  public void drawAlien(float fltAlienX, float fltAlienY) {
    image(imgAlien, fltAlienX, fltAlienY);
  }

}
