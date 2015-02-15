package org.usfirst.frc4915.MecanumDrive.subsystems;

import org.usfirst.frc4915.MecanumDrive.Robot;
import org.usfirst.frc4915.MecanumDrive.RobotMap;
import org.usfirst.frc4915.MecanumDrive.commands.elevator.ElevatorFineTune;
import org.usfirst.frc4915.debuggersystem.CustomDebugger.LoggerNames;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Elevator extends Subsystem {

	// These positions describe the number of totes you stacking on top of.
	// If you need to stack on top of 3 totes, use position 3.
	// If you need to stack on the ground, use position 0.

	// TODO initialize height
	// Not in inches. Between minimumPotentiometerValue and
	// maximumPotentiometerValue.
	public static double setPoint;

	// POTENTIOMTERS : fwd --> top, rev --> bottom

	// Set by ElevatorMax/MinHeightCalibrate commands
	public static double minimumPotentiometerValue = 0;
	public static double maximumPotentiometerValue = 1023;

	public static boolean SAFETY = true;
	
	public static final double RANGE_OF_MOTION = 53; // The elevator can go a
														// distance between 54
														// inches

	public static final double CHASIS_HEIGHT = 5; // These two measurements are
													// in inches
	public static final double HEIGHT_OF_TOTE = 12;

	private static final double JOYSTICK_SCALE = -5; // TODO Decide scale for
														// joystick movement
														// position change

	public CANTalon winch = RobotMap.elevatorWinchMotor;

	/**
	 * Initializes the default command (WPI java default method) Called on
	 * initialization of the subsystem
	 */
	public void initDefaultCommand() {
		setDefaultCommand(new ElevatorFineTune());
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
	}

	// TODO Make sure that the winch does not begin winding the wrong way -- We
	// may use a limit switch to tell if the cable is tight or not.
	// Discuss this with Elevator Subteam and Riyadth
	/**
	 * Moves the elevator at a speed given by the joystick (y axis).
	 * 
	 * @param joystick Forward on joystick is up, backward is down
	 */
	public void moveWithJoystick(Joystick joystick) {
		double joystickY = joystick.getAxis(Joystick.AxisType.kY);
		Robot.debugger.logError(LoggerNames.ELEVATOR, "Elevator joystick " + joystickY);
		if (Math.abs(joystickY) <= .2) {
			Robot.debugger.logError(LoggerNames.ELEVATOR, "Joystick value too small");
			moveElevator(0);
		} else {
			moveElevator(joystickY * JOYSTICK_SCALE);
		}
	}
	
	// TODO Change the functionality to increase/decrease the height value.
	/**
	 * Changes height based on the input
	 * 
	 * @param heightChange
	 *            + goes up, - goes down
	 */
	public void moveElevator(double heightChange) {
		setPoint += heightChange;
		moveToHeight();
	}

	/**
	 * Moves the elevator to the height variable Ensures that height is in range
	 * first
	 */
	public void moveToHeight() {
		if (SAFETY) {
			keepHeightInRange();
		}
		winch.set(setPoint);
	}
	
	/**
	 * Sets the height to where it currently is so that the elevator should not go up or down.
	 */
	public void setHieghtToCurrentPosition() {
		setPoint = getPosition();
	}

	/**
	 * Stops the winch from winding. This may still have the elevator fall under
	 * it's own weight.
	 */
	public void stopElevator() {
		winch.disableControl();
		Robot.debugger.logError(LoggerNames.ELEVATOR, "Winch has stopped");
	}

	/**
	 * @return the position of the elevator in inches (between 0 and 54)
	 */
	public double getPositionInches() {
		double position = (getPosition() - minimumPotentiometerValue) 
						* (RANGE_OF_MOTION / (maximumPotentiometerValue - minimumPotentiometerValue));
		Robot.debugger.logError(LoggerNames.ELEVATOR, "The elevator is at position " + position + " (inches)");
		return position;
	}

	/**
	 * @return the read value from the potentiometer (between 0 and 1023)
	 */
	public double getPosition() {
		Robot.debugger.logError(LoggerNames.ELEVATOR, "The potentiometer reads " + winch.getPosition());
		return winch.getPosition();
	}

	/**
	 * Converts from a position between zero totes to six totes to inches.
	 * If you need to stack on top of 3 totes, use position 3.
	 * If you need to stack on the ground, use position 0.
	 * 
	 * @param positionNumber
	 *            the number of totes you are stacking on top of.
	 */
	public void setHeightToPosition(double positionNumber) {

		// find the range between the min and max Potentiometer values, divide by 54 to get
		// the change in value per inch and multiply by the number of inches that the totes are stacked
		setPoint = minimumPotentiometerValue + ((maximumPotentiometerValue - minimumPotentiometerValue) 
											 * HEIGHT_OF_TOTE * positionNumber / RANGE_OF_MOTION);
		Robot.debugger.logError(LoggerNames.ELEVATOR, "Elevator's height is " + setPoint);
	}
	
	/**
	 * 
	 * @return Level of Elevator in number of totes
	 */
	public double getElevatorLevel() {
		return ((RANGE_OF_MOTION * (winch.getPosition())) / (maximumPotentiometerValue - minimumPotentiometerValue)) / 12;
	}

	/**
	 * 
	 * @return if the elevator is at it's max height, return
	 */
	public boolean isAtTopOfElevator() {
		if (winch.isFwdLimitSwitchClosed()) {
			Robot.debugger.logError(LoggerNames.ELEVATOR, "Elevator Top LimitSwitch has been reached");
		}
		return winch.isFwdLimitSwitchClosed();
	}

	/**
	 * 
	 * @return if the elevator is at it's min height, return true
	 */
	public boolean isAtBottomOfElevator() {
		if (winch.isRevLimitSwitchClosed()) {
			Robot.debugger.logError(LoggerNames.ELEVATOR, "Elevator Bottom LimitSwitch has been reached");
		}
		return winch.isRevLimitSwitchClosed();
	}

	/**
	 * Makes sure that the height value doesn't increase or decrease beyond the
	 * min/maximum values Also, if the limit switch is pressed (fwd --> top ~700?, rev
	 * --> bottom ~0) it will update maximum/minimum potentiometer values.
	 */
	public void keepHeightInRange() {
		if (isAtTopOfElevator()) {
			winch.enableBrakeMode(true);
			maximumPotentiometerValue = getPosition();
		}
		if (isAtBottomOfElevator()) {
			winch.enableBrakeMode(true);
			minimumPotentiometerValue = getPosition();
		}
		if (setPoint > maximumPotentiometerValue) {
			setPoint = maximumPotentiometerValue;
		}
		if (setPoint < minimumPotentiometerValue) {
			setPoint = minimumPotentiometerValue;
		}
		Robot.debugger.logError(LoggerNames.ELEVATOR, "Elevator's height is " + setPoint);
	}

}