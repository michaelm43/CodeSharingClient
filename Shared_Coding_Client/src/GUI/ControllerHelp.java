package GUI;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ControllerHelp {
	
	private Stage helpStage;

	@FXML private TextArea txtOverall;
	@FXML private TextArea txtHowWorks;
	@FXML private TextArea txtUsingApp;
	
	
	public ControllerHelp(){
		this.helpStage = new Stage();
		
		System.out.println("in construct");
		
		// Load the FXML file
		try{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("LayoutHelp.fxml"));
			
			// Set this class as the controller
			loader.setController(this);

			// Load the scene
			helpStage.setScene(new Scene(loader.load()));

			helpStage.initModality(Modality.APPLICATION_MODAL);

			// Setup the window/stage
			helpStage
			.setTitle("HELP");
			
			initTxtOverall();
			initTxtHowWorks();
			initTxtUsingApp();
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showStage() {
		this.helpStage.showAndWait();
	}

	private void initTxtOverall() {
		this.txtOverall.setText("Shared Coding is an application that help you work in a collaborative inviroment with your partners.\n"
				);
	}

	private void initTxtHowWorks() {
		this.txtHowWorks.setText("As a part of a collaborative group you can choose where you want to work on the file\n"
				+ "after you chose the place you want to work on, the server locks this place that other user will not be able to edit this place at the same time.\n"
				+ "you can edit the file (in a java program laguage, and when you finished click on 'send' button\n"
				+ "the server will unlock the place you edited and will push all other users your code.\n"
				+ "if you wrote a code that add compilation error to the initial code, yout code will be added to the file as a comment.\n"
				);
	}



	private void initTxtUsingApp() {
		this.txtUsingApp.setText("on the right top corner of the screen you have file button\n"
				+ "in the file button you can create:\n"
				+ "	-new File\n"
				+ "	-open an existing file\n"
				+ " -logout\n"
				+ "	-change your password\n"
				+ "	-delete files from your library\n"
				+ " -exit the project\n\n"
				+ "in addition, you can run the project in the 'run' button on the top of the screen and see the result at the bottom console\n"
				+ "and you can add a user to a project, just provide a correct email of someone in the system and be sure you dont add someone you dont know");
	}
}
