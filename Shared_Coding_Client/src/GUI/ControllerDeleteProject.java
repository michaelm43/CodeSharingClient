package GUI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import HttpRequests.ActionRequest;
import Logic.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ControllerDeleteProject {

	private Stage deleteProjectStage;
	private List<RadioButton> radioButtonFiles;
	private User user;
	private ControllerEditor controllerEditor;
	
	@FXML private VBox vboxCheckList;
	@FXML private Button btnDelete;
	@FXML private Button btnCancel;
	
	public ControllerDeleteProject(ControllerEditor controllerEditor, User user) {
		this.deleteProjectStage = new Stage();
		this.user = user;
		this.controllerEditor = controllerEditor;
		this.vboxCheckList = new VBox();
		
		this.radioButtonFiles= new ArrayList<>();
		for(int i = 0; i < user.getProjectList().size(); i++) 
			radioButtonFiles.add(new RadioButton(user.getProjectList().get(i)));

		
		// Load the FXML file
        try {    		     	
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LayoutDeleteProject.fxml"));

            // Set this class as the controller
            loader.setController(this);

            // Load the scene
            deleteProjectStage.setScene(new Scene(loader.load()));

            // Setup the window/stage
            deleteProjectStage.setTitle("delete projects");

        } catch (IOException e) {
            e.printStackTrace();
        }
        
		this.vboxCheckList.getChildren().addAll(radioButtonFiles);
		deleteProjectStage.initModality(Modality.APPLICATION_MODAL);
		deleteProjectStage.setResizable(false);
	}
	 
	
	public void showStage() {
		this.deleteProjectStage.showAndWait();
	}
	
	@FXML
	public void deleteFiles() throws IOException {
		if(ConfirmBox.display("WARNING", "are you sure you want to delete selected project?!?")) {
			for(int i=0;i<radioButtonFiles.size();i++)
				if(radioButtonFiles.get(i).isSelected()) {
					this.user.removeProject(radioButtonFiles.get(i).getText());
					new ActionRequest().deleteProject(user, controllerEditor.getProj());
					this.controllerEditor.setUser(user);
					if(radioButtonFiles.get(i).getText().equals(this.controllerEditor.getProj().getKey()))
						this.controllerEditor.setDeleted(true);
				}
			this.deleteProjectStage.close();
		}
	}
	
	@FXML 
	public void cancel() throws IOException {
		this.deleteProjectStage.close();
	}
}
