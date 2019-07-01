package parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import applications.tools.UsageLog;
import circularOrbit.Relation;
import constant.Period;
import exception.ElementLabelDuplicationException;
import exception.IllegalElementFormatException;
import exception.IncorrectElementDependencyException;
import exception.LackOfComponentException;
import exception.NoSuchElementException;
import exception.UndefinedElementException;
import physicalObject.App;

/**
 * 测试APP生态系统解析器类
 *
 */
public class PersonalAppEcosystemParserTest {

	/*
	 * Testing strategy:
	 * 	parserFile()
	 * 		fileName 	correct file/incorrect from PAE_Exception
	 * 		IllegalElementFormatException()
	 * 		IncorrectElementDependencyException()
	 * 		IncorrectElementLabelOrderException()
	 * 		TrackNumberOutOfRangeException()
	 * 		NoSuchElementException()
	 * 		
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
	public void testNoSuchElementException() {
		try {
			aep.parserFile("src/txt/PAE_Exception/PersonalAppEcosystem_NoPeriod.txt");
		} catch(Exception e) {
			assertEquals(e.getClass().getName(), NoSuchElementException.class.getName());
		}
		try {
			aep.parserFile("src/txt/PAE_Exception/PersonalAppEcosystem_NoUser.txt");
		} catch(Exception e) {
			assertEquals(e.getClass().getName(), NoSuchElementException.class.getName());
		}
	}
	
	@Test
	public void testIllegalElementFormatException() {
		try {
			aep.parserFile("src/txt/PAE_Exception/PersonalAppEcosystem_IllegalAppCompany.txt");
		} catch(Exception e) {
			assertEquals(e.getClass().getName(), IllegalElementFormatException.class.getName());
		}
		try {
			aep.parserFile("src/txt/PAE_Exception/PersonalAppEcosystem_IllegalAppDes.txt");
		} catch(Exception e) {
			assertEquals(e.getClass().getName(), IllegalElementFormatException.class.getName());
		}
		try {
			aep.parserFile("src/txt/PAE_Exception/PersonalAppEcosystem_IllegalAppName.txt");
		} catch(Exception e) {
			assertEquals(e.getClass().getName(), IllegalElementFormatException.class.getName());
		}
		try {
			aep.parserFile("src/txt/PAE_Exception/PersonalAppEcosystem_IllegalAppVersion.txt");
		} catch(Exception e) {
			assertEquals(e.getClass().getName(), IllegalElementFormatException.class.getName());
		}
		try {
			aep.parserFile("src/txt/PAE_Exception/PersonalAppEcosystem_IllegalInsDate.txt");
		} catch(Exception e) {
			assertEquals(e.getClass().getName(), IllegalElementFormatException.class.getName());
		}
		try {
			aep.parserFile("src/txt/PAE_Exception/PersonalAppEcosystem_IllegalInstallTime.txt");
		} catch(Exception e) {
			assertEquals(e.getClass().getName(), IllegalElementFormatException.class.getName());
		}
		try {
			aep.parserFile("src/txt/PAE_Exception/PersonalAppEcosystem_IllegalInsTime.txt");
		} catch(Exception e) {
			assertEquals(e.getClass().getName(), IllegalElementFormatException.class.getName());
		}
		try {
			aep.parserFile("src/txt/PAE_Exception/PersonalAppEcosystem_IllegalPeriod.txt");
		} catch(Exception e) {
			assertEquals(e.getClass().getName(), IllegalElementFormatException.class.getName());
		}
		try {
			aep.parserFile("src/txt/PAE_Exception/PersonalAppEcosystem_IllegalUniDate.txt");
		} catch(Exception e) {
			assertEquals(e.getClass().getName(), IllegalElementFormatException.class.getName());
		}
		try {
			aep.parserFile("src/txt/PAE_Exception/PersonalAppEcosystem_IllegalUsageLogTime.txt");
		} catch(Exception e) {
			assertEquals(e.getClass().getName(), IllegalElementFormatException.class.getName());
		}
		try {
			aep.parserFile("src/txt/PAE_Exception/PersonalAppEcosystem_IllegalUser.txt");
		} catch(Exception e) {
			assertEquals(e.getClass().getName(), IllegalElementFormatException.class.getName());
		}
	}
	
	@Test
	public void testLackOfCompnentException() {
		try {
			aep.parserFile("src/txt/PAE_Exception/PersonalAppEcosystem_AppLack.txt");
		} catch(Exception e) {
			assertEquals(e.getClass().getName(), LackOfComponentException.class.getName());
		}
		try {
			aep.parserFile("src/txt/PAE_Exception/PersonalAppEcosystem_InstallLack.txt");
		} catch(Exception e) {
			assertEquals(e.getClass().getName(), LackOfComponentException.class.getName());
		}
		try {
			aep.parserFile("src/txt/PAE_Exception/PersonalAppEcosystem_UninstallLack.txt");
		} catch(Exception e) {
			assertEquals(e.getClass().getName(), LackOfComponentException.class.getName());
		}
		try {
			aep.parserFile("src/txt/PAE_Exception/PersonalAppEcosystem_UsageLack.txt");
		} catch(Exception e) {
			assertEquals(e.getClass().getName(), LackOfComponentException.class.getName());
		}
	}
	
	@Test
	public void testUndefinedElementException() {
		try {
			aep.parserFile("src/txt/PAE_Exception/PersonalAppEcosystem_InstallUndefinedApp.txt");
		} catch(Exception e) {
			assertEquals(e.getClass().getName(), UndefinedElementException.class.getName());
		}
		try {
			aep.parserFile("src/txt/PAE_Exception/PersonalAppEcosystem_RelationUndefinedApp.txt");
		} catch(Exception e) {
			assertEquals(e.getClass().getName(), UndefinedElementException.class.getName());
		}
		try {
			aep.parserFile("src/txt/PAE_Exception/PersonalAppEcosystem_UninstallUndefinedApp.txt");
		} catch(Exception e) {
			assertEquals(e.getClass().getName(), UndefinedElementException.class.getName());
		}
		try {
			aep.parserFile("src/txt/PAE_Exception/PersonalAppEcosystem_UsageLogUndefinedApp.txt");
		} catch(Exception e) {
			assertEquals(e.getClass().getName(), UndefinedElementException.class.getName());
		}
	}
	
	@Test
	public void testElementLabelDuplicationException() {
		try {
			aep.parserFile("src/txt/PAE_Exception/PersonalAppEcosystem_RepeatApp.txt");
		} catch(Exception e) {
			assertEquals(e.getClass().getName(), ElementLabelDuplicationException.class.getName());
		}
	}
	
	@Test
	public void testIncorrectElementDependencyException() {
		try {
			aep.parserFile("src/txt/PAE_Exception/PersonalAppEcosystem_Overlap.txt");
		} catch(Exception e) {
			assertEquals(e.getClass().getName(), IncorrectElementDependencyException.class.getName());
		}
		try {
			aep.parserFile("src/txt/PAE_Exception/PersonalAppEcosystem_Relationitself.txt");
		} catch(Exception e) {
			assertEquals(e.getClass().getName(), IncorrectElementDependencyException.class.getName());
		}
		try {
			aep.parserFile("src/txt/PAE_Exception/PersonalAppEcosystem_RestIns.txt");
		} catch(Exception e) {
			assertEquals(e.getClass().getName(), IncorrectElementDependencyException.class.getName());
		}
		try {
			aep.parserFile("src/txt/PAE_Exception/PersonalAppEcosystem_RestUni.txt");
		} catch(Exception e) {
			assertEquals(e.getClass().getName(), IncorrectElementDependencyException.class.getName());
		}
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
