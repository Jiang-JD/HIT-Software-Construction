package customException.personalAppEcosystemException;

import java.io.IOException;

public class UninstallLogIllegalTimeFormatException extends IOException {
	public UninstallLogIllegalTimeFormatException() {
		super();
	}
	
	public UninstallLogIllegalTimeFormatException(String message) {
		super(message);
	}
	
	public UninstallLogIllegalTimeFormatException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public UninstallLogIllegalTimeFormatException(Throwable cause) {
		super(cause);
	}
}
