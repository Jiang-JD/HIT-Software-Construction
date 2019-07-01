package manager;

import apis.CircularOrbitApis;
import apis.Difference;
import applications.tools.AppInstallTime;
import applications.tools.Timespan;
import applications.tools.UsageLog;
import centralobject.Person;
import centralobject.PersonFactory;
import circularorbit.PersonalAppEcosystem;
import circularorbit.Relation;
import constant.Period;
import constant.Regex;
import exception.ElementLabelDuplicationException;
import exception.IllegalElementFormatException;
import exception.IncorrectElementDependencyException;
import exception.LackOfComponentException;
import exception.NoSuchElementException;
import exception.UndefinedElementException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.apache.log4j.Logger;

import parser.IoStrategy;
import parser.PersonalAppEcosystemParser;
import physicalobject.App;

/**.
 * PersonalApp管理类，这是一个{@code mutable} 类型，负责对用户软件生态系统中的
 * App和日志进行维护，管理和划分。其具有对日志进行划分从而生成不同轨道系统的功能。
 * 管理类中保管着所有App集合，App之间的关系，用户使用日志，App安装时间，用户名称， 划分时间，管理器能够对这些内容进行操作管理。
 */
public class PersonalAppManager implements Cloneable {

  private final Set<App> apps = new HashSet<App>();
  private final Map<String, App> appmap = new HashMap<String, App>();
  private final Map<String, AppInstallTime> appsInstall = new HashMap<String, AppInstallTime>();
  private final List<UsageLog> usageLog = new ArrayList<UsageLog>();
  private final List<Relation<String>> relation = new ArrayList<Relation<String>>();
  private final Map<String, List<Relation<String>>> relmap = 
        new HashMap<String, List<Relation<String>>>();
  private String user = "";
  private Period period = Period.Day;
  private Logger logger = Logger.getLogger(PersonalAppManager.class);

  /*
   * Abstract Function: 
   *    AF(apps, appsInstall, usageLog, relation, user, period) = 负责对用户软件生态系统中
   *                              的 App和日志进行维护，管理和划分的管理器
   * 
   * Rep Invariant: 
   *    apps 不含null 
   *    appsInstall size = apps.size， 不含null 
   *    usageLog不含null 
   *    relation 不含null，不含重复无向关系 
   *    user 名称为""或符合label格式 
   *    period 不为null
   * 
   * Safety from exposure: 
   *    所有域都是private final的，generateEcosystems()做了防御性拷贝，防止系统内部与管理器内部使用相同的引用。
   *    所有对外界暴露的API中涉及到的LIst等可变数据结构都做了防御型拷贝。
   * 
   */

  private void checkRep() {
    for (App a : apps) {
      assert a != null : "Null pointer in apps";
    }
    assert appsInstall.size() == apps.size() : "Size not equal";
    for (AppInstallTime a : appsInstall.values()) {
      assert a != null : "Null pointer in appsInstall";
    }
    for (UsageLog u : usageLog) {
      assert u != null : "Null pointer in usageLog";
    }
    for (int i = 0; i < relation.size() - 1; i++) {
      assert relation.get(i) != null : "Null pointer in relation";
      for (int j = i + 1; j < relation.size(); j++) {
        assert !relation.get(i).equals(relation.get(j)) : "Duplicate relation";
      }
    }
    assert user.equals("") || user.matches(Regex.REGEX_LABEL);
    assert period != null;
  }

