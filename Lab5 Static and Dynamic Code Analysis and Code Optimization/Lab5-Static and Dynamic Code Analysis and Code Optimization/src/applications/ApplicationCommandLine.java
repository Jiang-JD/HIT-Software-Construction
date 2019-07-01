package applications;

import apis.CircularOrbitApis;
import apis.CircularOrbitHelper;
import applications.tools.DividerAscendingOrder;
import applications.tools.DividerRandom;
import applications.tools.MementoCareTaker;
import applications.tools.Timespan;
import circularorbit.AtomStructure;
import circularorbit.PersonalAppEcosystem;
import circularorbit.TrackGame;
import constant.Menu;
import constant.Regex;
import constant.SystemType;

import exception.ElementLabelDuplicationException;
import exception.IllegalElementFormatException;
import exception.IncorrectElementDependencyException;
import exception.IncorrectElementLabelOrderException;
import exception.LackOfComponentException;
import exception.NoSuchElementException;
import exception.TrackNumberOutOfRangeException;
import exception.UndefinedElementException;

import java.awt.FileDialog;
import java.awt.Frame;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import manager.PersonalAppManager;
import manager.TrackGameManager;
import parser.AtomStructureParser;
import physicalobject.App;
import physicalobject.AppFactory;
import physicalobject.Athlete;
import physicalobject.AthleteFactory;
import physicalobject.Electron;
import physicalobject.ElectronFactory;

/**.
 * 一个多轨道系统的应用类
 *
 */
public class ApplicationCommandLine {

  CircularOrbitApis api = new CircularOrbitApis();

  /**.
   * show menu
   */
  private void show(String s) {
    System.out.print(s);
  }

  private void showl(String s) {
    System.out.println(s);
  }

  public void start() {
    SystemType st = choseSystem();
    execute(st);
  }

  private SystemType choseSystem() {
    Scanner in = new Scanner(System.in);
    SystemType st = null;
    show(Menu.MENU_SYSTEMS);
    boolean f = true;
    while (f) {
      String op = in.next();
      switch (op) {
        case "1": {
          st = SystemType.TrackGame;
          f = false;
          break;
        }
        case "2": {
          st = SystemType.AtomStructure;
          f = false;
          break;
        }
        case "3": {
          st = SystemType.PersonalAppEcosystem;
          f = false;
          break;
        }
        default: {
          show(Menu.ERRORINPUT);
        }
      }
    }
    return st;
  }

  /**.
   * chose a application to run
   * 
   * @param st systemtype
   */
  private void execute(SystemType st) {
    switch (st) {
      case TrackGame: {
        executeTrackGame();
        break;
      }
      case AtomStructure: {
        executeAtomStructure();
        break;
      }
      case PersonalAppEcosystem: {
        executeAppEco();
        break;
      }
      default: {
        throw new IllegalArgumentException("Wrong system tyep");
      }
    }
    return;
  }

