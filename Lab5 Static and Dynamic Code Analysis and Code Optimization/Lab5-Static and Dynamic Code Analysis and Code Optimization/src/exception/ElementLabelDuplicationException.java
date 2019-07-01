package exception;

/**.
 * 表示文本输入中出现元素标签重复异常
 *
 */
public class ElementLabelDuplicationException extends FileFormatException {
  private String sentence;

  public ElementLabelDuplicationException() {
    super();
  }

  public ElementLabelDuplicationException(String message) {
    super(message);
  }

  public ElementLabelDuplicationException(String message, Throwable cause) {
    super(message, cause);
  }

  public ElementLabelDuplicationException(Throwable cause) {
    super(cause);
  }

  public ElementLabelDuplicationException(String message, String sentence) {
    super(message);
    this.sentence = sentence;
  }

  public String getSentence() {
    return sentence;
  }
}
