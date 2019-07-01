package circularOrbit;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import org.apache.log4j.Logger;

import constant.Regex;
import parser.Parser;
import track.Track;
import track.TrackFactory;

/**
 * CircularOrbit的具体实现类，{@code mutable} 类型
 *
 * @param <L> 中心物体类型
 * @param <E> 轨道物体类型
 */
public class ConcreteCircularOrbit<L,E> implements CircularOrbit<L,E>, Iterable<E>,Cloneable{
	private Logger logger = Logger.getLogger(ConcreteCircularOrbit.class);
	private final String name;  //轨道系统的名称，比如恒星系统，原子等
	private final TrackFactory tf; //轨道工厂类
	private  L centralpoint = null; //轨道系统中心物体 
	private final List<Track> tracks = new ArrayList<Track>(); //系统中所有的轨道
	private final Map<Track, List<E>> trackmap = new HashMap<Track, List<E>>(); //每条轨道上的物体
	private final Map<E, Position> position = new HashMap<E, Position>(); //每个物体对应的轨道和初始角度
	private final Set<E> centrals = new HashSet<E>(); //与中心点有关系的轨道物体集合
	private final List<Relation<E>> relations = new ArrayList<Relation<E>>(); //保存轨道物体之间的关系
	
	/*
	 * Abstract Function:
	 * 	AF(name, tf, centralpoint, tracks, trackmap, position, centrals, relations) = 一个由唯一一个中心点和多条轨道构成的系统，在中心点 L 
	 * 																				 和 轨道上承载多个物体 E，物体在轨道上可保持静止或持续 
	 * 																				运动，物体与物体之间可存在联系。 
	 * 
	 * Rep Invariant:
	 *	name			非空对象，非空字符串，符合label构造规则
	 *	tf				非空对象，为TrackFactory子类
	 *	centralpoint	可以为空对象
	 *	tracks			列表内无空对象
	 *	trackmap		映射内无空对象，不存在一个key对应为null
	 *					trackmap中关键字数量 <= tracks长度	
	 *	position		position内无空对象，position内关键字数量 = trackmap中所有轨道物体数量，即每一个轨道物体对应一个position，
	 *					若轨道物体相同，例如电子，允许position中只保存一个对象的position或map为空
	 *					(注：因为电子对象都是相同的，所以在放入map中时会顶替上一次放入的position)
	 *	centrals		集合内无空对象
	 *	relations 		列表内无空对象，无重复相同的对象
	 *
	 * Safety from exposure:
	 * 	所有域是private final的，对于外部输入mutable类型的方法，ConcreteCircularOrbit(),restore()等涉及到ListMap
	 * 	都做了防御性拷贝，新建对象重新保存引用
	 *	对于输出mutable类型的方法，getTracks(), getTrackmap()都做了防御性拷贝，客户端拿不到直接引用
	 *	Track作为轨道系统的内部类，外部无法拿到直接引用，通过轨道编号来操作对应轨道
	 *	Relation作为轨道系统内部类，为private class， 客户端无法拿到引用
	 */
	
	private void checkRep() {
		assert name != null && !name.equals("") : "CircularOrbit name is null object or \"\"";
		assert name.matches(Regex.REGEX_LABEL) : "CircularOrbit name should be label type";
		assert tf != null : "TrackFactory is null";
		for(Track t : tracks) assert t != null :"Track is null object";
		int objsnum = 0;
		for(List<E> e : trackmap.values()) {
			assert e != null : "The objects on Track is null object";
			objsnum += e.size();
		}
		assert trackmap.size() <= tracks.size() : "The number of track mapping is larger than the number of tracks";
		for(Position pos : position.values()) assert pos != null:"The object position is null object";
		assert position.size() == objsnum || position.size() == 1 || position.size() == 0 : "The number of object positions should equals"
				+ " the number of objects OR should be 1 or 0 if objects are same"+ ", but objects num was "+objsnum +" and position was "
				+ position.size();
		for(E e: centrals) assert e!= null : "Null object in centrals";
		for(int i = 0; i < relations.size()-1; i++) {
			assert relations.get(i) != null : "Null object in relations";
			for(int j = i+1 ; j < relations.size(); j++) {
				assert !relations.get(i).equals(relations.get(j)) : "Duplicate Orbit object relation";
			}
		}
	}
	
	
	public ConcreteCircularOrbit(String name, TrackFactory tf) {
		this.name = name;
		this.tf = tf;
		checkRep();
	}
	
