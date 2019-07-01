package applications.view;

import constant.Regex;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import manager.TrackGameManager;

public class ExchangeController {
	
    @FXML
    private TextField number1;

    @FXML
    private TextField number2;

    @FXML
    private Label inputerror;
    
    @FXML
    private Button btn_cancel;

    @FXML
    private Button btn_ok;
    
    private boolean isOK;
    private Stage stage;
    private TrackGameManager tgm;

    @FXML
    void handle_cancel(ActionEvent event) {
    	isOK = false;
    	stage.close();
    }

    @FXML
    void handle_ok(ActionEvent event) {
    	if(!checkInput()) {
    		return;
    	}
    	else {
    		isOK = true;
    		stage.close();
    	}
    }
    
    @FXML
    private void initialize() {
    }
    
    private boolean checkInput() {
    	String n1 = number1.getText();
    	String n2 = number2.getText();
    	if(!n1.matches(Regex.REGEX_NUMBER_SPEC)) {
    		inputerror.setText("First Number Format is wrong, please try again!");
    		return false;
    	}
    	if(!n2.matches(Regex.REGEX_NUMBER_SPEC)) {
    		inputerror.setText("Second Number Format is wrong, please try again!");
    		return false;
    	}
    	if(Double.parseDouble(n1) != (int)Double.parseDouble(n1)) {
    		inputerror.setText("First Number Format is wrong, please try again!");
    		return false;
    	}
    	if(Double.parseDouble(n2) != (int)Double.parseDouble(n2)) {
    		inputerror.setText("Second Number Format is wrong, please try again!");
    		return false;
    	}
    	if(tgm.getAthlete(Integer.parseInt(n1)) == null) {
    		inputerror.setText("First Athlete not exist, please try again!");
    		return false;
    	}
    	if(tgm.getAthlete(Integer.parseInt(n2)) == null) {
    		inputerror.setText("Second Athlete not exist, please try again!");
    		return false;
    	}
    	return true;
    }
    
    public boolean isOK_Clicked() {
    	return isOK;
    }
    
    public void setStage(Stage stage) {
    	this.stage = stage;
    }
    
    public void setTrackGameManager(TrackGameManager tgm) {
    	this.tgm = tgm;
    }
    
    public int getnum1() {
    	return Integer.parseInt(number1.getText());
    }
    
    public int getnum2() {
    	return Integer.parseInt(number2.getText());
    }

}
