package APIs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import centralObject.CentralPoint;
import centralObject.Nucleus;
import centralObject.Person;
import circularOrbit.AtomStructure;
import circularOrbit.CircularOrbit;
import circularOrbit.ConcreteCircularOrbit;
import circularOrbit.PersonalAppEcosystem;
import circularOrbit.TrackGame;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import physicalObject.App;
import physicalObject.Athlete;
import physicalObject.Electron;
import testObjects.TestObject;

/**
 * 绘制轨道系统GUI
 *
 */
public class fx extends Application {
	private String title;
	public static CircularOrbit co;
	private Scene sc;
	private final static double centerradius = 22;
	private final static double orbitradius = 15;
	private final static double trackradius = 50;
	
	@Override
	public void start(Stage primaryStage) {
		Group root = new Group();
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		Circle central;
		List<Circle> tracks = new ArrayList<Circle>();

		/*
		 * 绘制TrackGame 
		 */
		if (co instanceof TrackGame) {
			double base = trackradius;
			// add empty central point
			central = addEmptyCenter(root, scene);
			// add tracks
			for (int i = 0; i < co.getTrackNum(); i++) {
				tracks.add(addTrack(root, base + i * trackradius * 1.11, central));
			}
			// add athlete
			for (int i = 0; i < co.getTrackNum(); i++) {
				for (int j = 0; j < co.getObjects(i).size(); j++) {
					Athlete a = (Athlete) co.getObjects(i).get(j);
					addObj(root, a.getName(), tracks.get(i), true, true);
				}
			}
			sc = scene;
			title = ((TrackGame) co).getName();
			/*
			 * 绘制Atom
			 */
		} else if (co instanceof AtomStructure) {
			double base = trackradius;
			List<String> appname = new ArrayList<String>();
			// add empty central point
			Nucleus cp = (Nucleus)co.getCentralPoint();
			central = addCentralPoint(root, cp.getName(), scene);
			// add tracks
			for (int i = 0; i < co.getTrackNum(); i++) {
				tracks.add(addTrack(root, base + i * trackradius * 1.11, central));
			}
			// add electron
			for (int i = 0; i < co.getTrackNum(); i++) {
				for (int j = 0; j < co.getObjects(i).size(); j++) {
					Electron a = (Electron) co.getObjects(i).get(j);
					appname.add(a.getName());
				}
				List<Circle> circles = addObjsEvenDistributed(root, appname, tracks.get(i), false);
				appname.clear();
			}
			sc = scene;
			title = ((AtomStructure) co).getName();
		
			/*
			 * 绘制AppEcosystem
			 */
		} else if (co instanceof PersonalAppEcosystem) {
			double base = trackradius;
			List<App> apps = new ArrayList<App>();
			Map<App ,Circle> appc = new HashMap<App, Circle>();
			List<App> trackapps = new ArrayList<App>();
			List<String> appname = new ArrayList<String>();
			// add user
			Person cp = (Person)co.getCentralPoint();
			central = addCentralPoint(root, cp.getName(), scene);
			// add tracks
			for (int i = 0; i < co.getTrackNum(); i++) {
				tracks.add(addTrack(root, base + i * trackradius * 1.11, central));
			}
			// add app
			for (int i = 0; i < co.getTrackNum(); i++) {
				for (int j = 0; j < co.getObjects(i).size(); j++) {
					App a = (App) co.getObjects(i).get(j);
					apps.add(a);
					trackapps.add(a);
					appname.add(a.getName());
				}
				List<Circle> circles = addObjs(root, appname, tracks.get(i), false, true);
				for(int k = 0 ; k < appname.size(); k++) {
					appc.put(trackapps.get(k), circles.get(k));
				}
				trackapps.clear();
				appname.clear();
			}
			// add relation
			for(App a : apps) {
				Set<App> set = co.relatives(a);
				for(App t : set) {
					addRelation(root, appc.get(a), appc.get(t));
				}
			}
			sc = scene;
			title = ((PersonalAppEcosystem) co).getName();
		} else {
			double base = trackradius;
			List<TestObject> apps = new ArrayList<TestObject>();
			Map<TestObject ,Circle> appc = new HashMap<TestObject, Circle>();
			List<TestObject> trackapps = new ArrayList<TestObject>();
			List<String> appname = new ArrayList<String>();
			// add user
			CentralPoint cp = (CentralPoint)co.getCentralPoint();
			if(cp != null) {
				central = addCentralPoint(root, cp.getName(), scene);
			}
			else {
				central = addEmptyCenter(root, scene);
			}
			// add tracks
			for (int i = 0; i < co.getTrackNum(); i++) {
				tracks.add(addTrack(root, base + i * trackradius * 1.11, central));
			}
			// add app
			for (int i = 0; i < co.getTrackNum(); i++) {
				for (int j = 0; j < co.getObjects(i).size(); j++) {
					TestObject a = (TestObject) co.getObjects(i).get(j);
					apps.add(a);
					trackapps.add(a);
					appname.add(a.getName());
				}
				List<Circle> circles = addObjs(root, appname, tracks.get(i), false, true);
				for(int k = 0 ; k < appname.size(); k++) {
					appc.put(trackapps.get(k), circles.get(k));
				}
				trackapps.clear();
				appname.clear();
			}
			// add relation
			for(TestObject a : apps) {
				Set<TestObject> set = co.relatives(a);
				for(TestObject t : set) {
					addRelation(root, appc.get(a), appc.get(t));
				}
			}
			sc = scene;
			title = ((ConcreteCircularOrbit) co).getName();
		}
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		    @Override
		    public void handle(WindowEvent event) {
		        try {
					primaryStage.hide();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		});
		primaryStage.setWidth(2*(trackradius + co.getTrackNum() * trackradius * 1.11));
		primaryStage.setHeight(2*(trackradius + co.getTrackNum() * trackradius * 1.11));
		primaryStage.setTitle(title);
		primaryStage.show();
	}

