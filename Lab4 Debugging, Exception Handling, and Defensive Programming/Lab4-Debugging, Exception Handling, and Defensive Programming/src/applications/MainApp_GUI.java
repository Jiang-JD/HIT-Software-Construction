package applications;
import java.io.File;
import java.io.IOException;

import applications.tools.BuildVisualPane;
import applications.tools.Memento;
import applications.tools.MementoCareTaker;
import applications.view.ASMenuController;
import applications.view.DifferenceController;
import applications.view.DistanceController;
import applications.view.DurationController;
import applications.view.ExchangeController;
import applications.view.FileErrorController;
import applications.view.LogController;
import applications.view.LoginController;
import applications.view.PAMenuController;
import applications.view.RaceViewController;
import applications.view.RestoreController;
import applications.view.TGMenuController;
import applications.view.TransitController;
import circularOrbit.AtomStructure;
import circularOrbit.CircularOrbit;
import circularOrbit.PersonalAppEcosystem;
import circularOrbit.TrackGame;
import exception.FileFormatException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import manager.PersonalAppManager;
import manager.TrackGameManager;

public class MainApp_GUI extends Application {

	private Stage stage;
	private AnchorPane login;
	private int tg_num1;
	private int tg_num2;
	private int as_si;
	private int as_ei;
	private int as_num;
	private Memento as_memento;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage;
		this.stage.setTitle("CircularOrbitApplication");
		initLogin();
		
	}
	
	public void initLogin() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp_GUI.class.getResource("view/Login.fxml"));
			login = (AnchorPane)loader.load();
			LoginController controller = loader.getController();
			controller.setMainApp(this);
			//show login view
			Scene scene = new Scene(login);
			stage.setScene(scene);
			stage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void initFileErroe(File file, FileFormatException e) {
		Stage errorStage = new Stage();
		errorStage.setTitle("Illegal File Report");
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp_GUI.class.getResource("view/FileError.fxml"));
		try {
			AnchorPane fileError = (AnchorPane) loader.load();
			FileErrorController controller = loader.getController();
			controller.setFile(file);
			controller.setException(e);
			Scene scene = new Scene(fileError);
			errorStage.setScene(scene);
			errorStage.show();
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
	}
	
	private TGMenuController tgmenucontroller;
	
	public void initTGMenu(TrackGameManager tgm) {
		try {
			Stage tgstage = new Stage();
			tgstage.setTitle("TrackGame");
			FXMLLoader tgloader = new FXMLLoader();
			tgloader.setLocation(MainApp_GUI.class.getResource("view/TGMenu.fxml"));
			AnchorPane tgmenu = (AnchorPane)tgloader.load();
			Scene scene = new Scene(tgmenu);
			TGMenuController controller = tgloader.getController();
			tgmenucontroller = controller;
			controller.setTrackGameManager(tgm);
			controller.setMainApp(this);
			tgstage.setScene(scene);
			tgstage.show();
			getStage().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void initRaceView(TrackGame current, TrackGameManager tgm) {
		try {
			Stage racestage = new Stage();
			racestage.setTitle(current.getName());
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp_GUI.class.getResource("view/RaceView.fxml"));
			AnchorPane raceview;
			raceview = (AnchorPane) loader.load();
			Scene scene = new Scene(raceview);
			RaceViewController controller = loader.getController();
			controller.setTrackGame(current);
			controller.setStage(racestage);
			controller.setTrackGameManager(tgm);
			controller.setTGMenuController(tgmenucontroller);
			controller.refreshVis();
			racestage.setScene(scene);
			racestage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean initExchange(TrackGameManager tgm) {
		try {
			Stage exchangestage = new Stage();
			exchangestage.setTitle("Exchange Two Athletes");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp_GUI.class.getResource("view/Exchange.fxml"));
			AnchorPane exchange;
			exchange = (AnchorPane)loader.load();
			Scene scene = new Scene(exchange);
			ExchangeController controller = loader.getController();
			controller.setStage(exchangestage);
			controller.setTrackGameManager(tgm);
			exchangestage.setScene(scene);
			exchangestage.showAndWait();
			if(controller.isOK_Clicked()) {
				tg_num1 = controller.getnum1();
				tg_num2 = controller.getnum2();
				return true;
			}
			else {
				return false;
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Get TrackGame exchange number
	 */
	public int getTGNum1() {
		return tg_num1;
	}
	
	/**
	 * Get TrackGame exchange number
	 * @return number2
	 */
	public int getTGNum2() {
		return tg_num2;
	}
	
	/**
	 * Initialize AS Menu
	 */
	public void initASMenu(AtomStructure as) {
		try {
			Stage stage = new Stage();
			stage.setTitle("AtomStructure");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp_GUI.class.getResource("view/ASMenu.fxml"));
			AnchorPane menu = (AnchorPane)loader.load();
			Scene scene = new Scene(menu);
			ASMenuController controller = loader.getController();
			controller.setMainApp(this);
			controller.setAtomStructure(as);
			stage.setScene(scene);
			stage.show();
			getStage().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean initTransit(AtomStructure as) {
		try {
			Stage stage = new Stage();
			stage.setTitle("Transit");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp_GUI.class.getResource("view/Transit.fxml"));
			AnchorPane menu = (AnchorPane)loader.load();
			Scene scene = new Scene(menu);
			TransitController controller = loader.getController();
			controller.setAS(as);
			controller.setStage(stage);
			stage.setScene(scene);
			stage.showAndWait();
			if(controller.isOK_Clicked()) {
				as_si = controller.getStartIndex();
				as_ei = controller.getEndIndex();
				as_num = controller.getEleNumber();
				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public int getASStartIndex() {
		return as_si;
	}
	
	public int getASEndIndex() {
		return as_ei;
	}
	
	public int getEleNumber() {
		return as_num;
	}
	
	public boolean initRestore(MementoCareTaker mck) {
		try {
			Stage stage = new Stage();
			stage.setTitle("Resotre");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp_GUI.class.getResource("view/Restore.fxml"));
			AnchorPane menu = (AnchorPane)loader.load();
			Scene scene = new Scene(menu);
			RestoreController controller = loader.getController();
			controller.setMementoCareTaker(mck);
			controller.setStage(stage);
			stage.setScene(scene);
			stage.showAndWait();
			if(controller.isOK_Clicked()) {
				as_memento = controller.getMemento();
				return true;
			}
			else {
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public Memento getBackUp() {
		return as_memento;
	}
	
	private PAMenuController pamenucontroller;
	
	public void initPAMenu(PersonalAppManager pam) {
		try {
			Stage stage = new Stage();
			stage.setTitle("PersonalAppEcosystem");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp_GUI.class.getResource("view/PAMenu.fxml"));
			AnchorPane menu = (AnchorPane)loader.load();
			Scene scene = new Scene(menu);
			PAMenuController controller = loader.getController();
			pamenucontroller = controller;
			controller.setPersonalAppManager(pam);
			controller.setMainApp(this);
			stage.setScene(scene);
			stage.show();
			getStage().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void initDuration(PersonalAppEcosystem current, PersonalAppManager pam) {
		try {
			Stage durstage = new Stage();
			durstage.setTitle(current.getName());
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp_GUI.class.getResource("view/Duration.fxml"));
			AnchorPane view;
			view = (AnchorPane) loader.load();
			Scene scene = new Scene(view);
			DurationController controller = loader.getController();
			controller.setPersonalAppEcosystem(current);
			controller.setStage(durstage);
			controller.setPersonalAppManager(pam);
			controller.setPAMenuController(pamenucontroller);
			controller.refreshVis();
			durstage.setScene(scene);
			durstage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void initDistance(PersonalAppManager pam) {
		try {
			Stage stage = new Stage();
			stage.setTitle("Logic Distance");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp_GUI.class.getResource("view/Distance.fxml"));
			AnchorPane menu = (AnchorPane)loader.load();
			Scene scene = new Scene(menu);
			DistanceController controller = loader.getController();
			controller.setPersonalAppManager(pam);
			controller.setStage(stage);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void initDifference(PersonalAppManager pam) {
		try {
			Stage stage = new Stage();
			stage.setTitle("Difference");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp_GUI.class.getResource("view/Difference.fxml"));
			AnchorPane menu = (AnchorPane)loader.load();
			Scene scene = new Scene(menu);
			DifferenceController controller = loader.getController();
			controller.setPersonalAppManager(pam);
			controller.setStage(stage);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void initVisualize(CircularOrbit co) {
		Stage visstage = BuildVisualPane.drawCircularOrbit(co);
		visstage.show();
	}
	
	public void initLogSystem() {
		try {
			Stage stage = new Stage();
			stage.setTitle("Log System");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp_GUI.class.getResource("view/Log.fxml"));
			AnchorPane menu = (AnchorPane)loader.load();
			Scene scene = new Scene(menu);
			LogController controller = loader.getController();
			controller.setStage(stage);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	/**
	 * Get the current stage, login
	 * @return current stage
	 */
	public Stage getStage() {
		return stage;
	}
	
}
