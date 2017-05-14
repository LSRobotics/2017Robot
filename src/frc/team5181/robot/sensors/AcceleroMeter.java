package frc.team5181.robot.sensors;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;

public class AcceleroMeter extends BuiltInAccelerometer {

	public double[] get3Axis() {
		return new double[] {super.getX(), super.getY(), super.getZ()};
	}
	
	public double[] get2Axis() {
		return new double[]{ super.getX(), super.getY() };
	}

}
