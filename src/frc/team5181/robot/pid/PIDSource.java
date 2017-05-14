package frc.team5181.robot.pid;

import frc.team5181.robot.sensors.filter.Filter;

@FunctionalInterface
public interface PIDSource {
	/**
	 * When called, this method should return the current reading of
	 * the associated sensor.
	 * @return Current reading of the associated sensor
	 */
    double pidYield();
	default PIDFilteredSource filterWith(Filter filter) {
		return new PIDFilteredSource(this, filter);
	}
}
