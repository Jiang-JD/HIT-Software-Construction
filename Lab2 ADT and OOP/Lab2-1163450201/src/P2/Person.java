package P2;

/**
 * 表示一个人，为不可变类。一个人只有姓名一个属性。
 * @author hit
 *
 */
public class Person {
	private final String name;
	
	// Abstract Function:
	//	AF(name) = 一个在关系图中的人
	//
	// Representation invariant:
	//	name: 不为空字符串，不为空对象
	//
	// Safety from rep exposure:
	//	name由private和final修饰，没有setter方法
	
	/**
	 * 初始化一个Person类对象。
	 * @param name Person的名称，要求非空。
	 */
	public Person(String name) {
		if(name.equals("")) {
			try {
				throw new Exception("姓名不能为空");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		 this.name = name;
	}
	
	/**
	 * CheckRI
	 */
	private void checkRep() {
		assert name != null:"姓名不能为null";
		assert !name.equals(""):"姓名不能为空";
	}
	
	/**
	 * 获得人的名称
	 * @return 人的名称
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 将另一个Person对象与其比较，判断是否相等，依据为name是否相同。
	 * @param others 另一个Person对象
	 * @return true如果相等。
	 */
	@Override
	public boolean equals(Object otherobject) {
		if(this == otherobject) return true;
		if(otherobject == null) return false;
		if(getClass() != otherobject.getClass()) return false;
		
		Person other = (Person)otherobject;
		return this.name.equals(other.getName());
	}
	
	/**
	 * 生成描述Person的字符串
	 * @return 一个描述Person信息的字符串
	 */
	@Override
	public String toString() {
		return ("[Class: "+ this.getClass().getName()+ ",name: "+ this.name + "]");
	}
}
