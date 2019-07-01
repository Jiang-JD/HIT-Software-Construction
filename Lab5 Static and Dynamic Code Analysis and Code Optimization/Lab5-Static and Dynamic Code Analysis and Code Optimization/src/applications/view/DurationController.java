package applications.view;

import apis.CircularOrbitApis;
import applications.tools.AppInstallTime;
import applications.tools.BuildVisualPane;
import applications.tools.Timespan;
import applications.tools.UsageLog;
import circularorbit.PersonalAppEcosystem;
import constant.Regex;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import manager.PersonalAppManager;

import org.apache.log4j.Logger;

import physicalobject.App;
import physicalobject.AppFactory;

public class DurationController {

  @FXML
  private TextField name;

  @FXML
  private TextField company;

  @FXML
  private TextField version;

  @FXML
  private TextField area;

  @FXML
  private TextArea description;

  @FXML
  private Label erroradd;

  @FXML
  private TextField removename;

  @FXML
  private Label errorremove;

  @FXML
  private Label entropy;

  @FXML
  private TextField index;

  @FXML
  private StackPane vispane;

  private PersonalAppEcosystem current;
  private PersonalAppEcosystem tmp;
  private PersonalAppManager pam;
  private Stage stage;
  private AppFactory af = new AppFactory();
  private List<App> adda = new ArrayList<App>();
  private List<AppInstallTime> addt = new ArrayList<AppInstallTime>();
  private List<App> removea = new ArrayList<App>();
  private List<AppInstallTime> removet = new ArrayList<AppInstallTime>();
  private PaMenuController pac;
  private Logger logger = Logger.getLogger(Duration.class);

  @FXML
  void handleAdd(ActionEvent event) {
    if (checkAdd()) {
      App a = (App) af.create(
          "App ::= <" + name.getText() + "," + company.getText() + "," + version.getText() + ",\""
          + description.getText() + "\",\"" + area.getText() + "\">");
      adda.add(a);
      AppInstallTime at = pam.getInstallTime(a.getName());
      if (at == null) {
        at = new AppInstallTime();
      }
      at.add(tmp.duration());
      addt.add(at);
      tmp.addApp(a, Integer.parseInt(index.getText()), at);
      refreshVis();
    }
  }

  @FXML
  void handleCancel(ActionEvent event) {
    stage.close();
  }

  @FXML
  void handleRemove(ActionEvent event) {
    if (checkRemove()) {
      App a = tmp.getApp(removename.getText());
      removea.add(a);
      //移除相关使用记录
      List<UsageLog> ul = pam.getUsageLog();
      List<UsageLog> removeul = new ArrayList<UsageLog>();
      for (UsageLog u : ul) {
        if (u.getName().equals(a.getName()) && u.within(tmp.duration())) {
          removeul.add(u);
        }
      }
      ul.removeAll(removeul); //移除相关使用日志
      AppInstallTime installtime = pam.getInstallTime(a.getName()); //拿到的是管理器中的时间段引用，相当于直接修改map中的对象
      for (int i = 0; i < installtime.size(); i++) {
        if (installtime.get(i).overlap(tmp.duration())) {
          /*
           * 如果安装时间段包含了这个生态系统所表示的时间段
           * 那么就将这个安装时间段分割为两个部分
           */
          if (installtime.get(i).getStart().isBefore(tmp.duration().getStart())
                  && installtime.get(i).getEnd().isAfter(tmp.duration().getEnd())) {
            Timespan before = new Timespan(installtime.get(i).getStart(), 
                                          tmp.duration().getStart()); //before duration
            Timespan after = new Timespan(tmp.duration().getEnd(), 
                                          installtime.get(i).getEnd()); //after duration
            installtime.remove(installtime.get(i));
            installtime.add(before);
            installtime.add(after);
          /*
           * 如果安装时间段前半段与生态系统重合
           * 将重合部分删去 
           */
          } else if (installtime.get(i).getStart().isBefore(tmp.duration().getStart())
                    && installtime.get(i).getEnd().isBefore(tmp.duration().getEnd())) {
            Timespan before = new Timespan(installtime.get(i).getStart(),
                                         tmp.duration().getStart());
            installtime.remove(installtime.get(i));
            installtime.add(before);
          /*
           * 如果安装时间段后半段与生态系统重合
           * 将重合部分删去
           */
          } else if (installtime.get(i).getStart().isAfter(tmp.duration().getStart())
              && installtime.get(i).getEnd().isAfter(tmp.duration().getEnd())) {
            Timespan after = new Timespan(tmp.duration().getEnd(), installtime.get(i).getEnd());
            installtime.remove(installtime.get(i));
            installtime.add(after);
          /*
           * 如果安装时间段在生态系统时间段内部
           * 将这个安装时间段删去
           */
          } else if ((installtime.get(i).getStart().isAfter(tmp.duration().getStart()) 
                      || (installtime.get(i).getStart().equals(tmp.duration().getStart())))
                      && installtime.get(i).getEnd().isBefore(tmp.duration().getEnd()) 
                      || (installtime.get(i).getEnd().equals(tmp.duration().getEnd()))) {
            installtime.remove(installtime.get(i));
          }
        }
      }
      tmp.remove(a);
      refreshVis();

    }
  }

