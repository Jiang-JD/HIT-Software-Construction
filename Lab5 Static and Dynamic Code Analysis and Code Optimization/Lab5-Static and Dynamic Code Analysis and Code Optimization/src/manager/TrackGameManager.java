package manager;

import applications.tools.Divider;
import circularorbit.CircularOrbitFactory;
import circularorbit.TrackGame;
import exception.ElementLabelDuplicationException;
import exception.IllegalElementFormatException;
import exception.IncorrectElementLabelOrderException;
import exception.LackOfComponentException;
import exception.NoSuchElementException;
import exception.TrackNumberOutOfRangeException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import parser.IoStrategy;
import parser.TrackGameParser;
import physicalobject.Athlete;

/**.
 * {@code TrackGameManager} 是一个 {@code mutable} 类型，负责管理径赛中所有运动员和安排
 * 比赛分组的管理器。该类会在运行时维护一个所有运动员的集合，根据不同需求对集合进行不同的操作
 *
 */
public class TrackGameManager {
  private final Set<Athlete> athletes = new HashSet<Athlete>();// 运动员集合
  private final List<List<Athlete>> group = new ArrayList<List<Athlete>>(); // 保存分组
  private int tracknum = 0;
  private String racetype = "";
  private Logger logger = Logger.getLogger(TrackGameManager.class);

  /*
   * Abstract Function: 
   *    AF(athletes, group) = 一个维护运动员列表，组织比赛编排的管理器
   * 
   * Rep Invariant: 
   *    athletes 运动员集合，集合中对象不为空 
   *    group 分组情况，group或者为空表，或者前n-1组所含运动员数量相同，第n组运动员数量 <= 前n-1组数量 
   *    tracknum <=10 && >=4的正整数
   *    racetype 只能为 100 200 400 之一
   * 
   * Safety from exposure: 
   *    所有域是private final的，返回的mutable变量例如List Map，方法grouping(),
   *    exchange()都做了防御性拷贝
   * 
   */

  private void checkRep() {
    assert !athletes.contains(null) : "Null object exists in Athlete Set";
    assert tracknum >= 4 : "Track number should >= 1, but was" + tracknum;
    assert tracknum <= 10 : "Track number should <= 10, but was" + tracknum;
    assert racetype.matches("(100|200|400)") 
              : "RaceType should be one of 100 200 400, but was " + racetype;
    boolean divide = true;
    if (!group.isEmpty()) {
      int size = group.get(0).size(); // 每一小组的大小
      for (int i = 0; i < group.size() - 1; i++) {
        if (size != group.get(i).size()) {
          divide = false;
          break;
        }
      }
      if (size < group.get(group.size() - 1).size()) {
        divide = false;
      }
    }
    assert group.isEmpty() || divide : "Wrong type grouping. Number of each group are "
        + "unequal or number of last group larger than before.Groups=" + group.size();
  }

  /**.
   * 初始化管理器，将文本文件解析为一组Athlete对象，跑道数量，比赛项目。 输入文件所在路径，对管理器进行初始化.
   * 
   * @param filePath 待解析文本文件所在路径
   * @param io 读取文本的IO策略
   * @throws IllegalElementFormatException       运动员，跑道，比赛项目格式错误抛出
   * @throws ElementLabelDuplicationException    运动员标签重复抛出
   * @throws LackOfComponentException            运动员标签缺少分量时抛出
   * @throws IncorrectElementLabelOrderException 运动员标签分量顺序错误时抛出
   * @throws NoSuchElementException              缺少运动员或比赛项目或跑道数量抛出
   * @throws TrackNumberOutOfRangeException      跑道数目错误抛出
   */
  public boolean initial(String filePath, IoStrategy io)
      throws IllegalElementFormatException, NoSuchElementException, 
              IncorrectElementLabelOrderException,
      LackOfComponentException, ElementLabelDuplicationException, TrackNumberOutOfRangeException {
    clear();
    TrackGameParser tgp = new TrackGameParser();
    String t = tgp.parserFile(filePath, io);
    athletes.addAll(tgp.athletes(t));
    tracknum = tgp.trackNum(t);
    racetype = tgp.raceType(t);
    checkRep();
    logger.info("Initialize TrackGame Manager, filePaht: " + filePath 
        + ", IO: " + io.getClass().getSimpleName());
    return true;
  }
  
