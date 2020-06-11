package GUI;

import java.io.IOException;

import javax.swing.text.Position;

import org.fxmisc.richtext.CodeArea;

import Logic.*;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import Compile.*;

public class EditorController {

	public static String EXIT_TITLE = "Exit Message";
	public static String EXIT_MESSAGE = "Are you sure you want to exit the program?";
	public static String LOGOUT_TITLE = "Logout Message";
	public static String LOGOUT_MESSAGE = "Are you sure you want to logout?";
	
	

	private final Stage editorStage;

	private User user;
	private Project proj;

	//@FXML 
	private TextArea txtEditor;

	@FXML
	private TextArea txtConsole;

	@FXML
	private BorderPane borderPane;

	private int flag;
	
	private int caretpos;
	private int caretLine;
	
	private CodeArea codeArea;
	
	public EditorController(User user, Project proj) {
		this.editorStage = new Stage();
		this.user = user;
		this.proj = proj;
		this.codeArea = new CodeArea();
		

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
			editorStage
					.setTitle(this.proj.getName().concat("_").concat(this.proj.getCreator()));

		} catch (IOException e) {
			e.printStackTrace();
		}
		    
		
		borderPane.setCenter(codeArea);
		//setText();
		
		// txtEditor.setEditable(false);

		editorStage.setOnCloseRequest(e -> {
			e.consume();
			popUpMessage(EXIT_TITLE, EXIT_MESSAGE);
		});

		Runtime rt = Runtime.getRuntime();
		Shutdown sd = new Shutdown();
		rt.addShutdownHook(new Thread(sd));
		flag = 0;
	}

	public void showStage() {
		editorStage.show();
	}

	public void setText() {
		txtEditor.setText(proj.toString());
	}

	/*
	 * Editor -> Exit if the user pushed File->Exit he get a pop up message to check
	 * if he is sure
	 */
	@FXML
	public void exitProgram() throws IOException {
		popUpMessage(EXIT_TITLE, EXIT_MESSAGE);
	}

	/*
	 * check if the user sure he wants to close the program
	 */
	public void popUpMessage(String title, String msg) {
		Boolean isExit = ConfirmBox.display(title, msg);
		if (isExit)
			editorStage.close();
	}

	@FXML 
	public void addUser() throws IOException{
		
	}
	
	@FXML
	public void onRun() throws IOException {
		txtConsole.setVisible(true);
		try {
			txtConsole.setText("COMPILATION....");
			if (flag == 0) {
				Comp c = new Comp(proj.toString(), txtConsole);
				Thread t = new Thread(c, "compile");
				t.start();
				flag = 1; // NOW ITS TIME TO RUN
				
			} else {
				String path_run = this.proj.getName();
				Process p = Runtime.getRuntime().exec("cmd /c " + "cd .&& javaw " + path_run);
				Run r = new Run(txtConsole, p);
				Run1 r1 = new Run1(txtConsole, p);
				Run2 r2 = new Run2(txtConsole, p);
				Thread tr = new Thread(r, "run");
				Thread tr1 = new Thread(r1, "run1");
				Thread tr2 = new Thread(r2, "run2");
				tr.start();
				tr1.start();
				tr2.start();
				flag = 0;
			}
		} catch (Exception e) {
			txtConsole.setText("" + e);
			// System.out.println(e);
		}
	}
	
	/*
	@FXML 
	public void MouseClicked(){
		checkCaretLine();
		try {
			
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}
	*/

	public void checkCaretLine() {
		int maxLine = proj.getNumberOfLines();
		int caretPos = txtEditor.getCaretPosition();
		int tempPos = maxLine/2;
		int line = 0;
		 
		txtEditor.positionCaret(0);
		//while(tempPos>=1) {
			//for(int i = 0; i<3;i++) {
				
		//txtEditor.
		//fireEvent(new KeyEvent(null, txtEditor,KeyEvent.KEY_PRESSED, "", "", KeyCode.DOWN, false, false, false, false));
		//System.out.println(txtEditor.getCaretPosition());
			//}
		//}
		
		
//		return this.caretLine;
	}
	
	
	
	
//	@FXML
//	public void onCompile() throws IOException {
//		try {
//			Comp c = new Comp(proj.getProject().toString(), txtConsole);
//			Thread t = new Thread(c, "compile");
//			t.start();	
//		}
//			catch (Exception e22) {
//			System.out.println(e22);
//		}
//	}
}
