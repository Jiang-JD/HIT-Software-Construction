package applications.view;

import applications.MainAppGui;
import circularorbit.AtomStructure;
import exception.ElementLabelDuplicationException;
import exception.FileFormatException;
import exception.IllegalElementFormatException;
import exception.IncorrectElementDependencyException;
import exception.IncorrectElementLabelOrderException;
import exception.LackOfComponentException;
import exception.NoSuchElementException;
import exception.TrackNumberOutOfRangeException;
import exception.UndefinedElementException;
import java.io.File;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import manager.PersonalAppManager;
import manager.TrackGameManager;

import org.apache.log4j.Logger;

import parser.AtomStructureParser;
import parser.ChannelIo;
import parser.IoStrategy;

public class LoginController {
  private MainAppGui mainapp;

  @FXML
  private Button btnTrackgame;

  @FXML
  private Button btnAtomstructure;

  @FXML
  private Button btnAppeco;

  private Logger logger = Logger.getLogger(LoginController.class);

  @FXML
  private void handleOpenFile_TG() {
    boolean f = true;
    File file = null;
    IoStrategy io = new ChannelIo();
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(MainAppGui.class.getResource("view/Input.fxml"));
      AnchorPane input = (AnchorPane) loader.load();
      InputController controller = loader.getController();
      
      // show login view
      Scene scene = new Scene(input);
      Stage stage = new Stage();
      stage.setTitle("Choose File");
      controller.setStage(stage);
      stage.setScene(scene);
      stage.showAndWait();
      if (controller.getIsOK()) {
        file = controller.getFile(); 
        io = controller.getIo();
      } else {
        return;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    TrackGameManager tgm = new TrackGameManager();
    try {
      tgm.initial(file.getPath(), io);
    } catch (IllegalElementFormatException | NoSuchElementException 
        | IncorrectElementLabelOrderException
        | LackOfComponentException | ElementLabelDuplicationException 
        | TrackNumberOutOfRangeException e) {
      initFileError(file, e);
      f = false;
    }
    logger.info("User open the TrackGame: " + file.getPath());
    if (f) {
      mainapp.initTgMenu(tgm);
    }
  }

  @FXML
  private void handleOpenFile_AS() {
    boolean f = true;
    File file = null;
    IoStrategy io = new ChannelIo();
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(MainAppGui.class.getResource("view/Input.fxml"));
      AnchorPane input = (AnchorPane) loader.load();
      InputController controller = loader.getController();
      
      // show login view
      Scene scene = new Scene(input);
      Stage stage = new Stage();
      stage.setTitle("Choose File");
      controller.setStage(stage);
      stage.setScene(scene);
      stage.showAndWait();
      if (controller.getIsOK()) {
        file = controller.getFile(); 
        io = controller.getIo();
      } else {
        return;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    AtomStructure as = null;
    AtomStructureParser asp = new AtomStructureParser();
    try {
      as = asp.initial(file.getPath(), io);
    } catch (NoSuchElementException | IllegalElementFormatException 
        | TrackNumberOutOfRangeException
        | IncorrectElementLabelOrderException 
        | IncorrectElementDependencyException 
        | LackOfComponentException
        | ElementLabelDuplicationException e) {
      f = false;
      initFileError(file, e);
    }
    logger.info("User open the AtomStructure: " + file.getPath());
    if (f) {
      mainapp.initAsMenu(as);
    }

  }

  @FXML
  private void handleOpenFile_AE() {
    boolean f = true;
    File file = null;
    IoStrategy io = new ChannelIo();
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(MainAppGui.class.getResource("view/Input.fxml"));
      AnchorPane input = (AnchorPane) loader.load();
      InputController controller = loader.getController();
      
      // show login view
      Scene scene = new Scene(input);
      Stage stage = new Stage();
      stage.setTitle("Choose File");
      controller.setStage(stage);
      stage.setScene(scene);
      stage.showAndWait();
      if (controller.getIsOK()) {
        file = controller.getFile(); 
        io = controller.getIo();
      } else {
        return;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    PersonalAppManager pam = new PersonalAppManager();
    try {
      pam.initial(file.getPath(), io);
    } catch (UndefinedElementException | IllegalElementFormatException 
        | NoSuchElementException
        | LackOfComponentException 
        | ElementLabelDuplicationException 
        | IncorrectElementDependencyException e) {
      f = false;
      initFileError(file, e);
    }
    logger.info("User open the PersonalAppEcosystem: " + file.getPath());
    if (f) {
      mainapp.initPaMenu(pam);
    }
  }

  @FXML
  void handleLog(ActionEvent event) {
    mainapp.initLogSystem();
  }

  public void setMainApp(MainAppGui mainapp) {
    this.mainapp = mainapp;
  }

  private void initFileError(File file, FileFormatException e) {
    mainapp.initFileErroe(file, e);
  }

}
