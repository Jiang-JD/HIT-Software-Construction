package tool;

import items.Monkey;

/**
 * .保存猴子的位置信息，immutable类型
 *
 */
public class Position {
  private final Monkey monkey;
  private final String id;
  private final int pos;
  private final int time; //猴子存活时间
  
  private void checkRep() {
    assert monkey != null;
    assert pos >= 0;
  }
  
  /**
   * .初始化位置
   * @param monkey 待记录猴子
   * @param pos 待记录位置，此位置表示梯子上的位置，从左向右从0开始
   */
  public Position(Monkey monkey, int pos) {
    this.monkey = monkey;
    this.pos = pos;
    this.id = String.valueOf(monkey.getID());
    this.time = monkey.getTime();
    checkRep();
  }
  
  /**
   * .返回猴子对象
   * @return 猴子
   */
  public Monkey getMonkey() {
    return monkey;
  }
  
  /**
   * .返回位置坐标
   * @return 位置
   */
  public int getPosition() {
    return pos;
  }
  
  /**
   * .返回记录该位置时猴子的存活时长
   * @return 时间
   */
  public int getTime() {
    return time;
  }
  
  @Override 
  public String toString() {
    try {
      return "[position monkey: " + monkey.getID() + ", index: " + pos + "]";
    } catch (Exception e) {
      if (monkey == null) {
        return "[error position monkey: " + id + ", index: " + pos + "]";
      }
    }
    return id;
  }
}
