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
  PImage imgLives; 
 
  int intAlienRows = 5;
  int intAliensPerRow = 8;
  float fltAlienSpacing = 35;

  int intNumLives = 3;

  float fltShipX = 175;
  float fltShipY = 350; 

  float fltShipSpeed = 3;


  float[][] fltAlienX = new float[intAlienRows][intAliensPerRow];
  float[][] fltAlienY = new float[intAlienRows][intAliensPerRow];

  boolean[][] blnAlienAlive = new boolean[intAlienRows][intAliensPerRow];

  // Boolean variables to track keyboard input
  boolean blnLeft = false;
  boolean blnRight = false;

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

    imgLives = loadImage("Lives.png");
    imgLives.resize(25, 25);

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

    drawLivesIndicator();

    drawShip();

    // Draw the aliens 
    for (int intRow = 0; intRow < intAlienRows; intRow++) {
      for (int intCol = 0; intCol < intAliensPerRow; intCol++) {
        if (blnAlienAlive[intRow][intCol]) {
          drawAlien(fltAlienX[intRow][intCol], fltAlienY[intRow][intCol]);
        }
      }
    }

    // Ship movement based on keyboard input
    if (blnLeft) {
        fltShipX -= fltShipSpeed;
    }
    if (blnRight) {
        fltShipX += fltShipSpeed;
    }


  }

  /**
   * Called when a key is pressed. Handles keyboard input for controlling the ship.
   */
  public void keyPressed() {
    // Control player movement with AD keys
    if (key == 'a') {
      blnLeft = true;
    } 
    else if (key == 'd') {
      blnRight = true;
    }
  }

  /**
   * Called when a key is released. Handles keyboard input for controlling the player.
   */
  public void keyReleased() {
    // Release player movement based on key release 
    if (key == 'a') {
      blnLeft = false;
    } 
    else if (key == 'd') {
      blnRight = false;
    }
  }

  /**
   * Draws a ship on the screen at the initialized coordinates.
   */
  public void drawShip() {
    image(imgShip, fltShipX, fltShipY);
    
    fltShipX = constrain(fltShipX, 0, width - 50 );
    fltShipY = constrain(fltShipY, 0, height - 50);
  }

  /**
   * Draws an alien on the screen at the initialized coordinates. 
   * @param fltAlienX The X-coordinate of the aliens. 
   * @param fltAlienY The Y-coordinate of the aliens. 
   */
  public void drawAlien(float fltAlienX, float fltAlienY) {
    image(imgAlien, fltAlienX, fltAlienY);
  }

  /**
   * Draws the player lives indicator on the screen. 
   */
  public void drawLivesIndicator() {
    // Display "LIVES" to the left of the lives
    fill(255); 
    textSize(20); 
    textAlign(RIGHT); 
    text("LIVES", width - 130, 30);

    for (int i = 0; i < intNumLives; i++) {
      image(imgLives, width - 40 * (i + 1), 10);
    }
  }
}
