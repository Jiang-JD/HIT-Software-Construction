package circularOrbit;

import java.util.Objects;



/**
 * 这是一个{@code immutable} 类型，表示保存轨道物体与
 * 轨道物体之间的关系，为无向关系。
 *
 * @param <E> 轨道物体类型
 */
public class Relation<E> {
	private final E v1;
	private final E v2;
	
	/*
	 * Abstract Function:
	 * 	AF(v1, v2) = 一个表示轨道物体与轨道物体之间的无向关系
	 * 
	 * Rep Invariant:
	 * 	v1 	对象不为空
	 * 	v2 	对象不为空
	 * 
	 * Safety from exposure:
	 * 	所有域都是private final的，E是immutable类型，不提供Mutator，客户端无法拿到内部引用
	 */
	
	private void checkRep() { 
		assert v1 != null : "Relation vertex is null object";
		assert v2 != null : "Relation vertex is null object";
	}
	
	/**
	 * 初始化一个Relation对象，输入有向边的两个顶点，建立关系
	 * @param source 起点
	 * @param target 终点
	 */
	public Relation(E v1, E v2) {
		this.v1 = v1;
		this.v2 = v2;
		checkRep();
	}
	
	/**
	 * 输入无向关系中一个顶点，获得另一个顶点
	 * @param v 一个顶点
	 * @return 无向关系中v对面的顶点，如果v在这条关系中不存在，返回null
	 */
	public E getOther(E v) {
		assert v!= null: "Vertex is null object";
		if(v.equals(v1)) return v2;
		if(v.equals(v2)) return v1;
		return null;
	}
	
	/**
	 * 查找无向关系中是否包含顶点v
	 * @param v 待查找顶点
	 * @return 如果包含v，返回true，否则返回false
	 */
	public boolean contains(E v) {
		return v1.equals(v) || v2.equals(v);
	}
	
	/**
	 * 生成描述Relation的字符串
	 */
	@Override
	public String toString() {
		return "[Relation v1:"+v1.toString()+", v2:"+v2.toString()+"]";
	}
	
	/**
	 * 将其他Relation对象与当前Relation对象比较，如果两条关系的两个顶点都相同（不考虑前后顺序），则判定为两个对象
	 * 相同
	 */
	@Override 
	public boolean equals(Object otherobject) {
		if(this == otherobject) return true;
		if(otherobject == null) return false;
		if(getClass() != otherobject.getClass()) return false;
		
		Relation<E> other = (Relation<E>)otherobject;
		
		return (v1.equals(other.v1) && v2.equals(other.v2)) || (v1.equals(other.v2) && v2.equals(other.v1));
	}
	
	/**
	 * 生成Relation的哈希码
	 */
	@Override
	public int hashCode() {
		return Objects.hash(v1, v2);
	}
}
