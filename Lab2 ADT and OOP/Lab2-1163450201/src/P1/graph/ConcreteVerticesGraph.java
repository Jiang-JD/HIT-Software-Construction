/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An implementation of Graph.
 * 
 * <p>
 * PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteVerticesGraph<L> implements Graph<L> {

	private final List<Vertex<L>> vertices = new ArrayList<>();

	// Abstraction function:
	// AF(vertices) = 一个一个包含顶点和带权有向边的带权有向图
	// AF(vertices) = A weight direct Graph with vertices and weight arc
	//
	// Representation invariant:
	// vertices保存有向图中的点集和有向边的关系，点集中vertex对象非空
	// vertices is a list of vertices and it's relative edge in graph, all the
	// vertex in vertices not null.
	//
	// Safety from rep exposure:
	// vertices由private和final标示，即只能修改一次。所有的get方法都做了（vertices())防御性拷贝。
	// All the fields are private and final(i.e. vertices), and vertices() makes
	// defensive copy.

	/**
	 * 初始化一个新的空的有向图 <br>
	 * Initialize a new, empty direct graph.
	 */
	public ConcreteVerticesGraph() {
	
	}

	/**
	 * 检查RI <br>
	 * checkRI
	 * 
	 * @return 如果rep合法，返回true
	 */
	private void checkRep() {
		for (Vertex<L> v : vertices) {
			assert v != null : "对象为null";
		}
	}

	/**
	 * 检查L参数的合法性，是否满足RI <br>
	 * Check the validity of L parameter
	 * 
	 * @param l 参数L
	 * @return 合法返回true，否则返回false <br>
	 *         True if valid
	 */
	private boolean checkL(L l) {
		if (l == null) {
			System.out.println("空对象");
			return false;
		}
		if (l.equals("")) {
			System.out.println("名称不能为空");
			return false;
		}
		return true;
	}

	@Override
	public boolean add(L vertex) {
		if (!checkL(vertex))
			return false;
		for (Vertex<L> v : vertices) {
			if (v.getVertex().equals(vertex)) {
				System.out.println("不能重复添加");
				return false;
			}
		}

		vertices.add(new Vertex<L>(vertex));
		checkRep();
		return true;
	}

	@Override
	public int set(L source, L target, int weight) {
		if (!checkL(source) || !checkL(target))
			return -1;
		if (weight < 0) {
			System.out.println("权重非负");
			return -1;
		}

		/*
		 * 判断边在集合中是否存在，若是，则进行更新或删除，若否，则进行添加
		 */
		int oldweight = -1;
		boolean f = false;
		boolean exist1 = false; // source是否存在
		Vertex<L> exv = null;
		// 检查点集中是否存在
		for (Vertex<L> v : vertices) {
			if (v.getVertex().equals(source)) {
				exist1 = true;
				exv = v;

				// 检查是否存在target有向边
				Iterator<Map.Entry<L, Integer>> it = v.getEdges().entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry<L, Integer> me = it.next();
					if (me.getKey().equals(target)) { // 边集中包含target有向边
						// 判断是否是删除或者更新
						oldweight = me.getValue(); // 保存旧权重
						// 移除元素
						if (weight == 0) {
							f = true;
							it.remove(); // 移除边
						}
						// 更新元素
						else {
							f = true;
							me.setValue(new Integer(weight)); // 更新值
						}
						break;
					}
				}
			}
		}

		// 点集中不存在，添加
		if (!f) {
			oldweight = 0;
			// 点存在，边不存在
			if (exist1) {
				exv.addEdge(target, weight);
				add(target);
			}
			// 点不存在
			else {
				add(source);
				for (Vertex<L> v : vertices) {
					if (v.getVertex().equals(source)) {
						v.addEdge(target, weight);
					}
				}
				add(target);
			}

		}
		checkRep();
		return oldweight;
	}

	@Override
	public boolean remove(L vertex) {
		if (!checkL(vertex))
			return false;
		for (Vertex<L> v : vertices) {
			if (v.getVertex().equals(vertex)) {
				// 先在vertices中移除这个点，之后删除所有以该点作为终点的有向边。
				// WARNNING：可能存在修改vertices结构，导致for失效风险
				vertices.remove(v);
				for (Vertex<L> other : vertices) {
					other.getEdges().remove(vertex); // 删除other点中所有相关边
				}
				checkRep();
				return true;
			}

		}
		return false;
	}

	@Override
	public Set<L> vertices() {
		Set<L> vset = new HashSet<L>();
		for (Vertex<L> v : vertices) {
			vset.add(v.getVertex());
		}

		return vset;
	}

	@Override
	public Map<L, Integer> sources(L target) {
		if (!checkL(target))
			return null;
		Map<L, Integer> sources = new HashMap<L, Integer>();
		for (Vertex<L> v : vertices) {
			if (v.getEdges().containsKey(target)) {
				sources.put(v.getVertex(), v.getEdges().get(target));
			}
		}
		checkRep();
		return sources;

	}

	@Override
	public Map<L, Integer> targets(L source) {
		if (!checkL(source))
			return null;
		for (Vertex<L> v : vertices) {
			if (v.getVertex().equals(source)) {
				checkRep();
				return v.getEdges();
			}
		}
		return null;
	}

	/**
	 * 生成图的描述字符串，显示图的定点数、边数，顶点集，有向边的详细信息 <br>
	 * 例如："[图顶点数目：3, 有向边数目：2]\n[A, B, C]\n[[有向边：起点为：A, 终点为：B, 权重为：1]\n]"
	 * 
	 * <p>
	 * Initialize a string depicts the Graph. Including the number of
	 * vertices,edges,direct arc.
	 * 
	 * @return 描述字符串 <br>
	 *         string depicts graph
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		int edges = 0;
		int i = 0;
		for (Vertex<L> v : vertices) {
			edges += v.getEdges().size();
		}
		sb.append("[图顶点数目：" + vertices.size() + ", 有向边数目：" + edges + "]\n[");

		if (!vertices.isEmpty()) {
			for (i = 0; i < vertices.size() - 1; i++) {
				sb.append(vertices.get(i).getVertex());
				sb.append(", ");
			}
			sb.append(vertices.get(i).getVertex() + "]\n[");
		} else {
			sb.append("]\n[");
		}
		for (Vertex<L> v : vertices) {
			sb.append(v.toString());
		}
		sb.append("]");
		return sb.toString();
	}

}

/**
 * 表示图中的一个顶点和与其相关的边。可变类（Mutable)。Vertex属于ConcreteVerticesGraph的内部的类。
 * Vertex包含一个顶点和以该顶点作为起点的有向边。顶点用L标签标示。
 * 
 * <p>
 * Implement a vertex and relative edge in graph.Vertex is a Mutable, internal
 * class of ConcreteVerticesGraph.Vertex contains a vertex and edges which use
 * this vertex as source. The label of vertex is L.
 * <p>
 * PS2 instructions: the specification and implementation of this class is up to
 * you.
 */
