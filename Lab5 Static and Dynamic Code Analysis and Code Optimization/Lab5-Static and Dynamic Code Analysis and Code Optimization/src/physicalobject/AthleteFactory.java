package physicalobject;

import parser.TrackGameParser;

public class AthleteFactory extends PhysicalObjectFactory {

  /**.
   * 生产新的Athlete对象，输入参数为描述Athlete的字符串，字符串的格式为
   * 
   * <p>{@code Athlete ::= <姓名,号码,国籍,年龄,本年度最好成绩>}
   * 
   * <p>代表一个运动员，其中姓名是 word ，号码和年龄是正整数， 国籍是三位大写字母，成绩是最多两位整数和必须两位小数 构成（例如 9.10、10.05）
   * 
   * @param info 描述Athlete的字符串，字符串不为空
   */
  @Override
  public PhysicalObject create(String info) {
    assert info != null : "info string is null object";
    TrackGameParser parser = new TrackGameParser();
    String name = parser.getName(info);
    int number = parser.getNumber(info);
    String nation = parser.getNation(info);
    int age = parser.getAge(info);
    double pb = parser.getPb(info);

    return new Athlete(name, number, nation, age, pb);
  }

  /**.
   * 生产Athlete对象，输入Athlete参数包
   * 
   * @param pp Athlete参数包，要求为AthleteParamPackage类型
   */
  @Override
  public PhysicalObject create(ParamPackage pp) {
    if (pp == null) {
      throw new IllegalArgumentException("ParamPackage is null pointer");
    }
    if (!(pp instanceof AthleteParamPackage)) {
      throw new IllegalArgumentException("ParamPackage is not AthleteParamPackage");
    }

    AthleteParamPackage app = (AthleteParamPackage) pp;
    return new Athlete(
        app.getName(), app.getNumber(), app.getNation(), app.getAge(), app.getPersonalBest());
  }

}
