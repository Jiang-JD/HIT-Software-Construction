/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for ConcreteEdgesGraph.
 * 
 * This class runs the GraphInstanceTest tests against ConcreteEdgesGraph, as
 * well as tests for that particular implementation.
 * 
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteEdgesGraphTest extends GraphInstanceTest {
    
    /*
     * Provide a ConcreteEdgesGraph for tests in GraphInstanceTest.
     */
    @Override public Graph<String> emptyInstance() {
        return new ConcreteEdgesGraph<String>();
    }
    
    /*
     * Testing ConcreteEdgesGraph...
     */
    
    /* 
     * 测试策略
     * 	  
     * 输入划分：
     * 	  无输入，graph分为：空图，只有点的图，包含点和边的图
     * 	  toString()
     * 	  	空图，无输入，返回相应字符串
     * 		只包含点的图，无输入，返回相应字符串
     * 		包含点和边的图，无输入，返回相应字符串
     * 
     * Testing strategy for ConcreteEdgeGraph.toString()
     * 
     * Partition the input:
     * 	  empty graph, only have vertex, both vertex and edges
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
     * Testing Edge...
     */
    
    /* 
     * Edge的测试策略
     * 	  getSource()
     * 		输入边，输出边的起点
     * 	  getTarget()
     * 		输入边，输出边的终点
     * 	  getWeight()
     * 		输入边，输出边的权重
     * 	  toString()
     * 		无输入，输出描述字符串
     * 
     * Testing strategy for Edge
     * 	  getSource()
     * 		input an edge, output is source vertex
     * 	  getTarget()
     * 		input an edge, output is target vertex
     * 	  getWeight()
     * 		input an edge, output is weight
     * 	  toString()
     * 		no input, output is string
     * 
     */
    @Test
    public void testEdge() {
    	Edge<String> edge = new Edge<String>("A","B",1);
    	
    	assertEquals("A", edge.getSource());
    	assertEquals("B", edge.getTarget());
    	assertEquals(1, edge.getWeight());  
    	assertEquals("[有向边：起点为：A, 终点为：B, 权重为：1]", edge.toString());
    }
    
}
