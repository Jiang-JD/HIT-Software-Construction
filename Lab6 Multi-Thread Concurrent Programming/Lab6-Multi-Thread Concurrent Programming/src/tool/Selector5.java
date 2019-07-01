package tool;

import items.Ladder;
import items.Ladders;
import items.Monkey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * 这个策略优先选择离自己最近的猴子真实行进速度与自己最大速度最接近的梯子（误差不超过2且方向相同），
 * 如果某只猴子最大速度为2及以上，则猴子不能选择速度为1的梯子.
 * 若没有符合条件的梯子则选择空梯子，如果猴子行进速度为1、2或3，则在选择空梯子时至少保留
 * 总梯子数量的40%的空梯子，如果不满1条则补足为1条。剩余梯子不足时，该猴子原地等待。（如果只有一条梯子，则
 * 该猴子可以上去）
 * 对于速度超过3的猴子，梯子没有限制
 */
public class Selector5 implements Select {

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
        // 提前判断如果都是空的，随机上梯
        if (emptyflag) {
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
        SnapShot.preleft.put(monkey, lmap);
        SnapShot.preright.put(monkey, rmap);
        printWaitAndShot(monkey);
        LoggerTool.waiting(monkey);
        VisualTool.updateConsole(LoggerTool.waitingString(monkey));
        return true;
      }
      // 快照不为空，进行比较
      // 计算偏移量
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

      // 找到同方向上偏移量最接近本身速度的梯子，优先相同，其次偏差量在 +-2 以内
      //高速猴子允许偏差量更大，速度为10允许下探到7
      // 如果猴子行进速度为3或3以上，则不添加速度为1 2的梯子，6不添加4和5的梯子，2不添加1的梯子
      int zerosize = 0;
      int onesize = 0;
      int twosize = 0;
      int threesize = 0;
      List<Ladder> candidate = new LinkedList<Ladder>();
      int speed = monkey.getSpeed();
      if (monkey.getDirection().equals("R->L")) {
        speed = -speed;
      }
      for (Ladder l : tmp) {
        if (monkey.getSpeed() >= 3 && Math.abs(bias.get(l)) <= 2) {
          continue;
        }
        if (bias.get(l) == speed) {
          candidate.add(0, l);
          zerosize++;
        } else if (Math.abs(bias.get(l) - speed) == 1) {
          if (monkey.getSpeed() == 6 && Math.abs(bias.get(l)) < 6) {
            continue;
          }
          if (monkey.getSpeed() == 1) {
            continue;
          }
          if (monkey.getSpeed() == 2 && Math.abs(bias.get(l)) == 1) {
            continue;
          }
          candidate.add(zerosize, l);
          onesize++;
        } else if (Math.abs(bias.get(l) - speed) == 2) {
          if (monkey.getSpeed() == 6 && Math.abs(bias.get(l)) < 6) {
            continue;
          }
          if (monkey.getSpeed() == 1) {
            continue;
          }
          candidate.add(onesize + zerosize, l);
          twosize++;
        } else if (monkey.getSpeed() >= 10 && Math.abs(bias.get(l) - speed) == 3) {
          candidate.add(twosize + onesize + zerosize, l);
          threesize++;
        }
      }
      // 若筛选出的梯子非空，依次选择上梯
      if (!candidate.isEmpty()) {
        for (Ladder l : candidate) {
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

        // 若没有符合条件梯子，选择空梯
        // 若猴子的速度为1 2 3 选择空梯子时至少保留
        // 总梯子数量的40%的空梯子，如果不满1条则补足为1条。
        // 如果梯子总数为1，直接上
      } else {
        if (ladders.size() == 1) {
          synchronized (ladders.get(0)) {
            if (ladders.get(0).isEmpty()) {
              ladders.get(0).add(monkey);
              LoggerTool.moving(monkey, ladders, ladders.get(0));
              VisualTool.updateConsole(LoggerTool.movingString(monkey, ladders, ladders.get(0)));
              printClimb(monkey, ladders, ladders.get(0));
              return true;
            }
          }
        } else {
          int keep = (int) (ladders.size() * 0.4);
          if (keep < 1) {
            keep = 1;
          }
          int count = 0;
          for (Ladder l : ladders) {
            synchronized (l) {
              if (l.isEmpty()) {
                if (monkey.getSpeed() <= 3 && count >= keep) {
                  l.add(monkey);
                  LoggerTool.moving(monkey, ladders, l);
                  VisualTool.updateConsole(LoggerTool.movingString(monkey, ladders, l));
                  printClimb(monkey, ladders, l);
                  return true;
                } else if (monkey.getSpeed() > 3) {
                  l.add(monkey);
                  LoggerTool.moving(monkey, ladders, l);
                  VisualTool.updateConsole(LoggerTool.movingString(monkey, ladders, l));
                  printClimb(monkey, ladders, l);
                  return true;
                }
                count++;
              }
            }
          }
        }
      }
      // 空梯子也没有，原地等待并生成快照
      printObserveAndWait(monkey);
      LoggerTool.waiting(monkey);
      VisualTool.updateConsole(LoggerTool.waitingString(monkey));
      return takeSnapShot(ladders, monkey);
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
        if (!l.isEmpty()) {
          Position leftpos = new Position(l.leftmost(), l.indexOf(l.leftmost()));
          Position rightpos = new Position(l.rightmost(), l.indexOf(l.rightmost()));
          SnapShot.preleft.get(monkey).put(l, leftpos);
          SnapShot.preright.get(monkey).put(l, rightpos);
        }
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
