package applications;

import java.util.List;
import java.util.Map;

import physicalObject.Athlete;
/**
 * 针对TrackGame中运动员的分组策略，将运动员分为不同的小组，每个运动员
 * 属于单独一组且不会在另一组中出现。
 */
public interface Divider {
	/**
	 * 给定 n 个运动员，自动编排比赛方案，给定跑道数目，将n个运动员分组。
	 * 要求：<br>
	 * 每一组的人数不能超 过跑道数、每一组的每条跑道里最多 1 位运动员（但可以
	 * 没有运动员）； 如果第 n 组的人数少于跑道数，则第 0 到第 n-1 各组的人数必须
	 * 等于跑 道数；同一个运动员只能出现在一组比赛中
	 * 
	 * @param tracknum 跑道数目
	 * @param athletes 运动员列表
	 * @return 运动员的比赛分组
	 */
	List<List<Athlete>> grouping(int tracknum, List<Athlete> athletes);
}
