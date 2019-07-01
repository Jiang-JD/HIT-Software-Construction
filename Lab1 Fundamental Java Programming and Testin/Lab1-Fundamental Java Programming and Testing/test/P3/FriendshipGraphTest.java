package P3;
import static org.junit.Assert.*;
 
import org.junit.Test;


public class FriendshipGraphTest {
	FriendshipGraph graph = new FriendshipGraph();
	
	/**
	 * 测试addVertex
	 * 	添加空对象
	 * 	添加正确对象
	 */
	@Test
	public void addVertexTest() {
		Person a = null;
		assertEquals(true , graph.addVertex(new Person("Kit")));
		assertEquals(false , graph.addVertex(new Person("Kit")));
		assertEquals(false , graph.addVertex(a));
	}
	
	/**
	 * 测试addEdge
	 * 	添加两端其中至少有一个空对象，边的两端顶点都已添加到点集中
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
	
	/**
	 * 测试getDistance
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

}
