package physicalObject;

import parser.PersonalAppEcosystemParser;

/**
 * App的工厂类
 *
 */
public class AppFactory extends PhysicalObjectFactory {

	/**
	 * 生产APP对象，输入描述App的字符串，字符串格式为 
	 * <p>{@code App ::= <App 名称,公司,版本,"功能描述","业务领域">} 
	 * <p>其中， App 名称和公司为 label 类型， 版本是类似于使用诸如“.”、“_”、
	 * “-”分割的 label，例如 v3.2-3_001、2.30 等。功能描述和业务领域均带引
	 * 号，类 型是 sentence 类型
	 * 
	 * @param info 描述APP信息的字符串，字符串不为空
	 */
	@Override
	public PhysicalObject create(String info) {
		assert info != null : "string is null object";
		PersonalAppEcosystemParser ap = new PersonalAppEcosystemParser();
		String name = ap.getName(info);
		String company = ap.getCompany(info);
		String version = ap.getVersion(info);
		String description = ap.getDescription(info);
		String area = ap.getArea(info);
		
		return new App(name, company, version, description, area);
	}
	
	/**
	 * 生产APP对象，输入App参数包
	 * 
	 * @param pp App参数包，要求为AppParamPackage类型
	 */
	@Override
	public PhysicalObject create(ParamPackage pp) {
		if(pp == null) throw new IllegalArgumentException("ParamPackage is null pointer");
		if(!(pp instanceof AppParamPackage)) throw new IllegalArgumentException("ParamPackage is not AppParamPackage");
		
		AppParamPackage app = (AppParamPackage)pp;
		return new App(app.getName(), app.getCompany(),app.getVersion(), app.getDescripition(), app.getArea());
	}

}
