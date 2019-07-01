package parser;

import centralobject.Nucleus;
import circularorbit.CircularOrbit;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import manager.PersonalAppManager;
import manager.TrackGameManager;
import physicalobject.Electron;

/**
 * .使用Scanner读文本文件，无写回文本策略，写回文本功能不可用
 */
public class ScannerIo implements IoStrategy {

  @Override
  public String getText(String filePath) {
    StringBuilder sb = new StringBuilder();
    File file = new File(filePath);
    try {
      long startTime = System.currentTimeMillis();
      Scanner scan = new Scanner(file);
      while (scan.hasNext()) {
        sb.append(scan.nextLine()).append("\n");
      }
      scan.close();
      long endTime = System.currentTimeMillis();
      System.out.println("Scanner: " + (endTime - startTime));
      return sb.toString();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public boolean outPutFile(String filePath, CircularOrbit<Nucleus, Electron> co) {
    return false;
  }

  @Override
  public boolean outPutFile(String filePath, TrackGameManager tgm) {
    return false;
  }

  @Override
  public boolean outPutFile(String filePath, PersonalAppManager pam) {
    return false;
  }
}
