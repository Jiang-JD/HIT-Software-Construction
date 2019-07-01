package manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import applications.tools.DividerAscendingOrder;
import circularOrbit.TrackGame;
import physicalObject.Athlete;
import physicalObject.AthleteFactory;

/**
 * 测试TrackGameManager类
 *
 */
public class TrackGameManagerTest {
	
	/*
	 * 输入划分为
	 * 	addAthlete()
	 * 		athlete:	运动员未添加/已添加
	 * 
	 * 	grouping()
	 * 	  grouping()测试主要测试Divider分组器，其已经在DividerTest里测试，此单元测试不再进行测试
	 * 
	 * 	exchange()
	 * 	  a1,a2:		运动员在同一组/不在同一组
	 * 					运动员在运动员集合中/不在运动员集合中
	 * 
	 * 	indexOfGroup()
	 * 	  athlete:		运动员在分组中/不在分组中
	 * 	getAthlete()
	 * 	  number:		正确输入/错误输入
	 * 	  name:			正确输入/错误输入
	 */
	
	@Test
	public void testAddAthlete() {
		TrackGameManager tgm = new TrackGameManager();
		AthleteFactory af = new AthleteFactory();
		Athlete bolt = (Athlete) af.create("Athlete ::= <Bolt,1,JAM,38,9.94>");
		Athlete lewis = (Athlete) af.create("Athlete ::= <Lewis,2,USA,31,10.00>");
		
		assertTrue(tgm.add(bolt));
		assertTrue(tgm.add(lewis));
		//bolt already in system
		assertFalse(tgm.add(bolt));
	}
	
	@Test
	public void testExchange() {
		TrackGameManager tgm = new TrackGameManager();
		AthleteFactory af = new AthleteFactory();
		Athlete a1 = (Athlete)af.create("Athlete ::= <Bolt,1,JAM,38,9.94>");
		Athlete a2 = (Athlete)af.create("Athlete ::= <Lewis,2,USA,31,10.00>");
		Athlete a3 = (Athlete)af.create("Athlete ::= <Ronaldo,10,CNH,20,9.85>");
		Athlete a4 = (Athlete)af.create("Athlete ::= <Wei,13,JPN,40,9.95>");
		Athlete a5 = (Athlete)af.create("Athlete ::= <Chen,7,KOR,29,10.12>");
		Athlete a6 = (Athlete)af.create("Athlete ::= <Park,12,USA,28,10.01> ");
		Athlete a7 = (Athlete)af.create("Athlete ::= <Trump,9,CHN,19,9.89>");
		Athlete a8 = (Athlete)af.create("Athlete ::= <Trump,5,JPN,19,9.89>");
		
		tgm.add(a1);
		tgm.add(a2);
		tgm.add(a3);
		tgm.add(a4);
		tgm.add(a5);
		tgm.add(a6);
		tgm.add(a7);
		tgm.setRaceType("100");
		tgm.setTrackNumber(4);
		tgm.grouping(new DividerAscendingOrder());
		
		
		List<List<Athlete>> group1 = new ArrayList<List<Athlete>>();
		group1.add(Arrays.asList(a4,a6,a2,a5));
		group1.add(Arrays.asList(a3,a7,a1));
		
		List<List<Athlete>> group2 = new ArrayList<List<Athlete>>();
		group2.add(Arrays.asList(a4,a2,a6,a3));
		group2.add(Arrays.asList(a5,a7,a1));
		
		//Athlete not in the set
		assertNull(tgm.exchangeL(a1, a8));
		//Two athletes in the same group
		assertEquals(group1,tgm.exchangeL(a2, a6));
		//Two athletes in different group
		tgm.grouping(new DividerAscendingOrder());
		assertEquals(group2,tgm.exchangeL(a3, a5));
	}
	
	@Test
	public void testIndexOfGroup() {
		TrackGameManager tgm = new TrackGameManager();
		AthleteFactory af = new AthleteFactory();
		Athlete a1 = (Athlete)af.create("Athlete ::= <Bolt,1,JAM,38,9.94>");
		Athlete a2 = (Athlete)af.create("Athlete ::= <Lewis,2,USA,31,10.00>");
		Athlete a3 = (Athlete)af.create("Athlete ::= <Ronaldo,10,CNH,20,9.85>");
		Athlete a4 = (Athlete)af.create("Athlete ::= <Wei,13,JPN,40,9.95>");
		Athlete a5 = (Athlete)af.create("Athlete ::= <Chen,7,KOR,29,10.12>");
		Athlete a6 = (Athlete)af.create("Athlete ::= <Park,12,USA,28,10.01> ");
		Athlete a7 = (Athlete)af.create("Athlete ::= <Trump,9,CHN,19,9.89>");
		Athlete a8 = (Athlete)af.create("Athlete ::= <Trump,5,JPN,19,9.89>");
		
		tgm.add(a1);
		tgm.add(a2);
		tgm.add(a3);
		tgm.add(a4);
		tgm.add(a5);
		tgm.add(a6);
		tgm.add(a7);
		tgm.setRaceType("100");
		tgm.setTrackNumber(4);
		tgm.grouping(new DividerAscendingOrder());
		
		assertEquals(1, tgm.indexOfGroup(a3));
		assertEquals(0, tgm.indexOfGroup(a5));
		assertEquals(-1, tgm.indexOfGroup(a8));
	}
	
