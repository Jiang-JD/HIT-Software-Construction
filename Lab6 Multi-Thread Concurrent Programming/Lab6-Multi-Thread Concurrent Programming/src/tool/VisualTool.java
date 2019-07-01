package tool;

import items.Ladders;
import items.Monkey;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

/**
 * .GUI工具线程，用来绘制GUI和提供一些更新GUI的方法
 *
 */
public class VisualTool extends Thread {
  private static AnchorPane visualpane;
  private MonkeyGenerator mg;
  private Ladders ladders;
  private Label fairness;
  private Label throughput;
  private boolean stopMe = true;
  private static TextArea console;
  
  /**
   * .根据输入梯子，返回梯子GUIGroup容器
   * @param ladders 梯子
   * @return Group容器
   */
  public static Group update(Ladders ladders) {
    LaddersVis lvis = new LaddersVis(ladders.size(), ladders.length());
    
    lvis.getLaddersVis().setLayoutX(visualpane.getWidth() / 2 - lvis.getWidth() / 2);
    lvis.getLaddersVis().setLayoutY(visualpane.getHeight() / 2 - lvis.getHeight() / 2);
    
    synchronized (ladders) {
      for (int i = 0; i < ladders.size(); i++) {
        synchronized (ladders.get(i)) {
          for (int j = 0; j < ladders.length(); j++) {
            if (ladders.get(i).contain(j)) {
              // laddersvis.addMonkey(monkey, i, j);
              lvis.addMonkey(new MonkeyVis(ladders.get(i).get(j)), i, j);
            }
          }
        }
      }
    }
    return lvis.getLaddersVis();
    //return laddersvis.getLaddersVis();
  }
  
  /**
   * .添加菜单中的控制台输出，输出语句会换行
   * @param text 待输出语句
   */
  public static void updateConsole(String text) {
    if (console != null) {
      Platform.runLater(new Runnable() {
        @Override
        public void run() {
          console.appendText(text + "\n");
        }
      });
    }
  }
  
  public void setMG(MonkeyGenerator mg) {
    this.mg = mg;
  }
  
  public void setConsole(TextArea console) {
    VisualTool.console = console;
  }
  
  public void setLadders(Ladders ladders) {
    this.ladders = ladders;
  }
  
  public void setVisualpane(AnchorPane visualpane) {
    VisualTool.visualpane = visualpane;
  }
  
  public void setFairness(Label fairness) {
    this.fairness = fairness;
  }
  
  public void setThroughput(Label throughput) {
    this.throughput = throughput;
  }
  
  public static LaddersVis buildLaddersVis(Ladders ladders) {
    return new LaddersVis(ladders.size(), ladders.length());
  }
  
  /**
   * .终止此线程方法
   */
  public void stopMe() {
    stopMe = false;
  }

  @Override
  public void run() {
    while (stopMe) {
      try {
        Thread.sleep(400);
        while (!mg.isOver()) {
          updateLadder(update(ladders));
          Thread.sleep(1000);
        }
      } catch (InterruptedException e) {
        return;
      }
      updateInfo();
    }
  }
  
  /**
   * .将Ladders图像添加到GUI
   * @param lvis ladders图像
   */
  private void updateLadder(Group lvis) {
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        visualpane.getChildren().clear();
        visualpane.getChildren().add(lvis);
      }
    });
  }
  
  /**
   * .更新参数信息
   */
  private void updateInfo() {
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        fairness.setText(String.valueOf(mg.getFairness()));
        throughput.setText(String.valueOf(mg.getThroughput()));
      }
    });
  }
}

/**.
 * 梯子图像包装对象，将图像容器包装为可操作对象
 *
 */
class LadderVis {
  List<Line> rungs = new ArrayList<Line>();
  private Group ladder = new Group();
  private Group monkeys = new Group();
  private double width = 740;
  private double height = 50;
  private double padding = 30;
  
