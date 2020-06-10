package GUI;

import java.io.IOException;

import Logic.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EditorController {

	public static String EXIT_TITLE = "Exit Message";
	public static String EXIT_MESSAGE = "Are you sure you want to exit the program?";
	public static String LOGOUT_TITLE = "Logout Message";
	public static String LOGOUT_MESSAGE = "Are you sure you want to logout?";
	
	
	private final Stage editorStage;
	
	private User user; 
	private ProjectWithFields proj;
	
	//@FXML private TextArea txtEditor;
	@FXML private VBox vBoxEditor;
	
	
	public EditorController(User user, Project proj) {
		this.editorStage = new Stage();
		this.user = user;
		this.proj = new ProjectWithFields(proj);
		
		// Load the FXML file
        try {    		     	
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EditorLayout.fxml"));

            // Set this class as the controller
            loader.setController(this);

            // Load the scene
            editorStage.setScene(new Scene(loader.load()));
            
			editorStage.setMaximized(true);
			editorStage.initModality(Modality.APPLICATION_MODAL);

            // Setup the window/stage
            editorStage.setTitle(this.proj.getProject().getName().concat("_").concat(this.proj.getProject().getCreator()));
            vBoxEditor.setBackground(new Background(new BackgroundFill(Color.valueOf("#383838"), CornerRadii.EMPTY, Insets.EMPTY)));
            

        } catch (IOException e) {
            e.printStackTrace();
        }
        
        setText();
   //     txtEditor.setEditable(false);
        
		editorStage.setOnCloseRequest(e->{
			e.consume();
			popUpMessage(EXIT_TITLE, EXIT_MESSAGE);
		});
	}
	
	
	public void showStage() {
		editorStage.show();
	}
	
	
	public void setText() {
        this.proj.putInGui(vBoxEditor);
	}
	
	/*
	 *  Editor -> Exit
	 *  if the user pushed File->Exit he get a pop up message to check if he is sure
	 */
	@FXML
	public void exitProgram() throws IOException {
		popUpMessage(EXIT_TITLE, EXIT_MESSAGE);
	}
	
	
	/*
	 * check if the user sure he wants to close the program
	 */
	public void popUpMessage(String title, String msg) {
		Boolean isExit = ConfirmBox.display(title,msg);
		if(isExit)
			editorStage.close();
	}
}
