package track;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**.
 * 测试跑道工厂类方法
 *
 */
public class RaceTrackFactoryTest {
    
  /*
     * 测试策略
     *     create()
     *         输入正确参数，测试对象编号是否正确
     */
  @Test
  public void testFactory() {
    RaceTrackFactory rtf = new RaceTrackFactory();
    Track t1 = rtf.create(1);
    Track t2 = rtf.create(2);

    assertEquals(1, t1.getNumber());
    assertEquals(2, t2.getNumber());
  }
}