	/**
	 * 输入另一个轨道系统对象，初始化一个轨道系统，这个轨道系统的所有参数与输入对象保持一致
	 * @param co  轨道系统对象
	 */
	public ConcreteCircularOrbit(ConcreteCircularOrbit<L,E> co) {
		if(co == null) {
			logger.error("["+NullPointerException.class.getName()+"] [Throw] Null pointer co");
			throw new NullPointerException();
		}
		this.name = co.name;
		this.tf = co.tf;
		this.centralpoint = co.centralpoint;
		this.tracks.addAll(new ArrayList<Track>(co.tracks)); 
		for(Map.Entry<Track, List<E>> e : co.trackmap.entrySet()) {
			List<E> newlist = new ArrayList<E>(e.getValue());
			this.trackmap.put(e.getKey(), newlist);
		}
		this.position.putAll(new HashMap<E,Position>(co.position));
		this.centrals.addAll(new HashSet<E>(co.centrals));
		this.relations.addAll(new ArrayList<Relation<E>>(co.relations));
		checkRep();
	}
	
	/**
	 * 获得中心物体
	 * @return 中心物体
	 */
	@Override
	public L getCentralPoint() {
		return centralpoint;
	}
	
	/**
	 * 获得系统内所有轨道
	 * @return 所有轨道列表
	 */
	protected List<Track> getTracks() {
		return new ArrayList<Track>(tracks); 
	}
	
	/**
	 * 获得系统内轨道与轨道上物体的映射关系
	 * @return 轨道与轨道物体的映射关系
	 */
	protected Map<Track, List<E>> getTrackMap() {
		Map<Track, List<E>> map = new HashMap<Track, List<E>>();
		for(Map.Entry<Track, List<E>> e : trackmap.entrySet()) {
			List<E> newList = new ArrayList<E>(e.getValue());
			map.put(e.getKey(), newList);
		}
		return map;
	}
	
	/**
	 * Get the relation list
	 * @return raltion list
	 */
	protected List<Relation<E>> getRelation() {
		List<Relation<E>> newrl = new ArrayList<Relation<E>>(relations);
		return newrl;
	}
	
	/**
	 * Get central point
	 * @return central point
	 */
	protected Set<E> getCentrals() {
		Set<E> news = new HashSet<E>(centrals);
		return news;
	}
	
	/**
	 * Get Position map
	 * @return position map
	 */
	protected Map<E,Position> getPosition() {
		Map<E, Position> map = new HashMap<E, Position>(position);
		return map;
	}
	
	/**
	 * 将系统中的轨道集合恢复到
	 * @param l
	 * TODO 将恢复方法修改为参数只有co一个对象
	 */
	protected void restoreTracks(List<Track> l) {
		tracks.clear();
		tracks.addAll(new ArrayList<Track>(l));
	}
	
	/**
	 * Restore the trackmap
	 * @param map new trackmap
	 */
	protected void restoreMap(Map<Track, List<E>> map) {
		Map<Track, List<E>> newmap = new HashMap<Track, List<E>>();
		trackmap.clear();
		for(Map.Entry<Track, List<E>> e : map.entrySet()) {
			List<E> newList = new ArrayList<E>(e.getValue());
			newmap.put(e.getKey(), newList);
		}
		trackmap.putAll(newmap);
	}
	
	/**
	 * Restore the relation
	 * @param rl new relation
	 */
	protected void restoreRelation(List<Relation<E>> rl) {
		List<Relation<E>> newrl = new ArrayList<Relation<E>>(rl);
		relations.clear();
		relations.addAll(newrl);
	}
	
	/**
	 * Restore the Central point
	 * @param cen new centralpoint
	 */
	protected void restoreCentrals(Set<E> cen) {
		Set<E> news = new HashSet<E>(centrals);
		centrals.clear();
		centrals.addAll(news);
	}
	
