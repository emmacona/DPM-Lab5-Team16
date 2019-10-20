package ca.mcgill.ecse211.lab5;

import static ca.mcgill.ecse211.lab5.Resources.*;
import lejos.hardware.Button;

public class Main {
  
  private static int buttonChoice;
  
  public static void main(String args[]) {
    
    new Thread(odometer).start();                       // Start Odometer thread
    new Thread(new Display()).start();                  // Start Display thread
    if (Button.waitForAnyPress() != Button.ID_DOWN) {
      leftMotor.setAcceleration(6000);
      rightMotor.setAcceleration(6000);
      
      leftMotor.stop(true);
      rightMotor.stop(false);
      UltrasonicLocalizer.fallingEdge();
      leftMotor.stop(true);
      rightMotor.stop(false);
      LightLocalizer.localize();
      leftMotor.stop(true);
      rightMotor.stop(false);
      //Button.waitForAnyPress();
      
      double pointX = 1.0;
      double pointY = 7.5;
      Navigation.travelTo(pointX, pointY);
    }
    /*
     * Constants:
     *  Acceleration = 7000
     *  Speed = 410
     *  Angle = +/- 40
     */
    catapult1.setAcceleration(300);
    catapult1.setSpeed(70);
    catapult2.setAcceleration(300);
    catapult2.setSpeed(70);
    catapult1.rotate(40, true);
    catapult2.rotate(40, false);
    do {
      catapult1.setAcceleration(8000);                      // Set shooting speed
      catapult1.setSpeed(450);
      catapult2.setAcceleration(8000);
      catapult2.setSpeed(450);
      
      System.out.println("Ready");
      buttonChoice = Button.waitForAnyPress();
      if (buttonChoice == Button.ID_ESCAPE) System.exit(0); // Exits if Escape is pressed
      catapult1.rotate(-45, true);                          // Shoots!
      catapult2.rotate(-45, false);
      
      buttonChoice = Button.waitForAnyPress();                             // Reload
      catapult1.setAcceleration(300);
      catapult1.setSpeed(70);
      catapult2.setAcceleration(300);
      catapult2.setSpeed(70);
      catapult1.rotate(40, true);
      catapult2.rotate(40, false);
    } while (buttonChoice != Button.ID_ESCAPE);
    System.exit(0);
  }
  
  
}
