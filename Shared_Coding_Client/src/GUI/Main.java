package GUI;

import java.io.IOException;
//import java.net.Socket;
import java.net.UnknownHostException;

import HttpRequests.ActionRequest;
import HttpRequests.ElementRequest;
import HttpRequests.UserRequests;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
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
		connectView();
	}

	private void connectView() {
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("connect to server");
		window.setMinWidth(250);
		

		TextField tfIP = new TextField("192.168.1.22");
		TextField tfPort = new TextField("8089");
		
		
		Button btnConnect = new Button("Connect");
		
		btnConnect.setOnAction(e->{
			new ActionRequest(tfIP.getText());
			new UserRequests(tfIP.getText());
			new ElementRequest(tfIP.getText());
			
			window.close();
			
			ControllerLogin loginController = new ControllerLogin();
			
			loginController.showStage();
		});
		
		VBox layout = new VBox(10);
		HBox buttons= new HBox(10);
		
		buttons.getChildren().addAll(btnConnect);
		buttons.setAlignment(Pos.BOTTOM_CENTER);
		
		layout.getChildren().addAll(tfIP,tfPort,buttons);
		layout.setAlignment(Pos.CENTER);
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();

	}	
}
