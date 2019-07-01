package parser;

import applications.tools.AppInstallTime;
import applications.tools.Timespan;
import applications.tools.UsageLog;
import centralobject.Person;
import circularorbit.PersonalAppEcosystem;
import circularorbit.Relation;
import constant.Period;
import constant.Regex;
import constant.ReusablePool;
import exception.ElementLabelDuplicationException;
import exception.IllegalElementFormatException;
import exception.IncorrectElementDependencyException;
import exception.IncorrectElementLabelOrderException;
import exception.LackOfComponentException;
import exception.NoSuchElementException;
import exception.TrackNumberOutOfRangeException;
import exception.UndefinedElementException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import physicalobject.App;
import physicalobject.AppFactory;
import physicalobject.AppParamPackage;


/**.
 * PersonalAppEcosystem图文本输入解析器。能够解析文本转化为对象，同时具有格式检查功能。
 * 
 * <p>解析文本，将文本输入转化为对象. 提供方法使得解析器可以将App描述字符串通过get方法解析为App对象对应的属性，
 * 同时对于日志文件，将每条日志转化为对象存入管理类进行处理。
 * 
 * <p>文本格式的合法性检查.
 * 提供对于{@code label，word，number，sentence}等类型和PersonalAppEcosystem特有文本 格式进行检查的方法。
 *
 */
public class PersonalAppEcosystemParser extends Parser<Person, App> {
  private final Logger logger = Logger.getLogger(PersonalAppEcosystem.class);

