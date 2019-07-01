package applications;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import physicalObject.Athlete;

/**
 * 随机分组
 * 将运动员分组，具体策略为：完全随机<br>
 * 完成分组后，假设分了n组，则前n-1组的人数与跑道数目相等，第n组人数小于等于跑到数目
 *
 */
public class DividerRandom implements Divider {

	/**
	 * 将运动员分组，具体策略为：完全随机<br>
	 * 完成分组后，假设分了n组，则前n-1组的人数与跑道数目相等，第n组人数小于等于跑到数目
	 */
	@Override
	public List<List<Athlete>> grouping(int tracknum, List<Athlete> a) {
		List<Athlete> athletes = new ArrayList<Athlete>(a);
		int size = athletes.size();
		Random random = new Random();
		for(int i = 0; i < size; i++) {
			int randompos = random.nextInt(size);
			Collections.swap(athletes, i, randompos);
		}
		
		List<Athlete> tmp = new ArrayList<Athlete>();
		List<List<Athlete>> group = new ArrayList<List<Athlete>>();
		int i = 0;
		for(i = 0; i+tracknum < size; i+=tracknum)  {//当剩余人数大于跑道数时循环
			tmp.clear();
			for(int j = i; j < i+tracknum; j++) {
				tmp.add(athletes.get(j));
			}
			group.add(new ArrayList<Athlete>(tmp));
		}
		tmp.clear();
		for(; i < size;i++) { //将剩余运动员放入一组
			tmp.add(athletes.get(i));
		}
		group.add(new ArrayList<Athlete>(tmp));
		tmp.clear();
		
		return group;
	}
}
