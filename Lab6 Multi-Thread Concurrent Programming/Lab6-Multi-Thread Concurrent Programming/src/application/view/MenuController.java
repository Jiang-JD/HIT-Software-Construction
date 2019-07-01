package application.view;

import items.Ladders;
import items.Monkey;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import tool.MonkeyGenerator;
import tool.Parser;
import tool.Select;
import tool.Selector1;
import tool.Selector2;
import tool.Selector3;
import tool.Selector4;
import tool.Selector5;
import tool.Selector6;
import tool.Selector7;
import tool.VisualTool;

public class MenuController {

  @FXML
  private TextField timespan;

  @FXML
  private TextField pernum;

  @FXML
  private TextField totalnum;

  @FXML
  private TextField mv;

  @FXML
  private TextField totalladders;

  @FXML
  private TextField rungnum;
  
  @FXML
  private ChoiceBox<String> selectors;

  @FXML
  private Label throughput;

  @FXML
  private Label fairness;

  @FXML
  private TextArea console;

  @FXML
  private AnchorPane visualpane;
  
  private MonkeyGenerator mg = new MonkeyGenerator();
  private Stage stage;
  private Ladders ladders;
  private int t;
  private int N;
  private int k;
  private int MV;
  private boolean isFile = false;
  private List<Select> selectorlist = Arrays.asList(
      null, //无意义，填充位，此处是Random选项
      new Selector1(),
      new Selector2(),
      new Selector3(),
      new Selector4(),
      new Selector5(),
      new Selector6(),
      new Selector7()
      );
  private int selectori = 0;
  private Map<Integer, List<Monkey>> map;
  private String filePath;
  private VisualTool vtthread;
  private Thread mgthread;

  @FXML
  void handleOpenFile(ActionEvent event) {
    FileChooser fc = new FileChooser();
    fc.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"));
    File file = fc.showOpenDialog(stage);
    String filepath = "";
    if (file != null) {
      filepath = file.getAbsolutePath();
      this.filePath = filepath;
      Parser parser = new Parser();
      try {
        this.ladders = parser.parseLadders(filepath);
        this.map = parser.parseMonkey(filepath, ladders);
        //update Gui
        totalladders.setText(String.valueOf(ladders.size()));
        rungnum.setText(String.valueOf(ladders.length()));
        int N = 0;
        for (List<Monkey> l : map.values()) {
          N += l.size();
        }
        totalnum.setText(String.valueOf(N));
        pernum.setText("Unknown");
        timespan.setText("Unknown");
        mv.setText("Unknown");
        
        isFile = true;
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      return;
    }
  }

  @FXML
  void handleRun(ActionEvent event) {
    if (!isFile && (timespan.getText().isEmpty() 
        || !timespan.getText().matches("\\d+")
        || totalnum.getText().isEmpty()
        || !totalnum.getText().matches("\\d+")
        || pernum.getText().isEmpty()
        || !pernum.getText().matches("\\d+")
        || mv.getText().isEmpty()
        || !mv.getText().matches("\\d+")
        || totalladders.getText().isEmpty()
        || !totalladders.getText().matches("\\d+")
        || rungnum.getText().isEmpty())
        || !rungnum.getText().matches("\\d+")) {
      return;
    }
    if (!isFile) {
      t = Integer.parseInt(timespan.getText());
      N = Integer.parseInt(totalnum.getText());
      k = Integer.parseInt(pernum.getText());
      MV = Integer.parseInt(mv.getText());
      ladders = new Ladders(Integer.parseInt(totalladders.getText()), 
                    Integer.parseInt(rungnum.getText()));
    } else {
      try {
        this.ladders = new Parser().parseLadders(filePath);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    
    //clear
    fairness.setText("");
    throughput.setText("");
    console.setText("");
    mg.setLadders(ladders);
    if (vtthread != null && vtthread.isAlive()) {
      vtthread.interrupt();
    }
    if (mgthread != null && mgthread.isAlive()) {
      mgthread.interrupt();
    }
    visualpane.getChildren().clear();
    Thread mgthread;
    //start
    if (selectori == 0) {
      if (!isFile) {
        mgthread = new Thread() {
          @Override
          public void run() {
            mg.start(t, k, N, MV);
          }
        };
      } else {
        try {
          this.map = new Parser().parseMonkey(filePath, ladders);
        } catch (IOException e) {
          return;
        }
        mgthread = new Thread() {
          @Override
          public void run() {
            mg.start(map);
          }
        };
      }
    } else {
      if (!isFile) {
        mgthread = new Thread() {
          @Override
          public void run() {
            mg.start(t, k, N, MV, selectorlist.get(selectori));
          }
        };
      } else {
        try {
          this.map = new Parser().parseMonkey(filePath, ladders);
        } catch (IOException e) {
          return;
        }
        mgthread = new Thread() {
          @Override
          public void run() {
            mg.start(map, selectorlist.get(selectori));
          }
        };
      }
    }
    this.mgthread = mgthread;
    mgthread.setName("mgthread");
    mgthread.start();
    drawLadders();
  }
  
  @FXML
  private void initialize() {
    timespan.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(
          ObservableValue<? extends String> observable, String oldValue, String newValue) {
          isFile = false;
      }
    });
    pernum.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(
          ObservableValue<? extends String> observable, String oldValue, String newValue) {
          isFile = false;
      }
    });
    totalnum.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(
          ObservableValue<? extends String> observable, String oldValue, String newValue) {
          isFile = false;
      }
    });
    mv.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(
          ObservableValue<? extends String> observable, String oldValue, String newValue) {
          isFile = false;
      }
    });
    totalladders.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(
          ObservableValue<? extends String> observable, String oldValue, String newValue) {
          isFile = false;
      }
    });
    rungnum.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(
          ObservableValue<? extends String> observable, String oldValue, String newValue) {
          isFile = false;
      }
    });
    selectors.setValue("Random");
    selectors.setItems(FXCollections.observableArrayList("Random",
        "Selector1",
        "Selector2",
        "Selector3",
        "Selector4",
        "Selector5",
        "Selector6",
        "Selector7"));
    selectors.getSelectionModel().selectedIndexProperty()
        .addListener((ObservableValue<? extends Number> ov, Number oldVal, Number newVal) -> {
          this.selectori = newVal.intValue();
        });
  }
  
  public void setStage(Stage stage) {
    this.stage = stage;
  }
  
  private void drawLadders() {
    VisualTool vt = new VisualTool();
    vt.setLadders(ladders);
    vt.setMG(mg);
    vt.setVisualpane(visualpane);
    vt.setThroughput(throughput);
    vt.setFairness(fairness);
    vt.setConsole(console);
    if (vtthread != null && vtthread.isAlive()) {
      vtthread.stopMe();
    }
    vtthread = vt;
    vtthread.setName("vtthread");
    vt.start();
  }
  

}
