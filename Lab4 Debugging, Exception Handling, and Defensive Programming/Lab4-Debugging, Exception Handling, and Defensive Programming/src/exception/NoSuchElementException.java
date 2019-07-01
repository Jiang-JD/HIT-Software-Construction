package exception;

import java.io.IOException;

/**
 * 表示文本输入中缺少某一类元素标签的异常。比如缺少文本输入中要求的一些必须要存在
 * 的元素类型。
 */
public class NoSuchElementException extends FileFormatException {
	private String sentence;
	
	public NoSuchElementException() {
		super();
	}
	
	public NoSuchElementException(String message) {
		super(message);
	}
	
	public NoSuchElementException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public NoSuchElementException(Throwable cause) {
		super(cause);
	}
	
	public NoSuchElementException(String message, String sentence) {
		super(message);
		this.sentence = sentence;
	}
	
	public String getSentence() {
		return sentence;
	}
}
