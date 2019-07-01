package customException.trackGameException;

import java.io.IOException;

public class IllegalFormatRaceTypeException extends IOException {
	public IllegalFormatRaceTypeException() {
		super();
	}
	
	public IllegalFormatRaceTypeException(String message) {
		super(message);
	}
	
	public IllegalFormatRaceTypeException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public IllegalFormatRaceTypeException(Throwable cause) {
		super(cause);
	}
}
