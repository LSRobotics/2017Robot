package frc.team5181.robot;

public class Statics {
	
	//Wheels
	public static final int RIGHTPortFront = 0;
	public static final int RIGHTPortBack = 1;
	public static final int LEFTPortFront = 2;
	public static final int LEFTPortBack = 3;
	
	//Spark
	public static final int BallShoot = 5;
	public static final int Winch = 4;

	//Victor
	public static final int Intake = 6;

	//relay
	public static final int Agitator = 0;
	
	//IRSensor
	public static final int CHANNEL_BACK_LEFT_IR = 0;
	public static final int CHANNEL_BACK_RIGHT_IR = 1;

	//Encoders
	public static final int[] LEFT_Encoder  = { 0, 1 };
	public static final int[] RIGHT_Encoder = { 2, 3 };

	//Ultrasonic
	public static final int LEFT_Ultrasonic_trig = 4;
	public static final int LEFT_Ultrasonic_echo = 5;
	public static final int RIGHT_Ultrasonic_trig = 6;
	public static final int RIGHT_Ultrasonic_echo = 7;
	public static final int FRONT_LEFT_Ultrasonic_trig = 8;
	public static final int FRONT_LEFT_Ultrasonic_echo = 9;
	public static final int FRONT_RIGHT_Ultrasonic_trig = 10;
	public static final int FRONT_RIGHT_Ultrasonic_echo = 11;

	// Joystick
	public static final int JS_TRIGGER = 1;

	public static final int JS_Stick_X = 0;
	public static final int JS_Stick_Y = 1;
	public static final int JS_Stick_Twist = 2;

	//Pneumatics
	public static final int Compressor = 0;
	public static final int Solenoid_Forward = 0;
	public static final int Solenoid_Reverse = 1;

	//PDP
	public static final int PDP_RIGHTPortFront = 12;
	public static final int PDP_RIGHTPortBack = 13;
	public static final int PDP_LEFTPortFront = 14;
	public static final int PDP_LEFTPortBack = 15;
	
}
