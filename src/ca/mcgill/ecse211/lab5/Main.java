package ca.mcgill.ecse211.lab5;

import static ca.mcgill.ecse211.lab5.Resources.*;
import lejos.hardware.Button;

public class Main {
  
  private static int buttonChoice;
  private static final double shootingRange = 135;
  
  public static void main(String args[]) {
    
    new Thread(odometer).start();                       
    new Thread(new Display()).start();                  
    if (Button.waitForAnyPress() == Button.ID_DOWN) {   // Press DOWN for stationary launching
      launch();
    } else {                                            // Press any other button for localization and launch
      leftMotor.setAcceleration(6000);
      rightMotor.setAcceleration(6000);
      
      leftMotor.stop(true);                             // Added stops in between to also reduce random pulses
      rightMotor.stop(false);
      UltrasonicLocalizer.fallingEdge();
      leftMotor.stop(true);
      rightMotor.stop(false);
      LightLocalizer.localize();
      leftMotor.stop(true);
      rightMotor.stop(false);
      
      double targetX = 7.5;
      double targetY = 3.5;
      double deltaT = Math.atan((targetY - 1) / (targetX - 1)) * 180 / Math.PI;
      double updateT = 2.9 + (8.1-2.9) * deltaT / 90.0;                             // Theta correction that scales depending on where the target is
      odometer.update(0, 0, updateT);
      Navigation.travelTo(targetX, targetY, shootingRange);
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
    catapult1.setAcceleration(300);                         // Initially lowers the catapult
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
      
      //System.out.println("Ready");
      buttonChoice = Button.waitForAnyPress();
      if (buttonChoice == Button.ID_ESCAPE) break;          // Exits if Escape is pressed
      catapult1.rotate(-45, true);                          // Shoots!
      catapult2.rotate(-45, false);
      
      buttonChoice = Button.waitForAnyPress();
      if (buttonChoice == Button.ID_ESCAPE) break;          // Exits if Escape is pressed
      catapult1.setAcceleration(300);                       // Reload / lower catapult
      catapult1.setSpeed(70);
      catapult2.setAcceleration(300);
      catapult2.setSpeed(70);
      catapult1.rotate(40, true);
      catapult2.rotate(40, false);
    }
    System.exit(0);
  }
  
}
