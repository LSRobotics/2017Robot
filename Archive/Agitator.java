package frc.team5181.robot.actuators;

import edu.wpi.first.wpilibj.Relay;
import frc.team5181.robot.Statics;

/**
 *
 * Created by LSExplorers on 2/13/2017.
 * Disfunctional component.
 */
public class Agitator {

    static Relay agitatorMotor;
    private static boolean isOn;
    private static boolean isPressed;
    private static long startTime;
    private static long period;


    public static void init(long Iperiod){
        agitatorMotor = new Relay(Statics.Agitator);
        isOn = false;
        isPressed = false;
        period = Iperiod;
    }

    public static void set(boolean buttonState) {
        if (buttonState && !isPressed){
            isPressed = true;
            isOn = !isOn;
            if (isOn) startTime = System.currentTimeMillis();
        }
        if (!buttonState && isPressed){
            isPressed = false;
        }
        if (isOn) setOscillate();
    }

    public static void setOscillate(){
        long time = (System.currentTimeMillis() - startTime) % period;
        if (time < period / 2) agitatorMotor.set(Relay.Value.kForward);
        else agitatorMotor.set(Relay.Value.kReverse);
    }
}
