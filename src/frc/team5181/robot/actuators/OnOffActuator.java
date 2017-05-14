package frc.team5181.robot.actuators;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PWMSpeedController;
import edu.wpi.first.wpilibj.Relay;

/**
 * OnOffActuator
 * 2017Robot
 *
 * This is a class for passing an on/off status to 
 * any one motor/relay object that inherit this class
 *
 * Have to use PWMSpeedController or Relay
 * 
 * Value: 1.0 or 0.0 / kOn or kOff
 * 
 * Created by Tyler on 2017/01/27.
 * Copyright © 2017 Tyler Liu. All rights reserved.
 */
public class OnOffActuator {
    private Object actuator;
    private boolean isOn;
    private boolean isPressed;
    private boolean isPWM;
    private boolean isSwitch; // does it uses switch on/ switch of mechanism

    public OnOffActuator(Object actuator, boolean isSwitch) {
        if (!((actuator instanceof PWMSpeedController) || (actuator instanceof Relay))){
            DriverStation.reportError(new IllegalArgumentException().getMessage(), true);
        }
        isPWM = actuator instanceof PWMSpeedController;
        this.actuator = actuator;
        isOn = false;
        isPressed = false;
        this.isSwitch = isSwitch;
    }

	public OnOffActuator(Object actuator) {
		this(actuator,true);
	}

    public void set(boolean buttonState) {
    	if (isSwitch){
	        if (buttonState && !isPressed){
	            isPressed = true;
	            isOn = !isOn;
	            if (isPWM)((PWMSpeedController)actuator).set( isOn ? 1.0 : 0.0);
	            else ((Relay)actuator).set( isOn ? Relay.Value.kOn : Relay.Value.kOff);
	        }
			isPressed = buttonState;
    	} else { // by direct switch control
			if (isPWM){((PWMSpeedController)actuator).set(buttonState ? 1.0 : 0.0);}
			else ((Relay)actuator).set(buttonState ? Relay.Value.kOn : Relay.Value.kOff);
    	}
    }

	public void set(double buttonState) {
		if (isSwitch){
			if (buttonState >= 0.7 && !isPressed){
				isPressed = true;
				isOn = !isOn;
				if (isPWM)((PWMSpeedController)actuator).set( isOn ? 1.0 : 0.0);
				else ((Relay)actuator).set( isOn ? Relay.Value.kOn : Relay.Value.kOff);
			}
			isPressed = buttonState >= 0.7;
		} else { // by direct switch control
			if (isPWM){((PWMSpeedController)actuator).set( 1 - Math.pow(1 - buttonState, 2));}
			else ((Relay)actuator).set(buttonState >= 0.7 ? Relay.Value.kOn : Relay.Value.kOff);
		}
	}

}
