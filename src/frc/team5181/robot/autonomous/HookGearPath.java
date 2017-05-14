package frc.team5181.robot.autonomous;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team5181.robot.actuators.DriveTrain;
import frc.team5181.robot.UnitConvert;
import frc.team5181.robot.pid.PIDConstantPreset;
import frc.team5181.robot.sensors.UltrasonicGroup;
import frc.team5181.robot.sensors.UltrasonicGroup.UltrasonicPosition;
import frc.team5181.robot.sensors.WheelEncoders;
import frc.team5181.robot.pid.PIDController;
import frc.team5181.robot.tasking.SyncTask;
import frc.team5181.robot.tasking.TaskSequence;

public class HookGearPath extends TaskSequence {

	private PIDController moveForward;
	private PIDController turn60Degrees;
	private PIDController rushToLift;

	public HookGearPath(int position) {
		super();
		initPIDs(position);
		this.add(new SyncTask(WheelEncoders::reset));
		this.add(
			// Move forward
			new SyncTask(() -> SmartDashboard.putString("AutonomousPhase", "Move forward.")),
			moveForward
		);

		if (position != 2) this.add(
			// Turn 60 degrees
			new SyncTask(() -> SmartDashboard.putString("AutonomousPhase", "Turn 60 degrees.")),
			new SyncTask(WheelEncoders::reset),
			turn60Degrees,
			
			new SyncTask(() -> SmartDashboard.putString("AutonomousPhase", "Go for the gear.")),
         	rushToLift
		);
	}

	private void initPIDs(int position){

		moveForward = new PIDController()
				.setSource(WheelEncoders.getDistanceReader())
				.useTranslator(reading -> {
					SmartDashboard.putNumber("Error", UnitConvert.inchToCentimeter(116) - 51.55 - reading);
					SmartDashboard.putNumber("EncoderRaw", reading);
					if (position == 2)return UnitConvert.inchToCentimeter(118) - 72.5 - reading;
					return UnitConvert.inchToCentimeter(129) - 72.5 - reading;
				})
				.withPIDConstantPreset(PIDConstantPreset.DriveTranslational)
				.withAcceptableError(1.0)
				.neverEnd(false)
				.outputTo(DriveTrain.getTranslationalAdjuster(), a -> a / 100);

		turn60Degrees = new PIDController()
				.setSource(WheelEncoders.getTurnReader())
				.useTranslator(reading -> Math.PI / 180 * (60 + 7) * (position == 1 ? -1 : 1) - reading)
				.withPIDConstantPreset(PIDConstantPreset.DriveRotational)
				.withAcceptableError(Math.PI / 30)
				.outputTo(DriveTrain.getAngleAdjuster());

		rushToLift = new PIDController()
				.addSource( UltrasonicGroup.getsensor(UltrasonicPosition.FRONT_LEFT),
						UltrasonicGroup.getsensor(UltrasonicPosition.FRONT_RIGHT))
				.useCombiner(readings -> (outputReading(readings.get(0)) + outputReading(readings.get(1)))/2)
				.useTranslator(current -> current - UnitConvert.inchToCentimeter(4.0))
				.withPIDConstantPreset(PIDConstantPreset.DriveTranslational)
				.withAcceptableError(2.5)
				.neverEnd(false)
				.outputTo(DriveTrain.getTranslationalAdjuster(), a -> 0.5/*a / 100*/);
	}
	
	private double outputReading(double reading){
		DriverStation.reportError(""+reading, false);
		return reading;
	}
}
