package items;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;

import tool.Select;
import tool.SnapShot;

/**.
 * Monkey是一个继承自Thread的类型，这是一个mutable的类型。
 * 表示一只想要过河的猴子，它可能从左岸到右岸也可能从右岸到左岸，取决于
 * 传入的方向。Monkey具有ID，速度，方向，行动策略属性，这些在建立对象时决定。
 *
 */
public class Monkey extends Thread {
  private final int id;
  private final String direction;
  private final int speed;
  private final Ladders ladders;
  private Select selector = null;
  private final Logger logger = Logger.getLogger(Monkey.class);
  private final long starttime = System.currentTimeMillis();
  //private final CyclicBarrier barrier;
  private CountDownLatch countDownLatch = null;
  
  /*
   * Abstract Function:
   *  AF(id, direction, speed) = 一只想要过河的猴子
   *  
   * Rep invariant:
   *  id        自然数编号
   *  direction 方向只能为 R->L 或 L->R
   *  speed     正整数，>=1
   *  selector  选择策略，不为空
   *  ladders   不为空
   *  logger    不为空
   *  
   * Safe from rep exposure:
   *  所有域都是private的，对于setCountDown和setSelector，方法不会对猴子基本属性产生影响
   *  
   * Thread safe argument:
   *  该类型本身是线程，其中所有域是 private final的
   *  - ladders中所有梯子中方法被synchronized修饰
   *  - 其他域都是private final且为immutable的
   *  
   */
  
  private void checkRep() {
    assert id >= 0 : "ID should be nature num, but " + id;
    assert direction.matches("(R->L)|(L->R)") : "Direction should be R->L | L->R";
    assert speed >= 1 : "Speed should >= 1, but " + speed;
    //assert selector != null : "Select strategy is null";
    assert ladders != null : "Ladders is null";
  }
  
  /**
   * .初始化一只猴子
   * @param id 猴子编号，自然数
   * @param direction 方向，R->L或L->R
   * @param speed 速度，正整数 >= 1
   * @param ladders 梯子
   */
  public Monkey(int id, String direction, int speed, Ladders ladders) {
    super(String.valueOf(id));
    this.id = id;
    this.direction = direction;
    this.speed = speed;
    //this.selector = select;
    this.ladders = ladders;
    //this.barrier = barrier;
    checkRep();
  }
  
  /**.
   * 启动猴子线程，线程启动后，每隔一秒调用一次策略，决定下一步的行动
   * ，在行动之后会休眠1秒，直到决策行动后猴子抵达了对岸。抵达对岸之后
   * ，方法记录此时的时间戳，调用countDown()方法通知主线程该线程结束，随后线程
   * 结束运行。
   */
  @Override
  public void run() {
    if (selector == null) {
      throw new NullPointerException();
    }
    boolean f = true;
    while (f) {
      f = selector.select(this, ladders); //f表示是否处于等待或前进状态
      if (!f) {
        break;
      }
      try {
        sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    SnapShot.death.put(id, System.currentTimeMillis());  //将死亡时间存入map
    if (countDownLatch != null) {
      countDownLatch.countDown(); //到达屏障，等待主线程
    }
  }
  
  /**
   * .设置线程计数器
   * @param countdownlatch 计数器
   */
  public void setCountDownLatch(CountDownLatch countdownlatch) {
    this.countDownLatch = countdownlatch;
  }
  
  /**
   * .设置选择策略
   * @param selector 策略
   */
  public void setSelector(Select selector) {
    this.selector = selector;
  }
  
  /**
   * .返回猴子的日志器
   * @return 猴子的日志器
   */
  public Logger getLogger() {
    return logger;
  }
  
  /**
   * .返回猴子的id
   * @return 猴子的id
   */
  public int getID() {
    return id;
  }
  
  /**
   * .返回猴子的方向
   * @return 猴子的方向，为 L->R 或 R->L
   */
  public String getDirection() {
    return direction;
  }
  
  /**
   * .返回猴子的速度
   * @return
   */
  public int getSpeed() {
    return speed;
  }
  
  /**
   * .返回猴子的存活时间，存活时间是指从猴子线程开始运行到调用
   * 该方法这一刻线程的运行时长
   * @return 存活时间，单位为秒
   */
  public int getTime() {
    return (int) ((System.currentTimeMillis() - starttime) / 1000);
  }
  
  @Override 
  public String toString() {
    return "[monkey: " + id + ", direction: " + direction 
            + ", speed: " + speed 
            + ", selector: " + selector.getClass().getSimpleName() + "]";
  }
  
  @Override
  public boolean equals(Object otherobject) {
    if (this == otherobject) {
      return true;
    }
    if (otherobject == null) {
      return false;
    }
    if (getClass() != otherobject.getClass()) {
      return false;
    }

    Monkey other = (Monkey) otherobject;

    return this.id == other.id;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(id, direction, speed);
  }
}
