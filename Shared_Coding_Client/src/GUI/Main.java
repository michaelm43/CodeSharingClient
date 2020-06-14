package GUI;

import java.io.IOException;
//import java.net.Socket;
import java.net.UnknownHostException;

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
	public void start(Stage stage) throws UnknownHostException, IOException {
		
		//Socket soket = new Socket("10.0.1.26", 8089);
		
		ControllerLogin loginController = new ControllerLogin();
		
		loginController.showStage();
	}	
}
