package exception;

/**.
 * 表示元素中分量格式不符合规则异常
 *
 */
public class IllegalElementFormatException extends FileFormatException {
  private String sentence;

  public IllegalElementFormatException() {
    super();
  }

  public IllegalElementFormatException(String message) {
    super(message);
  }

  public IllegalElementFormatException(String message, Throwable cause) {
    super(message, cause);
  }

  public IllegalElementFormatException(Throwable cause) {
    super(cause);
  }

  public IllegalElementFormatException(String message, String sentence) {
    super(message);
    this.sentence = sentence;
  }

  public String getSentence() {
    return sentence;
  }
}
