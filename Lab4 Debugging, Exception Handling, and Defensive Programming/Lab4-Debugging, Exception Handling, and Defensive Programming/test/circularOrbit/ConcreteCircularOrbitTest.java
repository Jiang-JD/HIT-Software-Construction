package circularOrbit;

import centralObject.CentralPoint;
import physicalObject.PhysicalObject;
import testObjects.TestTrackFactory;

public class ConcreteCircularOrbitTest extends CircularOrbitTest<CentralPoint, PhysicalObject>{

	@Override
	public CircularOrbit<CentralPoint, PhysicalObject> emptyInstance() {
		ConcreteCircularOrbit<CentralPoint, PhysicalObject> co = new ConcreteCircularOrbit<CentralPoint, PhysicalObject>("Orbit",new TestTrackFactory());
		return co;
	}

}
