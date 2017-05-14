package frc.team5181.robot.actuators;

import edu.wpi.first.wpilibj.Spark;
import frc.team5181.robot.Statics;

/**
 * Winch
 * 2017Robot
 *
 * Created by Tyler on 2017/01/27.
 * Copyright © 2017 Tyler Liu. All rights reserved.
 */
public class Winch {

    private static OnOffActuator winchMotor;

    public static void init(){
        winchMotor = new OnOffActuator(new Spark(Statics.Winch), false);
    }

    public static void set(boolean buttonState) {
        winchMotor.set(buttonState);
    }
    public static void set(double buttonState) {
        winchMotor.set(buttonState);
    }
}
