package ca.mcgill.ecse211.lab5;

import static ca.mcgill.ecse211.lab5.Resources.*;
import lejos.hardware.Button;

public class Main {
  
  private static int buttonChoice;
  
  public static void main(String args[]) {
    
    new Thread(odometer).start();                       // Start Odometer thread
    new Thread(new Display()).start();                  // Start Display thread
    if (Button.waitForAnyPress() == Button.ID_DOWN) {   // Press DOWN for stationary launching
      launch();
    } else {                                            // Press any other button for localization and launch
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
      
      double pointX = 7.5;
      double pointY = 3.5;
      Navigation.travelTo(pointX, pointY);
      launch();
    }
  }
  
  public static void launch() {
    /*
     * Constants:
     *  Acceleration = 8000
     *  Speed = 500
     *  Angle = +/- 40
     */
    catapult1.setAcceleration(300);
    catapult1.setSpeed(70);
    catapult2.setAcceleration(300);
    catapult2.setSpeed(70);
    catapult1.rotate(40, true);
    catapult2.rotate(40, false);
    while(true) {
      catapult1.setAcceleration(8000);                      // Set shooting speed
      catapult1.setSpeed(500);
      catapult2.setAcceleration(8000);
      catapult2.setSpeed(500);
      
      System.out.println("Ready");
      buttonChoice = Button.waitForAnyPress();
      if (buttonChoice == Button.ID_ESCAPE) break; // Exits if Escape is pressed
      catapult1.rotate(-45, true);                          // Shoots!
      catapult2.rotate(-45, false);
      
      buttonChoice = Button.waitForAnyPress();              // Reload
      if (buttonChoice == Button.ID_ESCAPE) break; // Exits if Escape is pressed
      catapult1.setAcceleration(300);
      catapult1.setSpeed(70);
      catapult2.setAcceleration(300);
      catapult2.setSpeed(70);
      catapult1.rotate(40, true);
      catapult2.rotate(40, false);
    }
    System.exit(0);
  }
  
}
