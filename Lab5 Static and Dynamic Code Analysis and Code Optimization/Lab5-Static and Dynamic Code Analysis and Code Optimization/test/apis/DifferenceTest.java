package apis;

import static org.junit.Assert.assertEquals;

import apis.Difference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;


/**.
 * Test Difference Class
 */
public class DifferenceTest {

  /*
     * Testing strategy:
     *       toString()
     *         构造一个Difference对象与正确输出对比
     *         不可区分物体的系统
     *         轨道单个物体
     *         轨道多个物体
     */
    
  @Test
  public void testGetObjectDifference() {
    Map<Integer, Integer> qd = new HashMap<Integer, Integer>();
    qd.put(0, 2);
    qd.put(1, 3);
    qd.put(2, -3);

    final Map<Integer, Set<String>> m1s = new HashMap<Integer, Set<String>>();
    final Map<Integer, Set<String>> m2s = new HashMap<Integer, Set<String>>();

    m1s.put(0, new HashSet<String>() {
      {
        add("A");
      }
    });
    m1s.put(1, new HashSet<String>() {
      {
        add("E");
      }
    });
    m1s.put(2, new HashSet<String>());
    m2s.put(0, new HashSet<String>() {
      {
        add("B");
      }
    });
    m2s.put(1, new HashSet<String>() {
      {
        add("C");
      }
    });
    m2s.put(2, new HashSet<String>() {
      {
        add("R");
      }
    });

    final Map<Integer, Set<String>> m1m = new HashMap<Integer, Set<String>>();
    final Map<Integer, Set<String>> m2m = new HashMap<Integer, Set<String>>();

    m1m.put(0, new HashSet<String>() {
      private static final long serialVersionUID = 1L;

      {
        add("A");
        add("T");
      }
    });
    m1m.put(1, new HashSet<String>() {
      {
        add("E");
        add("O");
      }
    });
    m1m.put(2, new HashSet<String>());
    m2m.put(0, new HashSet<String>() {
      {
        add("B");
      }
    });
    m2m.put(1, new HashSet<String>() {
      {
        add("C");
        add("P");
        add("U");
      }
    });
    m2m.put(2, new HashSet<String>() {
      {
        add("R");
        add("Q");
        add("N");
      }
    });

    String s1 = 
        "Track number difference: -1\n" + "Track 1 Objects number: 2\n" 
        + "Track 2 Objects number: 3\n"
        + "Track 3 Objects number: -3\n";
    String s2 = 
        "Track number difference: -1\n" + "Track 1 Objects number: 2; Objects: A-B\n"
        + "Track 2 Objects number: 3; Objects: E-C\n" 
        + "Track 3 Objects number: -3; Objects: None-R\n";
    String s3 = "Track number difference: -1\n" + "Track 1 Objects number: 2; Objects: [A, T]-B\n"
        + "Track 2 Objects number: 3; Objects: [E, O]-[P, C, U]\n"
        + "Track 3 Objects number: -3; Objects: None-[Q, R, N]\n";

    int t1 = 2;
    int t2 = 3;
    
    assertEquals(s1, new Difference(t1, t2, qd, m1m, m2m, false).toString());
    assertEquals(s2, new Difference(t1, t2, qd, m1s, m2s, true).toString());
    assertEquals(s3, new Difference(t1, t2, qd, m1m, m2m, true).toString());
  }

}
