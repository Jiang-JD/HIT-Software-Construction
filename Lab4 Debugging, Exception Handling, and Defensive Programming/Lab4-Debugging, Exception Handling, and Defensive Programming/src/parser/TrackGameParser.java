package parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import centralObject.CentralPoint;
import constant.Regex;
import exception.ElementLabelDuplicationException;
import exception.IllegalElementFormatException;
import exception.IncorrectElementLabelOrderException;
import exception.LackOfComponentException;
import exception.NoSuchElementException;
import exception.TrackNumberOutOfRangeException;
import physicalObject.Athlete;
import physicalObject.AthleteFactory;
import physicalObject.AthleteParamPackage;

/**
 * 文本解析器，解析径赛结构的图输入
 *
 */
public class TrackGameParser extends Parser<CentralPoint, Athlete> {
	private final Logger logger = Logger.getLogger(TrackGameParser.class);
	
	/**
	 * 解析图输入，检查文本合法性。解析器会将文本解析为运动员，轨道数量，径赛种类
	 * 三个输入
	 * 
	 * <p>要求：<br>比赛项目格式为 {@code Game ::= 100|200|400} <br>
	 * 跑道数量格式为 {@code NumOfTracks ::= 跑道数目}，4 到 10 之间的整数 <br>
	 * 运动员格式为 {@code Athlete ::= <姓名,号码,国籍,年龄,本年度最好成绩>}  代表一个运动员，其中姓名是 word
	 * ，号码和年龄是正整数， 国籍是三位大写字母，成绩是最多两位整数和必须两位小数 构成（例如 9.10、10.05）
	 * 
	 * @return 如果所有文本格式合法且包含了Game，NumOfTreacks，Athlete三个内容，返回{@code true}，否则
	 * 若存在某一类内容格式不合法，返回false，控制台输出错误信息
	 */
	@Override
	public  boolean parserFile(String filePath) throws IllegalElementFormatException,NoSuchElementException
													,TrackNumberOutOfRangeException,LackOfComponentException
													,IncorrectElementLabelOrderException,ElementLabelDuplicationException {
		String s = "";
		try {
			File file = new File(filePath);
			FileInputStream fis;
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
			sb.append(s.trim());
			}
			s = sb.toString();
			fis.close();
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
		//Check RaceType
		Matcher m = Pattern.compile("[Gg]a[a-z]+\\s*::=\\s*\\d+").matcher(s);
		if(m.find()) {
			if(!m.group().matches(Regex.TRACKGAME_RACETYPE)) {
				logger.error("["+IllegalElementFormatException.class.getName()+"] [Reselect] Illegal format racetype, should be 100|200|400, but was "+m+"");
				throw new IllegalElementFormatException("Illegal format racetype, should be 100|200|400, but was "+m, m.group());
			}
		}
		else {
			logger.error("["+NoSuchElementException.class.getName()+"] [Reselect] No Game in the file");
				throw new NoSuchElementException("No Game in the file");
		}
		//Check TrackNumber 
		m = Pattern.compile("[Nn]um[oO]f[tT]racks?\\s*::=\\s*\\d+").matcher(s);
		if(m.find()) {
			if(!m.group().matches(Regex.TRACKGAME_NUMOFTRACKS)) {
				logger.error("["+TrackNumberOutOfRangeException.class.getName()+"] [Reselect] Illegal format trake number , should in 4-10, but was "+ m.group()+"");
				throw new TrackNumberOutOfRangeException("Illegal format trake number , should in 4-10, but was "+ m.group(), m.group());
			}
		}
		else {
			logger.error("["+NoSuchElementException.class.getName()+"] [Reselect] No NumOfTracks in the file");
			throw new NoSuchElementException("No NumOfTracks in the file");
		}
		//Check Athlete Format
		Set<String> athletes = new HashSet<String>();
		m = Pattern.compile("[Aa]th[a-z]+\\s*::=\\s*<\\s*[^>]+\\s*>").matcher(s);
		boolean f = false;
		while(m.find()) {
			f = true;
			if(!m.group().matches("Athlete\\s*::=\\s*<[\\S\\s]+")) {
				logger.error("["+IllegalElementFormatException.class.getName()+"] [Reselect] Athlete Tag spell wrong, should be Athlete ::= , but was: "+m.group()+"");
				throw new IllegalElementFormatException("Athlete Tag spell wrong, should be Athlete ::= , but was: "+m.group(), m.group());
			}
			if(!m.group().matches("Athlete\\s*::=\\s*<\\s*(\\S+)\\s*,\\s*(\\S+)\\s*,\\s*(\\S+)\\s*,\\s*(\\S+)\\s*,\\s*(\\S+)\\s*>")) {
				logger.error("["+LackOfComponentException.class.getName()+"] [Reselect] Athlete Tag lack components, should be 5 components, but was: "+m.group()+"");
				throw new LackOfComponentException("Athlete Tag lack components, should be 5 components, but was: "+m.group(), m.group());
			}
			String mt = m.group();
			Matcher mb = Pattern.compile("Athlete\\s*::=\\s*<\\s*(\\S+)\\s*,\\s*(\\S+)\\s*,\\s*(\\S+)\\s*,\\s*(\\S+)\\s*,\\s*(\\S+)\\s*>").matcher(mt);
			mb.find();
			if(!mb.group(1).matches(Regex.REGEX_WORD)) {
				logger.error("["+IllegalElementFormatException.class.getName()+"] [Reselect] Incorrect Name Format, should be word format, but was: "+mb.group(1)+"");
				throw new IllegalElementFormatException("Incorrect Name Format, should be word format, but was: "+mb.group(1)+" tag: "+mb.group(), mb.group());
			}
			if(!checkNumber(mb.group(2))) {
				logger.error("["+IllegalElementFormatException.class.getName()+"] [Reselect] Incorrect Number Format, should be number format, but was: "+mb.group(2)+"");
				throw new IllegalElementFormatException("Incorrect Number Format, should be number format, but was: "+mb.group(2)+" tag: "+mb.group(), mb.group());
			}
			if(Double.parseDouble(mb.group(2)) != (int)Double.parseDouble(mb.group(2))|| (((int)Double.parseDouble(mb.group(2)) < 0))) {
				logger.error("["+IllegalElementFormatException.class.getName()+"] [Reselect] Incorrect Number Format, should be positive or 0 Integer format, but was: "+mb.group(2)+"");
				throw new IllegalElementFormatException("Incorrect Number Format, should be positive or 0 Integer format, but was: "+mb.group(2)+" tag: "+mb.group(), mb.group());
			}
			if(!checkNumber(mb.group(4))) {
				logger.error("["+IllegalElementFormatException.class.getName()+"] [Reselect] Incorrect Age Format, should be number format, but was: "+mb.group(4)+"");
				throw new IllegalElementFormatException("Incorrect Age Format, should be number format, but was: "+mb.group(4)+" tag: "+mb.group(), mb.group());
			}
			if(!mb.group(3).matches("[A-Z]{3}")) {
				logger.error("["+IllegalElementFormatException.class.getName()+"] [Reselect] Incorrect Nation Format, should be 3 Captive letter, but was: "+mb.group(3)+"");
					throw new IllegalElementFormatException("Incorrect Nation Format, should be 3 Captive letter, but was: "+mb.group(3), mb.group());
			}
			if(mt.matches("Athlete\\s*::=\\s*<\\s*(\\d+)\\s*,\\s*([A-Za-z]+)\\s*,\\s*([A-Z]{3})\\s*,\\s*(\\d+)\\s*,\\s*((\\d{2}|\\d{1})\\.\\d{2})\\s*>")) {
				logger.error("["+IncorrectElementLabelOrderException.class.getName()+"] [Reselect] Athlete Name: "+mb.group(2)+" and Number: "+mb.group(1)+" are reversed: "+mt+"");
					throw new IncorrectElementLabelOrderException("Athlete Name: "+mb.group(2)+" and Number: "+mb.group(1)+" are reversed: "+mt, mt);
			}
			if(!mb.group(5).matches(Regex.TRACKGAME_PERSONALBEST)) {
				logger.error("["+IllegalElementFormatException.class.getName()+"] [Reselect] Incorrect Score Format, should be at most 2 integer and must be 2 decimal, but was: "+mb.group(5)+"");
				throw new IllegalElementFormatException("Incorrect Score Format, should be at most 2 integer and must be 2 decimal, but was: "+mb.group(5)+" tag: "+mb.group(), mb.group());
			}
			if(!mt.matches(Regex.TRACKGAME_ATHLETE)) {
				logger.error("["+IllegalElementFormatException.class.getName()+"] [Reselect] Illegal format in athlete tag: "+m.group()+"");
					throw new IllegalElementFormatException("Illegal format in athlete tag: "+m.group(), mt);
			}
			mb = Pattern.compile(Regex.TRACKGAME_ATHLETE).matcher(mt);
			mb.find();
			if(athletes.contains(mb.group(1))) {
				logger.error("["+ElementLabelDuplicationException.class.getName()+"] [Reselect] Athlete tags is duplicated: "+m.group()+"");
					throw new ElementLabelDuplicationException("Athlete tags is duplicated: "+m.group(), mt);
			}
			athletes.add(mb.group(1));
		}
		if(!f){
			logger.error("["+NoSuchElementException.class.getName()+"] [Reselect] No Athlete tag in the file");
				throw new NoSuchElementException("No Athlete tag in the file");
		}
		logger.info("Pass the file check: "+filePath);
		return true;
	}
	
