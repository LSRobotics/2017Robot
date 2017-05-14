package frc.team5181.robot.sensors;

import edu.wpi.first.wpilibj.I2C;

//switch to JNI of not working

public class LidarLite{
	
	public static final int ADDRESS_SIG_COUNT_VAL = 0x02;
	public static final int ADDRESS_ACQ_CONFIG_REG = 0x04;
	public static final int ADDRESS_THRESHOLD_BYPASS = 0x1c;
	public static final int ADDRESS_ACQ_COMMAND = 0x00;
	public static final int COMMAND_RESET = 0x00;
	public static final int COMMAND_READ_NO_BIAS_CORRECTION = 0x03;
	public static final int COMMAND_READ_WITH_BIAS_CORRECTION = 0x04;
	
	private I2C port;

	public LidarLite(int address) {
		this.port = new I2C(I2C.Port.kOnboard, address);
	}
	
	public LidarLite() {
		this(0x62); // Use the default address when not provided.
	}

	public interface LidarLiteConfiguration {
		int getSigCountVal();
		int getAcqConfigReg();
		int getThresholdBypass();
	}
	
	public enum LidarLiteConfigurationPreset implements LidarLiteConfiguration {
		
		/** Default mode, balanced performance. */
		DEFAULT          ( 0x80, 0x08, 0x00 ),
		
		/** Short range, high speed. Uses 0x1d maximum acquisition count. */
		SHORT_RANGE      ( 0x1d, 0x08, 0x00 ),
		
		/** Default range, higher speed short range. Turns on quick termination
		 * detection for faster measurements at short range (with decreased
		 * accuracy) */
		FAST_WHEN_CLOSE  ( 0x80, 0x00, 0x00 ),
		
		/** Maximum range. Uses 0xff maximum acquisition count. */
		MAX_RANGE        ( 0xff, 0x08, 0x00 ),
		
		/** High sensitivity detection. Overrides default valid measurement detection
		 * algorithm, and uses a threshold value for high sensitivity and noise. */
		HIGH_SENSITIVITY ( 0x80, 0x08, 0x80 ),
		
		/** Low sensitivity detection. Overrides default valid measurement detection
		 * algorithm, and uses a threshold value for low sensitivity and noise. */
		LOW_SENSITIVITY  ( 0x80, 0x08, 0xb0 );
		
		private final int sigCountVal;
		private final int acqConfigReg;
		private final int thresholdBypass;
		LidarLiteConfigurationPreset(int sigCountVal, int acqConfigReg, int thresholdBypass) {
			this.sigCountVal = sigCountVal;
			this.acqConfigReg = acqConfigReg;
			this.thresholdBypass = thresholdBypass;
		}
		
		@Override
		public int getSigCountVal() {
			return this.sigCountVal;
		}

		@Override
		public int getAcqConfigReg() {
			return this.acqConfigReg;
		}

		@Override
		public int getThresholdBypass() {
			return this.thresholdBypass;
		}
		
	}
	
	/**
	 * Configure this LidarLite with configuration provided.
	 * @param configuration The configuration is being used
	 */
	public void configure(LidarLiteConfiguration configuration) {
		this.port.write(ADDRESS_SIG_COUNT_VAL, configuration.getSigCountVal());
		this.port.write(ADDRESS_ACQ_CONFIG_REG, configuration.getAcqConfigReg());
		this.port.write(ADDRESS_THRESHOLD_BYPASS, configuration.getThresholdBypass());
	}
	
	/**
	 * Get the current reading of this LidarLite.
	 * @param biasCorrection Whether or not enable bias correction
	 * @return The current reading of this LidarLite.
	 */
	public int readDistance(boolean biasCorrection) {
		this.port.write(ADDRESS_ACQ_COMMAND, biasCorrection ? COMMAND_READ_WITH_BIAS_CORRECTION : COMMAND_READ_NO_BIAS_CORRECTION);
		// Array to store high and low bytes of distance
		byte[] distanceArray = new byte[2];
		// Read two bytes from register 0x8f (autoincrement for reading 0x0f and 0x10)
		this.port.read(0x8f, 2, distanceArray);
		// Shift high byte and add to low byte
		return (distanceArray[0] << 8) + distanceArray[1];
	}
	
	/**
	 * Get the current reading of this LidarLite with bias correction enabled.
	 * @return The current reading of this LidarLite
	 */
	public int readDistance() {
		return readDistance(true);
	}

	/**
	 * Reset this LidarLite.
	 */
	void reset()
	{
		this.port.write(ADDRESS_ACQ_COMMAND, COMMAND_RESET);
	}

}

