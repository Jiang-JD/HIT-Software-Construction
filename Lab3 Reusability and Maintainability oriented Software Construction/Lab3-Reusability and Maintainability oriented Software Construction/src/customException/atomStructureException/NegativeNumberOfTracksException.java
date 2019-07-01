package customException.atomStructureException;

import java.io.IOException;

public class NegativeNumberOfTracksException extends IOException {
	public NegativeNumberOfTracksException() {
		super();
	}
	
	public NegativeNumberOfTracksException(String message) {
		super(message);
	}
	
	public NegativeNumberOfTracksException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public NegativeNumberOfTracksException(Throwable cause) {
		super(cause);
	}
}