  /**.
   * 初始化管理器，将文本文件解析为一组Athlete对象，跑道数量，比赛项目。 输入文件所在路径，对管理器进行初始化.
   * 
   * @param filePath 待解析文本文件所在路径
   * @throws IllegalElementFormatException       运动员，跑道，比赛项目格式错误抛出
   * @throws ElementLabelDuplicationException    运动员标签重复抛出
   * @throws LackOfComponentException            运动员标签缺少分量时抛出
   * @throws IncorrectElementLabelOrderException 运动员标签分量顺序错误时抛出
   * @throws NoSuchElementException              缺少运动员或比赛项目或跑道数量抛出
   * @throws TrackNumberOutOfRangeException      跑道数目错误抛出
   */
  public boolean initial(String filePath)
      throws IllegalElementFormatException, NoSuchElementException, 
              IncorrectElementLabelOrderException,
      LackOfComponentException, ElementLabelDuplicationException, TrackNumberOutOfRangeException {
    clear();
    TrackGameParser tgp = new TrackGameParser();
    String t = tgp.parserFile(filePath);
    athletes.addAll(tgp.athletes(t));
    tracknum = tgp.trackNum(t);
    racetype = tgp.raceType(t);
    checkRep();
    logger.info("Initialize TrackGame Manager, filePaht: " + filePath);
    return true;
  }
  
  /**.
   * 将径赛系统写回文件, 将修改过的轨道系统重新按照规定格式写回指定文件目录
   * 
   * @param filePath 指定文件路径
   * @return 如果写回成功，返回true，否则返回false
   * @throws IOException  文件写入异常抛出
   */
  public boolean outPutFile(String filePath, IoStrategy io) throws IOException {
    return io.outPutFile(filePath, this);
  }

  /**
   *.清空管理器中保存的内容，运动员，分组，比赛项目，轨道数目等
   */
  public void clear() {
    athletes.clear();// 运动员集合
    group.clear();// 保存分组
    tracknum = 0;
    racetype = "";
    logger.info("Clear TrackGame Manager");
  }

  public boolean isEmpty() {
    return athletes.isEmpty() && tracknum == 0 && racetype.equals("");
  }

  /**.
   * 设置径赛跑道数目
   * 
   * @param tracknum 跑道数目
   */
  public void setTrackNumber(int tracknum) {
    this.tracknum = tracknum;
    assert tracknum >= 4 : "Track number should >= 4";
  }

  /**.
   * 获得跑道数目
   * 
   * @return 跑道数目
   */
  public int getTrackNumber() {
    return tracknum;
  }

  /**.
   * 设置比赛项目
   * 
   * @param racetype 比赛项目
   */
  public void setRaceType(String racetype) {
    this.racetype = racetype;
    assert racetype.matches("(100|200|400)") : "RaceType should be one of 100 200 400";
    logger.info("Set Game type: " + racetype);
  }

  /**.
   * 获得比赛项目
   * 
   * @return 比赛项目
   */
  public String getRaceType() {
    return racetype;
  }
  
  /**
   * .获得所有运动员对象集合的拷贝
   * @return 集合拷贝
   */
  public Set<Athlete> getAthletes() {
    return new HashSet<Athlete>(athletes);
  }

  /**.
   * 将一个运动员添加到系统的运动员集合中，要求运动员对象非空,运动员此前并未添加
   * 
   * @param athlete 待添加的运动员
   * @return 如果添加成功返回true，否则false
   */
  public boolean add(Athlete athlete) {
    if (athlete == null) {
      return false;
    }
    if (athletes.contains(athlete)) {
      return false;
    }
    athletes.add(athlete);
    logger.info("Add new Athlete: " + athlete.getName());
    return true;
  }

