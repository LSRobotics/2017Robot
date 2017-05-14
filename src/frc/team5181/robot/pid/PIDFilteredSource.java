package frc.team5181.robot.pid;

import frc.team5181.robot.sensors.filter.Filter;

/*package*/ class PIDFilteredSource implements PIDSource {

  private PIDSource source;
  private Filter filter;

  /*package*/ PIDFilteredSource(PIDSource source, Filter filter) {
    this.source = source;
    this.filter = filter;
  }

  @Override
  public double pidYield() {
	double yielding = source.pidYield();
	if (Double.isInfinite(yielding)) {
		return yielding;
	}
    filter.update(yielding);
    System.out.println(yielding + " " + filter.getSmoothedPos());
    return filter.getSmoothedPos();
  }
}
