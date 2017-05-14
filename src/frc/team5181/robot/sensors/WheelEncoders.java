package frc.team5181.robot.sensors;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import frc.team5181.robot.Statics;
import frc.team5181.robot.autonomous.FieldEquations;
import frc.team5181.robot.pid.PIDSource;

public class WheelEncoders {

	public static final double BOT_Radius = FieldEquations.BackWheelAxisLength; // in centimeters
	//TODO make sure gear side positive
	public static final double distancePerPulse = -0.2046 ;// in centimeters
	
	//weird fix
	public static final boolean leftOnly = false;

	private static class WheelEncoder extends Encoder {

		public WheelEncoder(int channelA, int channelB) {
			super(channelA, channelB);
		}

		public WheelEncoder(int[] channels) {
			super(channels[0], channels[1]);
		}

	}

	static Encoder left, right;

	public static void init() {
		left = new WheelEncoder(Statics.LEFT_Encoder);
		left.setDistancePerPulse(distancePerPulse);
		right = new WheelEncoder(Statics.RIGHT_Encoder);
		right.setDistancePerPulse(-distancePerPulse);
	}

	public static double getTurn() {// in radians, left turn is positive
		double diff = right.getDistance() - left.getDistance();
		if (leftOnly) diff = -2 * left.getDistance();
		return diff / BOT_Radius;
	}

	public static double getTurnDegree() {
		return (getTurn() * 180 / Math.PI);
	}

	public static double getDistance() {
		if (DriverStation.getInstance().isAutonomous() || DriverStation.getInstance().isTest()) DriverStation.reportError("L: "+left.getDistance() + " R: "+right.getDistance(), false);
		if (leftOnly) return left.getDistance();
		return (left.getDistance() + right.getDistance()) / 2;
	}

	public static double getParallelDistance() {
		if (Math.abs(left.getDistance() - right.getDistance()) < 0.5) {
			return getDistance();
		} else {
			return -1;
		}
	}

	public static double getLeftDistance() {
		return left.getDistance();
	}

	public static double getRightDistance() {
		return right.getDistance();
	}

	public static void reset() {
		left.reset();
		right.reset();
	}

	/**
	 * Get an anonymous PIDSource that reads the distance of this encoder in
	 * unit of cm.
	 * 
	 * @return An anonymous PIDSource.
	 */
	public static PIDSource getDistanceReader() {
		return WheelEncoders::getDistance;
	}

	/**
	 * Get a anonymous PIDSource that reads the turn of this encoder in unit of
	 * degrees.
	 * 
	 * @return An anonymous PIDSource.
	 */
	public static PIDSource getTurnReader() {
		return WheelEncoders::getTurn;
	}

}
