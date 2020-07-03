package GUI;

import java.io.IOException;
import java.text.SimpleDateFormat;

import Logic.*;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import Compile.*;
import Compile.Compiler;
import HttpRequests.ActionRequest;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.reactfx.Subscription;

public class ControllerEditor {

	public static String EXIT_TITLE = "Exit Message";
	public static String EXIT_MESSAGE = "Are you sure you want to exit the program?";
	public static String LOGOUT_TITLE = "Logout Message";
	public static String LOGOUT_MESSAGE = "Are you sure you want to logout?";

	private static final String[] KEYWORDS = new String[] { "abstract", "assert", "boolean", "break", "byte", "case",
			"catch", "char", "class", "const", "continue", "default", "do", "double", "else", "enum", "extends",
			"final", "finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface",
			"long", "native", "new", "package", "private", "protected", "public", "return", "short", "static",
			"strictfp", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "try", "void",
			"volatile", "while" };

	private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
	private static final String PAREN_PATTERN = "\\(|\\)";
	private static final String BRACE_PATTERN = "\\{|\\}";
	private static final String BRACKET_PATTERN = "\\[|\\]";
	private static final String SEMICOLON_PATTERN = "\\;";
	private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
	private static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";

	private static final Pattern PATTERN = Pattern.compile(
			"(?<KEYWORD>" + KEYWORD_PATTERN + ")" + "|(?<PAREN>" + PAREN_PATTERN + ")" + "|(?<BRACE>" + BRACE_PATTERN
					+ ")" + "|(?<BRACKET>" + BRACKET_PATTERN + ")" + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
					+ "|(?<STRING>" + STRING_PATTERN + ")" + "|(?<COMMENT>" + COMMENT_PATTERN + ")");

	private static final String sampleCode = String.join("\n",
			new String[] { "package com.example;", "", "import java.util.*;", "",
					"public class Foo extends Bar implements Baz {", "", "    /*", "     * multi-line comment",
					"     */", "    public static void main(String[] args) {", "        // single-line comment",
					"        for(String arg: args) {", "            if(arg.length() != 0)",
					"                System.out.println(arg);", "            else",
					"                System.err.println(\"Warning: empty string as argument\");", "        }", "    }",
					"", "}" });

	private final Stage editorStage;

	private User user;
	private Project proj;

	private CodeArea codeArea;
	private CodeArea editCode;
	private Button btnSend;

	@FXML
	private TextArea txtConsole;
	@FXML
	private BorderPane borderPane;
	@FXML 
	private VBox menuActiveUsers;

	private int caretLine = -1;

	private Stage addCodeStage;

	private boolean isDeleted = false;
	
	private boolean isEdited = false;
	private String originalLine;
	private String beforeChange;
	private int caretCol;
	
	
	public ControllerEditor(User user, Project project, Stage stage) {
		if (stage == null)
			this.editorStage = new Stage();
		else {
			this.editorStage = stage;
			this.editorStage.setMaximized(true);
		}

		this.user = user;
		this.proj = project;
		this.codeArea = new CodeArea();
		this.editCode = new CodeArea();
		this.addCodeStage = new Stage();
		addCodeStage.initModality(Modality.APPLICATION_MODAL);

		// Load the FXML file
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("LayoutEditor.fxml"));

			// Set this class as the controller
			loader.setController(this);

			// Load the scene
			editorStage.setScene(new Scene(loader.load()));

			editorStage.setMaximized(true);
//			editorStage.initModality(Modality.APPLICATION_MODAL);

			// Setup the window/stage
			editorStage.setTitle(this.proj.getName().concat("-").concat(this.proj.getCreator()));

