package customException.trackGameException;

import java.io.IOException;

public class AthleteRepeatInputException extends IOException {

	public AthleteRepeatInputException() {
			super();
		}
		
		public AthleteRepeatInputException(String message) {
			super(message);
		}
		
		public AthleteRepeatInputException(String message, Throwable cause) {
			super(message, cause);
		}
		
		public AthleteRepeatInputException(Throwable cause) {
			super(cause);
		}

}
