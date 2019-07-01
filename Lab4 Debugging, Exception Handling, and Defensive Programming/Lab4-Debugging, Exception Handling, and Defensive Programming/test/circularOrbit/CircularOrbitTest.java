package circularOrbit;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import centralObject.CentralPoint;
import centralObject.CentralPointFactory;
import physicalObject.*;
import testObjects.TestObjectFactory;

/**
 * 测试CircularOrbit的实例方法
 * @param <L> 中心点类型
 * @param <E> 轨道物体类型
 */
public abstract class CircularOrbitTest<L,E> {
	/*
	 * 测试策略
	 * 	  addTrack()
	 * 		track:	新的Track，相同的Track
	 * 
	 * 	  removeTrack()
	 * 		track:	系统中存在的Track/系统中不存在的Track
	 * 				Track上存在物体/Track不存在物体
	 * 				Track外层存在其他Track
	 * 
	 * 	  addCentralPoint()
	 * 		centralpoint：	系统中没有中心物体/系统已存在中心物体
	 * 
	 * 	  addObject()
	 * 		object：	轨道物体
	 * 		track：	轨道在系统中存在/不存在
	 * 	
	 * 	  remove()
	 * 		object:	物体在系统中存在/不存在
	 * 				物体都相同，移除最外层角度最大的
	 * 
	 * 	  remove()
	 * 		object：物体在系统中存在/不存在
	 * 				物体都相同，移除最外层角度最大的
	 * 		track：	轨道存在/不存在
	 * 
	 * 	  addCentralRelation()
	 * 		orbitObject：	轨道物体在系统中存在/轨道物体不存在系统中
	 * 						中心物体存在/不存在
	 * 
	 *    removeCentralRelation()
	 *    	orbitObject：	轨道物体在系统中存在/不存在
	 *    					轨道物体与中心物体有关系/没有关系
	 *    
	 *    addOrbitRelation()
	 *    	object1，object2：	物体在系统中存在/不存在
	 *    						重复添加
	 *    
	 *    removeOrbitRelation()
	 *    	object1，object2：	物体在系统中存在/不存在
	 *    						两个物体有关系/没有关系
	 *    centrals()
	 *    	中心点与物体有关系/与物体没有任何关系
	 *    
	 *    relatives()
	 *    	object：	存在与物体有关系的物体/不存在有关系的物体
	 *    			指定物体在系统中存在/在系统中不存在
	 *    
	 *    transit()
	 *    	object：	物体在系统中存在/不存在
	 *    	newtrack：	轨道在系统中存在/不存在
	 *    
	 *    transit()
	 *    	object：	物体在系统中存在/不存在
	 *    	oldtrack：	轨道在系统中存在/不存在
	 *    	newtrack：	轨道在系统中存在/不存在
	 *    
	 *    iterator()
	 *    	轨道物体在系统中遍历，测试是否按轨道次序和角度次序输出
	 *    
	 *    contains()
	 *    	object:	物体在系统中存在/不存在
	 *    
	 *    contains()
	 *    	object：	物体在轨道中存在/不存在
	 *    	track：	轨道存在/不存在
	 *    
	 *    contains()
	 *    	track：	轨道存在/不存在
	 *    
	 *    indexOf()
	 *    	track：	系统中存在轨道/不存在轨道
	 *    			单个轨道/多个相同轨道
	 *    
	 *    getAngle()
	 *    	object：物体存在初始角度/不存在初始角度
	 *    			物体在系统中存在/不存在
	 *    
	 *    getObjects()
	 *    	index：	轨道在系统中存在/不存在
	 *    			轨道上有物体/没有物体
	 *    
	 *    
	 */
	
	public abstract CircularOrbit<CentralPoint , PhysicalObject> emptyInstance();
	
