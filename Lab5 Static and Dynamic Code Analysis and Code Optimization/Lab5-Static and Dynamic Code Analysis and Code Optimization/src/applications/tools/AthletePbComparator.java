package applications.tools;

import java.io.Serializable;
import java.util.Comparator;

import physicalobject.Athlete;

/**.
 * Athlete成绩比较器，升序排列
 *
 */
public class AthletePbComparator implements Comparator<Athlete>, Serializable {

  private static final long serialVersionUID = 6645130957654009024L;

  @Override
  public int compare(Athlete o1, Athlete o2) {
    return Double.compare(o2.getPersonalBest(), o1.getPersonalBest());
  }

}
