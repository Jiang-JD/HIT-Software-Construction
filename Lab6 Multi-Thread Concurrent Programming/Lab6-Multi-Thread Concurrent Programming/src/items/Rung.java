package items;

/**
 * .梯子上的一个横挡
 *
 */
public class Rung {
  private final int position; //踏板在梯子中所处的位置，从0开始，方向从左到右
  
  /*
   * Abstract Function:
   *  AF(position) = 一个有编号的踏板
   *  
   * Rep invariant:
   *  number   >= 0
   *  
   * Safe from rep exposure:
   *  所有域都是private final的
   *  
   * Thread safe argument:
   *  这个类是线程安全的，因为Rung是immutable的
   *  - position 是final的，意味着不能被修改
   *  
   */
  
  private void checkRep() {
    assert position >= 0 : "Number < 0";
  }
  
  /**
   * .初始化一个踏板
   * @param number 编号
   */
  public Rung(int number) {
    this.position = number;
    checkRep();
  }
  
  /**
   *.返回踏板编号
   * @return 编号
   */
  public int getPosition() {
    return position;
  }
}
