package applications.view;

import java.io.File;

import org.apache.log4j.Logger;

import applications.MainApp_GUI;
import circularOrbit.AtomStructure;
import exception.ElementLabelDuplicationException;
import exception.FileFormatException;
import exception.IllegalElementFormatException;
import exception.IncorrectElementDependencyException;
import exception.IncorrectElementLabelOrderException;
import exception.LackOfComponentException;
import exception.NoSuchElementException;
import exception.TrackNumberOutOfRangeException;
import exception.UndefinedElementException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import manager.PersonalAppManager;
import manager.TrackGameManager;
import parser.AtomStructureParser;

public class LoginController {
	private MainApp_GUI mainapp;

	@FXML
	private Button btn_trackgame;

	@FXML
	private Button btn_atomstructure;

	@FXML
	private Button btn_appeco;
	
	private Logger logger = Logger.getLogger(LoginController.class);

	@FXML
	private void handleOpenFile_TG() { 
		boolean f = true;
		FileChooser fc = new FileChooser();
		fc.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"));
		TrackGameManager tgm = new TrackGameManager();
		File file = fc.showOpenDialog(mainapp.getStage());
		String filepath = "";
		if (file != null) {
			filepath = file.getPath();
		}
		else {
			return;
		}
		try {
			tgm.initial(filepath);
		} catch (IllegalElementFormatException | NoSuchElementException | IncorrectElementLabelOrderException
				| LackOfComponentException | ElementLabelDuplicationException | TrackNumberOutOfRangeException e) {
			initFileError(file, e);
			f = false;
		}
		logger.info("User open the TrackGame: "+filepath);
		if(f) mainapp.initTGMenu(tgm);
	}

	@FXML
	private void handleOpenFile_AS() {
		boolean f = true;
		FileChooser fc = new FileChooser();
		fc.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"));
		File file = fc.showOpenDialog(mainapp.getStage());
		String filepath = "";
		if (file != null) {
			filepath = file.getPath();
		}else {
			return;
		}
		AtomStructure as = null;
		AtomStructureParser asp = new AtomStructureParser();
		try {
			as = asp.initial(filepath);
		} catch (NoSuchElementException | IllegalElementFormatException | TrackNumberOutOfRangeException
				| IncorrectElementLabelOrderException | IncorrectElementDependencyException | LackOfComponentException
				| ElementLabelDuplicationException e) {
			f = false;
			initFileError(file,e);
		}
		logger.info("User open the AtomStructure: "+filepath);
		if(f) mainapp.initASMenu(as);

	}

	@FXML
	private void handleOpenFile_AE() {
		boolean f = true;
		FileChooser fc = new FileChooser();
		fc.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"));
		File file = fc.showOpenDialog(mainapp.getStage());
		String filepath = "";
		if (file != null) {
			filepath = file.getPath();
		}else {
			return;
		}
		PersonalAppManager pam = new PersonalAppManager();
		try {
			pam.initial(filepath);
		} catch (UndefinedElementException | IllegalElementFormatException | NoSuchElementException
				| LackOfComponentException | ElementLabelDuplicationException | IncorrectElementDependencyException e) {
			f = false;
			initFileError(file,e);
		}
		logger.info("User open the PersonalAppEcosystem: "+filepath);
		if(f) mainapp.initPAMenu(pam);
	}
	
	@FXML
    void handleLog(ActionEvent event) {
		mainapp.initLogSystem();
    }

	public void setMainApp(MainApp_GUI mainapp) {
		this.mainapp = mainapp;
	}

	private void initFileError(File file, FileFormatException e) {
		mainapp.initFileErroe(file, e);
	}

}
