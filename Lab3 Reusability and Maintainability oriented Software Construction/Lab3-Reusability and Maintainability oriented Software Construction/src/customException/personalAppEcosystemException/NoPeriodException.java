package customException.personalAppEcosystemException;

import java.io.IOException;

public class NoPeriodException extends IOException {
	public NoPeriodException() {
		super();
	}
	
	public NoPeriodException(String message) {
		super(message);
	}
	
	public NoPeriodException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public NoPeriodException(Throwable cause) {
		super(cause);
	}
}
