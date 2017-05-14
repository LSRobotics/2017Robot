package frc.team5181.robot.sensors;

import edu.wpi.first.wpilibj.DriverStation;

/**
 * Created by TylerLiu on 2017/03/03.
 */
public class Calibration {

    public static void periodic(){
        double[] measurement = IRSensorGroup.getDistances();
        DriverStation.reportError("IRLeft" + measurement[0], false);
        DriverStation.reportError("IRRight" + measurement[1], false);
        measurement = UltrasonicGroup.getDistances();
        DriverStation.reportError("Ultrasonic Left" + measurement[0], false);
        DriverStation.reportError("Ultrasonic Right" + measurement[1], false);
        DriverStation.reportError("Ultrasonic Back Left" + measurement[2], false );
        DriverStation.reportError("Ultrasonic Back Right" + measurement[3], false);
    }
}
