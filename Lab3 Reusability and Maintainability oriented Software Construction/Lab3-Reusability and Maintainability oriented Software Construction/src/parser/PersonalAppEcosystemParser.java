package parser;

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

import applications.AppInstallTime;
import applications.Timespan;
import applications.UsageLog;
import centralObject.Person;
import circularOrbit.Relation;
import constant.Period;
import constant.Regex;
import customException.personalAppEcosystemException.AppLackOfComponentException;
import customException.personalAppEcosystemException.IllegalAppNameException;
import customException.personalAppEcosystemException.IllegalUsageLogFormatException;
import customException.personalAppEcosystemException.InstallLogIllegalTimeFormatException;
import customException.personalAppEcosystemException.NoPeriodException;
import customException.personalAppEcosystemException.NoUserException;
import customException.personalAppEcosystemException.UndefinedAppException;
import customException.personalAppEcosystemException.UninstallLogIllegalTimeFormatException;
import physicalObject.App;
import physicalObject.AppFactory;
import physicalObject.AppParamPackage;

/**
 * PersonalAppEcosystem图文本输入解析器。能够解析文本转化为对象，同时具有格式检查功能。
 * <p>解析文本，将文本输入转化为对象.
 * 提供方法使得解析器可以将App描述字符串通过get方法解析为App对象对应的属性，
 * 同时对于日志文件，将每条日志转化为对象存入管理类进行处理。
 * <p>文本格式的合法性检查.
 * 提供对于{@code label，word，number，sentence}等类型和PersonalAppEcosystem特有文本
 * 格式进行检查的方法。
 *
 */
public class PersonalAppEcosystemParser extends Parser<Person, App> {

