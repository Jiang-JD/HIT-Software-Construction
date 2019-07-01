package customException.personalAppEcosystemException;

import java.io.IOException;

public class InstallLogIllegalTimeFormatException extends IOException {
	public InstallLogIllegalTimeFormatException() {
		super();
	}
	
	public InstallLogIllegalTimeFormatException(String message) {
		super(message);
	}
	
	public InstallLogIllegalTimeFormatException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public InstallLogIllegalTimeFormatException(Throwable cause) {
		super(cause);
	}
}
