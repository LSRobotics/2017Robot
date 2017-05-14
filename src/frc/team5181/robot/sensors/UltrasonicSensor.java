package frc.team5181.robot.sensors;

import edu.wpi.first.wpilibj.Ultrasonic;
import frc.team5181.robot.pid.PIDSource;
import frc.team5181.robot.sensors.UltrasonicGroup.UltrasonicPosition;


public class UltrasonicSensor extends Ultrasonic implements PIDSource {
	UltrasonicPosition position;

    public UltrasonicSensor (UltrasonicPosition position){
        super(position.getTrig(), position.getEcho(), Ultrasonic.Unit.kMillimeters);
        this.position = position;
    }

	@Override
	public double pidYield() {
		return UltrasonicGroup.getDistances(position);
	}

}