  @Override
  public String parserFile(String filePath, IoStrategy io) throws UndefinedElementException, 
                                                    IllegalElementFormatException, 
                                                    NoSuchElementException, 
                                                    LackOfComponentException,
                                                    ElementLabelDuplicationException, 
                                                    IncorrectElementDependencyException {
    String s = getText(filePath, io);
    // Check Period
    Matcher m = Pattern.compile("[Pp]er?i?od\\s*::=\\s*\\S+").matcher(s);
    if (m.find()) {
      if (!ReusablePool.PATTERN_APPECO_PERIOD.matcher(m.group()).matches()) {
        logger.error("[" + IllegalElementFormatException.class.getName() + "] [Reselect] "
            + "Illegal period format, should be Day|Hour|Week|Month , but was " + m.group() + "");
        throw new IllegalElementFormatException(
            "Illegal period format, " + "should be Day|Hour|Week|Month , "
                + "but was " + m.group(), m.group());
      }
    } else {
      logger.error("[" + NoSuchElementException.class.getName() + "] [Reselect] "
          + "No Period in the file");
      throw new NoSuchElementException("No Period in the file");
    }
    // Check User
    m = Pattern.compile("[Uu]ser\\s*::=\\s*\\S+").matcher(s);
    if (m.find()) {
      if (!m.group().matches(Regex.APPECO_USER)) {
        logger.error("[" + IllegalElementFormatException.class.getName() + "] [Reselect] "
            + "Illegal User format, should be label type , but was: " + m.group() + "");
        throw new IllegalElementFormatException(
            "Illegal User format, " + "should be label type , but was: " + m.group(), m.group());
      }
    } else {
      logger.error("[" + NoSuchElementException.class.getName() + "] [Reselect] "
          + "No User in the file");
      throw new NoSuchElementException("No User in the file");
    }
    // Check Apps
    Set<String> apps = new HashSet<String>(); // save apps
    boolean f = false;
    m = Pattern.compile("[Aa]pps?\\s*::=\\s*<(\\s*[^>]+)+\\s*>").matcher(s);
    while (m.find()) {
      f = true;
      String mt = m.group();
      if (!mt.matches("App\\s*::=\\s*<[\\S\\s]+")) {
        logger.error("[" + IllegalElementFormatException.class.getName() + "] [Reselect] "
            + "App Tag spell wrong, should be App ::= , but was: " + m.group() + "");
        throw new IllegalElementFormatException("App Tag spell wrong, should be App ::= , "
            + "but was: " + m.group(),
            m.group());
      }
      if (!mt.matches("App\\s*::=\\s*<\\s*(\\S+)\\s*,\\s*(\\S+)\\s*,\\s*(\\S+)\\s*,"
          + "\\s*([\\S\\s]+)\\s*,\\s*([\\S\\s]+)\\s*>")) {
        logger.error("[" + LackOfComponentException.class.getName() + "] [Reselect] "
            + "App tag lack components, should be 5 parts, but was: " + m.group() + "");
        throw new LackOfComponentException("App tag lack components, should be 5 parts, "
            + "but was: " + m.group(),
            m.group());
      }
      Matcher mb = Pattern.compile(
          "App\\s*::=\\s*<\\s*(\\S+)\\s*," + "\\s*(\\S+)\\s*,\\s*(\\S+)\\s*" + ",\\s*(\""
              + Regex.REGEX_SENTENCE + "\")\\s*,\\s*(\"" 
              + Regex.REGEX_SENTENCE + "\")\\s*>").matcher(mt);
      if (mb.find()) {
        if (!mb.group(1).matches(Regex.REGEX_LABEL)) {
          logger.error("[" + IllegalElementFormatException.class.getName() + "] [Reselect] "
              + "App Name is not a label type, but was: " + mb.group(1) + "");
          throw new IllegalElementFormatException("App Name is not a label type, " 
              + "but was: " + mb.group(1),
              mb.group());
        }
        if (!mb.group(2).matches(Regex.REGEX_LABEL)) {
          logger.error("[" + IllegalElementFormatException.class.getName() + "] [Reselect] "
              + "App company is not a label type, but was: " + mb.group(2) + "");
          throw new IllegalElementFormatException("App company is not a label type, " 
              + "but was: " + mb.group(2),
              mb.group());
        }
        if (!mb.group(3).matches(Regex.APPECO_VERSION)) {
          logger.error("[" + IllegalElementFormatException.class.getName() + "] [Reselect] "
              + "App version format is illegal, but was: " + mb.group(3) + "");
          throw new IllegalElementFormatException("App version format is illegal, "
              + "but was: " + mb.group(3),
              mb.group());
        }
        if (mb.group().matches(Regex.APPECO_APP)) {
          if (apps.contains(mb.group(1))) {
            logger.error("[" + ElementLabelDuplicationException.class.getName() + "] [Reselect] "
                + "App tag is duplicated, the app's name is: " + mb.group(1) + "");
            throw new ElementLabelDuplicationException("App tag is duplicated, "
                + "the app's name is: " + mb.group(1), mb.group());
          }
          apps.add(mb.group(1));
        }
      } else {
        logger.error("[" + IllegalElementFormatException.class.getName() + "] [Reselect] "
            + "Illegal format of App tag, please check the description or area" + mt + "");
        throw new IllegalElementFormatException("Illegal format of App tag, " 
            + "please check the description or area",
            mt);
      }
    }
    if (!f) {
      logger.error("[" + NoSuchElementException.class.getName() + "] [Reselect] " 
            + "No App in the file");
      throw new NoSuchElementException("No App in the file");
    }
    // InstallLog
    Set<String> install = new HashSet<String>();
    m = Pattern.compile("Install[Ll]ogs?\\s*::=\\s*<(\\s*[^>]+)+\\s*>").matcher(s);
    while (m.find()) {
      String ul = m.group();
      if (!ul.matches("InstallLog\\s*::=\\s*<[\\S\\s]+")) {
        logger.error("[" + IllegalElementFormatException.class.getName() + "] [Reselect] "
            + "InstallLog Tag spell wrong, should be InstallLog ::= , but was: " + m.group() + "");
        throw new IllegalElementFormatException(
            "InstallLog Tag spell wrong, " + "should be InstallLog ::= , "
                + "but was: " + m.group(), m.group());
      }
      if (!ul.matches("InstallLog\\s*::=\\s*<\\s*(\\S+)\\s*,\\s*(\\S+)\\s*,\\s*(\\S+)\\s*>")) {
        logger.error("[" + LackOfComponentException.class.getName() + "] [Reselect] "
            + "InstallLog tag lack components, should be 3 parts, but was: " + ul + "");
        throw new LackOfComponentException("InstallLog tag lack components, " 
            + "should be 3 parts, but was: " + ul,
            ul);
      }
      if (ul.matches(
          "InstallLog\\s*::=\\s*<\\s*(\\S+)\\s*,\\s*(\\S+)\\s*" + ",\\s*([\\w]+)\\s*>")) {
        Matcher mb = Pattern.compile(
            "InstallLog\\s*::=\\s*<\\s*(\\S+)\\s*," + "\\s*(\\S+)\\s*,\\s*([\\w]+)\\s*>")
            .matcher(ul);
        mb.find();
        if (!apps.contains(mb.group(3))) {
          logger.error("[" + UndefinedElementException.class.getName() + "] [Reselect] "
              + "Undefined App in installlog, app name is: " + mb.group(3) + "");
          throw new UndefinedElementException(
              "Undefined App in installlog, " + "app name is: " + mb.group(3) + ", "
                  + "tag: " + mb.group(), mb.group());
        }
        if (!mb.group(1).matches(Regex.APPECO_DATE)) {
          logger.error("[" + IllegalElementFormatException.class.getName() + "] [Reselect] "
              + "Illegal Date format in installlog, should be yyyy-mm-dd, "
              + "but was " + mb.group(1) + "");
          throw new IllegalElementFormatException("Illegal Date format in installlog, "
              + "should be yyyy-mm-dd, but was " + mb.group(1) + ", "
                  + "tag: " + mb.group(), mb.group());
        }
        if (!mb.group(2).matches(Regex.APPECO_TIME)) {
          logger.error("[" + IllegalElementFormatException.class.getName() + "] [Reselect] "
              + "Illegal Time format in installlog, should be hh:mm:ss, "
              + "but was " + mb.group(2) + "");
          throw new IllegalElementFormatException("Illegal Time format in installlog, " 
              + "should be hh:mm:ss, but was "
              + mb.group(2) + ", tag: " + mb.group(), mb.group());
        }
        if (install.contains(ul)) {
          logger.error("[" + ElementLabelDuplicationException.class.getName() + "] [Reselect] "
              + "InstallLog tag is duplicated, the tag is: " + ul + "");
          throw new ElementLabelDuplicationException("InstallLog tag is duplicated, " 
              + "the tag is: " + ul, ul);
        }
        install.add(ul);
      } else {
        logger.error("[" + IllegalElementFormatException.class.getName() + "] [Reselect] "
            + "Illegal format of installlog, check the app name");
        throw new IllegalElementFormatException("Illegal format of installlog, " 
            + "check the app name, tag: " + ul,
            ul);
      }
    }
    // UninstallLog
    Set<String> uninstall = new HashSet<String>();
    m = Pattern.compile("[Uu]ninstall[Ll]ogs?\\s*::=\\s*<(\\s*[^>]+)+\\s*>").matcher(s);
    while (m.find()) {
      String ul = m.group();
      if (!ul.matches("UninstallLog\\s*::=\\s*<[\\S\\s]+")) {
        logger.error("[" + IllegalElementFormatException.class.getName() + "] [Reselect] "
            + "UninstallLog Tag spell wrong, should be UninstallLog ::= , "
            + "but was: " + m.group() + "");
        throw new IllegalElementFormatException(
            "UninstallLog Tag spell wrong, " + "should be UninstallLog ::= , "
                + "but was: " + m.group(), m.group());
      }
      if (!ul.matches("UninstallLog\\s*::=\\s*<\\s*(\\S+)\\s*,\\s*(\\S+)\\s*,\\s*(\\S+)\\s*>")) {
        logger.error("[" + LackOfComponentException.class.getName() + "] [Reselect] "
            + "UninstallLog tag lack components, should be 3 parts, but was: " + ul + "");
        throw new LackOfComponentException("UninstallLog tag lack components, " 
            + "should be 3 parts, but was: " + ul,
            ul);
      }
      if (ul.matches(
          "UninstallLog\\s*::=\\s*<\\s*(\\S+)\\s*,\\s*(\\S+)\\s*" + ",\\s*([\\w]+)\\s*>")) {
        Matcher mb = Pattern.compile(
            "UninstallLog\\s*::=\\s*<\\s*(\\S+)\\s*," + "\\s*(\\S+)\\s*,\\s*([\\w]+)\\s*>")
            .matcher(ul);
        mb.find();
        if (!apps.contains(mb.group(3))) {
          logger.error("[" + UndefinedElementException.class.getName() + "] [Reselect] "
              + "Undefined App in uninstalllog, app name is: " + mb.group(3) + "");
          throw new UndefinedElementException(
              "Undefined App in uninstalllog, " + "app name is: " + mb.group(3) + ", "
                  + "tag: " + mb.group(), mb.group());
        }
        if (!mb.group(1).matches(Regex.APPECO_DATE)) {
          logger.error("[" + IllegalElementFormatException.class.getName() + "] [Reselect] "
              + "Illegal Date format in uninstalllog, should be yyyy-mm-dd, "
              + "but was " + mb.group(1) + "");
          throw new IllegalElementFormatException("Illegal Date format in uninstalllog,"
              + " should be yyyy-mm-dd, but was " + mb.group(1) + ", "
                  + "tag: " + mb.group(), mb.group());
        }
        if (!mb.group(2).matches(Regex.APPECO_TIME)) {
          logger.error("[" + IllegalElementFormatException.class.getName() + "] [Reselect] "
              + "Illegal Time format in uninstalllog, should be hh:mm:ss, " 
              + "but was " + mb.group(2) + "");
          throw new IllegalElementFormatException("Illegal Time format in uninstalllog, "
              + "should be hh:mm:ss, but was " + mb.group(2) + ", tag: " + mb.group(), mb.group());
        }
        if (uninstall.contains(ul)) {
          logger.error("[" + ElementLabelDuplicationException.class.getName() + "] [Reselect] "
              + "UninstallLog tag is duplicated, the tag is: " + ul + "");
          throw new ElementLabelDuplicationException("UninstallLog tag is duplicated, " 
              + "the tag is: " + ul, ul);
        }
        uninstall.add(ul);
      } else {
        logger.error("[" + IllegalElementFormatException.class.getName() + "] [Reselect] "
            + "Illegal format of uninstalllog, check the app name");
        throw new IllegalElementFormatException("Illegal format of uninstalllog, " 
            + "check the app name, tag: " + ul,
            ul);
      }
    }
    // IncorrectTime
    Map<String, AppInstallTime> installing = new HashMap<String, AppInstallTime>();
    for (String app : apps) {
      String installregex = 
          "InstallLog\\s*::=\\s*<\\s*(" + Regex.APPECO_DATE + ")\\s*" + ",\\s*(" + Regex.APPECO_TIME
          + ")\\s*,\\s*" + app + "\\s*>";
      String uninstallregex = 
          "UninstallLog\\s*::=\\s*<\\s*(" + Regex.APPECO_DATE + ")\\s*" + ",\\s*("
          + Regex.APPECO_TIME + ")\\s*,\\s*" + app + "\\s*>";

      Matcher inm = Pattern.compile(installregex).matcher(s);
      Matcher unm = Pattern.compile(uninstallregex).matcher(s);
      AppInstallTime atu = new AppInstallTime();
      while (inm.find()) {
        // 如果存在对应的卸载日志
        if (unm.find()) {
          Instant st = Instant.parse(inm.group(1) + "T" + inm.group(4) + "Z");
          Instant et = Instant.parse(unm.group(1) + "T" + unm.group(4) + "Z");
          // 如果这个app安装周期出现重叠，抛出异常
          if (atu.overlap(new Timespan(st, et))) {
            logger.error("[" + IncorrectElementDependencyException.class.getName() + "] [Reselect] "
                + "The timespan consist of installLog: " + inm.group() 
                + " and uninstallLog: " + unm.group()
                + " overlaps other timespan");
            throw new IncorrectElementDependencyException(
                "The timespan consist of installLog: " + inm.group()
                + " and uninstallLog: " + unm.group() 
                + " overlaps other timespan", inm.group() + "," + unm.group());
          }
          atu.add(new Timespan(st, et));
        } else {
          Instant st = Instant.parse(inm.group(1) + "T" + inm.group(4) + "Z");
          Instant et = Instant.MAX;
          // 如果这个app安装周期出现重叠，抛出异常
          if (atu.overlap(new Timespan(st, et))) {
            logger.error("[" + IncorrectElementDependencyException.class.getName() + "] [Reselect] "
                + "The timespan consist of installLog: " + inm.group()
                + " and uninstallLog: TIME_MAX overlaps other timespan");
            throw new IncorrectElementDependencyException(
                "The timespan consist of installLog: " + inm.group()
                + " and uninstallLog: TIME_MAX overlaps other timespan", inm.group());
          }
          atu.add(new Timespan(st, et));
          // 如果出现多条安装日志却没有卸载日志
          if (inm.find()) {
            logger.error("[" + IncorrectElementDependencyException.class.getName() + "] [Reselect] "
                + "An installLog at most appear one time if there no uninstallLog, " + "App: " + app
                + " has more than one installLog: " + inm.group() + "");
            throw new IncorrectElementDependencyException(
                "An installLog at most appear one time if there no uninstallLog, App: " + app
                    + " has more than one installLog: " + inm.group(),
                inm.group());
          }
          break;
        }
      }
      // 存在剩余的卸载日志
      if (unm.find()) {
        logger.error("[" + IncorrectElementDependencyException.class.getName() + "] [Reselect] "
            + "An installLog matches at most one uninstallLog, App: " + app 
            + " has mismatched uninstallLog: "
            + unm.group() + "");
        throw new IncorrectElementDependencyException(
            "An installLog matches at most one uninstallLog, App: " + app
            + " has mismatched uninstallLog: " + unm.group(), unm.group());
      }
      installing.put(app, atu);
    }
    // UsageLog
    Set<String> usage = new HashSet<String>();
    m = Pattern.compile("[Uu]sage[Ll]ogs?\\s*::=\\s*<(\\s*[^>]+)+\\s*>").matcher(s);
    while (m.find()) {
      String ul = m.group();
      if (!ul.matches("UsageLog\\s*::=\\s*<[\\S\\s]+")) {
        logger.error("[" + IllegalElementFormatException.class.getName() + "] [Reselect] "
            + "UsageLog Tag spell wrong, should be UsageLog ::= , but was: " + m.group() + "");
        throw new IllegalElementFormatException(
            "UsageLog Tag spell wrong, " + "should be UsageLog ::= , "
                + "but was: " + m.group(), m.group());
      }
      if (!ul.matches("UsageLog\\s*::=\\s*<\\s*(\\S+)\\s*"
          + ",\\s*(\\S+)\\s*,\\s*(\\S+)\\s*,\\s*(\\S+)\\s*>")) {
        logger.error("[" + LackOfComponentException.class.getName() + "] [Reselect] "
            + "UsageLog tag lack components, should be 4 parts, but was: " + ul + "");
        throw new LackOfComponentException("UsageLog tag lack components, " 
            + "should be 4 parts, but was: " + ul, ul);
      }
      if (ul.matches("UsageLog\\s*::=\\s*<\\s*(\\S+)\\s*,\\s*(\\S+)\\s*"
          + ",\\s*([\\w]+)\\s*,\\s*(\\d+)\\s*>")) {
        Matcher mb = Pattern
            .compile("UsageLog\\s*::=\\s*<\\s*(\\S+)\\s*,\\s*(\\S+)\\s*"
                + ",\\s*([\\w]+)\\s*,\\s*(\\d+)\\s*>").matcher(ul);
        mb.find();
        if (!apps.contains(mb.group(3))) {
          logger.error("[" + UndefinedElementException.class.getName() + "] [Reselect] "
              + "Undefined App in usagelog, app name is: " + mb.group(3) + "");
          throw new UndefinedElementException(
              "Undefined App in usagelog, app name is: " + mb.group(3) + ", "
                  + "tag: " + mb.group(), mb.group());
        }
        if (!mb.group(1).matches(Regex.APPECO_DATE)) {
          logger.error("[" + IllegalElementFormatException.class.getName() + "] [Reselect] "
              + "Illegal Date format in usagelog, should be yyyy-mm-dd, " 
              + "but was " + mb.group(1) + "");
          throw new IllegalElementFormatException("Illegal Date format in usagelog, "
              + "should be yyyy-mm-dd, " + "but was "
              + mb.group(1) + ", tag: " + mb.group(), mb.group());
        }
        if (!mb.group(2).matches(Regex.APPECO_TIME)) {
          logger.error("[" + IllegalElementFormatException.class.getName() + "] [Reselect] "
              + "Illegal Time format in usagelog, should be hh:mm:ss, " 
              + "but was " + mb.group(2) + "");
          throw new IllegalElementFormatException("Illegal Time format in usagelog, "
              + "should be hh:mm:ss, " + "but was "
              + mb.group(2) + ", tag: " + mb.group(), mb.group());
        }
        Instant st = Instant.parse(mb.group(1) + "T" + mb.group(2) + "Z");
        if (!installing.get(mb.group(3)).overlap(new Timespan(st, st))) {
          logger.error("[" + IncorrectElementDependencyException.class.getName() + "] [Reselect] " 
              + "App: " + mb.group(3) + " is not installed in usagelog time points, "
                  + "usagelog: " + mb.group());
          throw new IncorrectElementDependencyException(
              "App: " + mb.group(3) + " is not installed in usagelog time points, "
                  + "usagelog: " + mb.group(), mb.group());
        }
        if (usage.contains(ul)) {
          logger.error("[" + ElementLabelDuplicationException.class.getName() + "] [Reselect] "
              + "UsageLog tag is duplicated, the tag is: " + ul + "");
          throw new ElementLabelDuplicationException("UsageLog tag is duplicated, " 
              + "the tag is: " + ul, ul);
        }
        usage.add(ul);
      } else {
        logger.error("[" + IllegalElementFormatException.class.getName() + "] [Reselect] "
            + "Illegal format of usagelog, check the app name or duration");
        throw new IllegalElementFormatException(
            "Illegal format of usagelog, check the app name or duration, " + "tag: " + ul, ul);
      }
    }
    // Relation
    m = Pattern.compile("[Rr]elations?\\s*::=\\s*<(\\s*[^>]+)+\\s*>").matcher(s);
    while (m.find()) {
      String rl = m.group();
      if (!rl.matches("Relation\\s*::=\\s*<[\\S\\s]+")) {
        logger.error("[" + IllegalElementFormatException.class.getName() + "] [Reselect] "
            + "Relation Tag spell wrong, should be Relation ::= , but was: " + m.group() + "");
        throw new IllegalElementFormatException(
            "Relation Tag spell wrong, " + "should be Relation ::= , "
                + "but was: " + m.group(), m.group());
      }
      if (rl.matches("Relation\\s*::=\\s*<\\s*(\\w+)\\s*,\\s*(\\w+)\\s*>")) {
        Matcher mb = Pattern.compile(Regex.APPECO_RELATION).matcher(rl);
        mb.find();
        if (!apps.contains(mb.group(1))) {
          if (!apps.contains(mb.group(2))) {
            logger.error("[" + UndefinedElementException.class.getName() + "] [Reselect] "
                + "Undefined Apps in relation, apps name: " + mb.group(1) + ", " + mb.group(2));
            throw new UndefinedElementException("Undefined Apps in relation, " 
                + "apps name: " + mb.group(1) + ", "
                + mb.group(2) + ", tag: " + mb.group(), mb.group());
          }
          logger.error("[" + UndefinedElementException.class.getName() + "] [Reselect] "
              + "Undefined Apps in relation, apps name: " + mb.group(1) + "");
          throw new UndefinedElementException(
              "Undefined App in relation, " + "app name: " + mb.group(1) + ", tag: " + rl, rl);
        }
        if (!apps.contains(mb.group(2))) {
          logger.error("[" + UndefinedElementException.class.getName() + "] [Reselect] "
              + "Undefined Apps in relation, apps name: " + mb.group(2) + "");
          throw new UndefinedElementException(
              "Undefined App in relation, " + "app name: " + mb.group(2) + ", tag: " + rl, rl);
        }
        if (mb.group(1).equals(mb.group(2))) {
          logger.error("[" + IncorrectElementDependencyException.class.getName() + "] [Reselect] "
              + "Relation can not point to itself, tag: " + rl + "");
          throw new IncorrectElementDependencyException("Relation can not point to itself, " 
              + "tag: " + rl, rl);
        }
      } else {
        logger.error("[" + IllegalElementFormatException.class.getName() + "] [Reselect] "
            + "Illegal format of raltion, check the app name" + rl + "");
        throw new IllegalElementFormatException("Illegal format of raltion, " 
            + "check the app name", rl);
      }
    }
    logger.info("Pass the file check: " + filePath);
    return s;
  }
  
