package manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.time.Instant;
import java.util.List;

import org.junit.Test;

import APIs.Difference;
import applications.Timespan;
import circularOrbit.PersonalAppEcosystem;
import manager.PersonalAppManager;
import physicalObject.App;
import physicalObject.AppFactory;

/**
 * Test App manager
 *
 */
public class PersonalAppManagerTest {

	/*
	 * Testing strategy:
	 * 	  initial()
	 * 		filePath
	 * 	  generateEcosystem()
	 * 		观察输出
	 * 	  getDistance() 
	 * 		a1,a2 	不在管理器中/在管理器中
	 * 				有距离/无穷距离
	 * 	  getDifference()
	 * 		t1,t2	正确输入，与正确结果比较
	 */
	@Test
	public void testGenerateEcosystem() {
		PersonalAppManager pam = new PersonalAppManager();
		pam.initial("src/txt/PersonalAppEcosystem.txt");
		List<PersonalAppEcosystem> pl = pam.generateEcosystems();
		assertFalse(pl.isEmpty());
		assertEquals(5, pl.get(1).getObjects(4).size());
		assertEquals("QQ", pl.get(1).getObjects(0).get(0).getName());
	}
	
	@Test
	public void testGetDistance() {
		PersonalAppManager pam = new PersonalAppManager();
		pam.initial("src/txt/PersonalAppEcosystem.txt");
		List<PersonalAppEcosystem> pl = pam.generateEcosystems();
		PersonalAppEcosystem p = pl.get(1);
		
		assertEquals(3, pam.getDistance(p.getApp("Eleme"), p.getApp("Weibo")));
		assertEquals(Integer.MAX_VALUE, pam.getDistance(p.getApp("Didi"), p.getApp("Weibo")));
		AppFactory af = new AppFactory();
		App a = (App)af.create("App ::= <We,Tencent,13.2,\"The most popular social networking App in China\",\"Social network\">");
		assertEquals(-1,pam.getDistance(p.getApp("Eleme"), a));
	}
	
	@Test
	public void testGetDifference() {
		PersonalAppManager pam = new PersonalAppManager();
		pam.initial("src/txt/PersonalAppEcosystem.txt");
		
		Timespan t1 = new Timespan(Instant.parse("2019-01-01T16:00:00Z"), Instant.parse("2019-01-02T16:00:00Z"));
		Timespan t2 = new Timespan(Instant.parse("2019-01-02T08:00:00Z"), Instant.parse("2019-01-03T23:00:00Z"));
		Difference diff = pam.getDifference(t1,t2);
		assertEquals("Track number difference: 0\n" + 
				"Track 1 Objects number: 0; Objects: Wechat-BaiduMap\n" + 
				"Track 2 Objects number: 0; Objects: None-None\n" + 
				"Track 3 Objects number: 0; Objects: None-None\n" + 
				"Track 4 Objects number: 2; Objects: [QQ, Eleme]-None\n" + 
				"Track 5 Objects number: -2; Objects: BaiduMap-[QQ, Wechat, Eleme]\n" 
				,diff.toString());
	}

}