class Vertex<L> {

	private L vertex;
	private final Map<L, Integer> edges = new HashMap<L, Integer>(); // <终点，对应权值>

	// Abstraction function:
	// AF(vertex, edges) = 一个有向图中的顶点和其相关的带权有向边
	// AF(vertex, edges) = A vertex and it's relative weight edges in this graph.
	//
	// Representation invariant:
	// vertex是一个不为null的L类型对象
	// edges中保存着不为null的终点和大于0的权重
	//
	// vertex: a L type non-null object.
	// edges: contains a non-null target vertex and an edge which weight > 0
	//
	// Safety from rep exposure:
	// 所有的域都是private的，L是immutable的，edges是final的，但是getEdges()会返回给客户端它的
	// 指向，因此存在表示泄露风险。
	// All fields are private, type L is immutable. edges is decorated by final. BUT
	// getEdges()
	// will pass the node to the clients, thus there exists risk on REP EXPOSURE.

	/**
	 * 初始化一个不包含任何相联边的顶点。顶点中保存顶点自身和以其作为起点的有向边的终点和权重。
	 * 
	 * <p>
	 * Initialize a vertex without edges.Vertex contains vertex itself and the
	 * direct edges which use this vertex as source vertex. The edge consist of
	 * target vertex and weight.
	 * 
	 * @param vertex 顶点名称
	 */
	public Vertex(L vertex) {
		if (vertex == null) {
			try {
				throw new Exception("警告：空对象");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (vertex.equals("")) {
			try {
				throw new Exception("警告：空字符串");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		this.vertex = vertex;
	}

	/**
	 * 检查rep不变性 <br>
	 * Check Rep invariant
	 * 
	 * @return True if RI, else false
	 */
	public void checkRep() {
		if (vertex == null) {
			try {
				throw new Exception("vertex对象为null");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Set<L> k = edges.keySet();
		Collection<Integer> v = edges.values();
		for (L l : k) {
			if (l == null) {
				try {
					throw new Exception("对象为null");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		for (Integer i : v) {
			if (i <= 0) {
				try {
					throw new Exception("权重非正");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 获得顶点名称 <br>
	 * Get Vertex
	 * 
	 * @return 顶点名称
	 */
	public L getVertex() {
		return vertex;
	}

	/**
	 * 获得以顶点作为起点的所有有向边的Map，key为终点，values为权重
	 * <p>
	 * Get all edges use this vertex as source vertex.Return a directly node point
	 * to Map<target, weight>. which is not a copy.
	 * 
	 * @return 有向边Map
	 */
	public Map<L, Integer> getEdges() {
		return edges;
	}

	/**
	 * 添加有向边，权重为正整数
	 * <p>
	 * Add direct edge, the edge's weight should be > 0 and type is integer.
	 * 
	 * @param target 有向边的终点
	 * @param weight 有向边的权重
	 * @return 是否添加成功
	 */
	public boolean addEdge(L target, int weight) {
		if (target == null) {
			System.out.println("空对象");
			return false;
		}
		if (target.equals("")) {
			System.out.println("名称不能为空");
			return false;
		}
		if (weight <= 0) {
			System.out.println("权重应该非负非零");
			return false;
		}
		edges.put(target, weight);
		checkRep();
		return true;
	}

	/**
	 * 将这个顶点与其他顶点比较，只有当所有属性相同时才判断为相等，要求输入vertex非null。
	 * 
	 * <p>
	 * Compare this vertex with another vertex, two vertexes are equal if and only
	 * if when all properties are equal.
	 * 
	 * @param v 待判断的顶点 another vertex need compare
	 * @return 是否相等 true if two vertexes have same values.
	 */
	public boolean isEqual(Vertex<L> v) {
		return vertex.equals(v.getVertex()) && edges.equals(v.getEdges());
	}

	/**
	 * 生成描述Vertex的信息的字符串，包括顶点名称，以及相关的连接有向边的终点和权重
	 * 
	 * <p>
	 * Generate a string depicts information about vertex, includes the name of
	 * vertex, relative edges contains source, target and weight.
	 * 
	 * @return a String depicts information about vertex
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Set<L> keys = edges.keySet();
		for (L s : keys) {
			sb.append("[有向边：起点为：" + vertex + ", 终点为：" + s + ", 权重为：" + edges.get(s) + "]\n");
		}
		return sb.toString();
	}

}
