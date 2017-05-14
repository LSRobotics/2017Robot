package frc.team5181.robot.actuators;

import edu.wpi.first.wpilibj.Victor;
import frc.team5181.robot.Statics;

/**
 * Hopper
 * 2017Robot
 *
 * Created by Tyler on 2017/01/27.
 * Copyright © 2017 Tyler Liu. All rights reserved.
 */
public class Intake {
    static OnOffActuator intakeMotor;

    public static void init(){
        intakeMotor = new OnOffActuator(new Victor(Statics.Intake));
    }

    public static void set(boolean buttonState) {
        intakeMotor.set(buttonState);
    }
}
