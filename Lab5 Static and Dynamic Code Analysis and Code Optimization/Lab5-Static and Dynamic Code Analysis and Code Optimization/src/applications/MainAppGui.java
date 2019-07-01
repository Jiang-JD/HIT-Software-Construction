package applications;

import applications.tools.BuildVisualPane;
import applications.tools.Memento;
import applications.tools.MementoCareTaker;
import applications.view.AsMenuController;
import applications.view.DifferenceController;
import applications.view.DistanceController;
import applications.view.DurationController;
import applications.view.ExchangeController;
import applications.view.FileErrorController;
import applications.view.LogController;
import applications.view.LoginController;
import applications.view.OutputController;
import applications.view.PaMenuController;
import applications.view.RaceViewController;
import applications.view.RestoreController;
import applications.view.TgMenuController;
import applications.view.TransitController;
import circularorbit.AtomStructure;
import circularorbit.CircularOrbit;
import circularorbit.PersonalAppEcosystem;
import circularorbit.TrackGame;
import constant.SystemType;
import exception.FileFormatException;
import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import manager.PersonalAppManager;
import manager.TrackGameManager;

public class MainAppGui extends Application {

  private Stage stage;
  private AnchorPane login;
  private int tgnum1;
  private int tgnum2;
  private int asStartIndex;
  private int asEndIndex;
  private int asNum;
  private Memento asMemento;

  @Override
  public void start(Stage primaryStage) throws Exception {
    stage = primaryStage;
    this.stage.setTitle("CircularOrbitApplication");
    initLogin();

  }

