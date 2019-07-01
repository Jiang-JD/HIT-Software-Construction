package applications.view;

import java.util.Iterator;

import APIs.CircularOrbitAPIs;
import applications.MainApp_GUI;
import applications.tools.DividerAscendingOrder;
import applications.tools.DividerRandom;
import circularOrbit.TrackGame;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import manager.TrackGameManager;
import physicalObject.Athlete;

public class TGMenuController {
	
    @FXML
    private Menu mu_selection;

    @FXML
    private MenuItem mi_exchange;

    @FXML
    private MenuItem mi_log;

    @FXML
    private MenuItem mi_about;

    @FXML
    private TableView<TrackGame> groups;
    
    @FXML
    private TableColumn<TrackGame, String> indexColumn;

    @FXML
    private TableColumn<TrackGame, String> athletesColumn;

    @FXML
    private TableColumn<Integer, String> trackColumn;

    @FXML
    private TableColumn<Integer, String> trackathletesColumn;

    @FXML
    private Button btn_vis;

    @FXML
    private Button btn_edit;

    @FXML
    private Label name;

    @FXML
    private Label gametype;

    @FXML
    private Label tracknum;

    @FXML
    private Label athletenum;

    @FXML
    private Label entropy;

    @FXML
    private TableView<Integer> trackmap;
    
    private MainApp_GUI mainapp;
	private TrackGameManager tgm;
	private  ObservableList<TrackGame> tl = FXCollections.observableArrayList();
	private  ObservableList<Integer> il = FXCollections.observableArrayList();
	private TrackGame current;

    @FXML
    void handleAscendingOrder(ActionEvent event) {
    	tl.clear();
    	for(TrackGame tg : tgm.grouping(new DividerAscendingOrder())) {
    		tl.add(tg);
    	}
    	groups.setItems(tl);
    }

	@FXML
	void handleEdit(ActionEvent event) {
		if(current == null) return;
		mainapp.initRaceView(current, tgm);
	}
	
	public void editTG(TrackGame newt) {
		int index = tl.indexOf(current);
		tl.set(index, newt);
		current = tl.get(index);
		updateTrackGame(current);
	}

    @FXML
    void handleExchange(ActionEvent event) {
    	if(mainapp.initExchange(tgm)) {
    		Athlete a1 = tgm.getAthlete(mainapp.getTGNum1());
    		Athlete a2 = tgm.getAthlete(mainapp.getTGNum2());
    		int index = tl.indexOf(current);
    		tl.clear();
        	for(TrackGame tg : tgm.exchange(a1, a2)) {
        		tl.add(tg);
        	}
    		groups.setItems(tl);
    		if(index != -1) {
    			TrackGame newtg = tl.get(index);
    			updateTrackGame(newtg);
    		}
    	}
    	else 
    		return;
    }

    @FXML
    void handleRandomOrder(ActionEvent event) {
    	tl.clear();
    	for(TrackGame tg : tgm.grouping(new DividerRandom())) {
    		tl.add(tg);
    	}
    	groups.setItems(tl);
    }

    @FXML
    void handleVisualize(ActionEvent event) {
    	if(current == null) return;
    	mainapp.initVisualize(current);
    }
    
    @FXML
    void handleLog() {
    	mainapp.initLogSystem();
    }

    
    @FXML
    private void initialize() {
		groups.setPlaceholder(new Label("Please select grouping strategy"));
		trackmap.setPlaceholder(new Label("Please select a track game"));
		name.setText("");
		gametype.setText("");
		tracknum.setText("");
		athletenum.setText("");
		entropy.setText("");
		indexColumn.setCellValueFactory(cellData -> {
			TrackGame tg = cellData.getValue();
			return new ReadOnlyStringWrapper(tg.getName());
		});
		athletesColumn.setCellValueFactory(cellData -> {
			TrackGame tg = cellData.getValue();
			Iterator<Athlete> it = tg.iterator();
			StringBuilder sb = new StringBuilder();
			Athlete a;
			while (it.hasNext()) {
				a = it.next();
				sb.append("[" + a.getName() + ":" + a.getNumber() + "] ");
			}
			return new ReadOnlyStringWrapper(sb.toString());
		});
		trackColumn.setCellValueFactory(cellData -> { 
			int i = cellData.getValue();
			return new ReadOnlyStringWrapper(String.valueOf(current.getNumber(i)));
		});
		trackathletesColumn.setCellValueFactory(cellData -> {
			int i = cellData.getValue();
			StringBuilder sb = new StringBuilder();
			for(Athlete a : current.getObjects(i)) {
				sb.append("[" + a.getName() + ":" + a.getNumber() + "] ");
			}
			return new ReadOnlyStringWrapper(sb.toString());
		});
		groups.getSelectionModel().selectedItemProperty().addListener(
	            (observable, oldValue, newValue) -> {
	            	if(newValue != null)
	            		updateTrackGame(newValue);});
	}
    
    public void setTrackGameManager(TrackGameManager tgm) {
    	this.tgm = tgm;
    }
    
    public void setMainApp(MainApp_GUI mainapp) {
    	this.mainapp = mainapp;
    }
    
    private void updateTrackGame(TrackGame tg) {
    	current = tg;
    	il.clear();
    	for(int i = 0; i < tg.getTrackNum(); i++) {
    		il.add(i);
    	}
    	name.setText(tg.getName());
    	gametype.setText(tg.getRaceType());
    	tracknum.setText(String.valueOf(tg.getTrackNum()));
    	athletenum.setText(String.valueOf(tg.getObjectNum()));
    	entropy.setText(String.valueOf(new CircularOrbitAPIs().getObjectDistributionEntropy(tg)));
    	trackmap.setItems(il);
    }

}
