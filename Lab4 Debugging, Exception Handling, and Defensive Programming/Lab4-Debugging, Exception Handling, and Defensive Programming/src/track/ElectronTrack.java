package track;

import java.util.Objects;

import physicalObject.Electron;

/**
 * 电子轨道，形状为圆形，{@code immutable} 类
 *
 */
public class ElectronTrack extends Track { 
	private final int number;
	
	/*
	 * AF
	 * 	AF(number) = 一个带有编号的电子轨道
	 * 
	 * RI
	 * 	number >=1的正整数
	 * 
	 * Safety from exposure
	 * 	所有域是private final的，没有Mutator方法，客户端无法拿到引用
	 */
	
	private void checkRep() {
		assert number >= 1: "ElectronTrack format of number is wrong";
	}
	
	/**
	 * 初始化一个电子轨道，输入参数为轨道编号，例如1号轨道，2号轨道。
	 * @param number 轨道编号
	 */
	public ElectronTrack(int number) {
		this.number = number;
		checkRep();
	}
	
	/**
	 * 获得轨道半径，由于电子轨道没有半径，返回-1
	 */
	@Override
	public double getRadius() {
		return -1;
	}

	/**
	 * 获得电子轨道编号
	 */
	@Override
	public int getNumber() {
		return number;
	}
	
	
	@Override 
	public boolean equals(Object otherobject) {
		if(this == otherobject) return true;
		if(otherobject == null) return false;
		if(getClass() != otherobject.getClass()) return false;
		
		ElectronTrack other = (ElectronTrack)otherobject;
		
		return this.number == (other.getNumber());
	}
	
	@Override
	public String toString() {
		return "[ElectronTrack Number:"+number+"]";
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(number);
	}

}
