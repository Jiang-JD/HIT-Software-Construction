package circularorbit;

import applications.tools.Memento;

/**.
 * 提供一系列对象状态转换方法，用来切换对象的状态或维持对象当前状态
 *
 */
public interface State<L, E> {

  /**.
   * 创建当前轨道系统状态的备忘录条目
   * 
   * @param as 待备份系统
   * @return 当前轨道系统状态的备份
   */
  public Memento backup(AtomStructure as);

  /**.
   * 将轨道系统结构状态恢复到某一个备忘录记录的状态
   * 
   * @param m  待恢复到的备忘录条目，要求不为空
   * @param as 待恢复原子轨道系统，轨道系统不为空对象
   * @return 如果恢复成功，返回{@code true} ,否则返回 false
   */
  public boolean restore(Memento m, AtomStructure as);
}
