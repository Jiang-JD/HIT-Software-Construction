package track;

/**
 * {@code Track} 是围绕中心点的一条闭合曲线，一般为圆形或椭圆形或其 他不规则形状，本实验中统一考虑为标准圆形。
 * 轨道的 半径是指该轨道与中心点之间的距离。半径既可以表示物理上的半径，又可以表示关系中的权重，
 * 比如亲密度5，10，15等等。每一条在系统中的轨道的编号都不相同。
 *
 */
public abstract class Track {
	/**
	 * 获得轨道的半径
	 * @return 半径
	 */
	public abstract double getRadius();
	
	/**
	 * 获得轨道编号
	 * @return 编号
	 */
	public abstract int getNumber();
	
	
}
