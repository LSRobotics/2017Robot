package frc.team5181.robot.pid;

@FunctionalInterface
public interface PIDReducer {
	double reduce(double accumulator, double currentValue, double currentIndex);
}