  @FXML
  void handleSave(ActionEvent event) {
    if (!adda.isEmpty()) {
      pam.addAll(adda, addt);
      logger.info("User add new apps: " + adda.toString());
    }
    if (!removea.isEmpty()) {
      logger.info("User remove a app: " + removea.toString());
    }
    pac.editAppEco(tmp);
    stage.close();
  }

  public PersonalAppEcosystem getNewEco() {
    return tmp;
  }

  @FXML
  void initialize() {
  }

  private boolean checkAdd() {
    if (!name.getText().matches(Regex.REGEX_LABEL)) {
      erroradd.setText("Name should be label type, try again");
      return false;
    }
    if (pam.getApp(name.getText()) != null) {
      erroradd.setText("App already exist, try again");
      return false;
    }
    if (!company.getText().matches(Regex.REGEX_LABEL)) {
      erroradd.setText("Company should be label type, try again");
      return false;
    }
    if (!version.getText().matches(Regex.APPECO_VERSION)) {
      erroradd.setText("Version illegal type, try again");
      return false;
    }
    if (!description.getText().matches(Regex.REGEX_SENTENCE)) {
      erroradd.setText("Description should be sentence type, try again");
      return false;
    }
    if (!area.getText().matches(Regex.REGEX_SENTENCE)) {
      erroradd.setText("Area should be sentence type, try again");
      return false;
    }
    if (!index.getText().matches("^\\d+$")) {
      erroradd.setText("Track Number illegal, please try again!");
      return false;
    }
    if (!tmp.contains(Double.parseDouble(index.getText()))) {
      erroradd.setText("Track not exists, please try again!");
      return false;
    }
    erroradd.setText("");
    return true;
  }

  private boolean checkRemove() {
    if (!removename.getText().matches(Regex.REGEX_LABEL)) {
      errorremove.setText("Name should be label type, try again");
      return false;
    }
    if (tmp.getApp(removename.getText()) == null) {
      errorremove.setText("App not exist, try again");
      return false;
    }
    errorremove.setText("");
    return true;
  }

  /**
   * .Refresh Visual pane after change system
   */
  public void refreshVis() {
    vispane.getChildren().clear();
    vispane.getChildren().add(
        BuildVisualPane.drawCircularOrbit(tmp, vispane.getPrefWidth(), vispane.getPrefHeight()));
    entropy.setText(String.valueOf(new CircularOrbitApis().getObjectDistributionEntropy(tmp)));
  }

  public void setPersonalAppEcosystem(PersonalAppEcosystem pae) {
    this.current = pae;
    tmp = current.clone();
  }

  public void setPersonalAppManager(PersonalAppManager pam) {
    this.pam = pam;
  }

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  public void setPaMenuController(PaMenuController controller) {
    this.pac = controller;
  }

}
