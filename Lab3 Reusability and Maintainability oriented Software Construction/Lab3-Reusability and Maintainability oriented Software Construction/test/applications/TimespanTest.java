package applications;

import static org.junit.Assert.*;

import java.time.Instant;

import org.junit.Test;

import applications.Timespan;

/**
 * Testing Timespan class
 *
 */
public class TimespanTest {

	private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
	private static final Instant d6 = Instant.parse("2016-02-17T10:30:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-18T12:00:00Z");
    private static final Instant d7 = Instant.parse("2016-02-18T12:15:00Z");
    private static final Instant d4 = Instant.parse("2016-02-18T12:30:00Z");  
    private static final Instant d8 = Instant.parse("2016-02-18T12:50:00Z");
    private static final Instant d5 = Instant.parse("2016-02-18T13:00:00Z");
    
    /*
     * Testing strategy:
     * 	  overlap() 
     * 		ts:		时间段在比较时间段之前/之后
     * 				时间段与比较时间段只有起始点或终止点相交
     * 				时间段包含比较时间段
     * 				时间段属于比较时间段
     * 				时间段与比较时间段前半段重合/后半段重合
     * 	  toString()
     * 		比较生成字符串
     */
	@Test
	public void testOverlap() {
		Timespan comparetime = new Timespan(d2, d4);
		Timespan t1 = new Timespan(d1, d6);
		Timespan t2 = new Timespan(d8,d5);
		Timespan t3 = new Timespan(d6,d2);
		Timespan t4 = new Timespan(d4,d8);
		Timespan t5 = new Timespan(d1,d5);
		Timespan t6= new Timespan(d3,d7);
		Timespan t7 = new Timespan(d6,d3);
		Timespan t8 = new Timespan(d7,d8);
		
		assertFalse(comparetime.overlap(t1));
		assertFalse(comparetime.overlap(t2));
		assertFalse(comparetime.overlap(t3));
		assertFalse(comparetime.overlap(t4));
		assertTrue(comparetime.overlap(t5));
		assertTrue(comparetime.overlap(t6));
		assertTrue(comparetime.overlap(t7));
		assertTrue(comparetime.overlap(t8));
	}
	
	@Test
	public void testToString() {
		Timespan comparetime = new Timespan(d2, d4);
		
		String s = "[2016-02-17T11:00:00Z...2016-02-18T12:30:00Z]";
		assertEquals(s, comparetime.toString());
	}

}
