package GUI;

import java.io.IOException;
import java.util.regex.Pattern;

import HttpRequests.UserRequests;
import Logic.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ControllerRegister {

	private final Stage registerStage;
	
	@FXML private PasswordField txtPassword;
	@FXML private Label lblErrorMessage;
	@FXML private TextField txtEmail;
	@FXML private PasswordField txtVerifyPassword;
	
	public ControllerRegister() {
		this.registerStage = new Stage();
		
		// Load the FXML file
        try {    		     	
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LayoutRegister.fxml"));

            // Set this class as the controller
            loader.setController(this);

            // Load the scene
            registerStage.setScene(new Scene(loader.load()));

            // Setup the window/stage
            registerStage.setTitle("Register");

        } catch (IOException e) {
            e.printStackTrace();
        }
        
		registerStage.initModality(Modality.APPLICATION_MODAL);
		registerStage.setResizable(false);
	}
	
	
	@FXML
	public void exitAction() throws IOException{
		registerStage.close();
	}
	
	/*
	 * Register -> Login
	 * 2 cases:
	 * TODO 1) user field all the information correctly and pushed register. 
	 * TODO		update data base with new user.
	 * TODO 2) user dont want to register and pushed cancel button.
	 * 
	 */
	@FXML
	public void loginPageAction() throws IOException {
		
		if(!isValidEmail(txtEmail.getText()) || !isValidPassword(txtPassword.getText())) {
			lblErrorMessage.setText("Invalid email or password");
			lblErrorMessage.setVisible(true);
		}
		else if(!isMatchedPassword(txtPassword.getText(), txtVerifyPassword.getText())) {
			lblErrorMessage.setText("confirmation password not match");
			lblErrorMessage.setVisible(true);
		}
		else {
			if(new UserRequests().registerUser(new User(txtEmail.getText(),txtPassword.getText())))
				registerStage.close();
			else {
				lblErrorMessage.setText("User already exist");
				lblErrorMessage.setVisible(true);
			}
		}
	}
	
	public void showStage() {
		registerStage.showAndWait();
	}
	
	
	//--------------------------------------- Validation methods -------------------------------------
	//check comit
	
	/* 
	 * check that the email is from the pattern: email@gmail.com
	 */
	public static boolean isValidEmail(String email) 
    { 
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ 
                            "[a-zA-Z0-9_+&*-]+)*@" + 
                            "(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
                            "A-Z]{2,7}$"; 
                              
        Pattern pat = Pattern.compile(emailRegex); 
        if (email == null) 
            return false; 
        return pat.matcher(email).matches(); 
    } 
	
	
	/*
	 * check that the password is from the pattern:
	 * 		 Minimum eight characters, at least one letter and one number
	 */
	public static boolean isValidPassword(String psw) {
		String pswRegex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,12}$";
		Pattern pat = Pattern.compile(pswRegex);
		if(psw == null)
			return false;
		return pat.matcher(psw).matches();
	}


	/*
	 * check that the password and the verification password match
	 */
	public static boolean isMatchedPassword(String psw, String varifyPsw) {
		return psw.equals(varifyPsw);
	}
}
