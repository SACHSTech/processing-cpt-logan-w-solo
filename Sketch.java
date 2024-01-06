import processing.core.PApplet;
import processing.core.PImage;

public class Sketch extends PApplet {
	
  PImage imgAlien; 
  PImage imgBackground; 
  PImage imgShip; 
	
  float fltAlienX = 50; 
  float fltAlienY = 50; 

  float fltShipX = 175;
  float fltShipY = 300; 

  /**
   * Called once at the beginning of execution, put your size all in this method
   */
  public void settings() {
	// put your size call here
    size(400, 400);
  }

  /** 
   * Called once at the beginning of execution.  Add initial set up
   * values here i.e background, stroke, fill etc.
   */
  public void setup() {

    imgAlien = loadImage("Space Invaders Alien.png");
    imgAlien.resize(35, 35);

    imgShip = loadImage("Ship.png"); 
    imgShip.resize(50, 50);

    imgBackground = loadImage("background.png");
    imgBackground.resize(700, 400);
    }


  /**
   * Called repeatedly, anything drawn to the screen goes here
   */
  public void draw() {
	  
    image(imgBackground, 0, 0);
	  image(imgAlien, fltAlienX, fltAlienY);
    image(imgShip, fltShipX, fltShipY); 

  }
}


  
  // define other methods down here.
