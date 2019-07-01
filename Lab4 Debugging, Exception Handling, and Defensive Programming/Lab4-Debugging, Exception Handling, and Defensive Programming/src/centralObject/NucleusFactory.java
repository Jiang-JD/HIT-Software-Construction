package centralObject;

public class NucleusFactory extends CentralPointFactory {
	
	/**
	 * 生产新的原子核，输入参数为元素名称
	 */
	@Override
	public CentralPoint create(String name) {
		return new Nucleus(name);
	}
}
