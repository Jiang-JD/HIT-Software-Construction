package circularOrbit;

import static org.junit.Assert.*;

import org.junit.Test;

import physicalObject.Athlete;
import physicalObject.AthleteFactory;

/**
 * TrackGame Test Class
 */
public class TrackGameTest extends ConcreteCircularOrbitTest {
	/*
	 * Testing Strategy:
	 * 	getAthlete()
	 * 	  轨道系统中运动员存在/不存在
	 * 	toString()
	 * 	  轨道中存在物体/不存在物体
	 */
	
	@Test
	public void testAthlete() {
		TrackGame tg = new TrackGame("TrackGame", "100");
		AthleteFactory af = new AthleteFactory();
		int t1 = tg.addTrack(1);
		int t2 = tg.addTrack(2);
		int t3 = tg.addTrack(3);
		int t4 = tg.addTrack(4);
		tg.addObject((Athlete)af.create("Athlete ::= <Chen,7,KOR,29,10.12>"), t1);
		tg.addObject((Athlete)af.create("Athlete ::= <Park,12,USA,28,10.01>"), t2);
		
		assertEquals("Excepted two athlete are equals", (Athlete)af.create("Athlete ::= <Chen,7,KOR,29,10.12>"), tg.getAthlete(7));
		assertEquals("Excepted two athlete are equals", (Athlete)af.create("Athlete ::= <Park,12,USA,28,10.01>"), tg.getAthlete(12));
		assertNull(tg.getAthlete(88));
	}
	
	@Test
	public void testToString() {
		TrackGame tg = new TrackGame("TrackGame", "100");
		AthleteFactory af = new AthleteFactory();
		int t1 = tg.addTrack(1);
		int t2 = tg.addTrack(2);
		int t3 = tg.addTrack(3);
		int t4 = tg.addTrack(4);
		tg.addObject((Athlete)af.create("Athlete ::= <Chen,7,KOR,29,10.12>"), t1);
		tg.addObject((Athlete)af.create("Athlete ::= <Park,12,USA,28,10.01>"), t2);
		
		String s = "[TrackGame:TrackGame, RaceType:100, TrackNumber:4]\n" + 
				"[Athletes:[Track1: [[Athlete: name:Chen, age:29, number:7, nation:KOR, personal best:10.12]]][Track2: [[Athlete: name:Park, age:28, number:12, nation:USA, personal best:10.01]]][Track3: []][Track4: []]";
		
		assertEquals(s, tg.toString());
	}
	

}
