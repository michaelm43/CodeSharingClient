package GUI;

import java.io.IOException;

import HttpRequests.ElementRequest;
import HttpRequests.UserRequests;
import Logic.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControllerNewFile {

	@FXML TextField txtFileName;
	@FXML Label lblErrorMessage;
	
	private User user;
	private Project proj;
	private final Stage newFileStage;
	private final Stage editorStage;
	
	public ControllerNewFile(Stage stage, User user,Stage editorStage) {
		this.newFileStage = stage;
		this.user = user;
		this.editorStage = editorStage;
		
		
		// Load the FXML file
        try {    		     	
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LayoutNewFile.fxml"));

            // Set this class as the controller
            loader.setController(this);

            // Load the scene
            newFileStage.setScene(new Scene(loader.load(),300,150));

            // Setup the window/stage
            newFileStage.setTitle("New file");

        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	
	/*
	 * New File -> Editor
	 * open a new file with the file name.
	 * default name "Application"
	 * TODO update database
	 */
	@FXML
	public void NewFileAction() throws IOException {
		if(txtFileName.getText().isEmpty()) {
			lblErrorMessage.setText("File name can't be empty");
			lblErrorMessage.setVisible(true);
		}
		else {
			proj = new Project(txtFileName.getText(), user.getEmail());
			if(new ElementRequest().openNewFile(user,proj)) {
				newFileStage.close();
				String fileKey = user.getEmail().concat("-").concat(txtFileName.getText());
				user.addProject(fileKey);
				new UserRequests().editUser(user);
				ControllerEditor editorController = new ControllerEditor(user, proj,this.editorStage);
				editorController.showStage();
			}
			else {
				lblErrorMessage.setText("File name already exist");
				lblErrorMessage.setVisible(true);
			}
		}
	}
	
	
	/*
	 * New File -> Open File
	 * user want to open an existing file
	 * TODO upload relevant file names (by user permisions) and show them as a list to the user
	 */
	@FXML
	public void OpenPageAction() throws IOException {
		ControllerOpenFile openfileController = new ControllerOpenFile(user,this.editorStage);
		openfileController.showStage();
	}
}