  /**.
   * 初始化管理器，将文本文件解析为一组App对象，App安装时间，使用日志，App合作关系，划分时间和用户。 输入文件所在路径，对管理器进行初始化.
   * 划分时间默认为Day
   * 
   * @param filePath 待解析文本文件所在路径
   * @param io 读取文本的IO策略
   * @throws IncorrectElementDependencyException App或安装日志或卸载、使用日志出现时间段不正确抛出
   * @throws ElementLabelDuplicationException    App重复声明抛出
   * @throws LackOfComponentException            App缺少分量时抛出
   * @throws NoSuchElementException              缺少某类元素时抛出
   * @throws IllegalElementFormatException       App或其他元素标签格式不正确时抛出
   * @throws UndefinedElementException           安装、卸载、使用日志中出现未定义的App
   */
  public boolean initial(String filePath, IoStrategy io) throws UndefinedElementException, 
                                                        IllegalElementFormatException, 
                                                        NoSuchElementException, 
                                                        LackOfComponentException,
                                                        ElementLabelDuplicationException, 
                                                        IncorrectElementDependencyException {
    clear();
    PersonalAppEcosystemParser aep = new PersonalAppEcosystemParser();
    String fileText = aep.parserFile(filePath, io);
    apps.addAll(aep.getApps(fileText));
    for (App a : apps) {
      appmap.put(a.getName(), a);
    }
    appsInstall.putAll(aep.getAppInstallTime(fileText));
    usageLog.addAll(aep.getUsageLog(fileText));
    relation.addAll(aep.getRelation(fileText));
    for (Relation<String> r : relation) {
      List<Relation<String>> rl1 = relmap.get(r.getOne());
      List<Relation<String>> rl2 = relmap.get(r.getOther(r.getOne()));
      if (rl1 == null) {
        rl1 = new ArrayList<Relation<String>>();
      }
      if (rl2 == null) {
        rl2 = new ArrayList<Relation<String>>();
      }
      rl1.add(r);
      rl2.add(r);
      relmap.put(r.getOne(), rl1);
      relmap.put(r.getOther(r.getOne()), rl2);
    }
    period = aep.getPeriod(fileText);
    user = aep.getUser(fileText);
    checkRep();
    logger.info("Initial PersonalAppManager"
        + ", IO: " + io.getClass().getSimpleName());
    return true;
  }
  
  /**.
   * 初始化管理器，将文本文件解析为一组App对象，App安装时间，使用日志，App合作关系，划分时间和用户。 输入文件所在路径，对管理器进行初始化.
   * 划分时间默认为Day
   * 
   * @param filePath 待解析文本文件所在路径
   * @throws IncorrectElementDependencyException App或安装日志或卸载、使用日志出现时间段不正确抛出
   * @throws ElementLabelDuplicationException    App重复声明抛出
   * @throws LackOfComponentException            App缺少分量时抛出
   * @throws NoSuchElementException              缺少某类元素时抛出
   * @throws IllegalElementFormatException       App或其他元素标签格式不正确时抛出
   * @throws UndefinedElementException           安装、卸载、使用日志中出现未定义的App
   */
  public boolean initial(String filePath) throws UndefinedElementException, 
                                                        IllegalElementFormatException, 
                                                        NoSuchElementException, 
                                                        LackOfComponentException,
                                                        ElementLabelDuplicationException, 
                                                        IncorrectElementDependencyException {
    clear();
    PersonalAppEcosystemParser aep = new PersonalAppEcosystemParser();
    String fileText = aep.parserFile(filePath);
    apps.addAll(aep.getApps(fileText));
    for (App a : apps) {
      appmap.put(a.getName(), a);
    }
    appsInstall.putAll(aep.getAppInstallTime(fileText));
    usageLog.addAll(aep.getUsageLog(fileText));
    relation.addAll(aep.getRelation(fileText));
    for (Relation<String> r : relation) {
      List<Relation<String>> rl1 = relmap.get(r.getOne());
      List<Relation<String>> rl2 = relmap.get(r.getOther(r.getOne()));
      if (rl1 == null) {
        rl1 = new ArrayList<Relation<String>>();
      }
      if (rl2 == null) {
        rl2 = new ArrayList<Relation<String>>();
      }
      rl1.add(r);
      rl2.add(r);
      relmap.put(r.getOne(), rl1);
      relmap.put(r.getOther(r.getOne()), rl2);
    }
    period = aep.getPeriod(fileText);
    user = aep.getUser(fileText);
    checkRep();
    logger.info("Initial PersonalAppManager");
    return true;
  }
  
  /**.
   * 将用户生态系统写回文件, 将修改过的用户生态系统重新按照规定格式写回指定文件目录
   * 
   * @param filePath 指定文件路径
   * @return 如果写回成功，返回true，否则返回false
   * @throws IOException 文件输出异常抛出
   */
  public boolean outPutFile(String filePath, IoStrategy io) throws IOException {
    return io.outPutFile(filePath, this);
  }

