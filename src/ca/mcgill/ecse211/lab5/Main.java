package ca.mcgill.ecse211.lab5;

import static ca.mcgill.ecse211.lab5.Resources.*;
import lejos.hardware.Button;

public class Main {
  
  private static int buttonChoice;
  
  public static void main(String args[]) {
    /*
     * Constants:
     *  Acceleration = 7000
     *  Speed = 300
     *  Angle = +/- 40
     */
    while (true) {
      catapult1.setAcceleration(7000);                      // Set shooting speed
      catapult1.setSpeed(300);
      catapult2.setAcceleration(7000);
      catapult2.setSpeed(300);
      
      System.out.println("Ready");
      buttonChoice = Button.waitForAnyPress();
      if (buttonChoice == Button.ID_ESCAPE) System.exit(0); // Exits if Escape is pressed
      catapult1.rotate(-40, true);                          // Shoots!
      catapult2.rotate(-40, false);
      
      Button.waitForAnyPress();                             // Reload
      catapult1.setAcceleration(300);
      catapult1.setSpeed(70);
      catapult2.setAcceleration(300);
      catapult2.setSpeed(70);
      catapult1.rotate(40, true);
      catapult2.rotate(40, false);
    }
  }
  
}
