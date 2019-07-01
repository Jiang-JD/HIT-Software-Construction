package applications.tools;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import centralObject.Nucleus;
import physicalObject.Electron;
import track.Track;

/**
 * 电子跃迁记录备忘录条目类，存储一次跃迁前的轨道系统状态
 * 包括轨道信息，中心点，电子数量和所处轨道
 *
 */
public class Memento {
	private final List<Track> tracks;
	private final Map<Track,List<Electron>> trackmap;
	private final Nucleus cp;
	private final LocalDateTime time; 
	
	/*
	 * AF
	 * 	AF(tracks,trackmap, cp,time) = 一个记录了电子跃迁前原子轨道系统状态的备忘条目
	 * 
	 * RI
	 * 	tracks 		保存的轨道不为空
	 * 	trackmap	保存对象不为空，其中每个对应关系也不为空
	 * 	cp			保存对象不为空
	 * 	time		保存对象不为空
	 * 
	 * Safety from exposure:
	 * 	所有域都是private final的，构造器对与外界的list或mutable变量都做了防御性拷贝，防止拿到内部引用
	 */
	
	private void checkRep() {
		for(Track t: tracks) {
			assert t != null:"Memento null pointer in tracks";
		}
		for(List<Electron> l : trackmap.values()) {
			for(Electron e: l) {
				assert e != null : "Memento Electron is null pointer";
			}
		}
		assert cp != null : "Nucleus is null pointer";
	}
	
	/**
	 * 初始化一个原子结构备忘录条目
	 * @param tracks 轨道列表，要求列表非空其中轨道对象非空
	 * @param trackmap 轨道和电子映射关系，要求映射关系非空，电子列表中对象非空
	 * @param cp 中心原子核，要求非空
	 */
	public Memento(List<Track> tracks ,Map<Track,List<Electron>> trackmap ,Nucleus cp) {
		for(Track t: tracks) {
			if( t == null) {
				throw new NullPointerException("Memento null pointer in tracks");
			}
		}
		for(List<Electron> l : trackmap.values()) {
			for(Electron e: l) {
				if(e == null) { 
					throw new NullPointerException("Memento Electron is null pointer");
				}
			}
		}
		if(cp == null) throw new NullPointerException("Nucleus is null pointer");
		this.tracks = new ArrayList<Track>();
		this.tracks.addAll(tracks);
		this.trackmap = new HashMap<Track,List<Electron>>();
		this.trackmap.putAll(trackmap);
		this.cp = cp;
		time = LocalDateTime.now();
		checkRep();
	}
	
	/**
	 * 获得原子结构所有轨道
	 * @return 原子结构所有轨道
	 */
	public List<Track> getTracks() {
		return new ArrayList<Track>(tracks);
	}
	
	/**
	 * 获得原子结构的轨道与电子映射关系
	 * @return 映射关系
	 */
	public Map<Track,List<Electron>> getMap() {
		return new HashMap<Track,List<Electron>>(trackmap);
	}
	
	/**
	 * 获得原子结构的中心原子核
	 * @return 中心原子核
	 */
	public Nucleus getNucleus() {
		return cp;
	}
	
	/**
	 * 获得备忘录记录时间
	 * @return 备忘录记录时间
	 */
	public LocalDateTime getTime() {
		return time;
	}
	
	/**
	 * 将其他备忘录条目与当前备忘录条目进行比较，如果轨道集合，中心物体，映射关系和记录时间
	 * 都相同，则判断两个备忘录条目相同
	 */
	@Override 
	public boolean equals(Object otherobject) {
		if(this == otherobject) return true;
		if(otherobject == null) return false;
		if(getClass() != otherobject.getClass()) return false;
		
		Memento other = (Memento)otherobject;
		
		return cp.equals(other.getNucleus()) && tracks.equals(other.tracks)
				&& trackmap.equals(other.trackmap) && time.equals(other.time);
	}
	
	/**
	 * 生成描述备忘录条目的字符串
	 */
	@Override
	public String toString() {
		return "[Record  Time:"+time+", CentralPoint:"+cp+", Tracks:"+tracks+", ElectronMap:"+trackmap+"]";
		
	}
	
	/**
	 * 获得备忘录条目的哈希码
	 */
	@Override
	public int hashCode() {
		return Objects.hash(time, tracks, trackmap, cp);
	}
	
	
}
