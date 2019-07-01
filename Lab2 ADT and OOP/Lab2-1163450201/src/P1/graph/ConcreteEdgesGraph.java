/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An implementation of Graph.
 * 
 * <p>PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteEdgesGraph<L> implements Graph<L> {
    
    private final Set<L> vertices = new HashSet<>();
    private final List<Edge<L>> edges = new ArrayList<>();
    
    // Abstraction function:
    // 		AF(vertices,edges) = 一个包含顶点和带权有向边的带权有向图
    //		AF(vertices,edges) = A weight direct Graph with vertices and weight arc
    //
    // Representation invariant:
    //   	vertices保存ConcreteGraph图的顶点集，点集中对象非空
    //		edges保存ConcreteGraph图中的有向边，有向边中的点必须在vertices中 
    // 		edges中的权重为正整数
    //
    //		vertices is a set of Graph's vertices
    //		edges is a list saves all the edges in graph which source|target vertex MUST in the vertices
    //		the weight of edge MUST >0
    //
    // Safety from rep exposure:
    //  	所有的rep都是private的，并且所有获取rep的方法都做了防御性拷贝
    //		All fields are private and vertices() make defensive copies to avoid sharing data with clients.
    
    /**
     * 初始化一个新的，空的ConcreteGraph。
     * 
     * <br>Initialize a new empty ConcreteGraph.
     */
    public ConcreteEdgesGraph() {
    		}
    
    /**
     * 检查RI
     * <p>Check Representation invariant
     * @throws Exception
     */
    private void checkRep() {
    	for(L v: vertices) {
    		assert v != null : "点集中对象为null!";
    	}
    	for(Edge<L> e: edges) {
    		assert vertices.contains(e.getSource()) : "有向边起点不在点集中!";
    		assert vertices.contains(e.getTarget()):"有向边终点不在点集中!";
    		assert e.getWeight() > 0 :"有向边权重非正!";
    	}
    }
    
    /**
     * 检查L参数的合法性，是否满足RI
     * <br>Check the validity of L parameter  
     * @param l 参数L
     * @return 合法返回true，否则返回false
     * 			<br>True if valid
     */
    private boolean checkL(L l) {
    	if(l == null) {
        	System.out.println("空对象");
        	return false;
        }
        if(l.equals("")) {
        	System.out.println("名称不能为空");
        	return false;
        }
        return true;
    }
    
    @Override 
    public boolean add(L vertex) {
        if(!checkL(vertex)) return false;
        if(vertices.contains(vertex)) {
        	System.out.println("顶点已存在");
        	return false;
        }
        vertices.add(vertex);
        checkRep();
        return true;
    }
    
    @Override 
    public int set(L source, L target, int weight) {
        if(!checkL(source) || !checkL(target)) return -1;
        if(weight < 0) {
        	System.out.println("权重非负");
        	return -1;
        }
        
        
        /*
         * 	首先判断是否边集中存在
         * 		若存在则更新有向边或着删除有向边
         * 		若不存在则添加边并且添加点
         */
        int oldweight = -1;
        boolean f = false;
        for(Edge<L> e: edges) {
        	if(e.getSource().equals(source) && e.getTarget().equals(target)) {
        		if(weight == 0) {
        			oldweight = e.getWeight(); //保存权重
        			edges.remove(e);
        			f = true;
        		}
        		else {
        			oldweight = e.getWeight(); //保存旧权重
        			edges.remove(e); //这里不会影响edges结构使得for循环出现问题
        	        Edge<L> edge = new Edge<L>(source, target, weight);
        			edges.add(edge);  //为了保证不可变性，拷贝新的edge对象加入edges，同时删除旧元素
        			f = true;
        		}
        		break;
        	}
        }  
        if(!f) {
        	if(weight != 0) {
        		vertices.add(source);
        		vertices.add(target);
        		Edge<L> edge = new Edge<L>(source, target, weight);
        		edges.add(edge);
        		oldweight = 0;
        	}
        }
        //checkRep
        checkRep();
        return oldweight;
    }
    
    @Override 
    public boolean remove(L vertex) {
    	if(!checkL(vertex)) return false;
    	List<Edge<L>> remove = new ArrayList<Edge<L>>();
        if(vertices.contains(vertex)) {
        	vertices.remove(vertex);
        	for(Edge<L> e: edges) {
        		if(e.getSource().equals(vertex) || e.getTarget().equals(vertex)) {
        			remove.add(e); //记录待删除边
        		}
        	}
        	edges.removeAll(remove); //一次性删除
        	checkRep();
        	return true;
        }
        return false;
    }
    
    /**
     * 获得图的顶点集。
     * <p>Get the set of vertices.
     * @return Set类型图的顶点集 <p>A Set contains Vertices
     */
    @Override 
    public Set<L> vertices() {
        return new HashSet<L>(vertices);
    }
    
    @Override 
    public Map<L, Integer> sources(L target) {
    	if(!checkL(target)) return null;
    	Map<L, Integer> sources = new HashMap<L, Integer>();
    	for(Edge<L> e: edges) {
    		if(e.getTarget().equals(target)) {
    			sources.put((L) e.getSource(), e.getWeight());
    		}
    	}
    	//checkRep
    	checkRep();
    	return sources;
    }
    
    @Override 
    public Map<L, Integer> targets(L source) {
    	if(!checkL(source)) return null;
    	Map<L, Integer> targets = new HashMap<L, Integer>();
    	for(Edge<L> e: edges) {
    		if(e.getSource().equals(source)) {
    			targets.put((L) e.getTarget(), e.getWeight());
    		}
    	}
    	//checkRep
    	checkRep();
    	return targets;
    }
    
    /**
     * 	生成图的描述字符串，显示图的定点数、边数，顶点集，有向边的详细信息
     * 	</br>例如："[图顶点数目：3, 有向边数目：2]\n[A, B, C]\n[[有向边：起点为：A, 终点为：B, 权重为：1]\n]"
     * 
     * <p>Initialize a string depicts the Graph. Including the number of vertices,edges,direct arc.
     * <br>i.e "[图顶点数目：3, 有向边数目：2]\n[A, B, C]\n[[有向边：起点为：A, 终点为：B, 权重为：1]\n"
     * @return 描述字符串 <br>string depicts graph
     */
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	sb.append("[图顶点数目："+ vertices.size() +", 有向边数目：" + edges.size()+"]\n");
    	sb.append(vertices.toString() + "\n");
    	sb.append("[");
    	for(Edge<L> e: edges) {
    		sb.append(e.toString()+"\n");
    	}
    	sb.append("]");
    	return sb.toString();
    }
    
}

