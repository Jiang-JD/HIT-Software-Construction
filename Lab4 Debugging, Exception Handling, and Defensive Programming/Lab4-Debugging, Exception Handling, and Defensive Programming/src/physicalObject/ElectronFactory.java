package physicalObject;

/**
 * 电子轨道工厂类
 *
 */
public class ElectronFactory extends PhysicalObjectFactory {

	/**
	 * 生产一个新的电子轨道，输入参数为电子名称
	 */
	@Override
	public PhysicalObject create(String name) {
		return new Electron(name);
	}
	
	/**
	 * 参数包方法在
	 */
	@Override
	public PhysicalObject create(ParamPackage paramPackage) {
		return null;
	}

}