  /**.
   * 将一组运动员添加到系统的运动员集合中，要求运动员对象非空,运动员此前并未添加
   * 
   * @param athlete 待添加的运动员
   * @return 如果添加成功返回true，否则false
   */
  public boolean addAll(List<Athlete> athlete) {
    if (athlete == null) {
      return false;
    }
    StringBuilder s = new StringBuilder();
    for (Athlete a : athlete) {
      s.append(a.getName() + " ");
      if (athletes.contains(a)) {
        return false;
      }
    }
    athletes.addAll(athlete);
    logger.info("Add list of new athletes: " + s.toString());
    return true;
  }

  /**.
   * 将一名运动员移除，运动员非空
   * 
   * @param a 待移除运动员，运动员非空
   * @return 如果移除成功，返回true，否则若不存在，返回false
   */
  public boolean remove(Athlete a) {
    assert a != null : "Athlete is null object";
    if (!athletes.contains(a)) {
      return false;
    }
    logger.info("Remove athlete: " + a.getName());
    return athletes.remove(a);
  }

  /**
   * .移除一组运动员对象
   * @param aths 待移除的一组运动员
   * @return 如果移除成功，返回true，否则如果存在一个或多个不存在的远动员，返回false
   */
  public boolean removeAll(List<Athlete> aths) {
    assert aths != null : "Athlete is null object";
    StringBuilder s = new StringBuilder();
    for (Athlete a : aths) {
      s.append(a.getName() + " ");
      if (!athletes.contains(a)) {
        return false;
      }
    }
    logger.info("Remove list of athletes: " + s.toString());
    return athletes.removeAll(aths);
  }

  /**.
   * 将运动员分为不同的小组，每个运动员属于单独一组且不会在另一组中出现。每一组表示一场比赛的分配，每一组的 人数不能超 过跑道数、每一组的每条跑道里最多 1
   * 位运动员（但可以没有运动员）； 如果第 n 组的人数少于跑道数 ，则第 0 到第 n-1 各组的人数必须等于跑道数；同一个运动员只能出现在一组比赛中
   * 
   * @param tracknum 跑道数目，跑道数目为正整数且 >= 1
   * @param divider  分组器
   */
  protected List<List<Athlete>> groupingL(int tracknum, Divider divider) {
    assert tracknum >= 1 : "Track Number should >= 1, but was " + tracknum;
    assert divider != null : "Divider is null object";
    List<Athlete> athletes = new ArrayList<Athlete>(this.athletes);
    group.clear();
    group.addAll(divider.grouping(tracknum, athletes));
    checkRep();
    return new ArrayList<List<Athlete>>(group); // 防御性拷贝
  }

  /**.
   * 将运动员分为不同的小组，每个运动员属于单独一组且不会在另一组中出现。每一组表示一场比赛的分配，每一组的 人数不能超 过跑道数、每一组的每条跑道里最多 1
   * 位运动员（但可以没有运动员）； 如果第 n 组的人数少于跑道数 ，则第 0 到第 n-1 各组的人数必须等于跑道数；同一个运动员只能出现在一组比赛中
   * 
   * @param divider  分组器
   * @return 运动员的比赛分组，生成一组径赛轨道系统对象分别表示一组，组之间的顺序以列表中顺序为标准
   */
  public List<TrackGame> grouping(Divider divider) {
    if (divider == null) {
      throw new NullPointerException("Divider is null object");
    }
    List<List<Athlete>> athletes = groupingL(tracknum, divider);
    logger.info("Generate the group of TrackGame, strategy: " + divider.getClass().getSimpleName());
    return buildTrackGames(athletes);
  }

