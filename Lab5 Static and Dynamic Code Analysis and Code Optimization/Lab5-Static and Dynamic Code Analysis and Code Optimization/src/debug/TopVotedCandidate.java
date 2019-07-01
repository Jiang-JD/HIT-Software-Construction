package debug;
/**
 * In an election, the i-th vote was cast for persons[i] at time times[i].
 * 
 * Now, we would like to implement the following query function:
 * TopVotedCandidate.q(int t) will return the number of the person that was
 * leading the election at time t.
 * 
 * Votes cast at time t will count towards our query. In the case of a tie, the
 * most recent vote (among tied candidates) wins.
 * 
 * 
 * 
 * Example 1:
 * 
 * Input: ["TopVotedCandidate","q","q","q","q","q","q"],
 * [[[0,1,1,0,0,1,0],[0,5,10,15,20,25,30]],[3],[12],[25],[15],[24],[8]]
 *  Output:
 * [null,0,1,1,0,0,1] 
 * 
 * Explanation: 
 * At time 3, the votes are [0], and 0 is leading. 
 * At time 12, the votes are [0,1,1], and 1 is leading. 
 * At time 25, the votes are [0,1,1,0,0,1], and 1 is leading (as ties go to the most recent
 * vote.) 
 * This continues for 3 more queries at time 15, 24, and 8.
 * 
 * 
 * Note:
 * 
 * 1 <= persons.length = times.length <= 5000 
 * 0 <= persons[i] <= persons.length
 * times is a strictly increasing array with all elements in [0, 10^9].
 * TopVotedCandidate.q is called at most 10000 times per test case.
 * TopVotedCandidate.q(int t) is always called with t >= times[0].
 *
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TopVotedCandidate {
  List<List<Vote>> A;

  public TopVotedCandidate(int[] persons, int[] times) {
    /*
     * 1. 将arrayList修改为ArrayList<List<Vote>> why: A是嵌套列表类型
     */
    A = new ArrayList<List<Vote>>();
    Map<Integer, Integer> count = new HashMap<>();
    for (int i = 0; i < persons.length; ++i) {
      int p = persons[i], t = times[i];
      int c = count.getOrDefault(p, 1);

      /*
       * 1. 将c修改为c+1 why: count统计次数，每次c应+1
       */
      count.put(p, c + 1);
      while (A.size() <= c) {
        A.add(new ArrayList<Vote>());
      }
      A.get(c).add(new Vote(p, t));
    }
  }

  public int q(int t) {
    /*
     * 2. 将hi修改为hi = A.size()-1 why: hi表示list最高下标，应在list范围内
     */
    int lo = 1, hi = A.size() - 1;
    while (lo < hi) {
      int mi = lo + (hi - lo) / 2;
      if (A.get(mi).get(0).time <= t) {
        lo = mi;
        /*
         * 3. 添加判断条件 if(A.get(mi).get(A.get(mi).size()-1).time < t) lo = mi+1; why:
         * 相同次数对应的list应选择最后活跃的候选人，所以如果出现最后一个人仍有候选资格的情况，应将low 下标向后移动，防止死循环
         * 
         * 4. 添加判断条件 if(A.get(hi).get(0).time > t) hi = mi-1; why:
         * 如果high下标的第一个记录时间晚于t，则将high下标重置为mi-1，防止死循环
         */
        if (A.get(mi).get(A.get(mi).size() - 1).time < t)
          lo = mi + 1;
        if (A.get(hi).get(0).time > t)
          hi = mi - 1;
      } else
        /*
         * 5. 将mi修改为mi-1 why: high下标应为mid-1
         */
        hi = mi - 1; // -1
    }
    /*
     * 6. 添加查找为空的判断 why: 若次数为1的第一条记录时间晚于t，则说明没有符合条件的结果
     */
    if (A.get(1).get(0).time > t)
      return -1;
    int i = lo;

    lo = 0;
    /*
     * 7. 将hi修改为hi = A.size()-1 why: hi表示list最高下标，应在list范围内
     */
    hi = A.get(i).size() - 1; // -1
    while (lo < hi) {
      int mi = lo + (hi - lo) / 2;
      if (A.get(i).get(mi).time <= t) {
        lo = mi;
        /*
         * 8. 添加判断条件 if(A.get(i).get(hi).time <= t) lo++; else hi--; why:
         * 若这个次数对应的list中high下标对应的记录满足要求，则需要将low后移，防止死循环 同理，如果不满足，high应前移
         */
        if (A.get(i).get(hi).time <= t)
          lo++;
        else
          hi--;
      } else
        hi = mi - 1;
    }
    int j = Math.max(lo, 0);
    return A.get(i).get(j).person;
  }
}

class Vote {
  int person, time;

  Vote(int p, int t) {
    person = p;
    time = t;
  }
}