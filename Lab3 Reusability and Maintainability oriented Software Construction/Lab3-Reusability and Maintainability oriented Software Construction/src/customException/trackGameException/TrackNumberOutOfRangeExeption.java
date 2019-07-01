package customException.trackGameException;

import java.io.IOException;

public class TrackNumberOutOfRangeExeption extends IOException {
	public TrackNumberOutOfRangeExeption() {
		super();
	}
	
	public TrackNumberOutOfRangeExeption(String message) {
		super(message);
	}
	
	public TrackNumberOutOfRangeExeption(String message, Throwable cause) {
		super(message, cause);
	}
	
	public TrackNumberOutOfRangeExeption(Throwable cause) {
		super(cause);
	}
}