	@Test
	public void testAddTrack() {
		CircularOrbit<CentralPoint , PhysicalObject> co = emptyInstance();
		
		assertEquals("Excepted True",0, co.addTrack(1));
		assertEquals("Excepted False",-1, co.addTrack(1));
		
		//t2应该在最外层
		assertEquals("Excepted t2 at the outmost", 1, co.addTrack(2));
	}
	
	@Test
	public void testRemoveTrack() {
		PhysicalObjectFactory pof = new TestObjectFactory();
		PhysicalObject po1 = pof.create("A");
		PhysicalObject po2 = pof.create("B");
		PhysicalObject po3 = pof.create("C");
		
		CircularOrbit<CentralPoint , PhysicalObject> co = emptyInstance();
		
		int t1 = co.addTrack(1); //轨道为空
		int t2 = co.addTrack(2); //轨道不为空
		co.addObject(po1, t1);
		co.addObject(po2, t2);
		int t3 = co.addTrack(3); //测试顺序
		co.addObject(po3, t3);
		
		//轨道物体应一起移除
		assertTrue("Excepted true",co.remove(t2));
		assertTrue("Excepted object exists", co.contains(po1));
		assertFalse("Excepted object not exists", co.contains(po2));
		
		//t3在t2移除后应顺位向前一位
		assertEquals("Excepted index is 1", t3-1, co.indexOf(po3));
	}
	
	@Test
	public void testAddCentralPoint() {
		CentralPointFactory cpf = new CentralPointFactory();
		CentralPoint cp1 = (CentralPoint)cpf.create("A");
		CentralPoint cp2 = (CentralPoint)cpf.create("B");
		
		CircularOrbit<CentralPoint , PhysicalObject> co = emptyInstance();
		
		assertNull("Excepted null",co.addCentralPoint(cp1));
		
		assertEquals("Excepted cp1", cp1, co.addCentralPoint(cp2));
	}
	
	@Test
	public void testAddObject() {
		PhysicalObjectFactory pof = new TestObjectFactory();
		PhysicalObject po1 = pof.create("A");
		
		CircularOrbit<CentralPoint , PhysicalObject> co = emptyInstance();
		
		int t1 = co.addTrack(1);
		int t3 = 3;
		
		assertTrue("Excepted true", co.addObject(po1, t1));
		assertFalse("Excepted t2 add false", co.addObject(po1,t3));
	}
	
	@Test
	public void testRemoveObject() {
		PhysicalObjectFactory pof = new TestObjectFactory();
		PhysicalObject po1 = pof.create("A");
		PhysicalObject po2 = pof.create("B"); 
		PhysicalObject po4 = pof.create("A");
		PhysicalObject po5 = pof.create("A");
		
		CircularOrbit<CentralPoint , PhysicalObject> co = emptyInstance();
		int t1 = co.addTrack(1);
		int t2 = co.addTrack(2);
		
		co.addObject(po1, t1);
		assertTrue("Excepted is true", co.remove(po1));
		//po2未加入系统
		assertFalse("Excepted po2 is false", co.remove(po2));
		
		//same objects in differ tracks
		co.addObject(po1, t1, 0.5);
		co.addObject(po4, t2, 0.7);
		co.addObject(po5, t2, 2.44);
		
		assertTrue("Excepted to be true", co.remove(po1));
	}
	
	@Test
	public void testRemoveObjectTrack() {
		PhysicalObjectFactory pof = new TestObjectFactory();
		PhysicalObject po1 = pof.create("A");
		PhysicalObject po2 = pof.create("A");
		PhysicalObject po3 = pof.create("A");
		
		CircularOrbit<CentralPoint , PhysicalObject> co = emptyInstance();
		int t1 = co.addTrack(1);
		int t2 = co.addTrack(2);
		co.addObject(po1, t2);
		
		assertFalse("Excepted po1 in t1 is false", co.remove(po1,t1));
		assertTrue("Excepted po1 to be true", co.remove(po1, t2));
		
		co.addObject(po1, t1, 1.2);
		co.addObject(po2, t1, 2.4);
		co.addObject(po3, t1, 0.2);
		
		assertTrue("Excepted po2 to be true", co.remove(po2,t1));
	}
	