	/**
	 * Restore the position
	 * @param map new position
	 */
	protected void restorePosition(Map<E, Position> map) {
		position.clear();
		Map<E, Position> newmap = new HashMap<E, Position>(map);
		position.putAll(newmap);
	}
	
	/**
	 * 将轨道系统恢复到某一个轨道系统状态
	 * @param co
	 */
	public void restore(ConcreteCircularOrbit<L,E> co) {
		if(co == null) {
			logger.error("["+NullPointerException.class.getName()+"] [Throw] Null pointer co");
			throw new NullPointerException();
		}
		centralpoint = co.centralpoint;
		tracks.clear();
		trackmap.clear();
		position.clear();
		centrals.clear();
		relations.clear();
		this.tracks.addAll(new ArrayList<Track>(co.tracks));
		this.trackmap.putAll(new HashMap<Track, List<E>>(co.trackmap));
		this.position.putAll(new HashMap<E,Position>(co.position));
		this.centrals.addAll(new HashSet<E>(co.centrals));
		this.relations.addAll(new ArrayList<Relation<E>>(co.relations));
		checkRep();
	}
	
	
	/**
	 * 获得轨道系统的名称
	 * @return 轨道系统的名称
	 */
	public String getName() {
		return name;
	}
	
	@Override
	public int addTrack(double radius) {
		Track t = tf.create(radius);
		if(tracks.contains(t)) return -1;
		tracks.add(t);
		return tracks.indexOf(t);
	}

	@Override
	public boolean remove(int index) {
		if(index < 0) {
			logger.error("["+IllegalArgumentException.class.getName()+"] [Throw] Track index should >= 0");
			throw new IllegalArgumentException("Track index should >= 0");
		}
		if(index >= tracks.size()) return false;
		Track f = tracks.remove(index);
		if(f==null) return false;
		
		//若Track移除成功，则要将track中物体 轨道物体位置，中心关系，物体之间关系 全部移除
		List<E> objects = trackmap.remove(f); //Remove mapping
		if(objects != null) {
			for(E e : objects) {  //For all objects on specify track
				centrals.remove(e); //Remove central relation
				for(Relation<E> r : relations) {
					if(r.contains(e)) relations.remove(r); //Remove Orbit relation
				}
				position.remove(e); //Remove position
			}
			checkRep();
			return true;
		}
		return false;
	}

	@Override
	public L addCentralPoint(L centralPoint) {
		if(centralPoint == null) {
			logger.error("["+NullPointerException.class.getName()+"] [Throw] CentralPoint is null object");
			throw new NullPointerException("CentralPoint is null object");
		}
		L old = centralpoint;
		centralpoint = centralPoint;
		return old;
	}
	

	@Override
	public boolean addObject(E object, int index) {
		if(object == null) {
			logger.error("["+NullPointerException.class.getName()+"] [Throw] OrbitObject is null object");
			throw new NullPointerException("OrbitObject is null object");
		}
		if(index < 0) { 
			logger.error("["+IllegalArgumentException.class.getName()+"] [Throw] Track index should >= 0");
			throw new IllegalArgumentException("Track index should >= 0");
		}
		if(index >= tracks.size()) return false;
		
		Track t = tracks.get(index);
		List<E> objs = trackmap.get(t);
		if(objs == null) { 	//If there is no object on track before.
			List<E> newobj = new ArrayList<E>();
			newobj.add(object);
			trackmap.put(t, newobj);
		}
		else {
			objs.add(object); //Add new object
			trackmap.put(t, objs); //Update
		}
		
		//Update position
		Position pos = new Position(t, -1);
		/*
		 * BE CAREFUL : 
		 * If all objects are same, like electron and so on, position will only be
		 * saved once.
		 */
		position.put(object, pos); 
		checkRep();
		return true;
	}

