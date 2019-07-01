package applications.view;

import applications.tools.LogFilter;
import applications.tools.Timespan;
import constant.Regex;
import constant.ReusablePool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.apache.log4j.Logger;


public class LogController {

  @FXML
  private DatePicker ed;

  @FXML
  private DatePicker sd;

  @FXML
  private ChoiceBox<String> classtype;

  @FXML
  private ChoiceBox<String> method;

  @FXML
  private ChoiceBox<String> operationtype;

  @FXML
  private ChoiceBox<String> type;

  @FXML
  private ChoiceBox<String> logtype;

  @FXML
  private CheckBox cbxBytime;

  @FXML
  private StackPane stackpane;

  @FXML
  private TableView<String> operation;

  @FXML
  private TableView<String> exception;

  @FXML
  private TableColumn<String, String> exceptionDate;

  @FXML
  private TableColumn<String, String> exceptionTime;

  @FXML
  private TableColumn<String, String> exceptionType;

  @FXML
  private TableColumn<String, String> exceptionLocation;

  @FXML
  private TableColumn<String, String> exceptionDetails;

  @FXML
  private TableColumn<String, String> exceptionResult;

  @FXML
  private TableColumn<String, String> operateDate;

  @FXML
  private TableColumn<String, String> operateTime;

  @FXML
  private TableColumn<String, String> operateType;

  @FXML
  private TableColumn<String, String> operateDetails;

  private Stage stage;
  private List<String> olog = new ArrayList<>();
  private List<String> elog = new ArrayList<>();
  private ObservableList<String> eobservallist = FXCollections.observableArrayList();
  private ObservableList<String> oobservallist = FXCollections.observableArrayList();
  private String currentclass = "None";
  private String currenttype = "None";
  private String currentoperation = "None";
  private String currentmethod = "None";
  private Timespan currenttimespan = null;
  private List<String> typelist = Arrays.asList("None", "NullPointerException", 
      "ElementLabelDuplicationException",
      "IllegalElementFormatException", "IncorrectElementDependencyException", 
      "IncorrectElementLabelOrderException",
      "LackOfComponentException", "NoSuchElementException", "TrackNumberOutOfRangeException",
      "UndefinedElementException");

  private List<String> operationlist = Arrays.asList("None", "ParserFile", "Transit", 
      "Add Track", "Remove Track",
      "Add Object", "Remove Object", "Add Central Point", "Add Relation", "Remove Relation");
  private List<String> classlist = Arrays.asList("None", "TrackGame", "AtomStructure", 
      "PersonalAppEcosystem",
      "TrackGameParser", "AtomStructureParser", "PersonalAppEcosystemParser");
  private List<String> logtypelist = Arrays.asList("OperationLog", "ExceptionLog");
  private List<String> methodlist = Arrays.asList("None", "<init>", "getObjectDistributionEntropy",
      "getLogicalDistance", "getPhysicalDistance", "getDifference", "add", "remove", 
      "clear", "handleAddEle",
      "handleAddTrack", "handleRemoveEle", "handleRemoveTrack", 
      "handleSave", "handleOpenFile_TG", "handleOpenFile_AS",
      "handleOpenFile_AE", "handleAddTrack", "handleRemoveTrack", 
      "handleSave", "handleRestore", "backup", "restore",
      "build", "addCentralPoint", "addObject", "addCentralRelation", 
      "removeCentralRelation", "addOrbitRelation",
      "removeOrbitRelation", "addApp", "generateEcosystems", "initial", 
      "buildEcosystem", "clear", "getDistance",
      "getDifference", "addAll", "removeAll", "setRaceType", "grouping", 
      "exchange", "parserFile", "getText", "getApps",
      "getRelation", "getPeriod", "getUser", "getAppInstallTime", 
      "getUsageLog", "raceType", "trackNum", "athletes");
  private Logger logger = Logger.getLogger(LogController.class);
  private LogFilter filter = new LogFilter();

