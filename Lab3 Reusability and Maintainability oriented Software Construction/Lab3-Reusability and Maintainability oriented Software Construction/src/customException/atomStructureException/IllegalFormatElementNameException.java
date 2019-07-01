package customException.atomStructureException;

import java.io.IOException;

public class IllegalFormatElementNameException extends IOException {
	public IllegalFormatElementNameException() {
		super();
	}
	
	public IllegalFormatElementNameException(String message) {
		super(message);
	}
	
	public IllegalFormatElementNameException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public IllegalFormatElementNameException(Throwable cause) {
		super(cause);
	}
}
