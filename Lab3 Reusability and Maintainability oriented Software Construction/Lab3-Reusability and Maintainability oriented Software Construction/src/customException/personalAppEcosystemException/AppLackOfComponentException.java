package customException.personalAppEcosystemException;

import java.io.IOException;

public class AppLackOfComponentException extends IOException {

	
	public AppLackOfComponentException() {
		super();
	}
	
	public AppLackOfComponentException(String message) {
		super(message);
	}
	
	public AppLackOfComponentException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public AppLackOfComponentException(Throwable cause) {
		super(cause);
	}
}
