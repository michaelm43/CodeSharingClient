package GUI;

import java.io.IOException;

import Logic.*;
import javafx.application.Platform;
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

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.reactfx.Subscription;

public class EditorController {

	public static String EXIT_TITLE = "Exit Message";
	public static String EXIT_MESSAGE = "Are you sure you want to exit the program?";
	public static String LOGOUT_TITLE = "Logout Message";
	public static String LOGOUT_MESSAGE = "Are you sure you want to logout?";
	
	 private static final String[] KEYWORDS = new String[] {
	         "abstract", "assert", "boolean", "break", "byte",
	         "case", "catch", "char", "class", "const",
	         "continue", "default", "do", "double", "else",
	         "enum", "extends", "final", "finally", "float",
	         "for", "goto", "if", "implements", "import",
	         "instanceof", "int", "interface", "long", "native",
	         "new", "package", "private", "protected", "public",
	         "return", "short", "static", "strictfp", "super",
	         "switch", "synchronized", "this", "throw", "throws",
	         "transient", "try", "void", "volatile", "while"
	 };
	 
	 private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
	 private static final String PAREN_PATTERN = "\\(|\\)";
	 private static final String BRACE_PATTERN = "\\{|\\}";
	 private static final String BRACKET_PATTERN = "\\[|\\]";
	 private static final String SEMICOLON_PATTERN = "\\;";
	 private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
	 private static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";

	 private static final Pattern PATTERN = Pattern.compile(
	         "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
	         + "|(?<PAREN>" + PAREN_PATTERN + ")"
	         + "|(?<BRACE>" + BRACE_PATTERN + ")"
	         + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
	         + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
	         + "|(?<STRING>" + STRING_PATTERN + ")"
	         + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
	 );
	    
	 private static final String sampleCode = String.join("\n", new String[] {
	         "package com.example;",
	         "",
	         "import java.util.*;",
	         "",
	         "public class Foo extends Bar implements Baz {",
	         "",
	         "    /*",
	         "     * multi-line comment",
	         "     */",
	         "    public static void main(String[] args) {",
	         "        // single-line comment",
	         "        for(String arg: args) {",
	         "            if(arg.length() != 0)",
	         "                System.out.println(arg);",
	         "            else",
	         "                System.err.println(\"Warning: empty string as argument\");",
	         "        }",
	         "    }",
	         "",
	         "}"
	     });


	private final Stage editorStage;

	private User user;
	private Project proj;
	
	private CodeArea codeArea;

	@FXML
	private TextArea txtConsole;

	@FXML
	private BorderPane borderPane;

	private int flag;
	
	private int caretpos;
	private int caretLine;
	
	
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
			
			initCodeArea();
			borderPane.setCenter(new VirtualizedScrollPane<>(codeArea));

		} catch (IOException e) {
			e.printStackTrace();
		}
		    

		editorStage.setOnCloseRequest(e -> {
			e.consume();
			popUpMessage(EXIT_TITLE, EXIT_MESSAGE);
		});

		Runtime rt = Runtime.getRuntime();
		Shutdown sd = new Shutdown();
		rt.addShutdownHook(new Thread(sd));
		flag = 0;
	}

	private void initCodeArea() {
		
		//add line numbers to the left of code area
		codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
				
		// recompute the syntax highlighting 500 ms after user stops editing
		Subscription cleanupWhenNoLongerNeedIt  = codeArea
				// plain changes = ignore style changes that are emitted when syntax highlighting is reapplied
                // multi plain changes = save computation by not rerunning the code multiple times
                //   when making multiple changes (e.g. renaming a method at multiple parts in file)
				.multiPlainChanges()
				// do not emit an event until 500 ms have passed since the last emission of previous stream
				.successionEnds(Duration.ofMillis(500))
				// run the following code block when previous stream emits an event
				.subscribe(ignore -> codeArea.setStyleSpans(0, computeHighlighting(codeArea.getText())));
		
		// when no longer need syntax highlighting and wish to clean up memory leaks
        // run: `cleanupWhenNoLongerNeedIt.unsubscribe();`
		
		 
		// auto-indent: insert previous line's indents on enter
        final Pattern whiteSpace = Pattern.compile( "^\\s+" );
        codeArea.addEventHandler( KeyEvent.KEY_PRESSED, KE ->
        {
            if ( KE.getCode() == KeyCode.ENTER ) {
            	int caretPosition = codeArea.getCaretPosition();
            	int currentParagraph = codeArea.getCurrentParagraph();
                Matcher m0 = whiteSpace.matcher( codeArea.getParagraph( currentParagraph-1 ).getSegments().get( 0 ) );
                if ( m0.find() ) Platform.runLater( () -> codeArea.insertText( caretPosition, m0.group() ) );
            }
        });
        
        
        codeArea.replaceText(0, 0, sampleCode);  
        
        codeArea.setOnKeyPressed(e->{
        	startLock();
        });
	}
	
	private void startLock() {
		//get the caret line.
		codeArea.getCurrentParagraph();
		//TODO invoke action to lock lines in db!
	}

	private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while(matcher.find()) {
            String styleClass =
                    matcher.group("KEYWORD") != null ? "keyword" :
                    matcher.group("PAREN") != null ? "paren" :
                    matcher.group("BRACE") != null ? "brace" :
                    matcher.group("BRACKET") != null ? "bracket" :
                    matcher.group("SEMICOLON") != null ? "semicolon" :
                    matcher.group("STRING") != null ? "string" :
                    matcher.group("COMMENT") != null ? "comment" :
                    null; /* never happens */ assert styleClass != null;
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
