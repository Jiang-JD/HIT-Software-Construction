package customException.trackGameException;

import java.io.IOException;

/**
 * 解析运动员国籍时出现的格式异常，运动员格式应为3个大写字母，如果解析器解析错误则会抛出次异常。
 */
public class IncorrectNationFormatException extends IOException {

	public IncorrectNationFormatException() {
		super();
	}
	
	public IncorrectNationFormatException(String message) {
		super(message);
	}
	
	public IncorrectNationFormatException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public IncorrectNationFormatException(Throwable cause) {
		super(cause);
	}

}
