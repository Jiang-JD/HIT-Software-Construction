package applications.view;

import exception.FileFormatException;

import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FileErrorController {

  @FXML
  private TextField label;

  @FXML
  private TextField filepath;

  @FXML
  private TextArea textfield;

  public void setFile(File file) {
    label.setText(file.getName());
    filepath.setText(file.getPath());
  }

  /**.
   * Set file exception 
   * @param e file error exception
   */
  public void setException(FileFormatException e) {
    String info = "Exception Class: " + e.getClass().getName() 
        + "\nException Message: " + e.getMessage()
        + "\nException Sentence: " + e.getSentence();
    textfield.setText(info);
  }

}
