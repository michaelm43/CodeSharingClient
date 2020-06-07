package GUI;

import java.io.IOException;

import Logic.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NewFileController {

	@FXML TextField txtFileName;
	
	private User user;
	private Project proj;
	private final Stage newFileStage;
	
	public NewFileController(Stage stage, User user) {
		this.newFileStage = stage;
		this.user = user;
		
		// Load the FXML file
        try {    		     	
            FXMLLoader loader = new FXMLLoader(getClass().getResource("NewFileLayout.fxml"));

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
		if(!txtFileName.getText().isEmpty()) {
			newFileStage.close();
			proj = new Project(txtFileName.getText(), user.getEmail());
			user.addProject(txtFileName.getText().concat("-").concat(user.getEmail()));
			EditorController editorController = new EditorController(user, proj);
			editorController.showStage();
		}
	}
	
	
	/*
	 * New File -> Open File
	 * user want to open an existing file
	 * TODO upload relevant file names (by user permisions) and show them as a list to the user
	 */
	@FXML
	public void OpenPageAction() throws IOException {
//		openFileScene = new Scene(FXMLLoader.load(getClass().getResource("OpenFileLayout.fxml")),300,150);
//		window = (Stage)((Node)event.getSource()).getScene().getWindow();
//		window.setScene(openFileScene);
//		window.show();
	}
}
