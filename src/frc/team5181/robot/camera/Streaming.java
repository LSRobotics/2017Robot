package frc.team5181.robot.camera;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;

public class Streaming {
	/**
	MjpegServer mjpegServer;
	UsbCamera frontCamera;
	
	public Streaming(){
		//Create Camera Object
		frontCamera = new UsbCamera("front_camera",0);
		//Initialize a mjpeg server
		mjpegServer = new MjpegServer("front_camera",554);
		//Put the Camera object to the mjpeg server
		mjpegServer.setSource(frontCamera);
	}
	**/
	
	public static void beginStreaming() {
		// This is a super wrapped class of the upper sample code
		// startAutomaticCapture() --> Get Camera Port -->
		// Get Camera Name --> Init Camera object -->
		// Init MJpegServer
		UsbCamera camera = CameraServer.getInstance().startAutomaticCapture("cam0", 0);
		camera.setResolution(300, 150);
		//camera.setResolution(600, 300);
	}
}