	/**
	 * 解析文本，获得比赛项目
	 * <p>要求：<br>比赛项目格式为 {@code Game ::= 100|200|400} <br>
	 * 
	 * @param file 待解析文本
	 * @return 比赛项目，为100|200|400之一，若未找到，返回""
	 */
	public String raceType(String fileText) {
		String regex = Regex.TRACKGAME_RACETYPE;
		Pattern pattern = Pattern.compile(regex);
		Matcher m = pattern.matcher(fileText);
		//Set system name
		if(m.find()) {
			logger.info("Parser the game: "+m.group(1));
			return m.group(1); 
		}
		return "";
	}
	
	/**
	 * 解析文本，获得径赛操场的轨道数目
	 * <p>跑道数量格式为 {@code NumOfTracks ::= 跑道数目}，4 到 10 之间的整数 <br>
	 * 
	 * @param file 带解析文件
	 * @return 操场轨道数目，如果没找到，返回-1
	 */
	public int trackNum(String fileText) {
		String regex = Regex.TRACKGAME_NUMOFTRACKS;
		Pattern pattern = Pattern.compile(regex);
		Matcher m = pattern.matcher(fileText);
		//Set system name
		if(m.find()) {
			logger.info("Parser track number: "+m.group(1));
			return Integer.parseInt(m.group(1)); 
		}
		return -1;
	}
	
