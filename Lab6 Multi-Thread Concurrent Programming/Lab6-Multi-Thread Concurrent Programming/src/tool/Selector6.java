package tool;

import items.Ladder;
import items.Ladders;
import items.Monkey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**.
 * 这个策略是见到梯子就上，只要是同方向就可以
 *
 */
public class Selector6 implements Select {

  @Override
  public boolean select(Monkey monkey, Ladders ladders) {
    Ladder ladder = null;
    for (Ladder l : ladders) {
      if (l.contain(monkey)) {
        ladder = l;
        break;
      }
    }
    if (ladder == null) {
      //看到空梯子就上
      for (Ladder l : ladders) {
        synchronized (l) {
          if (l.isEmpty()) {
            l.add(monkey);
            LoggerTool.moving(monkey, ladders, l);
            VisualTool.updateConsole(LoggerTool.movingString(monkey, ladders, l));
            printClimb(monkey, ladders, l);
            return true;
          }
        }
      }
      // 锁定上次快照，进行对比
      synchronized (SnapShot.preleft) {
        synchronized (SnapShot.preright) {
          // 上次快照为空，新建快照
          if ((SnapShot.preleft.get(monkey) == null && SnapShot.preright.get(monkey) == null)) {
            boolean emptyflag = true;
            Map<Ladder, Position> lmap = new HashMap<Ladder, Position>();
            Map<Ladder, Position> rmap = new HashMap<Ladder, Position>();
            for (Ladder l : ladders) {
              synchronized (l) {
                if (!l.isEmpty()) {
                  Position leftpos = new Position(l.leftmost(), l.indexOf(l.leftmost()));
                  Position rightpos = new Position(l.rightmost(), l.indexOf(l.rightmost()));
                  lmap.put(l, leftpos);
                  rmap.put(l, rightpos);
                  emptyflag = false;
                }
              }
            }
            SnapShot.preleft.put(monkey, lmap);
            SnapShot.preright.put(monkey, rmap);
            LoggerTool.waiting(monkey);
            VisualTool.updateConsole(LoggerTool.waitingString(monkey));
            printWaitAndShot(monkey);
            return true;
          }
          // 快照不为空
          // 每个梯子的最左和最右猴子位置单位时间偏移量（末-前），如果偏移为正，说明梯子L-R，否则R-L
          List<Ladder> tmp = new ArrayList<Ladder>();
          Map<Ladder, Integer> bias = new HashMap<Ladder, Integer>();
          for (Ladder l : ladders) {
            synchronized (l) {
              if (l.isEmpty() || SnapShot.preleft.get(monkey).get(l) == null) {
                continue;
              }
              int newposl = l.indexOf(SnapShot.preleft.get(monkey).get(l).getMonkey()); // 上一次快照最左猴子的新位置
              int newposr = l.indexOf(SnapShot.preright.get(monkey).get(l).getMonkey());// 上一次快照最右猴子的新位置
              // R->L
              if ((newposl == -1 || newposl < SnapShot.preleft.get(monkey).get(l).getPosition())
                  && (newposr <= SnapShot.preright.get(monkey).get(l).getPosition() && newposr != -1)) {
                int biasr = newposr - SnapShot.preright.get(monkey).get(l).getPosition();
                bias.put(l, biasr);
                if (monkey.getDirection().equals("R->L")) {
                  tmp.add(l);
                }
              // L->R
              } else if ((newposr == -1 || newposr > SnapShot.preright.get(monkey).get(l).getPosition())
                  && (newposl >= SnapShot.preleft.get(monkey).get(l).getPosition())) {
                int biasl = newposl - SnapShot.preleft.get(monkey).get(l).getPosition();
                bias.put(l, biasl);
                if (monkey.getDirection().equals("L->R")) {
                  tmp.add(l);
                }
              }
            }
          }
          // 决策为，看到梯子就上
          if (!tmp.isEmpty()) {
            // 从前往后依次选择梯子上去
            for (Ladder l : tmp) {
              synchronized (l) {
                if ((monkey.getDirection().equals("L->R") && !l.contain(0))
                    || (monkey.getDirection().equals("R->L") && !l.contain(l.size() - 1))) {
                  l.add(monkey);
                  LoggerTool.moving(monkey, ladders, l);
                  VisualTool.updateConsole(LoggerTool.movingString(monkey, ladders, l));
                  printObserveAndClimb(monkey, ladders, l);
                  return true;
                }
              }
            }
            //此时情况为，符合条件梯子都被抢了，选择其他空梯上去
            for (Ladder l : ladders) {
              synchronized (l) {
                if (l.isEmpty()) {
                  l.add(monkey);
                  LoggerTool.moving(monkey, ladders, l);
                  VisualTool.updateConsole(LoggerTool.movingString(monkey, ladders, l));
                  printClimb(monkey, ladders, l);
                  return true;
                }
              }
            }
          // tmp为空，表示没有符合条件的梯子，选择空梯上去
          } else {
            for (Ladder l : ladders) {
              synchronized (l) {
                if (l.isEmpty()) {
                  l.add(monkey);
                  LoggerTool.moving(monkey, ladders, l);
                  VisualTool.updateConsole(LoggerTool.movingString(monkey, ladders, l));
                  printClimb(monkey, ladders, l);
                  return true;
                }
              }
            }
          }
          // 空梯子也没有，原地等待生成快照
          LoggerTool.waiting(monkey);
          VisualTool.updateConsole(LoggerTool.waitingString(monkey));
          printObserveAndWait(monkey);
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
    }
    return true;
  }
  
  /**
   * .Take a snapshot
   * 
   * @param ladders ladders
   * @param monkey  monkey
   * @return True if success.
   */
  private boolean takeSnapShot(Ladders ladders, Monkey monkey) {
    for (Ladder l : ladders) {
      synchronized (l) {
        if (l.isEmpty()) {
          l.add(monkey);
          LoggerTool.moving(monkey, ladders, l);
          VisualTool.updateConsole(LoggerTool.movingString(monkey, ladders, l));
//          System.out.println("情况有变！伞兵 " + monkey.getID() + " 号卢本伟登上 " 
//                    + ladders.indexOf(l) + " 梯子！时间： " + monkey.getTime());
//          return true;
        }
        Position leftpos = new Position(l.leftmost(), l.indexOf(l.leftmost()));
        Position rightpos = new Position(l.rightmost(), l.indexOf(l.rightmost()));
        SnapShot.preleft.get(monkey).put(l, leftpos);
        SnapShot.preright.get(monkey).put(l, rightpos);
      }
    }
//    System.out.println("等待！伞兵 " + monkey.getID() + " 号生成快照： \n" 
//                  + SnapShot.preleft.get(monkey).toString() + "\n"
//                  + SnapShot.preright.get(monkey).toString());
    return true;
  }
    
  /**
   * .Print wait and take snapshot
   * 
   * @param monkey monkey
   */
  private void printWaitAndShot(Monkey monkey) {
//    System.out.println("伞兵 " + monkey.getID() + " 号卢本伟原地待命！时间： " + monkey.getTime());
//    System.out.println("等待！伞兵 " + monkey.getID() + " 号生成快照： \n" 
//                        + SnapShot.preleft.get(monkey).toString() + "\n" 
//                        + SnapShot.preright.get(monkey).toString());
  }
    
  /**
   * .Print observe and wait
   * 
   * @param monkey monkey
   */
  private void printObserveAndWait(Monkey monkey) {
//    System.out.println("等待！伞兵 " + monkey.getID() + " 号观察快照： \n" 
//                        + SnapShot.preleft.get(monkey).toString() + "\n" 
//                        + SnapShot.preright.get(monkey).toString());
//    System.out.println("伞兵 " + monkey.getID() + " 号卢本伟原地待命！时间： " + monkey.getTime());
  }
    
  /**
   * .Print observe and climb on the ladder
   * 
   * @param monkey  monkey
   * @param ladders ladders
   * @param r       climb ladder
   */
  private void printObserveAndClimb(Monkey monkey, Ladders ladders, Ladder r) {
//    System.out.println("上梯！伞兵 " + monkey.getID() + " 号观察快照： \n" 
//                          + SnapShot.preleft.get(monkey).toString() + "\n" 
//                          + SnapShot.preright.get(monkey).toString());
//    System.out.println("伞兵 " + monkey.getID() + " 号卢本伟登上 " 
//                          + ladders.indexOf(r) + " 梯子！" 
//                          + "时间： " + monkey.getTime());
  }
    
  /**
   * .Print climb
   * 
   * @param monkey  monkey
   * @param ladders ladders
   * @param l       climb ladder
   */
  private void printClimb(Monkey monkey, Ladders ladders, Ladder l) {
//    System.out.println("伞兵 " + monkey.getID() + " 号卢本伟登上 " + ladders.indexOf(l) + " 梯子！"
//          + "时间： " + monkey.getTime());
  }
    
  /**
   * .Print move
   * 
   * @param monkey   monkey
   * @param distance distance
   * @param ladders  ladders
   * @param ladder   ladder where monkey on it
   */
  private void printMove(Monkey monkey, int distance, Ladders ladders, Ladder ladder) {
//    System.out.println("伞兵 " + monkey.getID() + " 号卢本伟 " + ladders.indexOf(ladder) + " 号梯前进 " 
//            + distance + " 格到达 " 
//            + ladder.getPosition(monkey) 
//            + "，方向： " + monkey.getDirection()
//            + " 时间： " + monkey.getTime());
  }

  private void printLand(Monkey monkey) {
//    System.out.println("伞兵 " + monkey.getID() + " 号卢本伟成功抵达对岸！时间： " + monkey.getTime());
  }
}
