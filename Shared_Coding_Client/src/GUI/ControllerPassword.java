package GUI;

import java.io.IOException;

import HttpRequests.UserRequests;
import Logic.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ControllerPassword {
	
	private final Stage changePasswordStage;
	private User user;
	private ControllerEditor editor;
	
	@FXML private PasswordField txtOldPassword;
	@FXML private PasswordField txtNewPassword;

	@FXML private Label lblErrorMessage;
	
	public ControllerPassword(ControllerEditor editor ,User user) {
		this.changePasswordStage = new Stage();
		this.user = user;
		this.editor = editor;
		
		// Load the FXML file
        try {    		     	
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LayoutPassword.fxml"));

            // Set this class as the controller
            loader.setController(this);

            // Load the scene
            changePasswordStage.setScene(new Scene(loader.load()));

            // Setup the window/stage
            changePasswordStage.setTitle("change password");

        } catch (IOException e) {
            e.printStackTrace();
        }
        
		changePasswordStage.initModality(Modality.APPLICATION_MODAL);
		changePasswordStage.setResizable(false);
	}
	 
	
	public void showStage() {
		this.changePasswordStage.show();
	}

	
	@FXML
	public void changePassword(){
		new ControllerRegister();

		if(user.getPassword().equals(txtOldPassword.getText())) {
			if(ControllerRegister.isValidPassword(txtNewPassword.getText()) &&
					txtOldPassword.getText()!=txtOldPassword.getText()) {
				user.setPassword(txtNewPassword.getText());
				new UserRequests().editUser(user);
				this.changePasswordStage.close();
				editor.setUser(user);
			}
			else {
				lblErrorMessage.setText("the new password is not valid");
				lblErrorMessage.setVisible(true);
			}
		}
		else {
			lblErrorMessage.setText("the password is incorect");
			lblErrorMessage.setVisible(true);
		}
	}
	
	@FXML
	public void cancel() {
		this.changePasswordStage.close();
	}

}
