package exception;

import java.io.IOException;

public class FileFormatException extends IOException {
  /**.
   * 
   */
  private static final long serialVersionUID = 1L;
  private String sentence;

  public FileFormatException() {
    super();
  }

  public FileFormatException(String message) {
    super(message);
  }

  public FileFormatException(String message, Throwable cause) {
    super(message, cause);
  }

  public FileFormatException(Throwable cause) {
    super(cause);
  }

  public FileFormatException(String message, String sentence) {
    super(message);
    this.sentence = sentence;
  }

  /**.
   * Get error sentence
   * 
   * @return sentence
   */
  public String getSentence() {
    return sentence;
  }
}
