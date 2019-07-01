package centralObject;

import java.util.Objects;

import constant.Regex;
import physicalObject.Electron;

/**
 * 原子系统的原子核，原子核视为单一物体，不考虑其中的中子和质子
 *
 */
public class Nucleus extends CentralPoint {

	/*
	 * AF
	 * 	AF(name) = 一个带有名称的轨道系统中心点
	 * 
	 * RI
	 * 	name name不为null也不为空串，name是一个label
	 * 
	 * Safety from exposure
	 * 	所有域是private final的，不提供Mutator，客户端拿不到直接引用
	 */
	
	private void checkRep() {
		assert getName() != null && !getName().equals("") : "CentralPoint name is null or \"\"";
		assert getName().matches(Regex.REGEX_LABEL): "CentralPoint name type is wrong";
	}

	/**
	 * 初始化一个原子核，输入参数为原子核的名称，以元素名称代替
	 * @param name 元素名称
	 */
	public Nucleus(String name) {
		super(name);
		checkRep();
	}

	@Override 
	public boolean equals(Object otherobject) {
		if(this == otherobject) return true;
		if(otherobject == null) return false;
		if(getClass() != otherobject.getClass()) return false;
		
		Electron other = (Electron)otherobject;
		
		return getName().equals(other.getName());
	}
	
	@Override
	public String toString() {
		return "[Nucleus:"+getName()+"]";
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getName());
	}
}
