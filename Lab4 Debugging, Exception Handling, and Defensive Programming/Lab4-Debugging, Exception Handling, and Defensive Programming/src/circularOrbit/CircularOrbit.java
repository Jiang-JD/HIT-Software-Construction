package circularOrbit;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import parser.Parser;


/**
 * {@code CircularOrbit}是一个由唯一一个中心点和多条轨道构成的系统，在中心点 {@code L} 和 轨道上承载多个物体 {@code E}，
 * 物体在轨道上可保持静止或持续 运动，物体与物体之间可存在联系。 将该类系统放在直角坐标系中，
 * 可看作是以原点(0,0) 为中心的多个同心圆.
 * 
 * <p>系统中的轨道都有自己的编号，由内向外的编号从0开始逐渐递增，且每条轨道的半径/权重/编号都不相同
 * <p>这个轨道系统是可变的，在运行过程中，轨道系统会根据不同的要求而发生变化。因此轨道系统是
 * {@code mutable}的。
 */
public interface CircularOrbit<L,E> extends Iterable<E>{
	/**
	 * 向对象中添加新的、空的track，如果对象中没有相同的track，那么就添加，否则不添加，
	 * 新添加的track默认在track序列的最后一位，也就是在同心圆的最外面一层。
	 * 
	 * @param radius 轨道的半径/权重/编号
	 * @return 如果添加成功则返回新轨道在系统中的编号,由内向外的编号从0开始逐渐递增，否则返回-1，即添加失败
	 */
	int addTrack(double radius);
	
	/**
	 * 移除对象中轨道，如果存在该轨道，则移除轨道，包括轨道中的physicalobject一块移除，
	 * 返回被移除track的对象。如果系统中的track是顺序的，那么该track顺位tracks依次前移一位。
	 * 即如果轨道顺序为 a-b-c-d，移去c，顺序变为 a-b-d
	 *  
	 * @param index 待移除的轨道编号,轨道编号 >= 0
	 * @return 如果移除成功，则返回true，否则若编号超过轨道数量返回false
	 */
	boolean remove(int index);
	
	/**
	 * 向系统添加中心物体，如果没有中心点物体，则添加中心点物体，返回null。若存在中心点物体，
	 * 则将中心点物体替换为新的中心物体，返回旧的物体。
	 * 
	 * @param centralPoint 需要添加的中心物体，中心物体非空
	 * @return 如果系统之前没有中心物体则返回null，否则如果是新旧物体的替换则返回之前的那个
	 * 旧的中心物体。
	 */
	L addCentralPoint(L centralPoint);
	
	/**
	 * 获得系统中心物体，若系统没有中心物体，返回null
	 * @return 系统中心物体，如果没有返回null
	 */
	L getCentralPoint();
	
	
	/**
	 * 向指定轨道添加物体，物体必须为PhysicalObject类型物体或其子类，track在轨道系统中必须存在。
	 * 不要求物体必须不同（例如电子都是相同的），添加的物体在track中没有次序，只是按照先后的存放
	 * 顺序存储。
	 * 
	 * @param object 需要添加的轨道物体，物体必须为PhysicalObject类型物体或其子类
	 * @param index  需要将物体添加到的轨道的编号，轨道编号 >= 0
	 * @return true如果添加成功，否则若轨道编号超过轨道数量则返回false
	 */
	boolean addObject(E object, int index);
	
	/**
	 * 向指定轨道添加物体，设置初始角度，物体必须为PhysicalObject类型物体或其子类，track在orbit中必须存在，
	 * angle为角度制，角度 0 <= angle < 360。不要求物体必须不同（例如电子都是相同的），添加
	 * 的物体在track中没有次序。
	 * 
	 * @param object  需要添加的轨道物体，物体必须为PhysicalObject类型物体或其子类
	 * @param index  需要将物体添加到的轨道的编号，轨道编号 >= 0
	 * @param angle  初始角度，极坐标角度 0 <= angle < 360
	 * @return  true如果添加成功，否则若轨道编号超过轨道数量则返回false
	 */
	boolean addObject(E object, int index, double angle);
	
	/**
	 * 获得指定轨道上的所有轨道物体，返回物体无确定顺序，若轨道没有物体，则返回空表
	 * @param index 指定轨道的编号，轨道编号 >= 0
	 * @return 指定轨道上所有的轨道物体，如果编号超过轨道数量，则返回null，如果指定轨道没有物体，则 返回空表
	 */
	List<E> getObjects(int index);
	
	/**
	 * 移除指定的物体，将指定物体从系统中移除，所属轨道将不包含物体。
	 * 特别的，如果系统中所有物体都一样（比如说电子），那么此方法会从最内层移除一个物体，如果物体包含角度，
	 * 将从初始角度最小的开始移除。
	 * 
	 * @param object 指定轨道上物体,物体非空
	 * @return 如果移除成功，返回true，否则，若物体不存在，返回false
	 */
	boolean remove(E object);
	