  /**.
   * 检查图输入，检查图输入是否符合格式要求。文本读取方式默认FileInputStream
   * 
   * @param filePath 待检查文本路径
   * @return 如果文本输入合法，返回文本字符串， 否则返回null
   * @throws NoSuchElementException              缺少元素名称抛出
   * @throws IllegalElementFormatException       标签格式不正确抛出
   * @throws IncorrectElementDependencyException 元素之间依赖关系不正确抛出
   * @throws ElementLabelDuplicationException    元素标签重复抛出
   * @throws LackOfComponentException            缺少分量抛出
   */
  public String parserFile(String filePath) throws UndefinedElementException, 
                                                    IllegalElementFormatException, 
                                                    NoSuchElementException, 
                                                    LackOfComponentException,
                                                    ElementLabelDuplicationException, 
                                                    IncorrectElementDependencyException {
    return parserFile(filePath, new ChannelIo());
  }

  /**.
   * 获得所有App集合
   * 
   * @param fileText 待解析文本，文本通过合法性检查
   * @return 一个App对象的集合
   */
  public Set<App> getApps(String fileText) {
    if (fileText == null) {
      throw new NullPointerException("FileText is null pointer");
    }

    Set<App> apps = new HashSet<App>();
    Pattern pattern = Pattern.compile(Regex.APPECO_APP);
    Matcher ma = pattern.matcher(fileText);
    AppFactory af = new AppFactory();
    while (ma.find()) {
      apps.add((App) af.create(new AppParamPackage(
          ma.group(1), ma.group(2), ma.group(3), ma.group(5), ma.group(6))));
    }
    logger.info("Parser all apps");
    return apps;
  }

