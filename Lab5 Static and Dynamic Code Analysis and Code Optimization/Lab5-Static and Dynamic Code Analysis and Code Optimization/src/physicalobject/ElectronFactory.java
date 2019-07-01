package physicalobject;

import constant.ReusablePool;

/**.
 * 电子轨道工厂类
 *
 */
public class ElectronFactory extends PhysicalObjectFactory {

  /**.
   * 生产一个新的电子轨道，输入参数为电子名称
   */
  @Override
  public PhysicalObject create(String name) {
    return new Electron(name);
  }
  
  /**.
   * 生产电子
   * @return 电子对象
   */
  public PhysicalObject create() {
    return ReusablePool.electron;
  }

  /**.
   * 参数包方法在
   */
  @Override
  public PhysicalObject create(ParamPackage paramPackage) {
    return null;
  }

}
