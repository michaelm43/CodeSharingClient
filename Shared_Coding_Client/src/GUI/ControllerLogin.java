package GUI;
import java.io.IOException;
import java.util.regex.Pattern;

import HttpRequests.UserRequests;
import Logic.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ControllerLogin{
	 
	@FXML Label lblErrorMessage;
	@FXML TextField txtEmail;
	@FXML PasswordField txtPassword;
	
	private User user;
	private final Stage loginStage;
	
	
	public ControllerLogin() {
		this.loginStage = new Stage();
		
		// Load the FXML file
        try {    		     	
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LayoutLogin.fxml"));

            // Set this class as the controller
            loader.setController(this);

            // Load the scene
            loginStage.setScene(new Scene(loader.load()));

            // Setup the window/stage
            loginStage.setTitle("Login");

            //TODO delete this lines 
            {
            	txtEmail.setText("m@g.com");
            	txtPassword.setText("michael123");
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	
	public void showStage() {
		loginStage.show();
	}
	
	
	/*
	 * Login -> NewFile 
	 * User pushed Login button after he filled email name and password
	 * check on data base if use exist
	 */
	@FXML
	public void loginAction() throws IOException {
		if(!isValidEmail(txtEmail.getText()) || !isValidPassword(txtPassword.getText())) {
			lblErrorMessage.setText("Invalid email or password");
			lblErrorMessage.setVisible(true);
				
		}
		else {
			user = new UserRequests().loginUser(new User(txtEmail.getText(),txtPassword.getText()));
			if(user != null) 
				new ControllerNewFile(this.loginStage, user,null);
			else {
				lblErrorMessage.setText("The user isn't exist");
				lblErrorMessage.setVisible(true);
			}
		}
	}
	
	/* 
	 *  Login -> Register
	 *  user dont have an account. 
	 *  user pushed the register label
	 */
	@FXML
	public void registerPageAction() throws IOException {
		ControllerRegister registerController = new ControllerRegister();
		
		registerController.showStage();
	}
	
	
	//--------------------------------------- Validation methods -------------------------------------
	
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
}
