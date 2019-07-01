package customException.atomStructureException;

import java.io.IOException;

public class NoNumberOfTracksException extends IOException {
	public NoNumberOfTracksException() {
		super();
	}
	
	public NoNumberOfTracksException(String message) {
		super(message);
	}
	
	public NoNumberOfTracksException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public NoNumberOfTracksException(Throwable cause) {
		super(cause);
	}
}
