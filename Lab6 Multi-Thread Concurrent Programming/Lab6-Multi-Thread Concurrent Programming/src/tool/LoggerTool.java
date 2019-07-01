package tool;

import items.Ladder;
import items.Ladders;
import items.Monkey;

/**
 * . 提供日志记录方法，用于猴子判断的一些工具方法
 */
public class LoggerTool {
  
  /**
   * .记录等待状态
   * @param monkey 猴子
   */
  public static void waiting(Monkey monkey) {
    monkey.getLogger().info(waitingString(monkey));
  }
  
  /**
   * .返回等待状态的描述字符串
   * @param monkey 猴子
   * @return 描述字符串
   */
  public static String waitingString(Monkey monkey) {
    if (monkey.getDirection().equals("L->R")) {
      return "Monkey " + monkey.getID() 
          + " is waiting on the LEFT bank, "
          + "time: " + monkey.getTime();
    } else {
      return "Monkey " + monkey.getID() 
          + " is waiting on the RIGHT bank, "
          + "time: " + monkey.getTime();
    }
  }
  
  /**
   * .记录移动状态，此时所处位置，方向，时间
   * @param monkey 猴子
   * @param ladders 一组梯子
   * @param ladder 所处梯子
   */
  public static void moving(Monkey monkey, Ladders ladders, Ladder ladder) {
    monkey.getLogger().info(movingString(monkey, ladders, ladder));
  }
  
  /**
   * .返回移动状态的描述字符串
   * @param monkey 猴子
   * @param ladders 一组梯子
   * @param ladder 猴子所在的梯子
   * @return 移动状态描述字符串
   */
  public static String movingString(Monkey monkey, Ladders ladders, Ladder ladder) {
    return "Monkey " + monkey.getID() 
        + " is on ladder " + ladders.indexOf(ladder)
        + " rung " + ladder.indexOf(monkey) 
        + " ,direction: " + monkey.getDirection()
        + ", time: " + monkey.getTime();
  }
  
  /**
   * .记录到岸状态
   * @param monkey 猴子
   */
  public static void landing(Monkey monkey) {
    monkey.getLogger().info(landingString(monkey));
  }
  
  /**
   * .返回到岸状态的描述字符串
   * @param monkey 猴子
   * @return 到岸状态字符串
   */
  public static String landingString(Monkey monkey) {
    if (monkey.getDirection().equals("L->R")) {
      return "Monkey " + monkey.getID() 
          + " is landing on the RIGHT bank from LEFT bank, "
          + "time: " + monkey.getTime();
    } else {
      return "Monkey " + monkey.getID() 
          + " is landing on the LEFT bank from RIGHT bank, "
          + "time: " + monkey.getTime();
    }
  }
}
