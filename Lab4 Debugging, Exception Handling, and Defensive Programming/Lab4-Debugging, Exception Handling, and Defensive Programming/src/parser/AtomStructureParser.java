package parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import centralObject.Nucleus;
import circularOrbit.AtomStructure;
import circularOrbit.CircularOrbitFactory;
import constant.Regex;
import exception.IncorrectElementLabelOrderException;
import exception.LackOfComponentException;
import exception.ElementLabelDuplicationException;
import exception.IllegalElementFormatException;
import exception.IncorrectElementDependencyException;
import exception.NoSuchElementException;
import exception.TrackNumberOutOfRangeException;
import physicalObject.Electron;

/**
 * 文本解析器，解析原子结构的图输入
 *
 */
public class AtomStructureParser extends Parser<Nucleus,Electron> {
	private final Logger logger = Logger.getLogger(AtomStructureParser.class);
	
	/**
	 * 解析AtomStructure文本输入，检查元素格式是否合法，元素标签是否存在，依赖关系是否正确等
	 * 
	 * @param filePath 文件路径
	 * @return 如果文件合法返回true，否则返回false
	 * @throws NoSuchElementException 缺少元素名称，轨道数量，电子分布时抛出
	 * @throws IllegalElementFormatException 标签格式不正确抛出
	 * @throws TrackNumberOutOfRangeException 轨道数目不符合要求抛出
	 * @throws IncorrectElementLabelOrderException 元素分布轨道编号不合法抛出
	 * @throws IncorrectElementDependencyException 电子分布与轨道数量不匹配抛出
	 */
	@Override
	public boolean parserFile(String filePath) throws NoSuchElementException, IllegalElementFormatException
														, TrackNumberOutOfRangeException, IncorrectElementLabelOrderException
														, IncorrectElementDependencyException{
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
		//Check Element Name
		Matcher m = Pattern.compile("[Ee]leme?nt[Nn]ame\\s*::=\\s*\\S+").matcher(s);  //get element name title
		if(m.find()) {
			if(!m.group().matches(Regex.ATOM_ELEMENTNAME)) {  //Element name format is wrong
				logger.error("["+IllegalElementFormatException.class.getName()+"] [Reselect] Illegal format ElementName,should be 1 uppercase or 1 uppercase by 1 lowcase, but was " + m.group());
					throw new IllegalElementFormatException("Illegal format ElementName, "
							+ "should be 1 uppercase or 1 uppercase by 1 lowcase, but was " + m.group(), m.group());
				}
		}
		else {
			logger.error("["+NoSuchElementException.class.getName()+"] [Reselect] No ElementName in the file");
			throw new NoSuchElementException("No ElementName in the file");
		}
		//Check TrackNumber 
		m = Pattern.compile("[Nn]umber[Oo]f[Tt]racks?\\s*::=\\s*\\S+").matcher(s); //get the number of tracks tag
		int num = 0;
		if(m.find()) {
			if(!m.group().matches(Regex.ATOM_NUMBEROFTRACKS)) {
				logger.error("["+TrackNumberOutOfRangeException.class.getName()+"] [Reselect] Track number out of range , should in 4 - 10, but was "+m.group()+"");
				throw new TrackNumberOutOfRangeException("Track number out of range , should in 4 - 10, but was "+m.group(), m.group());
			}
			else {
				Matcher ma = Pattern.compile(Regex.ATOM_NUMBEROFTRACKS).matcher(s);
				ma.find();
				num = Integer.parseInt(ma.group(1)); // save track number
			}
		}
		else {
			logger.error("["+NoSuchElementException.class.getName()+"] [Reselect] No NumberOfTracks in the file");
			throw new NoSuchElementException("No NumberOfTracks in the file");
		}
		//Check Electron Format
		m = Pattern.compile("[Nn]umber[Oo]f[Ee]lectrons?\\s*::=\\s*\\S+").matcher(s);  //get electron title
		boolean f = false;
		while(m.find()) {
			f = true;
			String mt = m.group();
			if(!mt.matches(Regex.ATOM_NUMBEROFELECTRON)) {
				logger.error("["+IllegalElementFormatException.class.getName()+"] [Reselect] Illegal format Electrons should be NumberOfElectron ::= 1/a;2/b;3/c... but was : "+m.group()+"");
				throw new IllegalElementFormatException("Illegal format Electrons should be NumberOfElectron ::= 1/a;2/b;3/c... but was : "+m.group(), m.group());
			}
			Matcher mb = Pattern.compile("NumberOfElectron\\s*::=\\s*\\S+").matcher(mt);
			if(mb.find()) {
				String regex = "(\\d+)/(\\d+)";
				mb = Pattern.compile(regex).matcher(mt);
				int i = 1;
				while(mb.find()) { 
					if(!checkNumber(mb.group(2))) {
						logger.error("["+IllegalElementFormatException.class.getName()+"] [Reselect] Illegal format Electrons number, should be number format, but was : "+mb.group(2)+"");
						throw new IllegalElementFormatException("Illegal format Electrons number, should be number format, but was : "+mb.group(2), mb.group());
					}
					if((Double.parseDouble(mb.group(2)) != (int)Double.parseDouble(mb.group(2))) || (((int)Double.parseDouble(mb.group(2)) <= 0))) {
						logger.error("["+IllegalElementFormatException.class.getName()+"] [Reselect] Illegal format Electrons number, should be positive Integer format, but was : "+mb.group(2)+"");
						throw new IllegalElementFormatException("Illegal format Electrons number, should be positive Integer format, but was : "+mb.group(2), mb.group());
					}
					if(i != Integer.parseInt(mb.group(1))) {
						logger.error("["+IncorrectElementLabelOrderException.class.getName()+"] [Reselect] Wrong Sequence Track Number, should be 1,2...n, but was "+ mt+"");
						throw new IncorrectElementLabelOrderException("Wrong Sequence Track Number, should be 1,2...n, but was "+ mt, mb.group());
					}
					i++;
				}
				if(i-1 != num) {
					logger.error("["+IncorrectElementLabelOrderException.class.getName()+"] [Reselect] Number of tracks not equal to number of electron should be "+num+", but was "+ (i-1)+"");
					throw new IncorrectElementDependencyException("Number of tracks not equal to number of electron should be "+num+", but was "+ (i-1),mt);
				}
			}
		}
		if(!f){
			logger.error("["+NoSuchElementException.class.getName()+"] [Reselect] No NumberOfElectron in the file");
			throw new NoSuchElementException("No NumberOfElectron in the file");
		}
		logger.info("Pass the file check: "+filePath);
		return true;
	}
	
