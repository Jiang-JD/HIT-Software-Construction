package tool;

import items.Ladder;
import items.Ladders;
import items.Monkey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;



/**
 * .这个策略是首先选择空梯子，其次会对梯子进行判断，如果该猴子登上梯子
 * 后所耗费的时间不超出自己单独走完梯子时间，则登陆该梯子。其次选择离自己最近的猴子
 * 真实行进速度与自己最大速度最接近的梯子（速度差距在40%以内），特别的，速度为1、2、3
 * 的猴子只允许登上速度为1 2 3的梯子
 */
public class Selector7 implements Select {

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
      if ((SnapShot.preleft.get(monkey) == null || SnapShot.preright.get(monkey) == null)) {
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
        return true;
      }
      //先上空梯子
      if (ladders.size() == 1) {
        synchronized (ladders.get(0)) {
          if (ladders.get(0).isEmpty()) {
            ladders.get(0).add(monkey);
            LoggerTool.moving(monkey, ladders, ladders.get(0));
            VisualTool.updateConsole(LoggerTool.movingString(monkey, ladders, ladders.get(0)));
            return true;
          }
        }
      }
      for (Ladder l : ladders) {
        synchronized (l) {
          if (l.isEmpty()) {
            l.add(monkey);
            LoggerTool.moving(monkey, ladders, l);
            VisualTool.updateConsole(LoggerTool.movingString(monkey, ladders, l));
            return true;
          }
        }
      }
      // 快照不为空，进行比较
      // 计算偏移量
      boolean slow = false;
      List<Ladder> tmp = new ArrayList<Ladder>();
      Map<Ladder, Integer> bias = new HashMap<Ladder, Integer>();
      for (Ladder l : ladders) {
        synchronized (l) {
          if (l.isEmpty() || SnapShot.preleft.get(monkey) == null || SnapShot.preleft.get(monkey).get(l) == null) {
            continue;
          }
          int newposl = l.indexOf(SnapShot.preleft.get(monkey).get(l).getMonkey()); // 上一次快照最左猴子的新位置
          int newposr = l.indexOf(SnapShot.preright.get(monkey).get(l).getMonkey());// 上一次快照最右猴子的新位置

          // R->L，从右向左，通过最左边的猴子判断方向，通过最右边猴子判断真实行进速度
          if ((newposl == -1 || newposl < SnapShot.preleft.get(monkey).get(l).getPosition())
              && (newposr <= SnapShot.preright.get(monkey).get(l).getPosition() && newposr != -1)) {
            int biasr;
            biasr = newposr - SnapShot.preright.get(monkey).get(l).getPosition();
            bias.put(l, biasr);
            if (Math.abs(bias.get(l)) < 3) {
              slow = true;
            }
            if (monkey.getDirection().equals("R->L")) {
              tmp.add(l);
            }
            // L->R
          } else if ((newposr == -1 || newposr > SnapShot.preright.get(monkey).get(l).getPosition())
              && (newposl >= SnapShot.preleft.get(monkey).get(l).getPosition() && newposl != -1)) {
            int biasl;
            biasl = newposl - SnapShot.preleft.get(monkey).get(l).getPosition();
            bias.put(l, biasl);
            if (Math.abs(bias.get(l)) < 3) {
              slow = true;
            }
            if (monkey.getDirection().equals("L->R")) {
              tmp.add(l);
            }
          }
        }
      }
      //查看是否有梯子的末尾猴子快到终点
      List<Ladder> candidate = new LinkedList<Ladder>();
      if (candidate.isEmpty()) {
        for (Ladder l : tmp) {
          synchronized (l) {
            if (l.isEmpty() || bias.get(l) == 0) {
              continue;
            }
            if (monkey.getSpeed() >= 3 && slow) {
              continue;
            }
            if (Math.abs(monkey.getSpeed() - Math.abs(bias.get(l))) > 2) {
              continue;
            }
            int pos;
            double suppose = ((double)l.size() / monkey.getSpeed()) + 1;
            if (monkey.getDirection().equals("L->R")) {
              pos = l.indexOf(l.leftmost());
              if (((double)(l.size() - pos) / bias.get(l)) + 2.7 <= suppose) {
                candidate.add(l);
              }
            } else {
              pos = l.indexOf(l.rightmost());
              if (((double)(pos) / (-bias.get(l))) + 2.7 <= suppose) {
                candidate.add(l);
              }
            }
          }
        }
      }
      // 找到同方向上偏移量最接近本身速度的梯子，速度偏差量为40%以内，10%，30%，40%
      int zerosize = 0;
      int onesize = 0;
      int twosize = 0;
      int threesize = 0;
      int start = 0;
      if (!candidate.isEmpty()) {
        start = candidate.size() - 1;
      }
      
      
      int speed = monkey.getSpeed();
      if (monkey.getDirection().equals("R->L")) {
        speed = -speed;
      }
      for (Ladder l : tmp) {
        synchronized (l) {
          if (monkey.getSpeed() == 3 || monkey.getSpeed() == 2) {
            if (Math.abs(bias.get(l)) == 3 || Math.abs(bias.get(l)) == 2) {
              candidate.add(l);
              start++;
              continue;
            }
          }
          if (monkey.getSpeed() == 1 && Math.abs(bias.get(l)) >= 2) {
            continue;
          }
          if (monkey.getSpeed() - Math.abs(bias.get(l)) >= 4) {
            continue;
          }
          double percent = ((double)Math.abs(bias.get(l) - speed)) / monkey.getSpeed();
          if (percent <= 0.1) {
            candidate.add(0, l);
            zerosize++;
          } else if (percent <= 0.2) {
            candidate.add(start + zerosize, l);
            onesize++;
          } else if (percent < 0.3) {
            if (monkey.getSpeed() >= 3 && Math.abs(bias.get(l)) <= 2) {
              continue;
            }
            candidate.add(start + onesize + zerosize, l);
            twosize++;
          } else if (monkey.getSpeed() >= 10 && Math.abs(bias.get(l) - speed) == 3) {
            candidate.add(start + twosize + onesize + zerosize, l);
            threesize++;
          }
        }
      }
      for (int i = 0; i < candidate.size() - 1; i++) {
        if (bias.get(candidate.get(i)) == bias.get(candidate.get(i + 1))) {
          if (candidate.get(i).sizeOfMonkeys() 
                > candidate.get(i + 1).sizeOfMonkeys()) {
            Collections.swap(candidate, i, i + 1);
          }
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
              return true;
            }
          }
        }
      }
      // 空梯子也没有，原地等待并生成快照
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
        } else {
          pos = ladder.move(monkey, distance - 1);
          LoggerTool.moving(monkey, ladders, ladder);
          VisualTool.updateConsole(LoggerTool.movingString(monkey, ladders, ladder));
        }
        if (pos == -1) {
          LoggerTool.landing(monkey);
          VisualTool.updateConsole(LoggerTool.landingString(monkey));
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
    return true;
  }
}
