package exception;

/**.
 * 表示在文本输入中使用的元素在文本中并没有定义的异常，比如在输入中某个输入构建一个关于 元素A的关系，但是输入中并没有关于A定义的语句。
 *
 */
public class UndefinedElementException extends FileFormatException {
  private String sentence;

  public UndefinedElementException() {
    super();
  }

  public UndefinedElementException(String message) {
    super(message);
  }

  public UndefinedElementException(String message, Throwable cause) {
    super(message, cause);
  }

  public UndefinedElementException(Throwable cause) {
    super(cause);
  }

  public UndefinedElementException(String message, String sentence) {
    super(message);
    this.sentence = sentence;
  }

  public String getSentence() {
    return sentence;
  }
}
