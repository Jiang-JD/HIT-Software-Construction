package customException.trackGameException;

import java.io.IOException;

public class NoRaceTypeException extends IOException {
	public NoRaceTypeException() {
		super();
	}
	
	public NoRaceTypeException(String message) {
		super(message);
	}
	
	public NoRaceTypeException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public NoRaceTypeException(Throwable cause) {
		super(cause);
	}
}
