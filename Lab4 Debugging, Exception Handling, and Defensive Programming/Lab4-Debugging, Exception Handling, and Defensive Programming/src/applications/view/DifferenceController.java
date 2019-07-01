package applications.view;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import APIs.CircularOrbitAPIs;
import applications.tools.BuildVisualPane;
import applications.tools.Timespan;
import circularOrbit.PersonalAppEcosystem;
import constant.Regex;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import manager.PersonalAppManager;

public class DifferenceController {

    @FXML
    private StackPane vispane1;

    @FXML
    private StackPane vispane2;

    @FXML
    private TextArea difference;

    @FXML
    private DatePicker sd;

    @FXML
    private TextField st;

    @FXML
    private DatePicker ed;

    @FXML
    private TextField et;
    
    @FXML
    private DatePicker sd1;

    @FXML
    private TextField st1;

    @FXML
    private DatePicker ed1;

    @FXML
    private TextField et1;

    
    @FXML
    private Label error;


    private PersonalAppManager pam;
    private Stage stage;
    
    @FXML
    void handleCancel(ActionEvent event) {
    	stage.close();
    }

    @FXML
    void handleFind(ActionEvent event) {
    	if(!st.getText().matches(Regex.APPECO_TIME)) {
    		error.setText("Start time should be hh:mm:ss");
    		return;
    	}
    	if(!et.getText().matches(Regex.APPECO_TIME)) {
    		error.setText("End time should be hh:mm:ss");
    		return;
    	}
    	if(!st1.getText().matches(Regex.APPECO_TIME)) {
    		error.setText("Start time should be hh:mm:ss");
    		return;
    	}
    	if(!et1.getText().matches(Regex.APPECO_TIME)) {
    		error.setText("End time should be hh:mm:ss");
    		return;
    	}
    	String s_time = st.getText();
    	String e_time = et.getText();
    	String s_date, e_date;
    	String s_time1 = st1.getText();
    	String e_time1 = et1.getText();
    	String s_date1, e_date1;
    	if(sd.getValue()!= null && ed.getValue()!= null) {
    		s_date = sd.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString();
    		e_date = ed.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString();
    	}
    	else {
    		error.setText("Please choose a date");
    		return;
    	}
    	if(sd1.getValue()!= null && ed1.getValue()!= null) {
    		s_date1 = sd1.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString();
    		e_date1 = ed1.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString();
    	}
    	else {
    		error.setText("Please choose a date");
    		return;
    	}
    	error.setText("");
    	
    	Instant start = Instant.parse(s_date+"T"+s_time+"Z");
    	Instant end = Instant.parse(e_date+"T"+e_time+"Z");
    	Instant start1 = Instant.parse(s_date1+"T"+s_time1+"Z");
    	Instant end1 = Instant.parse(e_date1+"T"+e_time1+"Z");
    	if(start.isAfter(end)) {
    		error.setText("Start should early than End");
    		return;
    	}
    	if(start1.isAfter(end1)) {
    		error.setText("Start should early than End");
    		return;
    	}
    	List<Timespan> tl = new ArrayList<Timespan>();
    	tl.add(new Timespan(start, end));
    	tl.add(new Timespan(start1, end1));
    	List<PersonalAppEcosystem> li = pam.getTimspanAppEco(tl);
    	difference.clear();
    	difference.setText(new CircularOrbitAPIs().getDifference(li.get(0),li.get(1)).toString());
    	vispane1.getChildren().clear();
    	vispane2.getChildren().clear();
    	vispane1.getChildren().add(BuildVisualPane.drawCircularOrbit(li.get(0), vispane1.getPrefWidth(), vispane1.getPrefHeight()));
    	vispane2.getChildren().add(BuildVisualPane.drawCircularOrbit(li.get(1), vispane2.getPrefWidth(), vispane2.getPrefHeight()));
    }
    
    public void setStage(Stage stage) {
    	this.stage = stage;
    }
    
    public void setPersonalAppManager(PersonalAppManager pam)  {
    	this.pam = pam;
    }

}
