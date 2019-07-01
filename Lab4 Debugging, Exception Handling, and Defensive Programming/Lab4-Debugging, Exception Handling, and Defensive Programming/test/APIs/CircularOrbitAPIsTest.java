package APIs;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import applications.tools.DividerAscendingOrder;
import centralObject.CentralPoint;
import circularOrbit.AtomStructure;
import circularOrbit.ConcreteCircularOrbit;
import circularOrbit.PersonalAppEcosystem;
import circularOrbit.TrackGame;
import manager.PersonalAppManager;
import manager.TrackGameManager;
import parser.AtomStructureParser;
import physicalObject.PhysicalObject;
import physicalObject.PhysicalObjectFactory;
import testObjects.TestObjectFactory;
import testObjects.TestTrackFactory;

/**
 * Testing APIs
 *
 */
public class CircularOrbitAPIsTest {
	CircularOrbitAPIs api = new CircularOrbitAPIs();
	/*
	 * Testing strtegy:
	 * 	 getDifference()
	 * 		输入具有单个物体的trackgame进行比较
	 * 		输入具有多个物体的atomstructure进行比较
	 * 		输入具有多个物体的personalapp进行比较
	 * 
	 * 	 getLogicalDistance()
	 * 		输入不具有关系的轨道系统/有关系的轨道系统
	 * 		输入有关系的轨道系统中存在关系的对象/不存在关系的对象
	 * 		输入物体存在于系统中/不存在于系统中
	 * 
	 * 	 getObjectDistributionEntropy()
	 * 		设置不同分布的轨道系统，计算熵值进行比较
	 * 		最小应为分布一条轨道，最大为均匀分布
	 * 
	 * 	 getPhysicalDistance()
	 * 		输入具有起始角度的轨道系统进行计算
	 */
	
