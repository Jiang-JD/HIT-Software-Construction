package testObjects;

import track.Track;
import track.TrackFactory;

public class TestTrackFactory extends TrackFactory {

	@Override
	protected Track createProduct(double radius) {
		return new TestTrack(radius);
	}

}
