package ca.mcgill.ecse211.lab5;

import static ca.mcgill.ecse211.lab5.Resources.*;

public class UltrasonicLocalizer {
   
  private static double[] angles = {0, 0};                      // Stores the 2 angles detected for each method
  private static float[] usData = new float[US_SENSOR.sampleSize()];   // Variable used to get USDistance
  private static double distance, deltaT;                       // Distance variable for USSensor and deltaT variable
  private final static double NOISE_MARGIN = 2;
  
  /**
   * Similar to the risingEdge method
   * 1. It starts by checking if it's facing a WALL. If yes, it will turn right until it's BIGGER than the band center
   * 2. After TURNING AWAY FROM a wall, it will continue to turn right until a falling edge is detected. (i.e. a wall is detected)
   * 3. The robot stops and records its angle
   * Repeat the 3 steps but in the opposite direction. This will give you 2 angles where a Falling Edge is detected
   * Use the math formula and add the deltaAngle to the current heading. This will update the current heading to its true heading
   * Then turn to 0 degree
   */
  public static void fallingEdge() {
    leftMotor.setSpeed(ROTATE_SPEED);
    rightMotor.setSpeed(ROTATE_SPEED);
    
    while(getDistance() < BAND_CENTER + NOISE_MARGIN) {      // Checks for the case when robot is facing a wall (right)
      leftMotor.forward();
      rightMotor.backward();
    }
    while(getDistance() > BAND_CENTER - NOISE_MARGIN) {      // Continues to turn until wall is detected
      leftMotor.forward();
      rightMotor.backward();
    }
    leftMotor.stop(true);
    rightMotor.stop(false);
    angles[0] = odometer.getXYT()[2];
    
    
    while(getDistance() < BAND_CENTER + NOISE_MARGIN) {      // Turns opposite side (left)
      leftMotor.backward();
      rightMotor.forward();
    }
    while (getDistance() > BAND_CENTER - NOISE_MARGIN) {     // Continues to turn until wall is detected
      leftMotor.backward();
      rightMotor.forward();
    }
    leftMotor.stop(true);
    rightMotor.stop(false);
    angles[1] = odometer.getXYT()[2];
    
    if (angles[0] > angles[1]) {
      deltaT = 45 - (angles[0] + angles[1]) / 2.0;
    } else {
      deltaT = 225 - (angles[0] + angles[1]) / 2.0;
    }
    
    double updatedHeading = odometer.getXYT()[2] + deltaT;
    updatedHeading = (updatedHeading > 360) ? updatedHeading - 360 : updatedHeading;
    odometer.setXYT(0, 0, updatedHeading);

    leftMotor.stop(true);
    rightMotor.stop(false);
  }
  
  public static double getDistance() {
    US_SENSOR.getDistanceMode().fetchSample(usData, 0);   // acquire data
    distance = (int) (usData[0] * 100.0);                 // extract from buffer, cast to int
    distance = distance > 150 ? 150 : distance;           // Small filter to filter out very large numbers
    return distance;
  }
}