  /**.
   * 对管理器进行克隆
   * 
   * @return 克隆对象
   */
  @Override
  public PersonalAppManager clone() {
    try {
      super.clone();
    } catch (CloneNotSupportedException e1) {
      e1.printStackTrace();
    }
    PersonalAppManager newpam = new PersonalAppManager();
    for (App a : apps) {
      newpam.apps.add(a);
    }
    for (UsageLog ul : usageLog) {
      newpam.usageLog.add(ul);
    }
    newpam.user = user;
    newpam.period = period;
    for (Relation<String> rl : relation) {
      newpam.relation.add(rl);
    }
    for (Map.Entry<String, AppInstallTime> e : appsInstall.entrySet()) {
      newpam.appsInstall.put(e.getKey(), e.getValue().clone());
    }
    return newpam;
  }

  /**.
   * 由文本输入生成一组用户生态系统，生态系统根据不同的时间范围进行划分， 在调用此方法前，确保已经调用{@code initial} 方法对管理器进行初始化
   * 
   * @return 一组用户生态系统
   */
  public List<PersonalAppEcosystem> generateEcosystems() {
    /* Check arguments */
    if (apps.isEmpty()) {
      throw new IllegalArgumentException("Apps not initialized");
    }
    if (appsInstall.isEmpty()) {
      throw new IllegalArgumentException("AppsInstall not initialized");
    }
    if (usageLog.isEmpty()) {
      throw new IllegalArgumentException("UsageLog not initialized");
    }
    if (relation.isEmpty()) {
      throw new IllegalArgumentException("Relation not initialized");
    }
    if (user.equals("")) {
      throw new IllegalArgumentException("User not initialized");
    }

    /* Divided by period */
    List<Timespan> timePartition = timePartition();

    /* Divide usageLogs in list */
    List<List<UsageLog>> logPartition = logPartition(timePartition);

    /* Find the Apps installed during the timespan */
    List<List<String>> appPartition = appPartition(timePartition);

    assert appPartition.size() == logPartition.size();

    /*
     * Generating intimacy map and build Ecosystem Depend on the arguments:
     * logPartition appPartition timePartition
     */
    List<PersonalAppEcosystem> systems = new ArrayList<PersonalAppEcosystem>();
    systems.addAll(massProduction(timePartition, logPartition, appPartition));
    checkRep();
    logger.info("Generate list of PersonalAppEcosystem");
    return systems;
  }

  /**.
   * 一个计数器对象，用于统计次数 https://www.jianshu.com/p/c42da3ad8743
   */
  private static class MutableInteger {
    private int val;

    public MutableInteger(int val) {
      this.val = val;
    }

    public int get() {
      return val;
    }

    public void set(int val) {
      this.val = val;
    }

    // used to print value convinently
    public String toString() {
      return Integer.toString(val);
    }
  }

