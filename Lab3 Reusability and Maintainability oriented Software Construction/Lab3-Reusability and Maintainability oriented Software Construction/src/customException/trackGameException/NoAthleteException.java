package customException.trackGameException;

import java.io.IOException;

public class NoAthleteException extends IOException {
	public NoAthleteException() {
		super();
	}
	
	public NoAthleteException(String message) {
		super(message);
	}
	
	public NoAthleteException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public NoAthleteException(Throwable cause) {
		super(cause);
	}
}
