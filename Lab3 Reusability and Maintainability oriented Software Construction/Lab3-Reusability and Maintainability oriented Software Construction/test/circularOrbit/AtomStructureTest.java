package circularOrbit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import applications.MementoTransit;
import centralObject.Nucleus;
import physicalObject.Electron;

public class AtomStructureTest {

	/*
	 * Testing strategy:
	 * 	  backup()
	 * 		系统初始化后进行备份
	 * 
	 * 	  restore()
	 * 		m	按之前备份条目进行恢复
	 * 
	 * 	  toString()
	 * 		与正确文本进行比较
	 */
	@Test
	public void testBackup() {
		AtomStructure as = new AtomStructure("AS");
		as.addTrack(1);
		as.addTrack(2);
		as.addObject(new Electron("E"), 0);
		as.addObject(new Electron("E"), 0);
		as.addObject(new Electron("E"), 0);
		as.addObject(new Electron("E"), 1);
		as.addObject(new Electron("E"), 1);
		as.addCentralPoint(new Nucleus("H"));
		MementoTransit m1 = as.backup();
		as.transit(0, 1);
		MementoTransit m2 = as.backup();
		
		assertEquals(3, m1.getMap().get(m1.getTracks().get(0)).size());
		assertEquals(2, m2.getMap().get(m2.getTracks().get(0)).size());
	}
	
	@Test
	public void testRestore() {
		AtomStructure as = new AtomStructure("AS");
		as.addTrack(1);
		as.addTrack(2);
		as.addObject(new Electron("E"), 0);
		as.addObject(new Electron("E"), 0);
		as.addObject(new Electron("E"), 0);
		as.addObject(new Electron("E"), 1);
		as.addObject(new Electron("E"), 1);
		as.addCentralPoint(new Nucleus("H"));
		MementoTransit m1 = as.backup();
		as.transit(0, 1);
		
		MementoTransit m2 = as.backup();
		
		assertEquals(3, as.getObjects(1).size());
		
		as.restore(m1);
		
		assertEquals(2, as.getObjects(1).size());
	}
	
	@Test
	public void testtoString() {
		AtomStructure as = new AtomStructure("AS");
		as.addTrack(1);
		as.addTrack(2);
		as.addObject(new Electron("E"), 0);
		as.addObject(new Electron("E"), 0);
		as.addObject(new Electron("E"), 0);
		as.addObject(new Electron("E"), 1);
		as.addObject(new Electron("E"), 1);
		as.addCentralPoint(new Nucleus("H"));
		
		assertEquals("[AtomStructure:AS, TrackNumber:2]\n" + 
				"[ElectronsNum:[Track1: 3][Track2: 2]", as.toString());
	}

}