  /**.
   * 获得所有App之间的无向关系
   * 
   * @param fileText 待解析文本，文本通过合法性检查
   * @return App之间的合作关系，关系为无向关系。
   */
  public List<Relation<String>> getRelation(String fileText) {
    if (fileText == null) {
      throw new NullPointerException("FileText is null pointer");
    }

    List<Relation<String>> relation = new ArrayList<Relation<String>>();
    Pattern pattern = Pattern.compile(Regex.APPECO_RELATION);
    Matcher ma = pattern.matcher(fileText);
    while (ma.find()) {
      Relation<String> r = new Relation<String>(ma.group(1), ma.group(2));
      relation.add(r);
    }
    logger.info("Parser all apps relations");
    return relation;
  }

  /**.
   * 获得划分时间范围
   * 
   * @param fileText 待解析文本，文本通过合法性检查
   * @return
   */
  public Period getPeriod(String fileText) {
    if (fileText == null) {
      throw new NullPointerException("FileText is null pointer");
    }

    Pattern pattern = Pattern.compile(Regex.APPECO_PERIOD);
    Matcher ma = pattern.matcher(fileText);
    Period period = null;
    if (ma.find()) {
      switch (ma.group(1)) {
        case "Day": {
          period = Period.Day;
          break;
        }
        case "Hour": {
          period = Period.Hour;
          break;
        }
        case "Week": {
          period = Period.Week;
          break;
        }
        case "Month": {
          period = Period.Month;
          break;
        }
        default: {
          throw new IllegalArgumentException("No match period");
        }
      }
    }
    logger.info("Parser period: " + period);
    return period;
  }

