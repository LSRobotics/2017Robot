package frc.team5181.robot.actuators;

import edu.wpi.first.wpilibj.Spark;
import frc.team5181.robot.Statics;

/**
 * BallShoot
 * 2017Robot
 *
 * Created by Tyler on 2017/01/27.
 * Copyright © 2017 Tyler Liu. All rights reserved.
 */
public class BallShoot {

    static OnOffActuator shootMotor;

    public static void init(){
        shootMotor = new OnOffActuator(new Spark(Statics.BallShoot), false);
    }

    public static void set(double triggerAxis) {
        shootMotor.set(-triggerAxis);
    }
}
