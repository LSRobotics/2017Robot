package frc.team5181.robot.pid;

import frc.team5181.robot.Gamepad;
import frc.team5181.robot.actuators.DriveTrain;
import frc.team5181.robot.sensors.WheelEncoders;

/**
 * Created by TylerLiu on 2017/03/18.
 */
public class PIDTest {
    private static PIDController controller;
    private static boolean POVPressed;
    private static boolean isReverse;

    public static void translationalInit(){
        isReverse = false;
        WheelEncoders.reset();
        controller = new PIDController();
        POVPressed = false;
        controller
                .setSource(WheelEncoders.getDistanceReader())
                .useTranslator(reading -> (isReverse ? -1 : 1) * 100 - reading)
                .neverEnd(true)
                .withAcceptableError(1.0)
                .withPIDConstantPreset(PIDConstantPreset.ZieglerTrans)
                .outputTo(DriveTrain.getTranslationalAdjuster(), error -> error / 100);
    }

    public static void testingPeriodic(){
        controller.update();
        if (Gamepad.D_PAD_STATE == -1) {
                POVPressed = false;
        } else {
            if (POVPressed) return;
            POVPressed = true;
            isReverse = !isReverse;
            DriveTrain.tankDrive(0, 0);
            WheelEncoders.reset();
            controller.reset();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }

    }

    public static void rotationalInit(){
        controller = new PIDController();
        POVPressed = false;
        controller
                .setSource(WheelEncoders.getTurnReader())
                .neverEnd(true)
                .withAcceptableError(Math.PI / 20)
                .withPIDConstantPreset(PIDConstantPreset.ZieglerRot)
                .outputTo(DriveTrain.getAngleAdjuster());
    }

}
