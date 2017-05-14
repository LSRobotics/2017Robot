package frc.team5181.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team5181.robot.actuators.DriveTrain;

public class WheelBalancer {
	public WheelBalancer() {}
	
	private double leftSpeed = 0;
	private double rightSpeed = 0;
	
	public void update(double d, double e) {
		this.leftSpeed += d;
		this.rightSpeed += e;
	}
	
	public void nextStep() {
		DriveTrain.tankDriveRaw(leftSpeed, rightSpeed);
		SmartDashboard.putString("WheelBalancer", leftSpeed + " | " + rightSpeed);
	}
}
