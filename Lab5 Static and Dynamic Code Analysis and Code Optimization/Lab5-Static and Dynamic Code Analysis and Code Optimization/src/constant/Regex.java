package constant;

/**.
 * 存储circularOrbit，physicalObject，centralPoint包中所用到的正则表达式。
 */
public class Regex {
  /*
   * NormalType
   */
  public static String REGEX_LABEL = "[A-Za-z0-9]+";
  public static String REGEX_WORD = "[A-Za-z]+";
  public static String REGEX_NUMBER = "^[.\\d]*$|^[.\\d]*e[\\d]+$";
  public static String REGEX_NUMBER_BELOW1e4 = "^[.\\d]*$";
  public static String REGEX_NUMBER_ABOVE1e4 = 
                "^[1-9]\\.[\\d]+e[1-9][\\d]+$" + "|^[1-9]e[1-9][\\d]+$"
              + "|^[1-9][.\\d]+e[^0-3]$" + "|^[1-9]e[^0-3]$";
  public static String REGEX_NUMBER_SPEC = 
                "^[.\\d]*$" + "|^[1-9]\\.[\\d]+e[1-9][\\d]+$" + "|^[1-9]e[1-9][\\d]+$"
                + "|^[1-9][.\\d]+e[^0-3]$" + "|^[1-9]e[^0-3]$";
  public static String REGEX_SENTENCE = "[0-9A-Za-z\\s]+";
  public static String REGEX_SPACE = "\\s*";
  public static String REGEX_EXCEPTIONLOG = 
      "(\\d{4}-(0[1-9]|1[0-2])-(0\\d|1\\d|2\\d|3[0-1])) ((0\\d|1\\d|2[0-3]):([0-5]\\d):([0-5]\\d)) "
      + "\\[ERROR\\] \\([\\w.:]+\\) \\[(\\w+)\\]-\\[([\\w.<>]+)\\] \\[(\\w+.)*\\] "
      + "\\[(\\w+)\\] ([\\S\\s]+)";
  public static String REGEX_OPERATIONLOG = 
      "(\\d{4}-(0[1-9]|1[0-2])-(0\\d|1\\d|2\\d|3[0-1])) ((0\\d|1\\d|2[0-3]):([0-5]\\d):([0-5]\\d)) "
      + "\\[INFO\\] \\([\\w.:]+\\) \\[(\\w+)\\]-\\[([\\w.<>]+)\\] "
      + "([\\S\\s]+)";
  /*
   * TrackGame
   */
  public static String TRACKGAME_RACETYPE = 
      "Game" + REGEX_SPACE + "::=" + REGEX_SPACE + "(100|200|400)";
  public static String TRACKGAME_NUMOFTRACKS = 
      "NumOfTracks" + REGEX_SPACE + "::=" + REGEX_SPACE + "([4-9]|10)";
  public static String TRACKGAME_ATHLETE = 
      "Athlete" + REGEX_SPACE + "::=" + REGEX_SPACE + "<" + REGEX_SPACE
      + "([A-Za-z]+)" + REGEX_SPACE + //
      "," + REGEX_SPACE + "(\\d+)" + REGEX_SPACE + // number
      "," + REGEX_SPACE + "([A-Z]{3})" + REGEX_SPACE + // nation
      "," + REGEX_SPACE + "(\\d+)" + REGEX_SPACE + // age
      "," + REGEX_SPACE + "((\\d{2}|\\d{1})\\.\\d{2})" + REGEX_SPACE + ">"; // pb
  public static String TRACKGAME_NATION = "[A-Z]{3}";
  public static String TRACKGAME_PERSONALBEST = "(\\d{2}|\\d{1})\\.\\d{2}";
  /*
   * AtomStructure
   */
  public static String ATOM_ELEMENTNAME = 
      "ElementName" + REGEX_SPACE + "::=" + REGEX_SPACE + "([A-Z][a-z]?)";
  public static String ATOM_NUMBEROFTRACKS = 
      "NumberOfTracks" + REGEX_SPACE + "::=" + REGEX_SPACE + "(\\d+)";
  public static String ATOM_NUMBEROFELECTRON = 
      "NumberOfElectron" + REGEX_SPACE + "::=" + REGEX_SPACE
      + "(((\\d+)/(\\d+);" + REGEX_SPACE + ")*((\\d+)/(\\d+)))";
  /*
   * AppSystem
   */
  public static String APPECO_USER = "User" + REGEX_SPACE + "::=" + REGEX_SPACE + "([A-Za-z0-9]+)";
  public static String APPECO_INSTALL = 
      "InstallLog" + REGEX_SPACE + "::=" + REGEX_SPACE + "<" + REGEX_SPACE
      + "(\\d{4}-(0[1-9]|1[0-2])-(0\\d|1\\d|2\\d|3[0-1]))" + REGEX_SPACE // date
      + "," + REGEX_SPACE + "((0\\d|1\\d|2[0-3]):([0-5]\\d):([0-5]\\d))" + REGEX_SPACE // time
      + "," + REGEX_SPACE + "(" + REGEX_LABEL + ")" + REGEX_SPACE + ">"; // appname
  public static String APPECO_UNINSTALL = 
      "UninstallLog" + REGEX_SPACE + "::=" + REGEX_SPACE + "<" + REGEX_SPACE
      + "(\\d{4}-(0[1-9]|1[0-2])-(0\\d|1\\d|2\\d|3[0-1]))" + REGEX_SPACE + "," + REGEX_SPACE
      + "((0\\d|1\\d|2[0-3]):([0-5]\\d):([0-5]\\d))" + REGEX_SPACE 
      + "," + REGEX_SPACE + "(" + REGEX_LABEL + ")"
      + REGEX_SPACE + ">";
  public static String APPECO_USAGELOG = 
      "UsageLog" + REGEX_SPACE + "::=" + REGEX_SPACE + "<" + REGEX_SPACE
      + "(\\d{4}-(0[1-9]|1[0-2])-(0\\d|1\\d|2\\d|3[0-1]))" + REGEX_SPACE // date
      + "," + REGEX_SPACE + "((0\\d|1\\d|2[0-3]):([0-5]\\d):([0-5]\\d))" + REGEX_SPACE // time
      + "," + REGEX_SPACE + "(" + REGEX_LABEL + ")" + REGEX_SPACE // appname
      + "," + REGEX_SPACE + "(\\d+)" + REGEX_SPACE + ">"; // duration
  public static String APPECO_APP = 
      "App" + REGEX_SPACE + "::=" + REGEX_SPACE + "<" + REGEX_SPACE + "(" + REGEX_LABEL
      + ")" + REGEX_SPACE // name
      + ",(" + REGEX_LABEL + ")" + REGEX_SPACE // company
      + "," + REGEX_SPACE 
      + "(v?(" + REGEX_LABEL + "[._-])*" + REGEX_LABEL + ")" + REGEX_SPACE // version
      + "," + REGEX_SPACE + "(\"" + REGEX_SENTENCE + "\")" + REGEX_SPACE // description
      + "," + REGEX_SPACE + "(\"" + REGEX_SENTENCE + "\")" + REGEX_SPACE + ">"; // area
  public static String APPECO_RELATION = 
      "Relation" + REGEX_SPACE + "::=" + REGEX_SPACE + "<" + REGEX_SPACE + "("
      + REGEX_LABEL + ")" + REGEX_SPACE + "," 
          + REGEX_SPACE + "(" + REGEX_LABEL + ")" + REGEX_SPACE + ">";
  public static String APPECO_PERIOD = 
      "Period" + REGEX_SPACE + "::=" + REGEX_SPACE + "(Hour|Day|Week|Month)";
  public static String APPECO_VERSION = "v?([A-Za-z0-9]+[._-])*[A-Za-z0-9]+";
  public static String APPECO_DATE = "\\d{4}-(0[1-9]|1[0-2])-(0\\d|1\\d|2\\d|3[0-1])"; // yyyy-mm-dd
  public static String APPECO_TIME = "(0\\d|1\\d|2[0-3]):([0-5]\\d):([0-5]\\d)"; // hh:mm:ss
}