	@Test
	public void testAddCetralRelation() {
		PhysicalObjectFactory pof = new TestObjectFactory();
		PhysicalObject po1 = pof.create("A");
		PhysicalObject po2 = pof.create("B");
		
		CentralPointFactory cpf = new CentralPointFactory();
		CentralPoint cp1 = (CentralPoint)cpf.create("A");
		
		CircularOrbit<CentralPoint , PhysicalObject> co = emptyInstance();
		int t1 = co.addTrack(1);
		co.addObject(po2, t1);
		
		//po1 not in system
		assertFalse("Excepted false", co.addCentralRelation(po1));
		//central point not in system
		assertFalse("Excpted false", co.addCentralRelation(po2));
		
		co.addCentralPoint(cp1);
		assertFalse("Excpted false", co.addCentralRelation(cp1));
		assertTrue("Excpted true", co.addCentralRelation(po2));
	}
	
	@Test
	public void testRemoveCentralRelation() {
		PhysicalObjectFactory pof = new TestObjectFactory();
		PhysicalObject po1 = pof.create("A");
		PhysicalObject po2 = pof.create("B");
		PhysicalObject po3 = pof.create("C");
		
		CentralPointFactory cpf = new CentralPointFactory();
		CentralPoint cp1 = (CentralPoint) cpf.create("A");
		
		CircularOrbit<CentralPoint , PhysicalObject> co = emptyInstance();
		int t1 = co.addTrack(1);
		int t2 = co.addTrack(2);
		co.addObject(po1, t1);
		co.addObject(po2, t2);
		co.addCentralPoint(cp1);
		co.addCentralRelation(po1);
		
		assertFalse("Excepted po3 returns false",co.removeCentralRelation(po3));
		//po2 has no relation
		assertFalse("Excepted po2 returns false", co.removeCentralRelation(po2));
		assertTrue("Excepted true", co.removeCentralRelation(po1));
	}
	
	@Test
	public void testAddOrbitRelation() {
		PhysicalObjectFactory pof = new TestObjectFactory();
		PhysicalObject po1 = pof.create("A");
		PhysicalObject po2 = pof.create("B");
		PhysicalObject po3 = pof.create("C");
		
		CircularOrbit<CentralPoint , PhysicalObject> co = emptyInstance();
		int t1 = co.addTrack(1);
		int t2 = co.addTrack(2);
		co.addObject(po1, t1);
		co.addObject(po2, t2);
		
		assertFalse("Excepted false", co.addOrbitRelation(po1, po3));
		assertTrue("Excepted true", co.addOrbitRelation(po1, po2));
		assertFalse("Excepted dupilicate add false",co.addOrbitRelation(po1, po2));
	}
	
	@Test
	public void testRemoveOrbitRelation() {
		PhysicalObjectFactory pof = new TestObjectFactory();
		PhysicalObject po1 = pof.create("A");
		PhysicalObject po2 = pof.create("B");
		PhysicalObject po3 = pof.create("C");
		PhysicalObject po4 = pof.create("D");
		
		CircularOrbit<CentralPoint , PhysicalObject> co = emptyInstance();
		int t1 = co.addTrack(1);
		int t2 = co.addTrack(2);
		co.addObject(po1, t1);
		co.addObject(po2, t2);
		co.addObject(po3, t1);
		co.addOrbitRelation(po1, po2);
		
		//po4 not in system
		assertFalse("Excepted false", co.removeOrbitRelation(po1, po4));
		assertFalse("Excepted false", co.removeOrbitRelation(po1, po3));
		assertTrue("Excepted to be po2", co.removeOrbitRelation(po1, po2));
	}
	
