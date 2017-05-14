package frc.team5181.robot.sensors;

import frc.team5181.robot.Statics;

public class UltrasonicGroup {

    public enum UltrasonicPosition{
        LEFT  ( Statics.LEFT_Ultrasonic_trig, Statics.LEFT_Ultrasonic_echo),
        RIGHT ( Statics.RIGHT_Ultrasonic_trig, Statics.RIGHT_Ultrasonic_echo),
        FRONT_LEFT  ( Statics.FRONT_LEFT_Ultrasonic_trig, Statics.FRONT_LEFT_Ultrasonic_echo),
        FRONT_RIGHT ( Statics.FRONT_RIGHT_Ultrasonic_trig, Statics.FRONT_RIGHT_Ultrasonic_echo);

        int trig, echo;

        UltrasonicPosition(int trig, int echo){
            this.trig = trig;
            this.echo = echo;
        }

        public int getTrig() {
            return trig;
        }

        public int getEcho() {
            return echo;
        }
    }

    static UltrasonicSensor[] sensors;
    static double[] lastMeasurement;

    public static void init(){
        sensors = new UltrasonicSensor[UltrasonicPosition.values().length];
        for (UltrasonicPosition s : UltrasonicPosition.values()){
            sensors[s.ordinal()] = new UltrasonicSensor(s);
        }
        for (UltrasonicPosition s : UltrasonicPosition.values()){
            sensors[s.ordinal()].setAutomaticMode(true);
        }
        lastMeasurement = new double[UltrasonicPosition.values().length];
        lastMeasurement[UltrasonicPosition.LEFT.ordinal()] = 200;
        lastMeasurement[UltrasonicPosition.RIGHT.ordinal()] = 200;
    }

    public static UltrasonicSensor getsensor(UltrasonicPosition position){
        return sensors[position.ordinal()];
    }

    public static double[] getDistances(){
        for (UltrasonicPosition sensorPosition : UltrasonicPosition.values()){
            if (sensors[sensorPosition.ordinal()].isRangeValid())
                lastMeasurement[sensorPosition.ordinal()] = sensors[sensorPosition.ordinal()].getRangeMM() / 10;
        }
        return lastMeasurement;
    }

    public static double getDistances(UltrasonicPosition position){
        if (sensors[position.ordinal()].isRangeValid() && sensors[position.ordinal()].getRangeMM() > 0.5) {
            lastMeasurement[position.ordinal()] = sensors[position.ordinal()].getRangeMM() / 10;
            return sensors[position.ordinal()].getRangeMM() / 10;
        }
        return lastMeasurement[position.ordinal()];
    }

    public static void free(){
        for (UltrasonicSensor u : sensors){
            u.free();
        }
    }
}
