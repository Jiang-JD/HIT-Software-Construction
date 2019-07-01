package tool;

import items.Ladders;
import items.Monkey;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;



/**
 * .猴子生成器，用来周期性生产指定数量猴子
 *
 */
public class MonkeyGenerator {
  private Ladders ladders;
  private Set<Monkey> monkeys = new HashSet<Monkey>();
  private List<Select> selectors = Arrays.asList(
      new Selector1(),
      new Selector2(),
      new Selector3(),
      new Selector4(),
      new Selector5(),
      new Selector6(),
      new Selector7()
      );
  private List<String> directions = Arrays.asList("L->R","R->L");
  private double throughput;
  private double fairness;
  private Logger logger = Logger.getLogger(MonkeyGenerator.class);
  private boolean isOver = false;
  
  /**
   * .每隔𝑡秒钟同时产生𝑘个 Monkey 对象（例如：第 0 秒生成𝑘个 Monkey 对象，
   * 第𝑡秒又同时产生 𝑘个 Monkey 对象，第 2𝑡秒…），并为各只猴子生成以下属性：<br>  
   * {@code 名字 ID（int）}：按照产生的时间次序进行自然数编号，同一时刻 同时生成的猴子的 ID 应有不同的编号<br>  
   * {@code 方向 direction（String）}：值随机指定，左岸到右岸(“L->R”)， 或者从右岸到左岸(“R->L”)<br>  
   * {@code 速度𝑣}：正整数，取值范围为[1,𝑀𝑉]，𝑀𝑉为最大可能的速度。 如果 不为整数，则最后一次产生的猴子个数为𝑁%𝑘
   * @param t 间隔时间
   * @param k 一次产生猴子数量
   * @param N 猴子总数量
   * @param mv 最大速度
   */
  public void start(int t, int k, int N, int mv) {
    isOver = false;
    //final CyclicBarrier barrier = new CyclicBarrier(K + 1);
    final CountDownLatch countDownLatch = new CountDownLatch(N);
    logger.info("Start to simulate, Time span: " + t + "s, "
                + "Per number: " + k + ", "
                + "Total: " + N + ", "
                + "Max speed: " + mv
                + ", Ladders: " + ladders.size()
                + ", Rungs: " + ladders.get(0).size());
    VisualTool.updateConsole("Start to simulate, Time span: " + t + "s, "
                + "Per number: " + k + ", "
                + "Total: " + N + ", "
                + "Max speed: " + mv
                + ", Ladders: " + ladders.size()
                + ", Rungs: " + ladders.get(0).size());
    Random random = new Random();
    SnapShot.preleft.clear();
    SnapShot.preright.clear();
    SnapShot.birth.clear();
    SnapShot.death.clear();
    int id = 0;
    int i = 0;
    long starttime = System.currentTimeMillis();
    for (i = 0; i + k < N; i += k) {
      for (int j = 0; j < k; j++) {
        Monkey m = new Monkey(++id, 
            directions.get(random.nextInt(2)), 
            random.nextInt(mv) + 1,
            ladders);
        m.setCountDownLatch(countDownLatch);
        m.setSelector(selectors.get(random.nextInt(selectors.size())));
        monkeys.add(m);
        SnapShot.birth.put(m.getID(), System.currentTimeMillis()); //记录出生时间
        m.start();
      }
      try {
        Thread.sleep(t * 1000); //停止t秒
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    for (; i < N; i++) {
      Monkey m = new Monkey(++id, 
          directions.get(random.nextInt(2)), 
          random.nextInt(mv) + 1, 
          ladders);
      m.setSelector(selectors.get(random.nextInt(selectors.size())));
      m.setCountDownLatch(countDownLatch);
      monkeys.add(m);
      SnapShot.birth.put(m.getID(), System.currentTimeMillis());
      m.start();
    }
    try {
      countDownLatch.await(); //等待所有猴子线程结束
      throughput = (double)N / ((System.currentTimeMillis() - starttime) / 1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    isOver = true;
    logger.info("End simulating, total: " + N  
                + ", time: " + (System.currentTimeMillis() - starttime) / 1000  
                + ", throughput: " + getThroughput(starttime, System.currentTimeMillis(), N) 
                + ", fairness: " + getFairness(N));
    VisualTool.updateConsole("End simulating, total: " + N + ", time: "
              + (System.currentTimeMillis() - starttime) / 1000 + ", throughput: "
              + getThroughput(starttime, System.currentTimeMillis(), N) 
              + ", fairness: " + getFairness(N));
    fairness = getFairness(N);
    System.out.println("Main Thread Down, total: " + N 
        + ", time: " + (System.currentTimeMillis() - starttime) / 1000
        + ", throughput: " + (throughput = getThroughput(starttime, System.currentTimeMillis(), N))
        + ", fairness: " + getFairness(N));
  }
  
  /**
   * .每隔𝑡秒钟同时产生𝑘个 Monkey 对象（例如：第 0 秒生成𝑘个 Monkey 对象，
   * 第𝑡秒又同时产生 𝑘个 Monkey 对象，第 2𝑡秒…），并为各只猴子生成以下属性：<br>  
   * {@code 名字 ID（int）}：按照产生的时间次序进行自然数编号，同一时刻 同时生成的猴子的 ID 应有不同的编号<br>  
   * {@code 方向 direction（String）}：值随机指定，左岸到右岸(“L->R”)， 或者从右岸到左岸(“R->L”)<br>  
   * {@code 速度𝑣}：正整数，取值范围为[1,𝑀𝑉]，𝑀𝑉为最大可能的速度。 如果 不为整数，则最后一次产生的猴子个数为𝑁%𝑘
   * @param t 间隔时间
   * @param k 一次产生猴子数量
   * @param N 猴子总数量
   * @param mv 最大速度
   * @param selector 指定的过河策略
   */
  public void start(int t, int k, int N, int mv, Select selector) {
    isOver = false;
    //final CyclicBarrier barrier = new CyclicBarrier(K + 1);
    final CountDownLatch countDownLatch = new CountDownLatch(N);
    logger.info("Start to simulate, Time span: " + t + "s, "
                + "Per number: " + k + ", "
                + "Total: " + N + ", "
                + "Max speed: " + mv
                + ", Ladders: " + ladders.size()
                + ", Rungs: " + ladders.get(0).size());
    VisualTool.updateConsole("Start to simulate, Time span: " + t + "s, "
                + "Per number: " + k + ", "
                + "Total: " + N + ", "
                + "Max speed: " + mv
                + ", Ladders: " + ladders.size()
                + ", Rungs: " + ladders.get(0).size());
    Random random = new Random();
    SnapShot.preleft.clear();
    SnapShot.preright.clear();
    SnapShot.birth.clear();
    SnapShot.death.clear();
    int id = 0;
    int i = 0;
    long starttime = System.currentTimeMillis();
    for (i = 0; i + k < N; i += k) {
      for (int j = 0; j < k; j++) {
        Monkey m = new Monkey(++id, 
            directions.get(random.nextInt(2)), 
            random.nextInt(mv) + 1,
            ladders);
        m.setSelector(selector);
        m.setCountDownLatch(countDownLatch);
        monkeys.add(m);
        SnapShot.birth.put(m.getID(), System.currentTimeMillis()); //记录出生时间
        m.start();
      }
      try {
        Thread.sleep(t * 1000); //停止t秒
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    for (; i < N; i++) {
      Monkey m = new Monkey(++id, 
          directions.get(random.nextInt(2)), 
          random.nextInt(mv) + 1, 
          ladders);
      m.setSelector(selector);
      m.setCountDownLatch(countDownLatch);
      monkeys.add(m);
      SnapShot.birth.put(m.getID(), System.currentTimeMillis());
      m.start();
    }
    try {
      countDownLatch.await(); //等待所有猴子线程结束
      throughput = (double)N / ((System.currentTimeMillis() - starttime) / 1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    isOver = true;
    logger.info("End simulating, total: " + N  
                + ", time: " + (System.currentTimeMillis() - starttime) / 1000  
                + ", throughput: " + getThroughput(starttime, System.currentTimeMillis(), N) 
                + ", fairness: " + getFairness(N));
    VisualTool.updateConsole("End simulating, total: " + N + ", time: "
              + (System.currentTimeMillis() - starttime) / 1000 + ", throughput: "
              + getThroughput(starttime, System.currentTimeMillis(), N) 
              + ", fairness: " + getFairness(N));
    fairness = getFairness(N);
    System.out.println("Main Thread Down, total: " + N 
        + ", time: " + (System.currentTimeMillis() - starttime) / 1000
        + ", throughput: " + (throughput = getThroughput(starttime, System.currentTimeMillis(), N))
        + ", fairness: " + getFairness(N));
  }
  
  /**
   * .传入启动时间和对应启动的猴子的映射，启动仿真
   * @param map 对应映射关系
   */
  public void start(Map<Integer, List<Monkey>> map) {
    isOver = false;
    int N = 0;
    for (List<Monkey> l : map.values()) {
      N += l.size();
    }
    //System.out.println(N);
    int maxtime = 0;
    for (int i : map.keySet()) {
      if (i > maxtime) {
        maxtime = i;
      }
    }
    Random random = new Random();
    CountDownLatch countDownLatch = new CountDownLatch(N);
    SnapShot.preleft.clear();
    SnapShot.preright.clear();
    SnapShot.birth.clear();
    SnapShot.death.clear();
    int i = 0;
    long starttime = System.currentTimeMillis();
    for (i = 0; i <= maxtime; i++) {
      List<Monkey> l = map.get(i);
      if (l != null) {
        for (Monkey m : l) {
          m.setSelector(selectors.get(random.nextInt(selectors.size())));
          m.setCountDownLatch(countDownLatch);
          SnapShot.birth.put(m.getID(), System.currentTimeMillis());
          m.start();
        }
      }
      if (i == maxtime) {
        break;
      }
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    try {
      countDownLatch.await(); //等待所有猴子线程结束
      throughput = (double)N / ((System.currentTimeMillis() - starttime) / 1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    isOver = true;
    logger.info("End simulating, total: " + N  
                + ", time: " + (System.currentTimeMillis() - starttime) / 1000  
                + ", throughput: " + getThroughput(starttime, System.currentTimeMillis(), N) 
                + ", fairness: " + getFairness(N));
    VisualTool.updateConsole("End simulating, total: " + N + ", time: "
              + (System.currentTimeMillis() - starttime) / 1000 + ", throughput: "
              + getThroughput(starttime, System.currentTimeMillis(), N) 
              + ", fairness: " + getFairness(N));
    fairness = getFairness(N);
    System.out.println("Main Thread Down, total: " + N 
        + ", time: " + (System.currentTimeMillis() - starttime) / 1000
        + ", throughput: " + (throughput = getThroughput(starttime, System.currentTimeMillis(), N))
        + ", fairness: " + getFairness(N));
    
  }
  
  /**
   * .传入启动时间和对应启动的猴子的映射和选择策略，启动仿真
   * @param map 映射
   * @param selector 策略
   */
  public void start(Map<Integer, List<Monkey>> map, Select selector) {
    isOver = false;
    int N = 0;
    for (List<Monkey> l : map.values()) {
      N += l.size();
    }
    int maxtime = 0;
    for (int i : map.keySet()) {
      if (i > maxtime) {
        maxtime = i;
      }
    }
    CountDownLatch countDownLatch = new CountDownLatch(N);
    SnapShot.preleft.clear();
    SnapShot.preright.clear();
    SnapShot.birth.clear();
    SnapShot.death.clear();
    int i = 0;
    long starttime = System.currentTimeMillis();
    for (i = 0; i <= maxtime; i++) {
      List<Monkey> l = map.get(i);
      if (l != null) {
        for (Monkey m : l) {
          m.setSelector(selector);
          m.setCountDownLatch(countDownLatch);
          SnapShot.birth.put(m.getID(), System.currentTimeMillis());
          m.start();
        }
      }
      if (i == maxtime) {
        break;
      }
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    try {
      countDownLatch.await(); //等待所有猴子线程结束
      throughput = (double)N / ((System.currentTimeMillis() - starttime) / 1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    isOver = true;
    logger.info("End simulating, total: " + N  
                + ", time: " + (System.currentTimeMillis() - starttime) / 1000  
                + ", throughput: " + getThroughput(starttime, System.currentTimeMillis(), N) 
                + ", fairness: " + getFairness(N));
    VisualTool.updateConsole("End simulating, total: " + N + ", time: "
              + (System.currentTimeMillis() - starttime) / 1000 + ", throughput: "
              + getThroughput(starttime, System.currentTimeMillis(), N) 
              + ", fairness: " + getFairness(N));
    fairness = getFairness(N);
    System.out.println("Main Thread Down, total: " + N 
        + ", time: " + (System.currentTimeMillis() - starttime) / 1000
        + ", throughput: " + (throughput = getThroughput(starttime, System.currentTimeMillis(), N))
        + ", fairness: " + getFairness(N));
    
  }
  
  public boolean isOver() {
    return isOver;
  }
  
  /**
   * .设置要模拟的一组梯子
   * @param ladders 一组梯子
   */
  public void setLadders(Ladders ladders) {
    this.ladders = ladders;
  }
  
  /**
   * .返回最近一次模拟的公平性数据
   * @return 公平性数据，fairness -> [-1,1]
   */
  public double getFairness(int N) {
    int faircount = 0;
    for (int i = 1; i <= N - 1; i++) {
      for (int j = i + 1; j <= N; j++) {
        if ((SnapShot.birth.get(j) - SnapShot.birth.get(i))
              * (SnapShot.death.get(j) - SnapShot.death.get(i))
              >= 0) {
          faircount += 1;
        } else {
          faircount += -1;
        }
      }
    }
    BigDecimal s1 = factorial(N);
    BigDecimal s2 = factorial(N - 2);
    s2 = s2.multiply(new BigDecimal(2));
    BigDecimal c = s1.divide(s2);
    c = new BigDecimal(faircount).divide(c, 10, RoundingMode.HALF_DOWN);
    return c.doubleValue();
  }
  
  public double getFairness() {
    return fairness;
  }
  
  /**
   * . 计算阶乘
   * 
   * @param num 数字
   * @return num的阶乘
   */
  private BigDecimal factorial(int num) {
    if (num < 0) {
      throw new IllegalArgumentException("Negative number");
    }
    if (num == 0) {
      return new BigDecimal(1);
    }
    BigDecimal temp = new BigDecimal(1);
    for (int i = 1; i <= num; i++) {
      temp = temp.multiply(new BigDecimal(i));
    }
    return temp;
  }
  
  /**
   * .计算吞吐率
   * @param starttime 开始时间，系统毫秒表示
   * @param endtime 结束时间，系统毫秒表示
   * @param K 猴子总数
   * @return 吞吐率
   */
  public double getThroughput(long starttime, long endtime, int K) {
    return (double)K / ((endtime - starttime) / 1000);
  }
  
  /**
   * .返回最近一次模拟的吞吐率
   * @return 吞吐率
   */
  public double getThroughput() {
    return throughput;
  }
}
