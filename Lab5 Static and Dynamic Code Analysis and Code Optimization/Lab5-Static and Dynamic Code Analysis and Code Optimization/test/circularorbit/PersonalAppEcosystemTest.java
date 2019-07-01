package circularorbit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import applications.tools.AppInstallTime;
import applications.tools.Timespan;
import centralobject.Person;
import circularorbit.PersonalAppEcosystem;
import java.time.Instant;

import org.junit.Test;

import physicalobject.App;
import physicalobject.AppFactory;

/**.
 * Test PersonalAppEcosystem
 *
 */
public class PersonalAppEcosystemTest {
  private static final Instant d3 = Instant.parse("2016-02-18T12:00:00Z");
  private static final Instant d7 = Instant.parse("2016-02-18T12:58:00Z");

  private static final Instant d8 = Instant.parse("2016-02-18T12:50:00Z");
  private static final Instant d5 = Instant.parse("2016-02-18T13:00:00Z");
  Timespan timespan = new Timespan(d8, d5);
     
  /*
     * Testing strategy:
     *       addInstalling()
     *         appName    在系统中/不在系统中
     * 
     *       getApp()
     *         name    在系统中/不在系统中
     * 
     *       remove()
     *         app        在系统中/不在系统中
     *         index    在范围内/不在范围内
     */
  @Test
  public void testAddIntalling() {
    PersonalAppEcosystem pae = new PersonalAppEcosystem("a1", timespan);
    pae.addCentralPoint(new Person("AN"));
    AppFactory af = new AppFactory();
    final App a = (App) af
        .create("App ::= <Wechat,Tencent,13.2,"
            + "\"The most popular social networking App in China\",\"Social network\">");
    AppInstallTime at = new AppInstallTime();
    at.add(timespan);
    final App b = (App) af
        .create("App ::= <QQ,Tencent,29.2,"
            + "\"The second popular social networking App in China\",\"Social network\">");
    AppInstallTime bt = new AppInstallTime();
    bt.add(timespan);
    final App c = (App) af.create(
        "App ::= <Weibo,Sina,v0.2.3.4,"
        + "\"The third popular social networking App in China\",\"Social network\">");
    AppInstallTime ct = new AppInstallTime();
    ct.add(timespan);
    final App d = (App) af.create("App ::= <Didi,Didi,ver03.32,"
        + "\"The most popular car sharing App in China\",\"Travel\">");
    AppInstallTime dt = new AppInstallTime();
    dt.add(timespan);
    pae.addTrack(1);
    pae.addTrack(2);
    pae.addTrack(3);
    pae.addApp(a, 0, at);
    pae.addApp(b, 1, bt);
    pae.addApp(c, 2, ct);
    pae.addApp(d, 2, dt);

    AppInstallTime tt = new AppInstallTime();
    tt.add(new Timespan(d3, d7));

    assertFalse(pae.addInstalling("MOMO", tt));
    assertTrue(pae.addInstalling("QQ", tt));
    assertEquals(tt, pae.getInstalling("QQ"));
  }

  @Test
  public void getApp() {
    PersonalAppEcosystem pae = new PersonalAppEcosystem("a1", timespan);
    pae.addCentralPoint(new Person("AN"));
    AppFactory af = new AppFactory();
    final App a = (App) af
        .create("App ::= <Wechat,Tencent,13.2,"
            + "\"The most popular social networking App in China\",\"Social network\">");
    AppInstallTime at = new AppInstallTime();
    at.add(timespan);
    final App b = (App) af
        .create("App ::= <QQ,Tencent,29.2,"
            + "\"The second popular social networking App in China\",\"Social network\">");
    AppInstallTime bt = new AppInstallTime();
    bt.add(timespan);
    final App c = (App) af.create(
        "App ::= <Weibo,Sina,v0.2.3.4,"
        + "\"The third popular social networking App in China\",\"Social network\">");
    AppInstallTime ct = new AppInstallTime();
    ct.add(timespan);
    final App d = (App) af.create("App ::= <Didi,Didi,ver03.32,"
        + "\"The most popular car sharing App in China\",\"Travel\">");
    AppInstallTime dt = new AppInstallTime();
    dt.add(timespan);
    pae.addTrack(1);
    pae.addTrack(2);
    pae.addTrack(3);
    pae.addApp(a, 0, at);
    pae.addApp(b, 1, bt);
    pae.addApp(c, 2, ct);
    pae.addApp(d, 2, dt);

    assertNull(pae.getApp("MOMO"));
    assertEquals(a, pae.getApp("Wechat"));
  }

