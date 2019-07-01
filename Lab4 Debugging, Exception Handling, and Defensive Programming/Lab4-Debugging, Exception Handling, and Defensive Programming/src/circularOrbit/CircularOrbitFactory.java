package circularOrbit;

import java.util.List;

import org.apache.log4j.Logger;

import centralObject.CentralPointFactory;
import centralObject.Nucleus;
import centralObject.NucleusFactory;
import physicalObject.Athlete;
import physicalObject.Electron;
import physicalObject.ElectronFactory;

public class CircularOrbitFactory {
	private static Logger logger = Logger.getLogger(CircularOrbitFactory.class);
	
	/**
	 * 径赛的静态工厂方法，输入径赛名称，比赛类型，跑道数目， 运动员列表，生成一个径赛系统
	 * @param name 径赛名称
	 * @param racetype 比赛项目
	 * @param tracknum 轨道数目
	 * @param athletes 运动员列表
	 * @return 一个径赛轨道系统
	 */
	public static TrackGame build(String name, String racetype, int tracknum, List<List<Athlete>> athletes) {
		if(!racetype.matches("(100|200|400)")) {
			logger.error("Error racetype");
			throw new IllegalArgumentException("Error racetype");
		}
		if(name == null) {
			logger.error("null name");
			throw new NullPointerException();
		}
		if(tracknum > 10 || tracknum < 4) {
			logger.error("error tracknum");
			throw new IllegalArgumentException("error tracknum");
		}

		TrackGame tg = new TrackGame(name, racetype);
		for(int i = 0 ; i < tracknum; i++) {
			logger.info(tg.getName()+" add new track: "+(i+1));
			tg.addTrack(i+1);
		}
		for(int i = 0 ; i < athletes.size(); i++) {
			for(Athlete a : athletes.get(i)) {
				tg.addObject(a, i);
				logger.info(tg.getName()+" add new athlete: "+a.getName()+" on track: "+(i+1));
			}
		}
		return tg;
		
	}
	
	/**
	 * 原子轨道系统静态工厂方法，输入原子核名称，轨道数目，电子数目，生成原子系统
	 * @param nucleus 原子核名称，符合构造规则
	 * @param tracknum 轨道数目，大于等于0
	 * @param electrons 电子数目，每个轨道电子数目大于等于0，列表与轨道应一一对应
	 * @return 原子轨道系统
	 */
	public static AtomStructure build(String nucleus, int tracknum, List<Integer> electrons) {
		if(!nucleus.matches("[A-Z][a-z]?")) {
			logger.error("Illegal nucleus name");
			throw new IllegalArgumentException();
		}
		if(tracknum < 0) {
			logger.error("illegal tracknum");
			throw new IllegalArgumentException();
		}
		if(electrons.size() != tracknum) {
			logger.error("Wrong mapping");
			throw new IllegalArgumentException();
		}
		for(Integer i : electrons) assert i >= 0;
		
		AtomStructure as = new AtomStructure(nucleus);
		//nucleus
		CentralPointFactory cpf = new NucleusFactory();
		Nucleus cp = (Nucleus) cpf.create(nucleus);
		as.addCentralPoint(cp);
		logger.info(as.getName()+" add new central point: "+cp.getName());
		//tracks
		for(int i = 0; i < tracknum; i++) {
			as.addTrack(i+1); //半径为轨道编号
			logger.info(as.getName()+" add new track: "+(i+1));
		}
		//electrons
		ElectronFactory ef = new ElectronFactory();
		for(int i = 0; i < tracknum; i++) {
			for(int j = 0; j < electrons.get(i); j++) {
				as.addObject((Electron)ef.create("electron"), i);
				logger.info(as.getName()+" add new electron on track: "+(i+1));
			}
		}
		
		return as;
	}
}
