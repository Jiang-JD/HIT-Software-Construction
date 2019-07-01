package tool;

import items.Ladders;
import items.Monkey;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * .解析器，解析文本输入
 *
 */
public class Parser {
  
  /**
   * .解析文本中的猴子，生成不同时间点对应生成的不同猴子的映射
   * @param filePath 文件路径
   * @param ladders 一组梯子，非空
   * @return 映射
   * @throws IOException 文件异常
   */
  public Map<Integer, List<Monkey>> parseMonkey(String filePath, Ladders ladders) 
                                                            throws IOException {
    if (ladders == null) {
      throw new IllegalArgumentException();
    }
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
      rbc.close();
      String text = sb.toString();
      int h = 0;
      int n = 0;
      Matcher m = Pattern.compile("n=(\\d+)").matcher(text);
      if (m.find()) {
        n = Integer.parseInt(m.group(1));
      }
      m = Pattern.compile("h=(\\d+)").matcher(text);
      if (m.find()) {
        h = Integer.parseInt(m.group(1));
      }
      m = Pattern.compile("monkey=<(\\d+),(\\d+),(\\S{4}),(\\d+)>").matcher(text);
      Map<Integer, List<Monkey>> map = new HashMap<Integer, List<Monkey>>();
      while (m.find()) {
        int time = Integer.parseInt(m.group(1));
        List<Monkey> l = map.get(time);
        if (l == null) {
          l = new ArrayList<Monkey>();
        }
        Monkey mo = new Monkey(Integer.parseInt(m.group(2)), 
                                m.group(3), 
                                Integer.parseInt(m.group(4)), ladders);
        l.add(mo);
        map.put(time, l);
      }
      return map;
    } catch (IOException e) {
      throw e;
    }
  }
  
  /**
   * .解析文本中一组梯子
   * @param filePath 文件路径
   * @return 一组梯子
   * @throws IOException 文件异常
   */
  public Ladders parseLadders(String filePath) throws IOException {
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
      rbc.close();
      String text = sb.toString();
      int h = 0;
      int n = 0;
      Matcher m = Pattern.compile("n=(\\d+)").matcher(text);
      if (m.find()) {
        n = Integer.parseInt(m.group(1));
      }
      m = Pattern.compile("h=(\\d+)").matcher(text);
      if (m.find()) {
        h = Integer.parseInt(m.group(1));
      }
      return new Ladders(n, h);
    } catch (IOException e) {
      throw e;
    }
  }
}
