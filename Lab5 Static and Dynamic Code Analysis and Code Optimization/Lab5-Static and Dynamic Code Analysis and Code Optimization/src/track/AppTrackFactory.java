package track;

/**.
 * APP Track的工厂类
 *
 */
public class AppTrackFactory extends TrackFactory {

  /**.
   * 生产APP生态系统轨道，输入参数为轨道编号，编号为>=1 的正整数
   * 
   * @param radius 编号，编号为 >=1 的正整数
   */
  @Override
  protected Track createProduct(double radius) {
    double eps = 1e-10;
    assert radius - Math.floor(radius) < eps 
        : "App track param radius should be an Integer,but was " + radius;
    assert radius >= 1 : "App track param < 1";
    return new AppTrack((int) radius);
  }

}
