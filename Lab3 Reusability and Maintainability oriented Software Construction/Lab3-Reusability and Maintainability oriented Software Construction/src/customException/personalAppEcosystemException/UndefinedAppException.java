package customException.personalAppEcosystemException;

import java.io.IOException;

public class UndefinedAppException extends IOException {
	public UndefinedAppException() {
		super();
	}
	
	public UndefinedAppException(String message) {
		super(message);
	}
	
	public UndefinedAppException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public UndefinedAppException(Throwable cause) {
		super(cause);
	}
}
