package parser;

import applications.tools.AppInstallTime;
import applications.tools.UsageLog;
import centralobject.Nucleus;
import circularorbit.CircularOrbit;
import circularorbit.Relation;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import manager.PersonalAppManager;
import manager.TrackGameManager;
import physicalobject.App;
import physicalobject.Athlete;
import physicalobject.Electron;

/**
 *.使用FileChannel，ByteBuffer读取和写回文本
 *
 */
public class ChannelIo implements IoStrategy {

  @Override
  public String getText(String filePath) {
    final long startTime = System.currentTimeMillis();
    StringBuilder sb = new StringBuilder();
    try {
      ReadableByteChannel rbc = Channels.newChannel(new FileInputStream(filePath));
      ByteBuffer buf = ByteBuffer.allocate(8192);
      int len;
      while (true) {
        buf.clear();
        if ((len = rbc.read(buf)) == -1) {
          break;
        }
        buf.flip();
        sb.append(new String(buf.array(), 0, len));
      }
      long endTime = System.currentTimeMillis();
      System.out.println("ChannelRead: " + (endTime - startTime));
      rbc.close();
      return sb.toString();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }


  @Override
  public boolean outPutFile(String filePath, TrackGameManager tgm) throws IOException {
    try {
      final long startTime = System.currentTimeMillis();
      FileOutputStream fos = new FileOutputStream(new File(filePath));
      FileChannel fc = fos.getChannel();
      ByteBuffer buf = ByteBuffer.allocate(8192);
      buf.put(("Game ::= " + tgm.getRaceType() + "\n").getBytes());
      buf.flip();
      fc.write(buf);
      buf.clear();
      
      buf.put(("NumOfTracks ::= " + String.valueOf(tgm.getTrackNumber()) + "\n").getBytes());
      buf.flip();
      fc.write(buf);
      buf.clear();
      
      DecimalFormat df = new DecimalFormat("#.00"); //对于成绩进行格式化输出
      Set<Athlete> athletes = tgm.getAthletes();
      for (Athlete a : athletes) {
        buf.put(("Athlete ::= <" + a.getName() + "," + a.getNumber()
                                + "," + a.getNation() + "," + a.getAge() 
                                + "," + df.format(a.getPersonalBest()) + ">\n").getBytes());
        buf.flip();
        fc.write(buf);
        buf.clear();
      }
      long endTime = System.currentTimeMillis();
      System.out.println("ChannelWrite TrackGame: " + (endTime - startTime));
      fos.close();
      return true;
    } catch (IOException e) {
      throw e;
    }
  }

  @Override
  public boolean outPutFile(String filePath, PersonalAppManager pam) throws IOException {
    try {
      final long startTime = System.currentTimeMillis();
      FileOutputStream fos = new FileOutputStream(new File(filePath));
      FileChannel fc = fos.getChannel();
      ByteBuffer buf = ByteBuffer.allocate(8192);
      buf.put(("User ::= " + pam.getUser() + "\n\n").getBytes());
      buf.flip();
      fc.write(buf);
      buf.clear();
      
      buf.put(("Period ::= " + String.valueOf(pam.getPeriod()) + "\n\n").getBytes());
      buf.flip();
      fc.write(buf);
      buf.clear();
      
      //Add Application name
      Set<App> apps = pam.getApps();
      for (App a : apps) {
        buf.put(("App ::= <" + a.getName() + "," + a.getCompany()
                                + "," + a.getVersion() + "," + a.getDescription() 
                                + "," + a.getArea() + ">\n").getBytes());
        buf.flip();
        fc.write(buf);
        buf.clear();
      }
      buf.put("\n".getBytes());
      buf.flip();
      fc.write(buf);
      buf.clear();
      //Add installLog and uninstallLog
      List<String> installlog = new LinkedList<String>();
      List<String> uninstalllog = new LinkedList<String>();
      DateTimeFormatter dtf = DateTimeFormatter
                                .ofPattern("yyyy-MM-dd,HH:mm:ss")
                                .withZone(ZoneOffset.UTC);// 设置时间日期格式和零起点
      Map<String, AppInstallTime> appsInstall = pam.getAllInstallTime();
      for (App a : apps) {
        AppInstallTime span = appsInstall.get(a.getName());
        int size = span.size();
        for (int i = 0; i < size; i++) {
          installlog.add("InstallLog ::= <" 
              + dtf.format(span.get(i).getStart())
              + "," + a.getName() + ">");
        }
        for (int i = 0; i < size; i++) {
          if (span.get(i).getEnd().equals(Instant.MAX)) {
            continue;
          }
          uninstalllog.add("UninstallLog ::= <" 
                + dtf.format(span.get(i).getEnd()) 
                + "," + a.getName() + ">");
        }
      }
      for (String s : installlog) {
        buf.put((s + "\n").getBytes());
        buf.flip();
        fc.write(buf);
        buf.clear();
      }
      buf.put("\n".getBytes());
      buf.flip();
      fc.write(buf);
      buf.clear();
      for (String s : uninstalllog) {
        buf.put((s + "\n").getBytes());
        buf.flip();
        fc.write(buf);
        buf.clear();
      }
      buf.put("\n".getBytes());
      buf.flip();
      fc.write(buf);
      buf.clear();
      //Add usageLog
      List<UsageLog> usageLog = pam.getUsageLog();
      for (UsageLog u : usageLog) {
        buf.put(("UsageLog ::= <" 
                + dtf.format(u.getTime()) 
                + "," + u.getName() 
                + "," + u.getDuration() + ">\n").getBytes());
        buf.flip();
        fc.write(buf);
        buf.clear();
      }
      buf.put("\n".getBytes());
      buf.flip();
      fc.write(buf);
      buf.clear();
      List<Relation<String>> relation = pam.getAllRelation();
      //Add Relation
      for (Relation<String> r : relation) {
        buf.put(("Relation ::= <" 
                + r.getOne()
                + "," + r.getOther(r.getOne()) + ">\n").getBytes());
        buf.flip();
        fc.write(buf);
        buf.clear();
      }
      buf.put("\n".getBytes());
      buf.flip();
      fc.write(buf);
      buf.clear();
      long endTime = System.currentTimeMillis();
      System.out.println("ChannelWrite PersonalAppEocysystem: " + (endTime - startTime));
      fos.close();
      return true;
    } catch (IOException e) {
      throw e;
    }
  }

  @Override
  public boolean outPutFile(String filePath, CircularOrbit<Nucleus, Electron> co) {
    try {
      final long startTime = System.currentTimeMillis();
      FileOutputStream fos = new FileOutputStream(new File(filePath));
      FileChannel fc = fos.getChannel();
      ByteBuffer buf = ByteBuffer.allocate(8192);
      buf.put(("ElementName ::= " + co.getCentralPoint().getName() + "\n").getBytes());
      buf.flip();
      fc.write(buf);
      buf.clear();
      
      buf.put(("NumberOfTracks ::= " + co.getTrackNum() + "\n").getBytes());
      buf.flip();
      fc.write(buf);
      buf.clear();
      
      StringBuilder sb = new StringBuilder("NumberOfElectron ::= ");
      int i = 0;
      for (i = 0; i < co.getTrackNum() - 1; i++) {
        sb.append((i + 1) + "/" + co.getObjects(i).size() + ";");
      }
      sb.append((i + 1) + "/" + co.getObjects(i).size());
      buf.put(sb.toString().getBytes());
      buf.flip();
      fc.write(buf);
      buf.clear();
      long endTime = System.currentTimeMillis();
      System.out.println("ChannelWrite AtomStructure: " + (endTime - startTime));
      fos.close();
      return true;
    } catch (IOException e) {
      return false;
    }
  }

}
