package applications;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import applications.tools.Memento;
import applications.tools.MementoCareTaker;
import circularorbit.AtomStructure;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.Test;

import parser.AtomStructureParser;

/**.
 * 测试备忘录机制
 *
 */
public class MementoCareTakerTest {
  /*
     * Testing strategy:
     *       add()
     *         m    正确的memento
     *         observed by get()
     * 
     *       get()
     *         index    索引在范围内/不再范围内
     *       remove()
     *         m    指定备忘录在记录中/不在记录中
     *         observed by contains()
     *       contains()
     *         m    指定备忘录在记录中/不在记录中
     *       toTable()
     *         比较输出字符串
     */
    
  @Test
  public void testAdd() {
    AtomStructureParser ap = new AtomStructureParser();
    AtomStructure as = null;
    try {
      as = ap.initial("src/txt/AtomicStructure.txt");
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (as != null) {
      MementoCareTaker ct = new MementoCareTaker();
      Memento mt = as.backup();
      ct.add(mt);

      assertEquals("Excepted two memento equals", ct.get(0), mt);
    }
  }

  @Test
  public void testGet() {
    AtomStructureParser ap = new AtomStructureParser();
    AtomStructure as = null;
    try {
      as = ap.initial("src/txt/AtomicStructure.txt");
    } catch (Exception e) {
      e.printStackTrace();
    }
    MementoCareTaker ct = new MementoCareTaker();
    if (as != null) {
      Memento mt1 = as.backup();
      ct.add(mt1);

      as.transit(0, 1);
      Memento mt2 = as.backup();
      ct.add(mt2);

      assertEquals(mt2, ct.get(0));
      assertNull(ct.get(3));
    }
  }

  @Test
  public void testRemove() {
    AtomStructureParser ap = new AtomStructureParser();
    AtomStructure as = null;
    try {
      as = ap.initial("src/txt/AtomicStructure.txt");
    } catch (Exception e) {
      e.printStackTrace();
    }
    MementoCareTaker ct = new MementoCareTaker();

    if (as != null) {
      Memento mt1 = as.backup();
      ct.add(mt1);

      as.transit(0, 1);
      Memento mt2 = as.backup();
      ct.add(mt2);

      ct.remove(mt2);

      assertNull(ct.get(1));
    }
  }

  @Test
  public void testContains() {
    AtomStructureParser ap = new AtomStructureParser();
    AtomStructure as = null;
    try {
      as = ap.initial("src/txt/AtomicStructure.txt");
    } catch (Exception e) {
      e.printStackTrace();
    }
    MementoCareTaker ct = new MementoCareTaker();

    if (as != null) {
      Memento mt1 = as.backup();
      ct.add(mt1);

      as.transit(0, 1);
      Memento mt2 = as.backup();
      ct.add(mt2);

      assertTrue(ct.contains(mt2));
    }
  }

  @Test
  public void testToTable() {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");// 设置时间日期格式
    AtomStructureParser ap = new AtomStructureParser();
    AtomStructure as = null;
    try {
      as = ap.initial("src/txt/AtomicStructure.txt");
    } catch (Exception e) {
      e.printStackTrace();
    }
    MementoCareTaker ct = new MementoCareTaker();
    if (as != null) {
      Memento mt1 = as.backup();
      ct.add(mt1);

      as.transit(0, 1);
      Memento mt2 = as.backup();
      LocalDateTime dt1 = LocalDateTime.now();
      LocalDateTime dt2 = LocalDateTime.now();
      ct.add(mt2);

      as.transit(2, 3);
      String s = "+--------------------------------------------------------------------+\n"
          + "|Index\t|Time\t\t\t|Central\t|Tracks\t|ElectronNum\n"
          + "+--------------------------------------------------------------------+\n" 
          + "|1\t|" + dt1.format(dtf)
          + "\t|Rb\t\t|5\t|1/1;2/9;3/18;4/8;5/1;\n"
          + "+--------------------------------------------------------------------+\n" 
          + "|2\t|" + dt2.format(dtf)
          + "\t|Rb\t\t|5\t|1/2;2/8;3/18;4/8;5/1;\n"
          + "+--------------------------------------------------------------------+\n";
      assertEquals(s, ct.toTable());
    }
  }

}
