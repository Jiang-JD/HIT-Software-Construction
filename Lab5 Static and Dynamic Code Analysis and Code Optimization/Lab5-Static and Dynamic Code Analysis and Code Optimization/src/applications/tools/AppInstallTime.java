package applications.tools;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**.
 * 这是一个{@code mutable} 类型，记录一个App的安装周期，一个app的安装周期指
 * 这个App在手机上处于安装状态的时间段的集合，不包括被卸载的时间段
 *
 */
public class AppInstallTime implements Cloneable {
  private final List<Timespan> installing = new ArrayList<Timespan>();

  /*
   * Abstract Function: 
   *    AF(installing) = 记录App的安装周期
   * 
   * Rep Invariant: 
   *    installing: 列表内所有对象不为空，各个时间段互不相交（允许分割点相同）
   * 
   * Safety from exposure: 
   *    域是private final的
   * 
   */

  private void checkRep() {
    for (Timespan t1 : installing) {
      assert t1 != null;
      for (Timespan t2 : installing) {
        if (!t1.equals(t2)) {
          assert !t1.overlap(t2) : t1 + " is overlap " + t2;
        }
      }
    }
  }

  /**.
   * 将输入时间段与安装周期比较，判断是否有相交部分。如果只有在起始处的时间点上相同，则不算相交
   * 
   * @param ts 待判断的时间段
   * @return 如果有相交部分，返回{@code true}，否则返回{@code false}
   */
  public boolean overlap(Timespan ts) {
    if (installing.isEmpty()) {
      return false;
    }
    for (Timespan t : installing) {
      if (t.overlap(ts)) {
        return true; // 如果有时间段重合，则说明ts时间段内App处于安装状态
      }
    }
    return false;
  }

  /**.
   * 添加安装时间段，待添加的时间段与其他时间段不能重合
   * 
   * @param ts 待添加的安装时间段，要求非空对象且该时间段与其他时间段不重合
   * @return 如果添加成功返回{@code true} ，否则返回{@code false}
   */
  public boolean add(Timespan ts) {
    if (ts == null) {
      return false;
    }
    for (Timespan t : installing) {
      if (ts.overlap(t)) {
        return false;
      }
    }
    installing.add(ts);
    checkRep();
    return true;
  }

  /**.
   * 移除一个时间段
   * 
   * @param ts 待移除时间段
   * @return 如果移除成功，返回{@code true} ，否则返回{@code false}
   */
  public boolean remove(Timespan ts) {
    if (ts == null) {
      return false;
    }
    return installing.remove(ts);
  }
  
  /**
   * .获得指定索引的时间段
   * @param index 指定索引
   * @return 对应时间段，不存在返回null
   */
  public Timespan get(int index) {
    return installing.get(index);
  }
  
  /**
   *.获得安装时间段的大小
   * @return 时间段大小
   */
  public int size() {
    return installing.size();
  }

  /**.
   * 获得App整个安装周期最早起始时间点和最晚终止时间点形成的时间段
   * 
   * @return 最早起始时间点和最晚终止时间点形成的时间段,如果App没有安装， 则返回null
   */
  public Timespan entireSpan() {
    if (installing.isEmpty()) {
      return null;
    }

    Instant start = Instant.MAX;
    Instant end = Instant.MIN;

    for (Timespan t : installing) {
      if (t.getStart().isBefore(start)) {
        start = t.getStart();
      }
      if (t.getEnd().isAfter(end)) {
        end = t.getEnd();
      }
    }

    return new Timespan(start, end);

  }
  
  /**.
   * 将当前的AppUasgeTime对象进行拷贝，为浅拷贝，指向Timespan的引用不做修改
   */
  public AppInstallTime clone() {
    try {
      super.clone();
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }
    AppInstallTime at = new AppInstallTime();
    for (Timespan t : installing) {
      at.add(t);
    }
    return at;
  }

  @Override
  public String toString() {
    return installing.toString();
  }
}