  /**.
   * 根据划分日期生成一组时间段划分。规则是
   * 
   * <p>Divided rules by different time periods
   * 
   * <p>{@code Hour} : <br>
   * Find the nearest integer hour in the past, i.e. 2019/5/2 9:51:22 becomes
   * 2019/5/2 9:00:00. Then make timespan in hours. 2019/5/2 9:00:00...2019/5/2
   * 10:00:00...2019/5/2 11:00:00.
   * 
   * <p>{@code Day} : <br>
   * Find the day of that date, start in 00:00:00.i.e. 2019/5/2 9:51:22 becomes
   * 2019/5/2 00:00:00 Then make timespan in days. 2019/5/2 00:00:00...2019/5/3
   * 00:00:00...2019/5/4 00:00:00.
   * 
   * <p>{@code Week} : <br>
   * Find the Monday of the week as the starting point, start in 00:00:00.i.e.
   * 2019/5/2 9:51:22 becomes 2019/4/29 00:00:00 Then make timespan in Weeks.
   * 2019/4/29 00:00:00...2019/5/6 00:00:00
   * 
   * 
   * <p>{@code Month} : <br>
   * Find the 1st of the month as the starting point, start in 00:00:00.i.e.
   * 2019/5/2 9:51:22 becomes 2019/5/1 00:00:00 Then make timespan in Weeks.
   * 2019/5/1 00:00:00...2019/6/1 00:00:00
   * 
   * @return 一组时间划分
   */
  private List<Timespan> timePartition() {
    /* Get the earliest and latest time */
    Instant min = Instant.MAX;
    Instant max = Instant.MIN;
    for (UsageLog l : usageLog) {
      if (l.getTime().isBefore(min)) {
        min = l.getTime();
      }
      if (l.getTime().isAfter(max)) {
        max = l.getTime();
      }
    }
    LocalDateTime earliest = LocalDateTime.ofInstant(min, ZoneOffset.UTC);
    LocalDateTime latest = LocalDateTime.ofInstant(max, ZoneOffset.UTC);

    /* Divided by period */
    /*
     * Divided rules by different time periods
     * 
     * Hour: Find the nearest integer hour in the past, i.e. 2019/5/2 9:51:22
     * becomes 2019/5/2 9:00:00. Then make timespan in hours. 2019/5/2
     * 9:00:00...2019/5/2 10:00:00...2019/5/2 11:00:00.
     * 
     * Day: Find the day of that date, start in 00:00:00.i.e. 2019/5/2 9:51:22
     * becomes 2019/5/2 00:00:00 Then make timespan in days. 2019/5/2
     * 00:00:00...2019/5/3 00:00:00...2019/5/4 00:00:00.
     * 
     * Week: Find the Monday of the week as the starting point, start in
     * 00:00:00.i.e. 2019/5/2 9:51:22 becomes 2019/4/29 00:00:00 Then make timespan
     * in Weeks. 2019/4/29 00:00:00...2019/5/6 00:00:00
     * 
     * Month: Find the 1st of the month as the starting point, start in
     * 00:00:00.i.e. 2019/5/2 9:51:22 becomes 2019/5/1 00:00:00 Then make timespan
     * in Weeks. 2019/5/1 00:00:00...2019/6/1 00:00:00
     * 
     */
    List<Timespan> timePartition = new ArrayList<Timespan>();
    LocalDateTime start;
    LocalDateTime end;
    switch (period) {
      case Hour: {
        start = earliest.minusMinutes(earliest.getMinute())
                        .minusSeconds(earliest.getSecond()); // Set start time by hour
        while (start.isBefore(latest)) {
          end = start.plusHours(1);
          Instant s = start.toInstant(ZoneOffset.UTC);
          Instant e = end.toInstant(ZoneOffset.UTC);
          timePartition.add(new Timespan(s, e)); // add timespan in 1 hour
          start = start.plusHours(1);
        }
        break;
      }
      case Day: {
        start = earliest.minusMinutes(earliest.getMinute())
            .minusHours(earliest.getHour())
            .minusSeconds(earliest.getSecond());
        while (start.isBefore(latest)) {
          end = start.plusDays(1);
          Instant s = start.toInstant(ZoneOffset.UTC);
          Instant e = end.toInstant(ZoneOffset.UTC);
          timePartition.add(new Timespan(s, e)); // add timespan in 1 day
          start = start.plusDays(1);
        }
        break;
      }
      case Week: {
        start = earliest.minusDays(earliest.getDayOfWeek().getValue() - 1)
                        .minusMinutes(earliest.getMinute())
                        .minusHours(earliest.getHour())
                        .minusSeconds(earliest.getSecond());
        while (start.isBefore(latest)) {
          end = start.plusWeeks(1);
          Instant s = start.toInstant(ZoneOffset.UTC);
          Instant e = end.toInstant(ZoneOffset.UTC);
          timePartition.add(new Timespan(s, e)); // add timespan in 1 week
          start = start.plusWeeks(1);
        }
        break;
      }
      case Month: {
        start = earliest.minusMinutes(earliest.getMinute())
            .minusHours(earliest.getHour())
            .minusSeconds(earliest.getSecond())
            .minusDays(earliest.getDayOfMonth() - 1);
        while (start.isBefore(latest)) {
          end = start.plusMonths(1);
          Instant s = start.toInstant(ZoneOffset.UTC);
          Instant e = end.toInstant(ZoneOffset.UTC);
          timePartition.add(new Timespan(s, e)); // add timespan in 1 month
          start = start.plusMonths(1);
        }
        break;
      }
      default: {
        throw new IllegalArgumentException("No match period");
      }
    }
    return timePartition;
  }

