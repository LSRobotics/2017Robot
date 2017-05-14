package frc.team5181.robot.sensors;

import frc.team5181.robot.Statics;

/**
 * Represents all IR sensors present on the robot.
 */
public class IRSensorGroup {
	
	/**
	 * Each element in this enum represents a position for an IR sensor.
	 */
	public enum IRSensorPosition {
		BACK_LEFT       ( Statics.CHANNEL_BACK_LEFT_IR  , IRSensorType.LONGRANGE ),
        BACK_RIGHT      ( Statics.CHANNEL_BACK_RIGHT_IR , IRSensorType.LONGRANGE );

		private int channel;
		private IRSensorType type;
		
		/**
		 * Get the channel the sensor on this position uses.
		 * @return The channel the sensor on this position uses
		 */
		public int getChannel() {
			return this.channel;
		}

		public IRSensorType getType(){
		    return this.type;
        }
		
        IRSensorPosition(int channel, IRSensorType type) {
			this.channel = channel;
			this.type = type;
		}

	}

	public enum IRSensorType {
		MIDRANGE, //GP2Y0A02YK0F, 20-150cm
		LONGRANGE, //GP2Y0A710K0F, 100-550cm
		SHORT_RANGE
	}

	private static IRSensor[] sensors = new IRSensor[IRSensorPosition.values().length];

	public static void init() {
		for (IRSensorPosition sensorPosition : IRSensorPosition.values()) {
			sensors[sensorPosition.ordinal()] = new IRSensor(sensorPosition);
		}
	}
	
	/**
	 * Get the instance of the IRSensor in the given position.
	 * @param position The position of the IRSensor we are looking for
	 * @return The instance of the IRSensor.
	 */
	public static IRSensor getSensor(IRSensorPosition position) {
		return sensors[position.ordinal()];
	}
	
	/**
	 * Get the all sensors exist.
	 * @return A map of all sensors exist.
	 */
	public static IRSensor[] getSensors() {
		return sensors;
	}

	public static double[] getDistances(){
		double[] lastMeasurement = new double[IRSensorPosition.values().length];
		for (IRSensorPosition sensorPosition : IRSensorPosition.values()){
			lastMeasurement[sensorPosition.ordinal()] = sensors[sensorPosition.ordinal()].readDistance() - 
					(sensorPosition.equals(IRSensorPosition.BACK_LEFT)? 6.25 : 0);
		}
		return lastMeasurement;
	}

	
}
