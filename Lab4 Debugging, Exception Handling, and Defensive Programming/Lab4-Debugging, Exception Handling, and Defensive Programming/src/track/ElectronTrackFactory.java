package track;

/**
 * 电子轨道的工厂类
 *
 */
public class ElectronTrackFactory extends TrackFactory {

	/**
	 * 生产电子轨道，输入轨道编号，编号为 >= 1 的正整数
	 */
	@Override
	protected Track createProduct(double radius) {
		return new ElectronTrack((int)radius);
	}

}
