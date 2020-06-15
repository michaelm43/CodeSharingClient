package GUI;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;


import Logic.Line;
import Logic.Project;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

public class ProjectWithFields {
	
	private Project proj;
	private List<TextField> editor;
	private VBox box;
	
	
	public ProjectWithFields(Project proj) {
		this.proj = proj;
		editor = new LinkedList<TextField>();
		setEditor();
	}

	public List<TextField> getEditor(){
		return editor;
	}

	public void setEditor() {
		ListIterator<Line> itr = proj.getLinesOfCode().listIterator();
		
		while(itr.hasNext()) {
			editor.add(new TextField(itr.next().getCode()));
		}
	}
	
	public Project getProject() {
		return proj;
	}
	
	public void setProject(Project proj) {
		this.proj = proj;
	}
	
	public VBox putInGui(VBox box) {
		ListIterator<TextField> itr = this.editor.listIterator();
		TextField temp;
		
		while(itr.hasNext()) {
			temp = itr.next();			
			temp = eventHandler(temp);
			box.getChildren().add(temp);
		}
		
		return box;
	}

	private TextField eventHandler(TextField temp) {
		temp.setOnKeyPressed(new EventHandler<KeyEvent>() {

			public void handle(KeyEvent e) {
				/*
				 * if need to check more keys pressed
				 */
				//System.out.println(e.getCode().toString());
				switch(e.getCode().toString()){
				case "ENTER":
					break;
				case "BACK_SPACE":
					//TODO
					break;
				case "DELETE":
					//TODO
					break;
				case "RIGHT":
					//TODO
					break;
				case "LEFT":
					//TODO
					break;
				case "UP":
					//TODO
					break;
				case "DOWN":
					//TODO
					editor.get(editor.indexOf(e.getSource())+1).requestFocus();
					break;
				}
				
			}
			
		});
		
		return temp;
		
	}
	
//	temp.setOnAction(e->{
//		System.out.println(e.getEventType().getName());
//		switch(e.getEventType().getName()) {
//			case 
//		}
//	});
}