/**
 * 这是一个不可变的数据类型，表示有向图中的一条带权有向边。有向边中包含起点、终点和权重。
 * 其中权重为非负整数。
 * 这个类是ConcreteEdgesGraph的内部的类。
 * Edge类比较的时候不能用equals，需要使用自身的isEqual方法。
 * 
 * <p>This is a immutable data type.Edge is a weight edge in Graph.Edge contains source,target and weight.
 * The weight of edge should be non-zero and nonnegative.
 * Edge is an internal class of ConcreteGraph.
 * <br>equals() in Edge is unable, need to use isEqual() to compare.
 * 
 * <p>PS2 instructions: the specification and implementation of this class is
 * up to you.
 * 
 */
class Edge<L> {
    
	private L source;  //源点
	private L target;  //target
	private int weight;		//权重，非负,initialized to 0
    
    // Abstraction function:
    //   AF(source, target, weight) = 一条在有向图中的有向边，包含起点，终点和权重
    //	 AF(source, target, weight) = an direct edge in graph, includes source, target and weight.
	//
	// Representation invariant:
    //   source: 有向边非空L对象
	//   target: 有向边非空L对象
	//   weight > 0
	//
    // Safety from rep exposure:
    //   所有的域都是private的，L是immutable的，int是primitive的
	//   All the fields are private, type L is immutable and int is primitive type.
    
    /**
     * 初始化一个新的Edge对象，包含起点，终点和权重
     * <br>Initialize a new Edge object, includes source, target and weight.
     * 
     * @param source 有向边的非空起点  nonempty source
     * @param target 有向边的非空终点    nonempty target
     * @param weight 有向边的非负、非零权重  nonnegative, nonzero weight
     * @throws Exception 参数不合法时抛出异常
     */
    public Edge(L source, L target, int weight) {
    	if(weight <= 0) {
    		try {
				throw new Exception("警告：权重非负");
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	if(source == null || target == null) {
    		try {
    			throw new Exception("警告：空对象");
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
    	if(source.equals("") || target.equals("")) {
    		try {
    			throw new Exception("警告：空字符串");
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
    	this.source = source;
    	this.target = target;
    	this.weight = weight;
    }
    
    /**
     * 检查RI
     * <br>Check RI
     * @return True if RI, else False.
     */
    private boolean checkRep() {
    	if(weight <= 0) {
    		System.out.println("警告：权重非负");
    		return false;
    	}
    	if(source == null || target == null) {
    		System.out.println("警告：空对象");
    		return false;
    	}
    	return true;
    }
    
    
    /**
     * 取得source点
     * <br>Get source
     * @return Vertex source
     */
    public L getSource() { 
    	return source;
    }
    
    /**
     * 取得target点
     * <br>Get target
     * @return Vertex target
     */
    public L getTarget() {
    	return target;
    }
    
    /**
     * 获得边权重
     * <br>Get weight
     * @return 权重 weight
     */
    public int getWeight() {
    	return weight;
    }
    
    
    /**
     * 将这个边与其他边比较，只有当两个端点和权重相同时才相等，要求Edge对象不能为null。
     * <br>Compare this edge with another edge, it's true if and only if the argument is not null and 
     * is a edge object that has the same vertex and weight. 
     * 
     * @param edge edge类
     * @return true当两个端点和权重相同, 否则false。 
     * <br>True when a edge object that has the same vertex and weight.
     */
    public boolean isEqual(Edge<L> edge) {
    		return this.source.equals(edge.getSource()) 
    				&& this.target.equals(edge.getTarget()) 
    				&& (this.weight == edge.getWeight());
    }

    /**
     * 生成描述有向边的字符串，例如 "[有向边：起点为：A，终点为：B，权重为：5]"
     * <br>Generate a string describe the direct edge. i.e. [有向边：起点为：A，终点为：B，权重为：5]
     * 
     * @return 描述有向边的字符串 <br>a string describe the direct edge
     */
    @Override
    public String toString() {
    	return "[有向边：起点为："+ getSource() + ", 终点为：" + getTarget() + ", 权重为：" + getWeight() + "]";
    }
    
}