  /**.
   * 获得用户名称
   * 
   * @param fileText 待解析文本，文本通过合法性检查
   * @return
   */
  public String getUser(String fileText) {
    if (fileText == null) {
      throw new IllegalArgumentException("fileText is null pointer");
    }

    Pattern pattern = Pattern.compile(Regex.APPECO_USER);
    Matcher ma = pattern.matcher(fileText);
    if (ma.find()) {
      logger.info("Parser user name: " + ma.group(1));
      return ma.group(1);
    }
    return null;
  }

  /**.
   * 获得每一个App安装在用户手机上的时间段AppUsageTime
   * 
   * @param fileText 待解析文本，文本通过合法性检查
   * @return
   */
  public Map<String, AppInstallTime> getAppInstallTime(String fileText) {
    if (fileText == null) {
      throw new IllegalArgumentException("fileText is null pointer");
    }

    String regex = Regex.APPECO_APP;
    Pattern pattern = Pattern.compile(regex);
    Set<String> apps = new HashSet<String>(); // Get the apps name
    Matcher ma = pattern.matcher(fileText);
    while (ma.find()) {
      apps.add(ma.group(1));
    }

    Map<String, AppInstallTime> amap = new HashMap<String, AppInstallTime>();
    fileText = fileText.replaceAll(Regex.APPECO_USAGELOG + "\n", "");
    for (String app : apps) {
      String installregex = 
          "InstallLog\\s*::=\\s*<\\s*(" + Regex.APPECO_DATE + ")\\s*" + ",\\s*(" + Regex.APPECO_TIME
          + ")\\s*,\\s*" + app + "\\s*>";
      String uninstallregex = 
          "UninstallLog\\s*::=\\s*<\\s*(" + Regex.APPECO_DATE + ")\\s*" + ",\\s*("
          + Regex.APPECO_TIME + ")\\s*,\\s*" + app + "\\s*>";

      Matcher inm = Pattern.compile(installregex).matcher(fileText);
      Matcher unm = Pattern.compile(uninstallregex).matcher(fileText);
      AppInstallTime atu = new AppInstallTime();
      while (inm.find()) {
        if (unm.find()) {
          Instant st = Instant.parse(inm.group(1) + "T" + inm.group(4) + "Z");
          Instant et = Instant.parse(unm.group(1) + "T" + unm.group(4) + "Z");
          atu.add(new Timespan(st, et));
        } else {
          Instant st = Instant.parse(inm.group(1) + "T" + inm.group(4) + "Z");
          Instant et = Instant.MAX;
          atu.add(new Timespan(st, et));
          break;
        }
      }
      amap.put(app, atu);
    }
    logger.info("Parser the install time on apps");
    return amap;
  }

