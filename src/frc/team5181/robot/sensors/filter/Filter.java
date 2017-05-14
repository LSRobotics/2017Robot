/**
 * Created by TylerLiu on 2017/02/14.
 */
package frc.team5181.robot.sensors.filter;

public interface Filter {
    double getSmoothedPos();
    void update(double pos);
    double getVelocity();
    double predict();
}
