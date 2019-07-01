package APIs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.apache.log4j.Logger;

import circularOrbit.AtomStructure;
import circularOrbit.CircularOrbit;
import circularOrbit.PersonalAppEcosystem;
import circularOrbit.TrackGame;
import constant.SystemType;
import physicalObject.App;
import physicalObject.Athlete;

public class CircularOrbitAPIs<E> {
	Logger logger = Logger.getLogger(CircularOrbitAPIs.class);
	
	/**
	 * 计算多轨道系统中各轨道上物体分布的熵值。通俗的说，如果所有物体都 分布在同一条轨道上，其
	 * 熵值最低；如果所有物体均匀的分布在每一条轨道 上，整个系统的熵值最高。 
	 * <br>轨道系统的熵值计算公式为：
	 * <br>定义轨道数量为 {@code n}，定义物体数量为 {@code m}
	 * <br>物体出现在某条轨道的概率为 {@code p}，{@code p = 1/n} 
	 * <br>轨道出现k个物体的概率为 {@code P(k) = C(x,n)[p^k * (1-p)^n-k]}
	 * <br>则单条轨道k个物体的信息为 {@code l(k) = -log2(P(k))}
	 * <br>则轨道系统的信息熵为 {@code H(x) = -sum(i from 0 to n)[P(i) * l(i)]}
	 * 
	 * 
	 * @param c 待计算轨道系统，不为空
	 * @return 多轨道系统中各轨道上物体分布的熵值
	 */
	public double getObjectDistributionEntropy(CircularOrbit c) {
		if(c == null) throw new NullPointerException();
		int n = c.getTrackNum();
		double p = 1/(double)n; //Probability
		int m = c.getObjectNum(); 
		double[] lx = new double[m+1];
		double[] ex = new double[n];
		double hx = 0;
		//Initialize lx
		for(int i = 0 ; i <= m; i++) {
			double l = lx(i,m,p);
			if(Double.isNaN(l)) {
				lx[i] = 0;
			}
			else {
				lx[i] = lx(i,m,p);
			}
		}
		for(int i = 0; i < c.getTrackNum(); i++) {
			ex[i] = lx[c.getObjects(i).size()];
		}
		for(int i = 0; i < c.getTrackNum(); i++) {
			hx += ex[i];
		}
		assert hx >= 0: "Entropy < 0," + hx; //check entropy >= 0
		logger.info("Calculate the Entropy " +hx);
		return hx;
	}
	
	/**
	 *  {@code P(k) = C(x,n)[p^k * (1-p)^n-k]}<br>{@code l(k) = -log2(P(k))}
	 * @param k 出现个数
	 * @param n 物体个数
	 * @param p 概率
	 * @return {@code P(i) * l(i)}
	 */
	private double lx(int k, int n, double p) {
		try {
		double C = factorial(n) / (factorial(k) * factorial(n-k));
		double P = C * Math.pow(p, k) * Math.pow(1-p, n-k);
		double lx = -(Math.log(P)/Math.log((double)2));
		return lx * P;
		}catch(Exception e) {
			return 0;
		}
	}
	
	/**
	 * 计算阶乘
	 * @param num
	 * @return num的阶乘
	 */
	private double factorial(int num){
		if(num < 0) throw new IllegalArgumentException("Negative number");
		if(num == 0) return 1;
		double temp = 1;
	    for(int i=1;i<=num;i++){
	    	temp *= i;
	    }
	    return temp;
	}

	
	/**
	 * 计算任意两个物体之间的最短逻辑距离。这里的逻辑距离是指：e1 和 e2 之 间通过最
	 * 少多少条边（relation）即可连接在一起。两个物体之间若无关系， 则距离无穷大。
	 * 当所要计算的物体不再系统内时，返回-1
	 * 
	 * @param c 待计算的轨道系统
	 * @param e1 待计算的轨道物体
	 * @param e2 待计算的轨道物体
	 * @return 两个物体的最短逻辑距离，如果物体不再系统中，返回-1
	 */
	public int getLogicalDistance (CircularOrbit c, E e1, E e2) {
		if(e1 == null || e2 == null) throw new NullPointerException("getLogicalDistance argument is null pointer");
		E base = e1;
		E target = e2;
		List<E> vertex = new ArrayList<E>();
		@SuppressWarnings("unchecked")
		Iterator<E> t = c.iterator();
		while(t.hasNext()) {
			vertex.add(t.next());
		}
		if(vertex.isEmpty()) return -1;
		if(!vertex.get(0).getClass().getName().equals(e1.getClass().getName()))
			throw new IllegalArgumentException("getLogicalDistance object format does not match system, but was " + vertex.get(0).getClass().getName()
					+", "+e1.getClass().getName());
		// 检查是否有顶点未加入顶点集
		if(!(c.contains(base)) || !(c.contains(target))) {
			return -1;
		}
		if(base.equals(target)) return 0;
		
		// BFS，depth数组存储各顶点层数，也即最短路径
		int[] depth = new int[vertex.size()];  //存放各顶点的所在层数
		boolean isFind = false;
		Queue<E> queue = new LinkedList<E>();
		queue.clear();
		queue.offer(base);
		boolean[] visited = new boolean[vertex.size()];
		E v = null;
		E p = null;
		
		visited[vertex.indexOf(base)] = true;
		depth[vertex.indexOf(base)] = 0;
		
		while (!queue.isEmpty()) {
			v = queue.poll();
			Iterator<E> it = c.relatives(v).iterator();  //将这个人的所有关系生成迭代器
			if(it.hasNext()) {
				p = it.next();
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
					p = it.next();
				}
				else p = null;
			}
			if (isFind) break;
		}
		
