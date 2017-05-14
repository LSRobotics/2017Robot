package frc.team5181.robot.actuators;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team5181.robot.Statics;
import frc.team5181.robot.pid.PIDTarget;

import java.text.DecimalFormat;

public class DriveTrain {

    private static double speedLimit;
    private static boolean isBumperPressed; //is speed limit change button pressed
    private static VictorSP leftFront;
    private static VictorSP leftBack;
    private static VictorSP rightFront;
    private static VictorSP rightBack;
    private static RobotDrive drive;
    private static DecimalFormat format; //for sending of the numbers

    public static void init(double initSpeedLimit) {
        leftFront = new VictorSP(Statics.LEFTPortFront);
        leftBack = new VictorSP(Statics.LEFTPortBack);
        rightFront = new VictorSP(Statics.RIGHTPortFront);
        rightBack = new VictorSP(Statics.RIGHTPortBack);

        drive = new RobotDrive(leftFront, leftBack, rightFront, rightBack);

        speedLimit = initSpeedLimit;
        format = (DecimalFormat) DecimalFormat.getNumberInstance();
        format.applyPattern("0.##");
    }

    /**
     * arcadeDrive method, Single joystick to control robot drive
     *
     * @param x double rotation magnitude of turning axis
     * @param y double drive magnitude of driving axis
     */
    public static void arcadeDrive(double x, double y) {
        System.out.println(x + ", " + y);
        drive.arcadeDrive(-limitSpeed(y), -(limitSpeed(Math.abs(y) >= 0.5 ? x * 0.6 : x)));
    }

    /**
     * tankDrive method
     *
     * @param left  double left-stick
     * @param right double right-stick
     */
    public static void tankDrive(double left, double right) {
        drive.tankDrive(limitSpeed(left), limitSpeed(right));
    }

    /**
     * The devised algorithm for limiting the speed.
     *
     * @param a input of the controller
     * @return the speed after limited
     */
    private static double limitSpeed(double a) {
        return a / Math.abs(a) * Math.min(Math.abs(a),speedLimit);
    }

    public static void updateSpeedLimit(boolean increase, boolean decrease, boolean stop) {
        if (stop) {
            speedLimit = 0;
        }
        //Speed Limit Control
        if (increase && !decrease && !isBumperPressed) {
            speedLimit = Math.min(speedLimit + 0.05, 1);
            isBumperPressed = true;
        }
        if (decrease && !increase && !isBumperPressed) {
            speedLimit = Math.max(speedLimit - 0.05, 0);
            isBumperPressed = true;
        }
        if (!decrease && !increase && isBumperPressed) {
            isBumperPressed = false;
        }
        SmartDashboard.putString("Speed Limit", format.format(speedLimit));
    }

    /**
     * Updates the speed limit
     *
     * @param newSpeedLimit the new speed limit
     */
    public static void updateSpeedLimit(double newSpeedLimit) {
        speedLimit = newSpeedLimit;
    }

    private static double mapSpeed(double amount) {
        return (Math.abs(amount) < 0.001) ? 0 : (amount > 0 ? 1 : -1) * (Math.min(0.7, Math.abs(amount) * 0.4 + 0.4));
    }

    public static PIDTarget getTranslationalAdjuster() {
        return amount -> {
            double mapped = mapSpeed(amount);
            //System.out.println("Amount: " + amount + " & Mapped: " + mapped); // TODO: REMOVE THIS
            //drive.tankDrive(mapped, mapped);
            drive.tankDrive(mapped, mapped /*+ 0.05*/);
        };
    }

    public static PIDTarget getAngleAdjuster() {// left as positive!
        return amount -> {
            double mapped = mapSpeed(amount);
            drive.tankDrive(-mapped, mapped);
        };
    }

    public static PIDTarget getLeftAdjuster() {
        return amount -> drive.tankDrive(-mapSpeed(amount), 0);
    }

    public static PIDTarget getRightAdjuster() {
        return amount -> drive.tankDrive(0, mapSpeed(amount));
    }

    public static PIDTarget getOneSideAdjuster(boolean isLeft) {
        return isLeft ? getLeftAdjuster() : getRightAdjuster();
    }
}
