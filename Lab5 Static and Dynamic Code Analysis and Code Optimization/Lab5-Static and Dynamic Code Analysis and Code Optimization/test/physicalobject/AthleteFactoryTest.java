package physicalobject;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**.
 * 测试AthleteFactory类
 *
 */
public class AthleteFactoryTest {
  /*
     * 输入划分为：正确输入，测试生成对象是否与正确对象相同
     */
  @Test
  public void testCreateProduct() {
    AthleteFactory af = new AthleteFactory();
    Athlete a = new Athlete("Bolt", 1, "JAM", 38, 9.94);
    String s = "Athlete ::= <Bolt,1,JAM,38,9.94>";
    Athlete b = (Athlete) af.create(s);

    assertTrue(a.equals(b));
  }
}
