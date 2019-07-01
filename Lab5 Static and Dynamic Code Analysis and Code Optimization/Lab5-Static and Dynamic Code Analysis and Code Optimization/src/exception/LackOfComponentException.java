package exception;

/**.
 * 表示元素标签缺少一些组成分量的异常
 *
 */
public class LackOfComponentException extends FileFormatException {
  private String sentence;

  public LackOfComponentException() {
    super();
  }

  public LackOfComponentException(String message) {
    super(message);
  }

  public LackOfComponentException(String message, Throwable cause) {
    super(message, cause);
  }

  public LackOfComponentException(Throwable cause) {
    super(cause);
  }

  public LackOfComponentException(String message, String sentence) {
    super(message);
    this.sentence = sentence;
  }

  public String getSentence() {
    return sentence;
  }
}
