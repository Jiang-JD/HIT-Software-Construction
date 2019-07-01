package circularOrbit;

import java.util.Set;

import applications.MementoTransit;
import centralObject.Nucleus;
import physicalObject.Electron;
import track.ElectronTrackFactory;

/**
 * {@code AtomStructure} 是一个原子结构模型。原子结构模型（AtomStructure）：一个原子由原子核和绕
 * 核运动的电子 组成，原子的质量主要集中在由质子和中子构成的原子核上，核外分布 着电子。原子可看作是以原子核为中心
 * 的多条电子轨道的结构。{@code AtomStructure} 将原子核作为整体看作中心点的物体，不考虑其内部的质
 * 子和中子。不考虑电子之间的关系。不考虑电子的绝对位置，也无需考虑 电子在轨道上的运动。
 * 电子可以在不同轨道之间跃迁。由于原子在运行时结构可变，因此是 {@code mutable} 的。 
 
 *
 */
public class AtomStructure extends ConcreteCircularOrbit<Nucleus, Electron> {

	private State<Nucleus, Electron> state = new ConcreteAtomState();
	
	/*
	 * Abstract Function:
	 * 	AF(rep extends circularorbit, state) = 一个能够进行备份和恢复的电子轨道系统
	 * 
	 * Rep Invariant:
	 * 	state 	state非空对象
	 * 
	 * Safety from exposure:
	 * 	state是private的
	 */
	
	private void checkRep() {
		assert state != null : "State is null pointer";
	}
	
	public AtomStructure(String name) {
		super(name, new ElectronTrackFactory());
		checkRep();
	}

	/**
	 * 将当前的原子轨道系统状态备份，备份信息包括中心物体，轨道和每一条轨道对应的电子。
	 * 
	 * @return 当前状态的备份
	 *
	 */
	public MementoTransit backup() {
		return state.backup(this);
	}
	
	/**
	 * 将原子结构恢复到某一个备忘录条目记录的状态，注意，指定这个方法会恢复原子结构的轨道，中心物体，
	 * 每一条轨道上所处的电子信息，也就意味着恢复之前的状态将被破坏。
	 * 
	 * @param m 待恢复备忘录条目，要求不为空
	 * @return 恢复之前的系统状态备忘条目
	 */
	public MementoTransit restore(MementoTransit m) {
		MementoTransit me = backup();
		if(state.restore(m, this)) {
			return me;
		}
		return null;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("[AtomStructure:"+getName()+", TrackNumber:"+getTrackNum()+"]\n");
		sb.append("[ElectronsNum:");
		for(int i = 0; i < getTrackNum(); i++) {
			sb.append("[Track"+(i+1)+": "+getObjects(i).size()+"]");
		}
		return sb.toString();
	}
	
	/* 
	 * PS: The following functions are DISABLED in AtomStructure
	 * 
	 */
	
	@Override
	public boolean addObject(Electron a, int index, double angle) {
		try {
			throw new Exception("Unable to use addObject(angle) in AtomStructure");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public boolean addCentralRelation(Electron a) {
		try {
			throw new Exception("Unable to use addCentralRelation() in AtomStructure");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public boolean addOrbitRelation(Electron a1, Electron a2) {
		try {
			throw new Exception("Unable to use addOrbitRelation() in AtomStructure");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public Set<Electron> centrals() {
		try {
			throw new Exception("Unable to use centrals() in AtomStructure");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public double getAngle(Electron a) {
		try {
			throw new Exception("Unable to use getAngle() in AtomStructure");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	@Override
	public Set<Electron> relatives(Electron a) {
		try {
			throw new Exception("Unable to use relatives() in AtomStructure");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public boolean removeCentralRelation(Electron a) {
		try {
			throw new Exception("Unable to use removeCentralRelation() in AtomStructure");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public boolean removeOrbitRelation(Electron a1, Electron a2) {
		try {
			throw new Exception("Unable to use removeOrbitRelation() in AtomStructure");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
}
