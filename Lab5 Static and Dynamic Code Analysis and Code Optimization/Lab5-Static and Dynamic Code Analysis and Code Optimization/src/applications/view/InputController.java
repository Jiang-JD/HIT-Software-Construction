package applications.view;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import parser.BufferIo;
import parser.ChannelIo;
import parser.FileStreamIo;
import parser.IoStrategy;
import parser.ReaderWriterIo;
import parser.ScannerIo;

public class InputController {

  @FXML
  private ChoiceBox<String> ioStrategy;

  @FXML
  private TextField filePath;
  
  private Stage stage;
  private File file;
  private List<IoStrategy> iolist = Arrays.asList(new ChannelIo(),
      new BufferIo(), new FileStreamIo(), new ReaderWriterIo(),
      new ScannerIo());
  private IoStrategy io = new ChannelIo();
  private boolean isOK = false;
  
  @FXML
  private void initialize() {
    ioStrategy.setItems(FXCollections.observableArrayList("Channel IO", "Buffer IO",
                                                          "File Stream IO", "Reader IO",
                                                          "Scanner IO"));
    
    ioStrategy.getSelectionModel().selectedIndexProperty()
        .addListener((ObservableValue<? extends Number> ov, Number oldVal, Number newVal) -> {
          updateIoStrategy(iolist.get(newVal.intValue()));
        });
    ioStrategy.setValue("Channel IO");
  }

  @FXML
  void handleCancel(ActionEvent event) {
    stage.close();
  }

  @FXML
  void handleChoose(ActionEvent event) {
    FileChooser fc = new FileChooser();
    fc.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"));
    File file = fc.showOpenDialog(stage);
    String filepath = "";
    if (file != null) {
      this.file = file;
      filePath.setText(file.getPath());
    } else {
      return;
    }
  }

  @FXML
  void handleRead(ActionEvent event) {
    if (!filePath.getText().isEmpty()) {
      isOK = true;
      stage.close();
    }
  }
  
  private void updateIoStrategy(IoStrategy io) {
    this.io = io;
  }
  
  public void setStage(Stage stage) {
    this.stage = stage;
  }
  
  public boolean getIsOK() {
    return isOK;
  }
  
  public IoStrategy getIo() {
    return io;
  }
  
  public File getFile() {
    return file;
  }

}