	@Override
	public boolean addObject(E object, int index, double angle) {
		if(object == null) { 
			logger.error("["+NullPointerException.class.getName()+"] [Throw] OrbitObject is null object");
			throw new NullPointerException("OrbitObject is null object");
		}
		if(index < 0) {
			logger.error("["+IllegalArgumentException.class.getName()+"] [Throw] Track index should >= 0");
			throw new IllegalArgumentException("Track index should >= 0");
		}
		if(angle < 0) { 
			logger.error("["+IllegalArgumentException.class.getName()+"] [Throw] Object angle should >= 0");
			throw new IllegalArgumentException("Object angle should >= 0");
		}
		if(angle > 360) { 
			logger.error("["+IllegalArgumentException.class.getName()+"] [Throw] Object angle should < 360");
			throw new IllegalArgumentException("Object angle should < 360");
		}
		
		if(index >= tracks.size()) return false;
		
		Track t = tracks.get(index);
		List<E> objs = trackmap.get(t);
		if(objs == null) { 	//If there is no object on track before.
			List<E> newobj = new ArrayList<E>();
			newobj.add(object);
			trackmap.put(t, newobj);
		}
		else {
			objs.add(object); //Add new object
			trackmap.put(t, objs); //Update
		}
		
		//Update position
		Position pos = new Position(t, angle);
		/*
		 * BE CAREFUL : 
		 * If all objects are same, like electron and so on, position will only be
		 * saved once.
		 */
		position.put(object, pos); 
		checkRep();
		return true;
	}

	@Override
	public List<E> getObjects(int index) {
		assert index >= 0:"Track index should >= 0";
		if(index >= tracks.size()) return null;
		
		Track t = tracks.get(index);
		List<E> objs = trackmap.get(t);
		if(objs == null) {  //If there is no object on track
			objs = new ArrayList<E>();
		}
		
		return objs;
	}

	@Override
	public boolean remove(E object) {
		if(object == null) { 
			logger.error("["+NullPointerException.class.getName()+"] [Throw] Remove object is null object");
			throw new NullPointerException("Remove object is null object");
		}
		
		List<E> objs;
		for(Track t : tracks) {
			if((objs = trackmap.get(t))!=null) {
				if(objs.contains(object)) {
					//The track most close to center 
					double angle = 360;
					int index = 0;
					
					//Find the object with minimum angle on track
					for(int i = 0; i < objs.size(); i++) {
						if(objs.get(i).equals(object)) {
							if(position.get(objs.get(i)).getAngle() < angle) {
								angle = position.get(objs.get(i)).getAngle();
								index = i;
							}
						}
					}
					
					//Remove this object and remove it's position, centrals, relation
					E e = objs.remove(index);
					trackmap.put(t, objs);
					position.remove(e);
					centrals.remove(e);
					List<Relation<E>> rl = new ArrayList<Relation<E>>();
					for(Relation<E> r : relations) {
						if(r.contains(e)) 
							rl.add(r);
					}
					relations.removeAll(rl); //Remove Orbit relation
					checkRep();
					return true;
				}
			}
			else {
				continue;
			}
		}
		return false;
	}

	@Override
	public boolean remove(E object, int index) {
		if(object == null) { 
			logger.error("["+NullPointerException.class.getName()+"] [Throw] RemoveObject is null object");
			throw new NullPointerException("OrbitObject is null object");
		}
		if(index < 0) {
			logger.error("["+IllegalArgumentException.class.getName()+"] [Throw] Track index should >= 0");
			throw new IllegalArgumentException("Track index should >= 0");
		}
		if(index >= tracks.size()) return false;
		
		Track t = tracks.get(index);
		List<E> objs = trackmap.get(t);
		if(objs == null) return false;  //Specified track has no objects
		if(!objs.contains(object)) return false; //Track has no specified object
		
		double angle = 360;
		int in = 0;
		//Find the object with minimum angle on track
		for(int i = 0; i < objs.size(); i++) {
			if(objs.get(i).equals(object)) {
				if(position.get(objs.get(i)) != null) {
					if(position.get(objs.get(i)).getAngle() < angle) {
						angle = position.get(objs.get(i)).getAngle();
						in = i;
					}
					
				}
			}
		}
		//Remove this object and remove it's position, centrals, relation
		E e = objs.remove(in);
		trackmap.put(t, objs);
		position.remove(e);
		centrals.remove(e);
		for(Relation<E> r : relations) {
			if(r.contains(e)) relations.remove(r); //Remove Orbit relation
		}
		checkRep();
		return true;
	}