  /**.
   * 获得使用日志列表
   * 
   * @param fileText 待解析文本，文本通过合法性检查
   * @return 一组使用日志
   */
  public List<UsageLog> getUsageLog(String fileText) {
    if (fileText == null) {
      throw new IllegalArgumentException("fileText is null pointer");
    }

    List<UsageLog> ulist = new ArrayList<UsageLog>();
    Matcher m = Pattern.compile(Regex.APPECO_USAGELOG).matcher(fileText);
    while (m.find()) {
      UsageLog ul = new UsageLog(m.group(8), Instant.parse(m.group(1) + "T" + m.group(4) + "Z"),
          Integer.parseInt(m.group(9)));
      ulist.add(ul);
    }
    logger.info("Parser all the usagelog");
    return ulist;
  }

  /**.
   * 从描述字符串中匹配出App的名称
   * 
   * @param s 描述App的字符串
   * {@code App ::= <App 名称,公司,版本,"功能描述","业务领域">}
   *          
   * @return App的名称
   */
  public String getName(String s) {
    assert s != null : "Input string is null";
    String regex = "<([A-Za-z0-9]+),";
    Pattern p = Pattern.compile(regex);
    Matcher m = p.matcher(s);
    if (m.find()) {
      return m.group(1);
    }
    return "";
  }

