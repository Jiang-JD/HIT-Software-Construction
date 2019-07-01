package track;

/**
 * 圆形轨道生产工厂类，生产圆形轨道
 */
public class CircularTrackFactory extends TrackFactory {
	/**
	 * 生产圆形轨道，输入参数为轨道半径{@code radius} ，这个参数也可以视为编号或权重
	 */
	@Override 
	protected Track createProduct(double radius) {
		return new CircularTrack();
	}
}
