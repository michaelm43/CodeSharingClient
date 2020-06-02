package GUI;
import java.io.IOException;

import java.util.regex.Matcher; 
import java.util.regex.Pattern; 

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Main extends Application{

	public static String EXIT_TITLE = "Exit Message";
	public static String EXIT_MESSAGE = "Are you sure you want to exit the program?";
	public static String LOGOUT_TITLE = "Logout Message";
	public static String LOGOUT_MESSAGE = "Are you sure you want to logout?";
	
	private Parent root;
	//private Register register;
	//private NewFile newFile;
	//private OpenFile openFile;
	
	private Stage window;
	private Scene loginScene;
	private Scene newFileScene;
	private Scene registerScene;
	private Scene openFileScene;
	private Stage editor;

	private Scene editorScene;
	
	@FXML TextArea txtEditor;
	@FXML TextField txtEmail; 
	
	public static void main(String[] args) {
		launch(args);	
	}

	
	/*
	 * Init the app on Login page..
	 */
	@Override
	public void start(Stage stage) throws Exception {
		this.window = stage;
		window.setTitle("Shared Code");
		window.setResizable(false);
		
		root = FXMLLoader.load(getClass().getResource("LoginLayout.fxml"));
				
		loginScene = new Scene(root,300,150);
		window.setScene(loginScene);
		window.show();
	}
	
	/*
	 * Login -> NewFile 
	 * User pushed Login button after he filled email name and password
	 * check on data base if use exist
	 */
	@FXML
	//@checkUser(email = txtEmail.get)
	//@checkPassword
	public void loginAction(ActionEvent event) throws IOException {
		//TODO check fields and user exist in database
		if(!isValidEmail(txtEmail.getText()))
			System.out.println("the email is not valid");
		else {
			newFileScene = new Scene(FXMLLoader.load(getClass().getResource("NewFileLayout.fxml")),300,150);
			window = (Stage)((Node)event.getSource()).getScene().getWindow();
			window.setScene(newFileScene);
			window.show();
		}
	}
	
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
	 *  Login -> Register
	 *  user dont have an account. 
	 *  user pushed the register label
	 */
	@FXML
	public void registerPageAction(MouseEvent event) throws IOException {
		registerScene = new Scene(FXMLLoader.load(getClass().getResource("RegisterLayout.fxml")));
//		window = (Stage)((Node)event.getSource()).getScene().getWindow();
		Stage stage = new Stage();
		stage.setScene(registerScene);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.show();
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
	public void loginPageAction(ActionEvent event) throws IOException {
		window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.close();
	}

	/*
	 * New File -> Editor
	 * open a new file with the file name.
	 * default name "Application"
	 * TODO update database
	 */
	@FXML
	public void NewFileAction(ActionEvent event) throws IOException {
		window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.close();
		
		editor = new Stage();
		editorScene = new Scene(FXMLLoader.load(getClass().getResource("EditorLayout.fxml")));
		editor.setMaximized(true);
		editor.setScene(editorScene);
		editor.initModality(Modality.APPLICATION_MODAL);
		editor.setOnCloseRequest(e->{
			e.consume();
			popUpMessage(EXIT_TITLE, EXIT_MESSAGE);
		});
		editor.show();
	}

	
	/*
	 * New File -> Open File
	 * user want to open an existing file
	 * TODO upload relevant file names (by user permisions) and show them as a list to the user
	 */
	@FXML
	public void OpenPageAction(ActionEvent event) throws IOException {
		openFileScene = new Scene(FXMLLoader.load(getClass().getResource("OpenFileLayout.fxml")),300,150);
		window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(openFileScene);
		window.show();
	}
	
	/*
	 *  Editor -> Exit
	 *  if the user pushed File->Exit he get a pop up message to check if he is sure
	 */
	@FXML
	public void exitProgram(ActionEvent event) throws IOException {
		editor = (Stage) txtEditor.getScene().getWindow();
		popUpMessage(EXIT_TITLE, EXIT_MESSAGE);
	}
	
	/*
	 * check if the user sure he wants to close the program
	 */
	public void popUpMessage(String title, String msg) {
		System.out.println("in pop up message");
		Boolean isExit = ConfirmBox.display(title,msg);
		if(isExit)
			editor.close();
	}
	/////////////
}
