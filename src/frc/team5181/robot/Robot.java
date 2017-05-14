package frc.team5181.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team5181.robot.actuators.*;
import frc.team5181.robot.autonomous.AutonChooser;
import frc.team5181.robot.autonomous.GearLoading;
import frc.team5181.robot.camera.Streaming;
import frc.team5181.robot.pid.PIDTuning;
import frc.team5181.robot.sensors.*;
import frc.team5181.robot.tasking.Task;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	Task autonCommand;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		DriveTrain.init(1.1);
		UltrasonicGroup.init();
		IRSensorGroup.init();
		WheelEncoders.init();
		BallShoot.init();
		Intake.init();
		Winch.init();
		//PDP.init();
		Streaming.beginStreaming();
		AutonChooser.chooserInit();
	}

	/**
	 *
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		autonCommand = AutonChooser.getAutonCommand();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		autonCommand.nextStep();
		
	}
	
	/**
	 * This function is called once before the operator control
	 */
	@Override
	public void teleopInit(){
		SmartDashboard.putString("Teleop","Enabled");
		SmartDashboard.putString("GearLoader", "Disabled");
		WheelEncoders.reset();
		this.previousBStatus = false;
		this.isAutoGearLoading = false;
	}
	
	private boolean previousBStatus;
	private GearLoading gearLoader;
	private boolean isAutoGearLoading;
	
	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
        Gamepad.setGPAxis();
        Gamepad.setGPButton();
        if (this.previousBStatus && !Gamepad.GP_B_BUTTON_STATE) { // B released
        	this.isAutoGearLoading = !this.isAutoGearLoading;
        	if (this.isAutoGearLoading) {
        		this.gearLoader = new GearLoading();
        	}
        }
        this.previousBStatus = Gamepad.GP_B_BUTTON_STATE;
		SmartDashboard.putString("GearLoader", this.isAutoGearLoading ? "Enabled" : "Disabled");
        if (this.isAutoGearLoading) {
        	if (this.gearLoader.nextStep()) {
        		this.isAutoGearLoading = false;
        	}
        } else {
            DriveTrain.updateSpeedLimit(Gamepad.GP_LEFT_BUMPER_STATE, Gamepad.GP_RIGHT_BUMPER_STATE, false);
            DriveTrain.arcadeDrive(Gamepad.GP_RIGHT_Stick_X_State, -Gamepad.GP_RIGHT_Stick_Y_State * 1.1);
            BallShoot.set(Gamepad.GP_LEFT_Trigger_State);
            Winch.set(Gamepad.GP_RIGHT_Trigger_State);
            Intake.set(Gamepad.GP_X_BUTTON_STATE);
            SmartDashboard.putNumber("EncoderDistance",WheelEncoders.getDistance());
            SmartDashboard.putNumber("EncoderTurn", WheelEncoders.getTurn());
            //PDP.updateCurrents();
        }
	}

	@Override
	public void testInit(){
		//PIDTuning.rotationalInit();
		NavX.init();
	} 
	
	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		//Calibration.periodic();
		//Gamepad.setNaturalState();
		//PIDTuning.tuningPeriodic();
		//DriveTrain.tankDrive(0.5, 0.6);
		double[] d = NavX.get3DRotations();
		DriverStation.reportError("ROT: " + d[0] + " " + d[1] + " " + d[2], false);
	}

	@Override
	public void disabledInit() {
		
	}
}

