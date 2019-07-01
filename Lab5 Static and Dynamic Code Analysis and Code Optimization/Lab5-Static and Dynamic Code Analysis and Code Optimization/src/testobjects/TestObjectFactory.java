package testobjects;

import physicalobject.ParamPackage;
import physicalobject.PhysicalObject;
import physicalobject.PhysicalObjectFactory;


/**.
 * 用于测试的轨道物体工厂方法，无实际意义
 *
 */
public class TestObjectFactory extends PhysicalObjectFactory {

  @Override
  public TestObject create(String name) {
    return new TestObject(name);
  }

  @Override
  public PhysicalObject create(ParamPackage paramPackage) {
    // TODO Auto-generated method stub
    return null;
  }

}
