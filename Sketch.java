import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * The game includes a player-controlled spaceship, aliens, bullets, meteors, and different screens for
 * start, win, and game over scenarios. Players can move the spaceship, shoot bullets, and try to defeat
 * the aliens to win the game. The game features multiple lives, a scoring system, and alien movements.
 * @author: L. Wong
 */
public class Sketch extends PApplet {
	
  // Image variables 
  PImage imgAlien; 
  PImage imgBackground; 
  PImage imgShip; 
  PImage imgLives; 
  PImage imgMeteor; 
  PImage imgStartScreen;
  PImage imgEndScreen; 
  PImage imgWinScreen; 

  // Alien variables
  int intAlienRows = 4;
  int intAliensPerRow = 8;
  float fltAlienSpacing = 35;
  float fltAlienMoveDistance = 1;
  boolean blnAlienDirection = true; 

  int intShootingInterval = 40;  
  float fltAlienBulletX;
  float fltAlienBulletY;

  // Declare and initializing arrays
  float[][] fltAlienX = new float[intAlienRows][intAliensPerRow];
  float[][] fltAlienY = new float[intAlienRows][intAliensPerRow];

  boolean[][] blnAlienAlive = new boolean[intAlienRows][intAliensPerRow];
 
  // Ship variables
  float fltShipX = 175;
  float fltShipY = 350; 
  float fltShipSpeed = 4;

  // Variables to track keyboard input
  boolean blnLeft = false;
  boolean blnRight = false;

  boolean blnShoot = false;
  float fltBulletX;
  float fltBulletY;

  // Game variables 
  boolean blnGameStart = false; 
  boolean blnWinner;
  int intNumLives = 3;
  int intScore = 0; 
  int intStart = 0; 
  
  /**
   * Called once at the beginning of execution.
   */
  public void settings() {
    size(500, 400);
  }

  /** 
   * Called once at the beginning of execution. Add initial set up values.
   */
  public void setup() {

    // Loading images and resizing 
    imgAlien = loadImage("Space Invaders Alien.png");
    imgAlien.resize(35, 35);

    imgShip = loadImage("Ship.png"); 
    imgShip.resize(50, 50);

    imgLives = loadImage("Lives.png");
    imgLives.resize(25, 25);

    imgMeteor = loadImage("meteor.png");
    imgMeteor.resize(60, 50); 

    imgStartScreen = loadImage("startScreen.jpeg");
    imgStartScreen.resize(575, 430); 

    imgBackground = loadImage("background.png");
    imgBackground.resize(700, 400);

    imgEndScreen = loadImage("endScreen.png");
    imgEndScreen.resize(500, 400); 

    imgWinScreen = loadImage("Win.png");
    imgWinScreen.resize(500, 400);

    // Nested loop initializes position of aliens in a 2D grid and sets them all alive.
    for(int intRow = 0; intRow < intAlienRows; intRow++) {
      for(int intCol = 0; intCol < intAliensPerRow; intCol++) {
        fltAlienX[intRow][intCol] = intCol * fltAlienSpacing + 60;
        fltAlienY[intRow][intCol] = intRow * fltAlienSpacing + 50;
        blnAlienAlive[intRow][intCol] = true;
      }
    }
    play1();
  }
    
