package parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import applications.UsageLog;
import circularOrbit.Relation;
import constant.Period;
import parser.PersonalAppEcosystemParser;
import physicalObject.App;

/**
 * 测试APP生态系统解析器类
 *
 */
public class PersonalAppEcosystemParserTest {

	/*
	 * Testing strategy:
	 * 	parserFile()
	 * 		fileName 	correct file/incorrect
	 * 	getApps()
	 * 		fileText	correct file text
	 * 		observed apps object
	 * 	getRealtion() 
	 * 		fileText 	correct file text
	 * 		observed Relation object
	 * 	getPeriod()
	 * 		fileText	correct file text
	 * 	getUser()
	 * 		fileText	correct file text
	 * 	getAppInstallTime()
	 * 		fileText	correct file text
	 * 	getUsageLog() 
	 * 		fileText	correct file text
	 * 	getName() 
	 * 		s	contain App name/ not contain
	 * 	getCompany
	 * 		s 	contain app company / not contain
	 * 	getVersion()
	 * 		s 	contain versino/ not contain
	 * 	getDescription()
	 * 		s 	contain description/ not contain
	 * 	getArea
	 * 		s 	contain area/ not contain
	 * 
	 */
	
	PersonalAppEcosystemParser aep = new PersonalAppEcosystemParser();
	String t  = aep.getText("src/txt/PersonalAppEcosystem.txt");
	
	@Test
	public void testParserFile() {
		assertFalse(aep.parserFile("src/txt/PersonalAppEcosystem_AppLack.txt"));
		assertFalse(aep.parserFile("src/txt/PersonalAppEcosystem_IllegalInstallTime.txt"));
		assertFalse(aep.parserFile("src/txt/PersonalAppEcosystem_IllegalUsageLogTime.txt"));
		assertFalse(aep.parserFile("src/txt/PersonalAppEcosystem_InstallUndefinedApp.txt"));
		assertFalse(aep.parserFile("src/txt/PersonalAppEcosystem_NoPeriod.txt"));
		assertFalse(aep.parserFile("src/txt/PersonalAppEcosystem_NoUser.txt"));
		assertFalse(aep.parserFile("src/txt/PersonalAppEcosystem_RelationUndfinedApp.txt"));
		assertFalse(aep.parserFile("src/txt/PersonalAppEcosystem_RepeatApp.txt"));
		assertFalse(aep.parserFile("src/txt/PersonalAppEcosystem_UninstallUndefinedApp.txt"));
		assertFalse(aep.parserFile("src/txt/PersonalAppEcosystem_UsageLogUndefinedApp.txt"));
		assertTrue(aep.parserFile("src/txt/PersonalAppEcosystem.txt"));
		
	}
	
	@Test 
	public void testGetAppInstallTime() {
		List<UsageLog> li = aep.getUsageLog(t);
		
		assertEquals(12,li.size());
		assertEquals(new UsageLog("Wechat", Instant.parse("2019-01-01T15:00:00Z"), 2), li.get(0));
	}
	
	@Test
	public void testGetApps() {
		Set<App> li = aep.getApps(t);
		
		assertEquals(6, li.size());
		App a = new App("Wechat","Tencent", "13.2","\"The most popular social networking App in China\"","\"Social network\"");
		assertTrue(li.contains(a));
	}
	
	@Test
	public void testGetCompany() {
		String r = "App ::= <BaiduMap,Baidu,2.9000000.20v03,\"The most popular map App in China\",\"Travel\">";
		String w = "lkjlkjlkjlk";
		
		assertEquals("Baidu", aep.getCompany(r));
		assertEquals("", aep.getCompany(w));
		
	}
	
	@Test
	public void testGetDescription() {
		String r = "App ::= <BaiduMap,Baidu,2.9000000.20v03,\"The most popular map App in China\",\"Travel\">";
		String w = "lkjlkjlkjlk";
		
		assertEquals("\"The most popular map App in China\"", aep.getDescription(r));
		assertEquals("", aep.getDescription(w));
		
	}
	
	@Test
	public void testGetName() {
		String r = "App ::= <BaiduMap,Baidu,2.9000000.20v03,\"The most popular map App in China\",\"Travel\">";
		String w = "lkjlkjlkjlk";
		
		assertEquals("BaiduMap", aep.getName(r));
		assertEquals("", aep.getName(w));
		
	}
	
	@Test
	public void testGetVersion() {
		String r = "App ::= <BaiduMap,Baidu,2.9000000.20v03,\"The most popular map App in China\",\"Travel\">";
		String w = "lkjlkjlkjlk";
		
		assertEquals("2.9000000.20v03", aep.getVersion(r));
		assertEquals("", aep.getVersion(w));
		
	}
	
	@Test
	public void testGetArea() {
		String r = "App ::= <BaiduMap,Baidu,2.9000000.20v03,\"The most popular map App in China\",\"Travel\">";
		String w = "lkjlkjlkjlk";
		
		assertEquals("\"Travel\"", aep.getArea(r));
		assertEquals("", aep.getArea(w));
	}
	
	@Test
	public void testGetPeriod() {
		assertEquals(Period.Day, aep.getPeriod(t));
	}
	
	@Test
	public void testGetRelation() {
		List<Relation<String>> rel = aep.getRelation(t);
		
		assertEquals(4, rel.size());
		assertTrue(rel.contains(new Relation<String>("QQ", "Weibo")));
	}
	
	@Test
	public void testGetUser() {
		assertEquals("TimWong", aep.getUser(t));
	}
}
