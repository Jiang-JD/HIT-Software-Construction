package customException.atomStructureException;

import java.io.IOException;

public class SequenceTrackNumberException extends IOException {
	public SequenceTrackNumberException() {
		super();
	}
	
	public SequenceTrackNumberException(String message) {
		super(message);
	}
	
	public SequenceTrackNumberException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public SequenceTrackNumberException(Throwable cause) {
		super(cause);
	}
}