	/**
	 * 解析图输入，将文本翻译为原子系统结构。解析器会将文本解析为元素名称，轨道数量，电子数目
	 * 三个输入，分别将电子对象添加到轨道中，设置元素名称，，将元素名称设置为中心原子核名称，将
	 * 轨道从1开始编号直到编号为 轨道的数量为止（即轨道的编号参数从1开始递增）。
	 * 
	 * @param fileText 待解析文本字符串形式，非空且格式正确
	 * @param co 待构造轨道系统，非空
	 * @return 如果文本格式正确，构造成功返回{@code true}，否则返回{@code false}
	 * @throws NoSuchElementException 缺少元素名称，轨道数量，电子分布时抛出
	 * @throws IllegalElementFormatException 标签格式不正确抛出
	 * @throws TrackNumberOutOfRangeException 轨道数目不符合要求抛出
	 * @throws IncorrectElementLabelOrderException 元素分布轨道编号不合法抛出
	 * @throws IncorrectElementDependencyException 电子分布与轨道数量不匹配抛出
	 * @throws ElementLabelDuplicationException 
	 * @throws LackOfComponentException 
	 */
	public AtomStructure initial(String filePath) throws NoSuchElementException, IllegalElementFormatException
														, TrackNumberOutOfRangeException, IncorrectElementLabelOrderException
														, IncorrectElementDependencyException, LackOfComponentException, ElementLabelDuplicationException {
			parserFile(filePath);
			String fileText = getText(filePath);
			String nucleus = "";
			int tracknum = 0;
			List<Integer> electrons = new ArrayList<Integer>();
			/*
			 * 设置原子核
			 */
			String regex = Regex.ATOM_ELEMENTNAME;
			Pattern pattern = Pattern.compile(regex);
			Matcher m = pattern.matcher(fileText);
			if(m.find()) {
				//获取元素名称 Get element name
				nucleus = m.group(1);
				logger.info("Parser nucleus name: "+nucleus);
			}
			else {
				try {
					throw new Exception("AtomStructure 元素名称格式错误");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			/*
			 * 向系统中添加轨道
			 */
			regex = Regex.ATOM_NUMBEROFTRACKS;
			pattern = Pattern.compile(regex);
			m = pattern.matcher(fileText);
			if(m.find()) {
				tracknum = Integer.parseInt(m.group(1));
				logger.info("Parser track number: "+tracknum);
			}
			else {
				try {
					throw new Exception("AtomStructure 轨道数目格式错误");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			/*
			 * 添加电子
			 */
			regex = "NumberOfElectron ::= \\S+";
			pattern = Pattern.compile(regex);
			m = pattern.matcher(fileText);
			if(m.find()) {
				regex = "(\\d+)/(\\d+)";
				pattern = Pattern.compile(regex);
				String t = m.group();
				m = pattern.matcher(t);
				while(m.find()) { //对每条轨道遍历
					int eleNum = Integer.parseInt(m.group(2));
					electrons.add(eleNum);
				}
				logger.info("Parser the electron number: "+t);
			}
			else {
				try {
					throw new Exception("AtomStructure 轨道电子格式错误");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return CircularOrbitFactory.build(nucleus, tracknum, electrons);
	}
}