  /**.
   * run track game application
   */
  private void executeTrackGame() {
    Scanner in = new Scanner(System.in);
    boolean f = true;
    TrackGameManager tgm = new TrackGameManager();
    List<TrackGame> tl = new ArrayList<TrackGame>();

    while (f) {
      show(Menu.MENU_TRACKGAME);
      String op = in.next();
      switch (op) {
        case "1": {
          show(Menu.INPUTCOMFIRM);
          if (in.hasNext()) {
            op = in.next();
          }
          while (!op.equals("y") && !op.equals("n")) {
            show(Menu.ERRORINPUT);
            op = in.next();
          }
          if (op.equals("y")) {
            FileDialog dialog = new FileDialog((Frame) null, "Select File to Open");
            dialog.setMode(FileDialog.LOAD);
            dialog.setVisible(true);
            String s = dialog.getDirectory() + dialog.getFile();
            try {
              if (!tgm.initial(s)) {
                show(Menu.ERRORFILE);
                break;
              }
            } catch (IllegalElementFormatException 
                | NoSuchElementException 
                | IncorrectElementLabelOrderException
                | LackOfComponentException 
                | ElementLabelDuplicationException 
                | TrackNumberOutOfRangeException e) {
              e.printStackTrace();
              show(Menu.ERRORFILE);
              break;
            }
            show(Menu.SUCCESS);
          }
          break;
        }
        case "2": {
          show(Menu.TRACKGAME_DIVIDER);
          if (in.hasNext()) {
            op = in.next();
          }
          if (op.equals("1")) {
            tl = tgm.grouping(new DividerRandom());
            show(Menu.SUCCESS);
          } else if (op.equals("2")) {
            tl = tgm.grouping(new DividerAscendingOrder());
            show(Menu.SUCCESS);
          } else {
            show(Menu.ERRORINPUT);
          }
          break;
        }
        case "3": {
          StringBuilder sb = new StringBuilder();
          sb.append("===========================\n" 
              + "总共分组数：" + tl.size() + "\n" 
              + "===========================\n");
          for (int i = 0; i < tl.size(); i++) {
            sb.append(i + 1 + ": ");
            TrackGame tg = tl.get(i);
            Iterator<Athlete> it = tg.iterator();
            while (it.hasNext()) {
              Athlete a = it.next();
              sb.append(a.getName() + ":" + a.getNumber() + ", ");
            }
            sb.append("\n-----------------------\n");
          }
          show(sb.toString());
          break;
        }
        case "4": {
          show(Menu.TRACKGAME_EXCHANGE);
          String n1 = in.next();
          String n2 = in.next();
          if (!n1.matches("\\d+") || !n2.matches("\\d+")) {
            show(Menu.ERRORINPUT);
            break;
          }
          int num1 = Integer.parseInt(n1);
          int num2 = Integer.parseInt(n2);
          Athlete a1 = tgm.getAthlete(num1);
          Athlete a2 = tgm.getAthlete(num2);
          if (a1 == null || a2 == null) {
            showl("运动员不存在");
          }
          tl = tgm.exchange(a1, a2);
          if (a1 != null && a2 != null) {
            showl("交换了[" + a1.getName() + "] 和 [" + a2.getName() + "]");
          }
          break;
        }
        case "5": {
          showl("请输入分组编号，从1开始");
          String s = in.next();
          if (s.matches("\\d+")) {
            int index = Integer.parseInt(s);
            if (index <= tl.size() && index > 0) {
              executeRace(tl.get(index - 1));
              show(Menu.SUCCESS);
              break;
            }
          }
          show(Menu.ERRORINPUT);
          break;
        }
        case "0": {
          f = false;
          break;
        }
        default: {
          showl(Menu.ERRORINPUT);
        }
      }
    }
    return;
  }

  /**.
   * 对某一个具体的径赛轨道系统进行处理
   * 
   * @param tg 径赛轨道系统
   */
  private void executeRace(TrackGame tg) {
    AthleteFactory af = new AthleteFactory();
    Scanner in = new Scanner(System.in);
    boolean f = true;

    while (f) {
      show(Menu.MENU_TRACKGAME_RACE);
      String op = in.next();
      switch (op) {
        case "1": {
          show(Menu.ADDTRACK);
          String index = in.next();
          if (index.matches("\\d+")) {
            int number = Integer.parseInt(index);
            if ((number = tg.addTrack(number)) != -1) {
              show(Menu.SUCCESS);
            } else {
              showl("轨道已存在");
            }
          } else {
            show(Menu.ERRORINPUT);
          }
          break;
        }
        case "2": {
          show(Menu.TRACKGAME_ADDATHLETE);
          String s = in.next();
          Matcher m = Pattern.compile("(<\\S+>)|(\\d+)").matcher(s);
          if (m.find()) {
            String des = "Athlete ::= " + m.group(1);
            if (!des.matches(Regex.TRACKGAME_ATHLETE)) {
              showl("运动员格式错误");
              break;
            }
            Athlete a = (Athlete) af.create(des);
            tg.addObject(a, Integer.parseInt(m.group(2)));
            show(Menu.SUCCESS);
          } else {
            show(Menu.ERRORINPUT);
          }
          break;
        }
        case "3": {
          show(Menu.REMOVETRACK);
          String index = in.next();
          if (index.matches("\\d+")) {
            int number = Integer.parseInt(index);
            if (tg.remove(number)) {
              show(Menu.SUCCESS);
            } else {
              showl("轨道不存在");
            }
          } else {
            show(Menu.ERRORINPUT);
          }
          break;
        }
        case "4": {
          show(Menu.TRACKGAME_REMOVEATHLETE);
          String s = in.nextLine();
          Matcher m = Pattern.compile("(\\d+)|(\\d+)").matcher(s);
          if (m.find()) {
            Athlete a = tg.getAthlete(Integer.parseInt(m.group(1)));
            if (a != null) {
              if (tg.remove(a, Integer.parseInt(m.group(2)))) {
                show(Menu.SUCCESS);
                break;
              }
            }
          }
          show(Menu.ERRORINPUT);
          break;
        }
        case "5": {
          showl(Menu.ENTROPHY + api.getObjectDistributionEntropy(tg));
          break;
        }
        case "6": {
          CircularOrbitHelper.visualize(tg);
          break;
        }
        case "0": {
          f = false;
          break;
        }
        default:
          break;
      }
    }
  }

