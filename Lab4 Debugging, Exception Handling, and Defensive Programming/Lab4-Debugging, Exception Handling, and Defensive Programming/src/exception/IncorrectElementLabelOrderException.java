package exception;

import java.io.IOException;

/**
 * 表示元素标签中的组成部分顺序错误异常，可能是颠倒或次序不正确
 *
 */
public class IncorrectElementLabelOrderException extends FileFormatException {
	private String sentence;
	
	public IncorrectElementLabelOrderException() {
		super();
	}
	
	public IncorrectElementLabelOrderException(String message) {
		super(message);
	}
	
	public IncorrectElementLabelOrderException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public IncorrectElementLabelOrderException(Throwable cause) {
		super(cause);
	}
	
	public IncorrectElementLabelOrderException(String message, String sentence) {
		super(message);
		this.sentence = sentence;
	}
	
	public String getSentence() {
		return sentence;
	}
}
