package P3;
import java.util.*;

public class Person {
	private String name;
	private Set<Person> friendship = new HashSet<Person>();  //朋友关系集合
	
	public Person(String name) {
		if(name.equals("")) {
			System.out.println("姓名不能为空");
			name = "undefined";
		}
		 this.name = name;
	}
	
	public Person() {
		System.out.println("姓名不能为空");
		this.name = "undefined";
	}
	
	public String getNme() {
		return name;
	}
	
	public void setName(String name) {
		this.name =  name;
	}
	
	/**
	 * 判断是否对象相等
	 * @param others
	 * @return boolean
	 */
	public boolean equals(Person others) {
		return this.name.equals(others.getNme());
	}
	
	/**
	 * 生成字符串
	 */
	public String toString() {
		return ("[Class: "+ this.getClass().getName()+ ",name: "+ this.name + "]");
	}
	
	/**
	 * 添加朋友到朋友关系中
	 * @param person
	 */
	public void addFriend(Person person) {
		friendship.add(person);
	}
	
	/**
	 * 获得此人的朋友关系
	 * @return 集合
	 */
	public Set<Person> getFriendship() {
		return friendship;
	}
	
	
	
	
}
