package physicalobject;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**.
 * 测试Athlete类
 *
 */
public class AthleteTest {
  /*
     * 输入划分为 Athlete() 输入正确参数 observed by getName(),
     * getNumber(),getNation(),getAge(),getPb()
     * 
     */
  @Test
  public void testAthlete() {
    Athlete a = new Athlete("JackMa", 98, "CHN", 33, 9.33);

    assertEquals("JackMa", a.getName());
    assertEquals(98, a.getNumber());
    assertEquals("CHN", a.getNation());
    assertEquals(33, a.getAge());
    assertEquals(9.33, a.getPersonalBest(), 0);
  }

  @Test
  public void testToString() {
    Athlete a = new Athlete("JackMa", 98, "CHN", 33, 9.33);

    assertEquals(
        "[Athlete: name:JackMa, age:33, number:98, nation:CHN, personal best:9.33]", a.toString());
  }

}
