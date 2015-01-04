// RobotBuilder Version: 1.5
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc4915.Better_Test_Robot;
    
import edu.wpi.first.wpilibj.*;

import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import java.util.Vector;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    public static SpeedController drivetrainLeft;
    public static SpeedController drivetrainRight;
    public static RobotDrive drivetrainRobotDriveTwoMotors;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    public static void init() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        drivetrainLeft = new Talon(0);
        LiveWindow.addActuator("Drivetrain", "Left", (Talon) drivetrainLeft);
        
        drivetrainRight = new Talon(1);
        LiveWindow.addActuator("Drivetrain", "Right", (Talon) drivetrainRight);
        
        drivetrainRobotDriveTwoMotors = new RobotDrive(drivetrainLeft, drivetrainRight);
        
        drivetrainRobotDriveTwoMotors.setSafetyEnabled(true);
        drivetrainRobotDriveTwoMotors.setExpiration(0.1);
        drivetrainRobotDriveTwoMotors.setSensitivity(0.5);
        drivetrainRobotDriveTwoMotors.setMaxOutput(1.0);
        


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
    }
}
