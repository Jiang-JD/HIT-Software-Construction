package track;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**.
 * 电子轨道工厂测试类
 *
 */
public class ElectronTrackFactoryTest {

  /*
     * 测试策略
     *     create()
     *         输入正确参数，测试对象编号是否正确
     */
  @Test
  public void testFactory() {
    ElectronTrackFactory etf = new ElectronTrackFactory();
    Track t1 = etf.create(1);
    Track t2 = etf.create(2);

    assertEquals(1, t1.getNumber());
    assertEquals(2, t2.getNumber());
  }

}
