package circularOrbit;

import static org.junit.Assert.*;

import org.junit.Test;

import applications.tools.MementoCareTaker;
import applications.tools.Memento;
import parser.AtomStructureParser;

/**
 * Testing ConcreteState Class
 *
 */
public class ConcreteStateTest {

	/*
	 * Testing strategy:
	 * 	  backup()
	 * 		测试备份是否正确
	 * 	  resore()
	 * 		进行多次备份，将轨道系统恢复，测试是否恢复正确
	 */
	
	@Test
	public void testBackUp() {
		AtomStructureParser ap = new AtomStructureParser();
		AtomStructure as = null;
		try {
			as = ap.initial("src/txt/AtomicStructure.txt");
		}catch(Exception e) {
			e.printStackTrace();
		}
		MementoCareTaker ct = new MementoCareTaker();
		ConcreteAtomState cas = new ConcreteAtomState();
		if(as != null) {
			Memento mt1 = cas.backup(as);
			ct.add(mt1);
			as.transit(0,1);
			Memento mt2 = cas.backup(as);
			ct.add(mt2);
			
			as.transit(2, 3);
			Memento mt3 = cas.backup(as);
			ct.add(mt3);
			
			//1st version, track1 has 2
			assertEquals(2, mt1.getMap().get(mt1.getTracks().get(0)).size());
			//2nd version, track2 has 9
			assertEquals(9, mt2.getMap().get(mt2.getTracks().get(1)).size());
		}
	}
	
	@Test
	public void testRestore() {
		AtomStructureParser ap = new AtomStructureParser();
		AtomStructure as = null;
		try {
			as = ap.initial("src/txt/AtomicStructure.txt");
		}catch(Exception e) {
			e.printStackTrace();
		}
		MementoCareTaker ct = new MementoCareTaker();
		ConcreteAtomState cas = new ConcreteAtomState();
		if(as != null) {
			Memento mt1 = cas.backup(as);
			ct.add(mt1);
			as.transit(0,1);
			Memento mt2 = cas.backup(as);
			ct.add(mt2);
			
			as.transit(2, 3);
			Memento mt3 = cas.backup(as);
			ct.add(mt3);
			
			//restore to 1st version, track1 has 1
			assertTrue(cas.restore(ct.get(0), as));
			assertEquals(1, as.getObjects(0).size());
			//restore to 2nd version, track2 has 9
			assertTrue(cas.restore(ct.get(1), as));
			assertEquals(9, as.getObjects(1).size());
		}
	}

}
