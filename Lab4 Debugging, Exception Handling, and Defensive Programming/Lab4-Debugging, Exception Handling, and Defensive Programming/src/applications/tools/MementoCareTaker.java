package applications.tools;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * 管理所有电子跃迁备忘录的类，管理所有备忘录状态，
 * 负责存入，取出相关状态的备忘录
 *
 */
public class MementoCareTaker {
	private final List<Memento> memento = new ArrayList<Memento>();
	private Logger logger = Logger.getLogger(MementoCareTaker.class);

	/*
	 * AF 
	 * 	AF(memento) = 一个管理所有备忘记录的备忘录
	 * 
	 * RI
	 * 	memento 列表保存的对象不为空
	 * 
	 * Safety from exposure
	 * 	所有域是private final的，memento是immutable类，客户端拿到 后无法修改
	 */
	
	private void checkRep() {
		for(Memento m : memento) {
			assert m != null : "备忘录中存在空对象";
		}
	}
	
	/**
	 * 获得备忘录中记录条数
	 * @return 备忘录中记录条数
	 */
	public int size() {
		return memento.size();
	}
	
	/**
	 * 检查备忘录是否为空，返回{@code true} 如果为空
	 * @return 如果备忘录为空，返回{@code true}
	 */
	public boolean isEmpty() {
		return memento.isEmpty();
	}
	
	/**
	 * 向备忘录中添加备忘条目，要求条目不为空
	 * @param m 待添加备忘条目
	 * @return 如果添加成功，返回{@code true}
	 */
	public boolean add(Memento m) {
		if(m == null) return false;
		memento.add(0,m);
		logger.info("Add a new Memento"+m.toString());
		checkRep();
		return true;
	}
	
	/**
	 * 获得指定备忘条目，输入指定位置
	 * @param index 需要获取的备忘录的指定位置
	 * @return 指定位置的备忘录，若index超过范围，返回null
	 */
	public Memento get(int index) {
		if(index < 0 || index >= memento.size()) return null;
		return memento.get(index);
	}
	
	/**
	 * 获得指定备忘录的索引
	 * @param m 指定备忘录
	 * @return 索引，未找到返回-1
	 */
	public int indexOf(Memento m) {
		return memento.indexOf(m);
	}
	
	/**
	 * 移除指定备忘条目，输入指定移除备忘条目
	 * @param m 待移除备忘条目
	 * @return 如果移除成功，返回{@code true}，否则false
	 */
	public boolean remove(Memento m) {
		if(m == null) return false;
		logger.info("Remove a memento" + m.toString());
		return memento.remove(m);
	}
	
	/**
	 * 查找备忘录中是否包含指定条目
	 * @param m 指定条目
	 * @return 如果包含，返回{@code true}，否则false
	 */
	public boolean contains(Memento m) {
		if(m == null) return false;
		return memento.contains(m);
	}
	
	/**
	 * 清空备忘录
	 */
	public void clear() {
		logger.info("Clear the memento");
		memento.clear();
	}
	
	/**
	 * 生成描述整个备忘录的字符串
	 */
	@Override
	public String toString() {
		return memento.toString();
	}
	
	/**
	 * 以表格形式输出备忘录信息
	 * @return 表格形式字符串信息
	 */
	public String toTable() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");//设置时间日期格式
		StringBuilder sb = new StringBuilder();
		sb.append("+--------------------------------------------------------------------+\n"
				+ "|Index\t|Time\t\t\t|Central\t|Tracks\t|ElectronNum\n"
				+ "+--------------------------------------------------------------------+\n");
		int index = 1;
		for(Memento m : memento) {
			StringBuilder ms = new StringBuilder();
			for(int i = 0; i < m.getTracks().size(); i++) {
				ms.append(m.getTracks().get(i).getNumber()+"/"+m.getMap().get(m.getTracks().get(i)).size()+";");
			}
			sb.append("|"+index+"\t|"+m.getTime().format(dtf)+"\t|"+m.getNucleus().getName()+"\t\t|"+m.getTracks().size()+"\t|"+ms.toString()
					 +"\n+--------------------------------------------------------------------+\n");
			index++;
		}
		return sb.toString();
	}
}
