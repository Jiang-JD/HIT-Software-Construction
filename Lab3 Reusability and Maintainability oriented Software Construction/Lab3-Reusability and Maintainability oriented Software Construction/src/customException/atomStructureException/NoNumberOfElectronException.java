package customException.atomStructureException;

import java.io.IOException;

public class NoNumberOfElectronException extends IOException {
	public NoNumberOfElectronException() {
		super();
	}
	
	public NoNumberOfElectronException(String message) {
		super(message);
	}
	
	public NoNumberOfElectronException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public NoNumberOfElectronException(Throwable cause) {
		super(cause);
	}
}
