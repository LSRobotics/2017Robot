package frc.team5181.robot.pid;

import java.util.List;

@FunctionalInterface
public interface PIDCombiner {
	double combine(List<Double> data);
}
