package testObjects;

import java.util.Objects;

import physicalObject.Electron;
import track.Track;

/**
 * Track测试类
 *
 */
public class TestTrack extends Track {

	private final double radius;
	private final int number;
	
	public TestTrack(double radius) {
		this.radius = radius;
		this.number = (int)radius;
	}
	
	@Override
	public double getRadius() {
		return radius;
	}

	@Override
	public int getNumber() {
		return number;
	}

	@Override 
	public boolean equals(Object otherobject) {
		if(this == otherobject) return true;
		if(otherobject == null) return false;
		if(getClass() != otherobject.getClass()) return false;
		
		TestTrack other = (TestTrack)otherobject;
		
		return this.radius == other.radius;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(radius, number);
	}
}