		if (!isFind) return Integer.MAX_VALUE;
		assert depth[vertex.indexOf(p)] == -1 || depth[vertex.indexOf(p)] >= 0;
		logger.info("Calculate the Logic Distance: "+depth[vertex.indexOf(p)]);
		return depth[vertex.indexOf(p)];
	}
	
	/**
	 * 计算任意两个物体之间的物理距离。若物体有具体位置，则可在直角坐标系 里计算出它们之间的物理距离。
	 * <p>注：目前只能计算初始的物理距离
	 * 
	 * @param c 待计算的轨道系统
	 * @param e1 待计算的轨道物体
	 * @param e2 待计算的轨道物体
	 * @return 两个物体的物理距离，如果物体是逻辑上的，即没有物理距离，或者物体在系统中不存在，则返回-1
	 */
	public double getPhysicalDistance (CircularOrbit c, E e1, E e2) {
		if(e1 == null || e2 == null) throw new NullPointerException("getPhysicalDistance argument is null pointer");
		E base = e1;
		E target = e2;
		List<E> vertex = new ArrayList<E>();
		@SuppressWarnings("unchecked")
		Iterator<E> t = c.iterator();
		while(t.hasNext()) {
			vertex.add(t.next());
		}
		if(vertex.isEmpty()) return -1; //no vertex
		if(!vertex.get(0).getClass().getName().equals(e1.getClass().getName()))
			throw new IllegalArgumentException("getLogicalDistance object format does not match system, but was " + vertex.get(0).getClass().getName()
					+", "+e1.getClass().getName());
		// 检查是否有顶点未加入顶点集
		if(!(c.contains(base)) || !(c.contains(target))) {
			return -1;
		}
		if(c.getAngle(base) == -1 || c.getAngle(target) == -1) return -1;  //no angle
		if(base.equals(target)) return 0; //compare itself
		
		double a = c.getRadius(c.indexOf(base));
		double b = c.getRadius(c.indexOf(target));
		double angle = c.getAngle(base) - c.getAngle(target);
		logger.info("Calculate the Physical Distance: "+cosineFormula(a, b, angle));
		return cosineFormula(a, b, angle);
	}
	
	/**
	 * 计算同端点两边另一个端点连线的长度，即两条有限长度的边从一个共同端点放射。输入两边长度和夹角度数 {@code a, b, angle} 。
	 * 其中 {@code angle} 为角度制，两条边长为正数。
	 * 
	 * @param a 三角形一条边长，大于0
	 * @param b 三角形另一边长度，大于0
	 * @param angle 两条边的夹角，角度制
	 * @return 三角形第三条的长度
	 */
	private double cosineFormula(double a, double b, double angle) {
		if(a <= 0 || b <=0) throw new IllegalArgumentException("The length of the triangle is not positive, a "+a+",b "+b);
		
		double radians = angle * (Math.PI / 180);
		double c2 = Math.pow(a, 2) + Math.pow(b, 2) - 2 * a * b * Math.cos(radians);
		assert Math.sqrt(c2) >= 0;
		return Math.sqrt(c2);
	}
	
	/**
	 * 计算两个多轨道系统之间的差异。包括：轨道数的差异、具有相
	 * 同次序的轨道上物体数量的差异和物体的差 异（如果物体不需要区分，则不给出物体的差异，只给出数量
	 * 差异）。c1 和 c2 必须为同类型的轨道才可以比较（例如不能比较一个太阳系和一个 100 米比赛）
	 * 
	 * @param c1 待比较的轨道系统，两个轨道系统类型相同，非空
	 * @param c2 待比较的轨道系统，两个轨道系统类型相同，非空
	 * @return Difference对象
	 */
	public Difference getDifference (CircularOrbit c1, CircularOrbit c2) {
		if(c1 == null || c2 == null) throw new NullPointerException("Null pointer in c1 or c2");
		if((c1 instanceof TrackGame) && !(c2 instanceof TrackGame)) throw new IllegalArgumentException("Different types of systems");
		if((c1 instanceof AtomStructure) && !(c2 instanceof AtomStructure)) throw new IllegalArgumentException("Different types of systems");
		if((c1 instanceof PersonalAppEcosystem) && !(c2 instanceof PersonalAppEcosystem)) throw new IllegalArgumentException("Different types of systems");
		
		int t1 = c1.getTrackNum();
		int t2 = c2.getTrackNum();
		int max = Math.max(t1, t2);
		Map<Integer, Integer> objNumDiff = new HashMap<Integer, Integer>();
		Map<Integer, Set<String>> obj1 = new HashMap<Integer, Set<String>>();
		Map<Integer, Set<String>> obj2 = new HashMap<Integer, Set<String>>();
		boolean di = false;
		SystemType st = null;
		/* Get distinguish */
		if(c1 instanceof TrackGame) {
			di = true;
			st = SystemType.TrackGame;
		}
		else if(c1 instanceof PersonalAppEcosystem) {
			di = true;
			st = SystemType.PersonalAppEcosystem;
		}
		else if(c1 instanceof AtomStructure) {
			di = false;
			st = SystemType.AtomStructure;
		}
		
		/* Get objects number difference */
		for(int i = 0; i < max; i++) {
			int n1;
			int n2;
			if(c1.getObjects(i) != null) {
				n1 = c1.getObjects(i).size();
			}
			else
				n1 = 0;
			if(c2.getObjects(i) != null) {
				n2 = c2.getObjects(i).size();
			}
			else
				n2 = 0;
			objNumDiff.put(i, n1-n2);
		}
		
		/* Get objects difference 
		 * Use Set intersection operation, difference operation
		 */
		if(di) {
			Set<String> set1 = new HashSet<String>();
			Set<String> set2 = new HashSet<String>();
			Set<String> iset = new HashSet<String>();
			switch(st) {
			case TrackGame : {
				for(int i = 0; i < max; i++) {
					if(c1.getObjects(i) != null) {
						for(int j = 0; j < c1.getObjects(i).size(); j++) {
						Athlete a = (Athlete)c1.getObjects(i).get(j);
						set1.add(a.getName());
						}
					}
					if(c2.getObjects(i) != null) {
						for(int j = 0; j < c2.getObjects(i).size(); j++) {
						Athlete a = (Athlete)c2.getObjects(i).get(j);
						set2.add(a.getName());
						}
					}
					iset.addAll(set1);
					iset.retainAll(set2);
					if(iset.isEmpty()) {
						obj1.put(i, new HashSet<String>(set1));
						obj2.put(i, new HashSet<String>(set2));
					}
					else {
						set1.removeAll(iset);
						obj1.put(i, new HashSet<String>(set1));
						set2.removeAll(iset);
						obj2.put(i, new HashSet<String>(set2));
					}
					set1.clear();
					set2.clear();
					iset.clear();
				}
				break;
			}
			case PersonalAppEcosystem: {
				for(int i = 0; i < max; i++) {
					if(c1.getObjects(i) != null) {
						for(int j = 0; j < c1.getObjects(i).size(); j++) {
						App a = (App)c1.getObjects(i).get(j);
						set1.add(a.getName());
						}
					}
					if(c2.getObjects(i) != null) {
						for(int j = 0; j < c2.getObjects(i).size(); j++) {
						App a = (App)c2.getObjects(i).get(j);
						set2.add(a.getName());
						}
					}
					iset.addAll(set1);
					iset.retainAll(set2);
					if(iset.isEmpty()) {
						obj1.put(i, new HashSet<String>(set1));
						obj2.put(i, new HashSet<String>(set2));
					}
					else {
						set1.removeAll(iset);
						obj1.put(i, new HashSet<String>(set1));
						set2.removeAll(iset);
						obj2.put(i, new HashSet<String>(set2));
					}
					set1.clear();
					set2.clear();
					iset.clear();
				}
				break;
			}
			default: {
				throw new IllegalArgumentException("No such SystemType");
			}
		}
		}
		logger.info("Get the Difference :"+c1.getClass().getSimpleName()+","+c2.getClass().getSimpleName());
		return new Difference(t1,t2,objNumDiff, obj1,obj2,di);
	}
}

