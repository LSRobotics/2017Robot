package frc.team5181.robot;

/**
 * Copyright
 */

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;

public class Gamepad {

    private static int GPportNumber = 0;
    private static int JSportNumber = 1;

    private static XboxController xGP = new XboxController(GPportNumber);
    private static Joystick jJS = new Joystick(JSportNumber);
    
    //TODO redo mapping
    
    //Gamepad
    //States of Buttons
    public static boolean GP_A_BUTTON_STATE = false;
    public static boolean GP_B_BUTTON_STATE = false;
    public static boolean GP_X_BUTTON_STATE = false;
    public static boolean GP_Y_BUTTON_STATE = false;
    public static boolean GP_LEFT_BUMPER_STATE = false;
    public static boolean GP_RIGHT_BUMPER_STATE = false;
    public static boolean GP_BACK_STATE = false;
    public static boolean GP_START_STATE = false;
    public static boolean GP_LEFT_STICK_DOWN_STATE = false;
    public static boolean GP_RIGHT_STICK_DOWN_STATE = false;

    // Axis
    public static double GP_LEFT_Stick_X_State = 0;
    public static double GP_LEFT_Stick_Y_State = 0;
    public static double GP_LEFT_Trigger_State = 0;
    public static double GP_RIGHT_Trigger_State = 0;
    public static double GP_RIGHT_Stick_X_State = 0;
    public static double GP_RIGHT_Stick_Y_State = 0;

    //DPAD
    public static int D_PAD_STATE = -1;

    //Joystick
    //States of Buttons
    public static boolean JS_TRIGGER_STATE = false;

    // Axis
    public static double JS_Stick_X_State = 0;
    public static double JS_Stick_Y_State = 0;
    
    //POV
    public static int POV_STATE = -1;

    public static void setNaturalState(int GPport, int JSport) {
            xGP = new XboxController(GPport);
            jJS = new Joystick(JSport);
            setNaturalState();
    }

    public static void resetJoystickPort() {
        xGP = new XboxController(GPportNumber);
        jJS = new Joystick(JSportNumber);
    }

    /**
     * set the state of driver station controllers
     */
    public static void setNaturalState() {
        //comment out unneeded
        setGPAxis();
        //setJSAxis();
        setGPButton();
        //setJSButton();
        //setJSPOV();
        setGPDPAD();
    }

    
    /**
     * enum declarations for XboxController's GetX / GetY calls
     */
    //GenericHID.Hand Left = kLeft;
    //GenericHID.Hand Right = kRight;
    
    
    /**
     * set game pad axis
     */
    public static void setGPAxis(){
        
        //  axis sets using enum value
        GP_LEFT_Stick_X_State  = xGP.getX(Hand.kLeft);
        GP_LEFT_Stick_Y_State  = xGP.getY(Hand.kLeft);
        GP_LEFT_Trigger_State  = xGP.getTriggerAxis(Hand.kLeft);
        GP_RIGHT_Trigger_State = xGP.getTriggerAxis(Hand.kRight);
        GP_RIGHT_Stick_X_State = xGP.getX(Hand.kRight);
        GP_RIGHT_Stick_Y_State = xGP.getY(Hand.kRight);
    }

    /**
     * set joystick axis
     */
    public static void setJSAxis(){
        JS_Stick_X_State = jJS.getRawAxis(Statics.JS_Stick_X);
        JS_Stick_Y_State = jJS.getRawAxis(Statics.JS_Stick_Y);
    }

    /**
     * set gamepad buttons
     */
    public static void setGPButton(){
        GP_A_BUTTON_STATE         = xGP.getAButton();
        GP_B_BUTTON_STATE         = xGP.getBButton();
        GP_X_BUTTON_STATE         = xGP.getXButton();
        GP_Y_BUTTON_STATE         = xGP.getYButton();
        GP_LEFT_BUMPER_STATE      = xGP.getBumper(Hand.kLeft);
        GP_RIGHT_BUMPER_STATE     = xGP.getBumper(Hand.kRight);
        GP_BACK_STATE             = xGP.getBackButton();
        GP_START_STATE            = xGP.getStartButton();
        GP_LEFT_STICK_DOWN_STATE  = xGP.getStickButton(Hand.kLeft);
        GP_RIGHT_STICK_DOWN_STATE = xGP.getStickButton(Hand.kRight);
    }

    /**
     * set joystick buttons
     */
    public static void setJSButton(){
        JS_TRIGGER_STATE = jJS.getRawButton(Statics.JS_TRIGGER);
        //TODO rewrite joystick buttons
    }

    /**
     * set joystick POV
     */
    public static void setJSPOV(){
        POV_STATE = jJS.getPOV();
    }

    /**
     * set Gamepad DPAD
     */
    public static void setGPDPAD(){
        D_PAD_STATE = xGP.getPOV();
    }

}