	@Override
	public boolean addCentralRelation(E orbitObject) {
		if(orbitObject == null) { 
			logger.error("["+NullPointerException.class.getName()+"] [Throw] OrbitObject is null object");
			throw new NullPointerException("OrbitObject is null object");
		}
		if(centralpoint == null) return false;
		if(!contains(orbitObject)) return false;
		return centrals.add(orbitObject);
	}

	@Override
	public boolean removeCentralRelation(E orbitObject) {
		if(orbitObject == null) { 
			logger.error("["+NullPointerException.class.getName()+"] [Throw] OrbitObject is null object");
			throw new NullPointerException("OrbitObject is null object");
		}
		
		return centrals.remove(orbitObject);
	}

	@Override
	public boolean addOrbitRelation(E object1, E object2) {
		if(object1 == null) { 
			logger.error("["+NullPointerException.class.getName()+"] [Throw] Object is null object");
			throw new NullPointerException("Object is null object");
		}
		if(object2 == null) { 
			logger.error("["+NullPointerException.class.getName()+"] [Throw] OrbitObject is null object");
			throw new NullPointerException("OrbitObject is null object");
		}
		
		if(!contains(object1) || !contains(object2)) return false;
		Relation<E> rel = new Relation<E>(object1, object2);
		if(relations.contains(rel)) return false;
		relations.add(rel);
		return true;
	}

	@Override
	public boolean removeOrbitRelation(E object1, E object2) {
		if(object1 == null) { 
			logger.error("["+NullPointerException.class.getName()+"] [Throw] Object is null object");
			throw new NullPointerException("Object is null object");
		}
		if(object2 == null) { 
			logger.error("["+NullPointerException.class.getName()+"] [Throw] OrbitObject is null object");
			throw new NullPointerException("OrbitObject is null object");
		}
		
		Relation<E> rel = new Relation<E>(object1, object2);
		if(!relations.contains(rel)) return false;
		
		return relations.remove(rel);
	}

	@Override
	public Set<E> centrals() {
		return new HashSet<E>(centrals);
	}

	@Override
	public Set<E> relatives(E object) {
		Set<E> relatives = new HashSet<E>();
		for(Relation<E> r : relations) {
			if(r.contains(object)) relatives.add(r.getOther(object));
		}
		return relatives;
	}

	@Override
	public CircularOrbit<L, E> buildCircularObject(File file, Parser<?, ?> parser) {
		return null;
	}

	@Override
	public int transit(E object, int newIndex) {
		if(object == null) { 
			logger.error("["+NullPointerException.class.getName()+"] [Throw] Object transited is null object");
			throw new NullPointerException("Object transited is null object");
		}
		if(newIndex < 0) {
			logger.error("["+IllegalArgumentException.class.getName()+"] [Throw] Track new Index should >= 0");
			throw new IllegalArgumentException("Track new index should >= 0");
		}
		if(newIndex >= tracks.size()) return -1;
		
		for(int i = 0; i < tracks.size(); i++) {
			Track t = tracks.get(i);
			List<E> objs = trackmap.get(t);
			if(objs != null && objs.contains(object)) {
				//If track contains the object, move the object to the new track and modify the position
				objs.remove(object);
				trackmap.put(t, objs); 
				Track newtrack = tracks.get(newIndex);
				List<E> newobjs = trackmap.get(newtrack);
				if(newobjs == null) {
					newobjs = new ArrayList<E>();
					newobjs.add(object);
				}
				else {
					newobjs.add(object);
				}
				trackmap.put(newtrack, newobjs); //Update mapping
				Position pos = new Position(newtrack, position.get(object).getAngle());
				position.put(object, pos); //Update position
				checkRep();
				logger.info("Transit "+object.getClass().getSimpleName()+" to track "+newIndex);
				return i; //Return old track index
			}
		}
		return -1;
	}
	
