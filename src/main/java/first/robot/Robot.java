// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package first.robot;

import first.robot.subsystems.Arm;
import org.wpilib.drive.MecanumDrive;
import org.wpilib.driverstation.Alert;
import org.wpilib.framework.OpModeRobot;
import org.wpilib.hardware.expansionhub.ExpansionHubMotor;
import org.wpilib.hardware.imu.OnboardIMU;

/**
 * The methods in this class are called automatically as described in the OpModeRobot documentation.
 * OpMode classes anywhere in the package (or sub-packages) where this class is located are
 * automatically registered to display in the Driver Station. If you change the name of this class
 * or the package after creating this project, you must also update the Main.java file in the
 * project.
 */
public class Robot extends OpModeRobot {
  ExpansionHubMotor fl = new ExpansionHubMotor(0, 1);
  ExpansionHubMotor fr = new ExpansionHubMotor(3, 0);
  ExpansionHubMotor br = new ExpansionHubMotor(3, 1);
  ExpansionHubMotor bl = new ExpansionHubMotor(0, 3);

  public final MecanumDrive drive;
  public final Arm arm = new Arm();
  public final OnboardIMU imu = new OnboardIMU(OnboardIMU.MountOrientation.PORTRAIT);

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  public Robot() {
    fl.setFloatOn0(false);
    fr.setFloatOn0(false);
    br.setFloatOn0(false);
    bl.setFloatOn0(false);

    fl.setReversed(true);
    bl.setReversed(true);


    drive = new MecanumDrive(fl::setThrottle, bl::setThrottle, fr::setThrottle, br::setThrottle);

    drive.setDeadband(0.5);
  }

  /** This function is called exactly once when the DS first connects. */
  @Override
  public void driverStationConnected() {}

  /**
   * This function is called periodically anytime when no opmode is selected, including when the
   * Driver Station is disconnected.
   */
  @Override
  public void nonePeriodic() {}

  @Override
  public void close() {
    fl.close();
    fr.close();
    bl.close();
    br.close();

    super.close();
  }
}
