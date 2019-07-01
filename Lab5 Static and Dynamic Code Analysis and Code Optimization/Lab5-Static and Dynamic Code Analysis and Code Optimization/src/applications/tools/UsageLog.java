package applications.tools;

import constant.Regex;

import java.time.Instant;
import java.util.Objects;

/**..
 * 一个{@code immutable} 类型，表示一条使用日志，其中包括使用日期，使用时间，使用App名称，使用时长
 *
 */
public class UsageLog {
  private final String name;
  private final Instant time;
  private int duration;

  /*
   * AF 
   *    AF(name, time, duration) = 一条记录某个App的使用日志，包括使用日期，使用时间，使用App名称，使用时长
   * 
   * Rep invariant: 
   *    name 非空，为label格式 
   *    time 非空 duration为正整数
   * 
   * Safety from exposure: 
   *    所有域是private final的，不提供Mutator，客户端无法拿到直接引用
   */

  private void checkRep() {
    assert name != null : "UsageLog name is null pointer";
    assert name.matches(Regex.REGEX_LABEL) : "Name is not label format";
    assert time != null : "Time is null pointer";
    assert duration > 0 : "Duration is <= 0";
  }

  /**.
   * 初始化一个使用日志，输入app名称，时间点，使用时长
   * @param name app名称
   * @param time 使用时间点
   * @param duration 使用时长
   */
  public UsageLog(String name, Instant time, int duration) {
    this.duration = duration;
    this.name = name;
    this.time = time;
    checkRep();
  }

  public String getName() {
    return name;
  }

  public Instant getTime() {
    return time;
  }

  public int getDuration() {
    return duration;
  }

  /**.
   * 将输入时间段与当前时间点比较，判断是否属于该时间段，时间段包含其终止时间点
   * 
   * @param ts 待判断时间段
   * @return 如果使用时间点在时间段内，返回{@code true} ，否则返回{@code false}
   */
  public boolean within(Timespan ts) {
    if (ts == null) {
      throw new NullPointerException("Timespan is null");
    }

    if ((ts.getStart().isBefore(time) 
        || ts.getStart().equals(time)) && ts.getEnd().isAfter(time)
        || ts.getEnd().equals(time)) {
      return true;
    }
    return false;
  }

  @Override
  public String toString() {
    return "[UsageLog AppName:" + this.getName() 
          + ", DateTime:" + this.getTime() 
          + ", Duration:" + this.getDuration()
        + "]";
  }

  /*
   * @see Object.equals()
   */
  @Override
  public boolean equals(Object thatObject) {
    if (!(thatObject instanceof UsageLog)) {
      return false;
    }

    UsageLog that = (UsageLog) thatObject;
    return this.name.equals(that.name) 
            && this.time.equals(that.time) 
            && this.duration == that.duration;
  }

  /*
   * @see Object.hashCode()
   */
  @Override
  public int hashCode() {
    return Objects.hash(name, time, duration);
  }

}
