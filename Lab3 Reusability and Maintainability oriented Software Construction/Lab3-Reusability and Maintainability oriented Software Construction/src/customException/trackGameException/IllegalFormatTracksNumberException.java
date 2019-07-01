package customException.trackGameException;

import java.io.IOException;

public class IllegalFormatTracksNumberException extends IOException {
	public IllegalFormatTracksNumberException() {
		super();
	}
	
	public IllegalFormatTracksNumberException(String message) {
		super(message);
	}
	
	public IllegalFormatTracksNumberException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public IllegalFormatTracksNumberException(Throwable cause) {
		super(cause);
	}
}
