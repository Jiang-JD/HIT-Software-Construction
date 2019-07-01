/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

/**
 * Tests for instance methods of Graph. 测试实例方法
 * 
 * <p>PS2 instructions: you MUST NOT add constructors, fields, or non-@Test 	你不要添加构造器，域或者没有@test的函数，
 * methods to this class, or change the spec of {@link #emptyInstance()}. 		也不要改变emptyInstance的要求
 * Your tests MUST only obtain Graph instances by calling emptyInstance(). 		你的测试必须仅通过调用emptyInstance()来获取图形实例
 * Your tests MUST NOT refer to specific concrete implementations. 				你的测试不能引用具体的实现？？？
 */
public abstract class GraphInstanceTest {
    
    //测试策略
    // 
	//	实例单元测试分别对Graph的不同方法进行测试，输入划分为
	//		add()
	//		  vertex：	单个顶点，重复顶点		
	//		set()
	//		  source/target:加入vertices中，没加入vertices中
	//		  weight:	>0, 0
	//		remove()
	// 		  vertex:	有边相连，没有边相连
	//					图中存在，图中不存在
	//		sources()
	//		  target:	有边相连（以target作为终点），没边相连
	//		targets()
	//		  source:	有边相连（以source作为起点），没边相连
	//
	// Testing strategy
	//
	//	Partition the operate as follows
	//		add()
	//		  vertex: single vertex, duplicate vertexes
	//		set()
	//		  source/target: exist in vertices, not exist in vertices
	//		  weight: >0, 0
	//		remove()
	//		  vertex: there are edges connect it, no edge
	//				  in this graph, not in this graph
	//		sources()
	//		  target: there are edges connect it(as target), no edge
	//		targets()
	//		  source: there are edges connect it(as source), no edge
    
    /**
     * Overridden by implementation-specific test classes.
     * 
     * @return a new empty graph of the particular implementation being tested
     */
    public abstract Graph<String> emptyInstance();
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testInitialVerticesEmpty() {
        assertEquals("expected new graph to have no vertices",
                Collections.emptySet(), emptyInstance().vertices());
    }
    
    /* 
     * 测试策略
     * 	  add() 
     * 		只输入一点，输出为true
     * 		重复添加同一点，输出为false
     * 		通过vertices()观察是否添加成功	
     * 	  覆盖：
     * 		单个顶点，重复顶点
     * 	
     * Testing strategy
     * 	  add()
     * 		only one input, output is true
     * 		add the same vertex, output is false
     * 		observe with vertices()
     * 	  covers:
     * 		single vertex, duplicate vertexes
     */
    @Test  
    public void testAddVertex() {
    	Set<String> set = new HashSet<String>();
    	set.add("A");
    	Graph<String> graph = emptyInstance();
    	graph.add("A");
    	
    	assertEquals("预期只有一点添加", set, graph.vertices());
    	
    	assertFalse("预期结果为false", graph.add("A"));
    }
    
    /* 
     * 测试策略：
     * 	  set()
     * 		添加一条新的边，输出为0
     * 		输入新权重和同一条边两个端点，更新一条边，输出为边的旧权重
     * 		同一对端点，输入权重为0，删除一条边，输出为旧权重
     * 		再次添加同一对端点，输入为权重，输出为0
     * 		测试多个点和一条边
     * 		通过vertices()观察
     * 	  覆盖：
     * 		source/target加入vertices中，没加入vertices中
	 *		weight > 0,weight = 0
     * 
     * Testing strategy
     * 	  set()
     * 		add a new edge,output is 0
     * 		add the same edge with new weight, update it, output is old weight
     * 		same edge with input weight is 0, delete edge, output is old weight
     * 		add the same edge again, input is new weight, output is 0
     * 		test multiple vertexes and one edge
     * 		observe with vertices()
     * 	  covers:
     * 		source/target exist in vertices, not in vertices
	 *		weight > 0,weight = 0
     */
    @Test
    public void testSet() {
    	Graph<String> graph = emptyInstance();
    	graph.add("A");graph.add("B");
    	
    	assertEquals("预期返回值为0", 0, graph.set("A", "B", 3)); //add new edge
    	
    	assertEquals("预期返回值为3", 3, graph.set("A", "B", 2)); //update edge
    	
    	assertEquals("预期返回值为2", 2, graph.set("A", "B", 0)); //delete edge
    	
    	assertEquals("预期返回值为0", 0, graph.set("A", "B", 3)); //add new edge
    	
    	Set<String> set = new HashSet<String>(); 
    	set.add("A");
    	set.add("B");
    	set.add("C");
    	set.add("D");
    	graph.set("C", "D", 7);
    	
    	assertEquals("预期点集相等", set, graph.vertices());
    	
    }
    