  /**.
   * 根据时间段的划分将使用日志划分，某个时间段对应的使用日志规则为：
   * 
   * <p>如果使用日志对应的时间点在时间段内，那么这条使用日志应当属于这个划分，不考虑使用时长
   * 
   * @param timePartition 时间段划分
   * @return 一组使用日志划分
   */
  private List<List<UsageLog>> logPartition(List<Timespan> timePartition) {
    List<List<UsageLog>> logPartition = new ArrayList<List<UsageLog>>();
    List<UsageLog> tmp = new LinkedList<UsageLog>(usageLog);
    for (Timespan ts : timePartition) {
      List<UsageLog> ulist = new ArrayList<UsageLog>();
      Iterator<UsageLog> it = tmp.iterator();
      while (it.hasNext()) {
        UsageLog ul = it.next();
        if (ul.within(ts)) {
          ulist.add(ul);
          it.remove();
        }
      }
      //      for (UsageLog ul : tmp) {
      //        if (ul.within(ts)) {
      //          removel.add(ul);
      //          ulist.add(ul);
      //        }
      //      }
      logPartition.add(ulist);
    }
    return logPartition;
  }

  /**.
   * 根据时间段的划分将App划分，某个时间段对应的App规则为：
   * 
   * <p>如果某个应用在某个时间段内被安装或被卸 载，那么它应该出现在该时间段对应的划分中； <br>
   * 如果某个应用在某个时 间段之前已被卸载，则不应出现在该时间段的划分中；<br>
   * 如果某个应 用在某个时间点被安装且后续没有被卸载，那么它就应始终出现在后续 各个时间段的划分中
   * 
   * @param timePartition 时间段划分
   * @return 一组App的划分
   */
  private List<List<String>> appPartition(List<Timespan> timePartition) {
    List<List<String>> appPartition = new ArrayList<List<String>>();
    for (Timespan ts : timePartition) {
      List<String> alist = new ArrayList<String>();
      for (Map.Entry<String, AppInstallTime> e : appsInstall.entrySet()) {
        if (e.getValue().overlap(ts)) {
          alist.add(e.getKey());
        }
      }
      appPartition.add(alist);
    }
    return appPartition;
  }

  /**.
   * 生产一组用户生态系统
   * 
   * @param logPartition  使用日志划分
   * @param appPartition  App划分
   * @param timePartition 时间段划分
   * @return
   */
  private List<PersonalAppEcosystem> massProduction(List<Timespan> timePartition, 
                                                List<List<UsageLog>> logPartition,
      List<List<String>> appPartition) {
    if (logPartition.size() != appPartition.size() || timePartition.size() != appPartition.size() 
        || logPartition.size() != timePartition.size()) {
      throw new IllegalArgumentException("Log,App,Time partition size not euqal");
    }

    PersonFactory pf = new PersonFactory();
    Person user = (Person) pf.create(this.user);
    List<Map<String, Double>> intimacy = new ArrayList<Map<String, Double>>(); // 亲密度映射，以后可能有用
    List<PersonalAppEcosystem> ecoList = new ArrayList<PersonalAppEcosystem>();
    for (int i = 0; i < logPartition.size(); i++) {
      /* Each time build a Ecosystem */
      Map<String, Double> amap = new HashMap<String, Double>();
      Map<String, MutableInteger> frequency = new HashMap<String, MutableInteger>();
      Map<String, MutableInteger> usetime = new HashMap<String, MutableInteger>();
      double maxIntimacy = Double.MIN_VALUE;
      double minIntimacy = Double.MAX_VALUE;
      // initialize
      for (UsageLog ul : logPartition.get(i)) {
        MutableInteger initFValue = new MutableInteger(1);
        MutableInteger oldFValue = frequency.put(ul.getName(), initFValue);

        if (oldFValue != null) {
          initFValue.set(oldFValue.get() + 1);
        }

        MutableInteger initTValue = new MutableInteger(ul.getDuration());
        MutableInteger oldTValue = usetime.put(ul.getName(), initTValue);

        if (oldTValue != null) {
          initTValue.set(oldTValue.get() + ul.getDuration());
        }
      }
      for (String app : appPartition.get(i)) {
        MutableInteger fre = frequency.get(app);
        MutableInteger len = usetime.get(app);
        if (fre == null) {
          amap.put(app, 0.0);
          if (maxIntimacy < 0.0) {
            maxIntimacy = 0.0;
          }
          if (minIntimacy > 0.0) {
            minIntimacy = 0.0;
          }
        } else {
          int f = fre.get();
          int l = len.get();
          double in = (double) f * 0.6 * (double) l * 0.4;
          amap.put(app, in);
          if (maxIntimacy < in) {
            maxIntimacy = in;
          }
          if (minIntimacy > in) {
            minIntimacy = in;
          }
        }
      }

      /* Build Track Map */
      double interval = (maxIntimacy - minIntimacy) / 5;
      BigDecimal maxIntimacyD = new BigDecimal(maxIntimacy);
      BigDecimal minIntimacyD = new BigDecimal(minIntimacy);
      BigDecimal intervalD = maxIntimacyD.subtract(minIntimacyD).divide(new BigDecimal(5));
      List<List<App>> trackmap = new ArrayList<List<App>>();
      double j = maxIntimacy;
      BigDecimal start = maxIntimacyD;
      for (int l = 0; l < 5; l++) {
        List<App> alist = new ArrayList<App>(); 
        BigDecimal end = start.subtract(intervalD);
        for (Map.Entry<String, Double> e : amap.entrySet()) {
          BigDecimal value = new BigDecimal(e.getValue());
          if ((start.compareTo(value) == 1) 
               && (end.compareTo(value) == -1 || end.compareTo(value) == 0)
               && !value.equals(0)) {
            alist.add(getApp(e.getKey()));
          } else if (l == 0 && start.compareTo(value) == 0) {
            alist.add(getApp(e.getKey()));
          } else if (l == 4 && end.compareTo(value) == 0) {
            alist.add(getApp(e.getKey()));
          }
        }
        trackmap.add(alist);
        start = start.subtract(intervalD);
      }
      /* Build Ecosystem */
      PersonalAppEcosystem pae = buildEcosystem(
          "AppEco" + i, timePartition.get(i), trackmap, appPartition.get(i),
          user);
      ecoList.add(pae);
      intimacy.add(amap);
    }
    return ecoList;
  }

