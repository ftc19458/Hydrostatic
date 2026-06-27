// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package first.robot.opmode;

import org.wpilib.driverstation.Gamepad;
import org.wpilib.math.util.MathUtil;
import org.wpilib.opmode.PeriodicOpMode;
import org.wpilib.opmode.Teleop;
import first.robot.Robot;
import org.wpilib.smartdashboard.SmartDashboard;

@Teleop(name="Demo Teleop")
public class MyTeleop extends PeriodicOpMode {
  private final Robot robot;

  Gamepad gamepad = new Gamepad(0);

  private boolean driveFieldCentric = true;

  /** The Robot instance is passed into the opmode via the constructor. */
  public MyTeleop(Robot robot) {
    this.robot = robot;
  }

  @Override
  public void disabledPeriodic() {
    /* Called periodically (on every DS packet) while the robot is disabled. */
  }

  @Override
  public void start() {
    robot.imu.resetYaw();
  }

  @Override
  public void periodic() {
    if (gamepad.getLeftStickButton()) {
      driveFieldCentric = !driveFieldCentric;
    }

    double drive = MathUtil.applyDeadband(-gamepad.getLeftY(), 0.1);
    double strafe = MathUtil.applyDeadband(gamepad.getLeftX(), 0.1);
    double turn = MathUtil.applyDeadband(gamepad.getRightX(), 0.1);
    var yaw = robot.imu.getRotation2d();

    if (driveFieldCentric) {
      robot.drive.driveCartesian(drive, strafe, turn, yaw.times(-1));
    } else {
      robot.drive.driveCartesian(drive, strafe, turn);
    }

    /* Called periodically (on every DS packet) while the robot is enabled. */
    robot.arm.update(gamepad);

    SmartDashboard.putBoolean("field centric", driveFieldCentric);

    SmartDashboard.putNumber("drive", drive);
    SmartDashboard.putNumber("strafe", strafe);
    SmartDashboard.putNumber("turn", turn);

    SmartDashboard.putString("yaw", yaw.toString());

    SmartDashboard.putNumber("pivot", robot.arm.getPivotPosition());
    SmartDashboard.putNumber("lift", robot.arm.getLiftPosition());
  }

  @Override
  public void end() {
    /* Called when the robot is disabled (after previously being enabled). */
    robot.arm.stop();
  }

  @Override
  public void close() {
    /* Called when the opmode is de-selected / no additional methods will be called. */
  }
}
