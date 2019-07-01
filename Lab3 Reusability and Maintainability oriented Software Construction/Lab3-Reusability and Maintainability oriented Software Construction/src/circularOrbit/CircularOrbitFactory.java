package circularOrbit;

import java.util.List;

import centralObject.CentralPointFactory;
import centralObject.Nucleus;
import centralObject.NucleusFactory;
import constant.Regex;
import physicalObject.Athlete;
import physicalObject.Electron;
import physicalObject.ElectronFactory;

public class CircularOrbitFactory {
	/**
	 * 径赛的静态工厂方法，输入径赛名称，比赛类型，跑道数目， 运动员列表，生成一个径赛系统
	 * @param name 径赛名称
	 * @param racetype 比赛项目
	 * @param tracknum 轨道数目
	 * @param athletes 运动员列表
	 * @return 一个径赛轨道系统
	 */
	public static TrackGame build(String name, String racetype, int tracknum, List<List<Athlete>> athletes) {
		assert racetype.matches("(100|200|400)") : "Wrong racetype in trackgame";
		assert name != null;
		assert tracknum > 0;

		TrackGame tg = new TrackGame(name, racetype);
		for(int i = 0 ; i < tracknum; i++) {
			tg.addTrack(i+1);
		}
		for(int i = 0 ; i < athletes.size(); i++) {
			for(Athlete a : athletes.get(i)) {
				tg.addObject(a, i);
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
		assert nucleus.matches("[A-Z][a-z]?");
		assert tracknum >= 0;
		assert electrons.size() == tracknum;
		for(Integer i : electrons) assert i >= 0;
		
		AtomStructure as = new AtomStructure(nucleus);
		//nucleus
		CentralPointFactory cpf = new NucleusFactory();
		Nucleus cp = (Nucleus) cpf.create(nucleus);
		as.addCentralPoint(cp);
		//tracks
		for(int i = 0; i < tracknum; i++) {
			as.addTrack(i+1); //半径为轨道编号
		}
		//electrons
		ElectronFactory ef = new ElectronFactory();
		for(int i = 0; i < tracknum; i++) {
			for(int j = 0; j < electrons.get(i); j++) {
				as.addObject((Electron)ef.create("electron"), i);
			}
		}
		
		return as;
	}
}