	@Override
	public boolean parserFile(String fileName) {
		File file = new File(fileName);
		FileInputStream fis;
		String s = "";
		try {
			fis = new FileInputStream(file);
			InputStreamReader isr;
			isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader bf = new BufferedReader(isr);
			StringBuilder sb = new StringBuilder();
			while (s != null) {
				s = bf.readLine();
				if (s == null) {
					break;
			}
			sb.append(s.trim() + "\n");
			}
			s = sb.toString();
			bf.close();
		}catch (FileNotFoundException e2) {
			e2.printStackTrace();
			return false;
		}catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		//Check Period
		Matcher m = Pattern.compile("Period ::= \\S+").matcher(s);
		if(m.find()) {
			if(!m.group().matches(Regex.APPECO_PERIOD)) {
				try {
					throw new IllegalArgumentException("Illegal period, should be Day|Hour|Week|Month , but was " + m.group());
				} catch(IllegalArgumentException e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		else {
			try {
				throw new NoPeriodException("File has No Period");
			} catch (NoPeriodException e) {
				e.printStackTrace();
				return false;
			}
		}
		//Check User 
		m = Pattern.compile("User ::= \\S+").matcher(s);
		if(m.find()) {
			if(!m.group().matches(Regex.APPECO_USER)) {
				try {
					throw new IllegalArgumentException("Illegal User, should be label type , but was " + m.group());
				} catch(IllegalArgumentException e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		else {
			try {
				throw new NoUserException("File has No User");
			} catch (NoUserException e) {
				e.printStackTrace();
				return false;
			}
		}
		//Check Apps
		Set<String> apps = new HashSet<String>();
		m = Pattern.compile(Regex.APPECO_APP).matcher(s);
		while(m.find()) {
			String mt = m.group();
			if(!mt.matches("App ::= <(\\S+),(\\S+),(\\S+),([\\S\\s]+),([\\S\\s]+)>")) {
				try {
					throw new AppLackOfComponentException("App Lack Of Component, should be 4 parts, but was: "+m.group());
				} catch(AppLackOfComponentException e) {
					e.printStackTrace();
					return false;
				}
			}
			Matcher mb = Pattern.compile("App ::= <(\\S+),("+Regex.REGEX_LABEL+"),"+"(v?("+Regex.REGEX_LABEL+"[._-])*"+Regex.REGEX_LABEL+"),"
					+ "(\""+Regex.REGEX_SENTENCE+"\"),(\""+Regex.REGEX_SENTENCE+"\")>").matcher(mt);
			if(mb.find()) {
				if(!mb.group(1).matches(Regex.REGEX_LABEL)) {
					try {
						throw new IllegalAppNameException("App Name not a label type , but was : "+m.group(1));
					}catch(IllegalAppNameException e) {
						e.printStackTrace();
						return false;
					}
				}
				else if(mb.group().matches(Regex.APPECO_APP)) {
					if(apps.contains(mb.group(1))) {
						try {
							throw new IllegalArgumentException("App Name repeat , but was : "+mb.group(1));
						}catch(IllegalArgumentException e) {
							e.printStackTrace();
							return false;
						}
					}
					apps.add(mb.group(1));
				}
			}else {
				try {
					throw new IllegalArgumentException("Illegal format of App");
				} catch(IllegalArgumentException e) {
					e.printStackTrace();
					return false;
				}
			}
			//TODO
		}
		//UsageLog
		m = Pattern.compile("UsageLog ::= <\\S+>").matcher(s);
		while(m.find()) {
			String ul = m.group();
			if(ul.matches(Regex.APPECO_USAGELOG)) {
				Matcher mb = Pattern.compile(Regex.APPECO_USAGELOG).matcher(ul);
				mb.find();
				if(!apps.contains(mb.group(8))) {
					try {
						throw new UndefinedAppException("Undefined App in usagelog");
					} catch(UndefinedAppException e) {
						e.printStackTrace();
						return false;
					}
				}
			} else {
				try {
					throw new IllegalUsageLogFormatException("Illegal format of usagelog");
				} catch(IllegalUsageLogFormatException e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		//InstallLog
		m = Pattern.compile("InstallLog ::= <\\S+>").matcher(s);
		while(m.find()) {
			String il = m.group();
			if(il.matches("InstallLog ::= <(\\S+),(\\S+),([\\w]+)>")) {
				if(!il.matches(Regex.APPECO_INSTALL)) {
					try {
						throw new InstallLogIllegalTimeFormatException("Install Log Illegal Time Format, should be yyyy-mm-dd,hh:mm:ss,but was "
								+il);
					} catch(InstallLogIllegalTimeFormatException e) {
						e.printStackTrace();
						return false;
					}
				}
				else {
					Matcher mb = Pattern.compile(Regex.APPECO_INSTALL).matcher(il);
					mb.find();
					if(!apps.contains(mb.group(8))) {
						try {
							throw new UndefinedAppException("Undefined App in installlog");
						} catch(UndefinedAppException e) {
							e.printStackTrace();
							return false;
						}
					}
				}	
			} else {
				try {
					throw new IllegalArgumentException("Illegal format of intalllog");
				} catch(IllegalArgumentException e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		//UninstallLog
		m = Pattern.compile("UninstallLog ::= <\\S+>").matcher(s);
		while(m.find()) {
			String il = m.group();
			if(il.matches("UninstallLog ::= <(\\S+),(\\S+),([\\w]+)>")) {
				if(!il.matches(Regex.APPECO_UNINSTALL)) {
					try {
						throw new UninstallLogIllegalTimeFormatException("Uninstall Log Illegal Time Format, should be yyyy-mm-dd,hh:mm:ss,but was "
								+il);
					} catch(UninstallLogIllegalTimeFormatException e) {
						e.printStackTrace();
						return false;
					}
				}
				else {
					Matcher mb = Pattern.compile(Regex.APPECO_UNINSTALL).matcher(il);
					mb.find();
					if(!apps.contains(mb.group(8))) {
						try {
							throw new UndefinedAppException("Undefined App in installlog");
						} catch(UndefinedAppException e) {
							e.printStackTrace();
							return false;
						}
					}
				}	
			} else {
				try {
					throw new IllegalArgumentException("Illegal format of unintalllog");
				} catch(IllegalArgumentException e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		//IncorrectTime
		//TODO
		//Relation
		m = Pattern.compile("Relation ::= <\\S+>").matcher(s);
		while(m.find()) {
			String rl = m.group();
			if(rl.matches("Relation ::= <(\\w+),(\\w+)>")) {
				if(!rl.matches(Regex.APPECO_RELATION)) {
					try {
						throw new IllegalArgumentException("Illegal format of relation");
					} catch(IllegalArgumentException e) {
						e.printStackTrace();
						return false;
					}
				}
				else {
					Matcher mb = Pattern.compile(Regex.APPECO_RELATION).matcher(rl);
					mb.find();
					if(!apps.contains(mb.group(1)) || !apps.contains(mb.group(2))) {
						try {
							throw new UndefinedAppException("Undefined App in relation");
						} catch(UndefinedAppException e) {
							e.printStackTrace();
							return false;
						}
					}
				}	
			} else {
				try {
					throw new IllegalArgumentException("Illegal format of raltion");
				} catch(IllegalArgumentException e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * 获得所有App集合
	 * @param fileText 待解析文本，文本通过合法性检查
	 * @return 一个App对象的集合
	 */
	public Set<App> getApps(String fileText) {
		if(fileText == null) throw new IllegalArgumentException("fileText is null pointer");
		
		Set<App> apps = new HashSet<App>();
		Pattern pattern = Pattern.compile(Regex.APPECO_APP);
		Matcher ma = pattern.matcher(fileText);
		AppFactory af = new AppFactory();
		while(ma.find()) {
			apps.add((App)af.create(new AppParamPackage(ma.group(1),ma.group(2),ma.group(3),ma.group(5),ma.group(6))));
		}
		return apps;
	}
	
	/**
	 * 获得所有App之间的无向关系
	 * 
	 * @param fileText 待解析文本，文本通过合法性检查
	 * @return App之间的合作关系，关系为无向关系。
	 */
	public List<Relation<String>> getRelation(String fileText) {
		if(fileText == null) throw new IllegalArgumentException("fileText is null pointer");
		
		List<Relation<String>> relation = new ArrayList<Relation<String>>();
		Pattern pattern = Pattern.compile(Regex.APPECO_RELATION);
		Matcher ma = pattern.matcher(fileText);
		while(ma.find()) {
			Relation<String> r = new Relation<String>(ma.group(1), ma.group(2));
			relation.add(r);
		}
		return relation;
	}
	
	/**
	 * 获得划分时间范围
	 * @param fileText 待解析文本，文本通过合法性检查
	 * @return
	 */
	public Period getPeriod(String fileText) {
		if(fileText == null) throw new IllegalArgumentException("fileText is null pointer");
		
		Pattern pattern = Pattern.compile(Regex.APPECO_PERIOD);
		Matcher ma = pattern.matcher(fileText);
		Period period = null;
		if(ma.find()) {
			switch (ma.group(1)) {
			case "Day": {
				period = Period.Day;
				break;
			}
			case "Hour": {
				period = Period.Hour;
				break;
			}
			case "Week" : {
				period = Period.Week;
				break;
			}
			case "Month" : {
				period = Period.Month;
				break;
			}
			default: {
				throw new IllegalArgumentException("No match period");
			}
			}
		}
		return period;
	}
	
	/**
	 * 获得用户名称
	 * @param fileText 待解析文本，文本通过合法性检查
	 * @return 
	 */
	public String getUser(String fileText) {
		if(fileText == null) throw new IllegalArgumentException("fileText is null pointer");
		
		Pattern pattern = Pattern.compile(Regex.APPECO_USER);
		Matcher ma = pattern.matcher(fileText);
		if(ma.find()) {
			return ma.group(1);
		}
		return null;
	}
	
	/**
	 * 获得每一个App安装在用户手机上的时间段AppUsageTime
	 * 
	 * @param fileText 待解析文本，文本通过合法性检查
	 * @return 
	 */
	public Map<String, AppInstallTime> getAppInstallTime(String fileText) {
		if(fileText == null) throw new IllegalArgumentException("fileText is null pointer");
		
		String regex = Regex.APPECO_INSTALL;
		Pattern pattern = Pattern.compile(regex);
		Set<String> apps = new HashSet<String>();  //Get the apps name
		Matcher ma = pattern.matcher(fileText);
		while(ma.find()) {
			apps.add(ma.group(8));
		}
		
		Map<String, AppInstallTime> amap = new HashMap<String, AppInstallTime>();
		
		for(String app : apps) {
			String installregex = "InstallLog ::= <(\\d{4}-(0[1-9]|1[0-2])-(0\\d|1\\d|2\\d|3[0-1]))"
					+ ",((0\\d|1\\d|2[0-3]):([0-5]\\d):([0-5]\\d)),"+app+">";
			String uninstallregex = "UninstallLog ::= <(\\d{4}-(0[1-9]|1[0-2])-(0\\d|1\\d|2\\d|3[0-1]))"
					+ ",((0\\d|1\\d|2[0-3]):([0-5]\\d):([0-5]\\d)),"+app+">";
			
			Matcher inm = Pattern.compile(installregex).matcher(fileText);
			Matcher unm = Pattern.compile(uninstallregex).matcher(fileText);
			AppInstallTime atu = new AppInstallTime();
			while(inm.find()) {
				if(unm.find()) {
					Instant st = Instant.parse(inm.group(1)+"T"+inm.group(4)+"Z");
					Instant et = Instant.parse(unm.group(1)+"T"+unm.group(4)+"Z");
					atu.add(new Timespan(st,et));
				}
				else {
					Instant st = Instant.parse(inm.group(1)+"T"+inm.group(4)+"Z");
					Instant et = Instant.MAX;
					atu.add(new Timespan(st, et));
					break;
				}
			}
			amap.put(app, atu);
		}
		return amap;
	}
	
	/**
	 * 获得使用日志列表
	 * 
	 * @param fileText 待解析文本，文本通过合法性检查
	 * @return 一组使用日志
	 */
	public List<UsageLog> getUsageLog(String fileText) {
		if(fileText == null) throw new IllegalArgumentException("fileText is null pointer");
		
		List<UsageLog> ulist = new ArrayList<UsageLog>();
		Matcher m = Pattern.compile(Regex.APPECO_USAGELOG).matcher(fileText);
		while(m.find()) {
			UsageLog ul = new UsageLog(m.group(8), Instant.parse(m.group(1)+"T"+m.group(4)+"Z"), Integer.valueOf(m.group(9)));
			ulist.add(ul);
		}
		return ulist;
	}
	
	/**
	 * 从描述字符串中匹配出App的名称
	 * @param s 描述App的字符串<p>{@code App ::= <App 名称,公司,版本,"功能描述","业务领域">} 
	 * @return  App的名称
	 */
	public String getName(String s) {
		assert s != null:"Input string is null";
		String regex = "<([A-Za-z0-9]+),";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(s);
		if(m.find()) return m.group(1);
		return "";
	}
	
	/**
	 * 从描述字符串中匹配出App所属公司
	 * @param s 描述App字符串<p>{@code App ::= <App 名称,公司,版本,"功能描述","业务领域">} 
	 * @return App所属公司
	 */
	public String  getCompany(String s) {
		assert s != null : "Input string is null";
		String regex = ",([A-Za-z0-9]+),";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(s);
		if(m.find()) return m.group(1);
		return "";
	}
	
	/**
	 * 从描述字符串中匹配出App的版本
	 * @param s 描述App的字符串<p>{@code App ::= <App 名称,公司,版本,"功能描述","业务领域">} 
	 * @return App的版本
	 */
	public String getVersion(String s) {
		assert s != null : "Input string is null";
		String regex = "<\\S+,\\S+,(v?([A-Za-z0-9]+[._-])*[A-Za-z0-9]+),";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(s);
		if(m.find()) return m.group(1);
		return "";
	}
	
	/**
	 * 从描述字符串中匹配出App的描述
	 * @param s 描述App的字符串<p>{@code App ::= <App 名称,公司,版本,"功能描述","业务领域">} 
	 * @return App的描述，带有双引号
	 */
	public String getDescription(String s) {
		assert s != null : "Input string is null";
		String regex = ",(\"[A-Za-z0-9\\s]+\"),";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(s);
		if(m.find()) return m.group(1);
		return "";
	}
	
	/**
	 * 从描述字符串中匹配出App所属领域
	 * @param s 描述App的字符串<p>{@code App ::= <App 名称,公司,版本,"功能描述","业务领域">} 
	 * @return App的所属领域，带有双引号
	 */
	public String getArea(String s) {
		assert s != null : "Input string is null";
		String regex = ",(\"[A-Za-z0-9\\s]+\")>";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(s);
		if(m.find()) return m.group(1);
		return "";
	}
}


