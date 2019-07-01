package exception;

import java.io.IOException;

/**
 *	 表示文本输入中实际轨道数目超过了定义的轨道数量异常。比如文本中定义a条轨道，但是其他输入中
 * 使用的轨道数量不等于a。
 */
public class TrackNumberOutOfRangeException extends FileFormatException {
	private String sentence;
	
	public TrackNumberOutOfRangeException() {
		super();
	}
	
	public TrackNumberOutOfRangeException(String message) {
		super(message);
	}
	
	public TrackNumberOutOfRangeException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public TrackNumberOutOfRangeException(Throwable cause) {
		super(cause);
	}
	
	public TrackNumberOutOfRangeException(String message, String sentence) {
		super(message);
		this.sentence = sentence;
	}
	
	public String getSentence() {
		return sentence;
	}
}