	/**
	 * 移除指定轨道上指定物体，将指定物体从系统中移除，所属轨道将不包含物体。
	 * 特别的，如果轨道中所有物体都一样（比如说电子），那么此方法会随机移除，如果物体包含角度，
	 * 将从初始角度最小的移除。
	 * @param object 指定轨道上物体
	 * @param index  指定轨道的编号，轨道编号 >= 0
	 * @return 如果移除成功，返回true，否则，若物体不存在或轨道不存在，返回false
	 */
	boolean remove(E object, int index);
	
	/**
	 * 添加中心物体和轨道物体的无向关系，关系只能添加一次且不能重复添加，中心点和轨道物体都必须
	 * 在轨道系统中存在。
	 * 
	 * @param orbitObject 需要添加关系的轨道物体
	 * @return true如果添加成功，否则，如果之前添加过这个物体的关系，返回false
	 */
	boolean addCentralRelation(E orbitObject);
	
	/**
	 * 移除中心物体和轨道物体关系，如果关系存在则移除，返回被移除关系的轨道物体对象，否则返回null。
	 * 要求该轨道系统中中心物体存在。中心点自身不能添加关系
	 * 。
	 * @param orbitObject 需要解除关系的轨道物体
	 * @return 如果关系存在则返回true。否则，如果该系统中不存在该轨道物体，或者
	 * 该轨道物体与中心物体没有关系，则返回false
	 */
	boolean removeCentralRelation(E orbitObject);
	
	/**
	 * 添加轨道物体无向关系，两个轨道物体必须存在与轨道系统中。不要求所在轨道必须相同。关系只能添加一次
	 * 且不能重复添加。
	 * 
	 * @param object1  需要添加关系的第一个物体
	 * @param object2  需要添加关系的第二个物体
	 * @return 如果之前没有添加，则添加关系成功返回true，否则返回false
	 */
	boolean addOrbitRelation(E object1, E object2);
	
	/**
	 * 移除两个轨道物体的关系，两个轨道物体都必须存在与轨道系统中。
	 * 
	 * @param object1  需要解除关系的第一个物体
	 * @param object2  需要解除关系的第二个物体
	 * @return 如果两个物体的关系存在则移除，返回true，否则，如果两个物体不存在关系
	 * ，返回false
	 */
	boolean removeOrbitRelation(E object1, E object2);
	
	/**
	 * 获得所有与中心点有关的轨道物体集合，轨道系统中必须存在中心点。
	 * 
	 * @return 所有与中心物体存在关系的轨道物体的集合，如果没有任何物体与中心点存在关系，则
	 * 返回空集
	 */
	Set<E> centrals();
	
	/**
	 * 获得所有与指定轨道物体有关系的轨道物体集合
	 * 
	 * @param object 指定查找的轨道物体
	 * @return 返回所有与指定轨道物体有关系的轨道物体的集合，如果指定轨道物体不存在于系统或
	 * 没有物体与指定物体有关系则返回空集
	 */
	Set<E> relatives(E object);
	
	/**
	 * 根据文件图输入和对应的解析器将轨道系统对象初始化，要求轨道系统对象为空图，否则之前的轨道系统
	 * 对象不会保存，在初始化时会被破坏原有结构。例如，跑步比赛的轨道系统初始化需要对应的文件输入和
	 * 对应的跑步比赛格式的解析器。
	 * 
	 * @param file 输入文件要求格式正确，否则会抛出错误。
	 * @param parser 文件解析器，对文件进行解析
	 * @return 初始化后的新的轨道系统对象
	 */
	CircularOrbit<L,E> buildCircularObject(File file, Parser<?, ?> parser);
	
	/**
	 * 修改某物体所在的轨道，也就是修改某轨道物体的轨道属性，例如a属于track1，修改为a属于track2。
	 * 新的轨道必须在轨道系统中存在，轨道物体也必须存在，修改后返回轨道物体之前所属的track对象的编号，
	 * 否则返回-1。特别的，如果所有物体都一样，则从最内层存在物体的轨道上随机选择一个物体跃迁。
	 * 
	 * @param object  需要修改的轨道物体对象
	 * @param newIndex  需要跃迁的新的轨道的编号，轨道编号 >= 0
	 * @return  如果修改成功，则返回之前旧的轨道的编号。否则返回-1
	 */
	int transit(E object, int newIndex);
	
