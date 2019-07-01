package constant;

/**
 * 存储circularOrbit，physicalobject，centralPoint包中所用到的正则表达式。
 */
public class Regex {
	/*
	 * NormalType
	 */
	public static String REGEX_LABEL = "[\\w]+";
	public static String REGEX_WORD = "[A-Za-z]+";
	public static String REGEX_NUMBER = "^[-\\+]?[.\\d]*$|^[-\\+]?[.\\d]*e[\\d]+$";
	public static String REGEX_NUMBER_BELOW1e4 = "^[-\\+]?[.\\d]*$";
	public static String REGEX_NUMBER_ABOVE1e4 = "^[-\\+]?[1-9]\\.[\\d]+e[1-9][\\d]+$" 
									+ "|^[-\\+]?[1-9]e[1-9][\\d]+$" 
									+ "|^[-\\+]?[1-9][.\\d]+e[^0-3]$"
									+ "|^[-\\+]?[1-9]e[^0-3]$";
	public static String REGEX_SENTENCE = "[0-9A-Za-z\\s]+";
	/*
	 * TrackGame
	 */
	public static String TRACKGAME_RACETYPE = "Game ::= (100|200|400)";
	public static String TRACKGAME_NUMOFTRACKS = "NumOfTracks ::= ([4-9]|10)";
	public static String TRACKGAME_ATHLETE = "Athlete ::= <([A-Za-z]+),(\\d+),([A-Z]{3}),(\\d+),"
			+ "((\\d{2}|\\d{1})\\.\\d{2})>";
	public static String TRACKGAME_NATION = "[A-Z]{3}";
	public static String TRACKGAME_PERSONALBEST = "(\\d{2}|\\d{1})\\.\\d{2}";
	/*
	 * AtomStructure
	 */
	public static String ATOM_ELEMENTNAME = "ElementName ::= ([A-Z][a-z]?)";
	public static String ATOM_NUMBEROFTRACKS = "NumberOfTracks ::= (\\d+)";
	public static String ATOM_NUMBEROFELECTRON = "NumberOfElectron ::= (((\\d+)/(\\d+);)*((\\d+)/(\\d+)))";
	/*
	 * AppSystem
	 */
	public static String APPECO_USER = "User ::= ([A-Za-z0-9]+)";
	public static String APPECO_INSTALL = "InstallLog ::= <(\\d{4}-(0[1-9]|1[0-2])-(0\\d|1\\d|2\\d|3[0-1]))"
											+ ",((0\\d|1\\d|2[0-3]):([0-5]\\d):([0-5]\\d)),([\\w]+)>";
	public static String APPECO_UNINSTALL = "UninstallLog ::= <(\\d{4}-(0[1-9]|1[0-2])-(0\\d|1\\d|2\\d|3[0-1]))" 
											+",((0\\d|1\\d|2[0-3]):([0-5]\\d):([0-5]\\d)),([\\w]+)>";
	public static String APPECO_USAGELOG = "UsageLog ::= <(\\d{4}-(0[1-9]|1[0-2])-(0\\d|1\\d|2\\d|3[0-1]))"
											+",((0\\d|1\\d|2[0-3]):([0-5]\\d):([0-5]\\d)),([\\w]+),(\\d+)>";
	public static String APPECO_APP = "App ::= <("+REGEX_LABEL+"),("+REGEX_LABEL+"),"+"(v?("+REGEX_LABEL+"[._-])*"+REGEX_LABEL+"),"
											+ "(\""+REGEX_SENTENCE+"\"),(\""+REGEX_SENTENCE+"\")>";
	public static String APPECO_RELATION = "Relation ::= <("+REGEX_LABEL+"),("+REGEX_LABEL+")>";
	public static String APPECO_PERIOD = "Period ::= (Hour|Day|Week|Month)";
	public static String APPECO_VERSION = "v?([A-Za-z0-9]+[._-])*[A-Za-z0-9]+";
}
