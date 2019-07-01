package physicalobject;

import constant.Regex;

/**.
 * 构造Athlete实例的参数包，包中包含所有需要构造一个Athlete实例的参数，这是一个{@code immutable} 类型的类。
 *
 */
public class AthleteParamPackage implements ParamPackage {
  private final String name;
  private final int number;
  private final String nation;
  private final int age;
  private final double pb; // Personal Best Score

  /*
   * AF 
   *    AF(name, number, nation, age, pb) = 一个具有姓名，号码，国籍，年龄，最好成绩的Athlete参数包
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
    assert number >= 0 : "Athlete Number is < 0, but was" + number;
    assert nation.matches(Regex.TRACKGAME_NATION) 
        : "Athlete Nation is not consist of 3 captive character";
    assert age > 0 : "Athlete Age is <= 0";
    assert pb < 100 && pb > 0 : "Athlete Personal Best Score is not obtein the format";
  }

  /**.
   * 初始化一个Athlete对象参数包
   * 
   * @param name   符合word格式
   * @param number 正整数
   * @param nation 三位大写字母
   * @param age    正整数
   * @param pb     最多两位整数和必须是两位小数构成
   */
  public AthleteParamPackage(String name, int number, String nation, int age, double pb) {
    this.name = name;
    this.number = number;
    this.nation = nation;
    this.age = age;
    this.pb = pb;
    checkRep();
  }

  public String getName() {
    return name;
  }

  public int getNumber() {
    return number;
  }

  public String getNation() {
    return nation;
  }

  public int getAge() {
    return age;
  }

  public double getPersonalBest() {
    return pb;
  }
}
