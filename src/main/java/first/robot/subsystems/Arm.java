package first.robot.subsystems;

import org.wpilib.hardware.expansionhub.ExpansionHubMotor;
import org.wpilib.driverstation.Gamepad;

/**
 * Simple Arm subsystem for controlling the robot's arm and lift.
 * - Right stick Y controls the pivot
 * - D-pad up/down controls the lift
 */
public class Arm {
  private final ExpansionHubMotor pivot = new ExpansionHubMotor(0, 0);
  private final ExpansionHubMotor liftLeft = new ExpansionHubMotor(0, 2);
  private final ExpansionHubMotor liftRight = new ExpansionHubMotor(3, 2);
  
  private static final double PIVOT_POWER_MULT = -1;  // Adjust for sensitivity
  private static final double LIFT_POWER = 0.7;       // Adjust for lift speed

  public Arm() {
    // Set braking behavior
    this.pivot.setFloatOn0(false);
    this.liftLeft.setFloatOn0(false);
    this.liftRight.setFloatOn0(false);
    this.liftLeft.setReversed(true);
    this.liftRight.setReversed(true);
  }
  
  /**
   * Update arm control based on gamepad input.
   * 
   * @param gamepad The gamepad to read input from
   */
  public void update(Gamepad gamepad) {
    // Control pivot with right stick Y (inverted so up = positive)
    double pivotPower = (gamepad.getRightTriggerAxis() - gamepad.getLeftTriggerAxis()) * PIVOT_POWER_MULT;
    pivot.setThrottle(pivotPower);
    
    // Control lift with D-pad up/down
    double liftPower = 0;
    if (gamepad.getDpadUpButton()) {
      liftPower = LIFT_POWER;  // Lift up
    } else if (gamepad.getDpadDownButton()) {
      liftPower = -LIFT_POWER; // Lift down
    }
    
    liftLeft.setThrottle(liftPower);
    liftRight.setThrottle(liftPower);
  }
  
  /**
   * Stop all arm motors.
   */
  public void stop() {
    pivot.setThrottle(0);
    liftLeft.setThrottle(0);
    liftRight.setThrottle(0);
  }
  
  /**
   * Get current pivot encoder position.
   * 
   * @return Pivot position in encoder counts
   */
  public double getPivotPosition() {
    return pivot.getEncoderPosition();
  }
  
  /**
   * Get current lift encoder position.
   * 
   * @return Lift position in encoder counts
   */
  public double getLiftPosition() {
    return liftLeft.getEncoderPosition();
  }
}
