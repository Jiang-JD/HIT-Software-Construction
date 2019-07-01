package circularorbit;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

/**.
 * 根据轨道物体对象的初始角度对position映射进行升序排序
 *
 * @param <E> 轨道物体类型
 */
class MapValueComparator<E> implements Comparator<Map.Entry<E, Position>>, Serializable {

  /**.
   * 
   */
  private static final long serialVersionUID = 2540349837753686771L;

  @Override
  public int compare(Entry<E, Position> o1, Entry<E, Position> o2) {
    double a1 = o1.getValue().getAngle();
    double a2 = o2.getValue().getAngle();
    return Double.compare(a1, a2);
  }
}
