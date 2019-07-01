package P2;

import static org.junit.Assert.*;

import org.junit.Test;


public class FriendshipGraphTest {
	/*
	 * 测试策略
	 * 	  输入划分为
	 * 	  addVertex()
	 * 		空对象，正确对象，重复对象
	 *	  addEdge()
	 *		对象为null/对象不为null
	 *		对象在点集中/对象不在点集中
	 *	  getDistance()
	 *		顶点加入点集中/未加入
	 *		顶点间存在边/不存在边
	 *		顶点为null/不为null
	 *		相同顶点/不同
	 */
	FriendshipGraph graph = new FriendshipGraph();
	
	/*
	 * 测试策略
	 * 	  addVertex()
	 * 		添加空对象，输出false
	 * 		添加正确对象，输出true
	 * 		添加重复对象，输出false
	 */
	@Test
	public void addVertexTest() {
		Person a = null;
		assertEquals(true , graph.addVertex(new Person("Kit")));
		assertEquals(false , graph.addVertex(new Person("Kit")));
		assertEquals(false , graph.addVertex(a));
	}
	
	/*
	 * 测试策略
	 * 	  addEdge()
	 *		输入两个null对象，输出false
	 *		输入一个空对象，输出false
	 *		输入不在点集中对象，输出false
	 *		输入两个正确对象，输出true
	 */
	@Test
	public void addEdgeTest() {
		Person a = null;
		Person b = null;
		assertEquals(false , graph.addEdge(a, b));
		assertEquals(false, graph.addEdge(a, new Person("oking")));
		assertEquals(false, graph.addEdge(new Person("yrxx"), b));
		graph.addVertex(a = new Person("A"));
		graph.addVertex(b = new Person("B"));
		assertEquals(true, graph.addEdge(a, b));
	}
	
	/*
	 * 测试策略
	 * 	  getDistance()
	 * 	  produced by addVertex(),addEdge()
	 * 		输入顶点未加入点集，输出-1
	 * 		输入顶点之间不存在边，输出-1
	 * 		输入存在null，输出-1
	 * 		输入相同顶点，输出0
	 * 		输入正确顶点，输出1
	 * 		输入多点图，输出最短距离
	 */
	@Test
	public void getDistanceTest() {
		Person a =  new Person("A");
		Person b = new Person("B");
		Person c = new Person("C");
		Person d = new Person("D");
		Person e = new Person("E");
		Person f = new Person("F");
		Person g = new Person("G");
		Person nu = null;
		
		//B未加入顶点
		graph.addVertex(a);
		assertEquals(-1, graph.getDistance(a, b));
		
		//不存在边
		graph.addVertex(b);
		assertEquals(-1, graph.getDistance(a, b));
		
		//存在空对象
		assertEquals(-1, graph.getDistance(nu, a));
		
		//A自身比较
		assertEquals(0, graph.getDistance(a, a));
		
		//AB存在一条边
		graph.addEdge(a, b);
		graph.addEdge(b, a);
		graph.getDistance(a, b);
		assertEquals(1, graph.getDistance(a, b));
		assertEquals(1, graph.getDistance(b, a));
		
		//A-B-C
		graph.addVertex(c);
		graph.addEdge(c, b);
		graph.addEdge(b, c);
		assertEquals(2, graph.getDistance(a, c));
		
		//A-B-C D
		graph.addVertex(d);
		assertEquals(-1, graph.getDistance(b, d));
		
		//多点图
		graph.addVertex(e);
		graph.addVertex(f);
		graph.addVertex(g);
		graph.addEdge(c, d);
		graph.addEdge(d, c);
		graph.addEdge(c, a);
		graph.addEdge(a, c);
		graph.addEdge(c, e);
		graph.addEdge(e, c);
		graph.addEdge(d, f);
		graph.addEdge(f, d);
		graph.addEdge(b, f);
		graph.addEdge(f, b);
		assertEquals(1, graph.getDistance(b, f));
		assertEquals(2, graph.getDistance(a, f));
		assertEquals(3, graph.getDistance(e, f));
		assertEquals(3, graph.getDistance(f, e));
		assertEquals(-1, graph.getDistance(e, g));
		assertEquals(-1, graph.getDistance(g, e));
		assertEquals(2, graph.getDistance(a, d));
		assertEquals(2, graph.getDistance(a, e));
	}
	
	/*
	 * Testing strategy
	 * 	  no input, output is "A"
	 */
	@Test
	public void testPersonGetName() {
		Person a = new Person("A");
		
		assertEquals("A", a.getName());
	}
	
	/*
	 * Testing strategy
	 * 	  no input, output is false (Person B)
	 * 	  no input, output is true (Person A)
	 */
	@Test
	public void testPersonEquals() {
		Person a1 = new Person("A");
		Person a2 = new Person("B");
		Person a3 = new Person("A");
		
		assertFalse(a1.equals(a2));
		assertTrue(a1.equals(a3));
	}
	
	/*
	 * Testing toString
	 */
	@Test
	public void testToString() {
		Person a = new Person("A");
		String s = "[Class: P2.Person,name: A]";
		System.out.println(a.toString());
		assertEquals(s, a.toString());
	}

}