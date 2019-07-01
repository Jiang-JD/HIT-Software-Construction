package applications.view;

import circularorbit.AtomStructure;
import constant.SystemType;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import manager.PersonalAppManager;
import manager.TrackGameManager;
import parser.AtomStructureParser;
import parser.BufferIo;
import parser.ChannelIo;
import parser.FileStreamIo;
import parser.IoStrategy;
import parser.ReaderWriterIo;
import parser.ScannerIo;

public class OutputController {

  @FXML
  private TextField fileName;

  @FXML
  private TextField fileDirectory;

  @FXML
  private TextField filePath;

  @FXML
  private Label error;
  
  @FXML
  private ChoiceBox<String> ioStrategy;
  
  private Stage stage;
  private AtomStructure as;
  private TrackGameManager tgm;
  private PersonalAppManager pam;
  private SystemType type;
  private List<IoStrategy> iolist = Arrays.asList(new ChannelIo(),
      new BufferIo(), new FileStreamIo(), new ReaderWriterIo());
  private IoStrategy io = new ChannelIo();

  @FXML
  void handleCancel(ActionEvent event) {
    stage.close();
  }
  
  @FXML
  void handleFileNameChange(InputMethodEvent event) {
  }

  @FXML
  void handleChoose(ActionEvent event) {
    DirectoryChooser chooser = new DirectoryChooser();
    File file = chooser.showDialog(stage);
    if (file == null) {
      return;
    }
    fileDirectory.setText(file.getPath());
    filePath.setText(fileDirectory.getText() + "\\" + fileName.getText() + ".txt");
  }

  @FXML
  void handleOutput(ActionEvent event) {
    if (!checkFilePath()) {
      return;
    }
    error.setText("");
    try {
      if (type.equals(SystemType.AtomStructure)) {

        new AtomStructureParser().outPutFile(as, fileDirectory.getText() 
                + "\\" + fileName.getText() + ".txt", io);
        error.setText("Success!");
      } else if (type.equals(SystemType.TrackGame)) {
        tgm.outPutFile(fileDirectory.getText() 
                + "\\" + fileName.getText() + ".txt", io);
        error.setText("Success!");
      } else if (type.equals(SystemType.PersonalAppEcosystem)) {
        pam.outPutFile(fileDirectory.getText() 
                + "\\" + fileName.getText() + ".txt", io);
        error.setText("Success!");
      }
    } catch (IOException e) {
      error.setText("File output error, please try again");
      return;
    }
  }
  
  @FXML
  private void initialize() {
    fileName.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(
          ObservableValue<? extends String> observable, String oldValue, String newValue) {
          filePath.setText(fileDirectory.getText() + "\\" + fileName.getText() + ".txt");
      }
    });
    ioStrategy.setItems(
        FXCollections.observableArrayList("Channel IO", "Buffer IO", 
                                      "File Stream IO", "Reader IO"));

    ioStrategy.getSelectionModel().selectedIndexProperty()
        .addListener((ObservableValue<? extends Number> ov, Number oldVal, Number newVal) -> {
          updateIoStrategy(iolist.get(newVal.intValue()));
        });
    ioStrategy.setValue("Channel IO");
  }
  
  private boolean checkFilePath() {
    String filename = fileName.getText() + ".txt";
    String fileDirec = fileDirectory.getText();
    File file = new File(fileDirec, filename);
    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException e) {
        error.setText("Create file error, please try again");
        return false;
      }
    }
    return true;
  }
  
  private void updateIoStrategy(IoStrategy io) {
    this.io = io;
  }
  
  /**
   * .Set Stage
   * @param stage stage
   */
  public void setStage(Stage stage) {
    this.stage = stage;
  }
  
  public void setAtomStructure(AtomStructure as) {
    this.as = as;
  }
  
  public void setTrackGame(TrackGameManager tgm) {
    this.tgm = tgm;
  }
  
  public void setPersonalAppEcosystem(PersonalAppManager pam) {
    this.pam = pam;
  }
  
  public void setSystem(SystemType type) {
    this.type = type;
  }

}