  public LadderVis(int num) {
    Line l1 = new Line(0,0,width,0);
    Line l2 = new Line(0,height,width,height);
    l1.setStrokeWidth(3);
    l2.setStrokeWidth(3);
    ladder.getChildren().addAll(l1, l2);
    double interval = (double)(width - 2 * padding) / (num - 1);
    for (int i = 0; i < num; i++) {
      Line l = new Line(padding + i * interval, 0, padding + i * interval, height);
      l.setStrokeWidth(3);
      rungs.add(l);
      ladder.getChildren().add(l);
    }
    ladder.getChildren().add(monkeys);
  }
  
  public double getRungLayoutX(int i) {
    return ladder.getLayoutX() + rungs.get(i).getStartX();
  }
  
  public double getWidth() {
    return width;
  }
  
  public double getHeight() {
    return height;
  }
  
  public double getRungX(int i) {
    return rungs.get(i).getStartX();
  }
  
  public double getRungY(int i) {
    return height / 2;
  }
  
  public Group getLadderVis() {
    return ladder;
  }
  
  public Line get(int i) {
    return rungs.get(i);
  }
}

/**
 * .一组梯子的GUI包装对象
 *
 */
class LaddersVis {
  private List<LadderVis> ladders = new ArrayList<LadderVis>();
  private Group laddersvis = new Group();
  private double padding = 20;
  private Group monkeys = new Group();
  
  public LaddersVis(int num, int length) {
    for (int i = 0; i < num; i++) {
      ladders.add(new LadderVis(length));
      laddersvis.getChildren().add(ladders.get(i).getLadderVis());
      ladders.get(i).getLadderVis().setLayoutY((ladders.get(i).getHeight() + padding) * i);
    }
    laddersvis.getChildren().add(monkeys);
  }
  
  public void addMonkey(MonkeyVis monkey, int i, int j) {
    monkey.getMonkeyVis().setLayoutY(
        ladders.get(i).getLadderVis().getLayoutY() + monkey.getHeight() / 2);
    monkey.getMonkeyVis().setLayoutX(ladders.get(i).getRungLayoutX(j) - monkey.getWidth() / 2);
    if (!monkeys.getChildren().contains(monkey.getMonkeyVis())) {
      Platform.runLater(new Runnable() {
        @Override
        public void run() {
          monkeys.getChildren().add(monkey.getMonkeyVis());
        }
      });
    }
    
  }
  
  public double getWidth() {
    return ladders.get(0).getWidth();
  }
  
  public double getHeight() {
    return ((ladders.get(0).getHeight() + padding) * ladders.size() - 1) 
          + ladders.get(0).getHeight();
  }
  
  public Group getLaddersVis() {
    return laddersvis;
  }
  
}

/**
 * .猴子GUI包装对象
 *
 */
class MonkeyVis {
  private Polygon monkeyvis;
  
  public MonkeyVis(Monkey monkey) {
    monkeyvis = new Polygon();
    if (monkey.getDirection().equals("L->R")) {
      monkeyvis.getPoints().addAll(new Double[]{
          0.0, 0.0,
          0.0, 30.0,
          30.0, 15.0 });
    } else {
      monkeyvis.getPoints().addAll(new Double[]{
          0.0, 15.0,
          30.0, 0.0,
          30.0, 30.0 });
    }
    if (monkey.getSpeed() <= 1) {
      monkeyvis.setFill(Color.RED);
    } else if (monkey.getSpeed() <= 3) {
      monkeyvis.setFill(Color.DARKORANGE);
    } else if (monkey.getSpeed() <= 5) {
      monkeyvis.setFill(Color.YELLOW);
    } else if (monkey.getSpeed() <= 8) {
      monkeyvis.setFill(Color.DEEPSKYBLUE);
    } else {
      monkeyvis.setFill(Color.LAWNGREEN);
    }
    monkeyvis.setStrokeWidth(3);
    monkeyvis.setStroke(Color.BLACK);
  }
  
  public Polygon getMonkeyVis() {
    return monkeyvis;
  }
  
  public double getWidth() {
    return 20;
  }
  
  public double getHeight() {
    return 20;
  }
}