	@Test
	public void testGetDifference() {
		/*
		 * Testing TrackGame
		 */
		TrackGameManager tgm1 = new TrackGameManager();
		try {
			tgm1.initial("src/txt/TrackGame_TestDifference1.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<TrackGame> tl1 = tgm1.grouping(new DividerAscendingOrder());
		TrackGameManager tgm2 = new TrackGameManager();
		try {
			tgm2.initial("src/txt/TrackGame_TestDifference2.txt");
		} catch(Exception e) {
			e.printStackTrace();
		}
		List<TrackGame> tl2 = tgm2.grouping(new DividerAscendingOrder());
		Difference diff1 = api.getDifference(tl1.get(0), tl2.get(0));
		String trackDiff = "Track number difference: 0\n" + 
				"Track 1 Objects number: 0; Objects: Ronaldo-Trump\n" + 
				"Track 2 Objects number: 0; Objects: Chistropher-Obama\n" + 
				"Track 3 Objects number: 0; Objects: Cliton-Chistropher\n" + 
				"Track 4 Objects number: 0; Objects: Bolt-Cliton\n" + 
				"Track 5 Objects number: 0; Objects: Lewis-Park\n" + 
				"Track 6 Objects number: 0; Objects: Peter-Chen\n" + 
				"Track 7 Objects number: 1; Objects: Coal-None\n";
		assertEquals(trackDiff, diff1.toString());
		
		/*
		 * Testing Atom
		 */
		AtomStructureParser asp = new AtomStructureParser();
		AtomStructure a1 = null;
		AtomStructure a2 = null;
		try {
			a1 = asp.initial("src/txt/AtomicStructure_TestDifference1.txt");
			a2 = asp.initial("src/txt/AtomicStructure_TestDifference2.txt");
		} catch(Exception e) {
			e.printStackTrace();
		}
		Difference diff2 = api.getDifference(a1,a2);
		String atomDiff = "Track number difference: 1\n" + 
				"Track 1 Objects number: 1\n" + 
				"Track 2 Objects number: 0\n" + 
				"Track 3 Objects number: 0\n" + 
				"Track 4 Objects number: 8\n" + 
				"Track 5 Objects number: 0\n" + 
				"Track 6 Objects number: 2\n";
		assertEquals(atomDiff, diff2.toString());
		
		/*
		 * Testing PersonalApp
		 */
		PersonalAppManager pam = new PersonalAppManager();
		try {
			pam.initial("src/txt/PersonalAppEcosystem_TestDifference.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<PersonalAppEcosystem> pl = pam.generateEcosystems();
		Difference diff3 = api.getDifference(pl.get(0), pl.get(1));
		String appDiff = "Track number difference: 0\n" + 
				"Track 1 Objects number: 0; Objects: Wechat-QQ\n" + 
				"Track 2 Objects number: 0; Objects: None-None\n" + 
				"Track 3 Objects number: 0; Objects: None-None\n" + 
				"Track 4 Objects number: 1; Objects: Eleme-None\n" + 
				"Track 5 Objects number: -4; Objects: None-[Weibo, Wechat, Eleme, BaiduMap]\n";
		assertEquals(appDiff, diff3.toString());
	}
	
	@Test
	public void testGetLogicDistance() {
		ConcreteCircularOrbit<CentralPoint, PhysicalObject> cco = new ConcreteCircularOrbit<CentralPoint, PhysicalObject>("A", new TestTrackFactory());
		int t1 = cco.addTrack(1);
		int t2 = cco.addTrack(2);
		int t3 = cco.addTrack(3);
		PhysicalObjectFactory pf = new TestObjectFactory();
		PhysicalObject a = pf.create("A");
		PhysicalObject b = pf.create("B");
		PhysicalObject c = pf.create("C");
		PhysicalObject d = pf.create("D");
		PhysicalObject e = pf.create("E");
		PhysicalObject f = pf.create("F");
		
		//a-b c-d a-c b-e
		cco.addObject(a, t1);
		cco.addObject(b, t1);
		cco.addObject(c, t2);
		cco.addObject(d, t3);
		cco.addObject(e, t1);
		
		cco.addOrbitRelation(a, b);
		cco.addOrbitRelation(c, d);
		cco.addOrbitRelation(a, c);
		cco.addOrbitRelation(a, d);
		
		assertEquals(1, api.getLogicalDistance(cco, c, d));
		assertEquals(2, api.getLogicalDistance(cco, d, b));
		//no connect
		assertEquals(Integer.MAX_VALUE, api.getLogicalDistance(cco, b, e));
		//f not in system
		assertEquals(-1, api.getLogicalDistance(cco, f, b));
	}
	
	/*
	 * Covers:
	 * 	  1-1 2-1 3-1 max
	 * 	  1-3 2-0 3-0 min
	 * 	  1-0 2-2 3-1 
	 *    1-2 2-0 3-1 
	 */
	@Test
	public void testGetObjectDistributionEntropy() {
		ConcreteCircularOrbit<CentralPoint, PhysicalObject> cco = new ConcreteCircularOrbit<CentralPoint, PhysicalObject>("A", new TestTrackFactory());
		
		int t1 = cco.addTrack(1);
		int t2 = cco.addTrack(2);
		int t3 = cco.addTrack(3);
		PhysicalObjectFactory pf = new TestObjectFactory();
		PhysicalObject a = pf.create("A");
		PhysicalObject b = pf.create("B");
		PhysicalObject c = pf.create("C");
		
		//1-1 2-1 3-1
		cco.addObject(a, t1);
		cco.addObject(b, t2);
		cco.addObject(c, t3);
		
		double en1 = api.getObjectDistributionEntropy(cco);
		
		//1-3 2-0 3-0
		cco.remove(a);
		cco.remove(b);
		cco.remove(c);
		cco.addObject(a, t1);
		cco.addObject(b, t1);
		cco.addObject(c, t1);
		
		double en2 = api.getObjectDistributionEntropy(cco);
		
		//1-0 2-2 3-1
		cco.remove(a);
		cco.remove(b);
		cco.remove(c);
		cco.addObject(a, t2);
		cco.addObject(b, t2);
		cco.addObject(c, t3);
		
		double en3 = api.getObjectDistributionEntropy(cco);
		
		//	 *    1-2 2-0 3-1 
		cco.remove(a);
		cco.remove(b);
		cco.remove(c);
		cco.addObject(a, t1);
		cco.addObject(b, t1);
		cco.addObject(c, t3);
		
		double en4 = api.getObjectDistributionEntropy(cco);
		
		assertEquals(1.55, en1, 0.1);
		assertEquals(1.216, en2, 0.1);
		assertEquals(1.522, en3, 0.1);
		assertEquals(1.522, en4, 0.1);
		
		/*
		 * Testing atom structure
		 */
		AtomStructureParser asp = new AtomStructureParser();
		AtomStructure a1 = null;
		try {
			a1 = asp.initial("src/txt/AtomicStructure.txt");
		} catch(Exception e) {
			e.printStackTrace();
		}
		if(a1 != null)
			assertEquals(0.921, api.getObjectDistributionEntropy(a1), 0.01); //spotbug 这里不会传null对象
	}
	
	@Test
	public void testGetPhysicalDistance() {
		ConcreteCircularOrbit<CentralPoint, PhysicalObject> cco = new ConcreteCircularOrbit<CentralPoint, PhysicalObject>("A", new TestTrackFactory());
		int t1 = cco.addTrack(1);
		int t2 = cco.addTrack(2);
		int t3 = cco.addTrack(3);
		PhysicalObjectFactory pf = new TestObjectFactory();
		PhysicalObject a = pf.create("A");
		PhysicalObject b = pf.create("B");
		PhysicalObject c = pf.create("C");
		PhysicalObject d = pf.create("D");
		PhysicalObject e = pf.create("E");
		
	    // a-b vertical c-d shupingde a-b regular triangle
		cco.addObject(a, t2, 180);
		cco.addObject(b, t1, 0);
		cco.addObject(c, t1, 270);
		cco.addObject(d, t3, 90);
		cco.addObject(e, t1);
		
		assertEquals(3, api.getPhysicalDistance(cco, a, b), 0.1);
		assertEquals(4, api.getPhysicalDistance(cco, c, d), 0.1);
		assertEquals(3.6055,api.getPhysicalDistance(cco, a, d), 0.1);
		
	}

}
