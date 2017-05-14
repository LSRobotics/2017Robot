package frc.team5181.robot.actuators;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import frc.team5181.robot.Gamepad;
import frc.team5181.robot.Statics;

/**
 * Created by TylerLiu on 2017/03/13.
 * compressor object and solenoid for intake?
 */
public class Pneumatics {
    static Compressor compressor;
    static DoubleSolenoid solenoid;

    public static void init(){
        compressor = new Compressor(Statics.Compressor);
        solenoid = new DoubleSolenoid(Statics.Solenoid_Forward, Statics.Solenoid_Reverse);
    }

    public static void updateByDPAD(){
        switch (Gamepad.D_PAD_STATE){
            case -1:
                solenoid.set(DoubleSolenoid.Value.kOff);
                break;
            case 0:
                solenoid.set(DoubleSolenoid.Value.kForward);
                break;
            case 180:
                solenoid.set(DoubleSolenoid.Value.kReverse);
                break;
            default:
                break;
        }

    }

    public static void updateByButton(boolean forward, boolean reverse){
        if ( (!forward && !reverse) || (forward && reverse)) // if both false or both true
                solenoid.set(DoubleSolenoid.Value.kOff);
        else if (forward)
                solenoid.set(DoubleSolenoid.Value.kForward);
        else // if reverse
                solenoid.set(DoubleSolenoid.Value.kReverse);

    }
}
