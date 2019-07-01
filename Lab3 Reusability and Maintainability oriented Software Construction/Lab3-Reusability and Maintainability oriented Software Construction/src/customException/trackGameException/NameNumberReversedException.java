package customException.trackGameException;

import java.io.IOException;

public class NameNumberReversedException extends IOException {
	public NameNumberReversedException() {
		super();
	}
	
	public NameNumberReversedException(String message) {
		super(message);
	}
	
	public NameNumberReversedException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public NameNumberReversedException(Throwable cause) {
		super(cause);
	}
}
