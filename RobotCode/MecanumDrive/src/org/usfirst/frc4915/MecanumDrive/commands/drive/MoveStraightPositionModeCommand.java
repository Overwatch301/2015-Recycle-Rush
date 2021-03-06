package org.usfirst.frc4915.MecanumDrive.commands.drive;

import java.util.ArrayList;
import java.util.List;

import org.usfirst.frc4915.MecanumDrive.Robot;
import org.usfirst.frc4915.MecanumDrive.subsystems.DriveTrain;
import org.usfirst.frc4915.debuggersystem.CustomDebugger;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Command;

public class MoveStraightPositionModeCommand extends Command {
	public static List<CANTalon> motors = DriveTrain.motors;
	public double inputDistance;
	private DriveTrain driveTrain = Robot.driveTrain;

	public MoveStraightPositionModeCommand(double inputDistance) {
		requires(driveTrain);
        Robot.debugger.logError(CustomDebugger.LoggerNames.DRIVETRAIN, "***MoveStraightPositionModeCommand inputDistance: " + inputDistance + "*******");
		this.inputDistance = inputDistance;
		// Use requires() here to declare subsystem dependencies
		// eg. requires(chassis);
	}

	private List<Double> desiredTicksValue;

	// Called just before this Command runs the first time
	/**
	 * This initializes the variables for the distance calculator.
	 */
	protected void initialize() {
		desiredTicksValue = new ArrayList<Double>();

		double ticksToMove = inputDistance * 12 * 1000 / (6 * Math.PI);

        Robot.debugger.logError(CustomDebugger.LoggerNames.DRIVETRAIN, "Input distance: " + inputDistance + " ft, ticks to move: " + ticksToMove);

		for (int i = 0; i < motors.size(); i++) {
			CANTalon motor = motors.get(i);

			double startingTickValue = motor.getPosition();
			Double endValue = startingTickValue + ticksToMove;
			if (i >= 2) {
				// right motors are inverted
				endValue = startingTickValue - ticksToMove;
			}

            Robot.debugger.logError(CustomDebugger.LoggerNames.DRIVETRAIN, "Motor " + i + ": starting position " + startingTickValue + ", desired position " + endValue);
			desiredTicksValue.add(endValue);
		}
	}

	/**
	 * This uses the wheel circumference and the number of rotations to compute
	 * the distance traveled.
	 */

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		if (inputDistance < 0)
			driveTrain.driveStraight(0.7);
		else
			driveTrain.driveStraight(-0.7);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		// checking to see if the front motors have finished (regardless of what direction the robot is driving)
		if (inputDistance > 0)
			return isMotorFinished(0) || isMotorFinished(2);
		else
			return isMotorFinished(1) || isMotorFinished(3);
	}
	private boolean isMotorFinished(int i) {
		boolean finished = false;
		double currentPosition = motors.get(i).getPosition();
		Double desiredPosition = desiredTicksValue.get(i);
        Robot.debugger.logError(CustomDebugger.LoggerNames.DRIVETRAIN, "Motor " + i + ": current position: " + currentPosition + ", desired position " + desiredPosition);

		if (i >= 2) {
			// right motors are inverted
			if (inputDistance < 0) {
				finished = currentPosition >= desiredPosition;
			} else {
				finished = currentPosition <= desiredPosition;
			}
		} else {
			if (inputDistance < 0) {
				finished = currentPosition <= desiredPosition;
			} else {
				finished = currentPosition >= desiredPosition;
			}
		}
		return finished;

	}
	// Called once after isFinished returns true
	protected void end() {
        Robot.debugger.logError(CustomDebugger.LoggerNames.DRIVETRAIN, "Stopping motor");
		driveTrain.getRobotDrive().stopMotor();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
        Robot.debugger.logError(CustomDebugger.LoggerNames.DRIVETRAIN, "Command interrupted!");
		end();
	}
}
