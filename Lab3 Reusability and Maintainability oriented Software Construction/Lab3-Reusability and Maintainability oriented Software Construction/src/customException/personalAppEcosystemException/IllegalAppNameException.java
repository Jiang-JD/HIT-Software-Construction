package customException.personalAppEcosystemException;

import java.io.IOException;

public class IllegalAppNameException extends IOException {
	public IllegalAppNameException() {
		super();
	}
	
	public IllegalAppNameException(String message) {
		super(message);
	}
	
	public IllegalAppNameException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public IllegalAppNameException(Throwable cause) {
		super(cause);
	}
}
