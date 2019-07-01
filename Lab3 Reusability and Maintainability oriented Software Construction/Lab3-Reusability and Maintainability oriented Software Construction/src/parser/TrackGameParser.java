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

import centralObject.CentralPoint;
import constant.Regex;
import customException.trackGameException.AthleteRepeatInputException;
import customException.trackGameException.IllegalFormatRaceTypeException;
import customException.trackGameException.IncorrectNationFormatException;
import customException.trackGameException.LackOfComponentException;
import customException.trackGameException.NameNumberReversedException;
import customException.trackGameException.NoAthleteException;
import customException.trackGameException.NoRaceTypeException;
import customException.trackGameException.NoTrackNumberException;
import customException.trackGameException.TrackNumberOutOfRangeExeption;
import physicalObject.Athlete;
import physicalObject.AthleteFactory;
import physicalObject.AthleteParamPackage;

/**
 * 文本解析器，解析径赛结构的图输入
 *
 */
public class TrackGameParser extends Parser<CentralPoint, Athlete> {
	
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
	public  boolean parserFile(String fileName) {
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
		Matcher m = Pattern.compile("Game ::= \\d+").matcher(s);
		if(m.find()) {
			if(!m.group().matches(Regex.TRACKGAME_RACETYPE)) {
				try {
					throw new IllegalFormatRaceTypeException("Illegal format racetype, should be 100|200|400");
				} catch(IllegalFormatRaceTypeException e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		else {
			try {
				throw new NoRaceTypeException("File has No RaceType");
			} catch (NoRaceTypeException e) {
				e.printStackTrace();
				return false;
			}
		}
		//Check TrackNumber 
		m = Pattern.compile("NumOfTracks ::= \\d+").matcher(s);
		if(m.find()) {
			if(!m.group().matches(Regex.TRACKGAME_NUMOFTRACKS)) {
				try {
					throw new TrackNumberOutOfRangeExeption("Illegal format trake number , should in 4 - 10, but was "+ m.group());
				} catch(TrackNumberOutOfRangeExeption e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		else {
			try {
				throw new NoTrackNumberException();
			}catch(NoTrackNumberException e) {
				e.printStackTrace();
				return false;
			}
		}
		//Check Athlete Format
		Set<String> athletes = new HashSet<String>();
		m = Pattern.compile("Athlete ::= <\\S+>").matcher(s);
		boolean f = false;
		while(m.find()) {
			f = true;
			if(!m.group().matches("Athlete ::= <(\\S+),(\\S+),(\\S+),(\\S+),(\\S+)>")) {
				try {
					throw new LackOfComponentException("Lack Of Component, should be 5:"+m.group());
				} catch(LackOfComponentException e) {
					e.printStackTrace();
					return false;
				}
			}
			String mt = m.group();
			Matcher mb = Pattern.compile("Athlete ::= <(\\S+),(\\S+),(\\S+),(\\S+),(\\S+)>").matcher(mt);
			mb.find();
			if(!mb.group(3).matches("[A-Z]{3}")) {
				try {
					throw new IncorrectNationFormatException("Incorrect Nation Format, should be 3 Captive letter :"+m.group());
				} catch(IncorrectNationFormatException e) {
					e.printStackTrace();
					return false;
				}
			}
			if(mt.matches("Athlete ::= <(\\d+),([A-Za-z]+),([A-Z]{3}),(\\d+),((\\d{2}|\\d{1})\\.\\d{2})>")) {
				try {
					throw new NameNumberReversedException("Name Number Reversed:"+m.group());
					
				} catch(NameNumberReversedException e) {
					e.printStackTrace();
					return false;
				}
			}
			if(!mt.matches(Regex.TRACKGAME_ATHLETE)) {
				try {
					throw new IllegalArgumentException("Illegal format athlete :"+m.group());
				} catch(IllegalArgumentException e) {
					e.printStackTrace();
					return false;
				}
			}
			mb = Pattern.compile(Regex.TRACKGAME_ATHLETE).matcher(mt);
			mb.find();
			if(athletes.contains(mb.group(1))) {
				try {
					throw new AthleteRepeatInputException("Athlete Repeat Input :"+m.group());
				} catch(AthleteRepeatInputException e) {
					e.printStackTrace();
					return false;
				}
			}
			athletes.add(mb.group(1));
		}
		if(!f){
			try {
				throw new NoAthleteException();
			}catch(NoAthleteException e) {
				e.printStackTrace();
				return false;
			}
		}
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
			return Integer.valueOf(m.group(1)); 
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
					Integer.valueOf(ma.group(2)), ma.group(3), 
					Integer.valueOf(ma.group(4)), 
					Double.valueOf(ma.group(5)))));
			
		}
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
			return Integer.valueOf(m.group(1)); 
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
			return Integer.valueOf(m.group(1)); 
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