  /**.
   * 生成一个用户生态系统
   * 
   * @param name     系统名称
   * @param ts       代表时间段
   * @param trackmap 轨道物体
   * @param apps     App对象
   * @param user     用户
   * @return 一个用户生态系统
   */
  private PersonalAppEcosystem buildEcosystem(String name, Timespan ts, 
                                         List<List<App>> trackmap, List<String> apps,
      Person user) {
    assert trackmap.size() == 5 : 
      "buildEco the length of trackmap is less than 5, but " + trackmap.size();
    /* Build Ecosystem */
    PersonalAppEcosystem pae = new PersonalAppEcosystem(name, ts);
    // add central point
    pae.addCentralPoint(user);
    logger.info(pae.getName() + " add new central point: " + user);
    // add objects and track
    for (int i = 0; i < 5; i++) {
      pae.addTrack(i + 1);
      logger.info(pae.getName() + " add new track: " + (i + 1));
      for (int k = 0; k < trackmap.get(i).size(); k++) {
        pae.addObject(trackmap.get(i).get(k), i);
        logger.info(pae.getName() 
            + " add new App: " + trackmap.get(i).get(k).getName() 
            + " on track " + (i + 1));
      }
    }
    // add relation
    for (String app : apps) {
      List<Relation<String>> rl = relmap.get(app);
      for (Relation<String> r : rl) {
        pae.addOrbitRelation(getApp(app), getApp(r.getOther(app)));
        logger.info(pae.getName() + " add new Relation: " + app + "," + r.getOther(app));
      }
      //      for (Relation<String> r : relation) {
      //        if (r.contains(app)) {
      //          pae.addOrbitRelation(getApp(app), getApp(r.getOther(app)));
      //          logger.info(pae.getName() + " add new Relation: " + app + "," + r.getOther(app));
      //        }
      //
      //      }
    }
    return pae;
  }

  /**.
   * 将管理器中保存的内容清空
   */
  public void clear() {
    apps.clear();
    relation.clear();
    appsInstall.clear();
    usageLog.clear();
    user = "";
    period = Period.Day;
    logger.info("Clear PersonalAppManager");
  }

  /**.
   * 根据输入的App的名称查找App对象
   * 
   * @param name 待查找App的名称
   * @return 如果找到，返回App对象，否则返回null
   */
  public App getApp(String name) {
    if (name == null) {
      throw new NullPointerException("App name is null pointer");
    }

    return appmap.get(name);
  }

  /**.
   * 设置使用日志的划分时间，输入参数Period枚举类型
   * 
   * @param period 划分时间
   * @return 如果设置成功，返回{@code true} ，否则返回{@code false}
   */
  public boolean setPeriod(Period period) {
    this.period = period;
    return true;
  }

