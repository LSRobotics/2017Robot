package frc.team5181.robot.sensors;

import edu.wpi.first.wpilibj.AnalogInput;
import frc.team5181.robot.pid.PIDSource;
import frc.team5181.robot.sensors.IRSensorGroup.IRSensorPosition;
import frc.team5181.robot.sensors.IRSensorGroup.IRSensorType;

/**
 * Represents a single IR sensor on the robot. Basically a wrapper around AnalogInput.
 */
public class IRSensor implements PIDSource {
	private AnalogInput analogInput;
	private IRSensorType irSensorType;

	public IRSensor(int channel, IRSensorType irSensorType) {
		this.analogInput = new AnalogInput(channel);
		this.irSensorType = irSensorType;
	}
	
	public double getRawVoltage() {
		return this.analogInput.getVoltage();
	}

	/*package*/ IRSensor(IRSensorPosition position) {
	    this.analogInput = new AnalogInput(position.getChannel());
	    this.irSensorType = position.getType();
    }
	/**
	 * Get the reading in terms of distance of this IRSensor.
	 * @return The reading in terms of distance of this IRSensor.
	 */ 
	public double readDistance() {
		return voltageToDistance(this.analogInput.getAverageVoltage());
	}

	public double voltageToDistance(double voltage){
		// Convert voltage data to Distance with a formula
		if (irSensorType.equals(IRSensorType.MIDRANGE)) {
			return 73.49526576 * Math.pow(voltage, -0.935) - 10;
		}
		if (irSensorType.equals(IRSensorType.LONGRANGE)){
			//     |<------------------ Centimeter ------------------->|
			//     |      |<------------------ Inch ------------------>|
			return 2.54 * (430.75 * Math.pow(voltage, -3.7031) + 23.645);
		}
		return 0;
	}

	@Override
	public double pidYield() {
		return this.readDistance();
	}

}
