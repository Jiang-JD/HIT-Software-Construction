package track;

import java.util.Objects;

/**.
 * 一个{@code immutable} 类型表示APP生态系统中的轨道，不同亲密度的APP分布在不同的轨道上
 *
 */
public class AppTrack extends Track {

  private final int number;

  /*
   * Abstract Function: 
   *    AF(Number) = 一个App生态系统中的轨道
   * 
   * Rep Invariant: 
   *    Number 大于等于1的正整数
   * 
   * Safety from exposure: 
   *    所有域是private final的，不提供mutator，返回的值都为immutable类型
   * 
   */

  private void checkRep() {
    assert number >= 1 : "APP track number < 1";
  }

  public AppTrack(int num) {
    this.number = num;
    checkRep();
  }

  /**.
   * 获得轨道的半径，方法不可用
   */
  @Override
  public double getRadius() {
    return -1;
  }

  /**.
   * 获得轨道的编号
   */
  @Override
  public int getNumber() {
    return number;
  }

  @Override
  public boolean equals(Object otherobject) {
    if (this == otherobject) {
      return true;
    }
    if (otherobject == null) {
      return false;
    }
    if (getClass() != otherobject.getClass()) {
      return false;
    }

    AppTrack other = (AppTrack) otherobject;

    return this.number == (other.getNumber());
  }

  @Override
  public String toString() {
    return "[AppTrack Number:" + number + "]";
  }

  @Override
  public int hashCode() {
    return Objects.hash(number);
  }

}
