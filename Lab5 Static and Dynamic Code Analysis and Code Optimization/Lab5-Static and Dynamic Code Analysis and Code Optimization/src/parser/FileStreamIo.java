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
 * .使用FileInputStream和FileOutputStream读取和写回文本
 *
 */
public class FileStreamIo implements IoStrategy {

  @Override
  public String getText(String filePath) {
    try {
      final long startTime = System.currentTimeMillis();
      FileInputStream fis = new FileInputStream(filePath);
      StringBuilder sb = new StringBuilder();
      int len = 0;
      byte[] buf = new byte[8192]; //Buffer Size
      while ((len = fis.read(buf)) != -1) {
        sb.append(new String(buf, 0, len));
      }
      fis.close();
      long endTime = System.currentTimeMillis();
      System.out.println("FileInputStream: " + (endTime - startTime));
      return sb.toString();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }


  @Override
  public boolean outPutFile(String filePath, CircularOrbit<Nucleus, Electron> co) {
    try {
      final long startTime = System.currentTimeMillis();
      FileOutputStream fos = new FileOutputStream(new File(filePath));
      fos.write((("ElementName ::= " + co.getCentralPoint().getName() + "\n").getBytes()));
      fos.write(("NumberOfTracks ::= " + co.getTrackNum() + "\n").getBytes());
      
      StringBuilder sb = new StringBuilder("NumberOfElectron ::= ");
      int i = 0;
      for (i = 0; i < co.getTrackNum() - 1; i++) {
        sb.append((i + 1) + "/" + co.getObjects(i).size() + ";");
      }
      sb.append((i + 1) + "/" + co.getObjects(i).size());
      fos.write(sb.toString().getBytes());
      long endTime = System.currentTimeMillis();
      System.out.println("FileOutputStream AtomStructure: " + (endTime - startTime));
      fos.close();
      return true;
    } catch (IOException e) {
      return false;
    }
  }

  @Override
  public boolean outPutFile(String filePath, TrackGameManager tgm) throws IOException {
    try {
      final long startTime = System.currentTimeMillis();
      FileOutputStream fos = new FileOutputStream(new File(filePath));
      fos.write(("Game ::= " + tgm.getRaceType() + "\n").getBytes());
      
      fos.write(("NumOfTracks ::= " + String.valueOf(tgm.getTrackNumber()) + "\n").getBytes());
      
      DecimalFormat df = new DecimalFormat("#.00"); //对于成绩进行格式化输出
      Set<Athlete> athletes = tgm.getAthletes();
      for (Athlete a : athletes) {
        fos.write(("Athlete ::= <" + a.getName() + "," + a.getNumber()
                                + "," + a.getNation() + "," + a.getAge() 
                                + "," + df.format(a.getPersonalBest()) + ">\n").getBytes());
      }
      long endTime = System.currentTimeMillis();
      System.out.println("FileOutputStream TrackGame: " + (endTime - startTime));
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
      fos.write(("User ::= " + pam.getUser() + "\n\n").getBytes());
      
      fos.write(("Period ::= " + String.valueOf(pam.getPeriod()) + "\n\n").getBytes());
      //Add Application name
      Set<App> apps = pam.getApps();
      for (App a : apps) {
        fos.write(("App ::= <" + a.getName() + "," + a.getCompany()
                                + "," + a.getVersion() + "," + a.getDescription() 
                                + "," + a.getArea() + ">\n").getBytes());
      }
      fos.write("\n".getBytes());
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
        fos.write((s + "\n").getBytes());
      }
      fos.write("\n".getBytes());
      for (String s : uninstalllog) {
        fos.write((s + "\n").getBytes());
      }
      fos.write("\n".getBytes());
      //Add usageLog
      List<UsageLog> usageLog = pam.getUsageLog();
      for (UsageLog u : usageLog) {
        fos.write(("UsageLog ::= <" 
                + dtf.format(u.getTime()) 
                + "," + u.getName() 
                + "," + u.getDuration() + ">\n").getBytes());
      }
      fos.write("\n".getBytes());
      List<Relation<String>> relation = pam.getAllRelation();
      //Add Relation
      for (Relation<String> r : relation) {
        fos.write(("Relation ::= <" 
                + r.getOne()
                + "," + r.getOther(r.getOne()) + ">\n").getBytes());
      }
      fos.write("\n".getBytes());
      long endTime = System.currentTimeMillis();
      System.out.println("FileOutputStream PersonalAppEcosystem: " + (endTime - startTime));
      fos.close();
      return true;
    } catch (IOException e) {
      throw e;
    }
  }

}