  /**.
   * 获取管理器中的使用日志划分时间
   * 
   * @return 划分时间，Period枚举类型
   */
  public Period getPeriod() {
    return period;
  }
  
  /**
   * .获得用户名称
   * @return 用户名称
   */
  public String getUser() {
    return user;
  }
  
  /**
   * .获得所有app安装时间映射的引用，注意引用是直接引用
   * @return 所有安装时间引用
   */
  public Map<String, AppInstallTime> getAllInstallTime() {
    return new HashMap<String, AppInstallTime>(appsInstall);
  }
  
  /**
   * .获得所有app关系
   * @return 所有app关系
   */
  public List<Relation<String>> getAllRelation() {
    return new ArrayList<Relation<String>>(relation);
  }
  
  /**.
   * 获取管理器中的某个app的安装时间的引用
   * 
   * @return 某个app的安装时间的引用，不存在返回null
   */
  public AppInstallTime getInstallTime(String appName) {
    if (appName == null) {
      throw new NullPointerException();
    }
    return appsInstall.get(appName);
  }
  
  /**
   * .获得所有使用日志
   * @return 所有日志的引用
   */
  public List<UsageLog> getUsageLog() {
    return usageLog;
  }

  /**.
   * 计算两个 App 之间的逻辑距离，逻辑距离是指能够通过合作关系连接的最小边数，若没有连接，则逻辑距离为无穷大， 自身与自身的距离为0
   * 
   * @param a1 待计算的App
   * @param a2 待计算的App
   * @return 两个App之间的逻辑距离，若出现查找App不存在时，返回-1
   */
  public int getDistance(App a1, App a2) {
    if (a1 == null || a2 == null) {
      throw new IllegalArgumentException("getDistance argument is null pointer");
    }
    String base = a1.getName();
    String target = a2.getName();
    List<String> vertex = new ArrayList<String>();
    for (App a : apps) {
      vertex.add(a.getName());
    }

    // 检查是否有顶点未加入顶点集
    if (!(vertex.contains(base)) || !(vertex.contains(target))) {
      return -1;
    }

    if (base.equals(target)) {
      return 0;
    }

    // BFS，depth数组存储各顶点层数，也即最短路径
    
    boolean isFind = false;
    Queue<String> queue = new LinkedList<String>();
    queue.clear();
    queue.offer(base);
    boolean[] visited = new boolean[vertex.size()];
    String v = null;
    String p = null;
    
    int[] depth = new int[vertex.size()]; // 存放各顶点的所在层数
    visited[vertex.indexOf(base)] = true;
    depth[vertex.indexOf(base)] = 0;

    while (!queue.isEmpty()) {
      v = queue.poll();
      Iterator<String> it = relatives(v).iterator(); // 将这个人的所有关系生成迭代器
      if (it.hasNext()) {
        p = it.next();
      }
      while (p != null) {
        if (visited[vertex.indexOf(p)] == false) {
          queue.add(p);
          visited[vertex.indexOf(p)] = true;
          depth[vertex.indexOf(p)] = depth[vertex.indexOf(v)] + 1; // p层数为父结点层数+1
          if (p.equals(target)) {
            isFind = true; // 找到目标立即跳出循环
            break;
          }
        }
        if (it.hasNext()) {
          p = (String) it.next();
        } else {
          p = null;
        }
      }
      if (isFind) {
        break;
      }
    }

    if (!isFind) {
      return Integer.MAX_VALUE;
    }
    checkRep();
    assert depth[vertex.indexOf(p)] == -1 || depth[vertex.indexOf(p)] >= 0;
    logger.info("Calculate App Logic Distance: " + depth[vertex.indexOf(p)]);
    return depth[vertex.indexOf(p)];
  }

  /**.
   * 获得所有与指定App有合作关系的App
   * 
   * @param app 待查找App名称
   * @return 所有有关App名称集合
   */
  private Set<String> relatives(String app) {
    Set<String> relatives = new HashSet<String>();
    for (Relation<String> r : relation) {
      if (r.contains(app)) {
        relatives.add(r.getOther(app));
      }
    }
    return relatives;
  }

