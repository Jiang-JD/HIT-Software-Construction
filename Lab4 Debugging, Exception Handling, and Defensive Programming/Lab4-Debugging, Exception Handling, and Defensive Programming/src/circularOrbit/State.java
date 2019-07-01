package circularOrbit;

import java.util.List;
import java.util.Map;

import applications.tools.Memento;
import centralObject.Nucleus;
import physicalObject.Electron;
import track.Track;

/**
 * 提供一系列对象状态转换方法，用来切换对象的状态或维持对象当前状态
 *
 */
public interface State<L,E> {
	
	/**
	 * 创建当前轨道系统状态的备忘录条目
	 * 
	 * @param tracks 待备份轨道，不为空对象且列表中不存在空对象
	 * @param trackmap 待备份映射关系，不为空对象且映射中不存在空对象
	 * @param cp 待备份中心轨道，不为空对象
	 * @return 当前轨道系统状态的备份
	 */
	public Memento backup(AtomStructure as);
	
	/**
	 * 将轨道系统结构状态恢复到某一个备忘录记录的状态
	 * 
	 * @param m  待恢复到的备忘录条目，要求不为空
	 * @param as 待恢复原子轨道系统，轨道系统不为空对象
	 * @return 如果恢复成功，返回{@code true} ,否则返回 false
	 */
	public boolean restore(Memento m, AtomStructure as);
}
