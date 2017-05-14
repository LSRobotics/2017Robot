package frc.team5181.robot.sensors;

import frc.team5181.robot.pid.PIDSource;

/**
 * Created by TylerLiu on 2017/03/17.
 */
public class RotationDetecter{

    static boolean useEncoder;
    static boolean useNavX;

    public static void setDetectorUsage(boolean useEncoder1, boolean useNavX1){
        useEncoder = useEncoder1;
        useNavX = useNavX1;
    }

    public static void setDetectorUsage(Usage u){
        switch (u){
            case useEncoder:
                setDetectorUsage(true, false);
            case useNavX:
                setDetectorUsage(false, true);
            case useBoth:
                setDetectorUsage(true, true);
        }
    }

    public static double getRotation(){
        if (useNavX && useEncoder){
            return (WheelEncoders.getTurn() + NavX.getRotation() / 180 * Math.PI) / 2;
        }
        if (useNavX){
            return NavX.getRotation() / 180 * Math.PI;
        }
        if (useEncoder){
            return WheelEncoders.getTurn();
        }

        return Double.NaN;
    }

    public static void reset(){
        if (useNavX){
            NavX.reset();
        }
        if (useEncoder){
            WheelEncoders.reset();
        }
    }

    public static enum Usage{ useEncoder, useNavX, useBoth }

    public static PIDSource getPIDSource(){
        return RotationDetecter::getRotation;
    }
}
