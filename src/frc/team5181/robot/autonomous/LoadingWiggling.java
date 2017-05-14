package frc.team5181.robot.autonomous;

import frc.team5181.robot.UnitConvert;
import frc.team5181.robot.actuators.DriveTrain;
import frc.team5181.robot.pid.PIDConstantPreset;
import frc.team5181.robot.pid.PIDController;
import frc.team5181.robot.sensors.UltrasonicGroup;
import frc.team5181.robot.sensors.WheelEncoders;
import frc.team5181.robot.tasking.SyncTask;
import frc.team5181.robot.tasking.TaskSequence;

/**
 * for wiggling left or right during loading
 */
public class LoadingWiggling extends TaskSequence{

    private final double changeAngle = Math.PI / 4;

    private PIDController   backUp,
                            align,
                            moveBack;

    public LoadingWiggling(){
        super();
        super.add(
                new SyncTask(WheelEncoders::reset),
                backUp,
                align,
                moveBack);
    }

    public void initPID(boolean isToLeft){
        backUp = new PIDController()
                .setSource(WheelEncoders.getTurnReader())
                .useTranslator(reading -> (isToLeft? -1 : 1) * changeAngle - reading)
                .withPIDConstantPreset(PIDConstantPreset.DriveOneside)
                .withAcceptableError(Math.PI / 30)
                .outputTo(DriveTrain.getOneSideAdjuster(!isToLeft), a -> a / 100);
        align = new PIDController()
                .setSource(WheelEncoders.getTurnReader())
                .useTranslator(reading -> -reading)
                .withPIDConstantPreset(PIDConstantPreset.DriveOneside)
                .withAcceptableError(Math.PI / 30)
                .outputTo(DriveTrain.getOneSideAdjuster(isToLeft), a -> a / 100);
        moveBack = new PIDController()
                .addSource( UltrasonicGroup.getsensor(UltrasonicGroup.UltrasonicPosition.FRONT_LEFT),
                        UltrasonicGroup.getsensor(UltrasonicGroup.UltrasonicPosition.FRONT_RIGHT))
                .useCombiner(readings -> (readings.get(0) + readings.get(1))/2)
                .useTranslator(current -> current - UnitConvert.inchToCentimeter(4.0))
                .withPIDConstantPreset(PIDConstantPreset.DriveTranslational)
                .withAcceptableError(2.5)
                .neverEnd(false)
                .outputTo(DriveTrain.getTranslationalAdjuster(), a -> a / 100);
    }
}
