package circularOrbit;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import applications.tools.AppInstallTime;
import applications.tools.Timespan;
import centralObject.Person;
import constant.Regex;
import physicalObject.App;
import track.AppTrackFactory;

/**
 * 这是一个{@code mutable} 类型，表示一个 {@code 个人App生态系统}。用户日常使用手机上的 各个 App，根据各个 
 * App 的使用时间长度和频度，可以计算出每个 App 与该用户的“亲密度”。根据亲密度的不同，可以构造以该
 * 用户为中心的 多级轨道结构，称之为“个人 App 生态系统”：围绕用户有多条轨道，与 用户亲密度最高的若干 
 * App 在最内层轨道，越往外的轨道上的 App 与用 户的亲密度越低。 轨道系统固定5条轨道，从里向外亲密度逐渐
 * 变低。App的亲密度的计算方法为：取当前轨道系统中的
 *
 */
public class PersonalAppEcosystem extends ConcreteCircularOrbit<Person, App>  implements Cloneable{
	private final Timespan duration;
	private  Map<String, AppInstallTime> installing = new HashMap<String, AppInstallTime>();
	Logger logger = Logger.getLogger(PersonalAppEcosystem.class); 
	
	/*
	 * Abstract Function:
	 * 	  AF(centralpoint, tracks, trackmap, relation) = 一个个人App生态系统
	 * 
	 * Rep Invariant:
	 * 	  centralpoint:		不为空Person对象的名称符合Label格式
	 * 	  tracks：			共有5条track
	 * 	  trackmap:			每条轨道上的对象在系统出现仅一次
	 * 	  relation:			App自身不产生关系
	 * 	  duration:			duration非空
	 * 	  installing:		每一个系统内的app都要有对应的安装周期
	 * 	   
	 * 	 如果某个应用在某个时间段内被安装或被卸 载，那么它应该出现在该时间段的轨道
	 * 	 系统中；如果某个应用在某个时间段之前已被卸载，则不应出现在该时间段的轨道
	 *    系统中。
	 *   
	 * Safety from exposure:
	 * 	  所有域是private final的，所有Observer返回类型均是 immutable的。
	 * 	 客户端无法拿到内部引用。
	 */
	
	private void checkRep() {
		if(getCentralPoint() != null) 
			assert getCentralPoint().getName().matches(Regex.REGEX_LABEL) : "Person name "+ getCentralPoint().getName()
																			+ " is not label format";
		Iterator<App> it = iterator();
		while(it.hasNext()) {
			App a = it.next();
			assert !relatives(a).contains(a) : "App "+ a.getName()+" has a relationship with itself";
			assert installing.containsKey(a.getName()) : "App " + a.getName() + " has no installing log";
			assert installing.get(a.getName()).overlap(duration) : "App " + a.getName()+" is uninstalling during this timespan";
		}
		assert duration != null : "Duration is null pointer";
	}
	
	public PersonalAppEcosystem(String name, Timespan duration) {
		super(name, new AppTrackFactory());
		this.duration = duration;
		logger.info("Build a new PersonalAppEcosystem "+this.getName());
		checkRep();
	}
	
	/**
	 * 将一个app的名称和它的安装时间段记录加入系统中，功能类似于Map
	 * @param app app的名称，app在系统中应存在,名称非空
	 * @param at 对应的安装时间段，对象非空
	 * @return 如果添加成功，返回true，否则如果指定app的名称在系统中不存在，返回false
	 */
	public boolean addInstalling(String appName, AppInstallTime at) {
		if(appName == null) { 
			logger.error("AppEcosystem appName is null pointer");
			throw new NullPointerException("AppEcosystem appName is null pointer");
		}
		if(at == null) { 
			logger.error("AppUsageTime is null pointer");
			throw new NullPointerException("AppUsageTime is null pointer");
		}
		
		if(getApp(appName) == null) return false;
		installing.put(appName, at);
		checkRep();
		return true;
	}
	
	/**
	 * 获得指定名称App的App安装时间
	 * 
	 * @param name 指定查找的App的名称,name非空
	 * @return 如果在系统中找到指定App，返回该App安装时间，否则若系统不存在指定App，返回null
	 */
	public AppInstallTime getInstalling(String name) {
		if(name == null) { 
			logger.error("AppEcosystem App name is null pointer");
			throw new NullPointerException("AppEcosystem App name is null pointer");
		}
		if(name.equals("")) return null;
		
		if(installing.containsKey(name)) {
			return installing.get(name);
		}
		return null;
	}
	
