package testObjects;

import java.util.Objects;

import physicalObject.Electron;
import physicalObject.PhysicalObject;

/**
 * 用于测试的轨道物体，无实际意义
 *
 */
public class TestObject extends PhysicalObject {

	private final String name;
	
	public TestObject(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override 
	public boolean equals(Object otherobject) {
		if(this == otherobject) return true;
		if(otherobject == null) return false;
		if(getClass() != otherobject.getClass()) return false;
		
		TestObject other = (TestObject)otherobject;
		
		return this.name.equals(other.getName());
	}
	
	@Override 
	public String toString() {
		return name;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
}

