package customException.personalAppEcosystemException;

import java.io.IOException;

public class NoUserException extends IOException {
	public NoUserException() {
		super();
	}
	
	public NoUserException(String message) {
		super(message);
	}
	
	public NoUserException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public NoUserException(Throwable cause) {
		super(cause);
	}
}
