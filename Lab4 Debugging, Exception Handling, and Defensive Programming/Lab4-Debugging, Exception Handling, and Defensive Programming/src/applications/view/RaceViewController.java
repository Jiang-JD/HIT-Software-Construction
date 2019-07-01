package applications.view;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import APIs.CircularOrbitAPIs;
import applications.tools.BuildVisualPane;
import circularOrbit.TrackGame;
import constant.Regex;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import manager.TrackGameManager;
import physicalObject.Athlete;
import physicalObject.AthleteFactory;

public class RaceViewController {

	@FXML
	private TextField newtrack;

	@FXML
	private TextField removetrack;

	@FXML
	private TextField athlete;

	@FXML
	private TextField name;

	@FXML
	private TextField number;

	@FXML
	private TextField nation;

	@FXML
	private TextField age;

	@FXML
	private TextField score;

	@FXML
	private TextField addtrack;

	@FXML
	private Label entropy;

	@FXML
	private Label errorAddTrack;

	@FXML
	private Label errorRemoveTrack;

	@FXML
	private Label errorRemoveAthlete;

	@FXML
	private Label errorAddAthlete;

	@FXML
	private StackPane vispane;
	
	@FXML
    private AnchorPane rightAnchor;
	
	private TrackGame current;
	private TrackGame tmp;
	private TrackGameManager tgm;
	private Stage stage;
	private AthleteFactory af = new AthleteFactory();
	private  ObservableList<TrackGame> tl = FXCollections.observableArrayList();
	private List<Athlete> adda = new ArrayList<Athlete>();
	private List<Athlete> removea = new ArrayList<Athlete>();
	private TGMenuController tgc;
	private Logger logger = Logger.getLogger(RaceViewController.class);

	@FXML
	void handleAddAthlete(ActionEvent event) {
		if(checkAddAthlete()) {
			Athlete a = (Athlete) af.create("Athlete ::= <"+name.getText()+","+number.getText()+","+nation.getText()+","+age.getText()+","+score.getText()+">");
			adda.add(a);
			tmp.addObject(a, Integer.parseInt(addtrack.getText()));
			refreshVis();
		}
	}
	
	@FXML
	void handleAddTrack() {
		if(checkAddTrack()) {
			tmp.addTrack(Double.parseDouble(newtrack.getText()));
			tmp.sort();
			refreshVis();
			logger.info("User add new track: "+newtrack.getText());
		}
	}

	@FXML
	void handleCancel(ActionEvent event) {
		stage.close();
	}

	@FXML
	void handleRemoveAthlete(ActionEvent event) {
		if(checkRemoveAthlete()) {
			Athlete a = tmp.getAthlete(athlete.getText());
			removea.add(a);
			tmp.remove(a);
			refreshVis();
		}
	}

	@FXML
	void handleRemoveTrack(ActionEvent event) {
		if(checkRemoveTrack()) {
			List<Athlete> al = tmp.getObjects(Integer.parseInt(removetrack.getText()));
			removea.addAll(al);
			tmp.remove(Integer.parseInt(removetrack.getText()));
			refreshVis();
			logger.info("User remove a track: "+removetrack.getText());
		}
	}

	@FXML
	void handleSave(ActionEvent event) {
		if(!adda.isEmpty()) {
			tgm.addAll(adda);
			logger.info("User add new athletes: "+adda.toString());
		}
		if(!removea.isEmpty()) {
			tgm.removeAll(removea);
			logger.info("User remove athletes: "+removea.toString());
		}
		tgc.editTG(tmp);
		stage.close();
	}
	
	
	@FXML
	void initialize() {
	}
	
	private boolean checkAddTrack() {
		if(!newtrack.getText().matches("^\\d+$")) {
			errorAddTrack.setText("Track Number illegal, please try again!");
			return false;
		}
		if(tmp.contains(Double.parseDouble(newtrack.getText()))) {
			errorAddTrack.setText("Track already exists, please try again!");
			return false;
		}
		if(tmp.getTrackNum()+1 > 10) {
			errorAddTrack.setText("Track Number > 10, please try again!");
			return false;
		}
		errorAddTrack.setText("");
		return true;
	}
	
