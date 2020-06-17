package GUI;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AddUserBox {
	
	private static String email;
	
	public static String display() {
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("choose user email");
		window.setMinWidth(250);
		
		Label errMsg = new Label();
		errMsg.setVisible(false);
		errMsg.setTextFill(Color.RED);
				
		TextField tfEmail = new TextField();
		
		
		Button btnYes = new Button("ADD");
		Button btnNo = new Button("Cancel");
		
		btnYes.setOnAction(e->{
			email = tfEmail.getText();
			window.close();
		});
		btnNo.setOnAction(e->{
			email = "";
			window.close();
		});
		
		VBox layout = new VBox(10);
		HBox buttons= new HBox(10);
		
		buttons.getChildren().addAll(btnNo,btnYes);
		buttons.setAlignment(Pos.BOTTOM_CENTER);
		
		layout.getChildren().addAll(label,tfEmail,buttons);
		layout.setAlignment(Pos.CENTER);
		
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
		
		return email;
	}
}
