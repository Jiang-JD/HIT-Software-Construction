package applications;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.Test;

import applications.MementoCareTaker;
import applications.MementoTransit;
import circularOrbit.AtomStructure;
import parser.AtomStructureParser;
import physicalObject.Electron;

/**
 * 测试备忘录机制
 *
 */
public class MementoCareTakerTest {
	/*
	 * Testing strategy:
	 * 	  add()
	 * 		m	正确的memento
	 * 		observed by get()
	 * 
	 * 	  get()
	 * 		index	索引在范围内/不再范围内
	 * 	  remove()
	 * 		m	指定备忘录在记录中/不在记录中
	 * 	  contains()
	 * 		m	指定备忘录在记录中/不在记录中
	 * 	  toTable()
	 * 		比较输出字符串
	 */
	
	@Test
	public void testAdd() {
		AtomStructureParser ap = new AtomStructureParser();
		AtomStructure as = 
		ap.initial("src/txt/AtomicStructure.txt");
		MementoCareTaker ct = new MementoCareTaker();
		MementoTransit mt = as.backup();
		ct.add(mt);
		
		assertEquals("Excepted two memento equals", ct.get(0), mt);
	}
	
	@Test
	public void testGet() {
		AtomStructureParser ap = new AtomStructureParser();
		AtomStructure as = 
		ap.initial("src/txt/AtomicStructure.txt");
		MementoCareTaker ct = new MementoCareTaker();
		
		MementoTransit mt1 = as.backup();
		ct.add(mt1);
		
		as.transit(0,1);
		MementoTransit mt2 = as.backup();
		ct.add(mt2);
		
		assertEquals(mt1, ct.get(0));
		assertNull(ct.get(3));
	}
	
	@Test
	public void testRemove() {
		AtomStructureParser ap = new AtomStructureParser();
		AtomStructure as = 
		ap.initial("src/txt/AtomicStructure.txt");
		MementoCareTaker ct = new MementoCareTaker();
		
		MementoTransit mt1 = as.backup();
		ct.add(mt1);
		
		as.transit(0,1);
		MementoTransit mt2 = as.backup();
		ct.add(mt2);
		
		ct.remove(mt2);
		
		assertNull(ct.get(1));
	}
	
	@Test
	public void testContains() {
		AtomStructureParser ap = new AtomStructureParser();
		AtomStructure as = 
		ap.initial("src/txt/AtomicStructure.txt");
		MementoCareTaker ct = new MementoCareTaker();
		
		MementoTransit mt1 = as.backup();
		ct.add(mt1);
		
		as.transit(0,1);
		MementoTransit mt2 = as.backup();
		ct.add(mt2);
		
		assertTrue(ct.contains(mt2));
	}
	
	@Test
	public void testToTable() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");//设置时间日期格式
		AtomStructureParser ap = new AtomStructureParser();
		AtomStructure as = ap.initial("src/txt/AtomicStructure.txt");
		MementoCareTaker ct = new MementoCareTaker();
		
		MementoTransit mt1 = as.backup();
		LocalDateTime dt1 = LocalDateTime.now();
		ct.add(mt1);
		
		as.transit(0,1);
		MementoTransit mt2 = as.backup();
		LocalDateTime dt2 = LocalDateTime.now();
		ct.add(mt2);
		
		as.transit(2, 3);
		String s = "+--------------------------------------------------------------------+\n" + 
				"|Index	|Time			|Central	|Tracks	|ElectronNum\n" + 
				"+--------------------------------------------------------------------+\n" + 
				"|1	|"+dt1.format(dtf)+"	|Rb		|5	|1/2;2/8;3/18;4/8;5/1;\n" + 
				"+--------------------------------------------------------------------+\n" + 
				"|2	|"+dt2.format(dtf)+"	|Rb		|5	|1/1;2/9;3/18;4/8;5/1;\n" + 
				"+--------------------------------------------------------------------+\n";
		assertEquals(s, ct.toTable());
	}

}
