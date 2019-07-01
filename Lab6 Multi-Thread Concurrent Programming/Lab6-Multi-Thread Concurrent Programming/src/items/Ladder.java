package items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * .表示猴子过河使用的梯子，mutable类型，梯子上含有至少一个踏板，
 * 每个踏板最多站一只猴子。该类型的每个方法的操作是原子操作，意味着每个方法是线程安全的，
 * 但是方法与方法之间的操作是非原子的。梯子维护猴子在梯子上的分布状态，提供
 * 方法用于对猴子的操作。
 *
 */
public class Ladder {
  private final List<Rung> rungs = new ArrayList<Rung>(); //踏板，顺序从左到右
  private final Set<Monkey> monkeys = new HashSet<Monkey>(); //当前在梯子上的猴子
  private final Map<Monkey, Rung> mstate = new HashMap<Monkey, Rung>(); //当前猴子对应的踏板位置
  private final Map<Rung, Monkey> rstate = new HashMap<Rung, Monkey>();
  private final int index;
  
  /*
   * Abstract Function:
   *  AF(rungs) = 一个具有踏板的梯子
   *  
   * Rep invariant:
   *  rungs   所存对象非空
   *  
   * Safe from rep exposure:
   *  所有的域都是private final的，leftmost() rightmost()返回的Monkey实例是immutable的
   *  
   * Thread safe argument:
   *  这个类不是线程安全的，因为这个类是mutable的，但是Ladder提供的方法是线程安全的
   *  - rungs 是private final的
   *  - mstate，rstate 是private final的，所有对于这两个rep的方法都被synchronized修饰
   */
  
  private void checkRep() {
    for (Rung r : rungs) {
      assert r != null;
    }
  }
  
  /**
   * . 初始化一个梯子
   * @param size Number of Rungs
   */
  public Ladder(int index, int size) {
    for (int i = 0; i < size; i++) {
      rungs.add(new Rung(i));
    }
    this.index = index;
    checkRep();
  }
  
  /**
   * .将猴子按照移动方向移动指定步数，返回移动后所处踏板位置
   * @param m 猴子，必须在梯子上
   * @param step 移动距离，不超过猴子最大速度
   * @return 移动后所处踏板位置，如果返回-1说明已经抵达对岸
   */
  public synchronized int move(Monkey m, int step) {
    if (step > m.getSpeed()) {
      throw new IllegalArgumentException();
    }
    //从左向右
    if (m.getDirection().equals("L->R")) {
      Rung rung = mstate.remove(m);
      rstate.remove(rung);
      int pos = rung.getPosition();
      pos += step;
      //猴子成功上岸
      if (pos >= rungs.size()) { 
        monkeys.remove(m);
        return -1;
      }
      mstate.put(m, rungs.get(pos));
      rstate.put(rungs.get(pos), m);
      return pos;
    //从右向左
    } else {
      Rung rung = mstate.remove(m);
      rstate.remove(rung);
      int pos = rung.getPosition();
      pos -= step;
      //猴子成功上岸
      if (pos < 0) { 
        monkeys.remove(m);
        return -1;
      }
      mstate.put(m, rungs.get(pos));
      rstate.put(rungs.get(pos), m);
      return pos;
    }
  }
  
  /**
   * .判断某个方向上是否存在猴子，如果存在返回与那个猴子之间的距离，这个距离
   * 包括那个猴子本身，例如：A猴子在0踏板上，B猴子在3踏板上，A使用这个方法向
   * 右方向的结果为3
   * 
   * @param m 待判断猴子，猴子必须在梯子上
   * @param direction 方向
   * @return 前进方向上与前方猴子距离，没有猴子返回-1
   */
  public synchronized int occupy(Monkey m, String direction) {
    int pos = mstate.get(m).getPosition();
    if (direction.equals("L->R")) {
      for (int i = pos + 1; i < rungs.size(); i++) {
        if (rstate.get(rungs.get(i)) != null) {
          return i - pos;
        }
      }
      return -1;
    } else {
      for (int i = pos - 1; i >= 0; i--) {
        if (rstate.get(rungs.get(i)) != null) {
          return pos - i;
        }
      }
      return -1;
    }
  }
  
  /**
   * .查看梯子是否含有指定猴子
   * @param m 指定猴子
   * @return 如果有返回true，否则返回false
   */
  public synchronized boolean contain(Monkey m) {
    return monkeys.contains(m);
  }
  
  /**
   * .查看梯子指定位置是否有猴子
   * @param position 指定位置
   * @return 如果有返回true，否则返回false
   */
  public synchronized boolean contain(int position) {
    return rstate.containsKey(rungs.get(position));
  }
  
  /**
   * .梯子是否为空
   * @return 如果为空返回true，否则返回false
   */
  public synchronized boolean isEmpty() {
    return monkeys.isEmpty();
  }
  
  /**
   * .获得梯子最左边猴子，没有返回null
   * @return 梯子最左边猴子
   */
  public synchronized Monkey leftmost() { 
    for (Rung r : rungs) {
      if (rstate.get(r) != null) {
        return rstate.get(r);
      }
    }
    return null;
  }
  
  /**
   * .获得梯子最右边猴子，没有返回null
   * @return 梯子最右边猴子
   */
  public synchronized Monkey rightmost() {
    Monkey rightmost = null;
    for (Rung r : rungs) {
      if (rstate.get(r) != null) {
        rightmost = rstate.get(r);
      }
    }
    return rightmost;
  }
  
  /**
   * .获得指定猴子在梯子上位置，从左到右从0开始，没有返回-1
   * @param m 指定猴子
   * @return 指定猴子的位置，没有返回-1
   */
  public int indexOf(Monkey m) {
    Rung r;
    if ((r = mstate.get(m)) != null) {
      return r.getPosition();
    }
    return -1;
  }
  
  /**
   * .返回指定位置上的猴子
   * @param i 指定位置
   * @return 猴子，没有返回null
   */
  public Monkey get(int i) {
    return rstate.get(rungs.get(i));
  }
  
  /**
   * .添加猴子到梯子的第一个踏板，位置取决与猴子的方向
   * @param m 猴子，不为空
   */
  public synchronized void add(Monkey m) {
    if (monkeys.contains(m)) {
      return;
    }
    if (m.getDirection().equals("L->R")) {
      monkeys.add(m);
      mstate.put(m, rungs.get(0));
      rstate.put(rungs.get(0), m);
    } else {
      monkeys.add(m);
      mstate.put(m, rungs.get(rungs.size() - 1));
      rstate.put(rungs.get(rungs.size() - 1), m);
    }
  }
  
  /**
   * .获得该梯子的长度，也即踏板的数量
   * @return 梯子的长度
   */
  public int size() {
    return rungs.size();
  }
  
  /**
   * .获得梯子上当前猴子的数量
   * @return 梯子上当前猴子的数量
   */
  public int sizeOfMonkeys() {
    return monkeys.size();
  }
  
  @Override 
  public String toString() {
    return "[ladder " + index + " rungs: " + rungs.size() + ", monkeys: " + monkeys.size() + "]";
  }

}