	private boolean checkRemoveTrack() {
		if(!removetrack.getText().matches("^\\d+$")) {
			errorRemoveTrack.setText("Track Number illegal, please try again!");
			return false;
		}
		if(!tmp.contains(Double.parseDouble(removetrack.getText()))) {
			errorRemoveTrack.setText("Track not exists, please try again!");
			return false;
		}
		if(tmp.getTrackNum()-1 < 4) {
			errorRemoveTrack.setText("Track Number < 4, please try again!");
			return false;
		}
		errorRemoveTrack.setText("");
		return true;
	}
	
	private boolean checkAddAthlete() {
		if(!name.getText().matches(Regex.REGEX_WORD)) {
			errorAddAthlete.setText("Name is illegal, please try again!");
			return false;
		}
		if(!number.getText().matches(Regex.REGEX_NUMBER_SPEC)) {
			errorAddAthlete.setText("Number is illegal, please try again!");
			return false;
		}
		if(Double.parseDouble(number.getText()) != (int)Double.parseDouble(number.getText())) {
			errorAddAthlete.setText("Number is not integer, please try again!");
			return false;
		}
		if(tgm.getAthlete((int)Double.parseDouble(number.getText())) != null) {
			errorAddAthlete.setText("Number already exists, please try again!");
			return false;
		}
		if(!nation.getText().matches(Regex.TRACKGAME_NATION)) {
			errorAddAthlete.setText("Nation is illegal, please try again!");
			return false;
		}
		if(!age.getText().matches(Regex.REGEX_NUMBER_SPEC)) {
			errorAddAthlete.setText("Age is illegal, please try again!");
			return false;
		}
		if(Double.parseDouble(age.getText()) != (int)Double.parseDouble(age.getText())) {
			errorAddAthlete.setText("Age is not integer, please try again!");
			return false;
		}
		if(!score.getText().matches(Regex.TRACKGAME_PERSONALBEST)) {
			errorAddAthlete.setText("Score is illegal, please try again!");
			return false;
		} 
		if(!addtrack.getText().matches("^\\d+$")) {
			errorAddAthlete.setText("Track Number illegal, please try again!");
			return false;
		}
		if(tmp.getTrackNum() <= Integer.parseInt(addtrack.getText())) {
			errorAddAthlete.setText("Track not exists, please try again!");
			return false;
		}
		if(!tmp.getObjects(Integer.parseInt(addtrack.getText())).isEmpty()) {
			errorAddAthlete.setText("Track has athlete, please try again!");
			return false;
		}
		errorAddAthlete.setText("");
		return true;
	}
	
	private boolean checkRemoveAthlete() {
		if(!athlete.getText().matches(Regex.REGEX_WORD)) {
			errorRemoveAthlete.setText("Name is illegal, please try again!");
			return false;
		}
		Athlete a;
		if((a=tgm.getAthlete(athlete.getText())) == null) {
			errorRemoveAthlete.setText("Athlete not exist, please try again!");
			return false;
		}
		if(!tmp.contains(a)) {
			errorRemoveAthlete.setText("Athlete not exist, please try again!");
			return false;
		}
		errorRemoveAthlete.setText("");
		return true;
	}
	
	public void refreshVis() {
		vispane.getChildren().clear();
		vispane.getChildren().add(BuildVisualPane.drawCircularOrbit(tmp, vispane.getPrefWidth(), vispane.getPrefHeight()));
		entropy.setText(String.valueOf(new CircularOrbitAPIs().getObjectDistributionEntropy(tmp)));
	}

	public void setTrackGame(TrackGame tg) {
		this.current = tg;
		tmp = current.clone();
	}
	
	public void setTrackGameManager(TrackGameManager tgm) {
		this.tgm = tgm;
	}
	
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	public void setTGMenuController(TGMenuController tgc) {
		this.tgc = tgc;
	}
}
