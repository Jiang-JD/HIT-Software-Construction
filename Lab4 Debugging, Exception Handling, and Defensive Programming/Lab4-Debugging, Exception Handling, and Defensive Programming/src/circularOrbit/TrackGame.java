package circularOrbit;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import centralObject.CentralPoint;
import physicalObject.Athlete;
import track.RaceTrackFactory;

/**
 * {@code TrackGame} 是一场径赛，径赛是在田径场的跑道上进行的跑和 走的竞赛项目的统称，例如 100 米、200 米、400 米
 * 、800 米等项目。一 个标准的径赛场地通常由两个半径相等的半圆弯道和两个长度相等的直 段组成。{@code TrackGame} 中场地形状
 * 简化成圆形。圆形周围划分为等 距的若干跑道，在短距离赛跑比赛中每个跑道容纳 1 名运动员，比赛过程中运动员不能切换跑道。<br>
 * ,{@code TrackGame} 会维护一个运动员集合，保存着所有在径赛中的运动员。在比赛前运动员会根据不同的分组进行变动，因此
 * 这个类是{@code mutable} 的 。
 *
 */
public class TrackGame extends ConcreteCircularOrbit<CentralPoint, Athlete>  implements Cloneable{
	private String racetype;
	private int trackNum; 
	private Logger logger = Logger.getLogger(TrackGame.class);
	
	/*
	 * AF
	 * 	AF(racetype) = 一个包含多名运动员，多条跑道的具有不同比赛项目的的径赛轨道系统
	 * 
	 * RI
	 * 	racetype 	比赛项目 只能为 100|200|400 其中之一
	 * 	trackmap	每一条轨道上只能存在最多一个物体
	 * 	centralpoint为空
	 * 	tracks		数量在4-10之间
	 * 	
	 * Safety from exposure
	 * 	racetype为String类型，其他域为private final。
	 *  grouping方法返回的分组做了防御性拷贝，客户端拿不到内部引用。
	 */
	
	private void checkRep() {
		for(int i = 0; i< getTrackNum() ; i++) {
			if(getObjects(i) != null) {
				assert getObjects(i).size() <= 1: "More than one objects on the Race Track";
			}
		}
		assert getCentralPoint() == null : "TrackGame centralpoint is non-null";
		assert racetype.matches("(100|200|400)") : "Wrong racetype in trackgame";
	}
	
	public TrackGame(String name, String racetype) {
		super(name, new RaceTrackFactory());
		this.racetype = racetype;
		logger.info("Build a new TrackGame, "+this.getName());
		checkRep();
	}
	
	/**
	 * 根据运动员的编号查找对应运动员，输入的编号为正整数，每个运动员对应唯一一个编号
	 * @param number 需要查找的运动员编号，编号为正整数
	 * @return 如果找到运动员，则返回对应Athlete对象，若没有找到，返回null
	 */
	public Athlete getAthlete(int number) {
		if(number < 0) {
			logger.error("Athlete number < 0");
			throw new IllegalArgumentException("Athlete number < 0");
		}
		List<Athlete> athletes = new ArrayList<Athlete>();
		for(int i = 0; i < getTrackNum(); i++) {
			athletes.addAll(getObjects(i));
		}
		 for(int i = 0; i< athletes.size(); i++) {
			 if(number == athletes.get(i).getNumber()) {
				 return athletes.get(i);
			 }
		 }
		 return null;
	}
	
	/**
	 * 根据运动员的姓名查找对应运动员，输入的姓名为非空字符串
	 * @param number 需要查找的运动员姓名，非空
	 * @return 如果找到运动员，则返回对应Athlete对象，若没有找到，返回null
	 */
	public Athlete getAthlete(String name) {
		if(name == null) throw new NullPointerException();
		assert !(name.equals("")) : "Name is \"\"";
		List<Athlete> athletes = new ArrayList<Athlete>();
		for(int i = 0; i < getTrackNum(); i++) {
			athletes.addAll(getObjects(i));
		}
		 for(int i = 0; i< athletes.size(); i++) {
			 if(name.equals(athletes.get(i).getName())) {
				 return athletes.get(i);
			 }
		 }
		 return null;
	}
	
	
	/**
	 * 设置比赛项目名称
	 * @param type 比赛项目名称
	 */
	public void setRaceType(String type) {
		if(racetype.matches("(100|200|400)")) {
			logger.info("Set a new racetype to trackgame "+type);
			racetype = type;
		}
	}
	
