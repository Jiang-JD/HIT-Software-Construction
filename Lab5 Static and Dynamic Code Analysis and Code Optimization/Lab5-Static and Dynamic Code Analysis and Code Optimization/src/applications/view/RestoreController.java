package applications.view;

import applications.tools.Memento;
import applications.tools.MementoCareTaker;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import org.apache.log4j.Logger;

public class RestoreController {

  @FXML
  private TableView<Memento> mementos;

  @FXML
  private Label info;

  @FXML
  private TableColumn<Memento, String> indexColumn;

  @FXML
  private TableColumn<Memento, String> timeColumn;

  @FXML
  private TableColumn<Memento, String> detailColumn;

  private Stage stage;
  private MementoCareTaker mck;
  private boolean isOK = false;
  private ObservableList<Memento> ml = FXCollections.observableArrayList();
  private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");// 设置时间日期格式
  private Memento current = null;
  private String hint = "Click to choose the memento to restore";
  private Logger logger = Logger.getLogger(RestoreController.class);

  @FXML
  void handleCancel(ActionEvent event) {
    isOK = false;
    stage.close();
  }

  @FXML
  void handleRestore(ActionEvent event) {
    if (current == null) {
      info.setText("Please select a memento to restore!");
      return;
    }
    info.setText(hint);
    isOK = true;
    logger.info("User restore the atom structure :" + current.toString());
    stage.close();
  }

  @FXML
  private void initialize() {
    indexColumn.setCellValueFactory(cellDate -> {
      return new ReadOnlyStringWrapper(String.valueOf(mck.indexOf(cellDate.getValue())));
    });
    timeColumn.setCellValueFactory(cellDate -> {
      return new ReadOnlyStringWrapper(String.valueOf(cellDate.getValue().getTime().format(dtf)));
    });
    detailColumn.setCellValueFactory(cellDate -> {
      StringBuilder sb = new StringBuilder();
      Memento m = cellDate.getValue();
      for (int i = 0; i < m.getTracks().size(); i++) {
        sb.append(
            "[Track:" + m.getTracks().get(i).getNumber() 
              + "/" + m.getMap().get(m.getTracks().get(i)).size() + "]");
      }
      return new ReadOnlyStringWrapper(sb.toString());
    });
    mementos.getSelectionModel().selectedItemProperty()
        .addListener((observable, oldValue, newValue) -> updateMemento(newValue));
  }

  private void updateMemento(Memento m) {
    current = m;
  }

  public boolean isOK_Clicked() {
    return isOK;
  }

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  /**
   * . Set care taker
   * @param mck memory care taker
   */
  public void setMementoCareTaker(MementoCareTaker mck) {
    this.mck = mck;
    ml.clear();
    for (int i = 0; i < mck.size(); i++) {
      ml.add(mck.get(i));
    }
    mementos.setItems(ml);
  }

  public Memento getMemento() {
    return current;
  }

}