    /*
     * 测试策略
     * 	  remove()
     * 		输入点集中不存在的点，输出为false
     * 		输入为点集中存在的点且该点不与任何边相连，输出为true
     * 		输入为点集中存在的点且该点为边的起点，输出为true
     * 		通过set()观察上一测试中的点是否有边与其相连，输出为0
     * 	  覆盖：
     * 		vertex:	有边相连，没有边相连
     * 				图中存在，图中不存在
     * 
     * Testing strategy
     * 	  remove()
     * 		input the vertex that not exist in vertices, output is false
     * 		input the vertex in vertices and with no edge connect, output is true
     * 		input the vertex in vertices and with one edge connect, output is true
     * 		observe with set() to check if exist edge connect to vertex, output is 0
     *	  covers:
     *		vertex has edges connected, no edges connected
     *		vertex in this graph, not in this graph
     */
    
    @Test
    public void testRemove() {
    	//A -2-> B , C
    	Graph<String> graph = emptyInstance();
    	graph.add("A");
    	graph.add("B");
    	graph.add("C");
    	graph.set("A", "B", 2);
    	
    	assertFalse("预期返回false", graph.remove("D"));
    	
    	assertTrue("预期返回true", graph.remove("C"));
    	
    	assertTrue("预期返回true", graph.remove("A"));
    	
    	assertEquals("预期返回0，即AB不存在边", 0,graph.set("A", "B", 2));
    }
    
    /*
     * 测试策略
     * 	  sources()
     * 		添加多个顶点和有向边，测试其中一点的sourcesMap
     * 		输入为其中一点
     * 		通过构造正确MAP来比较
     * 		A -1-> B <-2- C , B -3-> D; sources(B) = {<A, 1>,<C, 2>}
     *	  覆盖：
     *		有边相连（以target作为终点），没边相连
     * 
     * Testing strategy
     * 	  sources()
     * 		add multiple vertexes and edges, test sources with one vertex
     * 		input is one vertex in vertices
     * 		observe with a correct Map 
     * 		A -1-> B <-2- C , B -3-> D; sources(B) = {<A, 1>,<C, 2>}
     * 	  covers:
     * 		there are edges connect it(as target), no edge
     */
    @Test
    public void testSources() {
    	Graph<String> graph = emptyInstance();
    	graph.add("A");
    	graph.add("B");
    	graph.add("C");
    	graph.add("D");
    	graph.set("A","B",1);
    	graph.set("C","B",2);
    	graph.set("B","D",3);
    	
    	Map<String, Integer> map = new HashMap<String, Integer>();
    	map.put("A", 1);
    	map.put("C", 2);
    	
    	assertEquals("预期两个Map相等", map, graph.sources("B"));
    }
    
    /*
     * 测试策略
     * 	  targets()
     * 		添加多个顶点和有向边，测试其中一点的targetsMap
     * 		输入为其中一点
     * 		通过构造正确MAP来比较
     * 		A -1-> B A -2-> C ,B -3-> D; targets(B) = {<B, 1>,<C, 2>}
     * 	  覆盖：		
     * 		有边相连（以source作为起点），没边相连
     * 
     * Testing strategy
     * 	  targets()
     * 		add multiple vertexes and edges, test targets with one vertex
     * 		input is one vertex in vertices
     * 		observe with a correct Map 
     * 		A -1-> B A -2-> C ,B -3-> D; targets(B) = {<B, 1>,<C, 2>}
     * 	  covers:
     * 		there are edges connect it(as source), no edge
     */
    @Test
    public void testTargets() {
    	Graph<String> graph = emptyInstance();
    	graph.add("A");
    	graph.add("B");
    	graph.add("C");
    	graph.add("D");
    	graph.set("A","B",1);
    	graph.set("A","C",2);
    	graph.set("B","D",3);
    	
    	Map<String, Integer> map = new HashMap<String, Integer>();
    	map.put("B", 1);
    	map.put("C", 2);
    	graph.targets("A");
    	assertEquals("预期两个Map相等", map, graph.targets("A"));
    }
    
}