  @FXML
  private void initialize() {
    List<String> olist = new ArrayList<>();
    List<String> elist = new ArrayList<>();
    String path = "src/logs/operation";
    String epath = "src/logs/exception";
    try {
      File ofile = new File(path);
      String[] filelist = ofile.list();
      for (int i = 0; i < filelist.length; i++) {
        olist.add(path + "/" + filelist[i]);
        for (String s : olist) {
          FileReader fr = new FileReader(s);
          BufferedReader bf = new BufferedReader(fr);
          String str;
          // 按行读取字符串
          while ((str = bf.readLine()) != null) {
            if (str.matches(Regex.REGEX_OPERATIONLOG)) {
              olog.add(str);
              oobservallist.add(str); // set olog
            }
          }
          bf.close();
          fr.close();
        }
      }
      File exfile = new File(epath);
      String[] exfilelist = exfile.list();
      for (int i = 0; i < exfilelist.length; i++) {
        elist.add(epath + "/" + exfilelist[i]);
        FileReader fr = new FileReader(elist.get(i));
        BufferedReader bf = new BufferedReader(fr);
        String str;
        // 按行读取字符串
        while ((str = bf.readLine()) != null) {
          elog.add(str);
          eobservallist.add(str); // set elog
        }
        bf.close();
        fr.close();
      }
    } catch (Exception e) {
      e.printStackTrace(); // spotbug 标记多余exception，不修改，为了捕获所有异常
    }

    logtype.setItems(FXCollections.observableArrayList("OperationLog", "ExceptionLog"));
    classtype.setItems(FXCollections.observableArrayList("None", "TrackGame", 
        "AtomStructure", "PersonalAppEcosystem",
        "TrackGameParser", "PersonalAppEcosystemParser", "AtomStructureParser", "Parser"));
    type.setItems(FXCollections.observableArrayList("None", "NullPointerException", 
        "ElementLabelDuplicationException",
        "IllegalElementFormatException", "IncorrectElementDependencyException", 
        "IncorrectElementLabelOrderException",
        "LackOfComponentException", "NoSuchElementException", "TrackNumberOutOfRangeException",
        "UndefinedElementException"));
    method.setItems(FXCollections.observableArrayList("None", "<init>", 
        "getObjectDistributionEntropy",
        "getLogicalDistance", "getPhysicalDistance", "getDifference", 
        "add", "remove", "clear", "handleAddEle",
        "handleAddTrack", "handleRemoveEle", "handleRemoveTrack", "handleSave", "handleOpenFile_TG",
        "handleOpenFile_AS", "handleOpenFile_AE", "handleAddTrack", 
        "handleRemoveTrack", "handleSave", "handleRestore",
        "backup", "restore", "build", "addCentralPoint", "addObject", 
        "addCentralRelation", "removeCentralRelation",
        "addOrbitRelation", "removeOrbitRelation", "addApp", "generateEcosystems", 
        "initial", "buildEcosystem", "clear",
        "getDistance", "getDifference", "addAll", "removeAll", "setRaceType", 
        "grouping", "exchange", "parserFile",
        "getText", "getApps", "getRelation", "getPeriod", "getUser", 
        "getAppInstallTime", "getUsageLog", "raceType",
        "trackNum", "athletes"));
    operationtype.setItems(FXCollections.observableArrayList("None", "ParserFile", 
        "Transit", "Add Track",
        "Remove Track", "Add Object", "Remove Object", "Add Central Point", 
        "Add Relation", "Remove Relation"));

    /*
     * Set action
     */
    logtype.setValue("OperationLog");
    logtype.getSelectionModel().selectedIndexProperty()
        .addListener((ObservableValue<? extends Number> ov, Number oldVal, Number newVal) -> {
          updateLogType(newVal.intValue());
        });
    classtype.getSelectionModel().selectedIndexProperty()
        .addListener((ObservableValue<? extends Number> ov, Number oldVal, Number newVal) -> {
          updateClass(newVal.intValue());
        });
    type.getSelectionModel().selectedIndexProperty()
        .addListener((ObservableValue<? extends Number> ov, Number oldVal, Number newVal) -> {
          updateExceptionType(newVal.intValue());
        });
    method.getSelectionModel().selectedIndexProperty()
        .addListener((ObservableValue<? extends Number> ov, Number oldVal, Number newVal) -> {
          updateMethod(newVal.intValue());
        });
    operationtype.getSelectionModel().selectedIndexProperty()
        .addListener((ObservableValue<? extends Number> ov, Number oldVal, Number newVal) -> {
          updateOperation(newVal.intValue());
        });
    cbxBytime.selectedProperty().addListener(new ChangeListener<Boolean>() {
      public void changed(ObservableValue<? extends Boolean> ov, Boolean oldVal, Boolean newVal) {
        updateTime(newVal);
      }
    });

    /*
     * Set Operate Log
     */
    operateDate.setCellValueFactory(cellData -> {
      Matcher m = ReusablePool.PATTERN_OPERATIONLOG.matcher(cellData.getValue());
      if (m.find()) {
        try {
          return new ReadOnlyStringWrapper(m.group(1));
        } catch (Exception e) {
          System.out.println("o_date " + m.find());
          System.out.println("o_date " + cellData.getValue());
          return new ReadOnlyStringWrapper("");
        }
      }
      return new ReadOnlyStringWrapper("");
    });
    operateTime.setCellValueFactory(cellData -> {
      Matcher m = ReusablePool.PATTERN_OPERATIONLOG.matcher(cellData.getValue());
      if (m.find()) {
        return new ReadOnlyStringWrapper(m.group(4));
      }
      return new ReadOnlyStringWrapper("");

    });
    operateType.setCellValueFactory(cellData -> {
      Matcher m = ReusablePool.PATTERN_OPERATIONLOG.matcher(cellData.getValue());
      if (m.find()) {
        return new ReadOnlyStringWrapper(m.group(9));
      }
      return new ReadOnlyStringWrapper("");
    });
    operateDetails.setCellValueFactory(cellData -> {
      Matcher m = ReusablePool.PATTERN_OPERATIONLOG.matcher(cellData.getValue());
      if (m.find()) {
        return new ReadOnlyStringWrapper(m.group(10));
      }
      return new ReadOnlyStringWrapper("");
    });
    operation.setItems(oobservallist);
    /*
     * Set Exception Log
     */
    exceptionDate.setCellValueFactory(cellData -> {
      Matcher m = ReusablePool.PATTERN_EXCEPTIONLOG.matcher(cellData.getValue());
      if (m.find()) {
        try {
          return new ReadOnlyStringWrapper(m.group(1));
        } catch (Exception e) {
          return new ReadOnlyStringWrapper("");
        }
      }
      return new ReadOnlyStringWrapper("");
    });
    exceptionTime.setCellValueFactory(cellData -> {
      Matcher m = ReusablePool.PATTERN_EXCEPTIONLOG.matcher(cellData.getValue());
      if (m.find()) {
        return new ReadOnlyStringWrapper(m.group(4));
      }
      return new ReadOnlyStringWrapper("");
    });
    exceptionType.setCellValueFactory(cellData -> {
      Matcher m = ReusablePool.PATTERN_EXCEPTIONLOG.matcher(cellData.getValue());
      if (m.find()) {
        return new ReadOnlyStringWrapper(m.group(10));
      }
      return new ReadOnlyStringWrapper("");
    });
    exceptionDetails.setCellValueFactory(cellData -> {
      Matcher m = ReusablePool.PATTERN_EXCEPTIONLOG.matcher(cellData.getValue());
      if (m.find()) {
        try {
          return new ReadOnlyStringWrapper(m.group(12));
        } catch (Exception e) {
          return new ReadOnlyStringWrapper("");
        }
      }
      return new ReadOnlyStringWrapper("");
    });
    exceptionLocation.setCellValueFactory(cellData -> {
      Matcher m = ReusablePool.PATTERN_EXCEPTIONLOG.matcher(cellData.getValue());
      if (m.find()) {
        return new ReadOnlyStringWrapper(m.group(8) + "-" + m.group(9));
      }
      return new ReadOnlyStringWrapper("");
    });
    exceptionResult.setCellValueFactory(cellData -> {
      Matcher m = ReusablePool.PATTERN_EXCEPTIONLOG.matcher(cellData.getValue());
      if (m.find()) {
        return new ReadOnlyStringWrapper(m.group(11));
      }
      return new ReadOnlyStringWrapper("");
    });
    exception.setItems(eobservallist);

  }