	/*
	 * 由于BuildtrackGame是私有方法，所以通过grouping间接测试
	 */
	@Test
	public void testBuildeTrackGames() {
		TrackGameManager tgm = new TrackGameManager();
		AthleteFactory af = new AthleteFactory();
		Athlete a1 = (Athlete)af.create("Athlete ::= <Bolt,1,JAM,38,9.94>");
		Athlete a2 = (Athlete)af.create("Athlete ::= <Lewis,2,USA,31,10.00>");
		Athlete a3 = (Athlete)af.create("Athlete ::= <Ronaldo,10,CNH,20,9.85>");
		Athlete a4 = (Athlete)af.create("Athlete ::= <Wei,13,JPN,40,9.95>");
		Athlete a5 = (Athlete)af.create("Athlete ::= <Chen,7,KOR,29,10.12>");
		Athlete a6 = (Athlete)af.create("Athlete ::= <Park,12,USA,28,10.01> ");
		Athlete a7 = (Athlete)af.create("Athlete ::= <Trump,9,CHN,19,9.89>");
		
		tgm.add(a1);
		tgm.add(a2);
		tgm.add(a3);
		tgm.add(a4);
		tgm.add(a5);
		tgm.add(a6);
		tgm.add(a7);
		tgm.setRaceType("100");
		tgm.setTrackNumber(4);
		List<TrackGame> tgs = tgm.grouping(new DividerAscendingOrder());
		
		//a4 should be in 1st group and 1st track(the inside)
		assertEquals(a4, tgs.get(0).getObjects(0).get(0));
		//a3 should be in last group and 1st track(the inside)
		assertEquals(a3, tgs.get(1).getObjects(0).get(0));
		
	}
	
	@Test
	public void testGrouping() {
		TrackGameManager tgm = new TrackGameManager();
		try {
			tgm.initial("src/txt/TrackGame.txt");
		}catch(Exception e) {
			e.printStackTrace();
		}
		List<TrackGame> li = tgm.grouping(new DividerAscendingOrder());
		assertTrue(li.get(0).getRaceType().equals("100"));
		assertEquals(li.get(0).getObjects(4).get(0).getName(), "Chen");
	}
	
	@Test
	public void testRemove() {
		TrackGameManager tgm = new TrackGameManager();
		AthleteFactory af = new AthleteFactory();
		Athlete a1 = (Athlete)af.create("Athlete ::= <Bolt,1,JAM,38,9.94>");
		Athlete a2 = (Athlete)af.create("Athlete ::= <Lewis,2,USA,31,10.00>");
		Athlete a3 = (Athlete)af.create("Athlete ::= <Ronaldo,10,CNH,20,9.85>");
		Athlete a4 = (Athlete)af.create("Athlete ::= <Wei,13,JPN,40,9.95>");
		Athlete a5 = (Athlete)af.create("Athlete ::= <Chen,7,KOR,29,10.12>");
		Athlete a6 = (Athlete)af.create("Athlete ::= <Park,12,USA,28,10.01> ");
		Athlete a7 = (Athlete)af.create("Athlete ::= <Trump,9,CHN,19,9.89>");
		Athlete a8 = (Athlete)af.create("Athlete ::= <Trump,5,JPN,19,9.89>");
		

		tgm.add(a1);
		tgm.add(a2);
		tgm.add(a3);
		tgm.add(a4);
		tgm.add(a5);
		tgm.add(a6);
		tgm.add(a7);
		tgm.setRaceType("100");
		tgm.setTrackNumber(4);
		tgm.grouping(new DividerAscendingOrder());
		
		assertEquals(true, tgm.remove(a3));
		assertEquals(-1,tgm.indexOfGroup(a8));
		assertFalse(tgm.isEmpty());
	}
	
	@Test
	public void testGetMethod() {
		TrackGameManager tgm = new TrackGameManager();
		AthleteFactory af = new AthleteFactory();
		Athlete a1 = (Athlete)af.create("Athlete ::= <Bolt,1,JAM,38,9.94>");
		Athlete a2 = (Athlete)af.create("Athlete ::= <Lewis,2,USA,31,10.00>");
		Athlete a3 = (Athlete)af.create("Athlete ::= <Ronaldo,10,CNH,20,9.85>");
		Athlete a4 = (Athlete)af.create("Athlete ::= <Wei,13,JPN,40,9.95>");
		Athlete a5 = (Athlete)af.create("Athlete ::= <Chen,7,KOR,29,10.12>");
		Athlete a6 = (Athlete)af.create("Athlete ::= <Park,12,USA,28,10.01> ");
		Athlete a7 = (Athlete)af.create("Athlete ::= <Trump,9,CHN,19,9.89>");
		Athlete a8 = (Athlete)af.create("Athlete ::= <Trump,5,JPN,19,9.89>");
		

		tgm.add(a1);
		tgm.add(a2);
		tgm.add(a3);
		tgm.add(a4);
		tgm.add(a5);
		tgm.add(a6);
		tgm.add(a7);
		tgm.setRaceType("100");
		tgm.setTrackNumber(4);
		tgm.grouping(new DividerAscendingOrder());
		
		assertEquals(a3, tgm.getAthlete(10));
		assertEquals(null, tgm.getAthlete(89));
		assertEquals(a3, tgm.getAthlete("Ronaldo"));
		assertEquals(null, tgm.getAthlete("mayun"));
		
		assertEquals("100",tgm.getRaceType());
		assertEquals(4, tgm.getTrackNumber());
	}

}