	/**
	 * 解析文本输入，转换出所有运动员对象，返回运动员对象的集合
	 * <p>运动员格式为 {@code Athlete ::= <姓名,号码,国籍,年龄,本年度最好成绩>}  代表一个运动员，其中姓名是 word
	 * ，号码和年龄是正整数， 国籍是三位大写字母，成绩是最多两位整数和必须两位小数 构成（例如 9.10、10.05）
	 * 
	 * @param file 待解析文件
	 * @return 运动员对象集合，若没有运动员格式字符串，返回空集
	 */
	public List<Athlete> athletes(String fileText) {
		List<Athlete> list = new ArrayList<Athlete>();
		if(fileText == null) throw new IllegalArgumentException("fileText is null pointer");
		
		Pattern pattern = Pattern.compile(Regex.TRACKGAME_ATHLETE);
		Matcher ma = pattern.matcher(fileText);
		AthleteFactory af = new AthleteFactory();
		while(ma.find()) {
			list.add((Athlete)af.create(new AthleteParamPackage(ma.group(1),
					Integer.parseInt(ma.group(2)), ma.group(3), 
					Integer.parseInt(ma.group(4)), 
					Double.parseDouble(ma.group(5)))));
			
		}
		logger.info("Parser all the athletes objects, number is "+list.size());
		return list;
	}
	
	
	/**
	 * 获得运动员的姓名
	 * @param info 描述运动员信息字符串
	 * @return 运动员姓名，如果未找到若未找到或格式错误则抛出异常，返回"""
	 */
	public String getName(String info) {
		String regex = "Athlete ::= <([A-Za-z]+),";
		Pattern pattern = Pattern.compile(regex);
		Matcher m = pattern.matcher(info);
		if(m.find()) {
			return m.group(1); 
		}
		else {
			try {
				throw new Exception("Athlete的姓名格式错误");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "";
	}
	
	/**
	 * 获得运动员的号码
	 * @param info 描述运动员信息字符串
	 * @return 运动员年龄，若没找到若未找到或格式错误则抛出异常，返回-1
	 */
	public int getNumber(String info) {
		String regex = ",(\\d+),";
		Pattern pattern = Pattern.compile(regex);
		Matcher m = pattern.matcher(info);
		if(m.find()) {
			return Integer.parseInt(m.group(1)); 
		}
		else {
			try {
				throw new Exception("Athlete的号码格式错误");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return -1;
	}
	
	/**
	 * 获得运动员的国籍
	 * @param info 描述运动员信息字符串
	 * @return 运动员的国籍，若未找到若未找到或格式错误则抛出异常，返回""
	 */
	public String getNation(String info) {
		String regex = "[A-Z]{3}";
		Pattern pattern = Pattern.compile(regex);
		Matcher m = pattern.matcher(info);
		if(m.find()) {
			return m.group(0); 
		}
		else {
			try {
				throw new Exception("Athlete的国籍格式错误");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "";
	}
	
	/**
	 * 获得运动员的年龄
	 * @param info 描述运动员信息字符串
	 * @return 运动员的年龄，若未找到若未找到或格式错误则抛出异常，返回-1
	 */
	public int getAge(String info) {
		String regex = "<\\S+,\\S+,\\S+,(\\d+),";
		Pattern pattern = Pattern.compile(regex);
		Matcher m = pattern.matcher(info);
		if(m.find()) {
			return Integer.parseInt(m.group(1)); 
		}
		else {
			try {
				throw new Exception("Athlete的年龄格式错误");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return -1;
	}
	
	/**
	 * 获得运动员年度最好成绩
	 * @param info 描述运动员信息字符串
	 * @return 运动员年度最好成绩，若未找到或格式错误，抛出错误，返回""
	 */
	public double getPb(String info) {
		String regex = "(\\d{2}|\\d{1})[.]\\d{2}";
		Pattern pattern = Pattern.compile(regex);
		Matcher m = pattern.matcher(info);
		if(m.find()) {
			return Double.valueOf(m.group()); 
		}
		else {
			try {
				throw new Exception("Athlete的年度最好成绩格式错误");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return -1;
	}

}