  /**.
   * 初始化登陆界面
   */
  public void initLogin() {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(MainAppGui.class.getResource("view/Login.fxml"));
      login = (AnchorPane) loader.load();
      LoginController controller = loader.getController();
      controller.setMainApp(this);
      // show login view
      Scene scene = new Scene(login);
      stage.setScene(scene);
      stage.show();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * .
   * Initial File Error Window
   * @param file error file
   * @param e exception
   */
  public void initFileErroe(File file, FileFormatException e) {
    Stage errorStage = new Stage();
    errorStage.setTitle("Illegal File Report");
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(MainAppGui.class.getResource("view/FileError.fxml"));
    try {
      AnchorPane fileError = (AnchorPane) loader.load();
      FileErrorController controller = loader.getController();
      controller.setFile(file);
      controller.setException(e);
      Scene scene = new Scene(fileError);
      errorStage.setScene(scene);
      errorStage.show();
    } catch (IOException e1) {
      e1.printStackTrace();
      return;
    }
  }

  private TgMenuController tgmenucontroller;

  /**
   * .
   * Initial Track Game Menu window
   * @param tgm track game manager
   */
  public void initTgMenu(TrackGameManager tgm) {
    try {
      Stage tgstage = new Stage();
      tgstage.setTitle("TrackGame");
      FXMLLoader tgloader = new FXMLLoader();
      tgloader.setLocation(MainAppGui.class.getResource("view/TgMenu.fxml"));
      final AnchorPane tgmenu = (AnchorPane) tgloader.load();
      TgMenuController controller = tgloader.getController();
      tgmenucontroller = controller;
      controller.setTrackGameManager(tgm);
      controller.setMainApp(this);
      Scene scene = new Scene(tgmenu);
      tgstage.setScene(scene);
      tgstage.show();
      //getStage().close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * .Initial a track game edit window
   * @param current current chosen track game
   * @param tgm track game manager
   */
  public void initRaceView(TrackGame current, TrackGameManager tgm) {
    try {
      Stage racestage = new Stage();
      racestage.setTitle(current.getName());
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(MainAppGui.class.getResource("view/RaceView.fxml"));
      AnchorPane raceview;
      raceview = (AnchorPane) loader.load();
      RaceViewController controller = loader.getController();
      controller.setTrackGame(current);
      controller.setStage(racestage);
      controller.setTrackGameManager(tgm);
      controller.setTgMenuController(tgmenucontroller);
      controller.refreshVis();
      Scene scene = new Scene(raceview);
      racestage.setScene(scene);
      racestage.showAndWait();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * .Initial Exchange window
   * @param tgm track game manager
   * @return is exchange
   */
  public boolean initExchange(TrackGameManager tgm) {
    try {
      Stage exchangestage = new Stage();
      exchangestage.setTitle("Exchange Two Athletes");
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(MainAppGui.class.getResource("view/Exchange.fxml"));
      AnchorPane exchange;
      exchange = (AnchorPane) loader.load();
      Scene scene = new Scene(exchange);
      ExchangeController controller = loader.getController();
      controller.setStage(exchangestage);
      controller.setTrackGameManager(tgm);
      exchangestage.setScene(scene);
      exchangestage.showAndWait();
      if (controller.isOK_Clicked()) {
        tgnum1 = controller.getnum1();
        tgnum2 = controller.getnum2();
        return true;
      } else {
        return false;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  /**.
   * Get TrackGame exchange number
   */
  public int getTgNum1() {
    return tgnum1;
  }

  /**.
   * Get TrackGame exchange number
   * 
   * @return number2
   */
  public int getTgNum2() {
    return tgnum2;
  }

  /**.
   * Initialize AS Menu
   */
  public void initAsMenu(AtomStructure as) {
    try {
      Stage stage = new Stage();
      stage.setTitle("AtomStructure");
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(MainAppGui.class.getResource("view/AsMenu.fxml"));
      AnchorPane menu = (AnchorPane) loader.load();
      Scene scene = new Scene(menu);
      AsMenuController controller = loader.getController();
      controller.setMainApp(this);
      controller.setAtomStructure(as);
      stage.setScene(scene);
      stage.show();
      //getStage().close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * .Initial transit window
   * @param as Atom structure
   * @return is transit
   */
  public boolean initTransit(AtomStructure as) {
    try {
      Stage stage = new Stage();
      stage.setTitle("Transit");
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(MainAppGui.class.getResource("view/Transit.fxml"));
      AnchorPane menu = (AnchorPane) loader.load();
      Scene scene = new Scene(menu);
      TransitController controller = loader.getController();
      controller.setAS(as);
      controller.setStage(stage);
      stage.setScene(scene);
      stage.showAndWait();
      if (controller.isOK_Clicked()) {
        asStartIndex = controller.getStartIndex();
        asEndIndex = controller.getEndIndex();
        asNum = controller.getEleNumber();
        return true;
      } else {
        return false;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  public int getAsStartIndex() {
    return asStartIndex;
  }

  public int getAsEndIndex() {
    return asEndIndex;
  }

  public int getEleNumber() {
    return asNum;
  }

  /**
   * .Initial Restore window
   * @param mck moment care taker
   * @return is restore
   */
  public boolean initRestore(MementoCareTaker mck) {
    try {
      Stage stage = new Stage();
      stage.setTitle("Resotre");
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(MainAppGui.class.getResource("view/Restore.fxml"));
      AnchorPane menu = (AnchorPane) loader.load();
      Scene scene = new Scene(menu);
      RestoreController controller = loader.getController();
      controller.setMementoCareTaker(mck);
      controller.setStage(stage);
      stage.setScene(scene);
      stage.showAndWait();
      if (controller.isOK_Clicked()) {
        asMemento = controller.getMemento();
        return true;
      } else {
        return false;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  public Memento getBackUp() {
    return asMemento;
  }

  private PaMenuController pamenucontroller;

  /**
   * .Initial Personal App eco system menu window 
   * @param pam Personal app manager
   */
  public void initPaMenu(PersonalAppManager pam) {
    try {
      Stage stage = new Stage();
      stage.setTitle("PersonalAppEcosystem");
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(MainAppGui.class.getResource("view/PaMenu.fxml"));
      final AnchorPane menu = (AnchorPane) loader.load();
      PaMenuController controller = loader.getController();
      pamenucontroller = controller;
      controller.setPersonalAppManager(pam);
      controller.setMainApp(this);
      Scene scene = new Scene(menu);
      stage.setScene(scene);
      stage.show();
      //getStage().close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * .Initial current system edit window
   * @param current current app system
   * @param pam personal app manager
   */
  public void initDuration(PersonalAppEcosystem current, PersonalAppManager pam) {
    try {
      Stage durstage = new Stage();
      durstage.setTitle(current.getName());
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(MainAppGui.class.getResource("view/Duration.fxml"));
      AnchorPane view;
      view = (AnchorPane) loader.load();
      DurationController controller = loader.getController();
      controller.setPersonalAppEcosystem(current);
      controller.setStage(durstage);
      controller.setPersonalAppManager(pam);
      controller.setPaMenuController(pamenucontroller);
      controller.refreshVis(); 
      Scene scene = new Scene(view);
      durstage.setScene(scene);
      durstage.showAndWait();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * .Initial logic diatance window
   * @param pam personal app manager
   */
  public void initDistance(PersonalAppManager pam) {
    try {
      Stage stage = new Stage();
      stage.setTitle("Logic Distance");
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(MainAppGui.class.getResource("view/Distance.fxml"));
      AnchorPane menu = (AnchorPane) loader.load();
      Scene scene = new Scene(menu);
      DistanceController controller = loader.getController();
      controller.setPersonalAppManager(pam);
      controller.setStage(stage);
      stage.setScene(scene);
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * .Initial get Difference window
   * @param pam personal app manager
   */
  public void initDifference(PersonalAppManager pam) {
    try {
      Stage stage = new Stage();
      stage.setTitle("Difference");
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(MainAppGui.class.getResource("view/Difference.fxml"));
      AnchorPane menu = (AnchorPane) loader.load();
      Scene scene = new Scene(menu);
      DifferenceController controller = loader.getController();
      controller.setPersonalAppManager(pam);
      controller.setStage(stage);
      stage.setScene(scene);
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void initVisualize(CircularOrbit co) {
    Stage visstage = BuildVisualPane.drawCircularOrbit(co);
    visstage.show();
  }

  /**
   * .Initial log system window
   */
  public void initLogSystem() {
    try {
      Stage stage = new Stage();
      stage.setTitle("Log System");
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(MainAppGui.class.getResource("view/Log.fxml"));
      AnchorPane menu = (AnchorPane) loader.load();
      Scene scene = new Scene(menu);
      LogController controller = loader.getController();
      controller.setStage(stage);
      stage.setScene(scene);
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * .Initial Atom structure file output window
   * @param as Atom structure needed be output
   */
  public void initOutput(AtomStructure as) {
    try {
      Stage stage = new Stage();
      stage.setTitle("Atom Structure Output");
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(MainAppGui.class.getResource("view/Output.fxml"));
      AnchorPane menu = (AnchorPane) loader.load();
      OutputController controller = loader.getController();
      controller.setStage(stage);
      controller.setAtomStructure(as);
      controller.setSystem(SystemType.AtomStructure);
      Scene scene = new Scene(menu);
      stage.setScene(scene);
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  /** 
   * .Initial Track Game file output window
   * @param tgm Track Game Manager need to output
   */
  public void initOutput(TrackGameManager tgm) {
    try {
      Stage stage = new Stage();
      stage.setTitle("TrackGame Output");
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(MainAppGui.class.getResource("view/Output.fxml"));
      AnchorPane menu = (AnchorPane) loader.load();
      OutputController controller = loader.getController();
      controller.setStage(stage);
      controller.setTrackGame(tgm);
      controller.setSystem(SystemType.TrackGame);
      Scene scene = new Scene(menu);
      stage.setScene(scene);
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * .Initial Personal App Ecosystem file output window 
   * @param pam Personal App Manager need to output
   */
  public void initOutput(PersonalAppManager pam) {
    try {
      Stage stage = new Stage();
      stage.setTitle("PersonalAppEcosystem Output");
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(MainAppGui.class.getResource("view/Output.fxml"));
      AnchorPane menu = (AnchorPane) loader.load();
      OutputController controller = loader.getController();
      controller.setStage(stage);
      controller.setPersonalAppEcosystem(pam);
      controller.setSystem(SystemType.PersonalAppEcosystem);
      Scene scene = new Scene(menu);
      stage.setScene(scene);
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    launch(args);
  }

  /**.
   * Get the current stage, login
   * 
   * @return current stage
   */
  public Stage getStage() {
    return stage;
  }

}
