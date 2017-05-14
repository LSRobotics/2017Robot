package frc.team5181.robot.autonomous;

import frc.team5181.robot.UnitConvert;

/**
 * Created by TylerLiu on 2017/02/09.
 * All equation written in centimeters.
 * Inches values need to be specified explicitly
 */
public class FieldEquations {
    //TODO MEASURE THESE VALUES
    static final double RobotIRRadius = 19; //radius of IR sensor from center Axis of the Robot.
    static final double RobotIRBackWheelDistance = 10; //distances from the back IR sensor to the back wheel, turning axis.
    static final double FieldWidth = 823; 
    static final double FieldLength = 1656;
    static final double FrontURDistToBackWheelAxis = UnitConvert.inchToCentimeter(21.5); //TODO
    static final double LeftURDistToVerticalDivideLine = 38.1; //15 Inch
    static final double RightURDistToVerticalDivideLine = 35.56; //14 Inch
    static final double BackIRDistToBackWheelAxis = UnitConvert.inchToCentimeter(4);
    static final double BackWheelAxisToBackFrame = 12.065; //4.75 Inch
    static final double RobotWidth = UnitConvert.inchToCentimeter(36);
    static final double RobotLength = UnitConvert.inchToCentimeter(40);
    public static final double BackWheelAxisLength = UnitConvert.inchToCentimeter(28.3125);
    
    
    /**
     * calculate the target distance before turning to LIFT.
     * @param backDist the input distance from the back IR sensor.
     * @param expectedTurnPoint_y the second element of array expectedTurnPoint returned by calcRobotTurnPoint
     * @return the distance to drive before turning toward LIFT.
     * @return if return is positive, then drive forward; if return is negative, then drive backward;
     */
    static double getLiftAlignDistance(double backDist, double expectedTurnPoint_y){
    	// Robot Center Distance from the wall = IR sensor reads + IR sensor from the center of the Robot
    	/*
    	 * distToDrive = Expected Y value - Current Y value
    	 * if distToDrive > 0   --> Drive forward
    	 * if distToDrive < 0   --> Drive Backward
    	 */
    	//     |-------Dist to Sensor--------|
    	return expectedTurnPoint_y  - (backDist + BackIRDistToBackWheelAxis);
    }
    
    static double getLiftAlignDistance(double backDist, double expectedTurnPoint_y, boolean isBackFacingForward){
    	//Compatible function to getLiftAlignDistance
    	return isBackFacingForward ? (expectedTurnPoint_y - FrontURDistToBackWheelAxis) : getLiftAlignDistance(backDist, expectedTurnPoint_y); 
    }

    /**
     * return the target turning point before turning to the side LIFT.
     * @param sideDist
     * @param isLeftSide
     * @return
     */
    static double[] calcRobotTurnPoint(double sideDist, boolean isLeftSide){
    	double x = calcRobotCenterDistFromRightWall(sideDist,isLeftSide);
    	double y = -0.577350269189624*Math.abs( x + FieldWidth / 2)+379.9713;
    	//double y = (-0.577350269189624*Math.abs(x / 2.54 +FieldWidth /2 / 2.54)+149.595) * 2.54;
    	return new double[] {x, y};
    }
    
    static double[] calcRobotTurnPoint(double sideDist, boolean isLeftSide, boolean isSensor){
    	double x = isSensor ? calcRobotCenterDistFromRightWall(sideDist, isLeftSide) : calcRobotCenterDistFromRightWall(sideDist, isLeftSide);
    	double y = -0.577350269189624*Math.abs( x + FieldWidth / 2)+379.9713;
    	return new double[] {x,y};
    }
    
    static double calcRobotCenterDistFromRightWall(double sideDist, boolean leftSide){
    	return leftSide ? (-FieldWidth+sideDist+LeftURDistToVerticalDivideLine) : (-sideDist-RightURDistToVerticalDivideLine);
    }
    
    static double calcRobotCenterDistByFixDistance(double sideDist, boolean leftSide){
    	return leftSide ? (-FieldWidth+sideDist+(RobotWidth/2)) : (-sideDist-(RobotWidth/2));
    }
    
    /**
     * calculate the target distance after turning to LIFT
     * @param frontDist the front IR sensor reading 
     * @param expectedTurnPoint the array returned by calcRobotTurnPoint.
     * @return Return the distance to drive to the LIFT
     */
    
    static double getLiftDistance(double frontDist,double[] expectedTurnPoint){
    	return Math.sqrt(Math.pow(Math.abs(expectedTurnPoint[0] - (FieldWidth/2)),2)+Math.pow(379.9713-expectedTurnPoint[1], 2))-101.8978267482-frontDist;
    }


    /**
     * return the target distance for vertical alignment for loading station close to opponent wall
     * @param horizontal
     * @return the vertial position of the target
     */
    static double getVerticalLoadPositionA(double horizontal){
        return 12.1056 + 2.0924 * horizontal;
    }

    /**
     * return the target distance for vertical alignment for loading station far from opponent wall
     * @param horizontal
     * @return the vertial position of the target
     */
    static double getVerticalLoadPositionB(double horizontal){
        return -291.34 + 2.0924 * horizontal;
    }

    /**
     * return the target distance for horizontal alignment for loading station close to opponent wall
     * @param vertical
     * @return the horizontal position of the target
     */
    static double getHorizontalLoadPositionA(double vertical){
        return (vertical - 12.1056) / 2.0924;
    }

    /**
     * return the target distance for horizontal alignment for loading station far from opponent wall
     * @param vertical
     * @return the horizontal position of the target
     */
    static double getHorizontalLoadPositionB(double vertical){
        return (vertical + 291.34) / 2.0924;
    }
    
    static double[] blueSideGearDropper(double rightDist,int location){
    	double expectY = 0.0;
    	rightDist = (-FieldWidth+rightDist+RightURDistToVerticalDivideLine);
    	switch(location){
    		case 1:
    			expectY = 0.505125604*(-823+rightDist)+556.4267613371685355423;
    			break;
    		case 2:
    			expectY = 0.505125604*(-823+rightDist)+415.0950504668314644577;
    			break;
    	}
    	return new double[] {(-823+rightDist),expectY};
    }
    
    static double[] redSideGearDropper(double leftDist,int location){
    	double expectY = 0.0;
    	leftDist = (-FieldWidth+leftDist+LeftURDistToVerticalDivideLine);
    	switch(location){
    		case 1:
    			// Location 1
    			expectY=-0.505125604*(-823+leftDist)+1099.5732386628;
    			break;
    		case 2:
    			// Location 2
    			expectY=-0.505125604*(-823+leftDist)+1240.9049496218;
    			break;
    	}
    	return new double[] {(-823+leftDist),expectY};
    }
    
    static double calcDistToGearDropper(double frontDist, double expectTurningPoint_Y, boolean redSide, int location){
    	double driveDist = 0.0;
    	if (redSide){
    		driveDist = (frontDist + FrontURDistToBackWheelAxis) - (FieldLength - expectTurningPoint_Y);
    	} else { //!redSide
    		driveDist = (frontDist + FrontURDistToBackWheelAxis) - expectTurningPoint_Y;
    	}
    	return driveDist;
    }
    
}