  /**
   * Called repeatedly, anything drawn to the screen goes here
   */
  public void draw() {
    blnWinner = true; 

    if(intStart == 0){
      drawStartScreen(0, -10);
    }
    else{
      image(imgBackground, 0, 0);
  
      drawLivesIndicator();
      drawShip(fltShipX, fltShipY);
      drawMeteor();
      drawScore();
      moveAliens();
    
      // Iterate through grid of aliens and drawing them if they are alive, check if player wins
      for(int intRow = 0; intRow < intAlienRows; intRow++) {
        for(int intCol = 0; intCol < intAliensPerRow; intCol++) {
          if(blnAlienAlive[intRow][intCol] == true) {
            blnWinner = false; 
            drawAlien(fltAlienX[intRow][intCol], fltAlienY[intRow][intCol]);

            // Checks for collision with bullet of the current alien.
            // If true, sets that alien position to false and not alive.
            // Increases player score
            if (blnShoot == true && fltBulletY <= fltAlienY[intRow][intCol] + 35 && fltBulletY >= fltAlienY[intRow][intCol] &&
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
    if(blnLeft == true) {
      fltShipX -= fltShipSpeed;
    }
    if(blnRight == true) {
      fltShipX += fltShipSpeed;
    }
    // Checks if player shoots, draws bullet and movement.
    if(blnShoot == true){
      drawBullet(fltBulletX, fltBulletY);
      fltBulletY -= 10;

      // Checks if collision with meteor, stop drawing bullet.
      if (fltBulletY == 320 && fltBulletX >= 25 && fltBulletX <= 75 || fltBulletY == 320 && fltBulletX >= 125 && fltBulletX <= 175 ||
          fltBulletY == 320 && fltBulletX >= 225 && fltBulletX <= 275 || fltBulletY == 320 && fltBulletX >= 325 && fltBulletX <= 375 || fltBulletY == 320 && fltBulletX >= 425 && fltBulletX <= 475) {
        blnShoot = false; 
      }
    }

    // When alien will shoot next
    if(frameCount % intShootingInterval == 0) {
      alienShoot();
    }

    // Check if alien bullet is on the screen and update its position
    if(fltAlienBulletY > 0) {
      drawAlienBullet(fltAlienBulletX, fltAlienBulletY);
      fltAlienBulletY += 10;
    }

    // Check if alien bullet collides with ship, decrease number of lives
    for(int intRow = 0; intRow < intAlienRows; intRow++) {
      for(int intCol = 0; intCol < intAliensPerRow; intCol++) {
        if(blnAlienAlive[intRow][intCol] && fltAlienBulletY >= fltShipY && fltAlienBulletY <= fltShipY + 50 
            && fltAlienBulletX >= fltShipX && fltAlienBulletX <= fltShipX + 50) {
          intNumLives--;
          // Remove bullet from screen
          fltAlienBulletY = 0;

          // Check if game is over
          if(intNumLives <= 0) {
            blnGameStart = false;
          }
        }
      }
      
    }

    // Game Over draw end screen
    if(intNumLives == 0 && intStart != 0){
      drawEndScreen(0, 0);      
    }

    // Winner draw win screen
    if (blnWinner == true && intStart != 0){
      drawWinScreen(0,0);
    }
  }
  
  /**                                                       
   * Called when a key is pressed. Handles keyboard input for controlling the ship.
   */
  public void keyPressed(){
    // Control player movement with 'a' and 'd' keys
    if(key == 'a') {
      blnLeft = true;
    } 
    else if(key == 'd') {
      blnRight = true;
    }

    if(key == ' '){ 
      if(blnGameStart == false || intNumLives <= 0) {
        resetGame();
      } 
      else{
        blnShoot = true; 
        fltBulletX = fltShipX + 22;
        fltBulletY = fltShipY;
      }
    }
    if(key == ' ' && intStart == 0){
      intStart += 1; 
    }
  }

  /**
   * Called when a key is released. Handles keyboard input for controlling the player.
   */
  public void keyReleased(){
    // Release player movement based on key release 
    if(key == 'a') {
      blnLeft = false;
    } 
    else if(key == 'd') {
      blnRight = false;
    }
  }

  /**
   * Horizontally moves the aliens (right to left) based on their current direction.
   * If the aliens move near the edge of the screen, the direction changes.  
   */
  public void moveAliens(){
    boolean blnMoveDown = false;

    // Move Aliens left or right
    for(int intRow = 0; intRow < intAlienRows; intRow++) {
      for(int intCol = 0; intCol < intAliensPerRow; intCol++) {
        if(blnAlienDirection) {
          fltAlienX[intRow][intCol] += fltAlienMoveDistance; 
        } 
        else{
          fltAlienX[intRow][intCol] -= fltAlienMoveDistance; 
        }
      }
    }

    // Check if aliens reached the boundaries, set flag to move down
    for(int intRow = 0; intRow < intAlienRows; intRow++) {
      for(int intCol = 0; intCol < intAliensPerRow; intCol++) {
        if(fltAlienX[intRow][intCol] >= 450 - fltAlienMoveDistance || fltAlienX[intRow][intCol] <= fltAlienMoveDistance) {
          blnMoveDown = true;
        }
        // Reached bottom end game
        if(fltAlienY[intRow][intCol] >= 350){
          intNumLives = 0;
        }
      }
    }

    // Moves each alien in grid down and changes direction of movement
    if(blnMoveDown == true) {
      for(int intRow = 0; intRow < intAlienRows; intRow++) {
        for(int intCol = 0; intCol < intAliensPerRow; intCol++) {
          fltAlienY[intRow][intCol] += 10; 
        }
      }
      blnAlienDirection = !blnAlienDirection; 
    }
  }

  /**
   * Controls when and where the aliens shoot. 
   */
  public void alienShoot(){
    // Checks the game is in progress and get random alien
    if (blnGameStart == true) {
      int intRandomRow = (int) (random(intAlienRows));
      int intRandomCol = (int) (random(intAliensPerRow));

      // Check if selected alien is alive, offset bullet position closer to center of alien
      if (blnAlienAlive[intRandomRow][intRandomCol]) {
        float fltAlienX2 = fltAlienX[intRandomRow][intRandomCol] + 17;
        float fltAlienY2 = fltAlienY[intRandomRow][intRandomCol] + 35;

        // Check if the alien is between the meteors
        if (isBetweenMeteors(fltAlienX2, fltAlienY2)) {
          fltAlienBulletX = fltAlienX2;
          fltAlienBulletY = fltAlienY2;
        }
      }
    }
  }

  /**
   * Checks if the specified cordinates are between the meteors. 
   * @param fltX the X-coordinate to be checked.
   * @param fltY the Y-coordinate to be checked. 
   * @return true if the coordinates are not between the meteors, false otherwise. 
   */
  public boolean isBetweenMeteors(float fltX, float fltY){
    for(int i = 25; i <= width; i += 100){
      if(fltX > i && fltX < i + 50){
        return false; 
      }
    }
    return true; 
  }

  /**
   * Resets the game once player has lost. 
   */
  public void resetGame(){
    intNumLives = 3;
    intScore = 0;
    fltShipX = 175;
    fltShipY = 350;

    for (int intRow = 0; intRow < intAlienRows; intRow++) {
      for (int intCol = 0; intCol < intAliensPerRow; intCol++) {
        fltAlienX[intRow][intCol] = intCol * fltAlienSpacing + 60;
        fltAlienY[intRow][intCol] = intRow * fltAlienSpacing + 50;
        blnAlienAlive[intRow][intCol] = true;
      }
    }
    blnGameStart = true;
    intStart += 1;
  }

  /**
   * Draws a ship on the screen at the initialized coordinates.
   * @param fltShipX the X value of the ship.
   * @param fltShipY the Y value of the ship. 
   */
  public void drawShip(float fltShipX, float fltShipY){
    fltShipX = constrain(fltShipX, 0, width - 50);
    fltShipY = constrain(fltShipY, 0, height - 50);
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

  /**
   * Calculations for drawing the player lives indicator on the screen. 
   */
  public void drawLivesIndicator(){
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
  public void drawScore(){
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
  public void drawMeteor(){
    for(int i = 25; i <= width; i += 100){
      image(imgMeteor, i, 275); 
    }
  }

  /**
   * Calculations for drawing the bullets to the screen. 
   * @param fltBulletX the X value of the bullet.
   * @param fltBulletY the Y value of the bullet.
   */
  public void drawBullet(float fltBulletX, float fltBulletY){
    noStroke();
    fill(255); 
    rect(fltBulletX, fltBulletY, 3, 20);
  }

  /**
   * Calculations for drawing the alien bullets to the screen.
   * @param fltAlienBulletX the X value of the bullet.
   * @param fltAlienBulletY the Y value of the bullet. 
   */
  public void drawAlienBullet(float fltAlienBulletX, float fltAlienBulletY){
    noStroke();
    fill(255, 0, 0);
    rect(fltAlienBulletX, fltAlienBulletY, 3, 20);
  }

  /**
   * Draws the start screen. 
   * @param fltScreenX the X value of the start screen.
   * @param fltScreenY the Y value of the start screen. 
   */
  public void drawStartScreen(float fltScreenX, float fltScreenY){
    image(imgStartScreen, fltScreenX, fltScreenY);
    fill(255); 
    textSize(20); 
    text("PRESS SPACEBAR TO START", width - 430, height - 50);

    textSize(50); 
    text("Top Gun", width - 420, height - 175);
  }

  /**
   * Draws the end screen if player lives reaches 0 or if the aliens move too close to the bottom of the screen. 
   * @param fltEndX the X value of the end screen.
   * @param fltEndY the Y value of the end screen. 
   */
  public void drawEndScreen(float fltEndX, float fltEndY){
    image(imgEndScreen, fltEndX, fltEndY);
    fill(255); 
    textSize(20); 
    text("PRESS SPACEBAR TO PLAY AGAIN", width - 375, 350);
   
    drawScore(); 
    
    if(keyPressed && key == ' '){
      resetGame();
    }
  }

   /**
   * Draws the win screen. 
   * @param fltWinX the X value of the win screen.
   * @param fltWinY the Y value of the win screen. 
   */
  public void drawWinScreen(float fltWinX, float fltWinY){
    image(imgWinScreen, fltWinY, fltWinX); 
    fill(255); 
    textSize(20); 
    text("PRESS SPACEBAR TO PLAY AGAIN", width - 375, 350);

    if(keyPressed && key == ' '){
      resetGame();
    }
  }

  /**
   * Plays the audio file "TopGun.wav" in a continuous loop.
   * Uses the Java Sound API to handle audio playback.
   * If an error occurs during playback, an error message is printed to the console.
   */
  public void play1() {
    try{
      AudioInputStream aStream = AudioSystem.getAudioInputStream(new File("TopGun.wav").getAbsoluteFile());
      Clip aClip = AudioSystem.getClip();
      aClip.open(aStream);
      aClip.start();
      aClip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    catch(Exception ex){
      System.out.println("Error playing sound");
      ex.printStackTrace();
    }
  }
}
