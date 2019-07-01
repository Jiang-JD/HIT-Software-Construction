package customException.personalAppEcosystemException;

import java.io.IOException;

public class IllegalUsageLogFormatException extends IOException {
	public IllegalUsageLogFormatException() {
		super();
	}
	
	public IllegalUsageLogFormatException(String message) {
		super(message);
	}
	
	public IllegalUsageLogFormatException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public IllegalUsageLogFormatException(Throwable cause) {
		super(cause);
	}
}