	/**
	 * 启动入口
	 */
	public void show() {
		launch();
	}

	/**
	 * 向轨道系统添加中心点
	 * @param root GroupPane容器
	 * @param name 中心点名称
	 * @param s 场景
	 * @return 生成的中心点Circle
	 */
	public static Circle addCentralPoint(Group root, String name, Scene s) {
		Circle cen = new Circle(centerradius, Color.RED);
		cen.centerXProperty().bind(s.widthProperty().divide(2));
		cen.centerYProperty().bind(s.heightProperty().divide(2));
		cen.setStroke(Color.BLACK);
		cen.setStrokeWidth(3);
		Text t = new Text(name);
		t.setFont(new Font("consolas", centerradius*0.83));
		t.xProperty().bind(cen.centerXProperty().subtract(cen.getRadius() * 0.7));
		t.yProperty().bind(cen.centerYProperty().subtract(cen.getRadius() * 1.2));
		
		root.getChildren().addAll(cen, t);
		return cen;
	}

	/**
	 * 向系统添加空的中心点，适用于径赛
	 * @param root GroupPane容器
	 * @param s 场景
	 * @return 空中心circle
	 */
	public static Circle addEmptyCenter(Group root, Scene s) {
		Circle cen = new Circle(30, Color.web("white", 0));
		cen.centerXProperty().bind(s.widthProperty().divide(2));
		cen.centerYProperty().bind(s.heightProperty().divide(2));
		root.getChildren().addAll(cen);
		return cen;
	}

	/**
	 * 向轨道系统添加Track
	 * @param root GroupPane
	 * @param size 轨道半径
	 * @param cen 中心点
	 * @return 轨道Circle
	 */
	public static Circle addTrack(Group root, double size, Circle cen) {
		Circle t1 = new Circle(size, Color.web("white", 0));
		t1.centerXProperty().bind(cen.centerXProperty());
		t1.centerYProperty().bind(cen.centerYProperty());
		t1.setStroke(Color.DARKGRAY);
		t1.setStrokeWidth(2);
		root.getChildren().add(t1);
		return t1;
	}

	/**
	 * 向轨道系统添加单个物体
	 * @param root GroupPane
	 * @param name 物体名称
	 * @param track 所属轨道
	 * @param tidy 是否与其他轨道物体对齐，即排在同一条线上
	 * @param showname 是否显示物体名称
	 * @return 物体Circle
	 */
	public static Circle addObj(Group root, String name, Circle track, boolean tidy, boolean showname) {
		Circle c = new Circle(orbitradius, Color.LIGHTBLUE);
		// calculate obj position
		DoubleProperty r = track.radiusProperty();
		double angle = 0;
		if (!tidy) {
			angle = Math.random() * 360;
		} else {
			angle = 90;
		}
		double sin = Math.sin(angle * (Math.PI / 180));
		double cos = Math.cos(angle * (Math.PI / 180));
		DoubleBinding x = r.multiply(cos).add(track.centerXProperty());
		DoubleBinding y = r.multiply(sin).add(track.centerYProperty());
		c.centerXProperty().bind(x);
		c.centerYProperty().bind(y);
		c.setStroke(Color.BLACK);
		c.setStrokeWidth(3);
		if(showname) {
			Text tn = new Text(name);
			tn.setFont(new Font("consolas", orbitradius * 1.111));
			tn.xProperty().bind(c.centerXProperty().subtract(c.getRadius() * 0.8));
			tn.yProperty().bind(c.centerYProperty().subtract(c.getRadius() * 1.2));
			root.getChildren().addAll(c, tn);
		}
		else {
			root.getChildren().add(c);
		}
		return c;
	}
	
