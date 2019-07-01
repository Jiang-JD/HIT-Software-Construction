package parser;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import centralObject.CentralPoint;
import parser.Parser;
import parser.TrackGameParser;
import physicalObject.Athlete;
import physicalObject.AthleteFactory;
import physicalObject.PhysicalObject;

/**
 * 测试TrackGame的文本解析器
 *
 */
public class TrackGameParserTest extends ParserTest{
	
	String s = "Athlete ::= <Cma,1882,GER,49,9.57>";
	String c = "Athlete ::= <Cma,1882,GER,49,19.57>";
	TrackGameParser p = (TrackGameParser) emptyInstance();
	/*
	 * 输入划分为
	 * 	getName() 
	 * 		正确输入
	 * 	getNumber()
	 * 		正确输入
	 * 	getNation()
	 * 		正确输入
	 * 	getAge()
	 * 		正确输入
	 * 	getPb()
	 * 		正确输入
	 * 	raceType()
	 * 		fileText 存在racetype/不存在
	 * 	trackNum()
	 * 		fileText 存在tracknum/不存在
	 * 	athletes()
	 * 		fileText 测试正确输入
	 * 	grouping() 
	 * 		filePath 输入合法文件测试
	 */

	@Override
	public Parser<? extends CentralPoint,? extends PhysicalObject> emptyInstance() {
		return new TrackGameParser();
	}
	
	@Test
	public void testGetName() {
		assertEquals("Cma", p.getName(s));
	}
	
	@Test
	public void testGetNumber() {
		assertEquals(1882, p.getNumber(s));
	}
	
	@Test
	public void testGetNation() {
		assertEquals("GER", p.getNation(s));
	}

	@Test
	public void testGetAge() {
		assertEquals(49, p.getAge(s));
	}
	
	@Test
	public void testGetPb() {
		assertEquals(9.57, p.getPb(s), 0);
		assertEquals(19.57, p.getPb(c), 0);
	}
	
	@Test
	 public void testRaceType() {
		String s = "Athlete ::= <Trump,9,CHN,19,9.89> \r\n" + 
				"Athlete ::= <Obama,6,RUS,19,9.90> \r\n" + 
				"Game ::= 100";
		
		String e = "Athlete ::= <Trump,9,CHN,19,9.89> \\r\\n\" + \r\n" + 
				"Athlete ::= <Obama,6,RUS,19,9.90> \\r\\n";
		
		assertEquals("100", p.raceType(s));
		assertEquals("", p.raceType(e));
	}
	
	@Test
	public void testTrackNum() {
		String s1 = "Athlete ::= <Tommy,3,JAM,19,10.11> \r\n" + 
				"Athlete ::= <Coal,11,RUS,19,10.11> \r\n" + 
				"NumOfTracks ::= 5 ";
		String s2 = "Athlete ::= <Tommy,3,JAM,19,10.11> \r\n" + 
				"Athlete ::= <Coal,11,RUS,19,10.11> \r\n";
		
		assertEquals(5, p.trackNum(s1));
		assertEquals(-1,p.trackNum(s2));
	}
	
	@Test
	public void testAthletes() {
		String s = "Athlete ::= <Bolt,1,JAM,38,9.94>\r\n" + 
				"Athlete ::= <Lewis,2,USA,31,10.00> \r\n" + 
				"Athlete ::= <Ronaldo,10,CNH,20,9.85> \r\n" + 
				"Athlete ::= <Wei,13,JPN,40,9.95> \r\n" + 
				"Athlete ::= <Chen,7,KOR,29,10.12> \r\n" + 
				"Athlete ::= <Park,12,USA,28,10.01> \r\n" + 
				"Athlete ::= <Trump,9,CHN,19,9.89>"; 
		
		AthleteFactory af = new AthleteFactory();
		Athlete a1 = (Athlete)af.create("Athlete ::= <Bolt,1,JAM,38,9.94>");
		Athlete a2 = (Athlete)af.create("Athlete ::= <Lewis,2,USA,31,10.00>");
		Athlete a3 = (Athlete)af.create("Athlete ::= <Ronaldo,10,CNH,20,9.85>");
		Athlete a4 = (Athlete)af.create("Athlete ::= <Wei,13,JPN,40,9.95>");
		Athlete a5 = (Athlete)af.create("Athlete ::= <Chen,7,KOR,29,10.12>");
		Athlete a6 = (Athlete)af.create("Athlete ::= <Park,12,USA,28,10.01> ");
		Athlete a7 = (Athlete)af.create("Athlete ::= <Trump,9,CHN,19,9.89>");
		
		List<Athlete> list = Arrays.asList(a1,a2,a3,a4,a5,a6,a7);
		
		assertEquals(list, p.athletes(s));
	}
	
	
	@Test
	public void parserFileTest() {
		assertFalse(p.parserFile("src/txt/TrackGame_NameAge.txt"));
		assertFalse(p.parserFile("src/txt/TrackGame_RepeatAthlete.txt"));
		assertFalse(p.parserFile("src/txt/TrackGame_NoGame.txt"));
		assertFalse(p.parserFile("src/txt/TrackGame_TrackNumOutofRange.txt"));
		assertFalse(p.parserFile("src/txt/TrackGame_LackPart.txt"));
		assertTrue(p.parserFile("src/txt/TrackGame.txt"));
	}
}
