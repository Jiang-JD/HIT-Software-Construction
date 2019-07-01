package physicalObject;

import constant.Regex;

/**
 * App对象的参数包，包中包含所有需要构造一个App实例的参数，这是一个{@code immutable} 类型类。
 *
 */
public class AppParamPackage implements ParamPackage{
	private final String name;
	private final String company; 
	private final String version;
	private final String description;
	private final String area;
	
	/*
	 * Abstract Function:
	 * 	AF(name, company, version, description, area) = 一个用于实例化App对象的参数包
	 * 
	 * Rep Invariant:
	 * 	name		类型为label
	 * 	company		类型为label
	 * 	version		类型为label，使用 . _ -分割
	 * 	description	sentence类型，带引号
	 * 	area		sentence类型，带引号
	 * 
	 * Safety from exposure:
	 * 	所有域是private final的，只提供Observer，客户端无法拿到类的内部引用
	 */
	
	private void checkRep() {
		assert name.matches(Regex.REGEX_LABEL) : "App Name "+ name+" is not label format";
		assert company.matches(Regex.REGEX_LABEL): "App company is not label format";
		assert version.matches(Regex.APPECO_VERSION) :"App version is not label type(include ._-)";
		assert description.matches("\""+Regex.REGEX_SENTENCE+"\"") : "App description is not \"sentence\" type";
		assert area.matches("\""+Regex.REGEX_SENTENCE+"\"") : "App area is not \"sentence\" type";
	}
	
	/**
	 * 初始化一个APP对象，一个APP对象包含名称，公司，版本，描述和所属领域信息。
	 * 
	 * @param name APP名称，类型为label
	 * @param company APP所属公司，类型为label
	 * @param version APP版本，类型为label，用 -_. 分割
	 * @param description APP描述，类型为sentence，带引号
	 * @param area APP所属领域，类型为sentence，带引号
	 */
	public AppParamPackage(String name, String company, String version, String description, String area) {
		this.name = name;
		this.version = version;
		this.company = company;
		this.description = description;
		this.area = area;
		checkRep();
	}
	
	public String getName() {
		return name;
	}
	
	public String getCompany() {
		return company;
	}
	
	public String getVersion() {
		return version;
	}
	
	public String getDescripition() {
		return description;
	}
	
	public String getArea() {
		return area;
	}
	
	
}
