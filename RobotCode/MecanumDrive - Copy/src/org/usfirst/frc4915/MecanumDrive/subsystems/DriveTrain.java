// RobotBuilder Version: 1.5
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc4915.MecanumDrive.subsystems;

import java.util.Arrays;
import java.util.List;

import org.usfirst.frc4915.MecanumDrive.Robot;
import org.usfirst.frc4915.MecanumDrive.RobotMap;
import org.usfirst.frc4915.MecanumDrive.commands.*;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Subsystem;

//TODO write javadoc comments for all these methods

/**
 *
 */
public class DriveTrain extends Subsystem {
    SpeedController leftFront = RobotMap.mecanumDriveControls1LeftFront10;
    SpeedController leftRear = RobotMap.mecanumDriveControls1LeftRear11;
    SpeedController rightFront = RobotMap.mecanumDriveControls1RightFront12;
    SpeedController rightRear = RobotMap.mecanumDriveControls1RightRear13;
    RobotDrive robotDrive;

    public static List <CANTalon> motors =  Arrays.asList(RobotMap.mecanumDriveControls1LeftFront10, 
			RobotMap.mecanumDriveControls1LeftRear11, 
			RobotMap.mecanumDriveControls1RightFront12,
			RobotMap.mecanumDriveControls1RightRear13 );

    public static Gyro gyro = RobotMap.gyro;
    public static Ultrasonic distanceSensor = RobotMap.distanceSensor;
    
    public double Throttle = 0;

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        //setDefaultCommand(new MecanumDrive());
        
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
        
        robotDrive = RobotMap.driveTrainRobotDrive;
    }
    
    public RobotDrive getRobotDrive() {
    	return robotDrive;
    }
    
    /**
     * Drives a mecanum drivetrain in the direction of the joystick pointed
     * 
     * @param joystick that controls the robot movement
     */
    public void mecanumDrive(Joystick joystick) {
    	
        double joystickX = joystick.getAxis(Joystick.AxisType.kX);
        double joystickY = joystick.getAxis(Joystick.AxisType.kY);
        double joystickTwist = joystick.getAxis(Joystick.AxisType.kTwist);
        Throttle = 0.50 * (joystick.getThrottle()) + 0.50; 
        if ((Math.abs(joystickX) < 0.2)) {
        	joystickX = 0;
        }
        if ((Math.abs(joystickY) < 0.2)) {
        	joystickY = 0;
        }
        if ((Math.abs(joystickTwist) < 0.2)) {
        	joystickTwist = 0;
        }
        System.out.println(joystickX + ", " + joystickY + ", " + joystickTwist);
        if ((Math.abs(joystickX) < 0.2) && (Math.abs(joystickY) < 0.2) && (Math.abs(joystickTwist) < 0.2)) {
            System.out.println("Stopping Motor");	
        	robotDrive.stopMotor();
        } else {
        	System.out.println("Driving");
        	robotDrive.mecanumDrive_Cartesian(joystickX, joystickY, joystickTwist, 0.0);
        }

    }
    
    public void driveStraight(double speed) {
    	robotDrive.mecanumDrive_Cartesian(0.0, speed, 0.0, 0.0);
    }
    

    /**
     * 
     * @param motor is the motor on the wheels with an encoder used to determine the distance traveled. 
     * @param elapsed is the time since the last sampling of the motor. 
     * @return the distance traveled since the last sampling of the encoder. 
     */
    // calculates the distance traveled using the wheel circumference and the number of wheel rotations. 
    public double getDistanceForMotor(CANTalon motor, long elapsed){
    	int ticksPerRevolution = 1000;
    	double circumferenceOfWheel = 6*Math.PI;
    	int inchesPerFoot = 12;
    	System.out.println("Speed" + motor.getSpeed());
    	return motor.getSpeed()*elapsed/ticksPerRevolution*circumferenceOfWheel/inchesPerFoot;
    }
    
    public void arcadeDrive(Joystick stick){
    	System.out.println("Arcade Drive");
    	robotDrive.arcadeDrive(stick);
    }
}

