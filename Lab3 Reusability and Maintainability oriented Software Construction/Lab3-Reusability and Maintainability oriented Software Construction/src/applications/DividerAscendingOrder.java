package applications;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import physicalObject.Athlete;

public class DividerAscendingOrder implements Divider {

	/**
	 * 将运动员分组，具体策略为：根据运动员的 本年度最好成绩从高到低排序，排名越靠前，则出场
	 * 越晚，且更占据中 央的赛道<br>
	 * 完成分组后，假设分了n组，则前n-1组的人数与跑道数目相等，第n组人数小于等于跑到数目,且每组中
	 * 运动员的成绩是按从高到底排列，而所有组是按每一组平均成绩从低到高排列
	 */
	@Override
	public List<List<Athlete>> grouping(int tracknum, List<Athlete> a) {
		List<Athlete> athletes = new ArrayList<Athlete>(a);
		int size = athletes.size();
		Collections.sort(athletes, new AthletePBComparator());
		 
		List<Athlete> tmp = new ArrayList<Athlete>();
		List<List<Athlete>> group = new ArrayList<List<Athlete>>();
		int i = 0;
		for(i = 0; i+tracknum < size; i+=tracknum)  {//当剩余人数大于跑道数时循环
			tmp.clear();
			for(int j = i+tracknum-1; j >= i; j--) {
				tmp.add(athletes.get(j));
			}
			group.add(new ArrayList<Athlete>(tmp));
		}
		tmp.clear();
		int end = i;
		for(i = size-1; i >= end;i--) { //将剩余运动员放入一组
			tmp.add(athletes.get(i));
		}
		group.add(new ArrayList<Athlete>(tmp));
		tmp.clear();
		return group;
	}
	
	

}

/**
 * Athlete成绩比较器，升序排列
 *
 */
class AthletePBComparator implements Comparator<Athlete> {

	@Override
	public int compare(Athlete o1, Athlete o2) {
		if(o1.getPersonalBest() - o2.getPersonalBest() > 0) return -1;
		else if(o1.getPersonalBest() - o2.getPersonalBest() < 0) return 1;
		else return 0;
	}
	
}
