package circularorbit;

import centralobject.CentralPoint;
import circularorbit.CircularOrbit;
import circularorbit.ConcreteCircularOrbit;
import physicalobject.PhysicalObject;
import testobjects.TestTrackFactory;

public class ConcreteCircularOrbitTest extends CircularOrbitTest<CentralPoint, PhysicalObject> {

  @Override
  public CircularOrbit<CentralPoint, PhysicalObject> emptyInstance() {
    ConcreteCircularOrbit<CentralPoint, PhysicalObject> co = 
        new ConcreteCircularOrbit<CentralPoint, PhysicalObject>(
        "Orbit", new TestTrackFactory());
    return co;
  }

}
