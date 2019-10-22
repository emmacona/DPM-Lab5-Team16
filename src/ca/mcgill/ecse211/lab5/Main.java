package ca.mcgill.ecse211.lab5;

import lejos.hardware.Button;
// Static import to avoid duplicating variables and make the code easier to read
import static ca.mcgill.ecse211.lab5.Resources.*;

/**
 * The main driver class for the lab.
 */
public class Main {

  private static int buttonChoice;
  public static final double shootingRange = 135;
  private static final double ANGLE_CORRECTION_LOW = 2.9;
  private static final double ANGLE_CORRECTION_HIGH = 8.1;

  /**
   * The main entry point.
   *
   * @param args
   */
  public static void main(String[] args) {

    new Thread(odometer).start();
    new Thread(new Display()).start();
    if (Button.waitForAnyPress() == Button.ID_DOWN) { // Press DOWN for stationary launching
      launch();
    } else {                            // Press any other button for localization and launch

      leftMotor.stop(true);             // Added stops in between to also reduce random pulses
      rightMotor.stop(false);
      UltrasonicLocalizer.fallingEdge();
      leftMotor.stop(true);
      rightMotor.stop(false);
      LightLocalizer.localize();
      leftMotor.stop(true);
      rightMotor.stop(false);

      double deltaT = Math.atan((TARGET_Y - 1) / (TARGET_X - 1)) * 180 / Math.PI;
      double updateT = ANGLE_CORRECTION_LOW + (ANGLE_CORRECTION_HIGH - ANGLE_CORRECTION_LOW)
                         * deltaT / 90.0; // Theta correction that scales based on target location
      odometer.update(0, 0, updateT);

//      double deltaT = Math.atan((TARGET_Y - 1) / (TARGET_X - 1)) * 180 / Math.PI;
//      double updateT = ANGLE_CORRECTION_LOW + (ANGLE_CORRECTION_HIGH - ANGLE_CORRECTION_LOW) * deltaT / 90.0;                             // Theta correction that scales depending on where the target is
//      odometer.update(0, 0, updateT);
//
//      double launchX = findLaunchPoint()[0];
//      double launchY = findLaunchPoint()[1];
//      Navigation.travelTo(launchX, launchY);
      Navigation.travelTo(TARGET_X, TARGET_Y, shootingRange);
      launch();
    }
  }

  /**
   * Calculates the launch point x and y, using the target coordinate in Resources.
   *
   * @return the launch point, array of x and y position
   */
  public static double[] findLaunchPoint() {
    double dx = (TARGET_X - 1) * TILE_SIZE;
    double dy = (TARGET_Y - 1) * TILE_SIZE;
    double dTheta = Math.atan(dy / dx);

    double distance = Math.sqrt(dx * dx + dy * dy) - shootingRange;
    double launchX = TILE_SIZE + distance * Math.cos(dTheta);
    double launchY = TILE_SIZE + distance * Math.sin(dTheta);
    double[] result = {launchX / TILE_SIZE, launchY / TILE_SIZE};
    return result;
  }

  /**
   * Controls the movement of the catapult. Pressing the escape button will exit the program.
   * Otherwise, any other button will launch, as well as lower the catapult once launched.
   */
  public static void launch() {
    /*
     * Constants
     */
    final int loweringAcc = 300;
    final int loweringSpd = 70;
    final int loweringAng = 40;

    final int throwingAcc = 8000;
    final int throwingSpd = 500;
    final int throwingAng = 45;


    catapult1.setAcceleration(loweringAcc); // Initially lowers the catapult
    catapult1.setSpeed(loweringSpd);
    catapult2.setAcceleration(loweringAcc);
    catapult2.setSpeed(loweringSpd);
    catapult1.rotate(loweringAng, true);
    catapult2.rotate(loweringAng, false);
    while (true) {
      catapult1.setAcceleration(throwingAcc); // Set shooting speed
      catapult1.setSpeed(throwingSpd);
      catapult2.setAcceleration(throwingAcc);
      catapult2.setSpeed(throwingSpd);

      buttonChoice = Button.waitForAnyPress();
      if (buttonChoice == Button.ID_ESCAPE) {
        break; // Exits if Escape is pressed
      }
      catapult1.rotate(-throwingAng, true); // Shoots!
      catapult2.rotate(-throwingAng, false);

      buttonChoice = Button.waitForAnyPress();
      if (buttonChoice == Button.ID_ESCAPE) {
        break; // Exits if Escape is pressed
      }
      catapult1.setAcceleration(loweringAcc); // Reload / lower catapult
      catapult1.setSpeed(loweringSpd);
      catapult2.setAcceleration(loweringAcc);
      catapult2.setSpeed(loweringSpd);
      catapult1.rotate(loweringAng, true);
      catapult2.rotate(loweringAng, false);
    }
    System.exit(0);
  }

}
