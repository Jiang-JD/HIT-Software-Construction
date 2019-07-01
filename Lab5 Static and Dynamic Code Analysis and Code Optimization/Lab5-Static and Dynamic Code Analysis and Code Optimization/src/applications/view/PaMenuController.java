package applications.view;

import apis.CircularOrbitApis;
import applications.MainAppGui;
import circularorbit.PersonalAppEcosystem;

import java.util.List;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import manager.PersonalAppManager;
import physicalobject.App;

public class PaMenuController {

  @FXML
  private TableView<PersonalAppEcosystem> groups;

  @FXML
  private TableColumn<PersonalAppEcosystem, String> indexColumn;

  @FXML
  private TableColumn<PersonalAppEcosystem, String> durationColumn;

  @FXML
  private TableView<App> appstable;

  @FXML
  private TableColumn<App, String> appsColumn;

  @FXML
  private TableView<Integer> trackmap;

  @FXML
  private TableColumn<Integer, String> trackColumn;

  @FXML
  private TableColumn<Integer, String> trackappColumn;

  @FXML
  private Label name;

  @FXML
  private Label duration;

  @FXML
  private Label appsnum;

  @FXML
  private Label entropy;

  private MainAppGui mainapp;
  private PersonalAppManager pam;
  private PersonalAppManager pamclone;
  private ObservableList<PersonalAppEcosystem> pl = FXCollections.observableArrayList();
  private ObservableList<PersonalAppEcosystem> backup = FXCollections.observableArrayList();
  private ObservableList<App> al = FXCollections.observableArrayList();
  private ObservableList<Integer> il = FXCollections.observableArrayList();
  private PersonalAppEcosystem current;

  @FXML
  void handleAbout(ActionEvent event) {

  }

  @FXML
  void handleDifference(ActionEvent event) {
    mainapp.initDifference(pam);
  }

  @FXML
  void handleDistance(ActionEvent event) {
    mainapp.initDistance(pam);
  }

  @FXML
  void handleEdit(ActionEvent event) {
    if (current == null) {
      return;
    }
    mainapp.initDuration(current, pam);
  }

  /**.
   * Update the appeco in the edit operation
   * 
   * @param newp new appeco
   */
  public void editAppEco(PersonalAppEcosystem newp) {
    int index = pl.indexOf(current);
    pl.set(index, newp);
    current = pl.get(index);
    updatePersonalAppEcosystem(current);
  }

  @FXML
  void handleLog(ActionEvent event) {
    mainapp.initLogSystem();
  }

  @FXML
  void handleRecovery(ActionEvent event) {
    pam = pamclone.clone();
    List<PersonalAppEcosystem> list = pam.generateEcosystems();
    final int index = pl.indexOf(current); //arrays pl change after this code, can not change
    if (index == -1) {
      return;
    }
    pl.clear();
    for (PersonalAppEcosystem p : list) {
      pl.add(p);
    }
    groups.setItems(pl);
    al.clear();
    for (App a : pam.getApps()) {
      al.add(a);
    }
    appstable.setItems(al);
    current = pl.get(index);
    updatePersonalAppEcosystem(current);
  }
  
  

  @FXML
  void handleVisualize(ActionEvent event) {
    if (current == null) {
      return;
    }
    mainapp.initVisualize(current);
  }
  
  @FXML
  void handleOutput(ActionEvent event) {
    mainapp.initOutput(pam);
  }

  @FXML
  private void initialize() {
    trackmap.setPlaceholder(new Label("Please select a app ecosystem"));
    indexColumn.setCellValueFactory(cellData -> {
      return new ReadOnlyStringWrapper(String.valueOf(pl.indexOf(cellData.getValue())));
    });
    durationColumn.setCellValueFactory(cellData -> {
      return new ReadOnlyStringWrapper(cellData.getValue().duration().toString());
    });
    trackColumn.setCellValueFactory(cellData -> {
      int i = cellData.getValue();
      return new ReadOnlyStringWrapper(String.valueOf(current.getNumber(i)));
    });
    trackappColumn.setCellValueFactory(cellData -> {
      int i = cellData.getValue();
      StringBuilder sb = new StringBuilder();
      for (App a : current.getObjects(i)) {
        sb.append("[" + a.getName() + ":" + a.getCompany() + ":" + a.getArea() + "] ");
      }
      return new ReadOnlyStringWrapper(sb.toString());
    });
    appsColumn.setCellValueFactory(cellData -> {
      return new ReadOnlyStringWrapper(cellData.getValue().getName());
    });
    groups.getSelectionModel().selectedItemProperty()
        .addListener((observable, oldValue, newValue) -> {
          if (newValue != null) {
            updatePersonalAppEcosystem(newValue);
          }
        });
  }

  /**.
   * Set personal app manager
   * @param pam personal app manager
   */
  public void setPersonalAppManager(PersonalAppManager pam) {
    this.pam = pam;
    pamclone = pam.clone();
    List<PersonalAppEcosystem> list = pam.generateEcosystems();
    pl.clear();
    backup.clear();
    for (PersonalAppEcosystem p : list) {
      pl.add(p);
      backup.add(p.clone());
    }
    groups.setItems(pl);
    for (App a : pam.getApps()) {
      al.add(a);
    }
    appstable.setItems(al);

  }

  public void setMainApp(MainAppGui mainapp) {
    this.mainapp = mainapp;
  }

  private void updatePersonalAppEcosystem(PersonalAppEcosystem pae) {
    current = pae;
    il.clear();

    for (int i = 0; i < current.getTrackNum(); i++) {
      il.add(i);
    }
    name.setText(pae.getName());
    duration.setText(pae.duration().toString());
    appsnum.setText(String.valueOf(pae.getObjectNum()));
    entropy.setText(String.valueOf(new CircularOrbitApis().getObjectDistributionEntropy(pae)));
    trackmap.setItems(il);
    al.clear();
    for (App a : pam.getApps()) {
      al.add(a);
    }
    appstable.setItems(al);
  }

}