  /**.
   * run atom application
   */
  private void executeAtomStructure() {
    Scanner in = new Scanner(System.in);
    boolean f = true;
    AtomStructureParser asp = new AtomStructureParser();
    AtomStructure as = null;
    MementoCareTaker mct = new MementoCareTaker();
    while (f) {
      show(Menu.MENU_ATOMSTRUCTURE);
      String op = in.next();
      switch (op) {
        case "1": {
          show(Menu.INPUTCOMFIRM);
          if (in.hasNext()) {
            op = in.next();
          }
          while (!op.equals("y") && !op.equals("n")) {
            show(Menu.ERRORINPUT);
            op = in.next();
          }
          if (op.equals("y")) {
            FileDialog dialog = new FileDialog((Frame) null, "Select File to Open");
            dialog.setMode(FileDialog.LOAD);
            dialog.setVisible(true);

            String s = dialog.getDirectory() + dialog.getFile();
            try {
              if ((as = asp.initial(s)) != null) { // spotbug 标记 不修
                show(Menu.SUCCESS);
                break;
              }
            } catch (NoSuchElementException | IllegalElementFormatException 
                | TrackNumberOutOfRangeException
                | IncorrectElementLabelOrderException 
                | IncorrectElementDependencyException 
                | LackOfComponentException
                | ElementLabelDuplicationException e) {
              e.printStackTrace();
              show(Menu.ERRORFILE);
              break;
            }
            show(Menu.SUCCESS);
          }
          show(Menu.ERRORFILE);
          break;
        }
        case "2": {
          show(Menu.ADDTRACK);
          String index = in.next();
          if (index.matches("\\d+")) {
            int number = Integer.parseInt(index);
            if ((number = as.addTrack(number)) != -1) {
              show(Menu.SUCCESS);
            } else {
              showl("轨道已存在");
            }
          } else {
            show(Menu.ERRORINPUT);
          }
          break;
        }
        case "3": {
          show(Menu.ATOM_ADDELECTRON);
          int num1 = 0;
          int num2 = 0;
          String s1 = in.next();
          String s2 = in.next();
          if (!s1.matches("\\d+") || !s2.matches("\\d+")) {
            show(Menu.ERRORINPUT);
            break;
          }
          num1 = Integer.parseInt(s1);
          num2 = Integer.parseInt(s2);
          if (as.getObjects(num1) == null) {
            showl("超过轨道数量范围");
            break;
          }
          ElectronFactory ef = new ElectronFactory();
          for (int i = 0; i < num2; i++) {
            as.addObject((Electron) ef.create("electron"), num1);
          }
          show(Menu.SUCCESS);
          break;
        }
        case "4": {
          show(Menu.REMOVETRACK);
          String index = in.next();
          if (index.matches("\\d+")) {
            int number = Integer.parseInt(index);
            if (as.remove(number)) {
              show(Menu.SUCCESS);
              break;
            } else {
              showl("轨道不存在");
            }
          }
          show(Menu.ERRORINPUT);
          break;
        }
        case "5": {
          show(Menu.ATOM_REMOVEELECTRON);
          int num1 = 0;
          int num2 = 0;
          String s1 = in.next();
          String s2 = in.next();
          if (!s1.matches("\\d+") || !s2.matches("\\d+")) {
            show(Menu.ERRORINPUT);
            break;
          }
          num1 = Integer.parseInt(s1);
          num2 = Integer.parseInt(s2);
          if (as.getObjects(num1) == null) {
            showl("超过轨道数量范围");
            break;
          }
          if (as.getObjects(num1).size() < num2) {
            showl("轨道电子数量少于指定数量");
            break;
          }
          ElectronFactory ef = new ElectronFactory();
          for (int i = 0; i < num2; i++) {
            as.remove((Electron) ef.create("electron"), num1);
          }
          show(Menu.SUCCESS);
          break;
        }
        case "6": {
          show(Menu.ATOM_TRANSIT);
          int num1 = 0;
          int num2 = 0;
          String s1 = in.next();
          String s2 = in.next();
          if (!s1.matches("\\d+") || !s2.matches("\\d+")) {
            show(Menu.ERRORINPUT);
            break;
          }
          num1 = Integer.parseInt(s1);
          num2 = Integer.parseInt(s2);
          if (num1 >= as.getTrackNum() || num1 < 0) {
            showl("超过轨道数量范围");
            break;
          }
          if (num2 >= as.getTrackNum() || num2 < 0) {
            showl("超过轨道数量范围");
            break;
          }
          mct.add(as.backup());
          if (as.transit(num1, num2)) {
            show(Menu.SUCCESS);
            break;
          } else {
            mct.remove(mct.get(mct.size() - 1));
          }
          showl("原轨道不存在电子");
          break;
        }
        case "7": {
          showl(mct.toTable());
          show(Menu.ATOM_BACKUP);
          int num1 = 0;
          String s1 = in.next();
          if (!s1.matches("\\d+") && !s1.equals("n")) {
            show(Menu.ERRORINPUT);
            break;
          }
          if (s1.equals("n")) {
            break;
          }
          num1 = Integer.parseInt(s1);
          if (num1 >= mct.size()) {
            showl("超过备忘录数量范围");
            break;
          }
          mct.add(as.backup());
          as.restore(mct.get(num1 - 1));
          show(Menu.SUCCESS);
          break;
        }
        case "8": {
          showl(Menu.ENTROPHY + api.getObjectDistributionEntropy(as));
          break;
        }
        case "9": {
          CircularOrbitHelper.visualize(as);
          break;
        }
        case "0": {
          f = false;
          break;
        }
        default:
          break;
      }
    }
    return;
  }

