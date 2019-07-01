package parser;

import applications.tools.AppInstallTime;
import applications.tools.UsageLog;
import centralobject.Nucleus;
import circularorbit.CircularOrbit;
import circularorbit.Relation;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
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
 * 使用BufferReader读取文本，使用bufferWriter写回文本
 *
 */
public class BufferIo implements IoStrategy {

  @Override
  public String getText(String filePath) {
    try {
      long startTime = System.currentTimeMillis();
      BufferedReader br = new BufferedReader(new FileReader(filePath));
      StringBuilder sb = new StringBuilder();
      String s;
      while ((s = br.readLine()) != null) {
        sb.append(s).append("\n");
      }
      br.close();
      long endTime = System.currentTimeMillis();
      System.out.println("BufferedReader: " + (endTime - startTime));
      return sb.toString();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public boolean outPutFile(String filePath, CircularOrbit<Nucleus, Electron> co) 
                                                                  throws IOException {
    try {
      final long startTime = System.currentTimeMillis();
      File file = new File(filePath);
      FileOutputStream fos = new FileOutputStream(file);
      BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
      bw.write("ElementName ::= " + co.getCentralPoint().getName());
      bw.newLine();
      bw.write("NumberOfTracks ::= " + co.getTrackNum());
      bw.newLine();
      StringBuilder sb = new StringBuilder("NumberOfElectron ::= ");
      int i = 0;
      for (i = 0; i < co.getTrackNum() - 1; i++) {
        sb.append((i + 1) + "/" + co.getObjects(i).size() + ";");
      }
      sb.append((i + 1) + "/" + co.getObjects(i).size());
      bw.write(sb.toString());
      long endTime = System.currentTimeMillis();
      System.out.println("BufferedWriter AtomStructure: " + (endTime - startTime));
      bw.close();
      return true;
    } catch (IOException e) {
      throw e;
    }
  }

  @Override
  public boolean outPutFile(String filePath, TrackGameManager tgm) throws IOException {
    try {
      final long startTime = System.currentTimeMillis();
      File file = new File(filePath);
      FileOutputStream fos = new FileOutputStream(file);
      BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
      bw.write("Game ::= " + tgm.getRaceType());
      bw.newLine();
      bw.write("NumOfTracks ::= " + String.valueOf(tgm.getTrackNumber()));
      bw.newLine();
      DecimalFormat numberdf = new DecimalFormat("#.###E0####"); //对于 >= 10000的数字进行科学记数法输出
      DecimalFormat df = new DecimalFormat("#.00"); //对于成绩进行格式化输出
      Set<Athlete> athletes = tgm.getAthletes();
      for (Athlete a : athletes) {
        bw.write("Athlete ::= <" + a.getName() + "," + a.getNumber()
                                + "," + a.getNation() + "," + a.getAge() 
                                + "," + df.format(a.getPersonalBest()) + ">");
        bw.newLine();
      }
      long endTime = System.currentTimeMillis();
      System.out.println("BufferedWriter TrackGame: " + (endTime - startTime));
      bw.close();
      return true;
    } catch (IOException e) {
      throw e;
    }
  }

  @Override
  public boolean outPutFile(String filePath, PersonalAppManager pam) throws IOException {
    try {
      final long startTime = System.currentTimeMillis();
      File file = new File(filePath);
      FileOutputStream fos = new FileOutputStream(file);
      BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
      bw.write("User ::= " + pam.getUser());
      bw.newLine();
      bw.newLine(); 
      bw.write("Period ::= " + String.valueOf(pam.getPeriod()));
      bw.newLine();
      bw.newLine();
      //Add Application name
      Set<App> apps = pam.getApps();
      for (App a : apps) {
        bw.write("App ::= <" + a.getName() + "," + a.getCompany()
                                + "," + a.getVersion() + "," + a.getDescription() 
                                + "," + a.getArea() + ">");
        bw.newLine();
      }
      bw.newLine();
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
        bw.write(s);
        bw.newLine();
      }
      bw.newLine();
      for (String s : uninstalllog) {
        bw.write(s);
        bw.newLine();
      }
      bw.newLine();
      //Add usageLog
      List<UsageLog> usageLog = pam.getUsageLog();
      for (UsageLog u : usageLog) {
        bw.write("UsageLog ::= <" 
                + dtf.format(u.getTime()) 
                + "," + u.getName() 
                + "," + u.getDuration() + ">");
        bw.newLine();
      }
      bw.newLine();
      List<Relation<String>> relation = pam.getAllRelation();
      //Add Relation
      for (Relation<String> r : relation) {
        bw.write("Relation ::= <" 
                + r.getOne()
                + "," + r.getOther(r.getOne()) + ">");
        bw.newLine();
      }
      long endTime = System.currentTimeMillis();
      System.out.println("BufferedWriter PersonalAppEcosystem: " + (endTime - startTime));
      bw.close();
      return true;
    } catch (IOException e) {
      throw e;
    }
  }
    

}
