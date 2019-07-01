package physicalobject;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**.
 * APP测试类
 *
 */
public class AppTest {

  /*
     * 输入划分为 App() 
     *      输入正确参数 observed by getName(), getCompany(),getVersion(),getDescription(),getArea()
     * 
     */
  @Test
  public void testApp() {
    App a = new App("QQ", "Tencent", "29.2", 
        "\"The second popular social networking App in China\"",
        "\"Social network\"");

    assertEquals("QQ", a.getName());
    assertEquals("Tencent", a.getCompany());
    assertEquals("29.2", a.getVersion());
    assertEquals("\"The second popular social networking App in China\"", a.getDescription());
    assertEquals("\"Social network\"", a.getArea());
  }

  @Test
  public void testToString() {
    App a = new App("QQ", "Tencent", "29.2", 
        "\"The second popular social networking App in China\"",
        "\"Social network\"");

    assertEquals(
        "[APP name: QQ, company: Tencent, version: 29.2, description: "
        + "\"The second popular social networking App in China\", area: \"Social network\"]",
        a.toString());
  }

}
