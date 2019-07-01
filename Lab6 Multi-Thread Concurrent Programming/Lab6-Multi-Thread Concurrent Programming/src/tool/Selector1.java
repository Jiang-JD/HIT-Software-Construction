package tool;

import items.Ladder;
import items.Ladders;
import items.Monkey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


/**
 * .这个策略是优先选择没有猴子的梯子，若所有梯子上都有猴子，则优先 选择没有与
 * 我对向而行的猴子的梯子；若满足该条件的梯子有很多， 则随机选择； 
 *
 */
public class Selector1 implements Select {

  @Override
  public boolean select(Monkey monkey, Ladders ladders) {
    Ladder ladder = null;
    boolean full = true;
    for (Ladder l : ladders) {
      if (l.contain(monkey)) {
        ladder = l;
        break;
      }
    }
    if (ladder == null) {
      synchronized (ladders) {
        List<Ladder> tmp = new ArrayList<Ladder>();
        for (Ladder l : ladders) {
          if (l.isEmpty()) {
            full = false;
            tmp.add(l);
          }
        }
        //梯子都是满的，此时ladders并没有没锁住
        if (full) {
          //锁定上次快照，进行对比
          synchronized (SnapShot.preleft) {
            synchronized (SnapShot.preright) {
              //上次快照为空或者上次快照时间不为1秒前（时效性已过），新建快照
              if ((SnapShot.preleft.get(monkey) == null && SnapShot.preright.get(monkey) == null)) {
                Map<Ladder, Position> lmap = new HashMap<Ladder, Position>();
                Map<Ladder, Position> rmap = new HashMap<Ladder, Position>();
                for (Ladder l : ladders) {
                  synchronized (l) {
                    if (!l.isEmpty()) {
                      Position leftpos = new Position(l.leftmost(), l.indexOf(l.leftmost()));
                      Position rightpos = new Position(l.rightmost(), l.indexOf(l.rightmost()));
                      lmap.put(l, leftpos);
                      rmap.put(l, rightpos);
                    }
                  }
                }
                SnapShot.preleft.put(monkey, lmap);
                SnapShot.preright.put(monkey, rmap);
                printWaitAndShot(monkey);
                LoggerTool.waiting(monkey);
                VisualTool.updateConsole(LoggerTool.waitingString(monkey));
                return true;
              }
              
              //每个梯子的最左和最右猴子位置单位时间偏移量（末-前），如果偏移为正，说明梯子L-R，否则R-L
              List<Integer> bias = new ArrayList<Integer>();  
              for (Ladder l : ladders) {
                synchronized (l) {
                  if (l.isEmpty() || SnapShot.preleft.get(monkey).get(l) == null) {
                    continue;
                  }
                  int newposl = 
                      l.indexOf(SnapShot.preleft.get(monkey).get(l).getMonkey()); //上一次快照最左猴子新位置
                  int newposr = 
                      l.indexOf(SnapShot.preright.get(monkey).get(l).getMonkey());//上一次快照最右猴子新位置

                  // R->L
                  if ((newposl == -1 || newposl < SnapShot.preleft.get(monkey).get(l).getPosition())
                      && (newposr <= SnapShot.preright.get(monkey).get(l).getPosition() 
                      && newposr != -1)) {
                    int biasr = newposr - SnapShot.preright.get(monkey).get(l).getPosition();
                    if (biasr == 0) {
                      biasr = Integer.MIN_VALUE;
                    }
                    bias.add(biasr);
                    if (monkey.getDirection().equals("R->L")) {
                      tmp.add(l);
                    }
                    // L->R
                  } else if ((newposr == -1 || newposr > SnapShot.preright.get(monkey).get(l).getPosition())
                      && (newposl >= SnapShot.preleft.get(monkey).get(l).getPosition())) {
                    int biasl = newposl - SnapShot.preleft.get(monkey).get(l).getPosition();
                    if (biasl == 0) {
                      biasl = Integer.MAX_VALUE;
                    }
                    bias.add(biasl);
                    if (monkey.getDirection().equals("L->R")) {
                      tmp.add(l);
                    }
                  }
                }
              }
              
              //根据筛选出来的符合猴子前进方向的梯子进行决策，此时可能有梯子为空（线程竞争）
              if (!tmp.isEmpty()) {
                boolean onboard = false; //是否登上梯子，这里可能出现空梯子或者随机出的梯子第一个踏板有猴，所以需要添加标志判断
                while (!onboard) {
                  Ladder r;
                  synchronized ((r = tmp.get(new Random().nextInt(tmp.size())))) {
                    if ((monkey.getDirection().equals("L->R") && !r.contain(0))
                        || (monkey.getDirection().equals("R->L") && !r.contain(r.size() - 1))) {
                      r.add(monkey);
                      LoggerTool.moving(monkey, ladders, r);
                      VisualTool.updateConsole(LoggerTool.movingString(monkey, ladders, r));
                      printObserveAndClimb(monkey, ladders, r);
                      return true;
                    } 
                  }
                }
              } else {
                LoggerTool.waiting(monkey);
                VisualTool.updateConsole(LoggerTool.waitingString(monkey));
                printObserveAndWait(monkey);
                return takeSnapShot(ladders, monkey);
              }
            }
          }
          
        //梯子不是满的,tmp中为空梯子，但是此时梯子没有加锁，所以可能存在tmp中存储的梯子被抢占了，如果都被占用，则原地等待
        } else {
          int i = 0;
          while (i <= tmp.size()) {
            Ladder l;
            synchronized ((l = tmp.get(new Random().nextInt(tmp.size())))) {
              if (l.isEmpty()) {
                l.add(monkey);
                LoggerTool.moving(monkey, ladders, l);
                VisualTool.updateConsole(LoggerTool.movingString(monkey, ladders, l));
                printClimb(monkey, ladders, l);
                return true;
              }
            }
            i++;
          }
          printObserveAndWait(monkey);
          LoggerTool.waiting(monkey);
          VisualTool.updateConsole(LoggerTool.waitingString(monkey));
          return takeSnapShot(ladders, monkey);
        }
      }      
    } else {
      synchronized (ladder) {
        int distance = ladder.occupy(monkey, monkey.getDirection());
        int pos;
        if (distance > monkey.getSpeed() || distance == -1) {
          pos = ladder.move(monkey, monkey.getSpeed());
          LoggerTool.moving(monkey, ladders, ladder);
          VisualTool.updateConsole(LoggerTool.movingString(monkey, ladders, ladder));
          printMove(monkey, monkey.getSpeed(), ladders, ladder);
        } else {
          pos = ladder.move(monkey, distance - 1);
          LoggerTool.moving(monkey, ladders, ladder);
          VisualTool.updateConsole(LoggerTool.movingString(monkey, ladders, ladder));
          printMove(monkey, distance - 1, ladders, ladder);
        }
        if (pos == -1) {
          LoggerTool.landing(monkey);
          VisualTool.updateConsole(LoggerTool.landingString(monkey));
          printLand(monkey);
          return false;
        }
      }
      return true;
    }
    return true;
  }
  
