package circularorbit;

import applications.tools.Memento;
import centralobject.Nucleus;
import physicalobject.Electron;

/**.
 * State具体实现类，负责执行备份和恢复操作
 *
 */
public class ConcreteAtomState implements State<Nucleus, Electron> {

  /**.
   * 将当前的原子轨道系统状态备份，备份信息包括中心物体，轨道和每一条轨道对应的电子。
   */
  @Override
  public Memento backup(AtomStructure as) {
    if (as == null) {
      throw new NullPointerException();
    }
    return new Memento(as.getTracks(), as.getTrackMap(), as.getCentralPoint());
  }

  /**.
   * 将原子结构恢复到某一个备忘录条目记录的状态，注意，指定这个方法会恢复原子结构的轨道，中心物体，
   * 每一条轨道上所处的电子信息，也就意味着恢复之前的状态将被破坏。
   */
  @Override
  public boolean restore(Memento m, AtomStructure as) {
    if (m == null) {
      return false;
    }
    as.restoreTracks(m.getTracks());
    as.restoreMap(m.getMap());
    as.addCentralPoint(m.getNucleus());
    return true;
  }

}