  /**.
   * 从描述字符串中匹配出App所属公司
   * 
   * @param s 描述App字符串
   *          {@code App ::= <App 名称,公司,版本,"功能描述","业务领域">}
   * @return App所属公司
   */
  public String getCompany(String s) {
    assert s != null : "Input string is null";
    String regex = ",([A-Za-z0-9]+),";
    Pattern p = Pattern.compile(regex);
    Matcher m = p.matcher(s);
    if (m.find()) {
      return m.group(1);
    }
    return "";
  }

  /**.
   * 从描述字符串中匹配出App的版本
   * 
   * @param s 描述App的字符串
   *          {@code App ::= <App 名称,公司,版本,"功能描述","业务领域">}
   * @return App的版本
   */
  public String getVersion(String s) {
    assert s != null : "Input string is null";
    String regex = "<\\S+,\\S+,(v?([A-Za-z0-9]+[._-])*[A-Za-z0-9]+),";
    Pattern p = Pattern.compile(regex);
    Matcher m = p.matcher(s);
    if (m.find()) {
      return m.group(1);
    }
    return "";
  }

  /**.
   * 从描述字符串中匹配出App的描述
   * 
   * @param s 描述App的字符串
   *          {@code App ::= <App 名称,公司,版本,"功能描述","业务领域">}
   * @return App的描述，带有双引号
   */
  public String getDescription(String s) {
    assert s != null : "Input string is null";
    String regex = ",(\"[A-Za-z0-9\\s]+\"),";
    Pattern p = Pattern.compile(regex);
    Matcher m = p.matcher(s);
    if (m.find()) {
      return m.group(1);
    }
    return "";
  }

  /**.
   * 从描述字符串中匹配出App所属领域
   * 
   * @param s 描述App的字符串
   *          {@code App ::= <App 名称,公司,版本,"功能描述","业务领域">}
   * @return App的所属领域，带有双引号
   */
  public String getArea(String s) {
    assert s != null : "Input string is null";
    String regex = ",(\"[A-Za-z0-9\\s]+\")>";
    Pattern p = Pattern.compile(regex);
    Matcher m = p.matcher(s);
    if (m.find()) {
      return m.group(1);
    }
    return "";
  }

}
