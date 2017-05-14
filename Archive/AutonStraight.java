package frc.team5181.robot.autonomous;

import edu.wpi.first.wpilibj.DriverStation;
import frc.team5181.robot.actuators.DriveTrain;
import frc.team5181.robot.pid.PIDConstantPreset;
import frc.team5181.robot.pid.PIDController;
import frc.team5181.robot.sensors.WheelEncoders;
import frc.team5181.robot.tasking.SyncTask;
import frc.team5181.robot.tasking.TaskSequence;

/**
 * Created by TylerLiu on 2017/03/18.
 * integrated into HookGearPath
 */
public class AutonStraight extends TaskSequence {

    PIDController driveStraight;

    public AutonStraight() {
        super();
        initPID();
        this.add(
                new SyncTask(WheelEncoders::reset),
                driveStraight
        );
    }

    public void initPID() {
        driveStraight = new PIDController()
                .setSource(WheelEncoders.getDistanceReader())
                .useTranslator(reading -> {
                    DriverStation.reportError("" + reading, false);
                    return 200 - reading;
                })
                .withPIDConstantPreset(PIDConstantPreset.DriveTranslational)
                .withAcceptableError(1.0)
                .neverEnd(false)
                .outputTo(DriveTrain.getTranslationalAdjuster(), a -> a / 100);
    }
}
