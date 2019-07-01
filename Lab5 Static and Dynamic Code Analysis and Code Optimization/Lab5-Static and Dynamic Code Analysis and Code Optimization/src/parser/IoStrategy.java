package parser;

import centralobject.Nucleus;
import circularorbit.CircularOrbit;
import java.io.IOException;

import manager.PersonalAppManager;
import manager.TrackGameManager;
import physicalobject.Electron;

/**.
 * 不同策略IO接口，使用不同的IO策略输入文本
 *
 */
public interface IoStrategy {
  
  /**
   * .根据输入文件路径，获取文本文件的字符串形式
   * @param filePath 文件路径
   * @return 文本字符串
   */
  String getText(String filePath);
  
  /**
   * .根据文件路径，将原子系统写回文本文件中
   * @param filePath 文件路径
   * @param as 原子轨道
   * @return 如果写入成功，返回true，否则返回false
   * @throws IOException 文件写入异常抛出
   */
  boolean outPutFile(String filePath, CircularOrbit<Nucleus,Electron> as) throws IOException;
  
  /**
   * .根据文件路径，将径赛系统写回文本文件中
   * @param filePath 文件路径
   * @param tgm 径赛管理器
   * @return 如果写入成功，返回true，否则返回false
   * @throws IOException 文件写入异常抛出
   */
  boolean outPutFile(String filePath, TrackGameManager tgm) throws IOException;
  
  /**
   * .根据文件路径，将用户生态系统写回文本文件中
   * @param filePath 文件路径
   * @param pam 用户生态系统管理器e
   * @return 如果写入成功，返回true，否则返回false
   * @throws IOException 文件写入异常抛出
   */
  boolean outPutFile(String filePath, PersonalAppManager pam) throws IOException;
}
