package physicalobject;

import constant.Regex;
import java.util.Objects;

/**.
 * 表示一个运动员，这个运动员具有姓名，号码，国籍，年龄和年度最好成绩
 *
 */
public class Athlete extends PhysicalObject {
  private final String name;
  private final int number;
  private final String nation;
  private final int age;
  private final double pb; // Personal Best Score

  /*
   * AF 
   *    AF(name, number, nation, age, pb) = 一个具有姓名，号码，国籍，年龄，最好成绩的运动员
   * 
   * RI 
   *    name是word 
   *    number和age是正整数 
   *    nation是三位大写字母 
   *    pb是最多两位整数构成
   * 
   * Safety from exposure 
   *    所有的域是private final的，没有mutator方法，客户端无法拿到内部引用
   */

  private void checkRep() {
    assert name.matches(Regex.REGEX_WORD) : "Athlete Name is not word format";
    assert number >= 0 : "Athlete Number is < 0, but was " + number;
    assert nation.matches(Regex.TRACKGAME_NATION) 
          : "Athlete Nation is not consist of 3 captive character";
    assert age > 0 : "Athlete Age is <= 0";
    assert pb < 100 && pb > 0 : "Athlete Personal Best Score is not obtein the format";
  }

  /**.
   * 初始化一个Athlete对象
   * 
   * @param name   符合word格式
   * @param number 正整数
   * @param nation 三位大写字母
   * @param age    正整数
   * @param pb     最多两位整数和必须是两位小数构成
   */
  public Athlete(String name, int number, String nation, int age, double pb) {
    this.name = name;
    this.number = number;
    this.nation = nation;
    this.age = age;
    this.pb = pb;
    checkRep();
  }

  /**.
   * 获得运动员的姓名
   */
  @Override
  public String getName() {
    return name;
  }

  /**.
   * 获得运动员的号码
   * 
   * @return 运动员的号码
   */
  public int getNumber() {
    return number;
  }

  /**.
   * 获得运动员的国籍
   * 
   * @return 运动员的国籍
   */
  public String getNation() {
    return nation;
  }

  /**.
   * 获得运动员的年龄
   * 
   * @return 运动员年龄
   */
  public int getAge() {
    return age;
  }

  /**.
   * 获得运动员的年度最好成绩
   * 
   * @return 年度最好成绩
   */
  public double getPersonalBest() {
    return pb;
  }

  /**.
   * 生成Athlete对象的哈希码
   */
  @Override
  public int hashCode() {
    return Objects.hash(name, age, nation, number, pb);
  }

  /**.
   * 生成描述Athlete对象的字符串
   */
  @Override
  public String toString() {
    return "[Athlete: name:" + name + ", age:" + age 
        + ", number:" + number 
        + ", nation:" + nation 
        + ", personal best:" + pb + "]";
  }

  /**.
   * 将其他的对象与当前的Athlete对象比较是否相等，比较其所有属性，若Athlete对象 与其他的对象在姓名，年龄，国籍，号码，成绩都相同，则判定为相等。
   */
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

    Athlete other = (Athlete) otherobject;
    return name.equals(other.name) 
        && number == other.number 
        && nation.equals(other.nation) 
        && age == other.age
        && pb == other.pb;
  }

}
