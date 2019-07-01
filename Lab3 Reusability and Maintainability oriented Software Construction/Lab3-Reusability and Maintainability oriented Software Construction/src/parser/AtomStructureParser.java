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

import centralObject.Nucleus;
import circularOrbit.AtomStructure;
import circularOrbit.CircularOrbitFactory;
import constant.Regex;
import customException.atomStructureException.IllegalFormatElementNameException;
import customException.atomStructureException.NegativeNumberOfTracksException;
import customException.atomStructureException.NoElementNameException;
import customException.atomStructureException.NoNumberOfElectronException;
import customException.atomStructureException.NoNumberOfTracksException;
import customException.atomStructureException.SequenceTrackNumberException;
import customException.trackGameException.AthleteRepeatInputException;
import customException.trackGameException.IncorrectNationFormatException;
import customException.trackGameException.LackOfComponentException;
import customException.trackGameException.NameNumberReversedException;
import physicalObject.Electron;
import physicalObject.ElectronFactory;

/**
 * 文本解析器，解析原子结构的图输入
 *
 */
public class AtomStructureParser extends Parser<Nucleus,Electron> {
	
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
		//Check Element
		Matcher m = Pattern.compile("ElementName ::= \\S+").matcher(s);
		if(m.find()) {
			if(!m.group().matches(Regex.ATOM_ELEMENTNAME)) {
				try {
					throw new IllegalFormatElementNameException("Illegal format ElementName, should be 1 captive or 1 cap and 1 low, but was " + m.group());
				} catch(IllegalFormatElementNameException e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		else {
			try {
				throw new NoElementNameException("File has No ElementName");
			} catch (NoElementNameException e) {
				e.printStackTrace();
				return false;
			}
		}
		//Check TrackNumber 
		m = Pattern.compile("NumberOfTracks ::= \\d+").matcher(s);
		int num = 0;
		if(m.find()) {
			if(!m.group().matches(Regex.ATOM_NUMBEROFTRACKS)) {
				try {
					throw new NegativeNumberOfTracksException("Illegal format trake number , should in 4 - 10");
				} catch(NegativeNumberOfTracksException e) {
					e.printStackTrace();
					return false;
				}
			}
			else {
				Matcher ma = Pattern.compile(Regex.ATOM_NUMBEROFTRACKS).matcher(s);
				ma.find();
				num = Integer.valueOf(ma.group(1));
			}
		}
		else {
			try {
				throw new NoNumberOfTracksException();
			}catch(NoNumberOfTracksException e) {
				e.printStackTrace();
				return false;
			}
		}
		//Check Electron Format
		Set<String> athletes = new HashSet<String>();
		m = Pattern.compile("NumberOfElectron ::= \\S+").matcher(s);
		boolean f = false;
		while(m.find()) {
			f = true;
			String mt = m.group();
			if(!mt.matches(Regex.ATOM_NUMBEROFELECTRON)) {
				try {
					throw new IllegalArgumentException("Illegal format Electrons : "+m.group());
				} catch(IllegalArgumentException e) {
					e.printStackTrace();
					return false;
				}
			}
			Matcher mb = Pattern.compile("NumberOfElectron ::= \\S+").matcher(mt);
			if(mb.find()) {
				String regex = "(\\d+)/(\\d+)";
				mb = Pattern.compile(regex).matcher(mt);
				int i = 1;
				while(mb.find()) { 
					if(i != Integer.valueOf(mb.group(1))) {
						try {
							throw new SequenceTrackNumberException("Wrong Sequence Track Number, should be 1-n, but was "+mt);
						} catch(SequenceTrackNumberException e) {
							e.printStackTrace();
							return false;
						}
					}
					i++;
				}
				if(i-1 != num) {
					try {
						throw new IllegalArgumentException("Number of tracks not equal to number of electron, but was "+i+ " and "+num);
					} catch(IllegalArgumentException e) {
						e.printStackTrace();
						return false;
					}
				}
			}
		}
		if(!f){
			try {
				throw new NoNumberOfElectronException();
			}catch(NoNumberOfElectronException e) {
				e.printStackTrace();
				return false;
			}
		}
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
	 */
	public AtomStructure initial(String filePath) {
		try {
			String fileText = getText(filePath);
			String nucleus = null;
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
				tracknum = Integer.valueOf(m.group(1));
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
				ElectronFactory ef = new ElectronFactory();
				regex = "(\\d+)/(\\d+)";
				pattern = Pattern.compile(regex);
				String t = m.group();
				m = pattern.matcher(t);
				while(m.find()) { //对每条轨道遍历
					int eleNum = Integer.valueOf(m.group(2));
					electrons.add(eleNum);
				}
			}
			else {
				try {
					throw new Exception("AtomStructure 轨道电子格式错误");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return CircularOrbitFactory.build(nucleus, tracknum, electrons);
		}catch(IllegalArgumentException e) {
			return null;
		}
		
	}
}