	/**
	 * 获得指定名称App的App对象
	 * 
	 * @param name 指定查找的App的名称,name非空
	 * @return 如果在系统中找到指定App，返回该App对象，否则若系统不存在指定App，返回null
	 */
	public App getApp(String name) {
		if(name == null) { 
			logger.error("AppEcosystem App name is null pointer");
			throw new NullPointerException("AppEcosystem App name is null pointer");
		}
		if(name.equals("")) return null;
		
		for(int i = 0; i < getTrackNum(); i++) {
			for(App a : getObjects(i)) {
				if(a.getName().equals(name)) {
					return a;
				}
			}
		}
		return null;
	}
	
	/**
	 * 获得该生态系统所处的时间段
	 * @return 生态系统表示的时间段
	 */
	public Timespan duration() {
		return duration;
	}
	
	/**
	 * 向指定轨道添加App，track在轨道系统中必须存在。
	 * 
	 * @param app   需要添加的app，系统中不存在
	 * @param index 需要将物体添加到的轨道的编号，轨道编号 >= 0
	 * @param at    需要添加app的安装时间段
	 * @return      如果添加成功，返回true，否则返回false
	 */
	public boolean addApp(App app, int index, AppInstallTime at) {
		if(at == null) { 
			logger.error("AppUsageTime is null pointer");
			throw new NullPointerException("AppUsageTime is null pointer");
		}
		if(!contains(app) && super.addObject(app, index)) {
			installing.put(app.getName(), at);
			checkRep();
			logger.info("Add a new App "+app.getName()+" to the "+this.getName());
			return true;
		}
		return false;
	}
	
	
	@Override
	public PersonalAppEcosystem clone() {
		super.clone();
		PersonalAppEcosystem copy = new PersonalAppEcosystem(getName(),duration());
		copy.addCentralPoint(this.getCentralPoint());
		copy.restoreTracks(this.getTracks());
		copy.restoreMap(this.getTrackMap());
		copy.restorePosition(this.getPosition());
		copy.restoreRelation(this.getRelation());
		copy.restoreCentrals(this.getCentrals());
		Map<String, AppInstallTime> newinstalling = new HashMap<String, AppInstallTime>();
		for(Map.Entry<String, AppInstallTime> e : installing.entrySet()) {
			AppInstallTime newins = e.getValue().clone();
			newinstalling.put(e.getKey(), newins);
		}
		copy.installing = newinstalling;
		return copy;
	}
	
	@Override
	public boolean addObject(App app, int index) {
		if(app == null) { 
			logger.error("App is null pointer");
			throw new NullPointerException("App is null pointer");
		}
		if(!contains(app) && super.addObject(app, index)) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean remove(App app, int index) {
		if(super.remove(app, index)) {
			installing.remove(app.getName());
			checkRep();
			return true;
		}
		return false;
	}
	
	@Override
	public boolean remove(App app) {
		if(super.remove(app)) {
			installing.remove(app.getName());
			checkRep();
			return true;
		}
		return false;
	}
	
	@Override
	public boolean remove(int index) {
		List<App> objs = getObjects(index);
		if(super.remove(index)) {
			if(objs != null) {
				for(App a: objs) {
					installing.remove(a.getName());
				}
			}
			checkRep();
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("[PersonalAppEcosystem:"+getName()+", Duration:"+duration.toString()+", TrackNumber:"+getTrackNum()+"]\n");
		sb.append("[Apps:");
		for(int i = 0; i < getTrackNum(); i++) {
			sb.append("[Track"+(i+1)+": "+getObjects(i).size()+"]");
			sb.append("[");
			for(App a: getObjects(i)) {
				sb.append(a.getName()+",");
			}
			sb.append("]\n");
		}
		return sb.toString();
	}
	
	/* 
	 * PS: The following functions are DISABLED in PersonalAppEcosystem 
	 * 
	 */
	/**
	 * PersonalAppEcosystem中不可用
	 */
	@Override
	public boolean addObject(App a, int index, double angle) {
		try {
			throw new Exception("Unable to use addObject(angle) in PersonalAppEcosystem");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * PersonalAppEcosystem中不可用
	 */
	@Override
	public double getAngle(App a) {
		try {
			throw new Exception("Unable to use getAngle() in PersonalAppEcosystem");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	/**
	 * PersonalAppEcosystem中不可用
	 */
	@Override 
	public int transit(App object, int newIndex) {
		try {
			throw new Exception("Unable to use transit() in PersonalAppEcosystem");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	/**
	 * PersonalAppEcosystem中不可用
	 */
	@Override
	public boolean transit(App object, int oldIndex, int newIndex) {
		try {
			throw new Exception("Unable to use transit() in PersonalAppEcosystem");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * PersonalAppEcosystem中不可用
	 */
	@Override
	public boolean transit(int oldIndex, int newIndex) {
		try {
			throw new Exception("Unable to use transit() in PersonalAppEcosystem");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