	@Override
	public boolean transit(E object,int oldIndex, int newIndex) {
		if(object == null) { 
			logger.error("["+NullPointerException.class.getName()+"] [Throw] Object transited is null object");
			throw new NullPointerException("Object transited is null object");
		}
		if(newIndex < 0) {
			logger.error("["+IllegalArgumentException.class.getName()+"] [Throw] New index of track should >= 0");
			throw new IllegalArgumentException("New index of track should >= 0");
		}
		if(oldIndex < 0) {
			logger.error("["+IllegalArgumentException.class.getName()+"] [Throw] Old index of track should >= 0");
			throw new IllegalArgumentException("Old index of track should >= 0");
		}
		if(oldIndex >= tracks.size() || newIndex >= tracks.size()) return false;
		if(oldIndex == newIndex) return false;
		
		Track old = tracks.get(oldIndex);
		Track newt = tracks.get(newIndex);
		if(trackmap.get(old) == null || !trackmap.get(old).contains(object)) return false; //If old track has no such object
		List<E> objs = trackmap.get(old);
		objs.remove(object);
		trackmap.put(old, objs);
		List<E> newobjs = trackmap.get(newt);
		if(newobjs == null) {
			newobjs = new ArrayList<E>();
			newobjs.add(object);
		}
		else {
			newobjs.add(object);
		}
		trackmap.put(newt, newobjs); //Update mapping
		
		Position pos = new Position(newt, position.get(object).getAngle());
		position.put(object, pos); //update position
		checkRep();
		logger.info("Transit one "+object.getClass().getSimpleName()+"from track "+oldIndex+" to track "+newIndex);
		return true;
	}

	@Override
	public boolean transit(int oldIndex, int newIndex) {
		if(newIndex < 0) {
			logger.error("["+IllegalArgumentException.class.getName()+"] [Throw] New index of track should >= 0");
			throw new IllegalArgumentException("New index of track should >= 0");
		}
		if(oldIndex < 0) {
			logger.error("["+IllegalArgumentException.class.getName()+"] [Throw] Old index of track should >= 0");
			throw new IllegalArgumentException("Old index of track should >= 0");
		}
		if(oldIndex >= tracks.size() || newIndex >= tracks.size()) return false;
		if(oldIndex == newIndex) return false;
		
		Track old = tracks.get(oldIndex);
		Track newt = tracks.get(newIndex);
		if(trackmap.get(old) == null) return false; //If old track has no object
		List<E> objs = trackmap.get(old);
		if(objs.isEmpty()) return false;
		E object = objs.get(0);
		objs.remove(object);
		trackmap.put(old, objs);
		List<E> newobjs = trackmap.get(newt);
		if(newobjs == null) {
			newobjs = new ArrayList<E>();
			newobjs.add(object);
		}
		else {
			newobjs.add(object);
		}
		trackmap.put(newt, newobjs); //Update mapping
		
		Position pos = new Position(newt, position.get(object).getAngle());
		position.put(object, pos); //update position
		checkRep();
		logger.info("Transit one "+object.getClass().getSimpleName()+"from track "+oldIndex+" to track "+newIndex);
		return true;
	}
	
	@Override
	public java.util.Iterator<E> iterator() {
		return new OrbitIterator();
	}
	
	/**
	 * 内部类，生成迭代器，遍历轨道系统内所有的轨道物体，顺序为顺序为从内轨道逐步向外，
	 * 同一轨道物体按角度从小到大次序（如果没有角度就随机）
	 *
	 */
	private class OrbitIterator implements Iterator<E> {
		List<E> allSortedObject = new ArrayList<E>();
		int i = -1;
		
		public OrbitIterator() {
			//Sort the object by the angle, from 0 to 360
			//code comes from https://www.cnblogs.com/zhujiabin/p/6164826.html
			Map<E, Position> sortedMap = new LinkedHashMap<E, Position>();
		    List<Map.Entry<E, Position>> entryList = new ArrayList<Map.Entry<E, Position>>(position.entrySet());
		    Collections.sort(entryList, new MapValueComparator<E>());

		    Iterator<Entry<E, Position>> iter = entryList.iterator();
		    Map.Entry<E, Position> tmpEntry = null;
		    while (iter.hasNext()) {
		        tmpEntry = iter.next();
		        sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue()); //add sorted object in map
		    }
		    
