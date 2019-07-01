package physicalobject;

import constant.Regex;
import java.util.Objects;

public class App extends PhysicalObject {

  private final String name;
  private final String company;
  private final String version;
  private final String description;
  private final String area;

  /*
   * Abstract Function: 
   *    AF(name, company, version, description, area) = 一个手机上使用的App
   * 
   * Rep Invariant: 
   *    name 类型为label 
   *    company 类型为label 
   *    version 类型为label，使用 . _ -分割
   *    description sentence类型，带引号 
   *    area sentence类型，带引号
   * 
   * Safety from exposure: 
   *    所有域是private final的，不提供mutator，客户端无法拿到类的内部引用
   */

  private void checkRep() {
    assert getName().matches(Regex.REGEX_LABEL) : "App Name is not label format";
    assert company.matches(Regex.REGEX_LABEL) : "App company is not label format";
    assert version.matches(Regex.APPECO_VERSION) : "App version is not label type(include ._-)";
    assert description.matches("\"" + Regex.REGEX_SENTENCE + "\"") 
    : "App description is not \"sentence\" type";
    assert area.matches("\"" + Regex.REGEX_SENTENCE + "\"") : "App area is not \"sentence\" type";
  }

  /**.
   * 初始化一个APP对象，一个APP对象包含名称，公司，版本，描述和所属领域信息。
   * 
   * @param name        APP名称，类型为label
   * @param company     APP所属公司，类型为label
   * @param version     APP版本，类型为label，用 -_. 分割
   * @param description APP描述，类型为sentence，带引号
   * @param area        APP所属领域，类型为sentence，带引号
   */
  public App(String name, String company, String version, String description, String area) {
    this.name = name;
    this.company = company;
    this.version = version;
    this.description = description;
    this.area = area;
    checkRep();
  }

  /**.
   * 获得公司名称
   * 
   * @return 公司名称
   */
  public String getCompany() {
    return company;
  }

  /**.
   * 获得版本号
   * 
   * @return 版本号
   */
  public String getVersion() {
    return version;
  }

  /**.
   * 获得APP描述
   * 
   * @return APP描述
   */
  public String getDescription() {
    return description;
  }

  /**.
   * 获得APP所属领域
   * 
   * @return APP所属领域
   */
  public String getArea() {
    return area;
  }

  /**.
   * 获得APP名称
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

    App other = (App) otherobject;

    return this.name.equals(other.getName()) 
        && company.equals(other.company) 
        && version.equals(other.version)
        && description.equals(other.description) 
        && area.equals(other.area);
  }

  /**.
   * 生成描述APP的字符串
   */
  @Override
  public String toString() {
    return "[APP name: " + name + ", company: " + company 
        + ", version: " + version + ", description: " + description
        + ", area: " + area + "]";
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, company, version, description, area);
  }

}
