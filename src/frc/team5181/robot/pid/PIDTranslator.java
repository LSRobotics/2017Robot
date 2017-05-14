package frc.team5181.robot.pid;

@FunctionalInterface
public interface PIDTranslator {
	double translate(double data);
}