	@Test
	public void testCentrals() {
		PhysicalObjectFactory pof = new TestObjectFactory();
		PhysicalObject po1 = pof.create("A");
		PhysicalObject po2 = pof.create("B");
		PhysicalObject po3 = pof.create("C");
		CentralPointFactory cpf = new CentralPointFactory();
		CentralPoint cp1 = (CentralPoint)cpf.create("A");
		
		CircularOrbit<CentralPoint , PhysicalObject> co = emptyInstance();
		int t1 = co.addTrack(1);
		int t2 = co.addTrack(2);
		co.addCentralPoint(cp1);
		co.addObject(po1, t1);
		co.addObject(po2, t2);
		co.addObject(po3, t1);
		co.addCentralRelation(po1);
		co.addCentralRelation(po2);
		
		Set<PhysicalObject> set = new HashSet<PhysicalObject>();
		set.add(po1);
		set.add(po2);
		
		assertEquals("Excepted sets are equal", set, co.centrals());
	}
	
	@Test
	public void testRelatives() {
		PhysicalObjectFactory pof = new TestObjectFactory();
		PhysicalObject po1 = pof.create("A");
		PhysicalObject po2 = pof.create("B");
		PhysicalObject po3 = pof.create("C");
		PhysicalObject po4 = pof.create("D");
		PhysicalObject po5 = pof.create("E");
		
		CircularOrbit<CentralPoint , PhysicalObject> co = emptyInstance();
		int t1 = co.addTrack(1);
		int t2 = co.addTrack(2);
		
		co.addObject(po1, t1);
		co.addObject(po2, t2);
		co.addObject(po3, t1);
		co.addObject(po5, t2);
		
		co.addOrbitRelation(po1, po2);
		co.addOrbitRelation(po2, po3);
		
		Set<PhysicalObject> set = new HashSet<PhysicalObject>();
		
		//po4 not in system
		assertEquals(set, co.relatives(po4));
		//po5 has no relation
		assertEquals(set, co.relatives(po5));
		
		set.add(po2);
		assertEquals(set, co.relatives(po1));
		
		set.remove(po2);
		set.add(po1);
		set.add(po3);
		assertEquals(set, co.relatives(po2));
	}
	
	@Test
	public void testTransit() {
		PhysicalObjectFactory pof = new TestObjectFactory();
		PhysicalObject po1 = pof.create("A");
		PhysicalObject po2 = pof.create("B");
		
		CircularOrbit<CentralPoint , PhysicalObject> co = emptyInstance();
		int t1 = co.addTrack(1);
		int t2 = co.addTrack(2);
		
		co.addObject(po1, t1);
		
		assertEquals("Expected -1", -1, co.transit(po2, t2));
		assertEquals(t1, co.transit(po1, t2));
	}
	
	@Test
	public void testTransitWithSpecifiedOldTrack() {
		PhysicalObjectFactory pof = new TestObjectFactory();
		PhysicalObject po1 = pof.create("A");
		PhysicalObject po2 = pof.create("B");
		
		CircularOrbit<CentralPoint , PhysicalObject> co = emptyInstance();
		int t1 = co.addTrack(1);
		int t2 = co.addTrack(2);
		int t3 = co.addTrack(3);
		int t4 = 3;
		
		co.addObject(po1, t1);
		//po2 not in system
		assertFalse("Expected false",co.transit(po2, t1, t2));
		//t4 not in system
		assertFalse("Excepted false",co.transit(po1, t2, t4));
		//same track
		assertFalse("Excepted false", co.transit(po1, t2, t2));
		//po1 not in track2
		assertFalse("Excepted false", co.transit(po1, t2, t3));
		//correct answer
		assertTrue(co.transit(po1,t1, t2));
	}
	
