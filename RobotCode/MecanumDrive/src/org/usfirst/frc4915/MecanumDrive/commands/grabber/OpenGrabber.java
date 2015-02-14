package org.usfirst.frc4915.MecanumDrive.commands.grabber;

import org.usfirst.frc4915.MecanumDrive.Robot;
import org.usfirst.frc4915.debuggersystem.CustomDebugger;

import edu.wpi.first.wpilibj.command.Command;

public class OpenGrabber extends Command {

	private boolean finished = false;

	public OpenGrabber() {
		// Use requires() here to declare subsystem dependencies
		// eg. requires(chassis);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
        Robot.debugger.logError(CustomDebugger.LoggerNames.GRABBER, "Opening");
		finished = false;
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		Robot.grabber.vent();
		Robot.grabber.open();

		finished = true;
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return finished;
	}

	// Called once after isFinished returns true
	protected void end() {
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}
}
