package track;

public class RaceTrackFactory extends TrackFactory {

	/**
	 * 生产圆形跑道，输入参数为跑道编号，编号为 >= 1的正整数
	 * @param radius 跑道编号， >= 1的正整数
	 * @return 一个带有编号的跑道
	 */
	@Override
	protected Track createProduct(double radius) {
		return new RaceTrack((int)radius);
	}

}