			initCodeArea();
			borderPane.setCenter(new VirtualizedScrollPane<>(codeArea));

		} catch (IOException e) {
			e.printStackTrace();
		}

		editorStage.setOnCloseRequest(e -> {
			e.consume();
			try {
				popUpMessage(EXIT_TITLE, EXIT_MESSAGE);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		Runtime rt = Runtime.getRuntime();
		Shutdown sd = new Shutdown();
		rt.addShutdownHook(new Thread(sd));
	
	}

	private void initCodeArea() {

		// add line numbers to the left of code area
		codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));

		// recompute the syntax highlighting 500 ms after user stops editing
		Subscription cleanupWhenNoLongerNeedIt = codeArea
				// plain changes = ignore style changes that are emitted when syntax
				// highlighting is reapplied
				// multi plain changes = save computation by not rerunning the code multiple
				// times
				// when making multiple changes (e.g. renaming a method at multiple parts in
				// file)
				.multiPlainChanges()
				// do not emit an event until 500 ms have passed since the last emission of
				// previous stream
				.successionEnds(Duration.ofMillis(500))
				// run the following code block when previous stream emits an event
				.subscribe(ignore -> codeArea.setStyleSpans(0, computeHighlighting(codeArea.getText())));
		
		
		/*Subscription saveWhenNoWorking = codeArea
				// plain changes = ignore style changes that are emitted when syntax
				// highlighting is reapplied
				// multi plain changes = save computation by not rerunning the code multiple
				// times
				// when making multiple changes (e.g. renaming a method at multiple parts in
				// file)
				.multiPlainChanges()
				// do not emit an event until 500 ms have passed since the last emission of
				// previous stream
				.successionEnds(Duration.ofMillis(100000))
				// run the following code block when previous stream emits an event
				.subscribe(fireEvent -> {
					if(this.caretLine >= 0)
						Event.fireEvent(codeArea, new KeyEvent(
							KeyEvent.KEY_PRESSED, 
							null, 
							codeArea.getText(codeArea.getCurrentParagraph()), 
							KeyCode.S, 
							false, true, false, false));});*/  //TODO we want this code
		

		// when no longer need syntax highlighting and wish to clean up memory leaks
		// run: `cleanupWhenNoLongerNeedIt.unsubscribe();`

		// auto-indent: insert previous line's indents on enter
		final Pattern whiteSpace = Pattern.compile("^\\s+");
		codeArea.addEventHandler(KeyEvent.KEY_PRESSED, KE -> {
			if (KE.getCode() == KeyCode.ENTER) {
				int caretPosition = codeArea.getCaretPosition();
				int currentParagraph = codeArea.getCurrentParagraph();
				Matcher m0 = whiteSpace.matcher(codeArea.getParagraph(currentParagraph - 1).getSegments().get(0));
				if (m0.find())
					Platform.runLater(() -> codeArea.insertText(caretPosition, m0.group()));
			}
		});

		codeArea.replaceText(0, 0, proj.toString());
		
		getLabelFromString();
		//lock for the first time
		//TODO fix few users on same line
		//new ActionRequest().lockLines(user, this.proj, this.caretLine, 0);
		//codeArea.moveTo(proj.toString().length()-1);
		//TODO CHANGE
		//codeArea.setEditable(false);
		
		//TODO CHANGE
		codeArea.setOnMouseClicked(e -> mouseClicked());
		
		//TODO fix all of that in a method!
		codeArea.setOnKeyPressed(e -> keyPressed(e));
	}
	

	private void getLabelFromString() {
		menuActiveUsers.getChildren().clear();
		ListIterator<ActiveUser> itr = proj.getActiveUsers().listIterator();
		while(itr.hasNext()) {
			menuActiveUsers.getChildren().add(new Label(itr.next().getEmail()));
		}
	}

	
	private static StyleSpans<Collection<String>> computeHighlighting(String text) {
		Matcher matcher = PATTERN.matcher(text);
		int lastKwEnd = 0;
		StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
		while (matcher.find()) {
			String styleClass = matcher.group("KEYWORD") != null ? "keyword"
					: matcher.group("PAREN") != null ? "paren"
							: matcher.group("BRACE") != null ? "brace"
									: matcher.group("BRACKET") != null ? "bracket"
											: matcher.group("SEMICOLON") != null ? "semicolon"
													: matcher.group("STRING") != null ? "string"
															: matcher.group("COMMENT") != null ? "comment" : null;
			/* never happens */ assert styleClass != null;
			spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
			spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
			lastKwEnd = matcher.end();
		}
		spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
		return spansBuilder.create();
	}

	public void showStage() {
		editorStage.show();
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
	public void popUpMessage(String title, String msg) throws IOException {
		Boolean isExit = ConfirmBox.display(title, msg);
		if (isExit) {
			closeStage();
			System.exit(0);
		}
	}
	

	private void closeStage() throws IOException {
		if(this.caretLine >= 0)	{//means no lock yet happened
			if(this.isEdited)
				saveFile();
			new ActionRequest().unlockLines(user, this.proj, 1);
		}
		new ActionRequest().logoutProject(this.user, this.proj);	
		editorStage.close();
	}
	
	private void saveFile() throws IOException {
		String temp = checkErrors(this.codeArea.getText(this.caretLine), this.caretLine,this.proj.getLinesOfCode().get(this.caretLine).getCode(),false);
		this.proj = new ActionRequest().editCode(user, proj, temp);
	}

	public void sendNewCode() throws IOException {
		addCodeStage.close();
		this.proj.setText(this.caretLine, editCode.getText());
		this.codeArea.clear();

		// update server
		this.proj = new ActionRequest().editCode(user, proj,editCode.getText());
		if(this.proj != null) {
			this.proj = new Project(this.proj);
			this.codeArea.replaceText(0, 0, this.proj.toString());
			getLabelFromString();
			this.proj.unLock(this.user,editCode.getText().split("\n").length);
		}
		// fix conflicts	
	}

	@FXML
	public void addUser() throws IOException {
		String email = AddUserBox.display();
		if (email.isEmpty()) {
			popUpError("Cancel button pushed or email field is empty");
		} else if (!ControllerLogin.isValidEmail(email)) {
			popUpError("The email is not Valid");
		} else {
			if (!new ActionRequest().addNewUser(user, proj, email)) 
				popUpError("the email does not exist in the system");
			// The email is valid
		}
	}
	
	public void popUpError(String msg) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("ERROR");
		alert.setHeaderText(null);
		alert.setContentText(msg);
		alert.showAndWait();
	}

	public String compileProgram(String code) throws IOException {
		try {
			Compiler compiler = new Compiler();
			compiler.compile(code, proj.getName());
			return compiler.getCompilerErrorOutput();
		} catch (Exception e) {
			txtConsole.setText("" + e);
			return "error";
		}
	}

	@FXML
	public void onRun() throws IOException {
		txtConsole.setVisible(true);
		String errors = compileProgram(proj.toString());
		if (errors == null) { // RUN THE PROGRAM
			Run c = new Run(proj, txtConsole);
			Thread t = new Thread(c, "compile");
			
			t.start();		
		}
		else {
			txtConsole.setText("" + errors);
		}
	}

	public Stage getStage() {
		return editorStage;
	}

	@FXML
	public void newFile() throws IOException {
		newFileFunc();
	}

	public void newFileFunc() throws IOException {
		Stage newFileStage = new Stage();
		new ControllerNewFile(newFileStage, user, this.editorStage);
		if(this.caretLine >= 0)	{//means no lock yet happened
			if(this.isEdited)
				saveFile();
			new ActionRequest().unlockLines(user, this.proj, 1);
		}
		newFileStage.show();
	}

	@FXML
	public void openFile() throws IOException {
		new ControllerOpenFile(user, this.editorStage, null).showStage();
		if(this.caretLine >= 0)	{//means no lock yet happened
			if(this.isEdited)
				saveFile();
			new ActionRequest().unlockLines(user, this.proj, 1);
		}
		new ActionRequest().loginProject(this.user, this.proj);
	}

	@FXML
	public void logout() throws IOException {
		Boolean isExit = ConfirmBox.display("Logout", "are you shure you want to logout?");
		if (isExit) {
			closeStage();

			new ControllerLogin().showStage();
		}
	}

	@FXML
	public void changePassword() throws IOException {
		ControllerPassword controllerPassword = new ControllerPassword(this, user);
		controllerPassword.showStage();
	}

	@FXML
	public void deleteProject() throws IOException {
		new ControllerDeleteProject(this, user).showStage();
		if (this.isDeleted) {
			closeStage();
			newFileFunc();
			this.isDeleted = false;
		}
	}

	@FXML
	public void help() throws IOException {
		new ControllerHelp().showStage();
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Project getProj() {
		return proj;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	
	public String checkErrorsEnter(String prefix, String postfix, int prefixLine) throws IOException {
		Project tempProj;
		String errors;
		
		tempProj = new Project(this.proj);
		tempProj.setText(prefixLine, prefix);
		tempProj.getLinesOfCode().add(prefixLine+1, new Line(postfix,-1));
		
		errors = compileProgram(tempProj.toString());
		System.out.println("in check errors enter = \n" + errors);
		
		if(errors == null)
			return prefix + "\n" + postfix; 
						
		return checkErrors(prefix, prefixLine,this.getProj().getLinesOfCode().get(prefixLine).getCode(), false) + "\n" + checkErrors(postfix, prefixLine," ", false);
	}
	
	public String checkErrorsDelete(String prefix, String postfix, int strLine,String restoreStr,boolean isDeleted) throws IOException {
		Project tempProj = new Project(this.proj);
		String errors;
			
		tempProj.setText(strLine, prefix);
		errors = compileProgram(tempProj.toString());
		if(errors == null)
			return prefix + postfix; 
		else
			txtConsole.setText(errors);
		
		//String check = checkIfBracketsError(errors,isDeleted);
		//if(check != null)
			//return str + "\n" + check;
		
		tempProj.setText(strLine,"//" + prefix + postfix);
		tempProj.removeLine(strLine+1);
		return checkSecondLayerError(tempProj, strLine, "//" + prefix, restoreStr);
	}
	
	/*
	 * when the user is trying to lock a line that already locked
	 * or backspace / delete flows to other locked line, notify the user and cancel the action
	 */
	public void errorMessage(String msg) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Warning Dialog");
		//alert.setHeaderText("Could not connect to Server!");
		alert.setContentText(msg);
		alert.showAndWait();
	}
	
	public String checkErrors(String str, int strLine,String restoreStr,boolean isDeleted) throws IOException {
		Project tempProj = new Project(this.proj);
		String errors;
			
		tempProj.setText(strLine, str);
		errors = compileProgram(tempProj.toString());
		System.out.println("in check errors = \n" + errors);
		if(errors == null)
			return str; 
		else
			txtConsole.setText(errors);
		
		//String check = checkIfBracketsError(errors,isDeleted);
		//if(check != null)
			//return str + "\n" + check;
		
		tempProj.setText(strLine,"//" + str);
		return checkSecondLayerError(tempProj, strLine, "//" + str, restoreStr);
	}
	
	public String checkSecondLayerError(Project firstLayerProj, int changedLine, String changes,String restoreStr) throws IOException {
		String errors;
				
		errors = compileProgram(firstLayerProj.toString()); 
		System.out.println("in second = \n" + errors);
		if (errors == null)
			return changes;
		
		changes = restoreStr + "\t // 2nd Layer Compilation ERROR, revert!";		
		return changes;
	}
	
	public String checkIfBracketsError(String errors, boolean isDeleted){
		System.out.println("errors = \n" + errors);
		String[] errorArr = errors.split("\n");
		System.out.println("check boolean = " + errorArr[0].contains("reached end of file while parsing"));
		System.out.println("the error is = " + errorArr[1].trim());
		if(errorArr[0].contains("reached end of file while parsing"))
			return errorArr[1].trim();
		
		return null;
	}
	
	public String checkTheLine(int strLine, KeyCode key) throws IOException {
		String str = "";
		String restoreStr = this.getProj().getLinesOfCode().get(strLine).getCode();
		switch (key){
		case ENTER: {
			str = codeArea.getText(strLine).concat(codeArea.getText(codeArea.getCurrentParagraph()));
			String prefix = str.substring(0,this.caretCol);
			String postfix = str.substring(this.caretCol);
			if(prefix.length() == 0) {
				//Beginning of the line
				return "\n" + checkErrors(postfix, this.caretLine,restoreStr,false);
			}
			else if(postfix.length() == 0) {
				//end of line
				return checkErrors(prefix, this.caretLine,restoreStr,false) + "\n" + " "; 
			}
			else {
				//middle of the line or empty line
				return checkErrorsEnter(prefix, postfix, this.caretLine); // TODO CHECK { }
			}			
			//TODO check if needed line = ""
		}
		case BACK_SPACE:{
			//TODO check beginning of file
			String prefix;
			String postfix;
			if(strLine < getNumberOfLines()) 
				//change
				str = codeArea.getText(codeArea.getCurrentParagraph());
			else
				str = codeArea.getText(codeArea.getCurrentParagraph()-1);
			
			prefix =  str.substring(0,this.codeArea.getCaretColumn());
			postfix = str.substring(codeArea.getCaretColumn());
			return prefix + " " + checkErrors(postfix, strLine,"\n" + restoreStr,true);
		}
		case DELETE:{
			String prefix;
			String postfix;
			str = codeArea.getText(codeArea.getCurrentParagraph());
			prefix =  str.substring(0,this.codeArea.getCaretColumn());
			postfix = str.substring(codeArea.getCaretColumn());
			return checkErrorsDelete(prefix, postfix,strLine, restoreStr + "\n" + postfix, true);
		}
		default:
			break;
		}
		
		return checkErrors(codeArea.getText(strLine),strLine,restoreStr, true);
	}
	
	public int getNumberOfLines() {
		return this.codeArea.getText().split("\n").length;
	}
	
	/*
	 * caret is changing line. save the proj and move the caret to the new location
	 */
	public void mouseClicked() {
		int col = codeArea.getCaretColumn();
		System.out.println("Line = " + codeArea.getCurrentParagraph() + ", caretLine = " + this.caretLine);
		if(codeArea.getCurrentParagraph() != this.caretLine) {
			int line = codeArea.getCurrentParagraph();
			if(isEdited) {
				String temp;
				try {
					temp = checkErrors(this.codeArea.getText(this.caretLine), this.caretLine,this.proj.getLinesOfCode().get(this.caretLine).getCode(),false);
					this.proj = new ActionRequest().editCode(user, proj, temp);
					//TODO fix caret jump to the end of line
					this.codeArea.clear();
					this.codeArea.replaceText(0, 0, this.proj.toString());
					getLabelFromString();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			//2) unlock the last line
			if(this.caretLine >= 0)	//means no lock yet happened
				new ActionRequest().unlockLines(user, this.proj, 1);
			//3) lock the new line
			//TODO lock on file
			if(new ActionRequest().lockLines(user, this.proj, line, 1)) {
				this.originalLine = this.codeArea.getText(line);
				this.caretLine = line;
			}
			else {
				new ActionRequest().lockLines(user, this.proj, this.caretLine, 1);
				errorMessage("the line you are trying to reach is locked");
				col = this.caretCol;
			}
		}
		this.caretCol = col;
		this.codeArea.moveTo(this.caretLine,this.caretCol);
		this.beforeChange = this.editCode.getText(this.caretLine);
	}
	
	/*
	 * key pressed handler
	 * 2 cases:
	 * 1) ctrl+s. save on place with no locks.
	 * 2) caret changed line. save with new locks.
	 */
	public void keyPressed(KeyEvent e) {
		int col = this.codeArea.getCaretColumn();
		
		//char ch = e.getText().charAt(0);
		if(e.getText().length() > 0)
			col++;
		isEdited = true;
		//TODO add flag for change!
		/*
		 * if user saved the file (ctrl + s)
		 * 1) update the current line
		 */
		if(e.getCode() == KeyCode.S && e.isControlDown()) {
			//int col = this.codeArea.getCaretColumn();
			try {
				String temp = checkErrors(this.codeArea.getText(this.caretLine), this.caretLine,this.proj.getLinesOfCode().get(this.caretLine).getCode(),false);
				this.caretLine = codeArea.getCurrentParagraph();
				this.caretCol = codeArea.getCaretColumn();
				this.proj = new ActionRequest().editCode(user, proj, temp);
				this.codeArea.clear();
				this.codeArea.replaceText(0, 0, this.proj.toString());
				this.codeArea.moveTo(this.caretLine, this.caretCol);
				getLabelFromString();
				//TODO lock!
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			isEdited = false;
			this.beforeChange = this.editCode.getText(this.caretLine);
		}
		

		/*
		 * if the line number was changed 
		 * 1) need to update the db		TODO
		 * 2) unlock the last line		TODO
		 * 3) lock the new line and wait for another update		TODO 
		 */
		else if(codeArea.getCurrentParagraph() != this.caretLine 
				|| getNumberOfLines() < proj.getLinesOfCode().size()){	//the line was changed
			//System.out.println(checkTheLine(this.codeArea.getText(this.caretLine), e.getCode()));
			//TODO check compilation
			String temp;
			//int col = this.codeArea.getCaretColumn();
			int line = this.codeArea.getCurrentParagraph();
			try {
				temp = checkTheLine(this.caretLine, e.getCode());
				
				List<Object> tempList;
				String error;
				
				tempList = new ActionRequest().editCodeWithLocks(user, proj, temp, e.getCode().toString(), this.beforeChange);
				error = (String) tempList.get(0);		//First value is error
				this.proj = (Project) tempList.get(1);	//Second value is the project
				this.codeArea.clear();
				this.codeArea.replaceText(0, 0, this.proj.toString());
				
				if(!error.equals("")) {
					line = this.caretLine;
					col = this.caretCol;
					errorMessage(error);
				}
				
				this.codeArea.moveTo(line,col);
				this.caretLine = line;
				getLabelFromString();
				//2) unlock the last line
				/*if(this.caretLine < getNumberOfLines())
					new ActionRequest().unlockLines(user, this.proj, 1);
				//3) lock the new line
				this.caretLine = line;
				//this.caretCol = col;
				//TODO lock on file
				if(this.caretLine == getNumberOfLines()) 
					this.caretLine--;
				if(new ActionRequest().lockLines(user, this.proj, this.caretLine, 1)) 
					this.originalLine = this.codeArea.getText(this.caretLine);*/ //TODO check
			} catch (IOException err) {
				err.printStackTrace();
			}
			isEdited = false;
		}
		this.caretCol = col;
		if(this.caretLine < getNumberOfLines()) // IF THE LINE EXISTS
			this.beforeChange = this.codeArea.getText(this.caretLine);
	}
}
