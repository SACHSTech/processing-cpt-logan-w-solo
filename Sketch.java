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
  PImage imgMeteor; 
  PImage imgStartScreen;
 
  int intAlienRows = 4;
  int intAliensPerRow = 8;
  float fltAlienSpacing = 35;
  float fltAlienMoveDistance = 1;
  boolean blnAlienDirection = true; 

  int intNumLives = 3;
  int intScore = 0; 

  float fltShipX = 175;
  float fltShipY = 350; 

  float fltShipSpeed = 4;

  float[][] fltAlienX = new float[intAlienRows][intAliensPerRow];
  float[][] fltAlienY = new float[intAlienRows][intAliensPerRow];

  boolean[][] blnAlienAlive = new boolean[intAlienRows][intAliensPerRow];

  boolean blnGameStart = false; 

  // Boolean variables to track keyboard input
  boolean blnLeft = false;
  boolean blnRight = false;
  boolean blnStart = true; 

  boolean blnShoot = false;
  float fltBulletX;
  float fltBulletY;

  /**
   * Called once at the beginning of execution, size call in this method
   */
  public void settings() {
    size(500, 400);
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

    imgMeteor = loadImage("meteor.png");
    imgMeteor.resize(60, 50); 

    imgStartScreen = loadImage("startScreen.png");
    imgStartScreen.resize(500, 410); 

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
    if(blnStart == true){
      startScreen(0, -10);
    }
    else{
      image(imgBackground, 0, 0);
  
    drawLivesIndicator();
    drawShip(fltShipX, fltShipY);
    drawMeteor();
    drawScore();
    moveAliens();

      for (int intRow = 0; intRow < intAlienRows; intRow++) {
        for (int intCol = 0; intCol < intAliensPerRow; intCol++) {
          if (blnAlienAlive[intRow][intCol]) {
            drawAlien(fltAlienX[intRow][intCol], fltAlienY[intRow][intCol]);

            // Check for collision with bullet
            if (blnShoot && fltBulletY <= fltAlienY[intRow][intCol] + 35 && fltBulletY >= fltAlienY[intRow][intCol] &&
              fltBulletX <= fltAlienX[intRow][intCol] + 35 && fltBulletX >= fltAlienX[intRow][intCol]) {
              blnAlienAlive[intRow][intCol] = false; 
              blnShoot = false; 
              intScore += 20;
            }
          }
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
    if (blnShoot){
      drawBullet(fltBulletX, fltBulletY);
      fltBulletY -= 10;
      if (fltBulletY == 320 && fltBulletX >= 25 && fltBulletX <=75 || fltBulletY == 320 && fltBulletX >= 125 && fltBulletX <= 175 ||
      fltBulletY == 320 && fltBulletX >= 225 && fltBulletX <=275 || fltBulletY == 320 && fltBulletX >= 325 && fltBulletX <=375) {
        blnShoot = false; 
      }
    }
  }
  
  /**
   * Horizontally moves the aliens (right to left) based on their current direction.
   * If the aliens move near the edge of the screen, the direction changes.  
   */
  public void moveAliens() {
    for (int intRow = 0; intRow < intAlienRows; intRow++){
      for (int intCol = 0; intCol < intAliensPerRow; intCol++) {
        if (blnAlienDirection){
          fltAlienX[intRow][intCol] += fltAlienMoveDistance; // Move right
        } 
        else {
          fltAlienX[intRow][intCol] -= fltAlienMoveDistance; // Move left
        }
      }
    }
    alienBoundaries();
  }
  
  /**
   * Checks if the aliens reached the horizontal boundaries of the screen.
   * If an alien goes near the right or left edge, it changes the direction. 
   */
  public void alienBoundaries() {
    for (int intRow = 0; intRow < intAlienRows; intRow++) {
      for (int intCol = 0; intCol < intAliensPerRow; intCol++) {
        if (fltAlienX[intRow][intCol] >= 450 - fltAlienMoveDistance || fltAlienX[intRow][intCol] <= fltAlienMoveDistance) {
          blnAlienDirection = !blnAlienDirection; // Change direction
          return; 
        }
      }
    }
  }

  /**
   * Called when a key is pressed. Handles keyboard input for controlling the ship.
   */
  public void keyPressed() {
    // Control player movement with AD keys
    if(key == 'a') {
      blnLeft = true;
    } 
    else if(key == 'd') {
      blnRight = true;
    }

    if(key == ' '){ 
    if(!blnGameStart) {
      blnGameStart = true;
    } 
    else{
      blnShoot = true; 
      fltBulletX = fltShipX + 22;
      fltBulletY = fltShipY;
    }
    }

      if (key == ' '){
        blnStart = false; 
      }
  }

  /**
   * Called when a key is released. Handles keyboard input for controlling the player.
   */
  public void keyReleased() {
    // Release player movement based on key release 
    if(key == 'a') {
      blnLeft = false;
    } 
    else if(key == 'd') {
      blnRight = false;
    }
  }

  /**
   * Draws a ship on the screen at the initialized coordinates.
   * @param fltShipX the X value of the ship.
   * @param fltShipY the Y value of the ship. 
   */
  public void drawShip(float fltShipX, float fltShipY) {
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
   * Calculations for drawing the player lives indicator on the screen. 
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

  /**
   * Calculations for drawing the score indicator on the screen. 
   */
  public void drawScore() {
    // Display "SCORE" to the left of the lives
    fill(255); 
    textSize(20); 
    textAlign(LEFT); 
    text("SCORE", width - 480, 30);
    
    fill(255);
    textSize(20);
    text(Integer.toString(intScore), width - 390, 30);
  }

  /**
   * Calculations for drawing the meteors to the screen. 
   */
  public void drawMeteor() {
    for(int i = 25; i <= width; i += 100){
      image(imgMeteor, i, 275); 
    }
  }

  /**
   * Calculations for drawing the bullets to the screen. 
   * @param fltBulletX the X value of the bullet.
   * @param fltBulletY the Y value of the bullet.
   */
  public void drawBullet(float fltBulletX, float fltBulletY) {
    noStroke();
    fill(255); 
    rect(fltBulletX, fltBulletY, 3, 20);
  }

  /**
   * Draws the start screen. 
   * @param fltScreenX the X value of the start screen.
   * @param fltScreenY the Y value of the start screen. 
   */
  public void startScreen(float fltScreenX, float fltScreenY){
    image(imgStartScreen, fltScreenX, fltScreenY);

  }

}
