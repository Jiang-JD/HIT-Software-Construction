package P2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import graph.*;

public class FriendshipGraph {

	// 使用ConcreteVerticesGraph存贮关系网和Person顶点
	private final Graph<Person> graph = new ConcreteVerticesGraph<Person>();

	// Abstract Function:
	// 	AF(graph) = 一个朋友圈关系图
	//
	// Representation invariant:
	// 	graph中顶点不为空，边的权重>0
	//
	// Safety from rep exposure:
	// 	graph由private和final修饰，没有setter方法

	/**
	 * CheckRI
	 */
	private void checkRep() {
		for(Person p : graph.vertices()) {
			assert p!=null:"Person对象为null";
		}
	}
	
	public static void main(String[] args) {
		FriendshipGraph graph = new FriendshipGraph();
		Person rachel = new Person("Rachel");
		Person ross = new Person("Ross");
		Person ben = new Person("Ben");
		Person kramer = new Person("Kramer");
		graph.addVertex(rachel);
		graph.addVertex(ross);
		graph.addVertex(ben);
		graph.addVertex(kramer);
		graph.addEdge(rachel, ross);
		graph.addEdge(ross, rachel);
		graph.addEdge(ross, ben);
		graph.addEdge(ben, ross);
		System.out.println(graph.getDistance(rachel, ross));
		System.out.println(graph.getDistance(rachel, ben));
		System.out.println(graph.getDistance(rachel, rachel));
		System.out.println(graph.getDistance(rachel, kramer));
	}

	/**
	 * 向朋友圈中添加顶点（Person类），Person类要求非空，不能重复添加相同Person， 也就是不能有相同的姓名
	 * 
	 * @param Person类顶点
	 * @return true如果添加成功
	 */
	public boolean addVertex(Person person) {
		if (person == null) {
			System.out.println("输入对象不能为空，请检查输入");
			return false;
		}
		for (Person v : graph.vertices()) {
			if (v.getName().equals(person.getName())) {
				System.out.println("姓名不能重复，重复姓名：" + person.getName());
				return false;
			}

		}
		graph.add(person);
		checkRep();
		return true;
	}

	/**
	 * 向关系图中添加人际关系，添加边为单向边，也就是a指向b的单向关系 顶点不能为空，并且在添加边之前，确保所有的顶点都已经添加入点集 中。
	 * 
	 * @param a 起始点
	 * @param b 目标点
	 * @return true如果添加成功
	 */
	public boolean addEdge(Person a, Person b) {
		if (a == null || b == null) {
			System.out.println("两端不能为空，请检查输入");
			return false;
		}
		if (!(graph.vertices().contains(a))) {
			System.out.println(a.getName() + "未添加到顶点中，请检查输入");
			return false;
		}
		if (!(graph.vertices().contains(b))) {
			System.out.println(b.getName() + "未添加到顶点中，请检查输入");
			return false;
		}
		graph.set(a, b, 1);
		checkRep();
		return true;
	}

	/**
	 * 获得两人之间的最短关系距离 要求所有顶点非空，并且在查找距离之前，确保所有的顶点都已添加到点集中
	 * 
	 * @param source 起始点
	 * @param target 目标点
	 * @return 两点之间最短距离，为整数 例如A<->B<->C , D，则AB之间最短距离为1
	 *         AC之间的最短距离为2，AD之间的最短距离为-1，AA之间最短距离为0.
	 */
	public int getDistance(Person source, Person target) {
		if (source == null || target == null) {
			System.out.println("两端不能为空，请检查输入");
			return -1;
		}

		// 检查是否有顶点未加入顶点集
		if (!(graph.vertices().contains(source))) {
			System.out.println(source.getName() + "未添加到顶点中，请检查输入");
			if (!(graph.vertices().contains(target))) {
				System.out.println(target.getName() + "未添加到顶点中，请检查输入");
			}
			return -1;
		}

		if (!(graph.vertices().contains(target))) {
			System.out.println(target.getName() + "未添加到顶点中，请检查输入");
			if (!(graph.vertices().contains(source))) {
				System.out.println(source.getName() + "未添加到顶点中，请检查输入");
			}
			return -1;
		}

		if (source.equals(target))
			return 0;

		// BFS，depth数组存储各顶点层数，也即最短路径
		int[] depth = new int[graph.vertices().size()]; // 存放各顶点的所在层数
		boolean isFind = false;
		List<Person> vertices = new ArrayList<Person>(graph.vertices());

		Queue<Person> queue = new Queue<Person>(graph.vertices().size());
		queue.makeNull();
		queue.add(source);
		boolean[] visited = new boolean[graph.vertices().size()];
		Person v = null;
		Person p = null;

		visited[vertices.indexOf(source)] = true;
		depth[vertices.indexOf(source)] = 0;

		while (!queue.isEmpty()) {
			v = (Person) queue.Dequeue();
			queue.pull();
			Iterator<Map.Entry<Person, Integer>> it = graph.targets(v).entrySet().iterator(); // 将这个人的所有关系生成迭代器
			if (it.hasNext()) {
				p = it.next().getKey();
			}
			while (p != null) {
				if (visited[vertices.indexOf(p)] == false) {
					queue.add(p);
					visited[vertices.indexOf(p)] = true;
					depth[vertices.indexOf(p)] = depth[vertices.indexOf(v)] + 1; // p层数为父结点层数+1
					if (p.equals(target)) {
						isFind = true; // 找到目标立即跳出循环
						break;
					}
				}
				if (it.hasNext()) {
					p = it.next().getKey();
				} else
					p = null;
			}
			if (isFind)
				break;
		}

		if (!isFind)
			return -1;
		checkRep();
		return depth[vertices.indexOf(p)];
	}

}

/**
 * 循环队列类
 */
class Queue<E> {
	private Object[] queue = null;
	private int maxSize; //最大尺寸
	private int rear; //队尾指针
	private int front; //队首指针
	private int size = 0; //当前大小

	public Queue() {

	}

	public Queue(int initalSize) {
		if (initalSize > 0) {
			this.maxSize = initalSize;
			queue = new Object[initalSize];
			front = rear = 0;
		} else {
			System.out.println("输入值不能为0,当前为: " + initalSize);
			System.exit(0);
		}
	}

	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * 添加元素
	 * 
	 * @param e
	 * @return
	 */
	public boolean add(E e) {
		if (size == maxSize) {
			System.out.println("队列已满");
			return false;
		} else {
			queue[rear] = e;
			rear = (rear + 1) % maxSize;
			size++;
			return true;
		}
	}

	/**
	 * 从队首取出元素，不删除
	 * 
	 * @return 获得的元素
	 */
	@SuppressWarnings("unchecked")
	public E Dequeue() {
		if (isEmpty()) {
			System.out.println("队列为空");
			return null;
		} else {
			return (E) queue[front];
		}
	}

	/**
	 * 从队首删除元素
	 * 
	 * @return
	 */
	public E pull() {
		if (isEmpty()) {
			System.out.println("队列为空");
			return null;
		} else {
			E value = (E) queue[front];
			queue[front] = null;
			front = (front + 1) % maxSize;
			size--;
			return value;
		}
	}

	/**
	 * 获得长度
	 * 
	 * @return
	 */
	public int length() {
		return size;
	}

	/**
	 * 队列置空
	 */
	public void makeNull() {
		Arrays.fill(queue, null);
		size = 0;
		front = 0;
		rear = 0;
	}
}
