package applications.view;

import apis.CircularOrbitApis;
import applications.tools.BuildVisualPane;
import applications.tools.Timespan;
import circularorbit.PersonalAppEcosystem;
import constant.Regex;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import manager.PersonalAppManager;

public class DifferenceController {

  @FXML
  private StackPane vispane1;

  @FXML
  private StackPane vispane2;

  @FXML
  private TextArea difference;

  @FXML
  private DatePicker sd;

  @FXML
  private TextField st;

  @FXML
  private DatePicker ed;

  @FXML
  private TextField et;

  @FXML
  private DatePicker sd1;

  @FXML
  private TextField st1;

  @FXML
  private DatePicker ed1;

  @FXML
  private TextField et1;

  @FXML
  private Label error;

  private PersonalAppManager pam;
  private Stage stage;

  @FXML
  void handleCancel(ActionEvent event) {
    stage.close();
  }

  @FXML
  void handleFind(ActionEvent event) {
    if (!st.getText().matches(Regex.APPECO_TIME)) {
      error.setText("Start time should be hh:mm:ss");
      return;
    }
    if (!et.getText().matches(Regex.APPECO_TIME)) {
      error.setText("End time should be hh:mm:ss");
      return;
    }
    if (!st1.getText().matches(Regex.APPECO_TIME)) {
      error.setText("Start time should be hh:mm:ss");
      return;
    }
    if (!et1.getText().matches(Regex.APPECO_TIME)) {
      error.setText("End time should be hh:mm:ss");
      return;
    }
    String startDate = null; 
    String endDate = null;
    String startDate1 = null;
    String endDate1 = null;
    if (sd.getValue() != null && ed.getValue() != null) {
      startDate = sd.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString();
      endDate = ed.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString();
    } else {
      error.setText("Please choose a date");
      return;
    }
    if (sd1.getValue() != null && ed1.getValue() != null) {
      startDate1 = sd1.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString();
      endDate1 = ed1.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString();
    } else {
      error.setText("Please choose a date");
      return;
    }
    error.setText("");
    String startTime1 = st1.getText();
    String endTime1 = et1.getText();
    String startTime = st.getText();
    String endTime = et.getText();
    Instant start = Instant.parse(startDate + "T" + startTime + "Z");
    Instant end = Instant.parse(endDate + "T" + endTime + "Z");
    Instant start1 = Instant.parse(startDate1 + "T" + startTime1 + "Z");
    Instant end1 = Instant.parse(endDate1 + "T" + endTime1 + "Z");
    if (start.isAfter(end)) {
      error.setText("Start should early than End");
      return;
    }
    if (start1.isAfter(end1)) {
      error.setText("Start should early than End");
      return;
    }
    List<Timespan> tl = new ArrayList<Timespan>();
    tl.add(new Timespan(start, end));
    tl.add(new Timespan(start1, end1));
    List<PersonalAppEcosystem> li = pam.getTimspanAppEco(tl);
    difference.clear();
    difference.setText(new CircularOrbitApis().getDifference(li.get(0), li.get(1)).toString());
    vispane1.getChildren().clear();
    vispane2.getChildren().clear();
    vispane1.getChildren()
        .add(BuildVisualPane.drawCircularOrbit(
            li.get(0), vispane1.getPrefWidth(), vispane1.getPrefHeight()));
    vispane2.getChildren()
        .add(BuildVisualPane.drawCircularOrbit(
            li.get(1), vispane2.getPrefWidth(), vispane2.getPrefHeight()));
  }

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  public void setPersonalAppManager(PersonalAppManager pam) {
    this.pam = pam;
  }

}
