package track;

import java.util.Objects;

/**
 * {@code RaceTrack} 为一条圆形的跑道，每条跑道最多容纳一名运动员，跑道没有半径，作为
 * 理想化的二维圆形，每条跑道有唯一一个编号，其编号为 <b>>= 1</b> 的正整数，编号从场地
 * 的中心向外逐渐增大，即最里面的跑道编号为1，依次向外递增。
 * 
 */
public class RaceTrack extends Track {
	private final int number;
	/*
	 * AF
	 * 	AF(index) = 一条带有编号的跑道
	 * 
	 * RI 
	 * 	Number：跑道的编号是>=1的正整数
	 * 
	 * Safety from exposure
	 * 	index是private final的，没有mutator方法
	 */
	
	private void checkRep() {
		assert getNumber() >= 1 : "RaceTrack index < 1";
	}
	
	/**
	 * 初始化一个跑道对象，输入参数为跑道的编号，编号必须为 >= 1的正整数
	 * @param index 跑道编号， >= 1的正整数
	 */
	public RaceTrack(int index) {
		this.number = index;
		checkRep();	}
	
	/**
	 * 获得跑道的半径，返回-1
	 * @return 跑道的半径，返回-1
	 */
	@Override
	public double getRadius() {
		return -1;
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
		
		RaceTrack other = (RaceTrack)otherobject;
		
		return this.number == (other.getNumber());
	}
	
	@Override
	public String toString() {
		return "[RaceTrack Number:"+number+"]";
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(number);
	}

}
