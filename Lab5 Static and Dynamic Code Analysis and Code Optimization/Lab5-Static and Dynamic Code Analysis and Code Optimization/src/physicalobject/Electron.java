package physicalobject;

import java.util.Objects;

/**.
 * 表示一个原子轨道系统中的电子，{@code immutable} 类型。
 *
 */
public class Electron extends PhysicalObject {
  private final String name;

  /*
   * AF 
   *    AF(name) = 一个原子轨道系统中的电子
   * 
   * RI 
   *    name name不为null也不为空串，name是一个label
   * 
   * Safety from exposure 
   *    所有域是private final的，不提供Mutator，客户端拿不到直接引用
   */

  private void checkRep() {
    assert name != null && !name.equals("") : "Electron Name is null or \"\"";
    assert name.matches("[0-9A-Za-z]+") : "Electron format of name is wrong";
  }

  /**.
   * 初始化一个电子对象，输入电子的名称，构造电子对象
   * 
   * @param name 电子名称，要求名称不为空，且符合label规则
   */
  public Electron(String name) {
    this.name = name;
    checkRep();
  }

  /**.
   * 获得电子的名称
   */
  @Override
  public String getName() {
    return name;
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

    Electron other = (Electron) otherobject;

    return this.name.equals(other.getName());
  }

  @Override
  public String toString() {
    return "[Electron name: " + name + "]";
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }

}
