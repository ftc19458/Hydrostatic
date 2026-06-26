// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package first.robot.opmode;

import org.wpilib.drive.MecanumDrive;
import org.wpilib.driverstation.Gamepad;
import org.wpilib.hardware.expansionhub.ExpansionHubMotor;
import org.wpilib.hardware.imu.OnboardIMU;
import org.wpilib.hardware.imu.OnboardIMU.MountOrientation;
import org.wpilib.math.util.MathUtil;
import org.wpilib.opmode.PeriodicOpMode;
import org.wpilib.opmode.Teleop;
import first.robot.Robot;
import first.robot.subsystems.Arm;

@Teleop
public class MyTeleop extends PeriodicOpMode {
  private final Robot robot;

  Gamepad gamepad = new Gamepad(0);

  ExpansionHubMotor fr = new ExpansionHubMotor(3,0 );
  ExpansionHubMotor fl = new ExpansionHubMotor(0,1 );
  ExpansionHubMotor bl = new ExpansionHubMotor(0,3 );
  ExpansionHubMotor br = new ExpansionHubMotor(3,1 );

  ExpansionHubMotor pivot = new ExpansionHubMotor(0, 0);
  ExpansionHubMotor liftLeft = new ExpansionHubMotor(0, 2);
  ExpansionHubMotor liftRight = new ExpansionHubMotor(3, 2);

  OnboardIMU imu = new OnboardIMU(MountOrientation.PORTRAIT);
  
  Arm arm;


  MecanumDrive drive;
  

  /** The Robot instance is passed into the opmode via the constructor. */
  public MyTeleop(Robot robot) {
    fl.setFloatOn0(false);
    fr.setFloatOn0(false);
    br.setFloatOn0(false);
    bl.setFloatOn0(false);


    fl.setReversed(true);
    bl.setReversed(true);    


    drive = new MecanumDrive(fl::setThrottle, bl::setThrottle, fr::setThrottle, br::setThrottle);

    drive.setDeadband(0.5);
    this.robot = robot;
    
    arm = new Arm(pivot, liftLeft, liftRight);
  }

  @Override
  public void disabledPeriodic() {
    /* Called periodically (on every DS packet) while the robot is disabled. */
  }

  @Override
  public void start() {
    imu.resetYaw();
    
  }

  @Override
  public void periodic() {
    /* Called periodically (on every DS packet) while the robot is enabled. */
    drive.driveCartesian(MathUtil.applyDeadband(gamepad.getLeftY(), 0.1),  MathUtil.applyDeadband(-gamepad.getLeftX(),0.1), gamepad.getRightX(),imu.getRotation2d().times(-1));
    arm.update(gamepad);
    
  }

  @Override
  public void end() {
    /* Called when the robot is disabled (after previously being enabled). */
    arm.stop();
  }

  @Override
  public void close() {
    /* Called when the opmode is de-selected / no additional methods will be called. */
  }
}
