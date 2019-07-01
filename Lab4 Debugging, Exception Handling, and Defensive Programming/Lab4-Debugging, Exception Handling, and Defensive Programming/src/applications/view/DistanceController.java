package applications.view;

import constant.Regex;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import manager.PersonalAppManager;

public class DistanceController {

    @FXML
    private TextField fapp;

    @FXML
    private TextField sapp;

    @FXML
    private TextField distance;

    @FXML
    private Label error;
    
    private PersonalAppManager pam;
    private Stage stage;
    
    @FXML
    void handleCal(ActionEvent event) {
    	if(!fapp.getText().matches(Regex.REGEX_LABEL)) {
			error.setText("Name should be label type, try again");
			return;
		}
		if(pam.getApp(fapp.getText()) == null) {
			error.setText("First App not exist, try again");
			return;
		}
		if(!sapp.getText().matches(Regex.REGEX_LABEL)) {
			error.setText("Name should be label type, try again");
			return;
		}
		if(pam.getApp(sapp.getText()) == null) {
			error.setText("Second App not exist, try again");
			return;
		}
		error.setText("");
		distance.setText(String.valueOf(pam.getDistance(pam.getApp(fapp.getText()), pam.getApp(sapp.getText()))));
    }

    @FXML
    void handleCancel(ActionEvent event) {
    	stage.close();
    }
    
    public void setStage(Stage stage) {
    	this.stage = stage;
    }
    
    public void setPersonalAppManager(PersonalAppManager pam)  {
    	this.pam = pam;
    }

}
