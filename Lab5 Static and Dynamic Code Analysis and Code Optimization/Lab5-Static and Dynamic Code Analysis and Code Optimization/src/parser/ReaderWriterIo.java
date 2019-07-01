package parser;

import applications.tools.AppInstallTime;
import applications.tools.UsageLog;
import centralobject.Nucleus;
import circularorbit.CircularOrbit;
import circularorbit.Relation;
import java.io.FileReader;
import java.io.FileWriter;
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

/**.
 * 采用FileReader读取文本，使用FileWriter写回文本
 *
 */
public class ReaderWriterIo implements IoStrategy {

  @Override
  public String getText(String filePath) {
    StringBuilder sb = new StringBuilder();
    int len;
    try {
      final long startTime = System.currentTimeMillis();
      FileReader fr = new FileReader(filePath);
      char[] buf = new char[1024];
      while ((len = fr.read(buf)) != -1) {
        sb.append(new String(buf, 0, len));
      }
      fr.close();
      long endTime = System.currentTimeMillis();
      System.out.println("FileReader: " + (endTime - startTime));
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
      FileWriter fw = new FileWriter(filePath);
      fw.write("ElementName ::= " + co.getCentralPoint().getName() + "\n");
      fw.write("NumberOfTracks ::= " + co.getTrackNum() + "\n");
      
      StringBuilder sb = new StringBuilder("NumberOfElectron ::= ");
      int i = 0;
      for (i = 0; i < co.getTrackNum() - 1; i++) {
        sb.append((i + 1) + "/" + co.getObjects(i).size() + ";");
      }
      sb.append((i + 1) + "/" + co.getObjects(i).size());
      fw.write(sb.toString());
      long endTime = System.currentTimeMillis();
      System.out.println("FileWriter AtomStructure: " + (endTime - startTime));
      fw.close();
      return true;
    } catch (IOException e) {
      return false;
    }
  }

  @Override
  public boolean outPutFile(String filePath, TrackGameManager tgm) throws IOException {
    try {
      final long startTime = System.currentTimeMillis();
      FileWriter fw = new FileWriter(filePath);
      fw.write("Game ::= " + tgm.getRaceType() + "\n");
      
      fw.write("NumOfTracks ::= " + String.valueOf(tgm.getTrackNumber()) + "\n");
      
      DecimalFormat df = new DecimalFormat("#.00"); //对于成绩进行格式化输出
      Set<Athlete> athletes = tgm.getAthletes();
      for (Athlete a : athletes) {
        fw.write("Athlete ::= <" + a.getName() + "," + a.getNumber()
                                + "," + a.getNation() + "," + a.getAge() 
                                + "," + df.format(a.getPersonalBest()) + ">\n");
      }
      long endTime = System.currentTimeMillis();
      System.out.println("FileWriter TrackGame: " + (endTime - startTime));
      fw.close();
      return true;
    } catch (IOException e) {
      throw e;
    }
  }

  @Override
  public boolean outPutFile(String filePath, PersonalAppManager pam) throws IOException {
    try {
      final long startTime = System.currentTimeMillis();
      FileWriter fw = new FileWriter(filePath);
      fw.write("User ::= " + pam.getUser() + "\n\n");
      
      fw.write("Period ::= " + String.valueOf(pam.getPeriod()) + "\n\n");
      //Add Application name
      Set<App> apps = pam.getApps();
      for (App a : apps) {
        fw.write("App ::= <" + a.getName() + "," + a.getCompany()
                                + "," + a.getVersion() + "," + a.getDescription() 
                                + "," + a.getArea() + ">\n");
      }
      fw.write("\n");
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
        fw.write(s + "\n");
      }
      fw.write("\n");
      for (String s : uninstalllog) {
        fw.write(s + "\n");
      }
      fw.write("\n");
      //Add usageLog
      List<UsageLog> usageLog = pam.getUsageLog();
      for (UsageLog u : usageLog) {
        fw.write("UsageLog ::= <" 
                + dtf.format(u.getTime()) 
                + "," + u.getName() 
                + "," + u.getDuration() + ">\n");
      }
      fw.write("\n");
      List<Relation<String>> relation = pam.getAllRelation();
      //Add Relation
      for (Relation<String> r : relation) {
        fw.write("Relation ::= <" 
                + r.getOne()
                + "," + r.getOther(r.getOne()) + ">\n");
      }
      fw.write("\n");
      long endTime = System.currentTimeMillis();
      System.out.println("FileWriter PersonalAppEcosystem: " + (endTime - startTime));
      fw.close();
      return true;
    } catch (IOException e) {
      throw e;
    }
  }

}