  @FXML
  void handleByTime(ActionEvent event) {

  }

  private void updateTime(boolean select) {
    if (!select) {
      currenttimespan = null;
      update();
      return;
    }
    if (sd.getValue() == null || ed.getValue() == null) {
      return;
    }
    String startDate = sd.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString();
    String endDate = ed.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString();
    Instant start = Instant.parse(startDate + "T00:00:00Z");
    Instant end = Instant.parse(endDate + "T00:00:00Z");
    if (start.isAfter(end)) {
      return;
    }
    currenttimespan = new Timespan(start, end);
    update();
  }

  private void updateLogType(int index) {
    if (index == 0) {
      operation.toFront();
    } else {
      exception.toFront();
    }
    update();
  }

  private void updateClass(int index) {
    currentclass = classlist.get(index);
    update();
  }

  private void updateMethod(int index) {
    currentmethod = methodlist.get(index);
    update();
  }

  private void updateExceptionType(int index) {
    if (logtype.getSelectionModel().getSelectedIndex() == 0) {
      return;
    }
    currenttype = typelist.get(index);
    update();
  }

  private void updateOperation(int index) {
    if (logtype.getSelectionModel().getSelectedIndex() == 0) {
      currentoperation = operationlist.get(index);
    } else {
      return;
    }
    update();
  }

  private void update() {
    if (logtype.getSelectionModel().getSelectedIndex() == 0) {
      List<String> byclass = filter.filterByClass(currentclass, olog);
      List<String> byoperation = filter.filterByOperation(currentoperation, byclass);
      List<String> bytype = filter.filterByType(currenttype, byoperation);
      List<String> bytime = filter.filterByTime(currenttimespan, bytype);
      List<String> bymethod = filter.filterByMethod(currentmethod, bytime);
      oobservallist.clear();
      for (String s : bymethod) {
        oobservallist.add(s);
      }
      operation.setItems(oobservallist);
    } else {
      List<String> byclass = filter.filterByClass(currentclass, elog);
      List<String> byoperation = filter.filterByOperation(currentoperation, byclass);
      List<String> bytype = filter.filterByType(currenttype, byoperation);
      List<String> bytime = filter.filterByTime(currenttimespan, bytype);
      List<String> bymethod = filter.filterByMethod(currentmethod, bytime);
      eobservallist.clear();
      eobservallist.clear();
      for (String s : bymethod) {
        eobservallist.add(s);
      }
      exception.setItems(eobservallist);
    }
  }

  public void setStage(Stage stage) {
    this.stage = stage;
  }

}
