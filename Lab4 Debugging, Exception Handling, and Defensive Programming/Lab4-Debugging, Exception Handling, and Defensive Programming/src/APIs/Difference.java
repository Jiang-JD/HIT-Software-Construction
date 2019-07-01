package APIs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import applications.tools.Timespan;

/**
 * 这是一个 {@code immutable} 类型，表示两个同类型轨道系统之间的差异。差异包含轨道数的差
 * 异、具有相同次序的轨道上物体数量的差异和物体的差 异（如果物体不需要区分，则无需给出物体
 * 的差异，只需给出数量差异）
 *
 */
public class Difference {
	private final int t1;
	private final int t2;
	private final Map<Integer, Integer> n;
	private final Map<Integer, Set<String>> m1;
	private final Map<Integer, Set<String>> m2;
	private final boolean distinguishable;
	
	/*
	 * Abstract Function:
	 * 	AF(t1,t2,n,m1,m2,distinguishable) = 一个表示两个轨道系统差异的对象
	 * 
	 * Rep Invariant:
	 *	t1 	>= 0
	 *	t2 	>= 0
	 *	n	size = MAX(t1,t2)，不含null
	 *	m1	size = MAX(t1,t2)，不含null
	 *	m2  size = MAX(t1,t2)，不含null
	 *	
	 * Safety from exposure:
	 * 	域是private final的，不提供mutator，getObjectDifference()做了防御性拷贝，客户端拿不到内部引用
	 * 
	 */
	 
	private void checkRep() {
		assert t1 >= 0 : "System1 track number < 0, was" + t1;
		assert t2 >= 0 : "System2 track number < 0, was" + t2;
		int max = Math.max(t1,  t2);
		assert n.size() == max : "Objects number difference size != max track number , was" + n.size();
		for(Integer i : n.values()) {
			assert i != null;
		}
		if(!m1.isEmpty() && !m2.isEmpty()) {
			assert m1.size() == max : "System1 Objects difference size != max track number , was " + m1.size();
			assert m2.size() == max : "System2 Objects difference size != max track number , was" + m2.size();
		}
		for(Set<String> s : m1.values())
			assert s != null;
		
		for(Set<String> s : m2.values()) 
			assert s != null;
	}
	
	/**
	 * 初始化一个Difference对象，输入参数包括两个系统的轨道数量，每条轨道之间物体数量差异，每条轨道之间物体的差异，轨道物体是否可区分
	 * @param trackNum1  第一个系统的轨道数量
	 * @param trackNum2 第二个系统的轨道数量
	 * @param quantityDifference 每条轨道的物体数量差异
	 * @param objectDiff1  第一个系统中每条轨道物体与第二个的差集
	 * @param objectDiff2  第二个系统中每条轨道物体与第一个的差集
	 * @param isDistinguishable 轨道物体是否可区分
	 */
	public Difference(int trackNum1, int trackNum2, Map<Integer,Integer> quantityDifference, 
			Map<Integer, Set<String>> objectDiff1, Map<Integer, Set<String>> objectDiff2, boolean isDistinguishable) {
		if(trackNum1 < 0 || trackNum2 < 0) throw new IllegalArgumentException("Negative track number");
		if(quantityDifference == null) throw new IllegalArgumentException("Null pointer in quantityDifference");
		if(objectDiff1 == null || objectDiff2 == null) throw new IllegalArgumentException("Null pointer in objectDifference");
		
		t1 = trackNum1;
		t2 = trackNum2;
		n = new HashMap<Integer, Integer>(quantityDifference);
		Map<Integer, Set<String>> tmp = new HashMap<Integer, Set<String>>();
		for(Map.Entry<Integer, Set<String>> e : objectDiff1.entrySet()) {
			Set<String> s = new HashSet<String>(e.getValue());
			tmp.put(e.getKey(), s);
		}
		m1 = new HashMap<Integer, Set<String>>(tmp);
		tmp.clear();
		for(Map.Entry<Integer, Set<String>> e : objectDiff2.entrySet()) {
			Set<String> s = new HashSet<String>(e.getValue());
			tmp.put(e.getKey(), s);
		}
		m2 = new HashMap<Integer, Set<String>>(tmp);
		distinguishable = isDistinguishable;
		checkRep();
	}
	
	public int getTrack1Num() {
		return t1;
	}
	
	public int getTrack2Num() {
		return t2;
	}
	
	/**
	 * 获取每条轨道只在指定系统上存在的物体名称
	 * 
	 * @param index 索引，只能为1或2，分别代表第一个第二个系统
	 * @return 每条轨道只在指定系统上存在的物体名称
	 */
	public Map<Integer, Set<String>> getObjectDifference(int index) {
		if(index != 1 && index != 2) throw new IllegalArgumentException("Index out of range");
		
		Map<Integer, Set<String>> m = null;
		if(index == 1) m = m1;
		if(index == 2) m = m2;
		Map<Integer, Set<String>> tmp = new HashMap<Integer, Set<String>>();
		for(Map.Entry<Integer, Set<String>> e : m.entrySet()) {
			Set<String> s = new HashSet<String>(e.getValue());
			tmp.put(e.getKey(), s);
		}
		return tmp;
	}
	
	/**
	 * 生成描述Difference对象的字符串
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Track number difference: " + (t1-t2) + "\n");
		int max = Math.max(t1, t2);
		for(int i = 0; i < max; i++) {
			sb.append("Track "+(i+1)+" Objects number: "+n.get(i));
			if(distinguishable) {
				sb.append("; Objects: ");
				if(m1.get(i).isEmpty())
					sb.append("None");
				else if(m1.get(i).size() == 1) {
					sb.append(m1.get(i).toArray()[0]);
				}
				else {
					sb.append(m1.get(i));
				}
				sb.append("-");
				if(m2.get(i).isEmpty())
					sb.append("None");
				else if(m2.get(i).size() == 1) {
					sb.append(m2.get(i).toArray()[0]);
				}
				else {
					sb.append(m2.get(i));
				}
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
	@Override public boolean equals(Object thatObject) {
        if (!(thatObject instanceof Difference)) {
            return false;
        }

        Difference that = (Difference) thatObject;
        return this.t1 == that.t1 
                && this.t2 == that.t2
                && this.n.equals(that.n)
                && this.m1.equals(that.m1)
                && this.m2.equals(that.m2)
                && this.distinguishable == that.distinguishable;
    }

 
    @Override public int hashCode() {
        return Objects.hash(t1,t2,n,m1,m2,distinguishable);
    }
	
	
}