  /**
   * .Take a snapshot
   * @param ladders ladders
   * @param monkey monkey
   * @return True if success.
   */
  private boolean takeSnapShot(Ladders ladders, Monkey monkey) {
    for (Ladder l : ladders) {
      synchronized (l) {
        if (l.isEmpty()) {
          l.add(monkey);
          LoggerTool.moving(monkey, ladders, l);
          VisualTool.updateConsole(LoggerTool.movingString(monkey, ladders, l));
          return true;
        }
        Position leftpos = new Position(l.leftmost(), l.indexOf(l.leftmost()));
        Position rightpos = new Position(l.rightmost(), l.indexOf(l.rightmost()));
        SnapShot.preleft.get(monkey).put(l, leftpos);
        SnapShot.preright.get(monkey).put(l, rightpos);
      }
    }
//    System.out.println("等待！伞兵 " + monkey.getID() + " 号生成快照： \n" 
//                + SnapShot.preleft.get(monkey).toString() + "\n"
//                + SnapShot.preright.get(monkey).toString());
    return true;
  }
  
  /**
   * .Print wait and take snapshot
   * @param monkey monkey
   */
  private void printWaitAndShot(Monkey monkey) {
//    System.out.println("伞兵 " + monkey.getID() + " 号卢本伟原地待命！时间： " + monkey.getTime());
//    System.out.println("等待！伞兵 " + monkey.getID() + " 号生成快照： \n" 
//                      + SnapShot.preleft.get(monkey).toString() + "\n" 
//                      + SnapShot.preright.get(monkey).toString());
  }
  
  /**
   * .Print observe and wait
   * @param monkey monkey
   */
  private void printObserveAndWait(Monkey monkey) {
//    System.out.println("等待！伞兵 " + monkey.getID() + " 号观察快照： \n" 
//                      + SnapShot.preleft.get(monkey).toString() + "\n" 
//                      + SnapShot.preright.get(monkey).toString());
//    System.out.println("伞兵 " + monkey.getID() + " 号卢本伟原地待命！时间： " + monkey.getTime());
  }
  
  /**
   * .Print observe and climb on the ladder
   * @param monkey monkey
   * @param ladders ladders
   * @param r climb ladder
   */
  private void printObserveAndClimb(Monkey monkey, Ladders ladders, Ladder r) {
//    System.out.println("上梯！伞兵 " + monkey.getID() + " 号观察快照： \n" 
//                        + SnapShot.preleft.get(monkey).toString() + "\n" 
//                        + SnapShot.preright.get(monkey).toString());
//    System.out.println("伞兵 " + monkey.getID() + " 号卢本伟登上 " + ladders.indexOf(r) + " 梯子！"
//                        + "时间： " + monkey.getTime());
  }
  
  /**
   * .Print climb
   * @param monkey monkey
   * @param ladders ladders
   * @param l climb ladder
   */
  private void printClimb(Monkey monkey, Ladders ladders, Ladder l) {
//    System.out.println("伞兵 " + monkey.getID() + " 号卢本伟登上 " + ladders.indexOf(l) + " 梯子！"
//        + "时间： " + monkey.getTime());
  }
  
  /**
   *.Print move
   * @param monkey monkey
   * @param distance distance
   * @param ladders ladders
   * @param ladder ladder where monkey on it
   */
  private void printMove(Monkey monkey, int distance, Ladders ladders, Ladder ladder) {
//    System.out.println("伞兵 " + monkey.getID() + " 号卢本伟 " + ladders.indexOf(ladder) + " 号梯前进 " 
//          + distance + " 格到达 " 
//          + ladder.getPosition(monkey) 
//          + "，方向： " + monkey.getDirection()
//          + " 时间： " + monkey.getTime());
  }
  
  private void printLand(Monkey monkey) {
//    System.out.println("伞兵 " + monkey.getID() + " 号卢本伟成功抵达对岸！时间： " + monkey.getTime());
  }
}
