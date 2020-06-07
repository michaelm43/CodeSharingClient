package GUI;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application{
	
	public static void main(String[] args) {
		launch(args);	
	}

	/*
	 * Init the app on Login page..
	 */
	@Override
	public void start(Stage stage) {
		
		LoginController loginController = new LoginController();
		
		loginController.showStage();
	}	
}
