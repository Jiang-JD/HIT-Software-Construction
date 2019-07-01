package tool;

import items.Ladder;
import items.Ladders;
import items.Monkey;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * .这个策略是等到梯子完全空闲才登上梯子
 *
 */
public class Selector3 implements Select {

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
          synchronized (l) {
            if (l.isEmpty()) {
              full = false;
              tmp.add(l);
            }
          }
        }
        if (full) {
          LoggerTool.waiting(monkey);
          VisualTool.updateConsole(LoggerTool.waitingString(monkey));
          return true;
        }
        int i = 0;
        while (i <= tmp.size()) {
          Ladder l;
          synchronized ((l = tmp.get(new Random().nextInt(tmp.size())))) {
            if (l.isEmpty()) {
              l.add(monkey);
              LoggerTool.moving(monkey, ladders, l);
              VisualTool.updateConsole(LoggerTool.movingString(monkey, ladders, l));
              return true;
            }
          }
          i++;
        }
        LoggerTool.waiting(monkey);
        VisualTool.updateConsole(LoggerTool.waitingString(monkey));
        return true;
      }
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
    }
    return true;
  }

}
