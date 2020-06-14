package GUI;

import java.io.IOException;

import Logic.Project;
import Logic.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControllerOpenFile {
	
	@FXML ComboBox<String> cbFileNames;
	
	private User user;
	private Project proj;
	private final Stage openFileStage;
	
	public ControllerOpenFile(User user) {
		this.openFileStage = new Stage();
		this.user = user;
		
		// Load the FXML file
        try {    		     	
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LayoutOpenFIle.fxml"));

            // Set this class as the controller
            loader.setController(this);

            // Load the scene
            openFileStage.setScene(new Scene(loader.load(),300,150));

            // Setup the window/stage
            openFileStage.setTitle("open file");

        } catch (IOException e) {
            e.printStackTrace();
        }
        
        cbFileNames.getItems().addAll(user.getProjectList());
	}
	
	@FXML
	public void OpenFile() {
		
	}
	
	@FXML
	public void Cancel() {
		
	}
}
