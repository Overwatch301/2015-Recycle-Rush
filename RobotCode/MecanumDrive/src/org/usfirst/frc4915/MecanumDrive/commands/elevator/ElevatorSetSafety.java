package org.usfirst.frc4915.MecanumDrive.commands.elevator;

import org.usfirst.frc4915.MecanumDrive.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ElevatorSetSafety extends Command {

	private boolean safety;
	
    public ElevatorSetSafety(boolean value) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	safety = value;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.elevator.SAFETY = safety;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.elevator.SAFETY = safety;
    	Robot.elevator.minimumPotentiometerValue = 0;
    	Robot.elevator.maximumPotentiometerValue = 1023;
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
