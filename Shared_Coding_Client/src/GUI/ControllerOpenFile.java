package GUI;

import java.io.IOException;

import HttpRequests.ElementRequest;
import Logic.Project;
import Logic.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class ControllerOpenFile {
	
	@FXML ComboBox<String> cbFileNames;
	
	private User user;
	private Project proj;
	private final Stage openFileStage;
	private final Stage editorStage;
	private final Stage newFileStage;
	
	public ControllerOpenFile(User user,Stage editorStage,Stage newFileStage) {
		this.openFileStage = new Stage();
		this.editorStage = editorStage;
		this.newFileStage = newFileStage;
			
		this.user = user;
		
		// Load the FXML file
        try {    		     	
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LayoutOpenFIle.fxml"));

            // Set this class as the controller
            loader.setController(this);

            // Load the scene
            openFileStage.setScene(new Scene(loader.load()));

            // Setup the window/stage
            openFileStage.setTitle("open file");

        } catch (IOException e) {
            e.printStackTrace();
        }
        	cbFileNames.getItems().addAll(this.user.getProjectList());
        	//set the first project in list	
        	if(!this.user.getProjectList().isEmpty())
        		cbFileNames.setValue(this.user.getProjectList().get(0));
	}
	
	public void showStage() {
		openFileStage.showAndWait();
	}
	
	@FXML
	public void openFile() {
		String fileKey = cbFileNames.getValue();
		
		proj = new ElementRequest().openExistingFile(user,fileKey);
		if(proj != null) {
			//TODO shay: add to active Users (LOGIN) 
			ControllerEditor editorController = new ControllerEditor(user, proj,this.editorStage);
			this.openFileStage.close();
			if(this.newFileStage!=null)
				this.newFileStage.close();
			editorController.showStage();
		}
	}
	
	@FXML
	public void cancel() {
		openFileStage.close();
	}
}
