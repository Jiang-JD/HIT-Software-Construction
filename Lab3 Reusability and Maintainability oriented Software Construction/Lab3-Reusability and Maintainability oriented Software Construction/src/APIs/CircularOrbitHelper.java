package APIs;

import java.util.List;

import applications.DividerAscendingOrder;
import circularOrbit.AtomStructure;
import circularOrbit.CircularOrbit;
import circularOrbit.PersonalAppEcosystem;
import circularOrbit.TrackGame;
import manager.PersonalAppManager;
import manager.TrackGameManager;
import parser.AtomStructureParser;

public class CircularOrbitHelper {
	
	public static void visualize(CircularOrbit c) {
		fx f = new fx();
		f.co = c;
		f.show();
	}
}