	/**
	 * 获得比赛项目名称
	 * @return racetype 比赛项目名称 100|200|400
	 */
	public String getRaceType() {
		return racetype;
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("[TrackGame:"+getName()+", RaceType:"+racetype+", TrackNumber:"+getTrackNum()+"]\n");
		sb.append("[Athletes:");
		for(int i = 0; i < getTrackNum(); i++) {
			sb.append("[Track"+(i+1)+": "+getObjects(i).toString()+"]");
		}
		return sb.toString();
	}
	
	@Override
	public int addTrack(double radius) {
		if(getTrackNum() + 1 > 10) {
			try {
				throw new Exception("RaceTrack Number is More than 10");
			} catch (Exception e) {
				logger.error("RaceTrack Number is More than 10");
				return -1;
			}
		}
		return super.addTrack(radius);
	}
	
	@Override
	public boolean remove(int index) {
		if(getTrackNum() -1 < 4) {
			try {
				throw new IllegalArgumentException("RaceTrack Number is less than 4");
			} catch (Exception e) {
				logger.error("RaceTrack Number is less than 4");
				return false;
			}
		}
		return super.remove(index);
	}
	
	@Override
	public TrackGame clone() {
		super.clone();
		TrackGame copy = new TrackGame(getName(), racetype);
		copy.restoreTracks(this.getTracks());
		copy.restoreMap(this.getTrackMap());
		copy.restorePosition(this.getPosition());
		return copy;
	}
	
	
	/* 
	 * PS: The following functions are DISABLED in TrackGame 
	 * 
	 */
	
	/**
	 * TrackGame中不可用
	 */
	@Override
	public CentralPoint addCentralPoint(CentralPoint cp) {
		try {
			throw new Exception("Unable to use addCentralPoint() in TrackGame");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * TrackGame中不可用
	 */
	@Override
	public boolean addCentralRelation(Athlete a) {
		try {
			throw new Exception("Unable to use addCentralRelation() in TrackGame");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * TrackGame中不可用
	 */
	@Override
	public boolean addOrbitRelation(Athlete a1, Athlete a2) {
		try {
			throw new Exception("Unable to use addOrbitRelation() in TrackGame");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * TrackGame中不可用
	 */
	@Override
	public Set<Athlete> centrals() {
		try {
			throw new Exception("Unable to use centrals() in TrackGame");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * TrackGame中不可用
	 */
	@Override
	public double getAngle(Athlete a) {
		try {
			throw new Exception("Unable to use getAngle() in TrackGame");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	/**
	 * TrackGame中不可用
	 */
	@Override
	public Set<Athlete> relatives(Athlete a) {
		try {
			throw new Exception("Unable to use relatives() in TrackGame");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * TrackGame中不可用
	 */
	@Override
	public boolean removeCentralRelation(Athlete a) {
		try {
			throw new Exception("Unable to use removeCentralRelation() in TrackGame");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * TrackGame中不可用
	 */
	@Override
	public boolean removeOrbitRelation(Athlete a1, Athlete a2) {
		try {
			throw new Exception("Unable to use removeOrbitRelation() in TrackGame");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * TrackGame中不可用
	 */
	@Override 
	public int transit(Athlete object, int newIndex) {
		try {
			throw new Exception("Unable to use transit() in TrackGame");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	/**
	 * TrackGame中不可用
	 */
	@Override
	public boolean transit(Athlete object, int oldIndex, int newIndex) {
		try {
			throw new Exception("Unable to use transit() in TrackGame");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * TrackGame中不可用
	 */
	@Override
	public boolean transit(int oldIndex, int newIndex) {
		try {
			throw new Exception("Unable to use transit() in TrackGame");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
