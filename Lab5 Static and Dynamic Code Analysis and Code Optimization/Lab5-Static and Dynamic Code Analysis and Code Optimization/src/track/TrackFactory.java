package track;

/**.
 * 生产抽象Track类的模板工厂类，提供模板和生产流程。Track类需要的唯一参数为 {@code radius}
 * 半径，工厂类提供{@code create}方法用来生产Track类。
 *
 */
public abstract class TrackFactory {
  /**.
   * 生产一个新的抽象Track产品，输入参数为 {@code radius} 作为轨道的半径。
   * 
   * @param radius Track的半径
   * @return Track抽象产品
   */
  public final Track create(double radius) {
    Track product = createProduct(radius);
    return product;
  }

  /**.
   * 建抽象Track，输入半径，半径 >= 0
   * 
   * @param radius 半径 >= 0
   * @return 新的Track产品
   */
  protected abstract Track createProduct(double radius);
}