	/**
	 * 向轨道系统添加多个物体，位置随机
	 * @param root GroupPane
	 * @param name 物体名称列表
	 * @param track 所属轨道
	 * @param tidy 是否与其他轨道物体对齐，即排在同一条线上
	 * @param showname 是否显示物体名称
	 * @return 物体Circle列表
	 */
	public static List<Circle> addObjs(Group root, List<String> names, Circle track, boolean tidy, boolean showname) {
		List<Circle> li = new ArrayList<Circle>();
		// calculate obj position
		DoubleProperty r = track.radiusProperty();
		double angle = Math.random() * 360;
		double divide = 360.0/(names.size()*1.8);
		for(int i = 0; i < names.size(); i++) {
			Circle c = new Circle(orbitradius, Color.LIGHTBLUE);
			if (!tidy) {
				angle = angle + divide;
			} else {
				angle = 90 + i * divide;
			}
			double sin = Math.sin(angle * (Math.PI / 180));
			double cos = Math.cos(angle * (Math.PI / 180));
			DoubleBinding x = r.multiply(cos).add(track.centerXProperty());
			DoubleBinding y = r.multiply(sin).add(track.centerYProperty());
			c.centerXProperty().bind(x);
			c.centerYProperty().bind(y);
			c.setStroke(Color.BLACK);
			c.setStrokeWidth(3);
			if (showname) {
				Text tn = new Text(names.get(i));
				tn.setFont(new Font("consolas", orbitradius * 1.111));
				tn.xProperty().bind(c.centerXProperty().subtract(c.getRadius() * 0.8));
				tn.yProperty().bind(c.centerYProperty().subtract(c.getRadius() * 1.2));
				root.getChildren().addAll(c, tn);
			} else {
				root.getChildren().add(c);
			}
			li.add(c);
		}
		return li;
	}
	
	/**
	 * 向轨道系统添加多个物体，均匀分布在轨道上
	 * @param root GroupPane
	 * @param name 物体名称列表
	 * @param track 所属轨道
	 * @param showname 是否显示物体名称
	 * @return 物体Circle列表
	 */
	public static List<Circle> addObjsEvenDistributed(Group root, List<String> names, Circle track, boolean showname) {
		List<Circle> li = new ArrayList<Circle>();
		// calculate obj position
		DoubleProperty r = track.radiusProperty();
		double angle = 0;
		double divide = 360 / names.size();
		for(int i = 0; i < names.size(); i++) {
			Circle c = new Circle(orbitradius, Color.LIGHTBLUE);
			angle = i * divide;
			double sin = Math.sin(angle * (Math.PI / 180));
			double cos = Math.cos(angle * (Math.PI / 180));
			DoubleBinding x = r.multiply(cos).add(track.centerXProperty());
			DoubleBinding y = r.multiply(sin).add(track.centerYProperty());
			c.centerXProperty().bind(x);
			c.centerYProperty().bind(y);
			c.setStroke(Color.BLACK);
			c.setStrokeWidth(3);
			if (showname) {
				Text tn = new Text(names.get(i));
				tn.setFont(new Font("consolas", orbitradius * 1.111));
				tn.xProperty().bind(c.centerXProperty().subtract(c.getRadius() * 0.8));
				tn.yProperty().bind(c.centerYProperty().subtract(c.getRadius() * 1.2));
				root.getChildren().addAll(c, tn);
			} else {
				root.getChildren().add(c);
			}
			li.add(c);
		}
		return li;
	}

	/**
	 * 添加物体之间的关系
	 * @param root GroupPane
	 * @param o1 物体1
	 * @param o2 物体2
	 * @return 关系line
	 */
	public static Line addRelation(Group root, Circle o1, Circle o2) {
		Line line = new Line();
		line.setStroke(Color.BLACK);
		line.setStrokeWidth(2);
		line.getStrokeDashArray().addAll(10.0, 15.0, 10.0, 15.0);
		line.setStrokeDashOffset(0);
		line.startXProperty().bind(o1.centerXProperty());
		line.startYProperty().bind(o1.centerYProperty());
		line.endXProperty().bind(o2.centerXProperty());
		line.endYProperty().bind(o2.centerYProperty());
		root.getChildren().add(line);
		o1.toFront();
		o2.toFront();
		return line;
	}

}


