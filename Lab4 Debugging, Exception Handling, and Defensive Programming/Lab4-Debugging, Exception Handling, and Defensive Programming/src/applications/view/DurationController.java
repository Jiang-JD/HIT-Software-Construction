package applications.view;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import APIs.CircularOrbitAPIs;
import applications.tools.AppInstallTime;
import applications.tools.BuildVisualPane;
import circularOrbit.PersonalAppEcosystem;
import constant.Regex;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import manager.PersonalAppManager;
import physicalObject.App;
import physicalObject.AppFactory;

public class DurationController {

    @FXML
    private TextField name;

    @FXML
    private TextField company;

    @FXML
    private TextField version;

    @FXML
    private TextField area;

    @FXML
    private TextArea description;

    @FXML
    private Label erroradd;

    @FXML
    private TextField removename;

    @FXML
    private Label errorremove;

    @FXML
    private Label entropy;
    
    @FXML
    private TextField index;

    @FXML
    private StackPane vispane;

    private PersonalAppEcosystem current;
	private PersonalAppEcosystem tmp;
	private PersonalAppManager pam;
	private Stage stage;
	private AppFactory af = new AppFactory();
	private List<App> adda = new ArrayList<App>();
	private List<AppInstallTime> addt = new ArrayList<AppInstallTime>();
	private List<App> removea = new ArrayList<App>();
	private PAMenuController pac;
	private Logger logger = Logger.getLogger(Duration.class);
	
    @FXML
    void handleAdd(ActionEvent event) {
    	if(checkAdd()) {
			App a = (App) af.create("App ::= <"+name.getText()+","+company.getText()+","+version.getText()+",\""+description.getText()+"\",\""+area.getText()+"\">");
			adda.add(a);
			AppInstallTime at = new AppInstallTime();
			at.add(tmp.duration());
			addt.add(at);
			tmp.addApp(a, Integer.parseInt(index.getText()),at);
			refreshVis();
		}
    }

    @FXML
    void handleCancel(ActionEvent event) {
    	stage.close();
    }

    @FXML
    void handleRemove(ActionEvent event) {
    	if(checkRemove()) {
			App a = tmp.getApp(removename.getText());
			removea.add(a);
			tmp.remove(a);
			refreshVis();
			
		}
    }

    @FXML
	void handleSave(ActionEvent event) {
		if(!adda.isEmpty()) {
			pam.addAll(adda, addt);
			logger.info("User add new apps: "+adda.toString());
		}
		if(!removea.isEmpty()) {
			pam.removeAll(removea);
			logger.info("User remove a app: "+removea.toString());
		}
		pac.editAppEco(tmp);
		stage.close();
	}
    
    public PersonalAppEcosystem getNewEco() {
    	return tmp;
    }
	
	@FXML
	void initialize() {
	}
	
	private boolean checkAdd() {
		if(!name.getText().matches(Regex.REGEX_LABEL)) {
			erroradd.setText("Name should be label type, try again");
			return false;
		}
		if(pam.getApp(name.getText()) != null) {
			erroradd.setText("App already exist, try again");
			return false;
		}
		if(!company.getText().matches(Regex.REGEX_LABEL)) {
			erroradd.setText("Company should be label type, try again");
			return false;
		}
		if(!version.getText().matches(Regex.APPECO_VERSION)) {
			erroradd.setText("Version illegal type, try again");
			return false;
		}
		if(!description.getText().matches(Regex.REGEX_SENTENCE)) {
			erroradd.setText("Description should be sentence type, try again");
			return false;
		}
		if(!area.getText().matches(Regex.REGEX_SENTENCE)) {
			erroradd.setText("Area should be sentence type, try again");
			return false;
		}
		if(!index.getText().matches("^\\d+$")) {
			erroradd.setText("Track Number illegal, please try again!");
			return false;
		}
		if(!tmp.contains(Double.parseDouble(index.getText()))) {
			erroradd.setText("Track not exists, please try again!");
			return false;
		}
		erroradd.setText("");
		return true;
	}
	
	private boolean checkRemove() {
		if(!removename.getText().matches(Regex.REGEX_LABEL)) {
			errorremove.setText("Name should be label type, try again");
			return false;
		}
		if(tmp.getApp(removename.getText()) == null) {
			errorremove.setText("App not exist, try again");
			return false;
		}
		errorremove.setText("");
		return true;
	}
	
	public void refreshVis() {
		vispane.getChildren().clear();
		vispane.getChildren().add(BuildVisualPane.drawCircularOrbit(tmp, vispane.getPrefWidth(), vispane.getPrefHeight()));
		entropy.setText(String.valueOf(new CircularOrbitAPIs().getObjectDistributionEntropy(tmp)));
	}

	public void setPersonalAppEcosystem(PersonalAppEcosystem pae) {
		this.current = pae;
		tmp = current.clone();
	}
	
	public void setPersonalAppManager(PersonalAppManager pam) {
		this.pam = pam;
	}
	
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	public void setPAMenuController(PAMenuController controller) {
		this.pac = controller;
	}

}
