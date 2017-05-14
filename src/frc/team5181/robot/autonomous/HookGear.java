package frc.team5181.robot.autonomous;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team5181.robot.actuators.DriveTrain;
import frc.team5181.robot.UnitConvert;
import frc.team5181.robot.pid.PIDConstantPreset;
import frc.team5181.robot.sensors.IRSensorGroup;
import frc.team5181.robot.sensors.IRSensorGroup.IRSensorPosition;
import frc.team5181.robot.sensors.UltrasonicGroup;
import frc.team5181.robot.sensors.UltrasonicGroup.UltrasonicPosition;
import frc.team5181.robot.sensors.WheelEncoders;
import frc.team5181.robot.pid.PIDController;
import frc.team5181.robot.tasking.SyncTask;
import frc.team5181.robot.tasking.TaskSequence;

public class HookGear extends TaskSequence {

	private PIDController moveForward;
	private PIDController adjustForward;
	private PIDController turn60Degrees;
	private PIDController rushToLift;

	public HookGear(int position) {
		super();
		initPIDs(position);
		this.add(new SyncTask(WheelEncoders::reset));

		if (position != 2) {
			this.add(
					// Move forward
					new SyncTask(() -> SmartDashboard.putString("AutonomousPhase", "Move forward.")),
					moveForward,
                    new SyncTask(() -> SmartDashboard.putString("AutonomousPhase", "Adjust Position.")),
					adjustForward,

					// Turn 60 degrees
					new SyncTask(() -> SmartDashboard.putString("AutonomousPhase", "Turn 60 degrees.")),
					new SyncTask(WheelEncoders::reset),
					turn60Degrees
			);
		}

			// Go for the gear
			this.add(
					new SyncTask(() -> SmartDashboard.putString("AutonomousPhase", "Go for the gear.")),
                 	rushToLift
			);
	}

	private void initPIDs(int position){

		moveForward = new PIDController()
				.setSource(WheelEncoders.getDistanceReader())
				.useTranslator(reading ->
					reading < 115 ? 200 - reading : 0
				)
				.withPIDConstantPreset(PIDConstantPreset.DriveTranslational)
				.withAcceptableError(1.0)
				.neverEnd(false)
				.outputTo(DriveTrain.getTranslationalAdjuster(), a -> a / 100);

		adjustForward = new PIDController()
				.addSource( IRSensorGroup.getSensor(IRSensorPosition.BACK_LEFT),
						IRSensorGroup.getSensor(IRSensorPosition.BACK_RIGHT),
						WheelEncoders.getDistanceReader(),
						UltrasonicGroup.getsensor(position == 1
								? UltrasonicPosition.LEFT
								: UltrasonicPosition.RIGHT
						))
				.useCombiner(readings -> calcDriveDist(position == 1, readings.get(3), (readings.get(0) + /*readings.get(1) + */readings.get(2)) / /*3*/2))
				.withPIDConstantPreset(PIDConstantPreset.DriveTranslational)
				.withAcceptableError(1.0)
				.neverEnd(false)
				.outputTo(DriveTrain.getTranslationalAdjuster(), a -> a / 100);

		turn60Degrees = new PIDController()
				.setSource(WheelEncoders.getTurnReader())
				.useTranslator(reading -> Math.PI / 3 * (position == 1 ? -1 : 1) - reading)
				.withPIDConstantPreset(PIDConstantPreset.DriveRotational)
				.withAcceptableError(Math.PI / 30)
				.outputTo(DriveTrain.getAngleAdjuster());

		rushToLift = new PIDController()
				.addSource( UltrasonicGroup.getsensor(UltrasonicPosition.FRONT_LEFT),
						UltrasonicGroup.getsensor(UltrasonicPosition.FRONT_RIGHT))
				.useCombiner(readings -> (readings.get(0) + readings.get(1))/2)
				.useTranslator(current -> current - UnitConvert.inchToCentimeter(4))
				.withPIDConstantPreset(PIDConstantPreset.DriveTranslational)
				.withAcceptableError(2.5)
				.neverEnd(false)
				.outputTo(DriveTrain.getTranslationalAdjuster(), a -> a / 100);
	}

	private double calcDriveDist(boolean isLeftSide, double sideDist, double backDist) {
		double[] turningPoint = FieldEquations.calcRobotTurnPoint(sideDist,isLeftSide);
		return FieldEquations.getLiftAlignDistance(backDist, turningPoint[1]);
	}
}
