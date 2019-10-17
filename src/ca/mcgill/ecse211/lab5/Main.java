package ca.mcgill.ecse211.lab5;

import static ca.mcgill.ecse211.lab5.Resources.*;
import lejos.hardware.Button;

public class Main {
  
  public static void main(String args[]) {
    catapult.setAcceleration(15000);
    catapult.setSpeed(1200);
    System.out.println("Ready");
    Button.waitForAnyPress();
    catapult.rotate(95, false);
  }
  
}
