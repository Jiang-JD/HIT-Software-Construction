/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Tests for ConcreteVerticesGraph.
 * 
 * This class runs the GraphInstanceTest tests against ConcreteVerticesGraph, as
 * well as tests for that particular implementation.
 * 
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteVerticesGraphTest extends GraphInstanceTest {
    
    /*
     * Provide a ConcreteVerticesGraph for tests in GraphInstanceTest.
     */
    @Override public Graph<String> emptyInstance() {
        return new ConcreteVerticesGraph<String>();
    }
    
    /*
     * Testing ConcreteVerticesGraph...
     */
    
    /* 
     * 测试策略
     * 	  
     * 输入划分：
     * 	  无输入，图划分为：空图，只有点的图，包含点和边的图
     * 
     * 	  toString()
     * 	  	空图，无输入，返回相应字符串
     * 		只包含点的图，无输入，返回相应字符串
     * 		包含点和边的图，无输入，返回相应字符串
     * 
     * Testing strategy for ConcreteVerticesGraph.toString()
     * 
     * Partition the input:
     * 	  empty graph, only have vertex, both vertex and edges
     * 
     * 	  toString()
     * 		empty graph, no input, output is string
     * 		graph only has vertexes, no input, output is string
     * 		graph has both vertexes and edges, no input, output is string
     * 	  
     */
    @Test
    public void testToString() {
    	Graph<String> graph = emptyInstance();
    	
    	String s1 = "[图顶点数目：0, 有向边数目：0]\n[]\n[]";
    	
    	assertEquals("预期输出串相等", s1, graph.toString());
    	
    	graph.add("A");
    	graph.add("B");
    	graph.add("C");
    	
    	String s2 = "[图顶点数目：3, 有向边数目：0]\n[A, B, C]\n[]";
    	
    	assertEquals("预期输出相等", s2, graph.toString());
    	
    	graph.set("A", "B", 1);
    	graph.set("B", "A", 2);
    	
    	String s3 = "[图顶点数目：3, 有向边数目：2]\n[A, B, C]\n[[有向边：起点为：A, 终点为：B, 权重为：1]\n"
    			+ "[有向边：起点为：B, 终点为：A, 权重为：2]\n]";
    	
    	assertEquals("预期输出相等", s3, graph.toString());
    }
    
    /*
     * Testing Vertex...
     */
    
    /*
     * Vertex测试策略
     * 
     * Vertex为可变类，对输入的等价类划分为：
     * 	  addEdge(): 	权重 >0, 0, <0
     * 	  getEdges():	空图，带边图
     * 	  toString():	空图，带边图
     * 
     * Testing strategy for Vertex
     * 
     * Since Vertex is mutable, partition of input as follows:
     * 	  addEdge():	weight >0,0,<0
     * 	  getEdges():	empty graph, nonempty graph
     * 	  toString():	empty graph, nonempty graph
     */
    @Test
    public void testGetVertex() {
    	assertEquals("预期输出为A", "A", new Vertex<String>("A").getVertex());
    }
    
    
    /*
     * 测试策略
     * 	  addEdges()
     * 		input weight >0, output is true
     * 		input weight =0, output is false
     * 		input weight <0, output is false
     */
    @Test
    public void testAddEdges() {
    	assertFalse("预期为false", new Vertex<String>("A").addEdge("B", -1));
    	assertFalse("预期为false", new Vertex<String>("A").addEdge("B", 0));
    	assertTrue("预期为true", new Vertex<String>("A").addEdge("B", 1));
    	
    }
    
    /*
     * 测试策略
     * 	  getEdges()
     * 		empty graph, no input, output is empty map
     * 		nonempty graph, no input, output is nonempty map
     */
    @Test
    public void testGetEdges() {
    	Vertex<String> v = new Vertex<>("A");
    	
    	assertEquals("预期相同", new HashMap<String, Integer>() , v.getEdges());
    	
    	v.addEdge("B", 0);
    	v.addEdge("C", 1);
    	
    	Map<String, Integer> map = new HashMap<String, Integer>();
    	map.put("C", 1);
    	
    	assertEquals("预期相同", map, v.getEdges());
    }
    
    /*
     * 测试策略
     * 	  toString()
     * 		empty graph, no input, output is empty string
     * 		nonempty graph, no input, output is nonempty string
     */
    @Test
    public void testVertexToString() {
    	Vertex<String> v = new Vertex<>("A");
    	
    	String s1 = "";
    	assertEquals("预期相同", s1, v.toString());
    	
    	v.addEdge("B", 1);
    	
    	String s2 = "[有向边：起点为：A, 终点为：B, 权重为：1]\n";
    	assertEquals("预期相同", s2, v.toString());
    }
    
}