		    /*
		     * From inside track to outside track, for each track, traverse the sortedMap check if key object
		     * is on the track, if it's true, add the object to the allSortedObject.
		     * So that all the object on each track will be sorted by the angle.
		     */
		    sort();
			for(Track t : tracks) {
				for(Map.Entry<E, Position> e: sortedMap.entrySet()) {
					if(e.getValue().getTrack().equals(t)) {
						allSortedObject.add(e.getKey());
					}
				}
			}
		}
		
		@Override
		public boolean hasNext() {
			if(i < allSortedObject.size()-1) {
				i++;
				return true;
			}
			return false;
		}

		@Override
		public E next() {
			return allSortedObject.get(i);
		}
		
	}


	@Override
	public boolean contains(E object) {
		if(object == null) { 
			logger.error("["+NullPointerException.class.getName()+"] [Throw] Object transited is null object");
			throw new NullPointerException("Object transited is null object");
		}
		
		for(Track t : tracks) {
			List<E> objs = trackmap.get(t);
			if(objs != null) {
				for(E e : objs) {
					if (e != null && e.equals(object)) return true;
				}
				
			}
		}
		return false;
	}

	@Override
	public boolean contains(E object, int index) {
		if(index < 0) {
			logger.error("["+IllegalArgumentException.class.getName()+"] [Throw] Index of track should >= 0");
			throw new IllegalArgumentException("Index of track should >= 0");
		}
		if(object == null) { 
			logger.error("["+NullPointerException.class.getName()+"] [Throw] Object transited is null object");
			throw new NullPointerException("Object transited is null object");
		}
		if(index >= tracks.size()) return false;
		
		Track t = tracks.get(index);
		return trackmap.get(t).contains(object);
	}

	@Override
	public boolean contains(double radius) {
		for(Track t : tracks) {
			if(t.getNumber() == -1) {
				if(t.getRadius() == radius) return true;
			}else {
				if(t.getNumber() == (int)radius) return true;
			}
		}
		return false;
	}

	@Override
	public int indexOf(E object) {
		for(int i = 0; i < tracks.size(); i++) {
			if(trackmap.get(tracks.get(i)).contains(object)) return i;
		}
		return -1;
	}
	
	@Override
	public int indexOf(double radius) {
		if(radius <= 0) { 
			logger.error("["+IllegalArgumentException.class.getName()+"] [Throw] Radius <= 0, was " + radius);
			throw new IllegalArgumentException("Radius <= 0, was " + radius);
		}
		for(int i = 0; i < tracks.size(); i++) {
			if(tracks.get(i).getRadius()==radius || tracks.get(i).getNumber() == radius) return i;
		}
		return -1;
	}

	@Override
	public double getAngle(E object) {
		if(object == null) { 
			logger.error("["+NullPointerException.class.getName()+"] [Throw] Object transited is null object");
			throw new NullPointerException("Object transited is null object");
		}
		
		if(position.containsKey(object)) return position.get(object).getAngle();
		return -1;
	}

	@Override
	public int getTrackNum() {
		return tracks.size();
	}
	
	@Override
	public int getObjectNum() {
		int c = 0; 
		for(Map.Entry<Track, List<E>> e : trackmap.entrySet()) {
			c += e.getValue().size();
		}
		return c;
	}
	
	@Override
	public double getRadius(int index) {
		if(index < 0) {
			logger.error("["+IllegalArgumentException.class.getName()+"] [Throw] Index of track should >= 0");
			throw new IllegalArgumentException("Index of track should >= 0");
		}
		
		if(index >= tracks.size()) return -1;
		Track t = tracks.get(index);
		return t.getRadius();
	}
	
	@Override
	public int getNumber(int index) {
		if(index < 0) {
			logger.error("["+IllegalArgumentException.class.getName()+"] [Throw] Index of track should >= 0");
			throw new IllegalArgumentException("Index of track should >= 0");
		}
		
		if(index >= tracks.size()) return -1;
		Track t = tracks.get(index);
		return t.getNumber();
	}
	
	@Override
	public String toString() {
		return "[CircularOrbit: Name:"+name+", Number of tracks:"+tracks.size()+", CentralPoint:"+centralpoint.getClass().getSimpleName();
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(name, tracks, trackmap, position);
	}

	@Override
	public Map<Integer, Double> sort() {
		Collections.sort(tracks, new Comparator<Track>() {
			@Override
			public int compare(Track t1, Track t2) {
				if(t1.getNumber() == -1) {
					double result = t1.getRadius() - t2.getRadius();
					if(result > 1e-7) return 1;
					else if(result < -1e-7) return -1;
					else return 0;
				}
				else {
					return t1.getNumber()-t2.getNumber();
				}
			}
		});
		Map<Integer, Double> index = new HashMap<Integer, Double>();
		int i = 0;
		for(Track t : tracks) {
			index.put(i, t.getNumber() == -1?t.getRadius():t.getNumber());
			i++;
		}
		return index;
	}
	
	@Override
	public ConcreteCircularOrbit<L,E> clone() {
		try {
			super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return new ConcreteCircularOrbit<L,E>(this); 
	}
	
	
}

