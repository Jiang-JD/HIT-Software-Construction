/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;


import java.util.Map;
import java.util.Set;

/**
 * A mutable weighted directed graph with labeled vertices.
 * Vertices have distinct labels of an immutable type {@code L} when compared 	
 * using the {@link Object#equals(Object) equals} method. 						
 * Edges are directed and have a positive weight of type {@code int}. 			
 * 
 * <p>PS2 instructions: this is a required ADT interface.
 * You MUST NOT change the specifications or add additional methods.
 * 
 * <p>顶点有一个不可变类型的label
 * 比较的时候用equals方法
 * 边也有权重
 * @param <L> type of vertex labels in this graph, must be immutable 			
 * 				<p>L参数是顶点的label类型，必须是不可变类型																注意Graph是个接口
 */

public interface Graph<L> {
    
    /**
     * Create an empty graph.
     * 
     * @param <L> type of vertex labels in the graph, must be immutable
     * @return a new empty weighted directed graph 								一个新的空的带权有向图
     */
    public static <L> Graph<L> empty() {
    	Graph<L> graph = new ConcreteEdgesGraph<L>();  
    	//用户不在乎用什么具体方法实现，所以用Edges实现
    	return graph;
    }
    
    /**
     * Add a vertex to this graph.
     * 
     * @param vertex label for the new vertex 									顶点的标签
     * @return true if this graph did not already include a vertex with the		如果已经存在，返回false
     *         given label; otherwise false (and this graph is not modified) 	除了成功添加，其他都返回false
     *          																这个图不能被改
     */
    public boolean add(L vertex);
    
    /**
     * Add, change, or remove a weighted directed edge in this graph. 			添加，修改，删除有向边
     * If weight is nonzero, add an edge or update the weight of that edge;		如果权重非零，添加或更新那个边的权重
     * vertices with the given labels are added to the graph if they do not		如果添加的时候点还没有加进来，那就加进来
     * already exist.
     * If weight is zero, remove the edge if it exists (the graph is not		如果权重为0，如果边存在就给它移除
     * otherwise modified).
     * 
     * @param source label of the source vertex									起点的label
     * @param target label of the target vertex									目标的label
     * @param weight nonnegative weight of the edge								边的 **非负** 权重
     * @return the previous weight of the edge, or zero if there was no such
     *         edge																如果之前没这条边，返回0，如果有，返回之前的权重
     */
    public int set(L source, L target, int weight);
    
    /**
     * Remove a vertex from this graph; any edges to or from the vertex are
     * also removed.															移除一个点，跟点有关的有向边一起移除
     * 
     * @param vertex label of the vertex to remove
     * @return true if this graph included a vertex with the given label;
     *         otherwise false (and this graph is not modified)
     */
    public boolean remove(L vertex);
    
    /**
     * Get all the vertices in this graph.
     * 
     * @return the set of labels of vertices in this graph
     */
    public Set<L> vertices();
    
    /**
     * Get the source vertices with directed edges to a target vertex and the
     * weights of those edges.
     * 
     * @param target a label
     * @return a map where the key set is the set of labels of vertices such
     *         that this graph includes an edge from that vertex to target, and
     *         the value for each key is the (nonzero) weight of the edge from
     *         the key to target 							给一个target，返回map，关键字是源点，对应值是有向边的非零权重
     */
    public Map<L, Integer> sources(L target);
    
    /**
     * Get the target vertices with directed edges from a source vertex and the
     * weights of those edges.
     * 
     * @param source a label
     * @return a map where the key set is the set of labels of vertices such
     *         that this graph includes an edge from source to that vertex, and
     *         the value for each key is the (nonzero) weight of the edge from
     *         source to the key 							给一个source，返回map，关键字是终点，对应值是有向边的非零权重
     */
    public Map<L, Integer> targets(L source);
    
}