  @Test
  public void testRemove() {
    PersonalAppEcosystem pae = new PersonalAppEcosystem("a1", timespan);
    pae.addCentralPoint(new Person("AN"));
    AppFactory af = new AppFactory();
    final App a = (App) af
        .create("App ::= <Wechat,Tencent,13.2,"
            + "\"The most popular social networking App in China\",\"Social network\">");
    AppInstallTime at = new AppInstallTime();
    at.add(timespan);
    final App b = (App) af
        .create("App ::= <QQ,Tencent,29.2,"
            + "\"The second popular social networking App in China\",\"Social network\">");
    AppInstallTime bt = new AppInstallTime();
    bt.add(timespan);
    final App c = (App) af.create(
        "App ::= <Weibo,Sina,v0.2.3.4,"
        + "\"The third popular social networking App in China\",\"Social network\">");
    AppInstallTime ct = new AppInstallTime();
    ct.add(timespan);
    final App d = (App) af.create("App ::= <Didi,Didi,ver03.32,"
        + "\"The most popular car sharing App in China\",\"Travel\">");
    AppInstallTime dt = new AppInstallTime();
    dt.add(timespan);
    pae.addTrack(1);
    pae.addTrack(2);
    pae.addTrack(3);
    pae.addApp(a, 0, at);
    pae.addApp(b, 1, bt);
    pae.addApp(c, 2, ct);
    pae.addApp(d, 2, dt);

    pae.remove(a, 0);
    assertFalse(pae.contains(a, 0));
    pae.remove(b);
    assertFalse(pae.contains(b, 1));
    pae.remove(2);
    assertFalse(pae.contains(c));
  }

  @Test
  public void testToString() {
    PersonalAppEcosystem pae = new PersonalAppEcosystem("a1", timespan);
    pae.addCentralPoint(new Person("AN"));
    AppFactory af = new AppFactory();
    final App a = (App) af
        .create("App ::= <Wechat,Tencent,13.2,"
            + "\"The most popular social networking App in China\",\"Social network\">");
    AppInstallTime at = new AppInstallTime();
    at.add(timespan);
    final App b = (App) af
        .create("App ::= <QQ,Tencent,29.2,"
            + "\"The second popular social networking App in China\",\"Social network\">");
    AppInstallTime bt = new AppInstallTime();
    bt.add(timespan);
    final App c = (App) af.create(
        "App ::= <Weibo,Sina,v0.2.3.4,"
        + "\"The third popular social networking App in China\",\"Social network\">");
    AppInstallTime ct = new AppInstallTime();
    ct.add(timespan);
    final App d = (App) af.create("App ::= <Didi,Didi,ver03.32,"
        + "\"The most popular car sharing App in China\",\"Travel\">");
    AppInstallTime dt = new AppInstallTime();
    dt.add(timespan);
    pae.addTrack(1);
    pae.addTrack(2);
    pae.addTrack(3);
    pae.addApp(a, 0, at);
    pae.addApp(b, 1, bt);
    pae.addApp(c, 2, ct);
    pae.addApp(d, 2, dt);

    assertEquals("[PersonalAppEcosystem:a1, Duration:"
        + "[2016-02-18T12:50:00Z...2016-02-18T13:00:00Z], TrackNumber:3]\n"
        + "[Apps:[Track1: 1][Wechat,]\n" + "[Track2: 1][QQ,]\n" 
        + "[Track3: 2][Weibo,Didi,]\n", pae.toString());
  }
}
