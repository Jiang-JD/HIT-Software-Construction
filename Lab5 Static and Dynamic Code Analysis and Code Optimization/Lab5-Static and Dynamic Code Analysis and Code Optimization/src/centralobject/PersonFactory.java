package centralobject;

/**.
 * 用户工厂类
 *
 */
public class PersonFactory extends CentralPointFactory {

  /**.
   * 生产新的用户，输入参数为用户名称，名称为label类型
   */
  @Override
  public CentralPoint create(String name) {
    return new Person(name);
  }
}
