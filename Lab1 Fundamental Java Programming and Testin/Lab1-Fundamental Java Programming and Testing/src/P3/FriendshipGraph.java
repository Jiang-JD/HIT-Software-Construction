package P3;

import java.util.*;

public class FriendshipGraph {
	private ArrayList<Person> vertex = new ArrayList<Person>();

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
	 * 向朋友圈中添加顶点（人）
	 * @param person
	 * @return 是否添加成功
	 */
	public boolean addVertex(Person person) {
		if(person == null) {
			System.out.println("输入对象不能为空，请检查输入");
			return false;
		}
		// 检查是否有重名
				for(Person p : vertex) {
					if(p.getNme().equals(person.getNme())) {
					System.out.println("姓名不能重复，重复姓名：" + person.getNme());
					//System.exit(0);
					return false;
					}
				}
		vertex.add(person);
		return true;
	}
	
	/**
	 * 添加关系
	 * @param a
	 * @param b
	 * @return 是否添加成功
	 */
	public boolean addEdge(Person a, Person b) {
		if(a == null || b == null) {
			System.out.println("两端不能为空，请检查输入");
			return false;
		}
		if(!(vertex.contains(a))) {
			System.out.println(a.getNme() + "未添加到顶点中，请检查输入");
			return false;
		}
		if(!(vertex.contains(b))) {
			System.out.println(b.getNme() + "未添加到顶点中，请检查输入");
			return false;
		}
		a.addFriend(b);
		return true;
	}
	
	/**
	 * 获取两人之间的最短距离，利用BFS实现层序遍历，记录层数，因为Person类中将关系放在Set里，所以这里的BFS使用邻接表实现
	 * @param base
	 * @param target
	 * @return 最短距离
	 */
	public int getDistance(Person base, Person target) {
		if(base == null || target == null) {
			System.out.println("两端不能为空，请检查输入");
			return -1;
		}
		
		// 检查是否有顶点未加入顶点集
		if(!(vertex.contains(base))) {
			System.out.println(base.getNme() + "未添加到顶点中，请检查输入");
			if(!(vertex.contains(target))) {
				System.out.println(target.getNme() + "未添加到顶点中，请检查输入");
			}
			return -1;
		}
		
		if(!(vertex.contains(target))) {
			System.out.println(target.getNme() + "未添加到顶点中，请检查输入");
			if(!(vertex.contains(base))) {
				System.out.println(base.getNme() + "未添加到顶点中，请检查输入");
			}
			return -1;
		}
		
		
		if(base.equals(target)) return 0;
		
		// BFS，depth数组存储各顶点层数，也即最短路径
		int[] depth = new int[vertex.size()];  //存放各顶点的所在层数
		boolean isFind = false;
		
		@SuppressWarnings("rawtypes")
		Queue<Person> queue = new Queue<Person>(vertex.size());
		queue.makeNull();
		queue.add(base);
		boolean[] visited = new boolean[vertex.size()];
		Person v = null;
		Person p = null;
		
		visited[vertex.indexOf(base)] = true;
		depth[vertex.indexOf(base)] = 0;
		
		while (!queue.isEmpty()) {
			v = (Person) queue.Dequeue();
			queue.pull();
			Iterator<Person> it = v.getFriendship().iterator();  //将这个人的所有关系生成迭代器
			if(it.hasNext()) {
				p = (Person) it.next();
			}
			while (p != null) {
				if (visited[vertex.indexOf(p)] == false) {
					queue.add(p);
					visited[vertex.indexOf(p)] = true;
					depth[vertex.indexOf(p)] = depth[vertex.indexOf(v)] + 1;  //p层数为父结点层数+1
					if (p.equals(target)) {
						isFind = true;  //找到目标立即跳出循环
						break;
					}
				}
				if(it.hasNext()) {
					p = (Person)it.next();
				}
				else p = null;
			}
			if (isFind) break;
		}
		
		if (!isFind) return -1;
		return depth[vertex.indexOf(p)];
	}
	
	/**
	 * 检查是否存在重名
	 * @return boolean
	 */
	private String isPersonDuplicate() {
		for(int i = 0; i < vertex.size()-1; i++) {
			Person p = vertex.get(i);
			for(int j = i+1; j < vertex.size();j++) {
				Person check = vertex.get(j);
				if(p.equals(check)) {
					return check.getNme();
				}
			}
		}
		return null;
	}
}

/**
 * 循环队列类
 */
class Queue<E> {
	private Object[] queue = null;
	private int maxSize;
	private int rear;
	private int front;
	private int size = 0;

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
