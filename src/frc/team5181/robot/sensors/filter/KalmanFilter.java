/**
 * Created by TylerLiu on 2017/02/14.
 */
package frc.team5181.robot.sensors.filter;

public class KalmanFilter implements Filter {
    long lastTime;
    double pos;
    double velocity;

    public KalmanFilter(double pos){
        lastTime = System.currentTimeMillis();
        this.pos = pos;
        velocity = 0;
    }

    @Override
    public double getSmoothedPos() {
        return pos;
    }

    @Override
    public void update(double pos) {
        long time = System.currentTimeMillis();
        velocity = (pos - this.pos) / (time - lastTime) * 1000;
        this.pos = pos;
        this.lastTime = time;
    }

    @Override
    public double getVelocity() {
        return velocity;
    }

    @Override
    public double predict(){
        return pos + velocity / 1000 * (System.currentTimeMillis() - lastTime);
    }
}