/**
 * 表示轨道物体的位置，一个轨道物体的位置包括其所属的轨道（其中就含有了半径这个属性）
 * 和初始角度（如果轨道物体存在这个属性）。某些应用中，位置表示物体在轨道上的绝对位置。
 * 在某些应用中，位置表示物体所处的轨道。
 *
 */
class Position {
	private final Track track;
	private final double angle;
	
	/*
	 * Abstract Function
	 * 	AF(track, angle) = 一个轨道上物体的位置
	 * 
	 * Representation invariant
	 * 	track 轨道不为空且轨道在系统中存在
	 * 	angle angle等于-1 或者 0 <= angle < 360
	 * 
	 * Safety from rep exposure
	 * 	所有的rep都是private final的，不提供mutator
	 */
	
	private void checkRep() {
		assert track != null:"track为null";
		assert (angle == -1) || (0 <= angle && angle < 360): "angle不合法";
	}
	
	/**
	 * 初始化一个位置对象，包含轨道和初始角度，如果物体在轨道上没有初始角度，则角度设置为-1，
	 * 否则，一个物体的初始角度为极坐标中的角度，其范围为 0 <= angle < 360
	 * @param track 物体所属轨道对象，要求轨道不为空对象
	 * @param angle 物体的初始角度，范围为 0 <= angle < 360，若物体没有角度属性，设置为-1
	 */
	public Position(Track track, double angle) {
		this.track = track;
		this.angle = angle;
		checkRep();
	}
	
	/**
	 * 获取所在轨道，要求Track不为空
	 * @return 所在的轨道
	 */
	public Track getTrack() {
		return track;
	}
	
	/**
	 * 获取物体所处角度，该角度为极坐标下角度，
	 * 使用弧度制，0 <= angle < 360 或 -1
	 * 
	 * @return 物体的初始角度
	 */
	public double getAngle() {
		return angle;
	}
	
	/**
	 * 将一个对象与当前对象比较，若其初始角度和轨道相同，则判定为两个对象相等
	 */
	@Override 
	public boolean equals(Object otherobject) {
		if(this == otherobject) return true;
		if(otherobject == null) return false;
		if(getClass() != otherobject.getClass()) return false;
		
		Position other = (Position)otherobject;
		
		return this.track.equals(other.getTrack()) && this.angle == other.angle;
	}
	
	/**
	 * 输出描述位置的字符串
	 */
	@Override
	public String toString() {
		return "[Position  Track:"+track.toString()+", Angle:"+angle+"]";
	}
	
	/**
	 * 获得位置哈希码
	 */
	@Override
	public int hashCode() {
		return Objects.hash(track, angle);
	}
}

/**
 * 根据轨道物体对象的初始角度对position映射进行升序排序
 *
 * @param <E> 轨道物体类型
 */
class MapValueComparator<E> implements Comparator<Map.Entry<E, Position>>, Serializable {

	/**
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

