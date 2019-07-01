package applications;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import applications.tools.AppInstallTime;
import applications.tools.Timespan;
import java.time.Instant;

import org.junit.Test;



/**.
 * Testing AppTimeUage class
 *
 */
public class AppInstallTimeTest {

  private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
  private static final Instant d6 = Instant.parse("2016-02-17T10:30:00Z");
  private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
  private static final Instant d3 = Instant.parse("2016-02-18T12:00:00Z");
  private static final Instant d7 = Instant.parse("2016-02-18T12:15:00Z");
  private static final Instant d4 = Instant.parse("2016-02-18T12:30:00Z");
  private static final Instant d8 = Instant.parse("2016-02-18T12:50:00Z");
  private static final Instant d5 = Instant.parse("2016-02-18T13:00:00Z");

  Timespan t1 = new Timespan(d1, d6);
  Timespan t2 = new Timespan(d8, d5);
  Timespan t3 = new Timespan(d6, d2);
  Timespan t4 = new Timespan(d4, d8);
  Timespan t5 = new Timespan(d1, d5);
  Timespan t6 = new Timespan(d3, d7);
  Timespan t7 = new Timespan(d6, d3);
  Timespan t8 = new Timespan(d7, d8); // spotbug 不修改
    
  /*
     * Testing strategy:
     *       add()
     *         ts    添加有重叠时间段/不重叠时间段
     *       overlap()
     *         ts    有重叠/无重叠
     *       entireSpan()
     *           比较正确timespan,检查是否获取全范围
     */
    
  @Test
  public void testAdd() {
    AppInstallTime atu = new AppInstallTime();
    atu.add(t2);
    atu.add(t3);

    assertTrue(atu.add(t6));
    assertFalse(atu.add(t5));
  }

  @Test
  public void testOverlap() {
    AppInstallTime atu = new AppInstallTime();
    atu.add(t2);
    atu.add(t3);
    atu.add(t6);

    assertTrue(atu.overlap(new Timespan(d2, d4)));
    assertFalse(atu.overlap(t1));
  }

  @Test
  public void testEntireSpan() {
    AppInstallTime atu = new AppInstallTime();
    atu.add(t2);
    atu.add(t3);
    atu.add(t6);

    assertEquals(new Timespan(d6, d5), atu.entireSpan());
  }

  @Test
  public void testToString() {
    AppInstallTime atu = new AppInstallTime();
    atu.add(t2);
    atu.add(t3);
    atu.add(t6);

    assertEquals(
        "[[2016-02-18T12:50:00Z...2016-02-18T13:00:00Z], "
        + "[2016-02-17T10:30:00Z...2016-02-17T11:00:00Z], "
        + "[2016-02-18T12:00:00Z...2016-02-18T12:15:00Z]]",
        atu.toString());
  }

}
