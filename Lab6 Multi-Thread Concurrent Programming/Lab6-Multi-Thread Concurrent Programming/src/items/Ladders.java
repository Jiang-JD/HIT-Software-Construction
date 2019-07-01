package items;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * .表示横跨河上的一组梯子，mutable类型，梯子总数和长度不变，
 * 梯子的状态会发生变化，可以将Ladders对象视为List操作
 */
public class Ladders implements Iterable<Ladder> {
  private final List<Ladder> ladders = new ArrayList<Ladder>();
  
  /*
   * Abstract Function:
   *  AF(ladders) = 一组用来过河的梯子
   *  
   * Rep invariant:
   *  ladders   所存对象非空
   *  
   * Safe from rep exposure:
   *  所有的域都是private final的，observer()返回immutable类型
   *  注意：indexOf返回Monkey引用，可能出现泄露风险
   *  
   * Thread safe argument：
   *  该类型为线程安全的，ladders为private final
   *  - 所有引用到ladders的方法都是observer，不存在mutator
   *  
   */
  
  private void checkRep() {
    for (Ladder l : ladders) {
      assert l != null;
    }
  }
  
  /**
   * .初始化一个梯子组，输入参数为梯子数量和梯子的长度
   * @param number 梯子的数量，为正整数
   * @param length 梯子的长度，为正整数
   */
  public Ladders(int number, int length) {
    for (int i = 0; i < number; i++) {
      ladders.add(new Ladder(i, length));
    }
    checkRep();
  }
  
  /**
   * .返回指定位置上的梯子对象
   * @param i 指定位置，要求 0 <= i < length
   * @return 指定位置梯子对象
   */
  public synchronized Ladder get(int i) {
    return ladders.get(i);
  }
  
  /**
   * .返回梯子数量
   * @return 梯子数量
   */
  public int size() {
    return ladders.size();
  }
  
  /**
   * .返回踏板数量
   * @return 踏板数量
   */
  public int length() {
    return ladders.get(0).size();
  }
  
  /**
   * .返回指定梯子在这组梯子中的编号（位置，索引），没有返回-1
   * @param l 指定梯子
   * @return 梯子的索引，没有返回-1
   */
  public int indexOf(Ladder l) {
    return ladders.indexOf(l);
  }

  /**
   * .梯子迭代器
   */
  @Override
  public Iterator<Ladder> iterator() {
    return new Iterator<Ladder>() {
      int index = -1;
      
      @Override
      public boolean hasNext() {
        return index + 1 < ladders.size();
      }

      @Override
      public Ladder next() {
        index++;
        return ladders.get(index);
      }
    };
  }
  
}
