package frc.team5181.robot.sensors;

import com.kauailabs.navx.frc.AHRS;
import frc.team5181.robot.pid.PIDSource;
import edu.wpi.first.wpilibj.SPI;

/**
 * Created by TylerLiu on 2017/03/07.
 */
public class NavX implements PIDSource{
    private static AHRS ahrs;
    private static double setPoint;

    //Variables for Collision
    static double AccX, AccY, PrevAccX, PrevAccY;
    final static double Collision_DealtaG = 1f;

    public static void init(){
        ahrs = new AHRS(SPI.Port.kMXP);
    }

    // calibrate the sensor
    //corrected, using wheel side as X, original Z as new Y
    static float getCompassHeading() { // in degrees
        return ahrs.getCompassHeading();
    }

    static AHRS getNavX(){
        return ahrs;
    }

    static void setSetPoint(){
        setPoint = ahrs.getCompassHeading();
    }


    @Override
    public double pidYield() {
        if (Math.abs(NavX.getCompassHeading() - setPoint) <= 180) return NavX.getCompassHeading() - setPoint;
        if (NavX.getCompassHeading() - setPoint > 180) return NavX.getCompassHeading() - setPoint - 360;
        return NavX.getCompassHeading() - setPoint + 360;
    }

    public static boolean hadCollision() {
        AccX = ahrs.getWorldLinearAccelX();
        AccY = ahrs.getWorldLinearAccelY();

        double JerkX = AccX - PrevAccX;
        double JerkY = AccY - PrevAccY;

        PrevAccX = AccX;
        PrevAccY = AccY;

        return !(Math.abs(JerkX) < Collision_DealtaG || Math.abs(JerkY) < Collision_DealtaG);
    }

    public static double getRotation() {
        return ahrs.getPitch();
    }

    public static double[] get3DRotations() {
    	//gyro rotation X
        return new double[]{ahrs.getRawMagX(), ahrs.getRawMagY(), ahrs.getRawMagZ()};
    }


    public static double[] getDisplacement() {
        return new double[] {ahrs.getDisplacementX(), ahrs.getDisplacementY(), ahrs.getDisplacementZ()};
    }

    public static void reset() {
        ahrs.reset();
    }

    public static PIDSource getPIDSource(){
        return NavX::getRotation;
    }

}
