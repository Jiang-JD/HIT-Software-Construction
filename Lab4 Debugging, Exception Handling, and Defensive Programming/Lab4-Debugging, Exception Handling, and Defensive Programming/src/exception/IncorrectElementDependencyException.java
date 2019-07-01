package exception;

import java.io.IOException;

/**
 * 表示元素之间依赖关系不正确的异常。比如定义和使用的元素数量不一致。
 *
 */
public class IncorrectElementDependencyException extends FileFormatException {
	private String sentence;
	
	public IncorrectElementDependencyException() {
		super();
	}
	
	public IncorrectElementDependencyException(String message) {
		super(message);
	}
	
	public IncorrectElementDependencyException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public IncorrectElementDependencyException(Throwable cause) {
		super(cause);
	}
	
	public IncorrectElementDependencyException(String message, String sentence) {
		super(message);
		this.sentence = sentence;
	}
	
	public String getSentence() {
		return sentence;
	}
}