  /**.
   * 获取两个时间段的轨道系统结构差异。 返回Difference对象
   * 
   * @param t1 待比较时间段，非空
   * @param t2 待比较时间段，非空
   * @return 两个轨道系统之间的差异生成的Difference对象
   */
  public Difference getDifference(Timespan t1, Timespan t2) {
    if (t1 == null || t2 == null) {
      throw new IllegalArgumentException("Null pointer in time span");
    }
    List<Timespan> timePartition = new ArrayList<Timespan>();
    timePartition.add(t1);
    timePartition.add(t2);
    List<PersonalAppEcosystem> li = massProduction(timePartition, logPartition(timePartition),
        appPartition(timePartition));
    CircularOrbitApis<App> api = new CircularOrbitApis<App>();
    checkRep();
    logger.info("Get difference in two timespan: " + t1.toString() + "," + t2.toString());
    return api.getDifference(li.get(0), li.get(1));
  }

  /**.
   * 根据输入时间段生成一组生态系统
   * 
   * @param t 时间段，要求非空
   * @return 一组生态系统
   */
  public List<PersonalAppEcosystem> getTimspanAppEco(List<Timespan> t) {
    List<PersonalAppEcosystem> li = massProduction(t, logPartition(t), appPartition(t));
    checkRep();
    return li;
  }

  /**.
   * 添加App到管理器中，要求非空
   * 
   * @param apps    App对象
   * @param install 安装时间
   * @return 如果成功返回true，否则false
   */
  public boolean add(App apps, AppInstallTime install) {
    if (apps == null) {
      throw new NullPointerException("null pointer apps");
    }
    if (install == null) {
      throw new NullPointerException("null pointer install");
    }
    if (getApp(apps.getName()) != null) {
      return false;
    }

    this.apps.add(apps);
    this.appmap.put(apps.getName(), apps);
    this.appsInstall.put(apps.getName(), install);
    logger.info("Add new App: " + apps.getName());
    return true;
  }

  /**.
   * 移除App到管理器中，要求非空
   * 
   * @param apps App对象
   * @return 如果成功返回true，否则false
   */
  public boolean remove(App apps) {
    if (apps == null) {
      throw new NullPointerException("null pointer apps");
    }
    if (getApp(apps.getName()) == null) {
      return false;
    }

    this.apps.remove(apps);
    this.appmap.remove(apps.getName());
    this.appsInstall.remove(apps.getName());
    logger.info("Remove an App: " + apps.getName());
    return true;
  }

  /**.
   * 添加一组App到管理器中，要求非空
   * 
   * @param apps    App对象
   * @param install 安装时间,数量与App数量相同
   * @return 如果成功返回true，否则false
   */
  public boolean addAll(List<App> apps, List<AppInstallTime> install) {
    if (apps == null) {
      throw new NullPointerException("null pointer apps");
    }
    if (install == null) {
      throw new NullPointerException("null pointer install");
    }
    if (apps.size() != install.size()) {
      throw new IllegalArgumentException("Not euqals in apps and install time");
    }
    StringBuilder sb = new StringBuilder("");
    for (int i = 0; i < apps.size(); i++) {
      this.apps.add(apps.get(i));
      this.appmap.put(apps.get(i).getName(), apps.get(i));
      sb.append(apps.get(i).getName() + " ");
      this.appsInstall.put(apps.get(i).getName(), install.get(i));
    }

    logger.info("Add list of new apps: " + sb.toString());
    return true;
  }

  /**.
   * 移除一组App到管理器中，要求非空
   * 
   * @param apps App对象
   * @return 如果成功返回true，否则false
   */
  public boolean removeAll(List<App> apps) {
    if (apps == null) {
      throw new NullPointerException("null pointer apps");
    }
    for (App a : apps) {
      if (getApp(a.getName()) == null) {
        return false;
      }
    }
    StringBuilder s = new StringBuilder("");
    for (int i = 0; i < apps.size(); i++) {
      this.apps.remove(apps.get(i));
      this.appmap.remove(apps.get(i).getName());
      s.append(apps.get(i).getName() + " ");
      this.appsInstall.remove(apps.get(i).getName());
    }
    logger.info("Remove list of new apps: " + s.toString());
    return true;
  }

  /**.
   * 获得所有Apps对象
   * 
   * @return 一组app对象
   */
  public Set<App> getApps() {
    return new HashSet<App>(apps);
  }
}