  /**.
   * 获得分组后某个运动员所在组的编号，编号为从0开始递增的正整数，如果所有的组中都没有需要查找的运动员，则返回-1
   * 
   * @param a 需要查找的运动员，运动员非空
   * @return 所在组编号，若没找到则返回-1
   */
  public int indexOfGroup(Athlete a) {
    assert a != null : "Athlete is null object";
    for (int i = 0; i < group.size(); i++) {
      if (group.get(i).contains(a)) {
        return i;
      }
    }
    return -1;
  }

  /**.
   * 给两个选手更换赛道，更换组，如果两个选手在同一组，则更换赛道，如果在不同组，则更换组且更换赛道。
   * 两个选手不能为null，两个选手必须在径赛中存在，即必须在选手集合中。
   * 
   * @param a1 需要交换选手
   * @param a2 需要交换选手
   * @return 如果交换成功，返回调换后的分组，否则如果运动员在系统中不存在或交换失败，返回null。
   */
  protected List<List<Athlete>> exchangeL(Athlete a1, Athlete a2) {
    if (!(athletes.contains(a1) && athletes.contains(a2))) {
      return null;
    }
    // 若a1 a2在同一组，则交换两者在组中顺序即可
    int index1 = indexOfGroup(a1);
    int index2 = indexOfGroup(a2);
    int i = group.get(index1).indexOf(a1);
    int j = group.get(index2).indexOf(a2);

    if (index1 == index2) {
      Collections.swap(group.get(index1), i, j);
    } else { // 若a1 a2不在同一组，交换组和组中所在位置
      group.get(index1).remove(a1);
      group.get(index1).add(i, a2);
      group.get(index2).remove(a2);
      group.get(index2).add(j, a1);
    }
    checkRep();
    return new ArrayList<List<Athlete>>(group);
  }

  /**.
   * 给两个选手更换赛道，更换组，如果两个选手在同一组，则更换赛道，如果在不同组，则更换组且更换赛道。
   * 两个选手不能为null，两个选手必须在径赛中存在，即必须在选手集合中。
   * 
   * @param a1 需要交换选手
   * @param a2 需要交换选手
   * @return 如果交换成功，返回调换后的返回调换后的一组轨道系统对象，每一个对象表示一个分组，否则如果运动员在系统中不存在或交换失败，返回null。
   */
  public List<TrackGame> exchange(Athlete a1, Athlete a2) {
    List<List<Athlete>> athletes = exchangeL(a1, a2);
    if (athletes == null) {
      return null;
    }
    logger.info("Exchange two athletes position, " + a1.getName() + "," + a2.getName());
    return buildTrackGames(athletes);
  }

  /**.
   * 将输入List型Athlete分组转化为一组TrackGame对象
   * 
   * @return 一组TrackGame对象
   */
  private List<TrackGame> buildTrackGames(List<List<Athlete>> athletes) {
    List<TrackGame> list = new ArrayList<TrackGame>();
    for (int i = 0; i < athletes.size(); i++) {
      List<Athlete> al = new ArrayList<Athlete>(athletes.get(i));
      List<List<Athlete>> als = new ArrayList<List<Athlete>>();
      for (Athlete a : al) {
        List<Athlete> tmp = new ArrayList<Athlete>();
        tmp.add(a);
        als.add(tmp);
      }
      TrackGame tg = CircularOrbitFactory.build("TrackGame" + i, racetype, tracknum, als);
      list.add(tg);
    }
    return list;
  }

  /**.
   * 获取运动员对象，通过号码，不存在返回null
   * 
   * @param number 运动员号码
   * @return
   */
  public Athlete getAthlete(int number) {
    for (Athlete a : athletes) {
      if (a.getNumber() == number) {
        return a;
      }
    }
    return null;
  }

  /**.
   * 获取运动员对象，通过姓名，不存在返回null
   * 
   * @param name 名字
   * @return athlete
   */
  public Athlete getAthlete(String name) {
    for (Athlete a : athletes) {
      if (a.getName().equals(name)) {
        return a;
      }
    }
    return null;
  }
}
