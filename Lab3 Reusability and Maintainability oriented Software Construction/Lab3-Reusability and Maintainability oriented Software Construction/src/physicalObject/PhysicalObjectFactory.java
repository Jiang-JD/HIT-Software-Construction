package physicalObject;

/**
 * 生产抽象PhysicalObject类的模板工厂类，提供模板和生产流程。PhysicalObject类需要的唯一参数为
 *  {@code name} 名称，工厂类提供 {@code create()} 方法用来生产PhysicalObject。
 *
 */
public abstract class PhysicalObjectFactory {
	
	/**
	 * 生产一个新的抽象PhysicalObject产品，输入参数为 {@code name} 作为物体的名称。
	 * @param name
	 * @return 轨道物体实例
	 */
	public abstract PhysicalObject create(String info);
	
	/**
	 * 生产一个新的PhysicalObject产品， 输入参数为 {@code paramPackage} 参数包作为物体参数
	 * 
	 * @param paramPackage 构造PhysicalObject的参数包
	 * @return 轨道物体实例
	 */
	public abstract PhysicalObject create(ParamPackage paramPackage);
}
