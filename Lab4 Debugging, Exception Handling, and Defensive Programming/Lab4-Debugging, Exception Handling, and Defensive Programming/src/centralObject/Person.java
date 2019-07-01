package centralObject;

import java.util.Objects;

import constant.Regex;

/**
 * 一个{@code immutable} 类型，表示具有姓名的人
 *
 */
public class Person extends CentralPoint {

	/*
	 * Abstract Function:
	 * 	AF(name) = 一个手机使用用户
	 * 
	 * Rep Invariant:
	 * 	name	非空串""或空对象且符合lable构造规则
	 * 
	 * Safety from exposure:
	 * 	所有域是private final的，不提供mutator。
	 */
	
	private void checkRep() {
		assert super.getName() != null && !super.getName().equals("") : "Person name is null or \"\"";
		assert super.getName().matches(Regex.REGEX_LABEL) : "Person name is not Label type";
	}
	
	/**
	 * 初始化一个用户对象，输入用户的姓名
	 * @param name 用户名称，要求不为空串或空对象且符合label规则
	 */
	public Person(String name) {
		super(name);
		checkRep();
	}
	
	@Override 
	public boolean equals(Object otherobject) {
		if(this == otherobject) return true;
		if(otherobject == null) return false;
		if(getClass() != otherobject.getClass()) return false;
		
		Person other = (Person)otherobject;
		
		return this.getName().equals(other.getName());
	}
	
	@Override
	public String toString() {
		return "[Person name: "+getName()+"]";
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getName());
	}
	
}
