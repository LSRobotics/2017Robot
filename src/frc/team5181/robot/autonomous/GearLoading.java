package frc.team5181.robot.autonomous;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team5181.robot.UnitConvert;
import frc.team5181.robot.actuators.DriveTrain;
import frc.team5181.robot.pid.PIDConstantPreset;
import frc.team5181.robot.pid.PIDController;
import frc.team5181.robot.sensors.UltrasonicGroup;
import frc.team5181.robot.sensors.WheelEncoders;
import frc.team5181.robot.tasking.SyncTask;
import frc.team5181.robot.tasking.TaskSequence;

/**
 * Autonomous for gear loading
 */
public class GearLoading extends TaskSequence{

    public PIDController alignFront,
                         alignTarget,
                         turning,
                         rushDocking;

    /**
     * use position A for now
     */
    public GearLoading(){
        super();
        initPIDs();
        add(
                new SyncTask(() -> {
                    SmartDashboard.putString("AutonomousPhase", "Align");
                    WheelEncoders.reset();
                }),
                alignFront,

                new SyncTask(() ->
                    SmartDashboard.putString("AutonomousPhase", "Align vertically")),
                alignTarget,

                new SyncTask(() -> {
                    SmartDashboard.putString("AutonomousPhase", "Turn to loading station");
                    WheelEncoders.reset();
                }),
                turning,

                new SyncTask(() -> {
                    SmartDashboard.putString("AutonomousPhase", "rushing");
                    WheelEncoders.reset();
                }),
                rushDocking
        );

    }

    private void initPIDs(){
        alignFront = new PIDController()
                .addSource( UltrasonicGroup.getsensor(UltrasonicGroup.UltrasonicPosition.FRONT_LEFT),
                        UltrasonicGroup.getsensor(UltrasonicGroup.UltrasonicPosition.FRONT_RIGHT))
                .useCombiner(readings -> Math.atan((readings.get(0) - readings.get(1)) / FieldEquations.BackWheelAxisLength))
                .useTranslator(reading -> -reading)
                .withPIDConstantPreset(PIDConstantPreset.DriveRotational)
                .withAcceptableError(Math.PI / 30)
                .outputTo(DriveTrain.getAngleAdjuster(), a -> a / 100);
        alignTarget = new PIDController()
                .addSource( UltrasonicGroup.getsensor(UltrasonicGroup.UltrasonicPosition.FRONT_LEFT),
                        UltrasonicGroup.getsensor(UltrasonicGroup.UltrasonicPosition.FRONT_RIGHT),
                UltrasonicGroup.getsensor(DriverStation.getInstance().getAlliance() == DriverStation.Alliance.Red
                        ? UltrasonicGroup.UltrasonicPosition.LEFT
                        : UltrasonicGroup.UltrasonicPosition.RIGHT
                ))
                .useCombiner(readings -> calcAlignDist(readings.get(2), (readings.get(0) + readings.get(1)) / 2))
                .withPIDConstantPreset(PIDConstantPreset.DriveTranslational)
                .withAcceptableError(1.0)
                .neverEnd(false)
                .outputTo(DriveTrain.getTranslationalAdjuster(), a -> a / 100);
        turning = new PIDController()
                .setSource(WheelEncoders.getTurnReader())
                .useTranslator(reading -> Math.PI / 180 * (90 - 25.54) *
                        (DriverStation.getInstance().getAlliance() == DriverStation.Alliance.Blue ? -1 : 1) - reading)
                .withPIDConstantPreset(PIDConstantPreset.DriveRotational)
                .withAcceptableError(Math.PI / 30)
                .outputTo(DriveTrain.getAngleAdjuster(), a -> a / 100);

        rushDocking = new PIDController()
                .addSource( UltrasonicGroup.getsensor(UltrasonicGroup.UltrasonicPosition.FRONT_LEFT),
                        UltrasonicGroup.getsensor(UltrasonicGroup.UltrasonicPosition.FRONT_RIGHT))
                .useCombiner(readings -> (readings.get(0) + readings.get(1))/2)
                .useTranslator(current -> current - UnitConvert.inchToCentimeter(4.0))
                .withPIDConstantPreset(PIDConstantPreset.DriveTranslational)
                .withAcceptableError(2.5)
                .neverEnd(false)
                .outputTo(DriveTrain.getTranslationalAdjuster(), a -> a / 100);
    }

    static double calcAlignDist(double horizontal, double vertical){
        return FieldEquations.getVerticalLoadPositionA(horizontal) - vertical;
    }
}
