package tool;

import items.Ladder;
import items.Monkey;
import java.util.HashMap;
import java.util.Map;


/**.
 * 保存每只猴子的前一秒的观察信息以及其他的一些数据
 */
public class SnapShot {
  //保存每一只猴子自己对于上一秒梯子情况的快照
  //每个梯子最左边猴子和它的位置
  public static Map<Monkey, Map<Ladder, Position>> preleft = 
      new HashMap<Monkey, Map<Ladder, Position>>();
  //每个梯子最右边猴子和它的位置
  public static Map<Monkey, Map<Ladder, Position>> preright = 
      new HashMap<Monkey, Map<Ladder, Position>>();
  
  public static Map<Integer, Long> birth = new HashMap<Integer, Long>(); //猴子出生时间
  public static Map<Integer, Long> death = new HashMap<Integer, Long>(); //猴子死亡时间
  public static int costtime; //整体耗时
}
