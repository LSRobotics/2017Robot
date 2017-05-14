package frc.team5181.robot.sensors.filter;

/**
 * use a moving average filter to predict and smooth data
 * Created by TylerLiu on 2017/02/14.
 */
public class MAFilter implements Filter{
    private long lastTime;
    private double pos;
    private double lastAvg;
    private double vel;
    private double alpha;
    private double velCutOff;

    /**
     *
     * @param pos starting position
     * @param alpha constant for rolling over
     *              percentage of weight for current reading
     */
    public MAFilter(double pos, double alpha){
        lastTime = System.currentTimeMillis();
        if (Double.isNaN(pos) || Double.isInfinite(pos)) {
        	this.alpha = alpha;
        	return;
        	//invalid data
        }
        this.pos = pos;
        lastAvg = pos;
        this.alpha = alpha;
        this.velCutOff = Double.NaN;
    }

    public MAFilter(double pos){
        this(pos, 0.625);
    }

    public MAFilter(double pos, double alpha, double velCutOff){
        this(pos, alpha);
        setVelCutOff(velCutOff);
    }

    @Override
    public double getSmoothedPos() {
        return lastAvg;
    }

    @Override
    public void update(double pos) {
        long time = System.currentTimeMillis();
        if ((Double.isNaN(pos) || Double.isInfinite(pos)) || time == lastTime) return; //same data as last time or invalid data
        double pred = (alpha * pos + (1 - alpha) * lastAvg);
        double vel = (pred - lastAvg) / (time - lastTime) * 1000;
        if ((!Double.isNaN(velCutOff)) && Math.abs(vel) > velCutOff) return;
        this.pos = pos;
        this.vel = vel;
        lastAvg = pred;
        lastTime = time;
    }

    @Override
    public double getVelocity() {
        return vel;
    }

    @Override
    public double predict() {
        return pos + vel / 1000 * (System.currentTimeMillis() - lastTime);
    }

    public MAFilter setVelCutOff(double cutOff){
        velCutOff = Math.abs(cutOff);
        return this;
    }

    /* testing function
    public static void main (String[] args) throws Exception{
        MAFilter f = new MAFilter(50, 0.3);
        Random r = new Random();
        for (int i = 0; i < 1000; i ++){
            double k = 50 + 25 * Math.sin(i * Math.PI / 100.0) + r.nextGaussian() * 3;
            f.update(k);
            System.out.println(k + " " + f.getSmoothedPos());
            Thread.sleep(1);
        }
    }
    */
}