  /**.
   * run appeco application
   */
  private void executeAppEco() {
    Scanner in = new Scanner(System.in);
    boolean f = true;
    PersonalAppManager pam = new PersonalAppManager();
    List<PersonalAppEcosystem> pl = new ArrayList<PersonalAppEcosystem>();
    String path = "";
    while (f) {
      show(Menu.MENU_APPECO);
      String op = in.next();
      switch (op) {
        case "1": {
          show(Menu.INPUTCOMFIRM);
          if (in.hasNext()) {
            op = in.next();
          }
          while (!op.equals("y") && !op.equals("n")) {
            show(Menu.ERRORINPUT);
            op = in.next();
          }
          if (op.equals("y")) {
            FileDialog dialog = new FileDialog((Frame) null, "Select File to Open");
            dialog.setMode(FileDialog.LOAD);
            dialog.setVisible(true);

            String s = dialog.getDirectory() + dialog.getFile();
            path = s;
            try {
              if (!pam.initial(s)) {
                show(Menu.ERRORFILE);
                break;
              }
            } catch (UndefinedElementException | IllegalElementFormatException 
                | NoSuchElementException
                | LackOfComponentException 
                | ElementLabelDuplicationException 
                | IncorrectElementDependencyException e) {
              e.printStackTrace();
              show(Menu.ERRORFILE);
              break;
            }
            pl = pam.generateEcosystems();
            show(Menu.SUCCESS);
          }
          break;
        }
        case "2": {
          try {
            pam.initial(path);
          } catch (UndefinedElementException | IllegalElementFormatException 
              | NoSuchElementException
              | LackOfComponentException 
              | ElementLabelDuplicationException 
              | IncorrectElementDependencyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          pl = pam.generateEcosystems();
          show(Menu.SUCCESS);
          break;
        }
        case "3": {
          StringBuilder sb = new StringBuilder();
          sb.append("===========================\n" 
              + "总共分组数：" + pl.size() 
              + "\n" + "===========================\n");
          for (int i = 0; i < pl.size(); i++) {
            sb.append(i + 1 + ": ");
            PersonalAppEcosystem ae = pl.get(i);
            sb.append(ae.toString());
            sb.append("\n-----------------------\n");
          }
          show(sb.toString());
          break;
        }
        case "4": {
          showl("请输入分组编号，从1开始");
          String s = in.next();
          if (s.matches("\\d+")) {
            int index = Integer.parseInt(s);
            if (index <= pl.size() && index > 0) {
              executeEco(pl.get(index - 1));
              show(Menu.SUCCESS);
              break;
            }
          }
          show(Menu.ERRORINPUT);
          break;
        }
        case "5": {
          show(Menu.APPEOC_TIMESPAN);
          String s1 = in.next();
          String s2 = in.next();
          String regex = "((\\d{4}-(0[1-9]|1[0-2])-(0\\d|1\\d|2\\d|3[0-1]))"
              + ",((0\\d|1\\d|2[0-3]):([0-5]\\d):([0-5]\\d)))="
              + "((\\d{4}-(0[1-9]|1[0-2])-(0\\d|1\\d|2\\d|3[0-1]))"
              + ",((0\\d|1\\d|2[0-3]):([0-5]\\d):([0-5]\\d)))";
          if (!s1.matches(regex) || !s2.matches(regex)) {
            show(Menu.ERRORINPUT);
            break;
          }
          Matcher m1 = Pattern.compile(regex).matcher(s1);
          Matcher m2 = Pattern.compile(regex).matcher(s2);
          m1.find();
          m2.find();
          Instant i1 = Instant.parse(m1.group(2) + "T" + m1.group(5) + "Z");
          Instant i2 = Instant.parse(m1.group(10) + "T" + m1.group(13) + "Z");
          Instant i3 = Instant.parse(m2.group(2) + "T" + m2.group(5) + "Z");
          Instant i4 = Instant.parse(m2.group(10) + "T" + m2.group(13) + "Z");
          if (i1.isAfter(i2) || i3.isAfter(i4)) {
            showl("起始时间点早于终止时间点");
            break;
          }
          showl(pam.getDifference(new Timespan(i1, i2), new Timespan(i3, i4)).toString());
          break;
        }
        case "6": {
          show(Menu.APPECO_DISTANCE);
          String s1 = in.next();
          String s2 = in.next();
          if (!s1.matches(Regex.REGEX_LABEL) || !s2.matches(Regex.REGEX_LABEL)) {
            show(Menu.ERRORINPUT);
            break;
          }
          App a = null;
          App b = null;
          if ((a = pam.getApp(s1)) != null && (b = pam.getApp(s2)) != null) {
            showl("App之间的逻辑距离为：");
            int dis = pam.getDistance(a, b);
            if (dis == Integer.MAX_VALUE) {
              showl("无穷大，之间没有联系");
            } else {
              showl(Integer.toString(dis));
            }
            break;
          } else {
            showl("查找的App不存在");
            break;
          }
        }
        case "0": {
          f = false;
          break;
        }
        default:
          break;
      }

    }
    return;
  }

  /**.
   * Handle the specify AppEcosystem
   * 
   * @param eco Specify system
   */
  private void executeEco(PersonalAppEcosystem eco) {
    AppFactory af = new AppFactory();
    Scanner in = new Scanner(System.in);
    boolean f = true;

    while (f) {
      show(Menu.APPECO_PERIOD);
      String op = in.next();
      switch (op) {
        case "1": {
          show(Menu.APPECO_ADDAPP);
          String s = in.next();
          Matcher m = Pattern.compile("(<\\S+>)|(\\d+)").matcher(s);
          if (m.find()) {
            String des = "App ::= " + m.group(1);
            if (!des.matches(Regex.APPECO_APP)) {
              showl("App格式错误");
              break;
            }
            if (Integer.parseInt(m.group(2)) > 4) {
              showl("指定轨道应为0-4");
              break;
            }
            App a = (App) af.create(des);
            eco.addObject(a, Integer.parseInt(m.group(2)));
            show(Menu.SUCCESS);
          } else {
            show(Menu.ERRORINPUT);
          }
          break;
        }
        case "2": {
          show(Menu.APPECO_REMOVEAPP);
          String s = in.next();
          if (s.matches(Regex.REGEX_LABEL)) {
            App a = eco.getApp(s);
            if (a != null) {
              if (eco.remove(a)) {
                show(Menu.SUCCESS);
                break;
              }
            } else {
              showl("系统不存在这个App");
              break;
            }
          }
          show(Menu.ERRORINPUT);
          break;
        }
        case "3": {
          showl(Menu.ENTROPHY + api.getObjectDistributionEntropy(eco));
          break;
        }
        case "4": {
          CircularOrbitHelper.visualize(eco);
          break;
        }
        case "5": {
          showl(eco.toString());
          break;
        }
        case "0": {
          f = false;
          break;
        }
        default:
          break;
      }
    }
  }
}
