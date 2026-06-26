package first.robot.subsystems;

import org.wpilib.hardware.expansionhub.ExpansionHubMotor;
import org.wpilib.driverstation.Gamepad;

/**
 * Simple Arm subsystem for controlling the robot's arm and lift.
 * - Right stick Y controls the pivot
 * - D-pad up/down controls the lift
 */
public class Arm {
  private final ExpansionHubMotor pivot;
  private final ExpansionHubMotor liftLeft;
  private final ExpansionHubMotor liftRight;
  private double liftTarget;
  
  private static final double PIVOT_POWER_MULT = 1;  // Adjust for sensitivity
  private static final double LIFT_STEP = 20.0;      // Encoder counts per loop while D-pad is held
  private static final double LIFT_MIN = 0.0;
  private static final double LIFT_MAX = 10000.0;  // Adjust based on your lift's range
  
  /**
   * Constructor for Arm subsystem.
   * 
   * @param pivot Motor controlling the pivot (arm rotation)
   * @param liftLeft Left lift motor
   * @param liftRight Right lift motor
   */
  public Arm(ExpansionHubMotor pivot, ExpansionHubMotor liftLeft, ExpansionHubMotor liftRight) {
    this.pivot = pivot;
    this.liftLeft = liftLeft;
    this.liftRight = liftRight;
    this.liftTarget = liftLeft.getEncoderPosition();
    
    // Set braking behavior
    this.pivot.setFloatOn0(false);
    this.liftLeft.setFloatOn0(false);
    this.liftRight.setFloatOn0(false);

    // Use encoder counts directly for closed-loop lift position control.
    this.liftLeft.setDistancePerCount(1.0);
    this.liftRight.setDistancePerCount(1.0);
  }
  
  /**
   * Update arm control based on gamepad input.
   * 
   * @param gamepad The gamepad to read input from
   */
  public void update(Gamepad gamepad) {
    // Control pivot with right stick Y (inverted so up = positive)
    double pivotPower = -gamepad.getRightY() * PIVOT_POWER_MULT;
    pivot.setThrottle(pivotPower);
    
    // D-pad nudges the lift target; motor PID holds target when released.
    if (gamepad.getDpadUpButton()) {
      liftTarget += LIFT_STEP;
    } else if (gamepad.getDpadDownButton()) {
      liftTarget -= LIFT_STEP;
    }

    liftTarget = clamp(liftTarget, LIFT_MIN, LIFT_MAX);
    
    liftLeft.setPositionSetpoint(liftTarget);
    liftRight.setPositionSetpoint(liftTarget);
  }
  
  /**
   * Stop all arm motors.
   */
  public void stop() {
    pivot.setThrottle(0);
    liftTarget = liftLeft.getEncoderPosition();
    liftLeft.setPositionSetpoint(liftTarget);
    liftRight.setPositionSetpoint(liftTarget);
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

  private static double clamp(double value, double min, double max) {
    return Math.max(min, Math.min(max, value));
  }
}