	@Test
	public void testContains() {
		PhysicalObjectFactory pof = new TestObjectFactory();
		PhysicalObject po1 = pof.create("A");
		PhysicalObject po2 = pof.create("B");
		PhysicalObject po3 = pof.create("C");
		
		CircularOrbit<CentralPoint , PhysicalObject> co = emptyInstance();
		int t1 = co.addTrack(1);
		int t2 = co.addTrack(2);
		int t3 = co.addTrack(3);
		
		co.addObject(po1, t1);
		co.addObject(po3, t3);
		co.remove(t2);
		
		//Track
		//t2 not in system
		assertFalse(co.contains(2.0));
		assertTrue(co.contains(1.0));
		
		//Object
		assertFalse(co.contains(po2));
		assertTrue(co.contains(po1));
		
		//Object and Track
		assertTrue(co.contains(po1, t1));
		assertFalse(co.contains(po3, t1));
	}
	
	@Test
	public void testIterator() {
		PhysicalObjectFactory pof = new TestObjectFactory();
		PhysicalObject po1 = pof.create("A");
		PhysicalObject po2 = pof.create("B");
		PhysicalObject po3 = pof.create("C");
		PhysicalObject po4 = pof.create("D");
		PhysicalObject po5 = pof.create("E");
		
		CircularOrbit<CentralPoint , PhysicalObject> co = emptyInstance();
		int t1 = co.addTrack(1);
		int t2 = co.addTrack(2);
		int t3 = co.addTrack(3);
		
		co.addObject(po1, t1, 12);
		co.addObject(po3, t3, 25);
		co.addObject(po2, t2, 55);
		co.addObject(po4, t1, 120);
		co.addObject(po5, t3, 355);
		
		Iterator<PhysicalObject> it = co.iterator();
		
		List<PhysicalObject> list = new ArrayList<PhysicalObject>();
		list.add(po1);
		list.add(po4);
		list.add(po2);
		list.add(po3);
		list.add(po5);
		
		List<PhysicalObject> ilist = new ArrayList<PhysicalObject>();
		while(it.hasNext()) {
			ilist.add(it.next());
		}
		
		assertEquals(list, ilist);
	}
	
	@Test
	public void testGetAngle() {
		PhysicalObjectFactory pof = new TestObjectFactory();
		PhysicalObject po1 = pof.create("A");
		PhysicalObject po2 = pof.create("B");
		PhysicalObject po3 = pof.create("C");
		PhysicalObject po4 = pof.create("D");
		
		CircularOrbit<CentralPoint , PhysicalObject> co = emptyInstance();
		int t1 = co.addTrack(1);
		int t2 = co.addTrack(2);
		int t3 = co.addTrack(3);
		
		co.addObject(po1, t1, 50);
		co.addObject(po3, t3, 33);
		co.addObject(po2, t2);
		
		//po1 is 50
		assertEquals(50, co.getAngle(po1), 0);
		//po2 has no angle
		assertEquals(-1, co.getAngle(po2), 0);
		//po4 not in system
		assertEquals(-1, co.getAngle(po4),0);
	}
	
	@Test
	public void testGetObjects() {
		PhysicalObjectFactory pof = new TestObjectFactory();
		PhysicalObject po1 = pof.create("A");
		PhysicalObject po2 = pof.create("B");
		PhysicalObject po3 = pof.create("C");
		PhysicalObject po4 = pof.create("D");
		
		CircularOrbit<CentralPoint , PhysicalObject> co = emptyInstance();
		int t1 = co.addTrack(1);
		int t2 = co.addTrack(2);
		int t3 = co.addTrack(3);
		
		co.addObject(po1, t1);
		co.addObject(po2, t1);
		co.addObject(po3, t2);
		co.addObject(po4, t2);
		
		assertEquals(Arrays.asList(po1,po2), co.getObjects(t1));
		assertEquals(Arrays.asList(po3,po4), co.getObjects(t2));
		
		assertEquals(Arrays.asList(), co.getObjects(t3));
		assertNull(co.getObjects(3));
		
	}
}