	/**
	 * 修改指定轨道上某物体所在的轨道，也就是修改某轨道物体的轨道属性，例如指定属于track1的a，修改为a属于track2。
	 * 新的轨道必须在轨道系统中存在，轨道物体也必须存在。特别的，如果所有物体都一样，则从制定轨道上随机选择一个物体
	 * 跃迁。
	 * 
	 * @param object  需要修改的轨道物体对象
	 * @param oldIndex  需要跃迁的旧的轨道的编号，轨道编号 >= 0
	 * @param newIndex  需要跃迁的新的轨道的编号，轨道编号 >= 0，新轨道编号与旧轨道不同
	 * @return  如果修改成功，则返回true。否则返回false
	 */
	boolean transit(E object, int oldIndex, int newIndex);
	
	/**
	 * 将指定轨道上的一个物体移动到指定的新轨道上，物体的选择为其内部存储顺序第一个。指定的轨道在系统中需要存在。
	 * 
	 * @param oldIndex  需要跃迁的旧的轨道的编号，轨道编号 >= 0
	 * @param newIndex  需要跃迁的新的轨道的编号，轨道编号 >= 0，新轨道编号与旧轨道不同
	 * @return 如果修改成功，返回true， 否则，如果指定轨道上无物体，返回false
	 */
	boolean transit(int oldIndex, int newIndex);
	
	/**
	 * 生成迭代器遍历轨道物体，顺序为从内轨道逐步向外，同一轨道物体按角度从小到大次序（如果没有角度就随机）
	 * 
	 * @return 遍历轨道物体的迭代器
	 */
	Iterator<E> iterator();
	
	/**
	 * 查找系统中是否包含指定物体
	 * 
	 * @param object  需要查找物体，物体不为空
	 * @return 如果系统中存在该轨道物体返回true，否则如果系统中不存在该轨道物体则返回false
	 */
	boolean contains(E object);
	
	/**
	 * 查找指定轨道中是否包含指定轨道物体
	 * 
	 * @param object  需要查找的轨道物体
	 * @param index  需要查找的轨道编号，轨道编号 >= 0
	 * @return  true如果指定轨道中指定物体存在，否则，如果指定轨道不存在或者指定物体不存在或者
	 * 轨道中不包含轨道物体，则返回false。
	 */
	boolean contains(E object, int index);
	
	/**
	 * 查找系统中是否包含指定轨道
	 * @param radius  需要查找的指定轨道的半径
	 * @return true如果指定轨道在系统中存在，否则返回false
	 */
	boolean contains(double radius);
	
	/**
	 * 获取指定物体在系统中所在轨道的索引/编号，轨道编号从里向外分别为0，1，2……, 如果系统中不存在指定的轨道，返回-1。
	 * 具体来说，如果存在多个相同物体存在于多个轨道，返回的索引是系统中最靠近中心的轨道的索引。
	 * 
	 * @param object 需要查找的物体
	 * @return 轨道的索引，不存在返回-1，如果存在多个则返回最靠近中心的轨道索引。
	 */
	int indexOf(E object);
	
	/**
	 * 获取指定半径/编号轨道的索引/编号，轨道编号从里向外分别为0，1，2……, 如果系统中不存在指定的轨道，返回-1。
	 * 
	 * @param radius 需要查找的轨道的半径/编号,半径/编号 > 0
	 * @return 轨道的索引，不存在返回-1
	 */
	int indexOf(double radius);
	
	/**
	 * 获得指定物体的初始角度，特别的，如果系统中所有物体都一样（比如电子），那么将会随机选取一个物体
	 * 的初始角度返回，如果物体不存在初始角度，则返回-1。
	 * 
	 * @param object  指定的物体
	 * @return 物体的初始角度，如果有多个相同物体，则随机选择，若物体没有初始角度，或者物体在系统中不存在则返回-1
	 */
	double getAngle(E object);
	
	/**
	 * 获得轨道数目，数目为当前在系统中所有轨道的数目
	 * @return 轨道数目
	 */
	int getTrackNum();
	
	/**
	 * 获得系统中所有轨道物体的数量
	 * @return 物体数量
	 */
	int getObjectNum();
	
	/**
	 * 获得指定轨道的半径，若轨道没有半径属性，返回-1
	 * @param index 指定轨道的索引，索引为 >= 0整数
	 * @return 指定轨道的半径，若轨道没有半径属性或指定轨道不存在， 返回-1
	 */
	double getRadius(int index);
	
	/**
	 * 获得指定轨道的自身的编号，若轨道没有编号属性，返回-1
	 * @param index 指定轨道的索引，索引为 >= 0整数
	 * @return 指定轨道的编号，若轨道没有编号属性或指定轨道不存在， 返回-1
	 */
	int getNumber(int index);
	
	/**
	 * 对轨道物体按照编号/半径进行升序排序，返回排序后轨道索引与半径/编号的映射关系。
	 * 排序后轨道顺序将发生变化，因此之前不同轨道对应的索引可能变化。
	 * @return
	 */
	Map<Integer, Double> sort();
	
	/**
	 * 对轨道系统进行克隆
	 * @return 克隆后的系统
	 */
	ConcreteCircularOrbit<L, E> clone();
}
