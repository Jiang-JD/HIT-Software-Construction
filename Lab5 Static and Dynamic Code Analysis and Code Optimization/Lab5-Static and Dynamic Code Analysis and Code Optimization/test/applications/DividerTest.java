package applications;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import applications.tools.Divider;
import applications.tools.DividerAscendingOrder;
import applications.tools.DividerRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import physicalobject.Athlete;
import physicalobject.AthleteFactory;

/**.
 * 测试运动员分组的Divider接口
 * 有两种策略：随机和按成绩分组
 */
public class DividerTest {

  /*
     * 输入划分
     *     DividerRandom()
     *         输入一组运动员集合，测试划分是否合法
     *     DividerAscendingOrder()
     *         输入一组运动员集合，测试划分是否按照成绩升序排列，以及是否符合分组规则
     */
    
  /*
     * 测试策略
     *       由于是随机分组，所以只要满足基本规则就可以
     */
  @Test
  public void testRandomDivider() {
    Divider d = new DividerRandom();
    List<Athlete> l = new ArrayList<Athlete>();
    AthleteFactory af = new AthleteFactory();
    l.add((Athlete) af.create("Athlete ::= <Bolt,1,JAM,38,9.94>"));
    l.add((Athlete) af.create("Athlete ::= <Lewis,2,USA,31,10.00>"));
    l.add((Athlete) af.create("Athlete ::= <Ronaldo,10,CNH,20,9.85>"));
    l.add((Athlete) af.create("Athlete ::= <Wei,13,JPN,40,9.95>"));

    int tracknum = 3;
    List<List<Athlete>> group = d.grouping(tracknum, l);

    boolean divide = true;
    if (!group.isEmpty()) {
      int size = group.get(0).size(); // 每一小组的大小
      for (int i = 0; i < group.size() - 1; i++) {
        if (size != group.get(i).size()) {
          divide = false;
          break;
        }
      }
      if (size < group.get(group.size() - 1).size()) {
        divide = false;
      }
    }
    assertTrue("分组格式出错，存在组之间人数不等或最后一组人数超标", divide);
  }

  @Test
  public void testAscendingOrder() {
    final Divider d = new DividerAscendingOrder();
    List<Athlete> l = new ArrayList<Athlete>();
    AthleteFactory af = new AthleteFactory();
    Athlete a1 = (Athlete) af.create("Athlete ::= <Bolt,1,JAM,38,9.94>");
    Athlete a2 = (Athlete) af.create("Athlete ::= <Lewis,2,USA,31,10.00>");
    Athlete a3 = (Athlete) af.create("Athlete ::= <Ronaldo,10,CNH,20,9.85>");
    Athlete a4 = (Athlete) af.create("Athlete ::= <Wei,13,JPN,40,9.95>");
    Athlete a5 = (Athlete) af.create("Athlete ::= <Chen,7,KOR,29,10.12>");
    Athlete a6 = (Athlete) af.create("Athlete ::= <Park,12,USA,28,10.01> ");
    Athlete a7 = (Athlete) af.create("Athlete ::= <Trump,9,CHN,19,9.89>");
    l.add(a1);
    l.add(a2);
    l.add(a3);
    l.add(a4);
    l.add(a5);
    l.add(a6);
    l.add(a7);

    List<List<Athlete>> correct = new ArrayList<List<Athlete>>();
    correct.add(Arrays.asList(a2, a6, a5));
    correct.add(Arrays.asList(a7, a1, a4));
    correct.add(Arrays.asList(a3));

    int tracknum = 3;
    List<List<Athlete>> group = d.grouping(tracknum, l);
    assertEquals("Excepted Equals", correct, group);
  }

}
