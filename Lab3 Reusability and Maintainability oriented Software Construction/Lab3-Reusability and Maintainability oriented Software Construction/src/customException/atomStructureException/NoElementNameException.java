package customException.atomStructureException;

import java.io.IOException;

public class NoElementNameException extends IOException {
	public NoElementNameException() {
		super();
	}
	
	public NoElementNameException(String message) {
		super(message);
	}
	
	public NoElementNameException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public NoElementNameException(Throwable cause) {
		super(cause);
	}
}
