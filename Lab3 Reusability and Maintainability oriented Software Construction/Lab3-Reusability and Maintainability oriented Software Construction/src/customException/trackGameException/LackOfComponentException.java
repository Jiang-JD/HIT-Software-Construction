package customException.trackGameException;

import java.io.IOException;

public class LackOfComponentException extends IOException {

	
	public LackOfComponentException() {
		super();
	}
	
	public LackOfComponentException(String message) {
		super(message);
	}
	
	public LackOfComponentException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public LackOfComponentException(Throwable cause) {
		super(cause);
	}
}
