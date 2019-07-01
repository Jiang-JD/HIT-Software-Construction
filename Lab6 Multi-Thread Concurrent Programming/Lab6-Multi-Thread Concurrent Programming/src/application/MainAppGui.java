package application;

import application.view.MenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainAppGui extends Application {
  
  private Stage stage;
  
  @Override
  public void start(Stage arg0) throws Exception {
    stage = arg0;
    this.stage.setTitle("Moneky River");
    initMenu();
  }
  
  public static void main(String[] args) {
    launch();
  }
  
  /**.
   * Initialize Menu window
   */
  public void initMenu() {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(MainAppGui.class.getResource("view/Menu.fxml"));
      AnchorPane root = (AnchorPane) loader.load();
      MenuController controller = loader.getController();
      controller.setStage(stage);
      // show login view
      Scene scene = new Scene(root);
      stage.setScene(scene);
      stage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
