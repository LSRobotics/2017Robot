package frc.team5181.robot.autonomous;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team5181.robot.UnitConvert;
import frc.team5181.robot.actuators.DriveTrain;
import frc.team5181.robot.pid.PIDConstantPreset;
import frc.team5181.robot.pid.PIDController;
import frc.team5181.robot.sensors.NavX;
import frc.team5181.robot.sensors.RotationDetecter;
import frc.team5181.robot.sensors.UltrasonicGroup;
import frc.team5181.robot.sensors.WheelEncoders;
import frc.team5181.robot.tasking.SyncTask;
import frc.team5181.robot.tasking.TaskSequence;

/**
 * Created by TylerLiu on 2017/04/06.
 * HookGear by Ultrasonic and Side-Encoder
 */
public class HookGearUSE extends TaskSequence{
    private PIDController moveForward;
    private PIDController aimHooker;
    //private PIDController turn120Degrees2;
    private PIDController rushToLift;

    public HookGearUSE(int position, boolean leftSide) {
        super();
        initPIDs(position, leftSide);
        this.add(new SyncTask(() -> RotationDetecter.setDetectorUsage(false, true)));
        this.add(
                // Move forward
                new SyncTask(() -> SmartDashboard.putString("AutonomousPhase", "Move forward.")),
                moveForward
        );

        this.add(
                // Turn 60 degrees
                new SyncTask(() -> SmartDashboard.putString("AutonomousPhase", "Turn 120 degrees.")),
                new SyncTask(NavX::reset),
                aimHooker,
                //new SyncTask(NavX::reset),
                //turn120Degrees2,

                new SyncTask(() -> SmartDashboard.putString("AutonomousPhase", "Go for the gear.")),
                rushToLift
        );
    }

    private void initPIDs(int position, boolean leftSide){

        moveForward = new PIDController()
                .addSource( UltrasonicGroup.getsensor(UltrasonicGroup.UltrasonicPosition.FRONT_LEFT),
                        UltrasonicGroup.getsensor(UltrasonicGroup.UltrasonicPosition.FRONT_RIGHT))
                .useCombiner(readings -> (readings.get(0) + readings.get(1))/2)
                .useTranslator(reading -> {
                    SmartDashboard.putNumber("Error", calcDriveDist(position == 1 ? true : false, UnitConvert.inchToCentimeter(73.125),reading));
                    SmartDashboard.putNumber("EncoderRaw", reading);
                    return reading - (calcDriveDist(position == 1 ? true : false, UnitConvert.inchToCentimeter(73.125), reading));
                })
                .withPIDConstantPreset(PIDConstantPreset.DriveTranslational)
                .withAcceptableError(2.5)
                .neverEnd(false)
                .outputTo(DriveTrain.getTranslationalAdjuster(), a -> a / 100);

        aimHooker = leftSide ? 
        			new PIDController()
				.setSource(WheelEncoders.getTurnReader())
				.useTranslator(reading -> Math.PI / 180 * (120) * (position == 1 ? -1 : 1) - reading)
				.withPIDConstantPreset(PIDConstantPreset.DriveRotational)
				.withAcceptableError(Math.PI / 30)
				.outputTo(DriveTrain.getAngleAdjuster()) : 
					new PIDController()
				.setSource(WheelEncoders.getTurnReader())
				.useTranslator(reading -> reading - Math.PI / 180 * (120) * (position == 1 ? -1 : 1))
				.withPIDConstantPreset(PIDConstantPreset.DriveRotational)
				.withAcceptableError(Math.PI / 30)
				.outputTo(DriveTrain.getAngleAdjuster()); 

        rushToLift = new PIDController()
                .addSource( UltrasonicGroup.getsensor(UltrasonicGroup.UltrasonicPosition.FRONT_LEFT),
                        UltrasonicGroup.getsensor(UltrasonicGroup.UltrasonicPosition.FRONT_RIGHT))
                .useCombiner(readings -> (readings.get(0) + readings.get(1))/2)
                .useTranslator(current -> current - UnitConvert.inchToCentimeter(4.0))
                .withPIDConstantPreset(PIDConstantPreset.DriveTranslational)
                .withAcceptableError(2.5)
                .neverEnd(false)
                .outputTo(DriveTrain.getTranslationalAdjuster(), a -> a / 100);
    }
    
	private double calcDriveDist(boolean isLeftSide, double sideDist, double backDist) {
		double[] turningPoint = FieldEquations.calcRobotTurnPoint(sideDist,isLeftSide,false);
		return FieldEquations.getLiftAlignDistance(backDist, turningPoint[1], true);
	}
}
