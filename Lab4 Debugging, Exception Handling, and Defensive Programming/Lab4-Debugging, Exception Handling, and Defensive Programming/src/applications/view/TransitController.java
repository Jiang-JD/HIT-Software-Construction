package applications.view;

import applications.MainApp_GUI;
import circularOrbit.AtomStructure;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TransitController {

    @FXML
    private TextField start;

    @FXML
    private TextField end;

    @FXML
    private TextField num;
    
    @FXML
    private Label error;

    
    private AtomStructure as;
    private boolean isOK = false;
    private Stage stage;

    @FXML
    void handleCancel(ActionEvent event) {
    	isOK = false;
    	stage.close();
    }

    @FXML
    void handleTransit(ActionEvent event) {
    	if(!start.getText().matches("^\\d+$")) {
    		error.setText("Illegal start format, should be integer >= 0, try again");
    		return;
    	}
    	if(Integer.parseInt(start.getText()) >= as.getTrackNum()) {
    		error.setText("Index out of range, try again");
    		return;
    	}
    	if(!end.getText().matches("^\\d+$")) {
    		error.setText("Illegal end format, should be integer >= 0, try again");
    		return;
    	}
    	if(Integer.parseInt(end.getText()) >= as.getTrackNum()) {
    		error.setText("Index out of range, try again");
    		return;
    	}
    	if(!num.getText().matches("^\\d+$")) {
    		error.setText("Illegal Number format, should be integer >= 0, try again");
    		return;
    	}
    	if(Integer.parseInt(num.getText()) > as.getObjects(Integer.parseInt(start.getText())).size()) {
    		error.setText("Transit too much electrons, try again");
    		return;
    	}
    	error.setText("");
    	isOK = true;
    	stage.close();
    }
    
    public void setStage(Stage stage)  {
    	this.stage = stage;
    }
    
    public void setAS(AtomStructure as) {
    	this.as = as;
    }
    
    public boolean isOK_Clicked() {
    	return isOK;
    }
    
    public int getStartIndex() {
    	return Integer.parseInt(start.getText());
    }
    
    public int getEndIndex() {
    	return Integer.parseInt(end.getText());
    }
    
    public int getEleNumber() {
    	return Integer.parseInt(num.getText());
    }

}