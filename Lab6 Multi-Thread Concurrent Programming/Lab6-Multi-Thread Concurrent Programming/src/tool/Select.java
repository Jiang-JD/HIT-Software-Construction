package tool;

import items.Ladders;
import items.Monkey;

/**
 * .决策接口，需要实现select方法
 *
 */
public interface Select {
  /**
   * .执行选择策略，如果抵达对岸返回false
   * @param monkey 待执行猴子
   * @param ladders 梯子集合
   * @return 如果处于等待或移动状态返回true，到岸 返回false
   */
  boolean select(Monkey monkey, Ladders ladders);
}
