package GUI;

import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBox {
	
	static boolean answer;
	
	public static Boolean display(String title, String message) {
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMinWidth(250);
		
		Label label = new Label();
		label.setText(message);
		
		Button btnYes = new Button("Yes");
		Button btnNo = new Button("No");
		
		btnYes.setOnAction(e->{
			answer = true;
			window.close();
		});
		btnNo.setOnAction(e->{
			answer = false;
			window.close();
		});
		
		VBox layout = new VBox(10);
		HBox buttons= new HBox(10);
		
		buttons.getChildren().addAll(btnYes,btnNo);
		
		layout.getChildren().addAll(label,buttons);
		layout.setAlignment(Pos.CENTER);
		
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
		
		return answer;
	}
}
