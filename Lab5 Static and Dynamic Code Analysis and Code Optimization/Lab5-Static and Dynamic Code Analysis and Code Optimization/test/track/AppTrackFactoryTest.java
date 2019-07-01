package track;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**.
 * 测试App轨道工厂类
 *
 */
public class AppTrackFactoryTest {

  /*
     * 测试策略
     *     create()
     *         输入正确参数，测试对象编号是否正确
     */
  @Test
  public void testFactory() {
    AppTrackFactory atf = new AppTrackFactory();
    Track t1 = atf.create(1);
    Track t2 = atf.create(2);

    assertEquals(1, t1.getNumber());
    assertEquals(2, t2.getNumber());
  }

}
